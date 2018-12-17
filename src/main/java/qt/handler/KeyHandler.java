package qt.handler;

import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import qt.Main;
import qt.gameSystem.GameSystem;


/**
 * Handles key pressed event.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class KeyHandler implements EventHandler<KeyEvent> {

	@Override
	public void handle(KeyEvent event) {
		if (event.getCode() == KeyCode.SPACE) {
			if (!GameSystem.isStarted()) {
				GameSystem.reset();
				GameSystem.init(Main.getRoot());
				GameSystem.start();
			} else if (GameSystem.isFinished()) {
				GameSystem.reset();
				GameSystem.resetSeed();
				GameSystem.init(Main.getRoot());
			}
		} else if (event.getCode() == KeyCode.L) {
			FileChooser fileChooser1 = new FileChooser();
			fileChooser1.setTitle("Read game");
			File file = fileChooser1.showOpenDialog(Main.getStage());
			if (file != null) {
				GameSystem.reset();
				GameSystem.read(file);
				GameSystem.init(Main.getRoot());
			}
		} else if (event.getCode() == KeyCode.S) {
			FileChooser fileChooser1 = new FileChooser();
			fileChooser1.setTitle("Save game");
			File file = fileChooser1.showSaveDialog(Main.getStage());
			if (file != null) {
				GameSystem.save(file);
			}
		}
	}
}
