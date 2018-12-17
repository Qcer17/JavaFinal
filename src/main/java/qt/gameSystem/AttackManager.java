package qt.gameSystem;

import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javafx.application.Platform;
import qt.object.Bullet;
import qt.object.Creature;

/**
 * Processes attack balls movement and damage they caused. 
 * @author Tian Qin
 * @version 1.0.0
 */
public class AttackManager implements Runnable {
	private LinkedList<Bullet> queueCurTurn = new LinkedList<>();
	private LinkedList<Bullet> queueNextTurn = new LinkedList<>();
	private CyclicBarrier barrier;

	public void setBarrier(CyclicBarrier barrier) {
		this.barrier = barrier;
	}

	public void updateTurn() {
		for (Bullet bullet : queueCurTurn) {
			if (!bullet.vanish() && !bullet.hit()) {
				queueNextTurn.add(bullet);
			} else {
				Platform.runLater(() -> bullet.getImageView().setVisible(false));
			}
		}
		queueCurTurn = queueNextTurn;
		queueNextTurn = new LinkedList<>();
	}

	public synchronized void processDamage(Creature attacker, Creature defender) {

		defender.getAttribute().updateLife(-attacker.getAttribute().getAttack());
		if (defender.getAttribute().getLife() == 0) {
			defender.setDead();
		}
		SynRunLater.runLater(() -> defender.getLifeBar()
				.setProgress((double) defender.getAttribute().getLife() / GameSystem.getLife()));

	}

	public synchronized void addBullet(Bullet[] bullets) {
		for (Bullet bullet : bullets) {
			queueNextTurn.add(bullet);
		}
	}

	@Override
	public void run() {
		int cnt = 0;
		while (cnt < 1000 && !GameSystem.isFinished()) {
			translateBullets();
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			cnt++;
		}
		for (Bullet bullet : queueCurTurn) {
			SynRunLater.runLater(() -> bullet.getImageView().setVisible(false));
		}
	}

	private void translateBullets() {
		for (Bullet bullet : queueCurTurn) {
			SynRunLater.runLater(() -> bullet.getImageView().setVisible(true));
		}
		for (int i = 0; i < GameSystem.getSpeed(); i++) {
			for (Bullet bullet : queueCurTurn) {
				bullet.rawTranslate(bullet.getDirection());
			}
			try {
				Thread.sleep(GameSystem.getSleepLength());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Bullet bullet : queueCurTurn) {
			bullet.setIntLocation((int) (bullet.getPositionInt().getX() + bullet.getDirection().getDx()),
					(int) (bullet.getPositionInt().getY() + bullet.getDirection().getDy()));
		}
	}
}
