package com.logicmonitor.lfps.io;

import java.io.File;
import java.io.IOException;

/**
 * Created by allen.gl on 2015/5/13.
 */
public interface IOService {
    int readFileLineNumber(File logFile) throws IOException;

    void writeLineNumberToFile(File logFile, long start, long end) throws IOException;

    void readFileAndWriteLineNumber(File logFile) throws IOException;
}
