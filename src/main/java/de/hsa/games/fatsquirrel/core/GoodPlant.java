package de.hsa.games.fatsquirrel.core;

public class GoodPlant extends Entity {

    private static int energy = 100;

    GoodPlant(int id, XY position) {
        super(id, energy, position);
    }

    public void nextStep(EntityContext context) throws Exception {
    }
}
