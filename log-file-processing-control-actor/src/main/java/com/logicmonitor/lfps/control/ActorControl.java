package com.logicmonitor.lfps.control;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.logicmonitor.lfps.actors.LineNumberRequestingActor;
import com.logicmonitor.lfps.actors.LogFileListingActor;
import com.logicmonitor.lfps.actors.TaskDispatchingActor;
import com.logicmonitor.lfps.actors.WatchActor;
import com.logicmonitor.lfps.messages.LogFileScanRequestMessage;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class ActorControl {

    public static void startProcessing() {
        final ActorSystem system = ActorSystem.create("LogFileProcessingSystem");
        system.actorOf(Props.create(WatchActor.class), "watchActor");
        ActorRef logFileListingActor = system.actorOf(Props.create(LogFileListingActor.class), "logFileListingActor");
        system.actorOf(Props.create(TaskDispatchingActor.class), "taskDispatchingActor");
        system.actorOf(Props.create(LineNumberRequestingActor.class), "lineNumberRequestingActor");
        logFileListingActor.tell(new LogFileScanRequestMessage("E:\\logs"), null);

        system.awaitTermination();
    }



    public static void main(String[] args) {
//        final ActorSystem system = ActorSystem.create("LogFileProcessingSystem");
////        ActorRef timeCollectingActor = system.actorOf(Props.create(TimeCollectingActor.class), "timeCollectingActor");
////        timeCollectingActor.tell(TimeCollectingActor.START_MESSAGE, null);
//        ActorRef watchActor = system.actorOf(Props.create(WatchActor.class), "watchActor");
//        ActorRef logFileListingActor = system.actorOf(Props.create(LogFileListingActor.class), "logFileListingActor");
//        ActorRef taskDispatchingActor = system.actorOf(Props.create(TaskDispatchingActor.class), "taskDispatchingActor");
////        ActorRef lineNumberDispatchingActor = system.actorOf(Props.create(LineNumberDispatchingActor.class), "lineNumberDispatchingActor");
//        ActorRef lineNumberRequestingActor = system.actorOf(Props.create(LineNumberRequestingActor.class), "lineNumberRequestingActor");
//        logFileListingActor.tell(new LogFileScanRequestMessage("E:\\logs"), null);

        startProcessing();
    }




}
