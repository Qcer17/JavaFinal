package qt.attribute;

/**
 * Enumerate 9 directions in CVM.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public enum Direction {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), UPRIGHT(1, -1), UPLEFT(-1, -1), DOWNRIGHT(1, 1), DOWNLEFT(-1,
			1), CENTER(0, 0);

	private final int dx;
	private final int dy;

	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void updatePos(Position p) {
		p.set(p.getX() + dx, p.getY() + dy);
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public String toString() {
		return dx + "," + dy;
	}
}
