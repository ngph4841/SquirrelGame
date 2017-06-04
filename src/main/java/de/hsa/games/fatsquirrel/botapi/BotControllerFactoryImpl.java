package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.botimpls.BotControllerMaster;
import de.hsa.games.fatsquirrel.botimpls.BotControllerMini;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerFactoryImpl implements BotControllerFactory{ //in die Gameimpl zum erstellen der botcontroller
    private Class<?> objectClass = null;
    private Object BotController = null;
    
    public BotControllerFactoryImpl(){
    }

    @Override
    public BotController createMasterBotController(String classPath) {
        try {
            objectClass = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            BotController = objectClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (BotControllerMaster) BotController;
        //return new BotControllerMaster();
    }

    @Override
    public BotController createMiniBotController(String classPath) {
        try {
            objectClass = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            BotController = objectClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (BotControllerMini) BotController;
        //return new BotControllerMini();
    }
}
