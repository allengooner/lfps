package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.messages.FinishProcessingMessage;
import com.logicmonitor.lfps.messages.StartProcessingMessage;

import java.util.BitSet;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class WatchActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

//    public WatchActor(Object monitor) {
//        this.monitor = monitor;
//    }

//    private Object monitor;

    private BitSet bs;

    private int targetCount;

    private long start;

    private long end;

    @Override
    public void onReceive(Object message) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("Watch Actor Received Message:" + message);
        }

        if (message instanceof StartProcessingMessage) {
            start = System.currentTimeMillis();
            targetCount = ((StartProcessingMessage) message).getLogFileCount();
            bs = new BitSet(targetCount);
        } else if (message instanceof FinishProcessingMessage) {
            bs.set(((FinishProcessingMessage) message).getFinishedLogFileIndex());

            if (bs.cardinality() == targetCount) {
                end = System.currentTimeMillis();
                if(logger.isInfoEnabled()) {
                    logger.info("Duration: " + (end - start) + "ms");
                }

//                if(monitor != null) {
//                    monitor.notifyAll();
//                }

                getContext().system().shutdown();
            }

        } else {
            unhandled(message);
        }



    }

//    private boolean forall(int[] array) {
//        for(int i: array) {
//            if(i != 1) {
//                return false;
//            }
//        }
//        return true;
//    }
}
