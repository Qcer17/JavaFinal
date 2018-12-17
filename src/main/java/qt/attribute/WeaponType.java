package qt.attribute;

/**
 * Enumerate weapons to be used in CVM.
 * @author Tian Qin
 * @version 1.0.0
 */
public enum WeaponType {
	SINGLEDIRECTION(1), CROSSDIRECTION(4), EIGHTDIRECTION(8);

	private int numBullet;

	private WeaponType(int numBullet) {
		this.numBullet = numBullet;
	}

	public int getNumBullet() {
		return numBullet;
	}
}
