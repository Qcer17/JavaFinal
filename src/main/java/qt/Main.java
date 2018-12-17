package qt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import qt.gameSystem.GameSystem;
import qt.handler.FormationButtonHandler;
import qt.handler.KeyHandler;

/**
 * Just an entrance.
 * @author Tian Qin
 * @version 1.0.0
 */
public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private static Pane root = new Pane();
	private static Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;

		Scene scene = new Scene(root, 800 + 100 * 2, 600);
		scene.setOnKeyPressed(new KeyHandler());
		primaryStage.setTitle("CVM  空格：开始/重置  S：保存  L：读取");

		Button button = new Button("阵型");
		button.setLayoutX(25);
		button.setLayoutY(570);
		button.setPrefWidth(50);
		button.setOnAction(new FormationButtonHandler(true));
		button.setFocusTraversable(false);
		root.getChildren().add(button);

		Button button2 = new Button("阵型");
		button2.setLayoutX(900 + 25);
		button2.setLayoutY(570);
		button2.setPrefWidth(50);
		button2.setOnAction(new FormationButtonHandler(false));
		button2.setFocusTraversable(false);
		root.getChildren().add(button2);

		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		primaryStage.setScene(scene);
		primaryStage.show();

		GameSystem.init(root);
	}

	public static Pane getRoot() {
		return root;
	}

	public static Stage getStage() {
		return stage;
	}

}
