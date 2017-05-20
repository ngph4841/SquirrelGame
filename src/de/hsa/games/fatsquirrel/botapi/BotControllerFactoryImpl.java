package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerFactoryImpl implements BotControllerFactory{
    private ControllerContext controllerContext;
    private EntityContext entityContext;

    BotControllerFactoryImpl(ControllerContext controllerContext, EntityContext entityContext){
        this.controllerContext = controllerContext;
        this.entityContext = entityContext;
    }

    @Override
    public BotController createMasterBotController() {
        return new BotControllerImpl(controllerContext,entityContext);
    }

    @Override
    public BotController createMiniBotController() {
        return new BotControllerImpl(controllerContext, entityContext);
    }
}
