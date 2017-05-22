package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerFactoryImpl implements BotControllerFactory{ //in die Gameimpl zum erstellen der botcontroller
    private ControllerContext controllerContext;

    public BotControllerFactoryImpl(ControllerContext controllerContext){
        this.controllerContext = controllerContext;
    }

    @Override
    public BotController createMasterBotController() {
        return new BotControllerImpl(controllerContext);
    }

    @Override
    public BotController createMiniBotController() {
        return new BotControllerImpl(controllerContext);
    }
}
