package com.logicmonitor.lfps.io.impl;

import com.logicmonitor.lfps.io.IOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by allen.gl on 2015/5/13.
 */
public class FileChannelIOService implements IOService {

    private static final Logger logger = LoggerFactory.getLogger(FileChannelIOService.class);

    private int bufferSize = 2 * 1024 * 1024;

    private static byte newLine = "\n".getBytes()[0];   // quick and dirty, consider only '\n', may have problem on some platform

    private static String newLineStr = System.getProperty("line.separator");

    @Override
    public int readFileLineNumber(File logFile) throws IOException {
        int lineNumber = 0;

        FileInputStream fis = new FileInputStream(logFile);
        FileChannel channel = fis.getChannel();
        try {
            ByteBuffer bb = ByteBuffer.allocateDirect(bufferSize);
            bb.clear();
            int len = 0;
            while ((len = channel.read(bb)) > 0) {
                bb.flip();
                byte[] bytes = new byte[len];
                bb.get(bytes);

                for(byte b : bytes) {
                    if(newLine == b){   // quick and dirty, consider only '\n', may have problem on some platform
                        lineNumber++;
                    }
                }

                bb.clear();
            }
        } finally {
            channel.close();
            fis.close();
        }

        return lineNumber;
    }

    @Override
    public void writeLineNumberToFile(File logFile, long start, long end) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
        File newLogFile = new File(logFile.getAbsolutePath() + ".new");
        newLogFile.createNewFile();
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newLogFile));
        FileOutputStream fos = new FileOutputStream(newLogFile);
        FileChannel wfc = fos.getChannel();
//        MappedByteBuffer mbb = wfc.map(FileChannel.MapMode.READ_WRITE, 0, wfc.size());
        long lineNumber = start;
        String line;
        // FIXME bufferSize should be configurable
        ByteBuffer bb = ByteBuffer.allocateDirect(bufferSize);  // assume in most cases it won't exceed 2MB
        bb.clear();
        try {
            while ((line = bufferedReader.readLine()) != null) {

                String newLine = lineNumber + " " + line + newLineStr;
                final byte[] bytesToWrite = newLine.getBytes();

                if(bb.remaining() >= bytesToWrite.length) {
                    bb.put(bytesToWrite);
                } else {
                    bb.flip();
                    while(bb.hasRemaining()) {
                        wfc.write(bb);
                    }
                    bb.clear();

                    bb.put(bytesToWrite);
                }


                lineNumber++;
            }

            bb.flip();

            while(bb.hasRemaining()) {
                wfc.write(bb);
            }
            bb.clear();

        } finally {
            bufferedReader.close();
//            bufferedWriter.close();
            wfc.close();
            fos.close();
        }


        if(lineNumber - 1 != end) {
            throw new RuntimeException(String.format("line number %d != end %d", lineNumber - 1, end));
        }
    }

    @Override
    public void readFileAndWriteLineNumber(File logFile) throws IOException {

    }

    public static void main(String[] args) throws Exception {
//        long lines = new FileChannelIOService().readFileLineNumber(new File("E:\\logs\\logtest.2015-02-07.log"));
//        new FileChannelIOService().writeLineNumberToFile(new File("E:\\logs\\logtest.2015-02-09.log"), 0L, 6514L);
    }
}
