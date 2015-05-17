package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.io.FileListReader;
import com.logicmonitor.lfps.messages.LogFileScanRequestMessage;

import java.io.File;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class LogFileListingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof LogFileScanRequestMessage) {
            if(logger.isDebugEnabled()) {
                logger.debug("Received log file dir:" +
                             ((LogFileScanRequestMessage) message).getLogDir());
            }
            final File logDir = new File(((LogFileScanRequestMessage) message).getLogDir());
            // thread confinement
            if(logger.isDebugEnabled()) {
                logger.debug("Finished log file dir:" +
                             ((LogFileScanRequestMessage) message).getLogDir());
            }
            getContext().actorSelection("/user/taskDispatchingActor")
                    .tell(new FileListReader(logDir), getSelf());
        } else {
            unhandled(message);
        }
    }
}
