package de.hsa.games.fatsquirrel.core;

public abstract class Squirrel extends Entity { // class Squirrel so Master&Mini are
												// next to eachother
	protected boolean stun;

	Squirrel(int id, int energy, XY position) {
		super(id, energy, position);
		this.stun = false;
	}
	
	public void stun(){
		stun = true;
	}
	
	public void cleanse(){
		stun = false;
	}

	public void nextStep(EntityContext context) throws Exception {
	}
}
