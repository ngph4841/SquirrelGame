package de.hsa.games.fatsquirrel.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nghia Pham on 29.05.2017.
 */
public class EntitySetTest {
    private EntitySet entitySet;

    @Test
    public void getEntity() throws Exception {
        entitySet = new EntitySet(2);
        MasterSquirrel masterSquirrel = new MasterSquirrel(10, new XY(0, 0));
        entitySet.add(masterSquirrel);
        assertEquals(masterSquirrel, entitySet.get(0));
    }

    @Test
    public void length() throws Exception {
        entitySet = new EntitySet(10);
        //size
        assertEquals(10, entitySet.size());
        assertFalse(entitySet.size() == 8);
    }

    @Test
    public void add() throws Exception {
        entitySet = new EntitySet(2);
        //entity is added into the list
        Wall wall = new Wall(-1, new XY(0, 0));
        entitySet.add(wall);
        assertEquals(entitySet.get(0), wall);

        //can't add same entity twice
        entitySet.add(wall);
        assertTrue(entitySet.get(1) == null);
    }

    @Test
    public void remove() throws Exception {
        entitySet = new EntitySet(2);
        //entity is added into the list
        Wall wall = new Wall(-1, new XY(0, 0));
        entitySet.add(wall);
        GoodPlant goodPlant = new GoodPlant(10, new XY(10, 10));

        //remove goodplant 2 -> 1
        entitySet.remove(goodPlant);
        assertEquals(1, entitySet.size());
    }

}