

import java.util.Random;

public class GoodBeast extends Entity {
	private static int startEnergy = 200;

	GoodBeast(int id, XY position) {
		super(id, startEnergy, position);

	}

	public void nextStep(EntityContext context) {
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
