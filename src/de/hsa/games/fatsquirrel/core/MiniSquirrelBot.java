package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.EntityType;

/**
 * Created by Freya on 19.05.2017.
 */
public class MiniSquirrelBot extends MiniSquirrel {
    MiniSquirrelBot(int id, int energy, XY position, int parentId) {
        super(id, energy, position, parentId);
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


