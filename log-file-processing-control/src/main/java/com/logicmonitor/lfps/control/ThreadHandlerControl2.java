package com.logicmonitor.lfps.control;

import com.logicmonitor.lfps.io.FileListReader;
import com.logicmonitor.lfps.io.IOService;
import com.logicmonitor.lfps.io.LogFileList;
import com.logicmonitor.lfps.io.impl.BufferedIOService;
import com.logicmonitor.util.Profiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by allen.gl on 2015/5/13.
 */
public class ThreadHandlerControl2 {

    private static final Logger logger = LoggerFactory.getLogger(ThreadHandlerControl2.class);

    private static ExecutorService executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 5);

//    private static ExecutorService executor = Executors
//            .newFixedThreadPool(1);

    private static LineNumberBatchDispatcher lineNumberBatchDispatcher = new LineNumberBatchDispatcher();

    private static FileListReader fileListReader = LogFileList.getFileListReader();

    private static CountDownLatch countDownLatch;

    public static void startProcessing() {
        Profiler.start();
//        Profiler.start("Log file listing");
        String[] logFiles = fileListReader.listSortedFile();
//        Profiler.release();

        countDownLatch = new CountDownLatch(logFiles.length);

//        Profiler.enter("Task submission");
        for (int i = 0; i < logFiles.length; i++) {
            executor.submit(
                    new AddLineNoTask(new File("E:\\logs", logFiles[i]), i, countDownLatch));
        }
//        Profiler.release();

        try {
//            Profiler.enter("Finished with " + logFiles.length + " log files!");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Profiler.release();
            if(logger.isInfoEnabled()) {
                logger.info("Task Summary:\n" + Profiler.dump());
                logger.info("Total Duration:" + Profiler.getDuration());
            }
        }

        System.exit(0);
    }

    private static class AddLineNoTask implements Runnable {

        private File logFile;

        private int logFileIndex;

        private CountDownLatch countDownLatch;

        private IOService ioService;

        private ThreadLocal<LineNumberBatchDispatcher.LineNumberRange> lineNumberRangeThreadLocal =
                new ThreadLocal<LineNumberBatchDispatcher.LineNumberRange>();

//        private ThreadLocal<Long> index = new ThreadLocal<Long>();

        public AddLineNoTask(File logFile, int logFileIndex, CountDownLatch countDownLatch) {
            this.logFile = logFile;
            this.logFileIndex = logFileIndex;
            this.countDownLatch = countDownLatch;
//            lineNumberRangeThreadLocal.set();
            this.ioService = new BufferedIOService();
        }

        public void run() {
            Profiler.start();
            if(logger.isDebugEnabled()) {
                logger.debug("Started " + Thread.currentThread().getName() + ":" + logFile);
            }
            try {
                int noOfLines = ioService.readFileLineNumber(logFile);

                LineNumberBatchDispatcher.LineNumberRange range;
                for(;;) {
                    range = lineNumberBatchDispatcher
                            .dispatch(logFileIndex, noOfLines);
                    if (range.getStart() == -1) {
                        try {
//                            TimeUnit.MILLISECONDS.sleep(1L);
                            TimeUnit.MICROSECONDS.sleep(1L);
//                            TimeUnit.NANOSECONDS.sleep(100L);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    } else {
                        break;
                    }
                }

                ioService.writeLineNumberToFile(logFile, range.getStart(), range.getEnd());

//                LineNumberBatchDispatcher.LineNumberRange range = lineNumberBatchDispatcher.dispatch(
//                        logFileIndex, noOfLines);
//                if(range.getStart() == -1) {
//                    Thread.yield();
//                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(logger.isDebugEnabled()) {
                    logger.debug("Finished " + Thread.currentThread().getName() + ":" + logFile);
                }
                countDownLatch.countDown();
                Profiler.release();
            }

            if(logger.isInfoEnabled()) {
                logger.info("Duration: " + Profiler.getDuration());
            }
        }
    }

    public static void main(String[] args) {
        ThreadHandlerControl2.startProcessing();
    }
}
