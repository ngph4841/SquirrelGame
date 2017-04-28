package de.hsa.games.fatsquirrel.core;

public class MiniSquirrel extends Squirrel { // extends squirrel like
												// mastersquirrel

	MiniSquirrel(int id, int energy, XY position) {
		super(id, energy, position);
	}
	
	public void nextStep(EntityContext context) throws Exception {
		context.tryMove(this, position);
	}
}
