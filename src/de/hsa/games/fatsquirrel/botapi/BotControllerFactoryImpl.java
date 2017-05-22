package de.hsa.games.fatsquirrel.botapi;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerFactoryImpl implements BotControllerFactory{ //in die Gameimpl zum erstellen der botcontroller
    public BotControllerFactoryImpl(){
    }

    @Override
    public BotController createMasterBotController() {
        return new BotControllerImpl();
    }

    @Override
    public BotController createMiniBotController() {
        return new BotControllerImpl();
    }
}
