package de.hsa.games.fatsquirrel.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Freya on 27.05.2017.
 */
public class MasterSquirrelTest {
    private MasterSquirrel master = new MasterSquirrel(1000,new XY(0,0));
    EntityContext entityContext;


    @Before
    public void setup() throws Exception {
    //entityContext = mock(EntityContext.class);
    }

    @Test
    public void nextStep() throws Exception {
        assertFalse(master.getStun()); //default value = false
        master.stun();
        assertTrue(master.getStun()); //after stun
        master.cleanse();
        assertFalse(master.getStun()); //after cleanse
    }

    @Test
    public void checkIfChild() throws Exception {
        MiniSquirrel child = master.spawnChild(100); //birth of child
        assertTrue(master.checkIfChild(child));
        assertFalse(master.checkIfChild(new Wall(10, new XY(10,10)))); //wall as child?
        //random MiniSquirrel
        assertFalse(master.checkIfChild(new MiniSquirrel(2001,200,new XY(20,10),2000)));
    }

    @Test
    public void moveMaster() throws Exception {
    }

    @Test
    public void spawnChild() throws Exception {
        MiniSquirrel child = master.spawnChild(100);

    }

}