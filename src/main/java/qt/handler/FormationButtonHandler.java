package qt.handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import qt.formation.FormationProvider;
import qt.gameSystem.GameSystem;

/**
 * Handles button pressed event.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
public class FormationButtonHandler implements EventHandler<ActionEvent> {

	private boolean isCalabash;

	public FormationButtonHandler(boolean isCalabash) {
		this.isCalabash = isCalabash;
	}

	private int id = 1;

	@Override
	public void handle(ActionEvent event) {
		if (GameSystem.isStarted())
			return;
		if (isCalabash)
			GameSystem.setCalabashFormation((id++) % FormationProvider.getNumFormations());
		else {
			GameSystem.setMonsterFormation((id++) % FormationProvider.getNumFormations());
		}
	}

}