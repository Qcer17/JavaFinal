package qt.formation;

import java.util.List;

import qt.gameSystem.Game;
import qt.gameSystem.GameSystem;
import qt.object.Creature;

public class ScaleFormation implements Formation {

	@Override
	public void format(Game game, List<? extends Creature> creatures, boolean left) {
		int numSoldier = creatures.size();
		int x = GameSystem.getM() / 2;
		int y = left ? GameSystem.getN() / 3 : GameSystem.getN() * 2 / 3;
		int cnt = 0;
		for (int i = 1;; i += 2) {
			int tx = x;
			for (int j = 0; j < i; j++) {
				creatures.get(cnt).setIntLocation(y, x);
				cnt++;
				x++;
				if (cnt == numSoldier)
					return;
			}
			x = tx - 1;
			y = left ? y - 1 : y + 1;
		}
	}

}
