package com.logicmonitor.lfps.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.messages.BatchApplyLineNumberMessage;
import com.logicmonitor.lfps.messages.LineNumberRangeMessage;
import com.logicmonitor.lfps.messages.LineWritingMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class LineNumberRequestingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private Map<Integer, BatchApplyLineNumberMessage> logFileMapping = new HashMap<Integer, BatchApplyLineNumberMessage>();

    private int currentContinuousIndex = -1;

    private long count = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof BatchApplyLineNumberMessage) {

            if(logger.isDebugEnabled()) {
                logger.debug("Received Line Number Requesting Message:" + message);
            }

            // do it linearly

            final BatchApplyLineNumberMessage msg = (BatchApplyLineNumberMessage) message;

            logFileMapping.put(msg.getLogFileIndex(), msg);

            final Set<Integer> indices = logFileMapping.keySet();
            final int start = currentContinuousIndex + 1;
            int end = msg.getLogFileIndex();
            if(indices.size() == msg.getTotalLogFileCount()) {
                end = msg.getTotalLogFileCount() - 1;
            }

            for(int j = start; j <= end; j++) {
                if(!indices.contains(j)) {
                    break;
                } else {
                    currentContinuousIndex = j;

                    BatchApplyLineNumberMessage toRequest = logFileMapping.get(j);

                    if(logger.isDebugEnabled()) {
                        logger.debug("Start dispatching line number:" + toRequest + "|" + j + "|" +
                                     indices);
                    }

                    int logFileIndex = toRequest.getLogFileIndex();
                    int batchCount = toRequest.getBatchCount();
                    final LineNumberRangeMessage range;
                    count += batchCount;
                    range = new LineNumberRangeMessage(count - batchCount + 1, count);

                    if(logger.isDebugEnabled()) {
                        logger.debug("Dispatching the next line number:" + message + "|" + range);
                    }

                    LineWritingMessage writingMessage = new LineWritingMessage(logFileIndex, toRequest.getLogFile(), range);
                    getContext().actorOf(Props.create(LineNumberWritingActor.class).withDispatcher("thread-pool-dispatcher")/*,
                            "lineNumberWritingActor" + j*/).
                            tell(writingMessage, getSelf());
                }
            }

        } else {
            unhandled(message);
        }
    }
}
