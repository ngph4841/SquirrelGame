package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;

public class State {
    private int highscore;
    private Board board;

    public State(Board board) throws Exception {
        this.highscore = 10000;
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public BoardView getBoardView()throws Exception {
        return board.flatten();
    }

    public void update() throws Exception {
        EntityContext context = board.flatten();
        this.board.getEntitySet().moveAll(context);        //call nextStep in all entities
    }

}
