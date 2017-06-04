package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.util.MainLogger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by Freya on 27.05.2017.
 */
public class FlattenedBoardTest {
    private FlattenedBoard fSpy;
    Board board;

    @Before
    public void setup() throws Exception {
        board = new Board(new BoardConfig(new XY(10,10),1,1,1,1,1000,
                "de.hsa.games.fatsquirrel.botimpls.BotControllerMaster","de.hsa.fatsquirrel.botimpls.BotControllerMini"),0);
        FlattenedBoard flattenedBoard = new FlattenedBoard(board);
        fSpy = Mockito.spy(flattenedBoard); //Mockito fSpy returning specific cases

        //moving against a Wall & null
        Mockito.when(fSpy.getEntityType(0,0)).thenReturn(new Wall(0,new XY(0,0))); //mock method with spy
        Mockito.when(fSpy.getEntityType(1,1)).thenReturn(null);
        Mockito.when(fSpy.getEntityType(2,2)).thenReturn(null);

        //refreshMethod
        Mockito.doNothing().when(fSpy).refresh();

        //checking if plant gets eaten
        Mockito.when(fSpy.getEntityType(3,3)).thenReturn(new GoodPlant(10,new XY(3,3)));
        Mockito.doNothing().when(fSpy).killAndReplace(new GoodPlant(10,new XY(3,3)));
        Mockito.doNothing().when(fSpy).kill(new GoodPlant(10,new XY(3,3)));

        //check nearestPlayer
        Mockito.when(fSpy.getEntityType(4,4)).thenReturn(new MasterSquirrel(10,1000,new XY(4,4)));
    }

    @Test
    public void getEntityType() throws Exception {
        setup();
        assertTrue(fSpy.getEntityType(0,0).equals(new Wall(0,new XY(0,0))));
        assertEquals(fSpy.getEntityType(1,1),null);
    }

    @Test
    public void tryMove() throws Exception {
        setup();
        MasterSquirrel master = new MasterSquirrel(100,new XY(2,2));

        //fSpy.tryMove(master,new XY(1,1));
        //Mockito.verify(fSpy,Mockito.atLeastOnce()).move(master,new XY(2,2)); //same but diff.

        //moves onto plant position, eats plant and new plant spawns
        fSpy.tryMove(master,new XY(1,1));
        assertTrue(new XY(3,3).equals(master.getPosition()));

        //stun works
        master.setPosition(new XY(1,1));
        assertFalse(master.getStun()); //before run into a wall
        fSpy.tryMove(master,new XY(-1,-1));
        assertTrue(master.getStun());
        //after
        assertTrue(new XY(1,1).equals(master.getPosition()));
        fSpy.tryMove(master, new XY(1,1)); //1turn
        assertTrue(master.getStun());
        fSpy.tryMove(master, new XY(1,1));  //2turn
        assertTrue(master.getStun());
        fSpy.tryMove(master, new XY(1,1));  //3turn
        assertTrue(master.getPosition().equals(new XY (1,1))); //didnt move
        assertFalse(master.getStun()); //not stunned anymore
    }


    @Test
    public void nearestPlayer() throws Exception { //alg for finding squirrel works
        setup();
        MasterSquirrel masterSquirrel = (MasterSquirrel) fSpy.nearestPlayer(new XY(0,0));
        assertTrue(masterSquirrel.equals(new MasterSquirrel(10,1000,new XY(4,4))));
    }

    @Test
    public void killAndReplace() throws Exception {
        GoodPlant goodPlant = new GoodPlant(10,new XY(10,10));
        fSpy.killAndReplace(goodPlant);
        assertFalse(goodPlant.getPosition().equals(new XY(10,10)));
    }

    @Test
    public void kill() throws Exception {
        fSpy.kill(new GoodPlant(10,new XY(10,10))); //entityset test?
    }

    @Test
    public void move() throws Exception {
        setup();
        MasterSquirrel master = new MasterSquirrel(100,new XY(1,1));

        //move() works on emptyspaces
        fSpy.move(master,new XY(2,2));
        assertTrue(new XY(2,2).equals(master.getPosition()));

        //cant go into negative array
        fSpy.move(master,new XY(-1,-1));
        assertFalse(new XY(-1,-1).equals(master.getPosition()));
    }
}