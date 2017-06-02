package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.core.Character;

import java.util.ArrayList;
import java.util.Iterator;

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
        //this.board.getList().moveAll(context);        //call nextStep in all entities

        //loop nextStep call
        for(int i = 0; i < board.getList().size(); i++){
            if(board.getList().get(i) instanceof Character | board.getList().get(i) instanceof Squirrel){
                board.getList().get(i).nextStep(context);
            }
        }

    }
}
