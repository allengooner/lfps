package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
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

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof LineCountingRequestMessage) {

            LineCountingRequestMessage requestMessage = (LineCountingRequestMessage) message;
            final File logFile = new File(((LineCountingRequestMessage) message).getLogDir(),
                    requestMessage.getLogFile());
            if(logger.isDebugEnabled()) {
                logger.debug("Start counting lines:" + logFile.getAbsolutePath());
            }
            LineNumberReader reader = new LineNumberReader(new FileReader(logFile));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    //ignore
                }

                int noOfLines = reader.getLineNumber();

                if(logger.isDebugEnabled()) {
                    logger.debug("Finished counting lines:" + logFile.getAbsolutePath() + "|" +
                                 noOfLines + " lines");
                }

                BatchApplyLineNumberMessage applyLineNumberMessage =
                        new BatchApplyLineNumberMessage(requestMessage.getLogFileIndex(),
                                logFile.getAbsolutePath(), noOfLines, ((LineCountingRequestMessage) message).getTotalLogFileCount());
                getContext().actorSelection("/user/lineNumberRequestingActor").tell(applyLineNumberMessage, getSelf());


//                while (!handled) {
//                    if(actorSelection == null) {
//                        actorSelection = getContext().actorSelection(
//                                "/user/lineNumberDispatchingActor");
//                    }
//
//                    TimeUnit.MICROSECONDS.sleep(1L);
//
//                    actorSelection.tell(applyLineNumberMessage, getSelf());
//                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//                Profiler.release();
                reader.close();
            }

//        } else if (message instanceof LineWritingMessage) {
//            logger.info("Before forwarding Line Writing Message:" + message);
//            handled = true;
//
//            getContext().actorOf(
//                    Props.create(LineNumberWritingActor.class).
//                            withDispatcher("thread-pool-dispatcher"),
//                    "lineNumberWritingActor" + ((LineWritingMessage) message).getLogFileIndex()).
//                    tell(message, getSelf());

        } else {
            unhandled(message);
        }
    }
}
