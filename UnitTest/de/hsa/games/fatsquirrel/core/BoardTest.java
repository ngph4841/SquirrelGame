package de.hsa.games.fatsquirrel.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Freya on 28.05.2017.
 */
public class BoardTest {
    BoardConfig boardConfig = new BoardConfig(new XY(10,10),1,1,1,1);
    Board board = new Board(boardConfig);

    @Test
    public void getConfig() throws Exception {
        //return same settings
        assertEquals(boardConfig,board.getConfig());
    }

    @Test
    public void flatten() throws Exception {
        assertTrue(board.flatten() instanceof FlattenedBoard);
    }

    @Test
    public void getEntitySet() throws Exception {
        board.fillSet();
        //assertTrue(board.getList() instanceof EntitySet);
    }

    @Test
    public void fillSet() throws Exception {
        board.fillSet(); //full list of entities
        for(int i = 0; i < board.getList().size(); i++) {
            assertTrue(board.getList().get(i) instanceof Entity);
        }

        //1 stellesquirrel
        assertTrue(board.getList().get(0) instanceof Squirrel);

        //fills out the outer walls
        for(int i = 1; i < boardConfig.getWallCount(); i++){
            assertTrue(board.getList().get(i) instanceof Wall);
        }
    }

}