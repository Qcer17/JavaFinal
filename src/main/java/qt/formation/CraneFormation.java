package qt.formation;

import java.util.List;

import qt.gameSystem.Game;
import qt.gameSystem.GameSystem;
import qt.object.Creature;

public class CraneFormation implements Formation {

	@Override
	public void format(Game game, List<? extends Creature> creatures, boolean left) {
		int numSoldier = creatures.size();
		int x = GameSystem.getM() / 2 - numSoldier / 2,
				y = left ? GameSystem.getN() / 2 - 2 : GameSystem.getN() / 2 + 1;
		for (int i = 0; i < numSoldier / 2; i++) {
			creatures.get(i).setIntLocation(y, x);
			x++;
			y = left ? y - 1 : y + 1;
		}
		for (int i = numSoldier / 2; i < numSoldier; i++) {
			creatures.get(i).setIntLocation(y, x);
			x++;
			y = left ? y + 1 : y - 1;
		}
	}

}
