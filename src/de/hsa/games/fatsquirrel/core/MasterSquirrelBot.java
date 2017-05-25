package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;
import de.hsa.games.fatsquirrel.util.LogAdvice;

import java.lang.reflect.Proxy;

/**
 * Created by Freya on 19.05.2017.
 */
public class MasterSquirrelBot extends MasterSquirrel {
    public MasterSquirrelBot(int id, int energy, XY position) {
        super(id, energy, position);
    }
    BotControllerFactory botControllerFactory = new BotControllerFactoryImpl();

    @Override
    public void nextStep(EntityContext context) throws Exception {
        ControllerContext controllerContext = new ControllerContextImpl(context, this);
        BotController botController = botControllerFactory.createMasterBotController();
        BotController proxiedBotController = (BotController) Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[] {BotController.class},new LogAdvice(botController));
        proxiedBotController.nextStep(controllerContext);
    }

    class ControllerContextImpl implements ControllerContext {
        EntityContext context;
        MasterSquirrelBot master;

        private ControllerContextImpl(EntityContext context, MasterSquirrelBot master) {
            this.context = context;
            this.master = master;
        }

        public Entity getEntity() {
            return master;
        }

        @Override
        public EntityContext getEntityContext() {
            return context;
        }

        @Override
        public XY getViewLowerLeft() {
            XY size = context.getSize();
            int x = position.getX() - 15;
            int y = position.getY() + 15;
            if (x < 0) { //links klein
                x = 0;
            }
            if (y > size.getY()) { //unten groß
                y = size.getY() - 1;
            }
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            XY size = context.getSize();
            int x = position.getX() + 15;
            int y = position.getY() - 15;
            if (x > size.getX()) { //rechts groß
                x = size.getX() - 1;
            }
            if (y < 0) { //oben klein
                y = 0;
            }
            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return master.getPosition();
        }

        @Override
        public EntityType getEntityAt(XY xy) {
            XY viewUpperRight = getViewUpperRight();
            XY viewLowerLeft = getViewLowerLeft();

            if (xy.getX() > viewUpperRight.getX() | xy.getX() < viewLowerLeft.getX()) {
                throw new OutOfViewException();
            } else if (xy.getY() > viewLowerLeft.getY() | xy.getY() < viewUpperRight.getY()) {
                throw new OutOfViewException();
            }

            if (context.getEntityType(xy) instanceof BadBeast) {
                return EntityType.BAD_BEAST;
            } else if (context.getEntityType(xy) instanceof GoodBeast) {
                return EntityType.GOOD_BEAST;
            } else if (context.getEntityType(xy) instanceof BadPlant) {
                return EntityType.BAD_PLANT;
            } else if (context.getEntityType(xy) instanceof GoodPlant) {
                return EntityType.GOOD_PLANT;
            } else if (context.getEntityType(xy) instanceof Wall) {
                return EntityType.WALL;
            } else if (context.getEntityType(xy) instanceof MasterSquirrel) {
                return EntityType.MASTER_SQUIRREL;
            } else if (context.getEntityType(xy) instanceof MiniSquirrel) {
                return EntityType.MINI_SQUIRREL;
            } else return EntityType.NONE;
        }

        @Override
        public boolean isMine(XY xy) {
            return master.checkIfChild(context.getEntityType(xy));
        }

        @Override
        public void move(XY direction) throws Exception {
            context.tryMove((MasterSquirrel) master, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) throws NotEnoughEnergyException {
            MiniSquirrelBot child = (MiniSquirrelBot) spawnChild(energy);
            child.setPosition(direction);
            //TODO wohin mit dem KIND ?
        }

        @Override
        public void implode(int impactRadius) {
        //I can't, i'm too old for this
        }

        @Override
        public int getEnergy() {
            return master.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            //i am the master...
            return new XY(0,0);
        }

        @Override
        public long getRemainingSteps() { //not implemented yet
            return 0;
        }
    }
}


