package qt.formation;

import java.util.List;

import qt.gameSystem.Game;
import qt.gameSystem.GameSystem;
import qt.object.Creature;

public class SquareFormation implements Formation {

	@Override
	public void format(Game game, List<? extends Creature> creatures, boolean left) {
		int numSoldier = creatures.size();
		int x = GameSystem.getM() / 2;
		int y = left ? GameSystem.getN() / 2 - 1 : GameSystem.getN() / 2 + 1;
		creatures.get(0).setIntLocation(y, x);
		y = left ? y - 1 : y + 1;
		int cnt = 1;
		int cols = (numSoldier - 1) / 2, gap = 0;
		for (int i = 0; i <= (cols - 1) / 2; i++) {
			gap = i + 1;
			creatures.get(cnt++).setIntLocation(y, x - gap);
			creatures.get(cnt++).setIntLocation(y, x + gap);
			y = left ? y - 1 : y + 1;
		}
		gap = (cols % 2 == 0) ? gap : gap - 1;
		for (int i = (cols - 1) / 2 + 1; i < cols; i++) {
			creatures.get(cnt++).setIntLocation(y, x - gap);
			creatures.get(cnt++).setIntLocation(y, x + gap);
			y = left ? y - 1 : y + 1;
			gap--;
		}
		if (cnt < numSoldier) {
			creatures.get(cnt).setIntLocation(y, x);
		}
	}
}
