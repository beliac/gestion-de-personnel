package LayoutController;

import java.time.LocalDate;
import java.util.ArrayList;

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
import model.EchlonNotification;
import model.Notification;

public class EchlonNotificationPageLyoutConroller  extends AbstractLayoutController {
	ArrayList<EchlonNotification> echlonnotificationList = new ArrayList<>();
	DataBaseController db = DataBaseController.getObjet();
	
	Boolean listinging_show = false;
	
	final String ALL_NOTIFICATIONS = "all_notifications";
	final String SEEN_NOTIFICATIONS = "seen_notifications";
	final String NEW_NOTIFICATIONS = "new_notifications";

	String getNotificationByType = NEW_NOTIFICATIONS;

	@FXML
	private TableView<EchlonNotification> echlonNotificationTableView;

	@FXML
	private TableColumn<EchlonNotification, String> nomCellTable;

	@FXML
	private TableColumn<EchlonNotification, String> preNomCellTable;

	@FXML
	private TableColumn<EchlonNotification, LocalDate> datedebutCellTable;

	@FXML
	private TableColumn<EchlonNotification, LocalDate> datefinCellTable;

	@FXML
	private TableColumn<EchlonNotification, String> vuCellTable;

	@FXML
	private TableColumn<EchlonNotification, CheckBox> checkCellTable;

	@FXML
	private MenuButton menuNotifType;

	@FXML
	private Button addBtn;

	

	@FXML
	private Button seenBtn;

	@FXML
	private Button removeBtn;
	

	@FXML
	void removeAction(ActionEvent event) {
		EchlonNotification notif = getSelectedItem();

		db.deleteEchlonNotification(notif.id);

		loadNotificationResetTable();
	}

	@FXML
	void seenAction(ActionEvent event) {
		EchlonNotification notif = getSelectedItem();

		notif.deja_vu = Notification.TRUE;

		db.updateEchlonNotification(notif);

		loadNotificationResetTable();
	}

	@FXML
	void showAction(ActionEvent event) {

	}

	@FXML
	void getNewNotificationAction(ActionEvent event) {
		menuNotifType.setText("New");
		getNotificationByType = NEW_NOTIFICATIONS;
		loadNotificationResetTable();
	}

	@FXML
	void getAllNotificationAction(ActionEvent event) {
		menuNotifType.setText("All");
		getNotificationByType = ALL_NOTIFICATIONS;
		loadNotificationResetTable();
	}

	@FXML
	void getSeenNotificationAction(ActionEvent event) {
		menuNotifType.setText("Seen");
		getNotificationByType = SEEN_NOTIFICATIONS;
		loadNotificationResetTable();
	}
	
	@FXML 
	void refrech() {
		loadNotificationResetTable();
	}
	
	// ----------------------------------------------
	// ----------------------------------------------
	// My functions
	// ----------------------------------------------
	// ----------------------------------------------

	@Override
	protected void loadData() {
		loadNotification();
	}

	@Override
	protected void loadWidget() {
		loadTableNotification();

	}

	@Override
	protected boolean runValidators() {
		return true;
	}

	private void loadNotificationResetTable() {
		loadNotification();
		loadTableNotification();
	}

	private void loadNotification() {
		if (getNotificationByType == ALL_NOTIFICATIONS) {
			echlonnotificationList = db.getAllEchlonNotification();
		} else if (getNotificationByType == SEEN_NOTIFICATIONS) {
			echlonnotificationList = db.getAllSeenEchlon();
		} else {
			echlonnotificationList = db.getAllNewEchlonNotification();
		}
	}

	private EchlonNotification getSelectedItem() {
		if (isOneSelected()) {
			for (EchlonNotification notificationItem : echlonnotificationList) {
				if (notificationItem.checkBox.selectedProperty().get() == true) {
					return notificationItem;
				}
			}
		}
		return null;
	}

//	private ArrayList<Notification> getListSelectedItems() {
//		ArrayList<Notification> selectedNotificationList = new ArrayList<>();
//		
//		if(isOneSelected()) {
//			for (Notification notificationItem : notificationList) {
//				if (notificationItem.checkBox.selectedProperty().get() == true) {
//					selectedNotificationList.add(notificationItem);
//				}
//			}
//		}
//		return selectedNotificationList;
//	}

	private Boolean isOneSelected() {
		for (EchlonNotification notificationItem : echlonnotificationList) {
			if (notificationItem.checkBox.selectedProperty().get() == true) {
				return true;
			}
		}
		return false;
	}

	private Boolean isMultipleSelected() {
		int count = 0;
		for (EchlonNotification notificationItem : echlonnotificationList) {
			if (notificationItem.checkBox.selectedProperty().get() == true) {
				count++;
				if (count > 1)
					return true;
			}
		}
		return false;
	}

	private void initNotificationCheckBox() {
		for (EchlonNotification notificationItem : echlonnotificationList) {
			notificationItem.checkBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					if (isOneSelected()) {
						
						
						removeBtn.setDisable(false);
						seenBtn.setDisable(false);
					} else {
						
						
						removeBtn.setDisable(true);
						seenBtn.setDisable(true);
					}

					if (isMultipleSelected()) {
						seenBtn.setDisable(true);
						removeBtn.setDisable(true);
					}

				}
			});
		}

	}

	private void loadTableNotification() {
		initNotificationCheckBox();

		ObservableList<EchlonNotification> list = FXCollections.observableArrayList(echlonnotificationList);

		System.out.println(list.size());
		nomCellTable.setCellValueFactory(new PropertyValueFactory<EchlonNotification, String>("nom"));
		preNomCellTable.setCellValueFactory(new PropertyValueFactory<EchlonNotification, String>("prenom"));
		datedebutCellTable.setCellValueFactory(new PropertyValueFactory<EchlonNotification, LocalDate>("date_debut"));
		datefinCellTable.setCellValueFactory(new PropertyValueFactory<EchlonNotification, LocalDate>("date_show"));
		vuCellTable.setCellValueFactory(new PropertyValueFactory<EchlonNotification, String>("deja_vu"));
		checkCellTable.setCellValueFactory(new PropertyValueFactory<EchlonNotification, CheckBox>("checkBox"));

		echlonNotificationTableView.setItems(list);

	}

	// Navigation

	@FXML
	private void goToNotificationPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();
		
		Stage thisPage = routeController.getPage("echlonPage");
		thisPage.hide();
		

		Stage notificationPage = routeController.getPage("notificationPage");
		notificationPage.show();
	}

	@FXML
	private void goToEnseignantPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		Stage notificationPage = routeController.getPage("echlonPage");
		notificationPage.close();
		
		Stage thisPage = routeController.getPage("EnseignantPage");
		thisPage.show();
	}

	@FXML
	private void goToCongePage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		Stage thisPage = routeController.getPage("echlonPage");
		thisPage.close();
		

		Stage notificationPage = routeController.getPage("congePage");
		notificationPage.show();
	}
	
	@FXML 
	private void showDepPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();
		Stage stage = routeController.getPage("depPage");
		stage.show();
	}
	@FXML 
	private void showEchlonPage() {
	
	}
	
	void loadWhenShow() {
		if(listinging_show == false) {
			listinging_show = true;
			RouteController route = RouteController.getRouteController();
			Stage thisPage = route.getPage("echlonPage");
			
			thisPage.setOnShowing((ev)->{
				loadNotificationResetTable();
			});
		}
	}

	@FXML
	private void goToEchlonPage() {
		
	}
	 @FXML
	    void exit(ActionEvent event) {
		   System.exit(0);
	    }
}
