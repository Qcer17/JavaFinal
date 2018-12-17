package qt.formation;

import java.util.List;

import qt.gameSystem.Game;
import qt.gameSystem.GameSystem;
import qt.object.Creature;

public class CrossFormation implements Formation {

	@Override
	public void format(Game game, List<? extends Creature> creatures, boolean left) {
		int numSoldier = creatures.size();
		int x = GameSystem.getM() - numSoldier;
		int y = left ? GameSystem.getN() / 4 : GameSystem.getN() * 3 / 4;
		for (int i = 0; i < numSoldier / 2; i++) {
			creatures.get(i).setIntLocation(y, x);
			x += 2;
		}
		y++;
		x -= 3;
		for (int i = numSoldier / 2; i < numSoldier; i++) {
			creatures.get(i).setIntLocation(y, x);
			x -= 2;
		}
	}

}
