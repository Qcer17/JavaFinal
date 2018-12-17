package qt.object;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javafx.scene.image.Image;
import qt.attribute.Direction;
import qt.gameSystem.GameSystem;

/**
 * Monsters, implemented as
 * a thread(Creature implements Runnable).
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class Monster extends Creature {
	private static int globalID = 0;
	protected int ID;
	private CyclicBarrier barrier;
	private Weapon weapon;

	public Monster(String fileName, int width, int height, String weaponFileName, int weaponWidth, int weaponHeight) {
		super(fileName, width, height);
		ID = globalID++;
		weapon = new Weapon(weaponFileName, weaponWidth, weaponHeight, false, ID);
		photo.setImage(new Image("" + fileName, width / 1.5, height / 1.5, false, true));
		photo.setLayoutX(100 + 800 + 10);
		photo.setLayoutY(ID * 1.4 * height);
		lifeBar.setPrefWidth(75);
		lifeBar.setLayoutX(100 + 800 + 10);
		lifeBar.setLayoutY(ID * 1.4 * height + height / 1.3);
	}

	public int getID() {
		return ID;
	}

	public void setBarrier(CyclicBarrier barrier) {
		this.barrier = barrier;
	}

	public static void reset() {
		globalID = 0;
	}

	@Override
	public void run() {
		int cnt = 0;
		boolean attacking = false;
		while (cnt < 1000 && !GameSystem.isFinished()) {
			if (isAlive) {
				if (!attacking) {
					Direction direction = GameSystem.getPositionManager().getNextDirection(ID, false);
					translate(direction);
					Direction direction2 = GameSystem.getPositionManager().nearbyEnemyDirection(positionInt, false);
					if (direction2 != Direction.CENTER && cnt % 4 == 0) {
						GameSystem.getAttackManager().addBullet(weapon.getBullet(direction2, positionInt, 7 + 1 + ID));
						attacking = true;
					}
				} else {
					attacking = false;
				}
			}
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			cnt++;
		}
	}
}
