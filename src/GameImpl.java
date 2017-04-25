
public class GameImpl extends Game{

	private State state;
	private XY direction;
	
	public GameImpl(State state){
		this.state = state;
		this.direction = new XY(0,0);
	}

	public void render() {//Spielzustand auf ausgabemedium
		UI output = new ConsoleUI();
		output.render(state.getBoardView());
	}

	public void processInput()throws Exception{//verarbeitet Benutzereingaben
		UI input = new ConsoleUI();
		direction = input.getCommand();
	}

	public void update()throws Exception {//verändert akt. Spielzustand
		state.update(direction);
		direction = new XY(0,0);
	}

	public void run()throws Exception{
		while (true) {
			render();
			processInput();
			update();
		}
	}

}
