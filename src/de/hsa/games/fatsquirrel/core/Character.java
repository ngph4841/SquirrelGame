package de.hsa.games.fatsquirrel.core;

public abstract class Character extends Entity{
	protected int turnCounter;

	Character(int id, int energy, XY position) {
		super(id,energy,position);
		turnCounter = 0;
	}
	
	public void nextStep(EntityContext context)throws Exception{
		
	}
}

