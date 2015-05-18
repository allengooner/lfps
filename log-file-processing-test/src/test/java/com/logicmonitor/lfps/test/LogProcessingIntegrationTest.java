package com.logicmonitor.lfps.test;

import com.logicmonitor.lfps.control.ActorControl;
import com.logicmonitor.lfps.control.ThreadHandlerControl;
import com.logicmonitor.lfps.io.FileListReader;
import com.logicmonitor.util.LogGenerator;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;

/**
 * Created by allen.gl on 2015/5/16.
 */
public class LogProcessingIntegrationTest {

    private String homeDir = System.getProperty("user.home");

    @BeforeTest
    public void generateLogFiles() throws IOException {
        new LogGenerator().generate(homeDir, 20);
    }

    @Test
    public void testIOThreadMode() throws IOException {
        new ThreadHandlerControl(new File(homeDir),
                Runtime.getRuntime().availableProcessors()).startProcessing("io");

        testFiles();
    }

    @Test
    public void testNIOThreadMode() throws IOException {
        new ThreadHandlerControl(new File(homeDir),
                Runtime.getRuntime().availableProcessors()).startProcessing("nio");

        testFiles();
    }


    private void testFiles() throws IOException {
        final File logDir = new File(homeDir);
        final String[] oldLogFiles = new FileListReader(logDir).listSortedFile();
        final String[] newLogFiles = new FileListReader(logDir).listSortedFile(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".new");
                    }
                });


        compareLineByLine(logDir, oldLogFiles, newLogFiles);

        for(String newLogFile: newLogFiles) {
            FileUtils.forceDelete(new File(logDir, newLogFile));
        }
    }


    @Test
    public void testIOActorMode() throws IOException {
        new ActorControl(homeDir).startProcessing("io");

        testFiles();
    }

    @Test
    public void testNIOActorMode() throws IOException {
        new ActorControl(homeDir).startProcessing("nio");

        testFiles();
    }

    private void compareLineByLine(File logDir, String[] oldLogFiles, String[] newLogFiles) throws IOException {
        Assert.assertEquals(oldLogFiles.length, newLogFiles.length,
                "original file count not equal to new");

        // compare line by line...
        long lineNumber = 1L;
        for (int i = 0; i < oldLogFiles.length; i++) {
            BufferedReader reader1 = new BufferedReader(
                    new FileReader(new File(logDir, oldLogFiles[i])));
            BufferedReader reader2 = new BufferedReader(
                    new FileReader(new File(logDir, newLogFiles[i])));

            try {
                String line1;
                String line2;
                while ((line1 = reader1.readLine()) != null) {
                    line2 = reader2.readLine();

                    Assert.assertEquals(lineNumber + " " + line1, line2,
                            String.format("Different line: %s | %d | %s | %s",
                                    newLogFiles[i], lineNumber, line1, line2));

                    lineNumber++;
                }
            } finally {
                reader1.close();
                reader2.close();
            }
        }
    }

}
