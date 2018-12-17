package qt.gameSystem;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import qt.formation.FormationProvider;
import qt.object.Calabash;
import qt.object.Monster;

/**
 * Generates a new game, which includes generating characters, 
 * formating characters and starting the game.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class Game {
	private ArrayList<Calabash> calabashes = new ArrayList<>();
	private ArrayList<Monster> monsters = new ArrayList<>();
	private Thread[] threads;

	Game() {
		int width = 40, height = 33;
		for (int i = 1; i <= GameSystem.getNumCalabashes(); i++) {
			calabashes.add(new Calabash(i + ".png", width, height, "attackBall" + i + ".gif", width, height));
		}
		calabashes.add(new Calabash("grandpa.png", width, height, "calabash.gif", width, height));

		monsters.add(new Monster("scorpion.png", width, height, "attackBallScorpion.gif", width, height));
		monsters.add(new Monster("snake.png", width, height, "attackBallSnake.gif", width, height));
		for (int i = 2; i < GameSystem.getNumMonsters(); i++) {
			monsters.add(new Monster("mouse.png", width, height, "attackBallMouse.gif", width, height));
		}

		Pane root = GameSystem.getRoot();
		for (Calabash c : calabashes) {
			root.getChildren().add(c.getImageView());
			root.getChildren().add(c.getPhoto());
			root.getChildren().add(c.getLifeBar());
		}
		for (Monster m : monsters) {
			root.getChildren().add(m.getImageView());
			root.getChildren().add(m.getPhoto());
			root.getChildren().add(m.getLifeBar());
		}

		format();
	}

	public void setBarrier() {
		for (Calabash c : calabashes) {
			c.setBarrier(GameSystem.getSynchronizer().getBarrier());
		}
		for (Monster m : monsters) {
			m.setBarrier(GameSystem.getSynchronizer().getBarrier());
		}
	}

	public void format() {
		FormationProvider.getFormation(GameSystem.getCalabashFormation()).format(this, calabashes, true);
		FormationProvider.getFormation(GameSystem.getMonsterFormation()).format(this, monsters, false);
	}

	public ArrayList<Calabash> getCalabashes() {
		return calabashes;
	}

	public ArrayList<Monster> getMonsters() {
		return monsters;
	}

	public void start() {
		threads = new Thread[calabashes.size() + monsters.size()];
		for (int i = 0; i < calabashes.size(); i++) {
			threads[i] = new Thread(calabashes.get(i));
		}
		for (int i = 0; i < monsters.size(); i++) {
			threads[calabashes.size() + i] = new Thread(monsters.get(i));
		}
		for (Thread t : threads) {
			t.start();
		}
	}

	public void join() {
		if (threads == null)
			return;
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
