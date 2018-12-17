package qt.object;

import qt.attribute.Direction;
import qt.attribute.Position;
import qt.attribute.WeaponType;
import qt.gameSystem.GameSystem;
import qt.gameSystem.SynRunLater;

/**
 * Weapons.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class Weapon extends CVMObject{

	private WeaponType type = WeaponType.SINGLEDIRECTION;
	private String bulletFileName;
	private int bulletWidth;
	private int bulletHeight;
	private int ID;
	private boolean isCalabash;

	public Weapon(String fileName, int width, int height, boolean isCalabash, int ID) {
		bulletFileName = fileName;
		bulletHeight = height;
		bulletWidth = width;
		this.isCalabash = isCalabash;
		this.ID = ID;
	}

	public Bullet[] getBullet(Direction direction, Position pInt, int kind) {
		Bullet[] ret = new Bullet[type.getNumBullet()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new Bullet(ID, isCalabash);
			ret[i].setImage(GameSystem.getAttackBallContainer().getBallImage(kind));
			final int j = i;
			SynRunLater.runLater(new Runnable() {

				@Override
				public void run() {
					ret[j].getImageView().setVisible(false);
					ret[j].setIntLocation((int) pInt.getX(), (int) pInt.getY());
				}
			});
		}
		switch (type) {
		case SINGLEDIRECTION:
			ret[0].setDirection(direction);
			break;
		case CROSSDIRECTION:
			ret[0].setDirection(Direction.DOWN);
			ret[1].setDirection(Direction.UP);
			ret[2].setDirection(Direction.LEFT);
			ret[3].setDirection(Direction.RIGHT);
			break;
		case EIGHTDIRECTION:
			ret[0].setDirection(Direction.DOWN);
			ret[1].setDirection(Direction.UP);
			ret[2].setDirection(Direction.LEFT);
			ret[3].setDirection(Direction.RIGHT);
			ret[4].setDirection(Direction.DOWNLEFT);
			ret[5].setDirection(Direction.DOWNRIGHT);
			ret[6].setDirection(Direction.UPLEFT);
			ret[7].setDirection(Direction.UPRIGHT);
			break;
		default:
			break;
		}
		return ret;
	}

	public void setType(WeaponType t) {
		type = t;
	}

}
