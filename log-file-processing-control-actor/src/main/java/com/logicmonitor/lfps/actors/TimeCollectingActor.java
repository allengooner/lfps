package com.logicmonitor.lfps.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by allen.gl on 2015/5/15.
 */
public class TimeCollectingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public static Object START_MESSAGE = new Object();

    public static Object END_MESSAGE = new Object();

    private long start;

    private long end;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message == START_MESSAGE) {
            start = System.currentTimeMillis();
        } else if (message == END_MESSAGE) {
            end = System.currentTimeMillis();
            if(logger.isInfoEnabled()) {
                logger.info("Duration: " + (end - start) + "ms");
            }
        } else {
            unhandled(message);
        }
    }

}
