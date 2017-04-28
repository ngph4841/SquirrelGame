package de.hsa.games.fatsquirrel.core;

public class Wall extends Entity {
	private static int energy = -10;

	Wall(int id, XY position) {
		super(id, energy, position);
	}

	public void nextStep(EntityContext context) throws Exception {
	}
}