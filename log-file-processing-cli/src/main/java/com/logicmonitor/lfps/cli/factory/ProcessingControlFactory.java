package com.logicmonitor.lfps.cli.factory;

import com.logicmonitor.lfps.control.ActorControl;
import com.logicmonitor.lfps.control.ThreadHandlerControl;

import java.io.File;

/**
 * Created by allen.gl on 2015/5/17.
 */
public class ProcessingControlFactory {

    private ThreadHandlerControl threadHandlerControl;

    private ActorControl actorControl;

    public ThreadHandlerControl getThreadHandlerControl(String logDir, int parallelLevel) {
        if(threadHandlerControl == null) {
            threadHandlerControl = new ThreadHandlerControl(new File(logDir), parallelLevel);
        }

        return threadHandlerControl;
    }

    public ActorControl getActorControl(String logDir) {
        if(actorControl == null) {
            actorControl = new ActorControl(logDir);
        }

        return actorControl;
    }

}
