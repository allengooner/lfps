package com.logicmonitor.lfps.control;

import com.logicmonitor.lfps.io.FileListReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by allen.gl on 2015/5/13.
 */
public class LineNumberBatchDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(LineNumberBatchDispatcher.class);

//    private int batchCount;

//    public LineNumberBatchDispatcher(int batchCount) {
//        this.batchCount = batchCount;
//    }

//    private AtomicLong atomicLong = new AtomicLong(0);

    private volatile long count = 0;

    private volatile int currentFileIndex = -1;

    private String[] logFiles = new FileListReader(new File("E:\\logs")).listSortedFile();

    public synchronized LineNumberRange dispatch(int logFileIndex, int batchCount) {
//        long nextStart = atomicLong.addAndGet(batchCount);
        if(logger.isDebugEnabled()) {
            logger.debug("Received dispatch:" + logFileIndex + "|" + batchCount);
        }

        LineNumberRange range;
        if (currentFileIndex != logFileIndex - 1) {
            range = new LineNumberRange(-1, -1);
        } else {
            count += batchCount;
            currentFileIndex++;
            range = new LineNumberRange(count - batchCount, count - 1);
        }

        if(logger.isDebugEnabled()) {
            logger.debug("Finished dispatch:" + logFileIndex + "|" + batchCount + "|" + range);
        }

        return range;
    }

    public static class LineNumberRange {

        private long start;

        private long end;

        public LineNumberRange(long start, long end) {
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
            return String.format("LineNumberRange[%d, %d]", start, end);
        }
    }

}
