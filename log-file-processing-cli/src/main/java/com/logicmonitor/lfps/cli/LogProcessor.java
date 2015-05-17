package com.logicmonitor.lfps.cli;

import org.apache.commons.cli.*;

/**
 * Created by allen.gl on 2015/5/16.
 */
public class LogProcessor {

    public static void main(String[] args) {
        Option help = new Option("help", "print this message");


        Option logDir = Option.builder().longOpt("logDir").required().hasArg().
                desc("log file directory").valueSeparator('=').build();
        Option concurrencyMode = Option.builder().longOpt("concurrencyMode").hasArg().
                desc("concurrency mode: thread or actor, default mode is thread").build();
        Option generateLog = Option.builder().longOpt("generateLog").hasArg().
                desc("generate log files for testing, specify the number of files").build();

//        Option property  = OptionBuilder.withArgName( "property=value" )
//                .hasArgs(2)
//                .withValueSeparator()
//                .withDescription( "use value for given property" )
//                .create( "D" );


        Options options = new Options();
        options.addOption(help).addOption(logDir).
                addOption(concurrencyMode).addOption(generateLog)/*.addOption(property)*/;


        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );

            if( line.hasOption( "logDir" ) ) {
                // initialise the member variable
                String dir = line.getOptionValue( "logDir" );
            }

            // print help message
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "log file processing system", options );
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }



    }

}
