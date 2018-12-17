package qt.gameSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import qt.attribute.Position;
import qt.object.Calabash;
import qt.object.Creature;
import qt.object.Monster;

/**
 * Contains all basic information a game needs, and
 * prepares to start a game.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class GameSystem {
	private static Game game;
	private static PositionManager positionManager;
	private static AttackManager attackManager;
	private static AttackBallContainer attackBallContainer;
	private static GlobalSynchronizer globalSynchronizer;
	private static int calabashFormation;
	private static int monsterFormation;
	private static double gridWidth;
	private static double gridHeight;
	private static double startX;
	private static double startY;
	private static int M;
	private static int N;
	private static int speed;
	private static int sleepLength;
	private static int life;
	private static int attack;
	private static int numCalabashes;
	private static int numMonsters;
	private static int viewField;
	private static int seed;
	private static boolean started;
	private static boolean finished;
	private static Pane root;

	static {
		startX = 91 + 100;
		startY = 90;
		gridWidth = 42;
		gridHeight = 32.5;
		M = 13;
		N = 15;
		speed = 42;
		sleepLength = 5;
		life = 100;
		attack = 10;
		numCalabashes = 7;
		numMonsters = 9;
		viewField = 7;
		calabashFormation = 0;
		monsterFormation = 0;
		started = false;
		finished = false;
		seed = new Random().nextInt(1000);
	}

	public static void init(Pane r) {
		root = r;

		ImageView imageView = new ImageView(new Image("background.jpg", 800, 600, false, true));
		root.getChildren().add(imageView);
		imageView.setLayoutX(100);
		game = new Game();
		globalSynchronizer = new GlobalSynchronizer();
		game.setBarrier();
		positionManager = new PositionManager();
		attackManager = new AttackManager();
		attackManager.setBarrier(globalSynchronizer.getBarrier());
		attackBallContainer = new AttackBallContainer(40);
		finished = false;

	}

	public static void reset() {
		finished = true;
		for (int i = root.getChildren().size() - 2; i > 0; i--) {
			SynRunLater.runLater(() -> root.getChildren().remove(2));
		}
		Monster.reset();
		Calabash.reset();
		game.join();
		started = false;
	}

	public static void save(File file) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(calabashFormation);
			fileWriter.write(monsterFormation);
			fileWriter.write(seed);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void resetSeed() {
		seed = new Random().nextInt(1000);
	}

	public static void read(File file) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			calabashFormation = fileReader.read();
			monsterFormation = fileReader.read();
			seed = fileReader.read();
			fileReader.close();
			started = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getSeed() {
		return seed;
	}

	public static void setCalabashFormation(int id) {
		calabashFormation = id;
		game.format();
	}

	public static void setMonsterFormation(int id) {
		monsterFormation = id;
		game.format();
	}

	public static int getCalabashFormation() {
		return calabashFormation;
	}

	public static int getMonsterFormation() {
		return monsterFormation;
	}

	public static Pane getRoot() {
		return root;
	}

	public static void start() {
		started = true;
		new Thread(attackManager).start();
		game.start();
	}

	public static boolean isStarted() {
		return started;
	}

	public static boolean isFinished() {
		if (finished)
			return true;
		boolean allDead = true;
		for (Creature c : game.getMonsters()) {
			if (c.isAlive()) {
				allDead = false;
				break;
			}
		}
		if (allDead)
			return true;
		for (Creature c : game.getCalabashes()) {
			if (c.isAlive()) {
				return false;
			}
		}
		return true;
	}

	public static Position int2DoublePos(Position p) {
		return new Position(startX + p.getX() * gridWidth, startY + p.getY() * gridHeight);
	}

	public static PositionManager getPositionManager() {
		return positionManager;
	}

	public static Game getGame() {
		return game;
	}

	public synchronized static AttackManager getAttackManager() {
		return attackManager;
	}

	public static AttackBallContainer getAttackBallContainer() {
		return attackBallContainer;
	}

	public static double getGridWidth() {
		return gridWidth;
	}

	public static double getGridHeight() {
		return gridHeight;
	}

	public static int getM() {
		return M;
	}

	public static int getN() {
		return N;
	}

	public static double getStartX() {
		return startX;
	}

	public static double getStartY() {
		return startY;
	}

	public static int getSpeed() {
		return speed;
	}

	public static int getSleepLength() {
		return sleepLength;
	}

	public static int getLife() {
		return life;
	}

	public static int getAttack() {
		return attack;
	}

	public static int getNumCalabashes() {
		return numCalabashes;
	}

	public static int getNumMonsters() {
		return numMonsters;
	}

	public static int getViewField() {
		return viewField;
	}

	public static GlobalSynchronizer getSynchronizer() {
		return globalSynchronizer;
	}
}
