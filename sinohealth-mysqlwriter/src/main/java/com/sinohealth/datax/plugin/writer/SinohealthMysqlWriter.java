package com.sinohealth.datax.plugin.writer;

import com.alibaba.datax.common.plugin.RecordReceiver;
import com.alibaba.datax.common.spi.Writer;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.rdbms.util.DataBaseType;
import com.alibaba.datax.plugin.rdbms.writer.Key;
import com.sinohealth.datax.plugin.rdbms.writer.SinohealthRdbmsWriter;
import java.util.List;

public class SinohealthMysqlWriter extends Writer {
    private static final DataBaseType DATABASE_TYPE = DataBaseType.MySql;

    public static class Job extends Writer.Job {
        private Configuration originalConfig = null;
        private SinohealthRdbmsWriter.Job sinohealthCommonWriterJob;

        @Override
        public void preCheck(){
            this.init();
            this.sinohealthCommonWriterJob.writerPreCheck(this.originalConfig, DATABASE_TYPE);
        }

        @Override
        public void init() {
            this.originalConfig = super.getPluginJobConf();
            this.sinohealthCommonWriterJob = new SinohealthRdbmsWriter.Job(DATABASE_TYPE);
            this.sinohealthCommonWriterJob.init(this.originalConfig);
        }

        // 一般来说，是需要推迟到 task 中进行pre 的执行（单表情况例外）
        // 全局准备工作，比如odpswriter清空目标表
        @Override
        public void prepare() {
            //实跑先不支持 权限 检验
            //this.commonRdbmsWriterJob.privilegeValid(this.originalConfig, DATABASE_TYPE);
            this.sinohealthCommonWriterJob.prepare(this.originalConfig);
        }

        @Override
        public List<Configuration> split(int mandatoryNumber) {
            return this.sinohealthCommonWriterJob.split(this.originalConfig, mandatoryNumber);
        }

        // 一般来说，是需要推迟到 task 中进行post 的执行（单表情况例外）
        // 全局的后置工作，比如mysqlwriter同步完影子表后的rename操作。
        @Override
        public void post() {
            this.sinohealthCommonWriterJob.post(this.originalConfig);
        }

        @Override
        public void destroy() {
            this.sinohealthCommonWriterJob.destroy(this.originalConfig);
        }

    }

    public static class Task extends Writer.Task {
        private Configuration writerSliceConfig;
        private SinohealthRdbmsWriter.Task sinohealthCommonWriterTask;

        @Override
        public void init() {
            this.writerSliceConfig = super.getPluginJobConf();
            this.sinohealthCommonWriterTask = new SinohealthRdbmsWriter.Task(DATABASE_TYPE);
            this.sinohealthCommonWriterTask.init(this.writerSliceConfig);
        }

        @Override
        public void prepare() {
            this.sinohealthCommonWriterTask.prepare(this.writerSliceConfig);
        }

        // 从RecordReceiver中读取数据，写入目标数据源。RecordReceiver中的数据来自Reader和Writer之间的缓存队列。
        public void startWrite(RecordReceiver recordReceiver) {
            this.sinohealthCommonWriterTask.startWrite(recordReceiver, this.writerSliceConfig,
                    super.getTaskPluginCollector());
        }

        @Override
        public void post() {
            this.sinohealthCommonWriterTask.post(this.writerSliceConfig);
        }

        @Override
        public void destroy() {
            this.sinohealthCommonWriterTask.destroy(this.writerSliceConfig);
        }

        @Override
        public boolean supportFailOver(){
            String writeMode = writerSliceConfig.getString(Key.WRITE_MODE);
            return "replace".equalsIgnoreCase(writeMode);
        }

    }


}
