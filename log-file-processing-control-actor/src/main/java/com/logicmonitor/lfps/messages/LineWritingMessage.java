package com.logicmonitor.lfps.messages;

import java.io.Serializable;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class LineWritingMessage implements Serializable {

    private int logFileIndex;

    private String logFile;

    private LineNumberRangeMessage lineNumberRange;

    public LineWritingMessage(int logFileIndex, String logFile,
                              LineNumberRangeMessage lineNumberRange) {
        this.logFileIndex = logFileIndex;
        this.logFile = logFile;
        this.lineNumberRange = lineNumberRange;
    }

    public int getLogFileIndex() {
        return logFileIndex;
    }

    public String getLogFile() {
        return logFile;
    }

    public LineNumberRangeMessage getLineNumberRange() {
        return lineNumberRange;
    }

    @Override
    public String toString() {
        return "LineWritingMessage{" +
               "logFileIndex=" + logFileIndex +
               ", logFile='" + logFile + '\'' +
               ", lineNumberRange=" + lineNumberRange +
               '}';
    }
}
