package qt.formation;

import java.util.List;

import qt.gameSystem.Game;
import qt.object.Creature;

/**
 * Every formation class should implement this interface.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public interface Formation {
	public void format(Game game, List<? extends Creature> creatures, boolean left);
}
