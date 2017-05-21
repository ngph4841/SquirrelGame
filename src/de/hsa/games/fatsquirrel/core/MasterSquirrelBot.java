package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.EntityType;

/**
 * Created by Freya on 19.05.2017.
 */
public class MasterSquirrelBot extends MasterSquirrel {
    public MasterSquirrelBot(int id, int energy, XY position) {
        super(id, energy, position);
    }

    @Override
    public void nextStep(EntityContext context){

    }

    class ControllerContextImpl implements ControllerContext {

        @Override
        public XY getViewLowerLeft() {
            return null;
        }

        @Override
        public XY getViewUpperRight() {
            return null;
        }

        @Override
        public EntityType getEntityAt(XY xy) {
            return null;
        }

        @Override
        public void move(XY direction) {

        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {

        }

        @Override
        public int getEnergy() {
            return 0;
        }
    }
}


