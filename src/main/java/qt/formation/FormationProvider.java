package qt.formation;

import java.util.LinkedList;

/**
 * Provides formation objects.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class FormationProvider {
	private static LinkedList<Formation> formations = new LinkedList<>();

	static {
		formations.add(new SnakeFormation());
		formations.add(new SquareFormation());
		formations.add(new CrossFormation());
		formations.add(new CraneFormation());
		formations.add(new GooseFormation());
		formations.add(new ScaleFormation());
	}

	public static Formation getFormation(int id) {
		return formations.get(id);
	}

	public static int getNumFormations() {
		return formations.size();
	}
}
