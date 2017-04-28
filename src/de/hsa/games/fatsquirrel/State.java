package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;

public class State {
	private int highscore;
	private Board board;
	private FlattenedBoard flattenedBoard;
	
	public State(Board board1)throws Exception{
		this.highscore = 10000;
		this.board = board1;
		this.flattenedBoard = new FlattenedBoard(board);
	}
	
	public Board getBoard(){
		return board;
	}
	
	public BoardView getBoardView(){
		return flattenedBoard;
	}
	
	public void update()throws Exception{
		EntityContext context = (EntityContext) flattenedBoard;
		board.getEntitySet().moveAll(context);		//call nextStep in all entities
		flattenedBoard = (FlattenedBoard) board.flatten();	//update Boardview
	}

}
