


public class XY { // immutable class
	private final int x;
	private final int y;

	XY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean euqals(XY o) {
		return x == o.getX() && y == o.getY();
	}

	public String toString() { // toString
		return x + "/" + y;
	}

}
