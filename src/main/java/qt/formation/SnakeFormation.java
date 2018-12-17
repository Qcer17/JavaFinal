package qt.formation;

import java.util.List;

import qt.gameSystem.Game;
import qt.gameSystem.GameSystem;
import qt.object.Creature;

public class SnakeFormation implements Formation {

	@Override
	public void format(Game game, List<? extends Creature> creatures, boolean left) {
		int numSoldier = creatures.size();
		int y = GameSystem.getM() / 2 - numSoldier / 2;
		int x = left ? 0 : GameSystem.getN() - 1;
		for (Creature c : creatures) {
			c.setIntLocation(x, y++);
		}
	}

}
