package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.botimpls.BotControllerMaster;
import de.hsa.games.fatsquirrel.botimpls.BotControllerMini;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerFactoryImpl implements BotControllerFactory{
    //object & class for dynamically int.
    private Class<?> objectClass = null;
    private Object BotController = null;
    private Logger logger = Logger.getLogger("launcherLogger");
    
    public BotControllerFactoryImpl(){
    }

    @Override
    public BotController createMasterBotController(String classPath) {
        //load class from String and dyn. int. BotController through the class
        try {
            objectClass = Class.forName(classPath);
            BotController = objectClass.newInstance();
        } catch (Exception e) {
            logger.log(Level.WARNING,e.getMessage());
        }
        return (BotControllerMaster) BotController;
    }

    @Override
    public BotController createMiniBotController(String classPath) {
        try {
            objectClass = Class.forName(classPath);
            BotController = objectClass.newInstance();
        } catch (Exception e) {
            logger.log(Level.WARNING,e.getMessage());
        }
        return (BotControllerMini) BotController;
    }
}
