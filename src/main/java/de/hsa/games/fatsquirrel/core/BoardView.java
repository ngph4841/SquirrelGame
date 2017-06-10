package de.hsa.games.fatsquirrel.core;

/**
 * The BoardView Interface is used in the UI for the rendering of the board.
 */
public interface BoardView {
    /**
     *
     * @param y y-coordinate
     * @param x x-coordinate
     * @return null if the position is empty, else the Entity at the position
     */
    public Entity getEntityType(int y, int x);

    /**
     * This method returns the size of the board.
     * @return XY with the Width as the x coordinate and Height as the y coordinate
     */
    public XY getSize();
}
