package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerImpl implements BotController {

    private ControllerContext controllerContext;
    private EntityContext entityContext;

    BotControllerImpl(ControllerContext controllerContext, EntityContext entityContext){
        this.controllerContext = controllerContext;
        this.entityContext = entityContext;
    }

    // TODO: diff. constructor for mini & master
 //   BotControllerImpl(ControllerContext controllerContext, EntityContext entityContext){
 //       this.controllerContext = controllerContext;
 //       this.entityContext = entityContext;
 //   }


    @Override
    public void nextStep(ControllerContext view) {

    }
}
