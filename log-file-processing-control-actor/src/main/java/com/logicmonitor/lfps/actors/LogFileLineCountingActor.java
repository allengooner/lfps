package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.control.ActorControl;
import com.logicmonitor.lfps.io.IOService;
import com.logicmonitor.lfps.io.impl.BufferedIOService;
import com.logicmonitor.lfps.io.impl.FileChannelIOService;
import com.logicmonitor.lfps.messages.BatchApplyLineNumberMessage;
import com.logicmonitor.lfps.messages.LineCountingRequestMessage;

import java.io.*;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class LogFileLineCountingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

//    private volatile boolean handled;
//
//    private volatile ActorSelection actorSelection;

    private IOService ioService;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof LineCountingRequestMessage) {

            LineCountingRequestMessage requestMessage = (LineCountingRequestMessage) message;
            final File logFile = new File(((LineCountingRequestMessage) message).getLogDir(),
                    requestMessage.getLogFile());
            if(logger.isDebugEnabled()) {
                logger.debug("Start counting lines:" + logFile.getAbsolutePath());
            }

            if(ioService == null) {
                if ("io".equalsIgnoreCase(ActorControl.ioServiceType)) {
                    this.ioService = new BufferedIOService();
                } else if ("nio".equalsIgnoreCase(ActorControl.ioServiceType)) {
                    this.ioService = new FileChannelIOService();
                }
            }

            int noOfLines = ioService.readFileLineNumber(logFile);

            if(logger.isDebugEnabled()) {
                    logger.debug("Finished counting lines:" + logFile.getAbsolutePath() + "|" +
                                 noOfLines + " lines");
                }

                BatchApplyLineNumberMessage applyLineNumberMessage =
                        new BatchApplyLineNumberMessage(requestMessage.getLogFileIndex(),
                                logFile.getAbsolutePath(), noOfLines, ((LineCountingRequestMessage) message).getTotalLogFileCount());
                getContext().actorSelection("/user/lineNumberRequestingActor").tell(applyLineNumberMessage, getSelf());




        } else {
            unhandled(message);
        }
    }
}
