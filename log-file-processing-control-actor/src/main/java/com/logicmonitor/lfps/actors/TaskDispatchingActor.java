package com.logicmonitor.lfps.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.logicmonitor.lfps.io.FileListReader;
import com.logicmonitor.lfps.messages.LineCountingRequestMessage;
import com.logicmonitor.lfps.messages.StartProcessingMessage;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class TaskDispatchingActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

//    private int NUMBER_OF_ROUTEE;
//
//    private Router router;
//    {
//        NUMBER_OF_ROUTEE = Runtime.getRuntime().availableProcessors();
//        List<Routee> routees = new ArrayList<Routee>();
//        for (int i = 0; i < NUMBER_OF_ROUTEE; i++) {
//            ActorRef r = getContext().actorOf(Props.create(LogFileLineCountingActor.class));
//            getContext().watch(r);
//            routees.add(new ActorRefRoutee(r));
//        }
//        router = new Router(new RoundRobinRoutingLogic(), routees);
//    }

    @Override
    public void onReceive(Object message) throws Exception {



        if(message instanceof FileListReader) {
            if(logger.isDebugEnabled()) {
                logger.debug("Start listing files!");
            }

            final String[] logFiles = ((FileListReader) message).listSortedFile();
            final String logDir = ((FileListReader) message).getDir().getAbsolutePath();
            // create parent
//            getContext().actorOf(Props.create(LogFileLineCountingActor.class, "/countinglogFileLineCountingActor" + i))
            for(int i = 0; i < logFiles.length; i++) {
                final LineCountingRequestMessage requestMessage =  new LineCountingRequestMessage(i, logFiles[i], logDir, logFiles.length);
                getContext().actorOf(
                        Props.create(LogFileLineCountingActor.class).
                                withDispatcher("thread-pool-dispatcher"),
                        "countinglogFileLineCountingActor" + i)
                        .tell(requestMessage, getSelf());
//                router.route(requestMessage, getSender());
            }
            getContext().actorSelection("/user/watchActor").tell(new StartProcessingMessage(logFiles.length), getSelf());
            if(logger.isDebugEnabled()) {
                logger.debug("Finished dipatching file-reading tasks:" + logFiles.length);
            }
        } else {
            unhandled(message);
        }
    }
}
