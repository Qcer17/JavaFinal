package qt.object;

import javafx.scene.image.ImageView;
import qt.attribute.Direction;
import qt.gameSystem.GameSystem;

/**
 * Bullets, aka, attack balls, to attack enemies.
 * In order to reduce overhead, it's not implemented
 * as individual thread and all bullets are processed 
 * in a single thread called AttackManager.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class Bullet extends CVMObject {
	private Direction direction;
	private boolean isCalabash;
	private int ID;

	public Bullet(int ID, boolean isCalabash) {
		this.ID = ID;
		this.isCalabash = isCalabash;
	}

	public void setImage(ImageView imageView) {
		this.imageView = imageView;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean hit() {
		Creature creature = GameSystem.getPositionManager().getCreatureAt(positionInt);
		if (creature == null)
			return false;
		else if (!creature.isAlive())
			return false;
		else if (isCalabash ^ (creature instanceof Calabash)) {
			GameSystem.getAttackManager().processDamage(isCalabash ? GameSystem.getGame().getCalabashes().get(ID)
					: GameSystem.getGame().getMonsters().get(ID), creature);
			return true;
		} else
			return false;
	}

	public boolean vanish() {
		int tx = (int) (positionInt.getX());
		int ty = (int) (positionInt.getY());
		return tx < 0 || tx >= GameSystem.getN() || ty < 0 || ty >= GameSystem.getM();
	}
}
