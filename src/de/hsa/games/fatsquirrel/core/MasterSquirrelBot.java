package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;

/**
 * Created by Freya on 19.05.2017.
 */
public class MasterSquirrelBot extends MasterSquirrel {
    public MasterSquirrelBot(int id, int energy, XY position) {
        super(id, energy, position);
    }

    @Override
    public void nextStep(EntityContext context) throws Exception {
        ControllerContext controllerContext = new ControllerContextImpl(context, this);
        BotControllerFactoryImpl botControllerFactory = new BotControllerFactoryImpl(controllerContext, context);
        BotController botController = botControllerFactory.createMasterBotController();
        botController.nextStep(controllerContext);
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
        public int getEnergy() {
            return 0;
        }
    }
}


