package com.logicmonitor.lfps.io.impl;

import com.logicmonitor.lfps.io.IOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class BufferedIOService implements IOService {

    private static final Logger logger = LoggerFactory.getLogger(BufferedIOService.class);

    private int defaultBufferSize = 2 * 1024 * 1024;

    @Override
    public int readFileLineNumber(File logFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(logFile), defaultBufferSize );
        int noOfLines = 0;
        try {
            while ((reader.readLine()) != null) {
                noOfLines++;
            }
        } finally {
            reader.close();
        }

        return noOfLines;
    }

    @Override
    public void writeLineNumberToFile(File logFile, long start, long end) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile), defaultBufferSize);
        File newLogFile = new File(logFile.getAbsolutePath() + ".new");
        newLogFile.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newLogFile), defaultBufferSize);
        long lineNumber = start;
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String newLine = lineNumber + " " + line;
                bufferedWriter.write(newLine, 0, newLine.length());
                bufferedWriter.newLine();
                lineNumber++;
            }
        } finally {
            bufferedReader.close();
            bufferedWriter.close();
        }


        if(lineNumber - 1 != end) {
            throw new RuntimeException(String.format("line number %d != end %d", lineNumber - 1, end));
        }
    }

    @Override
    public void readFileAndWriteLineNumber(File logFile) throws IOException {
        throw new UnsupportedOperationException();
    }
}
