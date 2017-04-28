package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.console.GameImpl;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.XY;

public class Launcher {
	public static void main(String[] args)throws Exception{
		XY size = new XY(50,50);
		BoardConfig settings = new BoardConfig(size,5,5,10,10);
		Board board1 = new Board(settings);
		State state1 = new State(board1);
		Game game1 = new GameImpl(state1);
		game1.run();
	}
}
