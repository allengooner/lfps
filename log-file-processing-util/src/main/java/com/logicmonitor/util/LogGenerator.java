package com.logicmonitor.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
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
public class LogGenerator {

    private String logFilePrefix = "logtest.";

    private String logFilePostfix = ".log";

    private int minLength = 100;

    private int maxLength = 200;

    private int singleFileSizeBase = 1024;
//    private int SINGLE_FILE_SIZE_BASE = 10;

    public LogGenerator(String logFilePrefix, String logFilePostfix, int minLength, int maxLength,
                        int singleFileSizeBase) {
        this.logFilePrefix = logFilePrefix;
        this.logFilePostfix = logFilePostfix;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.singleFileSizeBase = singleFileSizeBase;
    }

    public LogGenerator() {
    }

    public void generate(String dir, int fileCount) throws IOException {

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
            final String fileName = logFilePrefix + infix + logFilePostfix;
            final File logFile = new File(logDir, fileName);
            if(logFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(logFile);
                FileChannel fc = fos.getChannel();
                StringBuilder log = new StringBuilder();
//                log.append(DateUtil.getFullDateString(calendar.getTime())).append(" ");
                try {
                    int lineCount = 6 * singleFileSizeBase;
                    lineCount += random.nextInt(2 * singleFileSizeBase);
                    for(int j = 0; j < lineCount; j++) {
                        log.append(DateUtil.getFullDateString(calendar.getTime())).append(" ");
                        log.append(RandomStringGenerator.nextString(minLength, maxLength));

                        calendar.add(Calendar.MILLISECOND, random.nextInt(5));
                    }
                    ByteBuffer buf = ByteBuffer.allocateDirect(2 * 1024 * 1024 /** 1024*/);
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
            new LogGenerator().generate("E:\\logs", 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
