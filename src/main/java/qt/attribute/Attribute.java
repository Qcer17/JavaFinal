package qt.attribute;

/**
 * Attributes of creature in CVM.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class Attribute {
	private int life;
	private int attack;

	public Attribute(int life, int attack) {
		this.life = life;
		this.attack = attack;
	}

	public void updateLife(int delta) {
		life = Math.max(0, life + delta);
	}

	public void updateAttack(int delta) {
		attack = Math.max(0, attack + delta);
	}

	public int getAttack() {
		return attack;
	}

	public int getLife() {
		return life;
	}
}
