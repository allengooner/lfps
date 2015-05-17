package com.logicmonitor.lfps.io;

import java.io.File;

/**
 * Created by allen.gl on 2015/5/13.
 */
public class LogFileList {
    private static class FileListReaderHolder {
        private static String logDir = "E:\\logs";
        static {
            String confLogDir = System.getProperty("log.dir");
            if(confLogDir != null) {
                logDir = confLogDir;
            }
        }
        public static final FileListReader fileListReader = new FileListReader(new File(logDir));
    }

    public static FileListReader getFileListReader() {
        return FileListReaderHolder.fileListReader;
    }
}
