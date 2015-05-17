package com.logicmonitor.lfps.test;

import com.logicmonitor.lfps.control.ActorControl;
import com.logicmonitor.lfps.io.FileListReader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;

/**
 * Created by allen.gl on 2015/5/16.
 */
public class LogProcessingIntegrationTest {

    @BeforeTest
    public void generateLogFiles() throws IOException {
//        LogGenerator.generate("E:\\logs", 100000);
    }

//    @Test
//    public void testThreadMode() throws IOException {
////        String[] logFiles = LogFileList.getFileListReader().listSortedFile();
////
////        for(String logFile: logFiles) {
////
////        }
//        ThreadHandlerControl.startProcessing();
//
//        testFiles();
//    }


    private void testFiles() throws IOException {
        final File logDir = new File("E:\\logs");
        final String[] oldLogFiles = new FileListReader(logDir).listSortedFile();
        final String[] newLogFiles = new FileListReader(logDir).listSortedFile(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".new");
                    }
                });


        compareLineByLine(logDir, oldLogFiles, newLogFiles);
    }


    @Test
    public void testActorMode() throws IOException {
//        final Object monitor = new Object();
        ActorControl.startProcessing();

        // wait actors to finish theirs jobs
//        try {
//            monitor.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        // wait actors to finish theirs jobs...
//        // quick and dirty way
//        for(;;) {
//            try {
//                TimeUnit.SECONDS.sleep(1L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            if() {
//                break;
//            }
//        }

        testFiles();
    }

    private void compareLineByLine(File logDir, String[] oldLogFiles, String[] newLogFiles) throws IOException {
        Assert.assertEquals(oldLogFiles.length, newLogFiles.length,
                "original file count not equal to new");

        // compare line by line...
        long lineNumber = 0L;
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

    @AfterTest
    public void cleanUp() throws IOException {
//        FileUtils.deleteDirectory(new File("E:\\logs"));
    }
}
