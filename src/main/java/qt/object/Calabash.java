package qt.object;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.sun.org.apache.xerces.internal.dom.RangeExceptionImpl;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import qt.attribute.Direction;
import qt.attribute.WeaponType;
import qt.gameSystem.GameSystem;

/**
 * Calabash brothers, implemented as
 * a thread(Creature implements Runnable).
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class Calabash extends Creature {
	private static int globalID = 0;
	protected int ID;
	private CyclicBarrier barrier;
	private Weapon weapon;
	private Random random;

	public Calabash(String fileName, int width, int height, String weaponFileName, int weaponWidth, int weaponHeight) {
		super(fileName, width, height);
		ID = globalID++;
		weapon = new Weapon(weaponFileName, weaponWidth, weaponHeight, true, ID);
		photo.setImage(new Image("" + fileName, width / 1.5, height / 1.5, false, true));
		photo.setLayoutX(10);
		photo.setLayoutY(ID * 1.4 * height);
		lifeBar.setPrefWidth(75);
		lifeBar.setLayoutX(10);
		lifeBar.setLayoutY(ID * 1.4 * height + height / 1.3);
		random = new Random(GameSystem.getSeed() + ID);
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
					Direction direction = GameSystem.getPositionManager().getNextDirection(ID, true);
					translate(direction);
					Direction direction2 = GameSystem.getPositionManager().nearbyEnemyDirection(positionInt, true);
					if (direction2 != Direction.CENTER && cnt % 4 == 0) {
						randomSetWeaponType();
						GameSystem.getAttackManager().addBullet(weapon.getBullet(direction2, positionInt, ID));
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

	protected void randomSetWeaponType() {
		int rand = random.nextInt(10);
		if (rand < 8) {
			weapon.setType(WeaponType.SINGLEDIRECTION);
		} else if (rand < 9) {
			weapon.setType(WeaponType.CROSSDIRECTION);
		} else {
			weapon.setType(WeaponType.EIGHTDIRECTION);
		}
	}
}
