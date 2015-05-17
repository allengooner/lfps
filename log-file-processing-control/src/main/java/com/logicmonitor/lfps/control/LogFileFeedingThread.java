package com.logicmonitor.lfps.control;

import com.logicmonitor.lfps.io.FileListReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by allen.gl on 2015/5/13.
 */
public class LogFileFeedingThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(LogFileFeedingThread.class);

//    private BlockingQueue<String> logFileQueue = new SynchronousQueue<String>();
//    private BlockingQueue<String> logFileQueue = new LinkedTransferQueue<String>();
//    private BlockingQueue<String> logFileQueue = new LinkedBlockingQueue<String>();

    private String[] logFiles;

    @Override
    public void run() {
        // thread confinement
        logFiles = new FileListReader(new File("E:\\logs")).listSortedFile();
//        for(String logFile : logFiles) {
//            try {
//                logFileQueue.put(logFile);
//            } catch (InterruptedException e) {
//                // ignore
//            }
//        }
    }

    public String[] getLogFiles() {
        return logFiles;
    }
}
