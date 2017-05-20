package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.core.XY;

/**
 * Created by Freya on 19.05.2017.
 */
public interface ControllerContext {
    public XY getViewLowerLeft();
    public XY getViewUpperRight();
    public EntityType getEntityAt(XY xy);
    public void move(XY direction);
    public void spawnMiniBot(XY direction, int energy);
    public int getEnergy();
}
