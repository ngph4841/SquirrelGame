package de.hsa.games.fatsquirrel.core;

public interface EntityContext {
    /**
     * This method returns the size of the board.
     * @return XY with the Width as the x coordinate and Height as the y coordinate
     */
    public XY getSize();

    /**
     * This method checks if the move is valid and moves the entity if valid.
     * @param mini
     * @param moveDirection
     * @throws Exception
     */
    public void tryMove(MiniSquirrel mini, XY moveDirection) throws Exception;

    /**
     * This method checks if the move is valid and moves the entity if valid.
     * @param good
     * @param moveDirection
     * @throws Exception
     */
    public void tryMove(GoodBeast good, XY moveDirection) throws Exception;

    /**
     * This method checks if the move is valid and moves the entity if valid.
     * @param bad
     * @param moveDirection
     * @throws Exception
     */
    public void tryMove(BadBeast bad, XY moveDirection) throws Exception;

    /**
     * This method checks if the move is valid and moves the entity if valid.
     * @param master
     * @param moveDirection
     * @throws Exception
     */
    public void tryMove(MasterSquirrel master, XY moveDirection) throws Exception;

    /**
     * This method checks if the move is valid and moves the entity if valid.
     * @param position
     * @return
     */
    public Squirrel nearestPlayer(XY position);

    /**
     * This method deletes the entity out of the game.
     * @param entity which should be deleted
     * @throws Exception
     */
    public void kill(Entity entity) throws Exception;

    /**
     * This method deletes the current entity and initialises&adds a new entity to the game.
     * @param entity which should be deleted and added
     * @throws Exception
     */
    public void killAndReplace(Entity entity) throws Exception;

    /**
     * This method returns the Entity which is at the position XY
     * @param xy position in the board which is desired
     * @return null if there is no Entity, else the Entity which is at the position
     */
    public Entity getEntityType(XY xy);

    /**
     * This method returns the board.
     * @return the Board
     */
    public Board getBoard();

    /**
     * This method returns the StepCounter.
     * @return the StepCounter
     */
    public int getStepCounter();
}
