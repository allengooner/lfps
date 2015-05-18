package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.control.ActorControl;
import com.logicmonitor.lfps.io.IOService;
import com.logicmonitor.lfps.io.impl.BufferedIOService;
import com.logicmonitor.lfps.io.impl.FileChannelIOService;
import com.logicmonitor.lfps.messages.FinishProcessingMessage;
import com.logicmonitor.lfps.messages.LineNumberRangeMessage;
import com.logicmonitor.lfps.messages.LineWritingMessage;

import java.io.*;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class LineNumberWritingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private IOService ioService;

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof LineWritingMessage) {

            if (logger.isDebugEnabled()) {
                logger.debug("Start writing log file:" + message);
            }

            LineWritingMessage writingMessage = (LineWritingMessage) message;
            LineNumberRangeMessage range = writingMessage.getLineNumberRange();

            if (range.getStart() == -1) {
                //ignore
            } else {
                if (ioService == null) {
                    if ("io".equalsIgnoreCase(ActorControl.ioServiceType)) {
                        this.ioService = new BufferedIOService();
                    } else if ("nio".equalsIgnoreCase(ActorControl.ioServiceType)) {
                        this.ioService = new FileChannelIOService();
                    }
                }


                final File logFile = new File(writingMessage.getLogFile());
                ioService.writeLineNumberToFile(logFile, range.getStart(), range.getEnd());

                getContext().actorSelection("/user/watchActor").tell(new FinishProcessingMessage(writingMessage.getLogFileIndex()), getSelf());

                if(logger.isDebugEnabled()) {
                    logger.debug("Finished writing log file:" + message + " with " + (range.getEnd() - range.getStart()) +
                                " lines");
                }


            }
        }

    }
}
