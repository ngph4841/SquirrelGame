package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;
import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.XY;

/**
 * Created by Freya on 19.05.2017.
 */
public interface ControllerContext {
    public XY getViewLowerLeft();
    public XY getViewUpperRight();
    public EntityType getEntityAt(XY xy);
    public void move(XY direction) throws Exception;
    public void spawnMiniBot(XY direction, int energy) throws NotEnoughEnergyException;
    public int getEnergy();
    public Entity getEntity();
}
