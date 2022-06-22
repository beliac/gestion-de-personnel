package LayoutController;

import javafx.fxml.FXML;

public abstract class AbstractLayoutController {
	

	@FXML
	protected void initialize() {
		loadData();
		loadWidget();
		loadOthers();
	}

	protected abstract void loadData();

	protected abstract void loadWidget();

	void loadOthers() {}

	protected boolean isDataValid() {
		return runValidators();
	}
	
	protected abstract boolean runValidators();
}
