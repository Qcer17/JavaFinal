package qt.object;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import qt.attribute.Direction;
import qt.attribute.Position;
import qt.gameSystem.GameSystem;
import qt.gameSystem.SynRunLater;

/**
 * Base class of all objects in CVM world.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public abstract class CVMObject {
	protected ImageView imageView;
	private int speed = 42;
	protected Position positionInt = new Position(0, 0);
	protected Position positionDouble = new Position(0, 0);

	public CVMObject(String fileName, int width, int height) {
		imageView = new ImageView(new Image("" + fileName, width, height, false, true));
	}

	public CVMObject() {

	}

	public synchronized void rawTranslate(Direction direction) {
		double dx = (double) direction.getDx() * GameSystem.getGridWidth() / speed;
		double dy = (double) direction.getDy() * GameSystem.getGridHeight() / speed;
		double x = positionDouble.getX();
		double y = positionDouble.getY();
		if (dx != 0)
			SynRunLater.runLater(() -> imageView.setLayoutX(x + dx));
		if (dy != 0)
			SynRunLater.runLater(() -> imageView.setLayoutY(y + dy));
		positionDouble.set(x + dx, y + dy);
	}

	public void translate(Direction direction) {
		double dx = (double) direction.getDx() * GameSystem.getGridWidth() / speed;
		double dy = (double) direction.getDy() * GameSystem.getGridHeight() / speed;
		double x = positionDouble.getX();
		double y = positionDouble.getY();

		for (int i = 0; i < speed; i++) {
			final int j = i;
			if (dx != 0)
				SynRunLater.runLater(() -> imageView.setLayoutX(x + j * dx));
			if (dy != 0)
				SynRunLater.runLater(() -> imageView.setLayoutY(y + j * dy));
			try {
				Thread.sleep(GameSystem.getSleepLength());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setIntLocation((int) (positionInt.getX() + direction.getDx()), (int) (positionInt.getY() + direction.getDy()));
	}

	public void setIntLocation(int x, int y) {
		positionInt.set(x, y);
		positionDouble = GameSystem.int2DoublePos(positionInt);
		SynRunLater.runLater(() -> imageView.setLayoutX(positionDouble.getX()));
		SynRunLater.runLater(() -> imageView.setLayoutY(positionDouble.getY()));
	}

	public Position getPositionInt() {
		return positionInt;
	}

	public Position getPositionDouble() {
		return positionDouble;
	}

	public ImageView getImageView() {
		return imageView;
	}
}
