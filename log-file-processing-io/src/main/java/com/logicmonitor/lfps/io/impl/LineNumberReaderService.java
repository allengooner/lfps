package com.logicmonitor.lfps.io.impl;

import com.logicmonitor.lfps.io.IOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class LineNumberReaderService implements IOService {

    private static final Logger logger = LoggerFactory.getLogger(LineNumberReaderService.class);

    @Override
    public int readFileLineNumber(File logFile) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(logFile));
        try {
            while (reader.readLine() != null) {
            }
        } finally {
            reader.close();
        }

        int noOfLines = reader.getLineNumber();
        return noOfLines;
    }

    @Override
    public void writeLineNumberToFile(File logFile, long start, long end) throws IOException {

    }

    @Override
    public void readFileAndWriteLineNumber(File logFile) throws IOException {

    }
}
