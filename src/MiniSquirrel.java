

public class MiniSquirrel extends Squirrel { // extends squirrel like
												// mastersquirrel

	MiniSquirrel(int id, int energy, XY position) {
		super(id, energy, position);
	}
	
	public void nextStep(EntityContext context) throws Exception {
		XY moveDirection = new XY(0,0);
		context.tryMove(this, moveDirection);
		if(!stun){
			//later
		}
	}
}
