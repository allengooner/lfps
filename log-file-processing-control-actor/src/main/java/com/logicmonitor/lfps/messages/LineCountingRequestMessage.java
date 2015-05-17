package com.logicmonitor.lfps.messages;

import java.io.Serializable;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class LineCountingRequestMessage implements Serializable {

    private int logFileIndex;

    private String logFile;

    private String logDir;

    private int totalLogFileCount;

    public LineCountingRequestMessage(int logFileIndex, String logFile, String logDir, int totalLogFileCount) {
        this.logFileIndex = logFileIndex;
        this.logFile = logFile;
        this.logDir = logDir;
        this.totalLogFileCount = totalLogFileCount;
    }

    public int getTotalLogFileCount() {
        return totalLogFileCount;
    }

    public int getLogFileIndex() {
        return logFileIndex;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getLogDir() {
        return logDir;
    }

    @Override
    public String toString() {
        return "LineCountingRequestMessage{" +
               "logFileIndex=" + logFileIndex +
               ", logFile='" + logFile + '\'' +
               ", logDir='" + logDir + '\'' +
               ", totalLogFileCount=" + totalLogFileCount +
               '}';
    }
}
