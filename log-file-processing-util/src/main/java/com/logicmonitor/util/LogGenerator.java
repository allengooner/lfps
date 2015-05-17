package com.logicmonitor.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by allen.gl on 2015/5/13.
 */
public abstract class LogGenerator {

    private static final String LOG_FILE_PREFIX = "logtest.";

    private static final String LOG_FILE_POSTFIX = ".log";

    private static final int MIN_LENGTH = 100;

    private static final int MAX_LENGTH = 200;

    private static final int SINGLE_FILE_SIZE_BASE = 1024;
//    private static final int SINGLE_FILE_SIZE_BASE = 10;

    public static void generate(String dir, int fileCount) throws FileNotFoundException,
                                                                    IOException{

        final File logDir = new File(dir);
        FileUtils.deleteDirectory(logDir);

        if(!logDir.exists()) {
            FileUtils.forceMkdir(logDir);
        }

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        Random random = new Random();
        for(int i = 0; i < fileCount; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            final String infix = DateUtil.getShortDateString(calendar.getTime());
            final String fileName = LOG_FILE_PREFIX + infix + LOG_FILE_POSTFIX;
            final File logFile = new File(logDir, fileName);
            if(logFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(logFile);
                FileChannel fc = fos.getChannel();
                StringBuilder log = new StringBuilder();
//                log.append(DateUtil.getFullDateString(calendar.getTime())).append(" ");
                try {
                    int lineCount = 6 * SINGLE_FILE_SIZE_BASE;
                    lineCount += random.nextInt(2 * SINGLE_FILE_SIZE_BASE);
                    for(int j = 0; j < lineCount; j++) {
                        log.append(DateUtil.getFullDateString(calendar.getTime())).append(" ");
                        log.append(RandomStringGenerator.nextString(MIN_LENGTH, MAX_LENGTH));

                        calendar.add(Calendar.MILLISECOND, random.nextInt(5));
                    }
                    ByteBuffer buf = ByteBuffer.allocateDirect(4 * 1024 * 1024 /** 1024*/);
//                    ByteBuffer buf = ByteBuffer.allocate(4 * SINGLE_FILE_SIZE_BASE * SINGLE_FILE_SIZE_BASE);
                    int position = buf.position();
                    buf.put(log.toString().getBytes(), position, log.toString().getBytes().length);
                    buf.flip();
                    fc.write(buf);
                } finally {
                    fc.close();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            generate("E:\\logs", 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
