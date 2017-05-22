package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.*;

/**
 * Created by Freya on 19.05.2017.
 */
public class MiniSquirrelBot extends MiniSquirrel {
    BotControllerFactory botControllerFactory;
    MiniSquirrelBot(int id, int energy, XY position, int parentId) {
        super(id, energy, position, parentId);
        BotControllerFactory botControllerFactory = new BotControllerFactoryImpl();
    }

    @Override
    public void nextStep(EntityContext context)throws Exception{
        ControllerContext controllerContext = new ControllerContextImpl(context, this);
        BotController botController = botControllerFactory.createMiniBotController();
        botController.nextStep(controllerContext);
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
            int x = position.getX() - 10;
            int y = position.getY() + 10;
            if (x < 0) { //links klein
                x = 0;
            }
            if (y > size.getY()-1) { //unten groß
                y = size.getY();
            }
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            XY size = context.getSize();
            int x = position.getX() + 10;
            int y = position.getY() - 10;
            if (x > size.getX() - 1) { //rechts groß
                x = size.getX();
            }
            if (y < 0) { //oben klein
                y = 0;
            }
            return new XY(x, y);
        }

        @Override
        public EntityType getEntityAt(XY xy) {
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
        public Entity getEntity() {
            return mini;
        }

        @Override
        public EntityContext getEntityContext() {
            return context;
        }

        public void explode(int impactRadius) throws Exception { //double? & TODO fix this ugly child plz ....
            double impactArea = impactRadius * impactRadius * 3.14;
            // anders 2 oder 10 bearbeiten
            int x = mini.getPosition().getX();
            int y = mini.getPosition().getY();
            Entity[] entities = new Entity[(int) impactArea];
            int entitiesAmount = 0;

            int counter = (impactRadius - 1) * 2;
            for (int i = 1; i <= impactRadius; i++) {
                for (int j = counter; j > 0; j--) {
                    XY temp = new XY(x + j, y + i);
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
                counter -= 2;
            }

            for (int i = 1; i < impactRadius; i++) {
                switch (getEntityAt(new XY(x + i, y))) {
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
                        entities[entitiesAmount++] = context.getEntityType(new XY(x + i, y));
                }
            }

            for (int i = -1; i > -1 * impactRadius; i--) {
                switch (getEntityAt(new XY(x + i, y))) {
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
                        entities[entitiesAmount++] = context.getEntityType(new XY(x + i, y));
                }
            }

            counter = (impactRadius - 1) * 2;
            for (int i = -1; i >= -1*impactRadius; i--) {
                for (int j = counter; j > 0; j--) {
                    XY temp = new XY(x + j, y + i);
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
                counter -= 2;
            }

            int collectedEnergy = 0;
            for(int i = 0; i < entitiesAmount; i++) {
                double distance = Math.sqrt(Math.pow(entities[i].getPosition().getX(),2) + Math.pow(entities[i].getPosition().getY(),2));
                double energyLoss = 200 * (mini.getEnergy() / impactArea) * (1 - distance / impactRadius);
                //id check verwandte
                int deltaEnergy = entities[i].getEnergy() - (int) energyLoss;
                switch(getEntityAt(entities[i].getPosition())){
                    case BAD_BEAST:
                    case BAD_PLANT:
                        deltaEnergy = entities[i].getEnergy() + (int) energyLoss;
                        if (deltaEnergy >= 0){
                            context.killAndReplace(entities[i]);
                        }else{
                            entities[i].updateEnergy((int) energyLoss);
                        }
                        break;
                    case GOOD_PLANT:
                    case GOOD_BEAST:
                        if (deltaEnergy >= 0) { //entity mehr energy als energyloss / perfekt
                            collectedEnergy += (int) energyLoss;
                        } else { //entity weniger engery als energyloss
                            collectedEnergy += entities[i].getEnergy();
                        }
                        entities[i].updateEnergy((int)(-1*energyLoss));
                        if(entities[i].getEnergy() <= 0){
                            context.killAndReplace(entities[i]);
                        }
                        break;
                    case MINI_SQUIRREL:
                        MiniSquirrel temp = (MiniSquirrel) entities[i];
                        if(temp.getParentId() != mini.getParentId()){
                            if (deltaEnergy >= 0) { //entity mehr energy als energyloss / perfekt
                                collectedEnergy += (int) energyLoss;
                            } else { //entity weniger engery als energyloss
                                collectedEnergy += entities[i].getEnergy();
                            }
                            entities[i].updateEnergy((int)(-1*energyLoss));
                            if(temp.getEnergy() <= 0){
                                context.kill(temp);
                            }
                        }
                        break;
                    case MASTER_SQUIRREL:
                        collectedEnergy += energyLoss;
                        entities[i].updateEnergy((int)(-1*energyLoss));
                        break;
                }
            }
            //find parent and give him energy
            for(int i = 0; i < context.getSize().getX(); i++){
                for( int j = 0; j < context.getSize().getY(); j++){
                    if(context.getEntityType(new XY(i,j)).getId() == mini.getParentId()){
                        context.getEntityType(new XY(i,j)).updateEnergy(collectedEnergy);
                        context.kill(mini);
                        return;
                    }
                }
            }
        }


    }

}


