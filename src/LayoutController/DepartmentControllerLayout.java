package LayoutController;

import java.util.ArrayList;
//import java.util.Optional;
import java.util.Optional;

import controller.RouteController;
import database.DataBaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Departement;
import model.Enseignant;

public class DepartmentControllerLayout {
	DataBaseController db = DataBaseController.getObjet();
	Boolean isSelected = false;

	ArrayList<Departement> departementList;

	@FXML
	private ListView<Departement> departmentListView;

	@FXML
	private Button addBtn;

	@FXML
	private Button editBtn;

	@FXML
	private Button removeBtn;

	@FXML
	private void addBtnAction(ActionEvent event) {
		String depName = show();
		if (depName != null && !depName.isEmpty()) {
			Departement departement = new Departement();
			departement.intitule = depName;
			db.insertDepartmenet(departement);
			loadDepartment();
		}
	}


	
	@FXML
	void editBtnAction(ActionEvent event) {
		Departement departement = departmentListView.getSelectionModel().getSelectedItem();
		if(departement != null) {
			String newName = show();
			if(newName != null && !newName.isEmpty()) {
				departement.intitule = newName;
				db.updateDepartmenet(departement);
				loadDepartment();
			}
			
		}

	}

	@FXML
	void removeBtnAction(ActionEvent event) {
		Departement selectedItem = departmentListView.getSelectionModel().getSelectedItem();
		if(selectedItem != null) {
			db.deleteDepartment(selectedItem.mat);
			loadDepartment();
		}
	}

	@FXML
	public void initialize() {
		loadDepartment();

	}

	// My functions
	private void loadDepartment() {
		departementList = db.getAllDepartments();
		ObservableList<Departement> observableList = FXCollections.observableArrayList(departementList);

		departmentListView.setItems(observableList);
		// new PropertyValueFactory<Departement, String>("intitule")
//    	departmentListView.setItems(null);
	}

	@FXML
	void selectItem(MouseEvent event) {
		Departement selectedItem = departmentListView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			isSelected = true;
		}else {
			isSelected = false;
		}
		
		updatedButtonState();

	}
	
	

	private void updatedButtonState() {
		editBtn.setDisable(!isSelected);
		removeBtn.setDisable(!isSelected);
	}

	public String show() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add departemtnt");
		dialog.setHeaderText("Enter name of Department");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {

			return result.get();
		}

		return null;
	}
}
