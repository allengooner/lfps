package com.logicmonitor.lfps.messages;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class StartProcessingMessage {

    private int logFileCount;

    public StartProcessingMessage(int logFileCount) {
        this.logFileCount = logFileCount;
    }

    public int getLogFileCount() {
        return logFileCount;
    }

    @Override
    public String toString() {
        return "StartProcessingMessage{" +
               "logFileCount=" + logFileCount +
               '}';
    }
}
