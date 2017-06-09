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
        private static Logger logger;
        static{
            //int the logger
            logger = Logger.getLogger(MainLogger.class.getSimpleName());
            //int formatter, set formatting, and handler
            FileHandler fileHandler = null;
            try {
                fileHandler = new FileHandler("Log.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        }
    public static void log(Level level, String msg){
        logger.log(level, msg);
    }
}
