package de.hsa.games.fatsquirrel.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by Nghia Pham on 29.05.2017.
 */
public class BadBeastTest {
    BadBeast badBeast = new BadBeast(10, new XY(10,10));
    EntityContext eSpy;
    @Before
    public void setUp() throws Exception {
        BoardConfig boardConfig = new BoardConfig(new XY (10,10),1,1,1,1);
        Board board = new Board(boardConfig);
        FlattenedBoard flattenedBoard = new FlattenedBoard(board);
        eSpy = Mockito.spy(flattenedBoard);

        Mockito.when(eSpy.nearestPlayer(badBeast.getPosition())).thenReturn(new MasterSquirrel(1,1,new XY(1,1)));
        Mockito.when(eSpy.getEntityType(new XY (9,9))).thenReturn(null);
    }

    @Test
    public void getBite() throws Exception {
        assertTrue(badBeast.getBite() == 7);
        badBeast.bite();
        badBeast.bite();
        assertTrue(badBeast.getBite() == 5);
        for(int i = 0; i < 5; i++) {
            badBeast.bite();
        }
        assertTrue(badBeast.getBite() == 0);
    }

    @Test
    public void nextStep() throws Exception {
        setUp();
        XY position = badBeast.getPosition();
        badBeast.nextStep(eSpy);
        assertFalse(position.equals(badBeast.getPosition()));
    }

    @Test
    public void moveBeast() throws Exception {
        boolean pb1 = false;
        boolean pb2 = false;
        boolean pb3 = false;
        boolean pb4 = false;
        boolean pb5 = false;
        boolean pb6 = false;
        boolean pb7 = false;
        boolean pb8 = false;
        boolean pb9 = false;

        for(int i = 0; i < 250; i++){
            XY position = badBeast.moveBeast();

            if(position.equals(new XY(-1, -1))){
                pb1 = true;
            }
            else if(position.equals(new XY(-1, 0))){
                pb2 = true;
            }
            else if(position.equals(new XY(-1, 1))){
                pb3 = true;
            }
            else if(position.equals(new XY(0, -1))){
                pb4 = true;
            }
            else if(position.equals(new XY(0, 0))){
                pb5 = true;
            }
            else if(position.equals(new XY(0, 1))){
                pb6 = true;
            }
            else if(position.equals(new XY(1, -1))){
                pb7 = true;
            }
            else if(position.equals(new XY(1, 0))){
                pb8 = true;
            }
            else if(position.equals(new XY(1, 1))){
                pb9 = true;
            }
        }
        assertTrue(pb1 && pb2 && pb3 && pb4 && pb5 && pb6 && pb7 && pb8 && pb9);
    }

}