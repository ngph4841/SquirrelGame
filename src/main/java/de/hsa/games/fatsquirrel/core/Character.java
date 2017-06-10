package de.hsa.games.fatsquirrel.core;

/**
 * The character class is abstract and has different inherited classes.
 * (GoodBeast, BadBeast)
 * Contains the inherited values of the entity.class and a TurnCounter(int).
 */
public abstract class Character extends Entity {
    protected int turnCounter;

    /**
     * The TurnCounter is a regulator for the speed of the character.
     * @param id id of the object
     * @param energy energy of the object
     * @param position position in the Board
     */
    Character(int id, int energy, XY position) {
        super(id, energy, position);
        turnCounter = 0;
    }

    /**
     * @param context EntityContext contains the logic of the character
     * @throws Exception
     */
    public void nextStep(EntityContext context) throws Exception {
    }
}

