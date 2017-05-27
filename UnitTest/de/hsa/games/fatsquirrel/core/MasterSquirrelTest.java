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
    entityContext = mock(EntityContext.class);
        doCallRealMethod().when(entityContext).tryMove(master, new XY(0,0));
    }

    @Test
    public void nextStep() throws Exception {
    }

    @Test
    public void checkIfChild() throws Exception {
    }

    @Test
    public void moveMaster() throws Exception {
    }

    @Test
    public void spawnChild() throws Exception {
    }

}