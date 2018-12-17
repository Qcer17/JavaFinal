package qt.object;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import qt.attribute.Attribute;
import qt.gameSystem.GameSystem;
import qt.gameSystem.SynRunLater;

/**
 * Base class of all creatures.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public abstract class Creature extends CVMObject implements Runnable {
	protected boolean isAlive = true;
	protected Attribute attribute;
	protected ImageView photo = new ImageView();
	protected ProgressBar lifeBar = new ProgressBar(1);

	public Creature(String fileName, int width, int height) {
		super(fileName, width, height);
		attribute = new Attribute(GameSystem.getLife(), GameSystem.getAttack());
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setDead() {
		if (isAlive) {
			isAlive = false;
			SynRunLater.runLater(() -> imageView.setRotate(180));
		}
	}

	public ImageView getPhoto() {
		return photo;
	}

	public ProgressBar getLifeBar() {
		return lifeBar;
	}

}
