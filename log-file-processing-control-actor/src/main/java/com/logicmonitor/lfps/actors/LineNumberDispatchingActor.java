package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.messages.BatchApplyLineNumberMessage;
import com.logicmonitor.lfps.messages.LineNumberRangeMessage;
import com.logicmonitor.lfps.messages.LineWritingMessage;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class LineNumberDispatchingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private volatile long count = 0;

    private volatile int currentFileIndex = -1;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof BatchApplyLineNumberMessage) {
            if(logger.isDebugEnabled()) {
                logger.debug("Start dispatching line number:" + message);
            }

            final BatchApplyLineNumberMessage applyMessage = (BatchApplyLineNumberMessage) message;
            int logFileIndex = applyMessage.getLogFileIndex();
            int batchCount = applyMessage.getBatchCount();
            final LineNumberRangeMessage range;
            if(currentFileIndex != logFileIndex - 1) {
//                range = new LineNumberRangeMessage(-1, -1);
                //ignore
                if(logger.isDebugEnabled()) {
                    logger.debug("Not the next line number:" + message);
                }
            } else {
                count += batchCount;
                currentFileIndex++;
                range = new LineNumberRangeMessage(count - batchCount, count - 1);

                if(logger.isDebugEnabled()) {
                    logger.debug("Dispatching the next line number:" + range);
                }

                LineWritingMessage writingMessage = new LineWritingMessage(logFileIndex, applyMessage.getLogFile(), range);
                getSender().tell(writingMessage, getSelf());
            }


        } else {
            unhandled(message);
        }
    }

}
