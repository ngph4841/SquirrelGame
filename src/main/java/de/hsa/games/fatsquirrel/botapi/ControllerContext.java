package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;
import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;

/**
 * This Interface is implemented in hidden classes in the specific bot implementations.
 * All stuff that a controller can use to interact with the game engine.
 * XY values are all in terms of the board coordinate system.
 * The view of the bot controller is limited to a certain rectangle of the board.
 * If the squirrel is far enough away from the border of the board, this is a square with side length 31 resp. 21
 * Created by Freya on 19.05.2017.
 */
public interface ControllerContext {
    public Entity getEntity();
    public EntityContext getEntityContext();

    /**
     * This method returns the lower left corner of the limited view.
     * Near the boarder this square is intersected with the board rectangle.
     * @return lower left corner of the view rectangle
     */
    XY getViewLowerLeft();

    /**
     * This method returns the upper right corner of the limited view.
     * Near the boarder this square is intersected with the board rectangle.
     * @return upper right corner of the view rectangle
     */
    XY getViewUpperRight();

    /**
     * This method returns the XY position of object
     * @return  my cell coordinates, i. e. the position of the controlled player entity
     */
    XY locate();

    /**
     *This method returns the EntityType of the entity at the position.
     *@param xy cell coordinates
     *@return the type of the entity at that position or none
     */
    EntityType getEntityAt(XY xy);

    /**
     * This method checks if the entity is the parent or a brethren.
     * @param xy cell coordinates
     * @return true, if entity at xy is my master or one of my minis resp.
     */
    boolean isMine(XY xy);

    /**
     * This method moves the bot.
     * @param direction one of XY.UP, XY.DOWN, ...
     * @throws Exception if the move is invalid
     */
    void move(XY direction) throws Exception;

    /**
     * This method spawns a MiniSquirrel child(MiniSquirrels cant use this of course).
     * @param direction one of XY.UP, XY.DOWN, ...
     * @param energy start energy of the min, at least 100
     * @throws Exception SpawnException if either direction or energy is invalid
     */
    void spawnMiniBot(XY direction, int energy) throws Exception;

    /**
     * This method is damages all the entities in the impact area.
     * (walls & family are excluded)
     * @param impactRadius radius of the impact circle
     * @throws Exception if a MasterSquirrel tries to implode
     */
    void implode(int impactRadius) throws Exception;

    /**
     * This method returns the energy of the bot.
     * @return the current energy of the bot entity
     */
    int getEnergy();

    /**
     * This method returns a direction vector to the master, returns
     * (0,0) if called by a MasterSquirrelBot
     * @return the direction where the master can be found
     */
    XY directionOfMaster();

    /**
     * This method returns the remaining steps in the curren round.
     * @return how many steps are left until the end of the current round
     */
    int getRemainingSteps();

    /**
     * This method shows the emotion of  the bot.
     * @param text the comment of the bot during fighting, e. g. ouch
     */
    void shout(String text);
}
