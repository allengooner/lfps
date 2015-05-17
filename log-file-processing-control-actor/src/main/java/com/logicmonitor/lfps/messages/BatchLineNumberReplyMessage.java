package com.logicmonitor.lfps.messages;

import java.io.Serializable;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class BatchLineNumberReplyMessage implements Serializable {

    private long start;

    private long end;

    public BatchLineNumberReplyMessage(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "BatchLineNumberReplyMessage{" +
               "start=" + start +
               ", end=" + end +
               '}';
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
