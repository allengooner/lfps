package com.logicmonitor.lfps.cli;

import com.logicmonitor.lfps.cli.factory.ProcessingControlFactory;
import com.logicmonitor.lfps.control.ActorControl;
import com.logicmonitor.lfps.control.ThreadHandlerControl;
import com.logicmonitor.util.LogGenerator;
import org.apache.commons.cli.*;

/**
 * Created by allen.gl on 2015/5/16.
 */
public class LogProcessor {

    private static ProcessingControlFactory factory = new ProcessingControlFactory();

    public static void main(String[] args) {
        Option help = Option.builder().longOpt("help").desc("print this message").build();

        Option logDir = Option.builder().longOpt("logDir").hasArg().
                desc("log file directory").valueSeparator('=').build();
        Option concurrencyMode = Option.builder().longOpt("concurrencyMode").hasArg().
                desc("concurrency mode: thread or actor, default mode is thread").build();
        Option parallel = Option.builder().longOpt("parallel").hasArg().
                desc("specify the parallel level").valueSeparator('=').build();
        Option generateLog = Option.builder().longOpt("generateLog").hasArg().
                desc("generate log files for testing, specify the number of files to generate").build();
        Option ioType = Option.builder().longOpt("ioType").hasArg().
                desc("specify the ioType: io or nio, default nio, nio may have problem on some platform").
                valueSeparator('=').build();

//        Option property  = OptionBuilder.withArgName( "property=value" )
//                .hasArgs(2)
//                .withValueSeparator()
//                .withDescription( "use value for given property" )
//                .create( "D" );


        Options options = new Options();
        options.addOption(help).addOption(logDir).
                addOption(concurrencyMode).addOption(generateLog).addOption(parallel).addOption(ioType);


        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );

            if(line.hasOption("help")) {
                System.out.print("help");
                // print help message
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "java -jar *.jar <options>", options );
                System.exit(0);
            }

            final String homeDir = System.getProperty("user.home");
            String dir = line.getOptionValue( "logDir", homeDir);

            // generate logs
            if(line.hasOption("generateLog")) {

                String fileCount = line.getOptionValue("generateLog", "100");

                LogGenerator logGenerator = new LogGenerator();
                logGenerator.generate(dir, Integer.valueOf(fileCount));

                System.out.print("generated " + fileCount + " log files in " + dir );

                System.exit(0);
            }

            // get parallel level
            String parallelLevel = line.getOptionValue("parallel",
                    String.valueOf(Runtime.getRuntime().availableProcessors()));

            // get io type
            String ioServiceType = line.getOptionValue("ioType", "nio");

            // process logs
            String mode = line.getOptionValue("concurrencyMode", "thread");
            if("thread".equalsIgnoreCase(mode)) {
                ThreadHandlerControl threadHandlerControl = factory.getThreadHandlerControl(dir, Integer.valueOf(parallelLevel));
                threadHandlerControl.startProcessing(ioServiceType);
                System.out.print("Finished processing in thread mode");
                System.exit(0);
            } else if("actor".equalsIgnoreCase(mode)) {
                ActorControl actorControl = factory.getActorControl(dir);
                actorControl.startProcessing(ioServiceType);
                System.out.print("Finished processing in actor mode");
            }


        }
        catch( ParseException exp ) {
            // oops, something went wrong
            exp.printStackTrace();
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        } catch (Exception e) {
            System.err.println( "Log processor failed.  Reason: " + e.getMessage() );
        }



    }

}
