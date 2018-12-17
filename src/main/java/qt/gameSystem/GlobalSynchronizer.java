package qt.gameSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;

import qt.object.Calabash;
import qt.object.Monster;

/**
 * Provides global barriers to synchronize
 * character threads, position manager and 
 * attack manager threads.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class GlobalSynchronizer {
	private ArrayList<Calabash> calabashes = GameSystem.getGame().getCalabashes();
	private ArrayList<Monster> monsters = GameSystem.getGame().getMonsters();
	private CyclicBarrier barrier = new CyclicBarrier(calabashes.size() + monsters.size() + 1, new Runnable() {
		@Override
		public void run() {
			GameSystem.getPositionManager().updateBoard();
			GameSystem.getAttackManager().updateTurn();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	});

	public CyclicBarrier getBarrier() {
		return barrier;
	}
}
