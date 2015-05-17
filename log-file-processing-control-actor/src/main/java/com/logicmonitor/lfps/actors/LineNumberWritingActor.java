package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.messages.FinishProcessingMessage;
import com.logicmonitor.lfps.messages.LineNumberRangeMessage;
import com.logicmonitor.lfps.messages.LineWritingMessage;

import java.io.*;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class LineNumberWritingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof LineWritingMessage) {

            if(logger.isDebugEnabled()) {
                logger.debug("Start writing log file:" + message);
            }

            LineWritingMessage writingMessage = (LineWritingMessage) message;
            LineNumberRangeMessage range = writingMessage.getLineNumberRange();

            if (range.getStart() == -1) {
                //ignore
            } else {
                final File logFile = new File(writingMessage.getLogFile());
                BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
                File newLogFile = new File(logFile.getAbsolutePath() + ".new");
                newLogFile.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newLogFile));
                long lineNumber = range.getStart();
                long lineNumberEnd = range.getEnd();
                String line2;
                try {
                    while ((line2 = bufferedReader.readLine()) != null) {
                        String newLine = lineNumber + " " + line2;
                        bufferedWriter.write(newLine, 0, newLine.length());
                        bufferedWriter.newLine();
                        lineNumber++;
                    }
                } finally {
                    bufferedReader.close();
                    bufferedWriter.close();
                }

                getContext().actorSelection("/user/watchActor").tell(new FinishProcessingMessage(writingMessage.getLogFileIndex()), getSelf());

                if(lineNumber != lineNumberEnd + 1) {
                    throw new RuntimeException(String.format("write line %d != line number end %d", lineNumber, lineNumberEnd));
                }

                if(logger.isDebugEnabled()) {
                    logger.debug("Finished writing log file:" + message + " with " + lineNumber +
                                " lines");
                }


            }
        }

    }
}
