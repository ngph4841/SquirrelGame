package de.hsa.games.fatsquirrel.core;

public class MiniSquirrel extends Squirrel { // extends squirrel like
    // mastersquirrel
    private int parentId;

    MiniSquirrel(int id, int energy, XY position, int parentId) {
        super(id, energy, position);
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void nextStep(EntityContext context) throws Exception {
        context.tryMove(this, position);
    }
}
