package de.hsa.games.fatsquirrel.core;

public abstract class Squirrel extends Entity { // class Squirrel so Master&Mini are
    // next to eachother
    protected boolean stun;
    protected int stunCounter;

    Squirrel(int id, int energy, XY position) {
        super(id, energy, position);
        this.stun = false;
        this.stunCounter = 0;
    }

    public void stun() {
        stun = true;
    }

    public void cleanse() {
        stun = false;
    }

    public boolean getStun(){
        return stun;
    }

    public int getStunCounter(){
        return stunCounter;
    }

    public void increaseStunCounter(){
        stunCounter++;
    }

    public void resetStunCounter(){stunCounter = 0;}

    public void nextStep(EntityContext context) throws Exception {
    }
}
