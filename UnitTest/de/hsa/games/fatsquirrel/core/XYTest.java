package de.hsa.games.fatsquirrel.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Freya on 27.05.2017.
 */
public class XYTest {
    private XY xy = new XY(10,8);

    @Test
    public void equalsTest() throws Exception {
        assertTrue(xy.equals(new XY(10,8)));
        assertFalse(xy.equals(new XY (19,4)));
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals(xy.toString(), "10/8");
    }

    @Test
    public void plus() throws Exception { //assert equals 10/10 != 10/10
        assertTrue(xy.plus(new XY(0,2)).equals(new XY(10,10)));
        assertTrue(xy.plus(new XY(0,-2)).equals(new XY(10,6)));
    }

    @Test
    public void minus() throws Exception {
        assertTrue(xy.minus(new XY(4,3)).equals(new XY(6,5)));
        assertTrue(xy.minus(new XY(-4,3)).equals(new XY(14,5)));
    }

    @Test
    public void times() throws Exception {
        assertTrue(xy.times(3).equals(new XY(30,24)));
        assertTrue(xy.times(-1).equals(new XY(-10,-8)));
    }

    @Test
    public void length() throws Exception {
        assertTrue(xy.length() == Math.sqrt(164));
    }

    @Test
    public void distanceFrom() throws Exception {
        double temp = Math.abs(Math.sqrt(Math.pow(5, 2) + Math.pow(3, 2)));
        assertTrue(xy.distanceFrom(new XY(5,5)) > 0); //pos dis
        assertTrue(xy.distanceFrom(new XY(5,5)) == temp);
    }

    @Test
    public void hashCodeTest() throws Exception {
        //
    }

}