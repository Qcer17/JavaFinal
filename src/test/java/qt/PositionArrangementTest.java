package qt;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import qt.attribute.Direction;
import qt.attribute.Position;
import qt.gameSystem.GameSystem;

public class PositionArrangementTest {
	private boolean[][] occupied = new boolean[GameSystem.getN()][GameSystem.getM()];

	@Test
	public void fillNextDirectionTest() {
		for (int i = 0; i < occupied.length; i++) {
			for (int j = 0; j < occupied[0].length; j++) {
				occupied[i][j] = false;
			}
		}
		Position position = new Position(0, 0);
		int cnt = 0;
		for (int i = 0; i < occupied.length; i++) {
			for (int j = 0; j < occupied[0].length; j++) {
				Direction direction = fillNextDirection(position);
				cnt++;
				if (direction == Direction.CENTER) {
					assertTrue(cnt == GameSystem.getN() * GameSystem.getM());
				} else {
					occupied[(int) (position.getX() + direction.getDx())][(int) (position.getY()
							+ direction.getDy())] = true;
				}
			}
		}

	}

	private Direction fillNextDirection(Position p) {
		int[] dx = new int[] { 1, 0, -1, 0 };
		int[] dy = new int[] { 0, 1, 0, -1 };
		Direction[] directions = new Direction[] { Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP };
		for (int i = 0; i < 4; i++) {
			int tx = (int) p.getX() + dx[i];
			int ty = (int) p.getY() + dy[i];
			if ((tx < 0 || tx >= GameSystem.getN()) || (ty < 0 || ty >= GameSystem.getM()))
				continue;
			if (occupied[ty][tx])
				continue;
			return directions[i];
		}
		return Direction.CENTER;
	}
}
