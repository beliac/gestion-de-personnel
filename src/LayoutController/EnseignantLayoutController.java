package LayoutController;

import java.awt.Checkbox;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.print.attribute.standard.MediaSize.Engineering;
import javax.swing.text.TabableView;

//import controller.EnseignantController;
import controller.RouteController;
import database.DataBaseController;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Departement;
import model.Enseignant;
import model.EnseignantPermanent;
import model.Mandat;

public class EnseignantLayoutController {
	DataBaseController db = DataBaseController.getObjet();
	
//	EnseignantController enController = new EnseignantController();
	
	RouteController routeController = RouteController.getRouteController();
	ArrayList<Enseignant> enseignantList;
	Boolean listinging_show = false;
	
	@FXML
	private Button add_edit_btn, delete_btn;

	@FXML
	private TableView<Enseignant> enseignantTableView;

	@FXML
	private TableColumn<Enseignant, String> nomColumnTable, prenomColumnTable,emailColumnTable,typeColumnTable;
	
	@FXML 
	private TableColumn<Enseignant, String> telephoneColumnTable;
	
	@FXML 
	private TableColumn<Enseignant, String> celmandat;
	
	  

	@FXML
	TableColumn<Enseignant, Checkbox> check_box_clmn;

	@FXML
	public void initialize() {
		enseignantList = db.getAllEnseignant();
		
		delete_btn.setDisable(false);
		addCheckBoxListiner();
		loadAllEnseignant();

	}

	@FXML
	public void searchEnseignant() {
	}

	@FXML
	public void showEnseignantInfo() {
	}

	@FXML
	public void deleteItem() {
		Enseignant enseignant = getSelecteditem();
		
		if(enseignant == null) return;
		
		db.deleteEnseignant(enseignant.mat);
		
		loadEnsgAndResetTable();
	}
	
	@FXML
	private void Refrech() {
		loadEnsgAndResetTable();
	}
	@FXML
	private void update() {
	
	}
	
	public void loadEnsgAndResetTable() {
		enseignantList = db.getAllEnseignant();
		loadAllEnseignant();
	}

	
	void loadWhenShow() {
		if(listinging_show == false) {
			listinging_show = true;
			RouteController route = RouteController.getRouteController();
			Stage thisPage = route.getPage("EnseignantPage");
			
			thisPage.setOnShowing((ev)->{
				loadEnsgAndResetTable();
			});
		}
	}
	
	public void loadAllEnseignant() {
		ArrayList<Mandat> mandatList=  db.getAllMandats();
		for (Mandat mandat : mandatList) {
			for (Enseignant enseignant : enseignantList) {
				if(enseignant.mandat_id==mandat.id) {
					enseignant.mandat=mandat.intitule;
					break;
					
				}
				
			}
		}
		
		
		ObservableList<Enseignant> list = FXCollections.observableArrayList(enseignantList);

		nomColumnTable.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("nom"));
		prenomColumnTable.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("prenom"));
		typeColumnTable.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("enseignant_type"));
		telephoneColumnTable.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("telephone"));
		emailColumnTable.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("email"));
		celmandat.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("mandat"));
		check_box_clmn.setCellValueFactory(new PropertyValueFactory<Enseignant, Checkbox>("checkbox"));
		enseignantTableView.setItems(list);

	}

	private void addCheckBoxListiner() {
		for (Enseignant enseignant : enseignantList) {
			enseignant.checkBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					if (isOneSelected()) {
						//add_edit_btn.setText("Add");
						
						delete_btn.setDisable(false);

					} else {
						add_edit_btn.setText("Add");
						
						delete_btn.setDisable(true);

					}

					if (isMultipleSelected()) {
						add_edit_btn.setDisable(true);
						
						delete_btn.setDisable(true);
					} else {
						add_edit_btn.setDisable(false);
					}

				}
			});
		}

	}

	private Boolean isOneSelected() {
		for (Enseignant enseignant : enseignantList) {
			if (enseignant.checkBox.selectedProperty().get() == true) {
				return true;
			}
		}
		return false;
	}
	
	private Enseignant getSelecteditem() {
		if(isOneSelected()) {
			for (Enseignant enseignant : enseignantList) {
				if (enseignant.checkBox.selectedProperty().get() == true) {
					return enseignant;
				}
			}
			return null;
		}
		return null;
	}

	private Boolean isMultipleSelected() {
		int count = 0;
		for (Enseignant enseignant : enseignantList) {
			if (enseignant.checkBox.selectedProperty().get() == true) {
				count++;
				if (count > 1)
					return true;
			}
		}
		return false;
	}
	


	
	
	// Navigation
	
		@FXML 
		private void goToNotificationPage() {
			loadWhenShow();
			RouteController routeController = RouteController.getRouteController();
			
			Stage thisPage = routeController.getPage("EnseignantPage");
			thisPage.hide();
			

			Stage notifacationPage = routeController.getPage("notificationPage");
			notifacationPage.show();
			
		}
		
		@FXML 
		private void goToEnseignantPage() {
			//pass don't do anything
		}
		
		@FXML 
		private void goToCongePage() {
			loadWhenShow();
			RouteController routeController = RouteController.getRouteController();
			
			Stage thisPage = routeController.getPage("EnseignantPage");
			thisPage.hide();

			Stage notifacationPage = routeController.getPage("congePage");
			notifacationPage.show();
		}
		
//		@FXML 
//		private void goToEchlonPage() {
//			loadWhenShow();
//			RouteController routeController = RouteController.getRouteController();
//
//			Stage thisPage = routeController.getPage("EnseignantPage");
//			thisPage.close();
//			
//
//			Stage echlonPage = routeController.getPage("echlonPage");
//			echlonPage.show();
//		}
	
		@FXML
		private void goToAddEnsgPage() {
			loadWhenShow();
			RouteController routeController = RouteController.getRouteController();
			Stage stage = routeController.getPage("addEnsgPage");
			stage.show();
		}
		
		
		
		@FXML 
		private void showDepPage() {
			loadWhenShow();
			RouteController routeController = RouteController.getRouteController();
			Stage stage = routeController.getPage("depPage");
			stage.show();
		}
		
	
	
		private void moveEnsgStagtoPermn() {
			Enseignant ensg = getSelecteditem();
			
			if(ensg.enseignant_type.equals(Enseignant.STAGERER)) {
				db.deleteEnseignant(ensg.mat);
				
				EnseignantPermanent ensgPermanent = new EnseignantPermanent();
				
				ensgPermanent.getDataFromEnseignant(ensg);
				
				
				
			}
		}
		
		@FXML 
		private void showEchlonPage() {
			loadWhenShow();
			RouteController routeController = RouteController.getRouteController();

			Stage thisPage = routeController.getPage("EnseignantPage");
			thisPage.close();
			

			Stage echlonPage = routeController.getPage("echlonPage");
			echlonPage.show();
		}
		  @FXML
		    void exit(ActionEvent event) {
			   System.exit(0);
		    }
	
}
