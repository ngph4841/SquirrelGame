package de.hsa.games.fatsquirrel.core;

public interface EntityContext {
	public XY getSize();
	public void tryMove(MiniSquirrel mini, XY moveDirection)throws Exception;
	public void tryMove(GoodBeast good, XY moveDirection)throws Exception;
	public void tryMove(BadBeast bad, XY moveDirection);
	public void tryMove(MasterSquirrel master, XY moveDirection)throws Exception;
	public Squirrel nearestPlayer(XY position);
	
	public void kill(Entity entity)throws Exception;
	public void killAndReplace(Entity entity)throws Exception;
	public Entity getEntityType(XY xy);
}
