package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;
import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;

/**
 * Created by Freya on 19.05.2017.
 */
public interface ControllerContext {
    //    public XY getViewLowerLeft();
//    public XY getViewUpperRight();
//    public EntityType getEntityAt(XY xy);
//    public void move(XY direction) throws Exception;
//    public void spawnMiniBot(XY direction, int energy) throws NotEnoughEnergyException;
//    public int getEnergy();
    public Entity getEntity();
    public EntityContext getEntityContext();

    /**
     * All stuff that a controller can use to interact with the game engine.
     * XY values are all in terms of the board coordinate system.
     * The view of the bot controller is limited to a certain rectangle of the board.
     * If the squirrel is far enough away from the border of the board, this is a square with side length 31 resp. 21
     * Near the boarder this square is intersected with the board rectangle.
     */
    // @return lower left corner of the view rectangle
    XY getViewLowerLeft();

    //@return upper right corner of the view rectangle
    XY getViewUpperRight();

    // @return my cell coordinates, i. e. the position of the controlled player entity
    XY locate();

    //@param xy : cell coordinates
    //@return the type of the entity at that position or none
    //@throws OutOfViewException if xy is outside the view
    EntityType getEntityAt(XY xy);

    // @param xy : cell coordinates
    // @return true, if entity at xy is my master or one of my minis resp.
    // @throws OutOfViewException if xy is outside the view
    boolean isMine(XY xy);

    //@param direction : one of XY.UP, XY.DOWN, ...
    // XY.ZERO_ZERO means I want to pause
    void move(XY direction) throws Exception;

    // @param direction : one of XY.UP, XY.DOWN, ...
    // @param energy : start energy of the min, at least 100
    // @throws SpawnException if either direction or energy is invalid
    void spawnMiniBot(XY direction, int energy) throws NotEnoughEnergyException;

    // Very destructive event (see specification for details).
    // Can only be called for mini bot, otherwise exception.
    // @param impactRadius : radius of the impact circle
    void implode(int impactRadius) throws Exception;

    // @return the current energy of the player entity
    int getEnergy();

    // @return the direction where the master can be found
    XY directionOfMaster();

    // @return how many steps are left until the end of the current round
    long getRemainingSteps();

    // implementation is optional
    // @param text : the comment of the bot during fighting, e. g. ouch
    default void shout(String text) {
    }
}
