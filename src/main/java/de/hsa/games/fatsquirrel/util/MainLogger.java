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
        private Logger logger;
        private final FileHandler fileHandler = new FileHandler("Log.txt");
        private final SimpleFormatter formatter = new SimpleFormatter();

        public MainLogger(String className) throws IOException{
            //instance the logger
            this.logger = Logger.getLogger(className);
            //instance formatter, set formatting, and handler
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        }

    public void log(Level level, String msg){
        logger.log(level, msg);
    }

}
