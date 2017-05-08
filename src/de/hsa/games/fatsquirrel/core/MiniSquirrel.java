package de.hsa.games.fatsquirrel.core;

import java.util.Random;

public class MiniSquirrel extends Squirrel { // extends squirrel like
    // mastersquirrel
    private int parentId;
    private int tempo;
    private int stunCounter;

    MiniSquirrel(int id, int energy, XY position, int parentId) {
        super(id, energy, position);
        this.parentId = parentId;
        this.tempo = 0;
        this.stunCounter = 0;
    }

    public int getParentId() {
        return parentId;
    }

    public void nextStep(EntityContext context) throws Exception {
        if (tempo++ == 0) {
            if (!stun) {
                Random rn = new Random();
                int x = 0;
                int y = 0;
                int range = 3;
                x = rn.nextInt(range) + (-1);
                y = rn.nextInt(range) + (-1);

                XY direction = new XY(x, y);

                context.tryMove(this, direction);
            } else {
                stunCounter++;
                if (stunCounter == 3) {
                    stunCounter = 0;
                    cleanse();
                }
            }
        }
        if (tempo >= 2) {
            tempo = 0;
        }
    }
}
