package com.logicmonitor.lfps.messages;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class FinishProcessingMessage {

    private int finishedLogFileIndex;

    public FinishProcessingMessage(int finishedLogFileIndex) {
        this.finishedLogFileIndex = finishedLogFileIndex;
    }

    public int getFinishedLogFileIndex() {
        return finishedLogFileIndex;
    }

    @Override
    public String toString() {
        return "FinishProcessingMessage{" +
               "finishedLogFileIndex=" + finishedLogFileIndex +
               '}';
    }
}
