

public abstract class Game {
	private State state;
	
	protected Game(){
	}

	protected void render() {//Spielzustand auf ausgabemedium

	}

	protected void processInput()throws Exception{//verarbeitet Benutzereingaben

	}

	protected void update()throws Exception {//verändert akt. Spielzustand

	}

	public void run()throws Exception{
		while (true) {
			render();
			processInput();
			update();
		}
	}
}
