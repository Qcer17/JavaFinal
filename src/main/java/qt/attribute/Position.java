package qt.attribute;

/**
 * Positions in CVM.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class Position {
	private double x;
	private double y;

	public Position(double x, double y) {
		set(x, y);
	}

	public Position(Position p) {
		set(p);
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void set(Position p) {
		x = p.getX();
		y = p.getY();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public static int blockDistance(Position p1, Position p2) {
		return (int) (Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY()));
	}

	public boolean equalsX(Position p) {
		return p.getX() == x;
	}

	public boolean equalsY(Position p) {
		return p.getY() == y;
	}

	public boolean equals(Position p) {
		return equalsX(p) && equalsY(p);
	}

	public Direction relativeDirectionTo(Position p) {
		if (equalsX(p)) {
			if (y > p.getY())
				return Direction.DOWN;
			if (y < p.getY())
				return Direction.UP;
		}
		if (equalsY(p)) {
			if (x > p.getX())
				return Direction.RIGHT;
			if (x < p.getX())
				return Direction.LEFT;
		}
		if (Math.abs(x - p.getX()) == Math.abs(y - p.getY())) {
			if (x > p.getX()) {
				if (y > p.getY()) {
					return Direction.DOWNRIGHT;
				} else {
					return Direction.UPRIGHT;
				}
			}
			if (x < p.getX()) {
				if (y > p.getY()) {
					return Direction.DOWNLEFT;
				} else {
					return Direction.UPLEFT;
				}
			}
		}
		return Direction.CENTER;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}
}
