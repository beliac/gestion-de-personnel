package LayoutController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import controller.RouteController;
import database.DataBaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Conge;
import model.Enseignant;
import model.Conge;
import model.Conge;

public class CongePageLyoutConroller {
	DataBaseController db = DataBaseController.getObjet();
	ArrayList<Conge> congeList = new ArrayList<>();
	String type = "Conge Maladie";
	boolean listinging_show = false;

    @FXML
    private TableView<Conge> CongeTableView;

    @FXML
    private TableColumn<Conge, String> nomCellTable;

    @FXML
    private TableColumn<Conge, String> prenomCellTable;

    @FXML
    private TableColumn<Conge, String> typeCellTable;

    @FXML
    private TableColumn<Conge, LocalDate> dateDebutCellTable;

    @FXML
    private TableColumn<Conge, LocalDate> dateFinCellTable;

    @FXML
    private TableColumn<Conge, CheckBox> checkCellTable;

    @FXML
    private Button addBtn;

    @FXML
    private MenuButton menuCongeType;

    @FXML
    private Button removeBtn;
    
	@FXML
	public void initialize() {

		loadCongeResetTable();
	}
    
	void loadWhenShow() {
		if(listinging_show == false) {
			listinging_show = true;
			RouteController route = RouteController.getRouteController();
			Stage thisPage = route.getPage("congePage");
			
			thisPage.setOnShowing((ev)->{
				loadCongeResetTable();
			});
		}
	}
	
    @FXML
    void addAction(ActionEvent event) {
    	loadWhenShow();
    	
		RouteController route = RouteController.getRouteController();
		Stage addconge = route.getPage("addCongePage");
		
		
		addconge.show();
//		loadCongeResetTable();
    }

    @FXML
    void getAllCongeAction(ActionEvent event) {

    }

    @FXML
    void getNewCongeAction(ActionEvent event) {

    }

    @FXML
    void getSeenCongeAction(ActionEvent event) {

    }

    @FXML
    void removeAction(ActionEvent event) {
    	Conge conge = getSelectedItem();
    	
    	db.deleteConge(conge.id);
    	
    	loadCongeResetTable();
    	initCongeCheckBox();
    }
    
    
    @FXML
	void selctCongeEnCongeMaladie(ActionEvent event) {
		type = "Conge Maladie";
		menuCongeType.setText(type);
	}

	@FXML
	void selctCongeEnCongeMatirnite(ActionEvent event) {
		type = "Conge Matirnite";
		menuCongeType.setText(type);
	}

	@FXML
	void selectCongeEnMiseEnDisponibilite(ActionEvent event) {
		type = "Conge Mise en Disponibilite";
		menuCongeType.setText(type);
	}

	
	
	private void loadCongeResetTable() {
		loadConge();
		loadTableConge();
	}
	
	private void loadConge() {
		congeList = db.getAllCongs();
		
		ArrayList<Enseignant> engsList=db.getAllEnseignant();
		
		for (Conge conge : congeList) {
			for (Enseignant enseignant : engsList) {
				if(conge.enseignant_mat == enseignant.mat) {
					conge.getNomPrenomEnsg(enseignant);
				}
			}
		}
		
	}
	
	
	private Conge getSelectedItem() {
		if(isOneSelected()) {
			for (Conge congeSelected : congeList) {
				if (congeSelected.checkBox.selectedProperty().get() == true) {
					return congeSelected;
				}
			}
		}
		return null;
	}
	
	
//	private ArrayList<Conge> getListSelectedItems() {
//		ArrayList<Conge> selectedCongeList = new ArrayList<>();
//		
//		if(isOneSelected()) {
//			for (Conge notificationItem : notificationList) {
//				if (notificationItem.checkBox.selectedProperty().get() == true) {
//					selectedCongeList.add(notificationItem);
//				}
//			}
//		}
//		return selectedCongeList;
//	}
	@FXML
	private void Refrech() {
		loadCongeAndResetTable();
	}
	
	public void loadCongeAndResetTable() {
		congeList = db.getAllCongs();
		loadTableConge();
	}
	
	private Boolean isOneSelected() {
		for (Conge notificationItem : congeList) {
			if (notificationItem.checkBox.selectedProperty().get() == true) {
				return true;
			}
		}
		return false;
	}

	private Boolean isMultipleSelected() {
		int count = 0;
		for (Conge notificationItem : congeList) {
			if (notificationItem.checkBox.selectedProperty().get() == true) {
				count++;
				if (count > 1)
					return true;
			}
		}
		return false;
	}

	private void initCongeCheckBox() {
			
			if(congeList.isEmpty()) {
				addBtn.setDisable(false);
				removeBtn.setDisable(true);
				return ;
			}
		
			
			
			for (Conge	 conge : congeList) {
				conge.checkBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						if (isOneSelected()) {
							addBtn.setDisable(true);
							removeBtn.setDisable(false);
						} else {
							addBtn.setDisable(false);
							removeBtn.setDisable(true);
						}

						if (isMultipleSelected()) {
							addBtn.setDisable(true);
							removeBtn.setDisable(true);
						}

					}
				});
			}

		}
		
	private void loadTableConge() {
		initCongeCheckBox();
		
		ObservableList<Conge> list = FXCollections.observableArrayList(congeList);
		
		System.out.println(list.size());
		nomCellTable.setCellValueFactory(new PropertyValueFactory<Conge,String>("nom"));
		prenomCellTable.setCellValueFactory(new PropertyValueFactory<Conge,String>("prenom"));
		typeCellTable.setCellValueFactory(new PropertyValueFactory<Conge,String>("type"));
		dateDebutCellTable.setCellValueFactory(new PropertyValueFactory<Conge,LocalDate>("debut_cong"));
		dateFinCellTable.setCellValueFactory(new PropertyValueFactory<Conge,LocalDate>("fin_cong"));
		checkCellTable.setCellValueFactory(new PropertyValueFactory<Conge,CheckBox>("checkBox"));		

		CongeTableView.setItems(list);
		
	}
	
	
	// Navigation
	
	@FXML
	private void goToNotificationPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		
		Stage thisPage = routeController.getPage("congePage");
		thisPage.close();
		
		Stage notifacationPage = routeController.getPage("notificationPage");
		notifacationPage.show();
	}

	@FXML
	private void goToEnseignantPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		Stage notifacationPage = routeController.getPage("congePage");
		
		notifacationPage.close();

		Stage thisPage = routeController.getPage("EnseignantPage");
		thisPage.show();

	}
	@FXML 
	private void showEchlonPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		Stage thisPage = routeController.getPage("congePage");
		thisPage.close();
		

		Stage notificationPage = routeController.getPage("echlonPage");
		notificationPage.show();
	}

	@FXML
	private void goToCongePage() {
	
	}
	
	@FXML 
	private void showDepPage() {
		RouteController routeController = RouteController.getRouteController();
		Stage stage = routeController.getPage("depPage");
		stage.show();
	}
	
	  @FXML
	    void exit(ActionEvent event) {
		   System.exit(0);
	    }
	
}
