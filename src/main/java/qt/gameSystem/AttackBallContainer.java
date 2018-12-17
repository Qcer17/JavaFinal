package qt.gameSystem;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class BallImageList extends ArrayList<ImageView> {
}

/**
 * Generates attack balls and provide interface to reuse,
 * which reduces creation and destruction overhead.
 * 
 * @author Tian Qin
 * @version 1.0.0
 * 
 */
public class AttackBallContainer {
	private BallImageList[] lists;
	private int[] IDs;
	private int numPerKind;

	public AttackBallContainer(int numPerKind) {
		lists = new BallImageList[7 + 1 + 13];
		IDs = new int[7 + 1 + 13];
		this.numPerKind = numPerKind;
		for (int j = 0; j < IDs.length; j++) {
			IDs[j] = 0;
			lists[j] = new BallImageList();
		}
		for (int j = 0; j < 7; j++) {
			BallImageList list = lists[j];
			for (int i = 0; i < numPerKind; i++) {

				String s = "attackBall" + (j + 1) + ".gif";
				list.add(new ImageView(
						new Image(s, GameSystem.getGridWidth() / 1.5, GameSystem.getGridHeight() / 1.5, false, true)));
			}
		}
		genImages(lists[7], "calabash.gif");
		genImages(lists[8], "attackBallScorpion.gif");
		genImages(lists[9], "attackBallSnake.gif");
		for (int i = 10; i < IDs.length; i++) {
			genImages(lists[i], "attackBallMouse.gif");
		}
		for (BallImageList list : lists) {
			for (ImageView imageView : list) {
				imageView.setVisible(false);
				GameSystem.getRoot().getChildren().add(imageView);
			}
		}
	}
	
	public synchronized ImageView getBallImage(int kind) {
		ImageView ret = lists[kind].get(IDs[kind]);
		IDs[kind] = (IDs[kind] + 1) % numPerKind;
		return ret;
	}
	
	private void genImages(BallImageList ballImageList, String filePath) {
		for (int i = 0; i < numPerKind; i++) {
			ballImageList.add(new ImageView(new Image(filePath, GameSystem.getGridWidth() / 1.5,
					GameSystem.getGridHeight() / 1.5, false, true)));
		}
	}	
}
