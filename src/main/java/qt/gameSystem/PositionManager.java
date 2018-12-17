package qt.gameSystem;

import java.util.ArrayList;
import java.util.LinkedList;

import qt.attribute.Direction;
import qt.attribute.Position;
import qt.object.Calabash;
import qt.object.Creature;
import qt.object.Monster;

/**
 * Calculates and decides which position should 
 * a character move to, so that there will be no
 * collision.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class PositionManager {
	private ArrayList<Calabash> calabashes;
	private ArrayList<Monster> monsters;
	private int m, n;
	private Position[] cPositions;
	private Position[] mPositions;
	private Direction[] cDirections;
	private Direction[] mDirections;
	private int[][] board;
	private boolean[][] isCalabash;
	private boolean[][] occupied;

	public PositionManager() {
		calabashes = GameSystem.getGame().getCalabashes();
		monsters = GameSystem.getGame().getMonsters();
		m = GameSystem.getM();
		n = GameSystem.getN();
		cPositions = new Position[calabashes.size()];
		mPositions = new Position[monsters.size()];
		cDirections = new Direction[calabashes.size()];
		mDirections = new Direction[monsters.size()];
		board = new int[m][n];
		isCalabash = new boolean[m][n];
		occupied = new boolean[m][n];

		resetBoard();
		for (int i = 0; i < calabashes.size(); i++) {
			cPositions[i] = new Position(calabashes.get(i).getPositionInt());
		}
		for (int i = 0; i < monsters.size(); i++) {
			mPositions[i] = new Position(monsters.get(i).getPositionInt());
		}
		updateBoard();
	}

	public Creature getCreatureAt(Position p) {
		return getCreatureAt((int) p.getX(), (int) p.getY());
	}

	public Creature getCreatureAt(int x, int y) {
		int id = board[y][x];
		if (id == -1)
			return null;
		if (isCalabash[y][x]) {
			return calabashes.get(id);
		} else {
			return monsters.get(id);
		}
	}

	public Direction getNextDirection(int id, boolean isCalabash) {
		if (isCalabash) {
			return cDirections[id];
		}
		return mDirections[id];
	}

	public Direction nearbyEnemyDirection(Position p, boolean isCalabash) {
		ArrayList<? extends Creature> toBeComparedCreature = isCalabash ? monsters : calabashes;
		Position[] toBeCompared = isCalabash ? mPositions : cPositions;
		Direction direction = Direction.CENTER;
		for (int i = 0; i < toBeCompared.length; i++) {
			if (!toBeComparedCreature.get(i).isAlive())
				continue;
			direction = toBeCompared[i].relativeDirectionTo(p);
			if (direction != Direction.CENTER
					&& Position.blockDistance(p, toBeCompared[i]) <= GameSystem.getViewField()) {
				break;
			}
		}
		return direction;
	}

	private void resetBoard() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = -1;
				isCalabash[i][j] = false;
				occupied[i][j] = false;
			}
		}
	}

	public void updateBoard() {
		resetBoard();
		for (int i = 0; i < calabashes.size(); i++) {
			cPositions[i].set(calabashes.get(i).getPositionInt());
			int tx = (int) cPositions[i].getX();
			int ty = (int) cPositions[i].getY();
			board[ty][tx] = calabashes.get(i).getID();
			isCalabash[ty][tx] = true;
		}
		for (int i = 0; i < monsters.size(); i++) {
			mPositions[i].set(monsters.get(i).getPositionInt());
			int tx = (int) mPositions[i].getX();
			int ty = (int) mPositions[i].getY();
			board[ty][tx] = monsters.get(i).getID();
		}
		for (int i = 0; i < calabashes.size(); i++) {
			if (calabashes.get(i).isAlive()) {
				fillNextDirection(calabashes.get(i), true, i);
				occupied[(int) (cPositions[i].getY() + cDirections[i].getDy())][(int) (cPositions[i].getX()
						+ cDirections[i].getDx())] = true;
			}
		}
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).isAlive()) {
				fillNextDirection(monsters.get(i), false, i);
				occupied[(int) (mPositions[i].getY() + mDirections[i].getDy())][(int) (mPositions[i].getX()
						+ mDirections[i].getDx())] = true;
			}
		}
	}

	private void fillNextDirection(Creature c, boolean isCalabash, int index) {
		ArrayList<? extends Creature> toBeCompared = isCalabash ? monsters : calabashes;
		Direction[] toBeFilled = isCalabash ? cDirections : mDirections;
		int[] dx = new int[] { 1, 0, -1, 0 };
		int[] dy = new int[] { 0, 1, 0, -1 };
		Direction[] directions = new Direction[] { Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP };
		Position p = c.getPositionInt();
		toBeFilled[index]=Direction.CENTER;
		for (int i = 0; i < 4; i++) {
			int tx = (int) p.getX() + dx[i];
			int ty = (int) p.getY() + dy[i];
			if ((tx < 0 || tx >= GameSystem.getN()) || (ty < 0 || ty >= GameSystem.getM()))
				continue;
			if (occupied[ty][tx])
				continue;
			int id = board[ty][tx];
			if(id==-1) {
				toBeFilled[index]=directions[i];
			}
		}
		for (int i = 0; i < 4; i++) {
			int tx = (int) p.getX() + dx[i];
			int ty = (int) p.getY() + dy[i];
			if ((tx < 0 || tx >= GameSystem.getN()) || (ty < 0 || ty >= GameSystem.getM()))
				continue;
			if (occupied[ty][tx])
				continue;
			int id = board[ty][tx];
			if (id != -1 && (isCalabash ^ this.isCalabash[ty][tx]) && toBeCompared.get(id).isAlive()) {
				return;
			}
		}

		boolean[][] visited = new boolean[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				visited[i][j] = false;
			}
		}
		visited[(int) p.getY()][(int) p.getX()] = true;

		class Node {
			Position position;
			Direction direction;
			Node preNode = null;

			Node(Position position, Direction direction) {
				this.position = position;
				this.direction = direction;
			}
		}
		LinkedList<Node> queue = new LinkedList<>();
		Node startPoint = new Node(new Position(p), Direction.CENTER);
		queue.push(startPoint);
		while (!queue.isEmpty()) {
			Node node = queue.pollLast();
			Position position = node.position;

			for (int i = 0; i < 4; i++) {
				int tx = (int) position.getX() + dx[i];
				int ty = (int) position.getY() + dy[i];
				if ((tx < 0 || tx >= GameSystem.getN()) || (ty < 0 || ty >= GameSystem.getM()))
					continue;
				if (visited[ty][tx])
					continue;
				if (occupied[ty][tx])
					continue;
				int id = board[ty][tx];
				if (id == -1) {
					Node newNode = new Node(new Position(tx, ty), directions[i]);
					newNode.preNode = node;
					queue.push(newNode);
					visited[ty][tx] = true;
				} else if (isCalabash ^ this.isCalabash[ty][tx]) {
					if (toBeCompared.get(id).isAlive()) {
						Node tNode = node;
						while (tNode.preNode.preNode != null) {
							tNode = tNode.preNode;
						}
						toBeFilled[index] = tNode.direction;
						queue.clear();
						return;
					}
				} else {
					visited[ty][tx] = true;
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			int tx = (int) p.getX() + dx[i];
			int ty = (int) p.getY() + dy[i];
			if ((tx < 0 || tx >= GameSystem.getN()) || (ty < 0 || ty >= GameSystem.getM()))
				continue;
			if (occupied[ty][tx])
				continue;
			if (board[ty][tx] != -1)
				continue;
			mDirections[index] = directions[i];
			return;
		}
	}

}
