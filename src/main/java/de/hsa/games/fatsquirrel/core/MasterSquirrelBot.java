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

    public MiniSquirrelBot spawnChild(int energy) throws NotEnoughEnergyException {
        if (this.energy <= energy) {
            throw new NotEnoughEnergyException();
        }
        XY childStartPos = position.plus(new XY(0,1)); // spawn next to mother
        childrenCounter++;                            // new child & id of child is mothersId + child#
        MiniSquirrelBot child = new MiniSquirrelBot(this.id + childrenCounter, energy, childStartPos, this.id);
        int[] temp = new int[childrenCounter];                            // new array for childrenId
        for (int i = 0; i < childrenCounter - 2; i++) {                // transfer id of children
            temp[i] = childrenId[i];
        }
        temp[childrenCounter - 1] = child.getId();                        // save id of this new child
        childrenId = temp;
        updateEnergy(-energy);
        return child;
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
            int x = position.x - 15;
            int y = position.y + 15;
            if (x < 0) { //links klein
                x = 0;
            }
            if (y > size.y) { //unten groß
                y = size.y - 1;
            }
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            XY size = context.getSize();
            int x = position.x + 15;
            int y = position.y - 15;
            if (x > size.x) { //rechts groß
                x = size.x - 1;
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
            return master.checkIfChild(context.getEntityType(xy));
        }

        @Override
        public void move(XY direction) throws Exception {
            context.tryMove(master, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) throws Exception {
            int spaceCounter = 0;
            if(energy >= master.getEnergy()){
                throw new NotEnoughEnergyException();
            }
            for(int i = 0; i <= 8; i++){    //check space around master
                switch (i){
                    case 0:
                        if(getEntityAt(position.plus(new XY(1,-1)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                    case 1:
                        if(getEntityAt(position.plus(new XY(0,1)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                    case 2:
                        if(getEntityAt(position.plus(new XY(1,0)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                    case 3:
                        if(getEntityAt(position.plus(new XY(1,1)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                    case 4:
                        if(getEntityAt(position.plus(new XY(0,-1)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                    case 5:
                        if(getEntityAt(position.plus(new XY(-1,0)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                    case 6:
                        if(getEntityAt(position.plus(new XY(-1,-1)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                    case 7:
                        if(getEntityAt(position.plus(new XY(-1,1)))!= EntityType.NONE){
                            spaceCounter++;
                        }
                        break;
                }
            }
            if(spaceCounter >= 8){
                throw new SpawnException();
            }
            MiniSquirrelBot child = spawnChild(energy);
            child.setPosition(position.plus(direction));
            context.getBoard().getEntitySet().plus(child); //save child
        }

        @Override
        public void implode(int impactRadius) {
        System.out.println("I can't, i'm too old for this");
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


