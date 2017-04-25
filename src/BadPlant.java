

public class BadPlant extends Entity {
	private static int energy = -100;

	BadPlant(int id, XY position) {
		super(id, energy, position);
	}

	public void nextStep(EntityContext context) {

	}
}
