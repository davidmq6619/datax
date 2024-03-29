package com.sinohealth.datax.plugin.rdbms.reader;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.datax.common.element.*;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.plugin.RecordSender;
import com.alibaba.datax.common.plugin.TaskPluginCollector;
import com.alibaba.datax.common.statistics.PerfRecord;
import com.alibaba.datax.common.statistics.PerfTrace;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.rdbms.reader.Constant;
import com.alibaba.datax.plugin.rdbms.reader.Key;
import com.alibaba.datax.plugin.rdbms.reader.util.OriginalConfPretreatmentUtil;
import com.alibaba.datax.plugin.rdbms.reader.util.PreCheckTask;
import com.alibaba.datax.plugin.rdbms.reader.util.ReaderSplitUtil;
import com.alibaba.datax.plugin.rdbms.reader.util.SingleTableSplitUtil;
import com.alibaba.datax.plugin.rdbms.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.DBProperty;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.common.*;
import com.sinohealth.datax.entity.source.*;
import com.sinohealth.datax.entity.zktarget.*;
import com.sinohealth.datax.plugin.rdbms.init.BizDataIniter;
import com.sinohealth.datax.reflection.Reflector;
import com.sinohealth.datax.reflection.factory.DefaultObjectFactory;
import com.sinohealth.datax.reflection.factory.ObjectFactory;
import com.sinohealth.datax.utils.EtlSTConst;
import com.sinohealth.datax.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SinohealthRdbmsReader {

    public static class Job {
        public static final Logger LOG = LoggerFactory
                .getLogger(Job.class);

        public Job(DataBaseType dataBaseType) {
            OriginalConfPretreatmentUtil.DATABASE_TYPE = dataBaseType;
            SingleTableSplitUtil.DATABASE_TYPE = dataBaseType;
        }

        public void init(Configuration originalConfig) {

            OriginalConfPretreatmentUtil.doPretreatment(originalConfig);

            LOG.debug("After job init(), job config now is:[\n{}\n]",
                    originalConfig.toJSON());
        }

        public void preCheck(Configuration originalConfig, DataBaseType dataBaseType) {
            /*检查每个表是否有读权限，以及querySql跟splik Key是否正确*/
            Configuration queryConf = ReaderSplitUtil.doPreCheckSplit(originalConfig);
            String splitPK = queryConf.getString(Key.SPLIT_PK);
            List<Object> connList = queryConf.getList(Constant.CONN_MARK, Object.class);
            String username = queryConf.getString(Key.USERNAME);
            String password = queryConf.getString(Key.PASSWORD);
            ExecutorService exec;
            if (connList.size() < 10) {
                exec = Executors.newFixedThreadPool(connList.size());
            } else {
                exec = Executors.newFixedThreadPool(10);
            }
            Collection<PreCheckTask> taskList = new ArrayList<PreCheckTask>();
            for (int i = 0, len = connList.size(); i < len; i++) {
                Configuration connConf = Configuration.from(connList.get(i).toString());
                PreCheckTask t = new PreCheckTask(username, password, connConf, dataBaseType, splitPK);
                taskList.add(t);
            }
            List<Future<Boolean>> results = Lists.newArrayList();
            try {
                results = exec.invokeAll(taskList);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            for (Future<Boolean> result : results) {
                try {
                    result.get();
                } catch (ExecutionException e) {
                    DataXException de = (DataXException) e.getCause();
                    throw de;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            exec.shutdownNow();
        }


        public List<Configuration> split(Configuration originalConfig,
                                         int adviceNumber) {
            return ReaderSplitUtil.doSplit(originalConfig, adviceNumber);
        }

        public void post(Configuration originalConfig) {
            // do nothing
        }

        public void destroy(Configuration originalConfig) {
            // do nothing
        }

    }

    public static class Task {
        public static final Logger LOG = LoggerFactory
                .getLogger(Task.class);
        public static final boolean IS_DEBUG = LOG.isDebugEnabled();
        protected final byte[] EMPTY_CHAR_ARRAY = new byte[0];

        public DataBaseType dataBaseType;
        public int taskGroupId = -1;
        public int taskId = -1;

        public String username;
        public String password;
        public String jdbcUrl;
        public String mandatoryEncoding;

        protected Class<?> sourceClass;
        protected Class<?> targetClass;
        protected Processor processor;
        protected CommonData commonData;
        protected String initDataDbUrl;
        protected String initDataDbUser;
        protected String initDataDbPassword;
        protected DBConnectionPool pool;


        // 作为日志显示信息时，需要附带的通用信息。比如信息所对应的数据库连接等信息，针对哪个表做的操作
        public String basicMsg;

        public Task(DataBaseType dataBaseType) {
            this(dataBaseType, -1, -1);
        }

        public Task(DataBaseType dataBaseType, int taskGropuId, int taskId) {
            this.dataBaseType = dataBaseType;
            this.taskGroupId = taskGropuId;
            this.taskId = taskId;
        }

        public void init(Configuration readerSliceConfig) {
            /* for database connection */
            this.username = readerSliceConfig.getString(Key.USERNAME);
            this.password = readerSliceConfig.getString(Key.PASSWORD);
            this.jdbcUrl = readerSliceConfig.getString(Key.JDBC_URL);

            this.initDataDbUrl = readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.INIT_DATA_DB_URL);
            this.initDataDbUser = readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.INIT_DATA_DB_USER);
            this.initDataDbPassword = readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.INIT_DATA_DB_PASSWORD);
            EtlSTConst.orgId = readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.ORG_ID);
            EtlSTConst.storeId = readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.STORE_ID);
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
                LOG.info("sourceClass:[{}]", readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.SOURCE_CLASS));
                LOG.info("targetClass:[{}]", readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.TARGET_CLASS));
                LOG.info("processClass:[{}]", readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.PROCESSOR_CLASS));
                this.sourceClass = Class.forName("com.sinohealth.datax.entity.source." + readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.SOURCE_CLASS));
                this.targetClass = Class.forName("com.sinohealth.datax.entity.zktarget." + readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.TARGET_CLASS));
                this.processor = (Processor) new DefaultObjectFactory().create(Class.forName("com.sinohealth.datax.processors." + readerSliceConfig.getString(com.alibaba.datax.plugin.rdbms.writer.Key.PROCESSOR_CLASS)));
            } catch (ClassNotFoundException ex) {
                LOG.error("sourceClass/targetClass/processor class not found", ex);
            }

            LOG.info("Init biz cache begin......");
            long begin = System.currentTimeMillis();
            // 标准化緩存的初始化
            BizDataIniter bizDataIniter = new BizDataIniter(DataBaseType.PostgreSQL, initDataDbUrl, initDataDbUser, initDataDbPassword);
            // 加载检验标准化字典
            Map<String, StandardBasTestItem> testItemMap = bizDataIniter.findBasTestItemMap();
            // 加载检验标准化字典-大项
            Map<String, StandardBasTestItem> testMethodItemMap = bizDataIniter.findBasTestMethodItemMap();
            // 加载检验单位标准化字典
            Map<String, String> testUnitMap = bizDataIniter.findBasTestUnitMap();
            // 加载检查项字典
            Map<String, BasCheckItem> checkItemMap = bizDataIniter.findCheckItemList();
            //甲状腺等级
            List<KeywordsLevelDto> etlLevelJson = bizDataIniter.findEtlLevelJson();
            // 加载诊断标准化字典
            List<BasInspectionKeyword> diagnoseItemMap = bizDataIniter.findBasDiagnoseItemList();
            // 加载检查方法
            List<BasItemAlias> basItemAlias = bizDataIniter.findBasItemAlias();
            // 加载离散类型为2的字典
            Map<String, String> resultMap = Maps.newHashMap();
            resultMap.put("-", "-1");
            resultMap.put("--", "-1");
            resultMap.put("---", "-1");
            resultMap.put("----", "-1");
            resultMap.put("-----", "-1");
            resultMap.put("阴", "-1");
            resultMap.put("阴性", "-1");
            resultMap.put("阴性（-）", "-1");
            resultMap.put("阴性(-)", "-1");
            resultMap.put("弱阳性", "1");
            resultMap.put("阳性(轻度)", "1");
            resultMap.put("阳性（轻度）", "1");
            resultMap.put("弱阳+-", "1");
            resultMap.put("弱阳", "1");
            resultMap.put("+-", "1");
            resultMap.put("-+", "1");
            resultMap.put("±", "1");
            resultMap.put("阳", "2");
            resultMap.put("阳性", "2");
            resultMap.put("强阳性", "2");
            resultMap.put("阳性(", "2");
            resultMap.put("阳性+", "2");
            resultMap.put("“阳性”", "2");
            resultMap.put("检出（+)", "2");
            resultMap.put("检出（+）", "2");
            resultMap.put("陽性(+)", "2");
            resultMap.put("阳性(+）", "2");
            resultMap.put("+", "2");
            resultMap.put("++", "2");
            resultMap.put("+++", "2");
            resultMap.put("++++", "2");
            resultMap.put("+++++", "2");
            resultMap.put("＋", "2");
            resultMap.put("＋＋", "2");
            resultMap.put("＋＋＋", "2");
            resultMap.put("＋＋＋＋", "2");
            resultMap.put("＋＋＋＋＋", "2");
            resultMap.put("阳性(+)", "2");
            resultMap.put("阳性（+）", "2");
            resultMap.put("异常", "2");
            resultMap.put("阳性(3+)", "2");
            resultMap.put("强阳性(3+)", "2");
            resultMap.put("阳性(2+)", "2");
            resultMap.put("阳性(4+)", "2");
            resultMap.put("未见", "0");
            resultMap.put("未检出", "0");
            resultMap.put("未见异常", "0");
            resultMap.put("+1","2");
            resultMap.put("1+","2");
            resultMap.put("+2","2");
            resultMap.put("2+","2");
            resultMap.put("+3","2");
            resultMap.put("3+","2");
            resultMap.put("+4","2");
            resultMap.put("4+","2");
            resultMap.put("+5","2");
            resultMap.put("5+","2");
            resultMap.put("清晰透明","0");
            resultMap.put("浑浊","2");
            resultMap.put("轻度浑浊","2");
            resultMap.put("清晰","0");
            resultMap.put("normal","0");
            resultMap.put("rbc negative","0");
            resultMap.put("微浊","2");
            resultMap.put("(-)","0");
            resultMap.put("无异常","0");
            resultMap.put("RBCNegative","0");

            this.commonData = new CommonData();

            DBProperty dbProperty = new DBProperty();
            dbProperty.setUrl(jdbcUrl);
            dbProperty.setUser(username);
            dbProperty.setPassword(password);
            this.commonData.setDbProperty(dbProperty);
            this.commonData.setBasTestMethodItemMap(testMethodItemMap);
            this.commonData.setBasTestItemMap(testItemMap);
            this.commonData.setBasTestUnitMap(testUnitMap);
            this.commonData.setBasCheckItemMap(checkItemMap);
            this.commonData.setKeywordsLevelList(etlLevelJson);
            this.commonData.setBasInspectionKeywordList(diagnoseItemMap);
            this.commonData.setBasItemAliasList(basItemAlias);
            this.commonData.setResultDiscreteMap(resultMap);

            pool = DBConnectionPool.getInstance(jdbcUrl, username, password);

            LOG.info("Init biz cache end. Spend time :[{}] ms", (System.currentTimeMillis() - begin));

            //ob10的处理
            if (this.jdbcUrl.startsWith(com.alibaba.datax.plugin.rdbms.writer.Constant.OB10_SPLIT_STRING) && this.dataBaseType == DataBaseType.MySql) {
                String[] ss = this.jdbcUrl.split(com.alibaba.datax.plugin.rdbms.writer.Constant.OB10_SPLIT_STRING_PATTERN);
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

            this.mandatoryEncoding = readerSliceConfig.getString(Key.MANDATORY_ENCODING, "");

            basicMsg = String.format("jdbcUrl:[%s]", this.jdbcUrl);

        }

        public void startRead(Configuration readerSliceConfig,
                              RecordSender recordSender,
                              TaskPluginCollector taskPluginCollector, int fetchSize) {
            String querySql = readerSliceConfig.getString(Key.QUERY_SQL);
            String table = readerSliceConfig.getString(Key.TABLE);

            PerfTrace.getInstance().addTaskDetails(taskId, table + "," + basicMsg);

            LOG.info("Begin to read record by Sql: [{}\n] {}.",
                    querySql, basicMsg);
            PerfRecord queryPerfRecord = new PerfRecord(taskGroupId, taskId, PerfRecord.PHASE.SQL_QUERY);
            queryPerfRecord.start();

            Connection conn = null;
            ResultSet rs = null;
            try {
                conn = pool.getConnection();
                // session config .etc related
                DBUtil.dealWithSessionConfig(conn, readerSliceConfig,
                        this.dataBaseType, basicMsg);

                rs = DBUtil.query(conn, querySql, fetchSize);
                queryPerfRecord.end();

                //这个统计干净的result_Next时间
                PerfRecord allResultPerfRecord = new PerfRecord(taskGroupId, taskId, PerfRecord.PHASE.RESULT_NEXT_ALL);
                allResultPerfRecord.start();

                long rsNextUsedTime = 0;
                long lastTime = System.nanoTime();

                ObjectFactory objectFactory = new DefaultObjectFactory();
                Object targetEntity = objectFactory.create(targetClass);

                if (rs != null && !rs.wasNull()) {

                    ResultSetMetaData md = rs.getMetaData();
                    int columnCount = md.getColumnCount();
                    Reflector reflector = Reflector.forClass(sourceClass);
                    while (rs.next()) {
                        rsNextUsedTime += (System.nanoTime() - lastTime);
                        Object sourceEntity = objectFactory.create(sourceClass);
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = md.getColumnLabel(i);
                            if (StrUtil.isBlank(columnName)) {
                                columnName = md.getColumnName(i);
                            }
                            String propertyName = reflector.getFieldName(columnName);
                            if (reflector.hasSetter(propertyName)) {
                                Object columnValue = rs.getObject(i);
                                if (columnValue != null) {
                                    try {
                                        Class<?> fieldType = reflector.getSetterType(propertyName);
                                        if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                                            columnValue = rs.getShort(i);
                                        } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                                            columnValue = rs.getByte(i);
                                        } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                                            columnValue = rs.getInt(i);
                                        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                                            columnValue = rs.getLong(i);
                                        } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                                            columnValue = rs.getFloat(i);
                                        } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                                            columnValue = rs.getDouble(i);
                                        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                                            columnValue = rs.getBoolean(i);
                                        } else if (fieldType.equals(Date.class)) {
                                            columnValue = rs.getTimestamp(i);
                                        } else if (fieldType.equals(String.class)) {
                                            columnValue = rs.getString(i);
                                        }

                                        reflector.getSetInvoker(propertyName).invoke(sourceEntity, new Object[]{columnValue});
                                    } catch (Exception e) {
                                        throw new Exception("DaoHelper.parseResultList failed,columnName:" + columnName, e);
                                    }
                                }
                            }
                        }
                        if (sourceEntity != null) {
                            targetEntity = processor.dataProcess(sourceEntity, targetEntity, this.commonData);
                            if (targetEntity instanceof StandardCustomerRecordList) {
                                List<RegMnCustomer> details = ((StandardCustomerRecordList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (RegMnCustomer detail : details) {
                                        // create record
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            } else if (targetEntity instanceof StandardCheckRecordList) {
                                List<StandardCheckRecord> details = ((StandardCheckRecordList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardCheckRecord detail : details) {
                                        // create record
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            } else if (targetEntity instanceof StandardTestRecordList) {
                                List<StandardTestRecord> details = ((StandardTestRecordList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardTestRecord detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            } else if (targetEntity instanceof StandardDiagnoseRecordList) {
                                List<StandardDiagnoseRecord> details = ((StandardDiagnoseRecordList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardDiagnoseRecord detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            } else if (targetEntity instanceof StandardRegStdInfoList) {
                                List<StandardRegStdInfo> details = ((StandardRegStdInfoList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardRegStdInfo detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            } else if (targetEntity instanceof StandardMnStdInfoList) {
                                List<StandardMnStdInfo> details = ((StandardMnStdInfoList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardMnStdInfo detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            }else if (targetEntity instanceof StandardCheckFeiResultList) {
                                List<StandardCheckFeiResult> details = ((StandardCheckFeiResultList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardCheckFeiResult detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            }else if (targetEntity instanceof StandardCheckJzxResultList) {
                                List<StandardCheckJzxResult> details = ((StandardCheckJzxResultList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardCheckJzxResult detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            }else if (targetEntity instanceof StandardMnTestRecordList) {
                                List<StandardMnTestRecord> details = ((StandardMnTestRecordList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardMnTestRecord detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            }else if (targetEntity instanceof StandardMnCheckRecordList) {
                                List<StandardMnCheckRecord> details = ((StandardMnCheckRecordList) targetEntity).getList();
                                if (CollectionUtil.isNotEmpty(details)) {
                                    for (StandardMnCheckRecord detail : details) {
                                        Record record = recordSender.createRecord();
                                        record = ReflectUtil.getRecord(record, detail);
                                        recordSender.sendToWriter(record);
                                    }
                                }
                            } else {
                                Record record = recordSender.createRecord();
                                record = ReflectUtil.getRecord(record, targetEntity);
                                recordSender.sendToWriter(record);
                            }
                        }
                        lastTime = System.nanoTime();
                    }
                }

                allResultPerfRecord.end(rsNextUsedTime);
                //目前大盘是依赖这个打印，而之前这个Finish read record是包含了sql查询和result next的全部时间
                LOG.info("Finished read record by Sql: [{}\n] {}.",
                        querySql, basicMsg);

            } catch (Exception e) {
                LOG.error("startRead》》读取数据过程异常：", e);
                throw RdbmsException.asQueryException(this.dataBaseType, e, querySql, table, username);
            } finally {
                DBUtil.closeDBResources(null, conn);
            }
        }

        public void post(Configuration originalConfig) {
            // do nothing
        }

        public void destroy(Configuration originalConfig) {
            // do nothing
        }

        protected Record transportOneRecord(RecordSender recordSender, ResultSet rs,
                                            ResultSetMetaData metaData, int columnNumber, String mandatoryEncoding,
                                            TaskPluginCollector taskPluginCollector) {
            Record record = buildRecord(recordSender, rs, metaData, columnNumber, mandatoryEncoding, taskPluginCollector);
            recordSender.sendToWriter(record);
            return record;
        }

        protected Record buildRecord(RecordSender recordSender, ResultSet rs, ResultSetMetaData metaData, int columnNumber, String mandatoryEncoding,
                                     TaskPluginCollector taskPluginCollector) {
            Record record = recordSender.createRecord();

            try {
                for (int i = 1; i <= columnNumber; i++) {
                    switch (metaData.getColumnType(i)) {

                        case Types.CHAR:
                        case Types.NCHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                        case Types.NVARCHAR:
                        case Types.LONGNVARCHAR:
                            String rawData;
                            if (StringUtils.isBlank(mandatoryEncoding)) {
                                rawData = rs.getString(i);
                            } else {
                                rawData = new String((rs.getBytes(i) == null ? EMPTY_CHAR_ARRAY :
                                        rs.getBytes(i)), mandatoryEncoding);
                            }
                            record.addColumn(new StringColumn(rawData));
                            break;

                        case Types.CLOB:
                        case Types.NCLOB:
                            record.addColumn(new StringColumn(rs.getString(i)));
                            break;

                        case Types.SMALLINT:
                        case Types.TINYINT:
                        case Types.INTEGER:
                        case Types.BIGINT:
                            record.addColumn(new LongColumn(rs.getString(i)));
                            break;

                        case Types.NUMERIC:
                        case Types.DECIMAL:
                            record.addColumn(new DoubleColumn(rs.getString(i)));
                            break;

                        case Types.FLOAT:
                        case Types.REAL:
                        case Types.DOUBLE:
                            record.addColumn(new DoubleColumn(rs.getString(i)));
                            break;

                        case Types.TIME:
                            record.addColumn(new DateColumn(rs.getTime(i)));
                            break;

                        // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
                        case Types.DATE:
                            if (metaData.getColumnTypeName(i).equalsIgnoreCase("year")) {
                                record.addColumn(new LongColumn(rs.getInt(i)));
                            } else {
                                record.addColumn(new DateColumn(rs.getDate(i)));
                            }
                            break;

                        case Types.TIMESTAMP:
                            record.addColumn(new DateColumn(rs.getTimestamp(i)));
                            break;

                        case Types.BINARY:
                        case Types.VARBINARY:
                        case Types.BLOB:
                        case Types.LONGVARBINARY:
                            record.addColumn(new BytesColumn(rs.getBytes(i)));
                            break;

                        // warn: bit(1) -> Types.BIT 可使用BoolColumn
                        // warn: bit(>1) -> Types.VARBINARY 可使用BytesColumn
                        case Types.BOOLEAN:
                        case Types.BIT:
                            record.addColumn(new BoolColumn(rs.getBoolean(i)));
                            break;

                        case Types.NULL:
                            String stringData = null;
                            if (rs.getObject(i) != null) {
                                stringData = rs.getObject(i).toString();
                            }
                            record.addColumn(new StringColumn(stringData));
                            break;

                        default:
                            throw DataXException
                                    .asDataXException(
                                            DBUtilErrorCode.UNSUPPORTED_TYPE,
                                            String.format(
                                                    "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库读取这种字段类型. 字段名:[%s], 字段名称:[%s], 字段Java类型:[%s]. 请尝试使用数据库函数将其转换datax支持的类型 或者不同步该字段 .",
                                                    metaData.getColumnName(i),
                                                    metaData.getColumnType(i),
                                                    metaData.getColumnClassName(i)));
                    }
                }
            } catch (Exception e) {
                if (IS_DEBUG) {
                    LOG.debug("read data " + record.toString()
                            + " occur exception:", e);
                }
                //TODO 这里识别为脏数据靠谱吗？
                taskPluginCollector.collectDirtyRecord(record, e);
                if (e instanceof DataXException) {
                    throw (DataXException) e;
                }
            }
            return record;
        }
    }

}
