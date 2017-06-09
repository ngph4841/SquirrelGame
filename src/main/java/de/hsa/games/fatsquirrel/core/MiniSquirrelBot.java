package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.util.LogAdvice;

import java.lang.reflect.Proxy;

/**
 * Created by Freya on 19.05.2017.
 */
public class MiniSquirrelBot extends MiniSquirrel {
    private final BotControllerFactory botControllerFactory;
    private final String classPath;
    private int steps = 0;

    MiniSquirrelBot(int id, int energy, XY position, int parentId, String classPath) {
        super(id, energy, position, parentId);
        this.botControllerFactory = new BotControllerFactoryImpl();
        this.classPath = classPath;
    }


    @Override
    public void nextStep(EntityContext context) throws Exception {
        steps++;
        ControllerContext controllerContext = new ControllerContextImpl(context, this);
        BotController botController = botControllerFactory.createMiniBotController(classPath);
        BotController proxiedBotController = (BotController) Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{BotController.class}, new LogAdvice(botController));
        proxiedBotController.nextStep(controllerContext);

    }

    class ControllerContextImpl implements ControllerContext {
        EntityContext context;
        MiniSquirrelBot mini;

        private ControllerContextImpl(EntityContext context, MiniSquirrelBot mini) {
            this.mini = mini;
            this.context = context;
        }
        //implode in flattenedboard refactoring
        //controllercontext abstarct klasse

        @Override
        public XY getViewLowerLeft() {
            XY size = context.getSize();
            int x = position.x - 10;
            int y = position.y + 10;
            if (x < 0) { //links klein
                x = 0;
            }
            if (y > size.y - 1) { //unten groß
                y = size.y;
            }
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            XY size = context.getSize();
            int x = position.x + 10;
            int y = position.y - 10;
            if (x > size.x - 1) { //rechts groß
                x = size.x;
            }
            if (y < 0) { //oben klein
                y = 0;
            }
            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return mini.getPosition();
        }

        @Override
        public EntityType getEntityAt(XY xy) {
            XY viewUpperRight = getViewUpperRight();
            XY viewLowerLeft = getViewLowerLeft();

            if (xy.x > viewUpperRight.x | xy.x < viewLowerLeft.x) {
                throw new OutOfViewException();
            } else if (xy.y > viewLowerLeft.y | xy.y < viewUpperRight.y) {
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
            return context.getEntityType(xy).getId() == mini.getParentId();
        }

        @Override
        public void move(XY direction) throws Exception {
            context.tryMove((MiniSquirrel) mini, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            //nope mini cant spawn mini...
        }

        @Override
        public int getEnergy() {
            return mini.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            for (int i = 0; i < context.getSize().x; i++) {
                for (int j = 0; j < context.getSize().y; j++) {
                    if (context.getEntityType(new XY(i, j)) != null) {
                        if (context.getEntityType(new XY(i, j)) instanceof MasterSquirrel) {
                            if (context.getEntityType(new XY(i, j)).getId() == mini.getParentId()) {
                                return new XY(i - mini.getPosition().x, j - mini.getPosition().y);
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public int getRemainingSteps() {
            return context.getStepCounter() - steps;
        }

        @Override
        public void shout(String text) {
            System.out.println(text);
        }

        @Override
        public Entity getEntity() {
            return mini;
        }

        @Override
        public EntityContext getEntityContext() {
            return context;
        }

        public void implode(int impactRadius) throws Exception {
            double impactArea = impactRadius * impactRadius * 3.14;
            // anders 2 oder 10 bearbeiten

            int x = mini.getPosition().x;
            int y = mini.getPosition().y;
            Entity[] entities = new Entity[(int) impactArea];
            int entitiesAmount = 0;
            int counter = (impactRadius - 1) * 2;

            //calc. entities in the impactArea
            boolean turn = false;
            int axisY = impactRadius;
            int axisX = 0;
            int alpha =0;//for symmetry purposes
            while (axisY >=impactRadius*-1) {
                for(int i = 0; i <= axisX; i++) {
                    if(i != 0) {
                        alpha = axisX / 2;
                    }
                    XY temp = position.plus(new XY(axisX-alpha, axisY));
                    switch (getEntityAt(temp)) {
                        case NONE:
                            break;
                        case WALL:
                            break;
                        case GOOD_BEAST:
                        case BAD_BEAST:
                        case BAD_PLANT:
                        case GOOD_PLANT:
                        case MINI_SQUIRREL:
                        case MASTER_SQUIRREL:
                            entities[entitiesAmount++] = context.getEntityType(temp);
                    }
                }
                if (turn) {
                    axisX-=2;
                }else{
                    axisX+=2;
                }
                if(axisX >= 2*impactRadius){
                    turn = true;
                }
                axisY--;
            }

            int collectedEnergy = 0;
            for (int i = 0; i < entitiesAmount; i++) {
                double distance = Math.sqrt(Math.pow(entities[i].getPosition().x, 2) + Math.pow(entities[i].getPosition().y, 2));
                double energyLoss = 200 * (mini.getEnergy() / impactArea) * (1 - distance / impactRadius);

                //id check of all squirrels so family doesnt get hurt
                int deltaEnergy = entities[i].getEnergy() - (int) energyLoss;
                switch (getEntityAt(entities[i].getPosition())) {
                    case BAD_BEAST:
                    case BAD_PLANT:
                        deltaEnergy = entities[i].getEnergy() + (int) energyLoss;
                        if (deltaEnergy >= 0) {
                            context.killAndReplace(entities[i]);
                        } else {
                            entities[i].updateEnergy((int) energyLoss);
                        }
                        break;
                    case GOOD_PLANT:
                    case GOOD_BEAST:
                        if (deltaEnergy >= 0) {             //entity mehr energy als energyloss / perfekt
                            collectedEnergy += (int) energyLoss;
                        } else {                            //entity weniger engery als energyloss
                            collectedEnergy += entities[i].getEnergy();
                        }
                        entities[i].updateEnergy((int) (-1 * energyLoss));
                        if (entities[i].getEnergy() <= 0) {
                            context.killAndReplace(entities[i]);
                        }
                        break;
                    case MINI_SQUIRREL:
                        MiniSquirrel temp = (MiniSquirrel) entities[i];
                        if (temp.getParentId() != mini.getParentId()) {
                            if (deltaEnergy >= 0) {             //entity mehr energy als energyloss / perfekt
                                collectedEnergy += (int) energyLoss;
                            } else {                            //entity weniger engery als energyloss
                                collectedEnergy += entities[i].getEnergy();
                            }
                            entities[i].updateEnergy((int) (-1 * energyLoss));
                            if (temp.getEnergy() <= 0) {
                                context.kill(temp);
                            }
                        }
                        break;
                    case MASTER_SQUIRREL:
                        collectedEnergy += energyLoss;
                        entities[i].updateEnergy((int) (-1 * energyLoss));
                        break;
                }
            }
            //find parent and give him energy
            for (int i = 0; i < context.getSize().x; i++) {
                for (int j = 0; j < context.getSize().y; j++) {
                    if(context.getEntityType(new XY(i,j)) != null) {
                        if (context.getEntityType(new XY(i, j)).getId() == mini.getParentId()) {
                            context.getEntityType(new XY(i, j)).updateEnergy(collectedEnergy);
                            context.kill(mini);
                            return;
                        }
                    }
                }
            }
        }

    }

}


