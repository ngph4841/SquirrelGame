package de.hsa.games.fatsquirrel.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Freya on 21.05.2017.
 */
public class MainLogger {

    //extra logger in klassen proxy?


        static Logger logger;
        public FileHandler fileHandler;
        SimpleFormatter formatter;

        private MainLogger() throws IOException{
            //instance the logger
            logger = Logger.getLogger(MainLogger.class.getName());
            //instance the filehandler
            fileHandler = new FileHandler("Log.txt",true);
            //instance formatter, set formatting, and handler
            formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        }

    private static Logger getLogger(){
        if(logger == null){
            try {
                new MainLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    public static void log(Level level, String msg){
        getLogger().log(level, msg);
    }

}
