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

//    private static byte[] newLine = System.getProperty("line.separator").getBytes();
    private static String newLineStr = System.getProperty("line.separator");

    @Override
    public int readFileLineNumber(File logFile) throws IOException {

        FileInputStream fis = new FileInputStream(logFile);
        FileChannel channel = fis.getChannel();
        try {
            ByteBuffer bb = ByteBuffer.allocateDirect(bufferSize);
            bb.clear();
//            System.out.println("File size: " + channel.size() / 4);
            long len = 0;
            int offset = 0;
            while ((len = channel.read(bb))!= -1){
                bb.flip();
//                System.out.println("Offset: "+offset+"\tlen: "+len+"\tremaining:"+bb.hasRemaining());
    //            bb.asIntBuffer().get(ipArr,offset,(int)len/4);
    //            offset += (int)len/4;
                bb.clear();
            }
        } finally {
            channel.close();
            fis.close();
        }

        return 0;
    }

    @Override
    public void writeLineNumberToFile(File logFile, long start, long end) throws IOException {


        BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
        File newLogFile = new File(logFile.getAbsolutePath() + ".new");
        newLogFile.createNewFile();
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newLogFile));
        FileOutputStream fos = new FileOutputStream(newLogFile);
        FileChannel fc = fos.getChannel();
        long lineNumber = start;
        String line;
        ByteBuffer bb = ByteBuffer.allocateDirect(3 * 1024 * 1024);
        bb.clear();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String newLine = lineNumber + " " + line + newLineStr;

                bb.put(newLine.getBytes());

//                bufferedWriter.write(newLine, 0, newLine.length());
//                bufferedWriter.newLine();


                lineNumber++;
            }

            bb.flip();
            while(bb.hasRemaining()) {
                fc.write(bb);
            }

        } finally {
            bufferedReader.close();
//            bufferedWriter.close();
            fc.close();
            fos.close();
        }


        if(lineNumber != end) {
            throw new RuntimeException(String.format("line number %d != end %d", lineNumber, end));
        }
    }

    @Override
    public void readFileAndWriteLineNumber(File logFile) throws IOException {

    }

    public static void main(String[] args) throws Exception {
//        long lines = new FileChannelIOService().readFileLineNumber(new File("E:\\logs\\logtest.2015-02-03.log"));
        new FileChannelIOService().writeLineNumberToFile(new File("E:\\logs\\logtest.2015-02-09.log"), 0L, 13888L);
    }
}
