package de.hsa.games.fatsquirrel.core;


public class XY { // immutable class
    private final int x;
    private final int y;
    public static final XY ZERO_ZERO = new XY(0, 0);
    public static final XY RIGHT = new XY(1, 0);
    public static final XY LEFT = new XY(-1, 0);
    public static final XY UP = new XY(0, -1);
    public static final XY DOWN = new XY(0, 1);
    public static final XY RIGHT_UP = new XY(1, -1);
    public static final XY RIGHT_DOWN = new XY(1, 1);
    public static final XY LEFT_UP = new XY(-1, -1);
    public static final XY LEFT_DOWN = new XY(-1, 1);

    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    } //can delete this 2 ?

    public int getY() {
        return y;
    } //not in refacortin :(

    public boolean euqals(XY xy) {
        return (x == xy.getX() && y == xy.getY());
    }

    public String toString() { // toString
        return x + "/" + y;
    }

    public XY plus(XY xy) {
        return new XY(this.x + xy.getX(), this.y + xy.getY());
    }

    public XY minus(XY xy) {
        return new XY(this.x -xy.getX(),this.y - xy.getY());
    }

    public XY times(int factor) {
        return new XY(x*factor,y*factor);
    }

    public double length() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    /**
     *
     * @param xy a second coordinate pair
     * @return the euklidian distance (pythagoras)
     */
    public double distanceFrom(XY xy) {
        return Math.sqrt(Math.pow(x - xy.getX(), 2) + Math.pow(y -xy.getY(), 2));
    }

    public int hashCode() {
        return this.hashCode();
    }

}
