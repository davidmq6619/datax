package com.sinohealth.datax.plugin.rdbms.writer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.plugin.RecordReceiver;
import com.alibaba.datax.common.plugin.TaskPluginCollector;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.rdbms.util.*;
import com.alibaba.datax.plugin.rdbms.writer.Constant;
import com.alibaba.datax.plugin.rdbms.writer.Key;
import com.alibaba.datax.plugin.rdbms.writer.util.OriginalConfPretreatmentUtil;
import com.alibaba.datax.plugin.rdbms.writer.util.WriterUtil;
import com.google.common.collect.Maps;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.common.*;
import com.sinohealth.datax.entity.source.BasTestItemTemp;
import com.sinohealth.datax.plugin.rdbms.init.BizDataIniter;
import com.sinohealth.datax.reflection.Reflector;
import com.sinohealth.datax.reflection.factory.DefaultObjectFactory;
import com.sinohealth.datax.reflection.factory.ObjectFactory;
import com.sinohealth.datax.utils.DBHelper;
import com.sinohealth.datax.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SinohealthRdbmsWriter {

    public static class Job {
        public DataBaseType dataBaseType;

        public static final Logger LOG = LoggerFactory
                .getLogger(Job.class);

        public Job(DataBaseType dataBaseType) {
            this.dataBaseType = dataBaseType;
            OriginalConfPretreatmentUtil.DATABASE_TYPE = this.dataBaseType;
        }

        public void init(Configuration originalConfig) {
            OriginalConfPretreatmentUtil.doPretreatment(originalConfig, this.dataBaseType);
            LOG.debug("After job init(), originalConfig now is:[\n{}\n]",
                    originalConfig.toJSON());
        }

        /*目前只支持MySQL Writer跟Oracle Writer;检查PreSQL跟PostSQL语法以及insert，delete权限*/
        public void writerPreCheck(Configuration originalConfig, DataBaseType dataBaseType) {
            /*检查PreSql跟PostSql语句*/
            prePostSqlValid(originalConfig, dataBaseType);
            /*检查insert 跟delete权限*/
            privilegeValid(originalConfig, dataBaseType);
        }

        public void prePostSqlValid(Configuration originalConfig, DataBaseType dataBaseType) {
            /*检查PreSql跟PostSql语句*/
            WriterUtil.preCheckPrePareSQL(originalConfig, dataBaseType);
            WriterUtil.preCheckPostSQL(originalConfig, dataBaseType);
        }

        public void privilegeValid(Configuration originalConfig, DataBaseType dataBaseType) {
            /*检查insert 跟delete权限*/
            String username = originalConfig.getString(Key.USERNAME);
            String password = originalConfig.getString(Key.PASSWORD);
            List<Object> connections = originalConfig.getList(Constant.CONN_MARK,
                    Object.class);

            for (int i = 0, len = connections.size(); i < len; i++) {
                Configuration connConf = Configuration.from(connections.get(i).toString());
                String jdbcUrl = connConf.getString(Key.JDBC_URL);
                List<String> expandedTables = connConf.getList(Key.TABLE, String.class);
                boolean hasInsertPri = DBUtil.checkInsertPrivilege(dataBaseType, jdbcUrl, username, password, expandedTables);

                if (!hasInsertPri) {
                    throw RdbmsException.asInsertPriException(dataBaseType, originalConfig.getString(Key.USERNAME), jdbcUrl);
                }

                if (DBUtil.needCheckDeletePrivilege(originalConfig)) {
                    boolean hasDeletePri = DBUtil.checkDeletePrivilege(dataBaseType, jdbcUrl, username, password, expandedTables);
                    if (!hasDeletePri) {
                        throw RdbmsException.asDeletePriException(dataBaseType, originalConfig.getString(Key.USERNAME), jdbcUrl);
                    }
                }
            }
        }

        // 一般来说，是需要推迟到 task 中进行pre 的执行（单表情况例外）
        public void prepare(Configuration originalConfig) {
            int tableNumber = originalConfig.getInt(Constant.TABLE_NUMBER_MARK);
            if (tableNumber == 1) {
                String username = originalConfig.getString(Key.USERNAME);
                String password = originalConfig.getString(Key.PASSWORD);

                List<Object> conns = originalConfig.getList(Constant.CONN_MARK,
                        Object.class);
                Configuration connConf = Configuration.from(conns.get(0)
                        .toString());

                // 这里的 jdbcUrl 已经 append 了合适后缀参数
                String jdbcUrl = connConf.getString(Key.JDBC_URL);
                originalConfig.set(Key.JDBC_URL, jdbcUrl);

                String table = connConf.getList(Key.TABLE, String.class).get(0);
                originalConfig.set(Key.TABLE, table);

                List<String> preSqls = originalConfig.getList(Key.PRE_SQL,
                        String.class);
                List<String> renderedPreSqls = WriterUtil.renderPreOrPostSqls(
                        preSqls, table);

                originalConfig.remove(Constant.CONN_MARK);
                if (null != renderedPreSqls && !renderedPreSqls.isEmpty()) {
                    // 说明有 preSql 配置，则此处删除掉
                    originalConfig.remove(Key.PRE_SQL);
                    Connection conn = DBUtil.getConnection(dataBaseType, jdbcUrl, username, password);
                    LOG.info("Begin to execute preSqls:[{}]. context info:{}.", StringUtils.join(renderedPreSqls, ";"), jdbcUrl);
                    WriterUtil.executeSqls(conn, renderedPreSqls, jdbcUrl, dataBaseType);
                    DBUtil.closeDBResources(null, null, conn);
                }
            }

            LOG.debug("After job prepare(), originalConfig now is:[\n{}\n]",
                    originalConfig.toJSON());
        }

        public List<Configuration> split(Configuration originalConfig,
                                         int mandatoryNumber) {
            return WriterUtil.doSplit(originalConfig, mandatoryNumber);
        }

        // 一般来说，是需要推迟到 task 中进行post 的执行（单表情况例外）
        public void post(Configuration originalConfig) {
            int tableNumber = originalConfig.getInt(Constant.TABLE_NUMBER_MARK);
            if (tableNumber == 1) {
                String username = originalConfig.getString(Key.USERNAME);
                String password = originalConfig.getString(Key.PASSWORD);

                // 已经由 prepare 进行了appendJDBCSuffix处理
                String jdbcUrl = originalConfig.getString(Key.JDBC_URL);

                String table = originalConfig.getString(Key.TABLE);

                List<String> postSqls = originalConfig.getList(Key.POST_SQL,
                        String.class);
                List<String> renderedPostSqls = WriterUtil.renderPreOrPostSqls(
                        postSqls, table);

                if (null != renderedPostSqls && !renderedPostSqls.isEmpty()) {
                    // 说明有 postSql 配置，则此处删除掉
                    originalConfig.remove(Key.POST_SQL);

                    Connection conn = DBUtil.getConnection(this.dataBaseType,
                            jdbcUrl, username, password);

                    LOG.info(
                            "Begin to execute postSqls:[{}]. context info:{}.",
                            StringUtils.join(renderedPostSqls, ";"), jdbcUrl);
                    WriterUtil.executeSqls(conn, renderedPostSqls, jdbcUrl, dataBaseType);
                    DBUtil.closeDBResources(null, null, conn);
                }
            }
        }

        public void destroy(Configuration originalConfig) {
        }

    }

    public static class Task {
        protected static final Logger LOG = LoggerFactory
                .getLogger(Task.class);

        protected DataBaseType dataBaseType;
        public static final String VALUE_HOLDER = "?";

        protected String username;
        protected String password;
        protected String jdbcUrl;
        protected String table;
        protected List<String> columns;
        protected List<String> preSqls;
        protected List<String> postSqls;
        protected int batchSize;
        protected int batchByteSize;
        protected int columnNumber = 0;
        protected TaskPluginCollector taskPluginCollector;
        protected Class<?> sourceClass;
        protected Class<?> targetClass;
        protected Processor processor;
        protected String initDataDbUrl;
        protected String initDataDbUser;
        protected String initDataDbPassword;

        // 作为日志显示信息时，需要附带的通用信息。比如信息所对应的数据库连接等信息，针对哪个表做的操作
        protected static String BASIC_MESSAGE;

        protected static String INSERT_OR_REPLACE_TEMPLATE;

        protected String writeRecordSql;
        protected String writeMode;
        protected boolean emptyAsNull;
        protected Triple<List<String>, List<Integer>, List<String>> resultSetMetaData;

        protected CommonData commonData;
        protected DBConnectionPool pool;

        public Task(DataBaseType dataBaseType) {
            this.dataBaseType = dataBaseType;
        }

        public void init(Configuration writerSliceConfig) {
            this.username = writerSliceConfig.getString(Key.USERNAME);
            this.password = writerSliceConfig.getString(Key.PASSWORD);
            this.jdbcUrl = writerSliceConfig.getString(Key.JDBC_URL);

            this.initDataDbUrl = writerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.INIT_DATA_DB_URL);
            this.initDataDbUser = writerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.INIT_DATA_DB_USER);
            this.initDataDbPassword = writerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.INIT_DATA_DB_PASSWORD);
            if (StrUtil.isBlank(initDataDbUrl)) {
                this.initDataDbUrl = jdbcUrl;
            }
            if (StrUtil.isBlank(initDataDbUser)) {
                this.initDataDbUser = username;
            }
            if (StrUtil.isBlank(initDataDbPassword)) {
                this.initDataDbPassword = password;
            }

            LOG.info("initDataDbUrl:[{}]", initDataDbUrl);
            LOG.info("initDataDbUser:[{}]", initDataDbUser);
            LOG.info("initDataDbPassword:[{}]", initDataDbPassword);

            try {
                LOG.info("sourceClass:[{}]", writerSliceConfig.getString(Key.SOURCE_CLASS));
                LOG.info("targetClass:[{}]", writerSliceConfig.getString(Key.TARGET_CLASS));
                LOG.info("processClass:[{}]", writerSliceConfig.getString(Key.PROCESSOR_CLASS));
                LOG.info("清洗数据准备");
                this.sourceClass = Class.forName("com.sinohealth.datax.entity.source." + writerSliceConfig.getString(Key.SOURCE_CLASS));
                this.targetClass = Class.forName("com.sinohealth.datax.entity.zktarget." + writerSliceConfig.getString(Key.TARGET_CLASS));
                this.processor = (Processor) new DefaultObjectFactory().create(Class.forName("com.sinohealth.datax.processors." + writerSliceConfig.getString(Key.PROCESSOR_CLASS)));
            } catch (ClassNotFoundException ex) {
                LOG.error("sourceClass/targetClass/processor class not found", ex);
            }

            //ob10的处理
            if (this.jdbcUrl.startsWith(Constant.OB10_SPLIT_STRING) && this.dataBaseType == DataBaseType.MySql) {
                String[] ss = this.jdbcUrl.split(Constant.OB10_SPLIT_STRING_PATTERN);
                if (ss.length != 3) {
                    throw DataXException
                            .asDataXException(
                                    DBUtilErrorCode.JDBC_OB10_ADDRESS_ERROR, "JDBC OB10格式错误，请联系askdatax");
                }
                LOG.info("this is ob1_0 jdbc url.");
                this.username = ss[1].trim() + ":" + this.username;
                this.jdbcUrl = ss[2];
                LOG.info("this is ob1_0 jdbc url. user=" + this.username + " :url=" + this.jdbcUrl);
            }

            this.table = writerSliceConfig.getString(Key.TABLE);
            if (StringUtils.isBlank(this.table)) {
                this.table = Reflector.forClass(targetClass).getTableName();
            }
            this.columns = writerSliceConfig.getList(Key.COLUMN, String.class);
            if (CollectionUtil.isEmpty(columns)) {
                // 如果没有配置column,那么就通过反射获取字段
                Reflector reflector = Reflector.forClass(targetClass);
                this.columns = reflector.getAllColumns();
            }
            this.columnNumber = this.columns.size();

            this.preSqls = writerSliceConfig.getList(Key.PRE_SQL, String.class);
            this.postSqls = writerSliceConfig.getList(Key.POST_SQL, String.class);
            this.batchSize = writerSliceConfig.getInt(Key.BATCH_SIZE, Constant.DEFAULT_BATCH_SIZE);
            this.batchByteSize = writerSliceConfig.getInt(Key.BATCH_BYTE_SIZE, Constant.DEFAULT_BATCH_BYTE_SIZE);

            writeMode = writerSliceConfig.getString(Key.WRITE_MODE, "INSERT");
            emptyAsNull = writerSliceConfig.getBool(Key.EMPTY_AS_NULL, true);
            INSERT_OR_REPLACE_TEMPLATE = writerSliceConfig.getString(Constant.INSERT_OR_REPLACE_TEMPLATE_MARK);
            this.writeRecordSql = String.format(INSERT_OR_REPLACE_TEMPLATE, this.table);

            LOG.info("Init biz cache begin......");
            long begin = System.currentTimeMillis();
            // 标准化緩存的初始化
            BizDataIniter bizDataIniter = new BizDataIniter(DataBaseType.MySql, initDataDbUrl, initDataDbUser, initDataDbPassword);
            // 加载检验标准化字典
            Map<String, StandardBasTestItem> testItemMap = bizDataIniter.findBasTestItemMap();
            // 加载检验单位标准化字典
            Map<String, String> testUnitMap = bizDataIniter.findBasTestUnitMap();
            // 加载检验结果离散化字典
            //Map<String, List<BasTestResultDiscrete>> testResultDiscreteMap = bizDataIniter.findTestResultDiscreteMap();
            // 加载检查项字典
            Map<String, BasCheckItem> checkItemMap = bizDataIniter.findCheckItemList();
            // 加载诊断标准化字典


            // 加载离散类型为2的字典
            Map<String, String> resultMap = Maps.newHashMap();
            resultMap.put("-", "-1");
            resultMap.put("阴", "-1");
            resultMap.put("阴性", "-1");
            resultMap.put("弱阳性", "1");
            resultMap.put("弱阳", "1");
            resultMap.put("+-", "1");
            resultMap.put("-+", "1");
            resultMap.put("±", "1");
            resultMap.put("+", "2");
            resultMap.put("阳", "2");
            resultMap.put("阳性", "2");

            this.commonData = new CommonData();
            this.commonData.setBasTestItemMap(testItemMap);
            this.commonData.setBasTestUnitMap(testUnitMap);
            //this.commonData.setLisTestResultDiscreteMap(testResultDiscreteMap);
            this.commonData.setResultDiscreteMap(resultMap);
            this.commonData.setBasCheckItemMap(checkItemMap);


            // init pool
            this.pool = DBConnectionPool.getInstance(jdbcUrl, username, password);

            LOG.info("Init biz cache end. Spend time :[{}] ms", (System.currentTimeMillis() - begin));

            BASIC_MESSAGE = String.format("jdbcUrl:[%s], table:[%s]",
                    this.jdbcUrl, this.table);
        }

        public void prepare(Configuration writerSliceConfig) {
            Connection connection = DBUtil.getConnection(this.dataBaseType,
                    this.jdbcUrl, username, password);

            DBUtil.dealWithSessionConfig(connection, writerSliceConfig,
                    this.dataBaseType, BASIC_MESSAGE);

            int tableNumber = writerSliceConfig.getInt(
                    Constant.TABLE_NUMBER_MARK);
            if (tableNumber != 1) {
                LOG.info("Begin to execute preSqls:[{}]. context info:{}.",
                        StringUtils.join(this.preSqls, ";"), BASIC_MESSAGE);
                WriterUtil.executeSqls(connection, this.preSqls, BASIC_MESSAGE, dataBaseType);
            }

            DBUtil.closeDBResources(null, null, connection);
        }

        public void startWriteWithConnection(RecordReceiver recordReceiver, TaskPluginCollector taskPluginCollector, Connection connection, Class source, Class target, Processor processor) {
            this.taskPluginCollector = taskPluginCollector;

            // 用于写入数据的时候的类型根据目的表字段类型转换
            this.resultSetMetaData = DBUtil.getColumnMetaData(connection,
                    this.table, StringUtils.join(this.columns, ","));
            // 写数据库的SQL语句
            calcWriteRecordSql();

            List<Record> writeBuffer = new ArrayList<Record>(this.batchSize);
            int bufferBytes = 0;
            try {
                Record record;
                while ((record = recordReceiver.getFromReader()) != null) {
                    writeBuffer.add(record);
                    bufferBytes += record.getMemorySize();

                    if (writeBuffer.size() >= batchSize || bufferBytes >= batchByteSize) {
                        doBatchInsert(writeBuffer, source, target, processor);
                        writeBuffer.clear();
                        bufferBytes = 0;
                    }
                }
                if (!writeBuffer.isEmpty()) {
                    doBatchInsert(writeBuffer, source, target, processor);
                    writeBuffer.clear();
                    bufferBytes = 0;
                }
            } catch (Exception e) {
                throw DataXException.asDataXException(
                        DBUtilErrorCode.WRITE_DATA_ERROR, e);
            } finally {
                writeBuffer.clear();
                bufferBytes = 0;
                DBUtil.closeDBResources(null, null, connection);
            }
        }

        public void startWrite(RecordReceiver recordReceiver,
                               Configuration writerSliceConfig,
                               TaskPluginCollector taskPluginCollector) {
            Connection connection = DBUtil.getConnection(this.dataBaseType,
                    this.jdbcUrl, username, password);
            DBUtil.dealWithSessionConfig(connection, writerSliceConfig,
                    this.dataBaseType, BASIC_MESSAGE);
            LOG.info("Begin write...");
            long begin = System.currentTimeMillis();
            startWriteWithConnection(recordReceiver, taskPluginCollector, connection,
                    sourceClass, targetClass, processor);
            long end = System.currentTimeMillis();
            LOG.info("End write, Time spend [{}] ms", end - begin);
        }


        public void post(Configuration writerSliceConfig) {
            int tableNumber = writerSliceConfig.getInt(
                    Constant.TABLE_NUMBER_MARK);

            boolean hasPostSql = (this.postSqls != null && this.postSqls.size() > 0);
            if (tableNumber == 1 || !hasPostSql) {
                return;
            }

            Connection connection = DBUtil.getConnection(this.dataBaseType,
                    this.jdbcUrl, username, password);

            LOG.info("Begin to execute postSqls:[{}]. context info:{}.",
                    StringUtils.join(this.postSqls, ";"), BASIC_MESSAGE);
            WriterUtil.executeSqls(connection, this.postSqls, BASIC_MESSAGE, dataBaseType);
            DBUtil.closeDBResources(null, null, connection);
        }

        public void destroy(Configuration writerSliceConfig) {
        }

        protected void doBatchInsert(List<Record> buffer, Class source, Class target, Processor processor)
                throws SQLException {
            Connection connection = this.pool.getConnection();
            PreparedStatement preparedStatement = null;
            try {
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(this.writeRecordSql);
                for (Record record : buffer) {
                    preparedStatement = fillPreparedStatement(preparedStatement, record, source, target, processor);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                LOG.warn("回滚此次写入, 采用每次写入一行方式提交. 因为:" + e.getMessage());
                connection.rollback();
                doOneInsert(connection, buffer, source, target, processor);
            } catch (Exception e) {
                throw DataXException.asDataXException(
                        DBUtilErrorCode.WRITE_DATA_ERROR, e);
            } finally {
                DBUtil.closeDBResources(preparedStatement, connection);
            }
        }

        protected void doOneInsert(Connection connection, List<Record> buffer, Class source, Class target, Processor processor) {
            PreparedStatement preparedStatement = null;
            try {
                connection.setAutoCommit(true);
                preparedStatement = connection
                        .prepareStatement(this.writeRecordSql);

                for (Record record : buffer) {
                    try {
                        preparedStatement = fillPreparedStatement(
                                preparedStatement, record, source, target, processor);
                        preparedStatement.execute();
                    } catch (SQLException e) {
                        LOG.debug(e.toString());

                        this.taskPluginCollector.collectDirtyRecord(record, e);
                    } finally {
                        // 最后不要忘了关闭 preparedStatement
                        preparedStatement.clearParameters();
                    }
                }
            } catch (Exception e) {
                throw DataXException.asDataXException(
                        DBUtilErrorCode.WRITE_DATA_ERROR, e);
            } finally {
                DBUtil.closeDBResources(preparedStatement, connection);
            }
        }

        // 直接使用了两个类变量：columnNumber,resultSetMetaData
        protected PreparedStatement fillPreparedStatement(PreparedStatement preparedStatement, Record record, Class<?> source,
                                                          Class<?> target, Processor processor) {
            // record对象转换源pojo对象
            Object sourceEntity = ReflectUtil.transform(source, record);
            ObjectFactory objectFactory = new DefaultObjectFactory();
            Object targetEntity = objectFactory.create(target);
            List<BasTestItemTemp> basTestItemList = null;
            try {
                String sql = "select  * from user_report_info where member_id='"+sourceEntity+"'; ";
                Connection connection = this.pool.getConnection();
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
                basTestItemList = DBHelper.parseResultList(BasTestItemTemp.class, rs);
                connection.commit();
            }catch (Exception e){
            }

            //targetEntity = processor.dataProcess(sourceEntity, targetEntity, this.commonData);
            //清洗并入库
            if (sourceEntity != null) {
                targetEntity = processor.dataProcess(sourceEntity, targetEntity, this.commonData);
            }
            fillPreparedStatementColumn(preparedStatement, targetEntity);

            return preparedStatement;
        }

        public PreparedStatement fillPreparedStatementColumn(PreparedStatement preparedStatement, Object targetEntity) {
            for (int i = 0; i < this.columnNumber; i++) {
                try {
                    String columnName = this.resultSetMetaData.getLeft().get(i);
                    Reflector reflector = Reflector.forClass(targetEntity.getClass());
                    String fieldName = reflector.getFieldName(columnName);
                    Object value = reflector.getGetInvoker(fieldName).invoke(targetEntity, null);
                    int columnSqlType = this.resultSetMetaData.getMiddle().get(i);
                    preparedStatement = fillPreparedStatementDetail(preparedStatement, i, columnSqlType, value);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return preparedStatement;
        }

        protected PreparedStatement fillPreparedStatementDetail(PreparedStatement preparedStatement, int columnIndex,
                                                                int columnSqlType, Object value) throws SQLException {
            java.util.Date date;
            switch (columnSqlType) {
                case Types.CHAR:
                case Types.NCHAR:
                case Types.CLOB:
                case Types.NCLOB:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                case Types.NVARCHAR:
                case Types.LONGNVARCHAR:
                    if (value != null) {
                        preparedStatement.setString(columnIndex + 1, value.toString());
                    } else {
                        preparedStatement.setString(columnIndex + 1, null);
                    }
                    break;
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.BIGINT:
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.FLOAT:
                case Types.REAL:
                case Types.DOUBLE:
                    String strValue = null;
                    if (value != null) {
                        strValue = value.toString();
                    }
                    if (emptyAsNull && StringUtils.isBlank(strValue)) {
                        preparedStatement.setString(columnIndex + 1, null);
                    } else {
                        preparedStatement.setString(columnIndex + 1, strValue);
                    }
                    break;

                //tinyint is a little special in some database like mysql {boolean->tinyint(1)}
                case Types.TINYINT:
                    if (null == value) {
                        preparedStatement.setString(columnIndex + 1, null);
                    } else {

                        preparedStatement.setString(columnIndex + 1, value.toString());
                    }
                    break;
                // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
                case Types.DATE:
                    if (this.resultSetMetaData.getRight().get(columnIndex)
                            .equalsIgnoreCase("year")) {
                        // todo

                    } else {
                        Date sqlDate = null;
                        try {
                            if (value == null) {
                                preparedStatement.setDate(columnIndex + 1, null);
                                break;
                            }
                            date = (java.util.Date) value;
                        } catch (DataXException e) {
                            throw new SQLException(String.format(
                                    "Date 类型转换错误：[%s]", value));
                        }

                        if (null != date) {
                            sqlDate = new Date(date.getTime());
                        }
                        preparedStatement.setDate(columnIndex + 1, sqlDate);
                    }

                    break;

                case Types.TIME:
                    Time sqlTime = null;
                    try {
                        if (value == null) {
                            preparedStatement.setDate(columnIndex + 1, null);
                            break;
                        }
                        date = (java.util.Date) value;
                    } catch (Exception e) {
                        throw new SQLException(String.format(
                                "TIME 类型转换错误：[%s]", value));
                    }

                    if (null != date) {
                        sqlTime = new Time(date.getTime());
                    }
                    preparedStatement.setTime(columnIndex + 1, sqlTime);
                    break;

                case Types.TIMESTAMP:
                    Timestamp sqlTimestamp = null;
                    if (value == null) {
                        preparedStatement.setDate(columnIndex + 1, null);
                        break;
                    }
                    try {
                        date = (java.util.Date) value;
                    } catch (Exception e) {
                        throw new SQLException(String.format(
                                "TIME 类型转换错误：[%s]", value));
                    }

                    if (null != date) {
                        sqlTimestamp = new Timestamp(
                                date.getTime());
                    }
                    preparedStatement.setTimestamp(columnIndex + 1, sqlTimestamp);
                    break;

                case Types.BINARY:
                case Types.VARBINARY:
                case Types.BLOB:
                case Types.LONGVARBINARY:
                    if (value != null) {
                        preparedStatement.setBytes(columnIndex + 1, (byte[]) value);
                    } else {
                        preparedStatement.setBytes(columnIndex + 1, null);
                    }
                    break;

                case Types.BOOLEAN:
                    if (value != null) {
                        preparedStatement.setString(columnIndex + 1, value.toString());
                    } else {
                        preparedStatement.setString(columnIndex + 1, null);
                    }
                    break;
                // warn: bit(1) -> Types.BIT 可使用setBoolean
                // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
                case Types.BIT:
                    if (value == null) {
                        preparedStatement.setString(columnIndex + 1, null);
                    }
                    if (this.dataBaseType == DataBaseType.MySql) {
                        preparedStatement.setBoolean(columnIndex + 1, (Boolean) value);
                    } else {
                        preparedStatement.setString(columnIndex + 1, (String) value);
                    }
                    break;
                default:
                    throw DataXException
                            .asDataXException(
                                    DBUtilErrorCode.UNSUPPORTED_TYPE,
                                    String.format(
                                            "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库写入这种字段类型. 字段名:[%s], 字段类型:[%d], 字段Java类型:[%s]. 请修改表中该字段的类型或者不同步该字段.",
                                            this.resultSetMetaData.getLeft()
                                                    .get(columnIndex),
                                            this.resultSetMetaData.getMiddle()
                                                    .get(columnIndex),
                                            this.resultSetMetaData.getRight()
                                                    .get(columnIndex)));
            }

            return preparedStatement;
        }

        public void calcWriteRecordSql() {
            if (!VALUE_HOLDER.equals(calcValueHolder(""))) {
                List<String> valueHolders = new ArrayList<String>(columnNumber);
                for (int i = 0; i < columns.size(); i++) {
                    String type = resultSetMetaData.getRight().get(i);
                    valueHolders.add(calcValueHolder(type));
                }

                boolean forceUseUpdate = false;
                //ob10的处理
                if (dataBaseType != null && dataBaseType == DataBaseType.MySql && OriginalConfPretreatmentUtil.isOB10(jdbcUrl)) {
                    forceUseUpdate = true;
                }

                INSERT_OR_REPLACE_TEMPLATE = WriterUtil.getWriteTemplate(columns, valueHolders, writeMode, dataBaseType, forceUseUpdate);
                writeRecordSql = String.format(INSERT_OR_REPLACE_TEMPLATE, this.table);
                LOG.info("writeRecordSql:[{}]", writeRecordSql);
            }
        }

        protected String calcValueHolder(String columnType) {
            return VALUE_HOLDER;
        }
    }

}
