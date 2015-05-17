package com.logicmonitor.lfps.messages;

import java.io.Serializable;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class BatchApplyLineNumberMessage implements Serializable {

    private int logFileIndex;

    private String logFile;

    private int batchCount;

    private int totalLogFileCount;

    public int getLogFileIndex() {
        return logFileIndex;
    }

    public String getLogFile() {
        return logFile;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public int getTotalLogFileCount() {
        return totalLogFileCount;
    }

    public BatchApplyLineNumberMessage(int logFileIndex, String logFile, int batchCount, int totalLogFileCount) {

        this.logFileIndex = logFileIndex;
        this.logFile = logFile;
        this.batchCount = batchCount;
        this.totalLogFileCount = totalLogFileCount;
    }

    @Override
    public String toString() {
        return "BatchApplyLineNumberMessage{" +
               "logFileIndex=" + logFileIndex +
               ", logFile='" + logFile + '\'' +
               ", batchCount=" + batchCount +
               ", totalLogFileCount=" + totalLogFileCount +
               '}';
    }
}
