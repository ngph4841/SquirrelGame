package de.hsa.games.fatsquirrel.core;

/**
 * The squirrel class is abstract and has different inherited classes.
 * (MasterSquirrel,MiniSquirrel,HandOperatedMasterSquirrel)
 * The Bots inherit from their respected normal versions.
 * Contains the inherited values of the entity.class and a Stun(boolean)&StunCounter(int).
 */
public abstract class Squirrel extends Entity {
    protected boolean stun;
    protected int stunCounter;

    /**
     *Stun regulates the movement of the squirrel,
     * and the StunCounter regulates the Stun.
     * @param id id of the object
     * @param energy energy of the object
     * @param position position in the Board
     */
    Squirrel(int id, int energy, XY position) {
        super(id, energy, position);
        this.stun = false;
        this.stunCounter = 0;
    }

    /**
     * This method sets the stun to true.
     */
    public void stun() {
        stun = true;
    }

    /**
     * This method sets the stun to false.
     */
    public void cleanse() {
        stun = false;
    }

    /**
     * This method returns the squirrels stun state.
     * @return the squirrels stun state
     */
    public boolean getStun(){
        return stun;
    }

    /**
     * This method returns how many turns this squirrel has been stunned.
     * @return the StunCounter of the squirrel
     */
    public int getStunCounter(){
        return stunCounter;
    }

    /**
     * This method increases the StunCounter by 1.
     */
    public void increaseStunCounter(){
        stunCounter++;
    }

    /**
     * This method sets the StunCounter to 0.
     */
    public void resetStunCounter(){
        stunCounter = 0;
    }

    /**
     *
     * @param context EntityContext contains the logic of the squirrel
     * @throws Exception
     */
    public void nextStep(EntityContext context) throws Exception {
    }
}
