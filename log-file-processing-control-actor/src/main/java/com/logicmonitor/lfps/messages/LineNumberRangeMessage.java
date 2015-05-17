package com.logicmonitor.lfps.messages;

import java.io.Serializable;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class LineNumberRangeMessage implements Serializable {

    private long start;

    private long end;

    public LineNumberRangeMessage(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("LinuNumberRangeMessage[%d, %d]", start, end);
    }
}
