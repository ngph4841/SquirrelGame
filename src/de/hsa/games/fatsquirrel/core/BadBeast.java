package de.hsa.games.fatsquirrel.core;
import java.util.Random;

public class BadBeast extends Entity {
	private static int startEnergy = -10;
	private int bite = 7;

	BadBeast(int id, XY position) {
		super(id, startEnergy, position);

	}
	
	public int getBite(){
		return bite;
	}
	
	public void bite(){
		bite--;
	}

	public void nextStep(EntityContext context)throws Exception {
		context.tryMove(this, moveBeast()); 	//check if vector viable&move
	}

	public static XY moveBeast() {	//returns moveDircetion Vector
		Random rn = new Random();
		int x = 0;
		int y = 0;
		int range = 3;
		x = rn.nextInt(range) + (-1);
		y = rn.nextInt(range) + (-1);

		return new XY(x, y);
	}
}
