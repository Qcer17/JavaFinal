package qt;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import qt.attribute.Direction;
import qt.attribute.Position;

public class PositionTest {
	@Test
	public void relativePositionTest() {
		Random random = new Random(8);
		for (int i = 0; i < 10000000; i++) {
			Position position1 = new Position(random.nextInt(20), random.nextInt(20));
			Position position2 = new Position(random.nextInt(20), random.nextInt(20));
			if (!position1.equals(position2)) {
				Direction direction = position1.relativeDirectionTo(position2);
				if (direction != Direction.CENTER) {
					for (int j = 0; j < 20; j++) {
						position2.set(position2.getX() + direction.getDx(), position2.getY() + direction.getDy());
						if (position1.equals(position2))
							break;
					}
					assertTrue(position1.equals(position2));
				}
			}
		}
	}
}
