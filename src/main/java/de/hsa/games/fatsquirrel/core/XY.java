package de.hsa.games.fatsquirrel.core;

/**
 * The XY class is immutable and contains a X&Y coordinate.
 * This class also contains static final constant vectors.
 */
public class XY {
    public final int x;
    public final int y;

    //static constants
    public static final XY ZERO_ZERO = new XY(0, 0);
    public static final XY RIGHT = new XY(1, 0);
    public static final XY LEFT = new XY(-1, 0);
    public static final XY UP = new XY(0, -1);
    public static final XY DOWN = new XY(0, 1);
    public static final XY RIGHT_UP = new XY(1, -1);
    public static final XY RIGHT_DOWN = new XY(1, 1);
    public static final XY LEFT_UP = new XY(-1, -1);
    public static final XY LEFT_DOWN = new XY(-1, 1);

    /**
     * The x&y params are final.
     * @param x x-coordinate in the 2-dimensional board
     * @param y y-coordinate in the 2-dimensional board
     */
    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method compares the values of this object to the xy param.
     * @param xy XY object to be compared
     * @return true if the values are equal, else false.
     */
    public boolean equals(XY xy) {
        return (x == xy.x && y == xy.y);
    }

    /**
     * This method returns the objects values in a String format.
     * @return the object values "x/y"
     */
    public String toString() { // toString
        return x + "/" + y;
    }

    /**
     * This method adds this object with the XY param and returns a new XY object.
     * @param xy XY object to be added
     * @return new XY object
     */
    public XY plus(XY xy) {
        return new XY(this.x + xy.x, this.y + xy.y);
    }

    /**
     * This method subtracts XY param from this object and returns a new XY object.
     * @param xy XY object to be added
     * @return new XY object
     */
    public XY minus(XY xy) {
        return new XY(this.x -xy.x,this.y - xy.y);
    }

    /**
     * This method multiplies this object times the factor.
     * @param factor the multiply factor
     * @return new XY object
     */
    public XY times(int factor) {
        return new XY(x*factor,y*factor);
    }

    public double length() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    /**
     * This method calculates the euklidian distance between this object and the XY param.
     * @param xy a second coordinate pair
     * @return the euklidian distance (pythagoras)
     */
    public double distanceFrom(XY xy) {
        return Math.abs(Math.sqrt(Math.pow(x - xy.x, 2) + Math.pow(y -xy.y, 2)));
    }

    /**
     * This method returns the hashcode of this object.
     * @return hashcode of this object
     */
    public int hashCode() {
        return this.hashCode();
    }

}
