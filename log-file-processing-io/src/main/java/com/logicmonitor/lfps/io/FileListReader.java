package com.logicmonitor.lfps.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by allen.gl on 2015/5/13.
 *
 * Not Thread-Safe.
 */
public class FileListReader {

    public File getDir() {
        return dir;
    }

    private File dir;

    private volatile String[] logFiles;

    public FileListReader(File dir) {
        this.dir = dir;
    }

    public String[] listSortedFile() {

        return listSortedFile(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("logtest") && name.endsWith(".log");
            }
        });

    }

    public String[] listSortedFile(FilenameFilter filenameFilter) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException();
        }

        if (logFiles == null) {
            logFiles = dir.list(filenameFilter);

            Arrays.sort(logFiles, new Comparator<String>() {
                public int compare(String f1, String f2) {
                    return f1.compareTo(f2);
                }
            });
        }

        return logFiles;
    }
}
