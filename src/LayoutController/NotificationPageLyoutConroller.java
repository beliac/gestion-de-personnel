package LayoutController;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.management.NotificationListener;

import controller.RouteController;
import database.DataBaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Enseignant;
import model.Notification;

public class NotificationPageLyoutConroller extends AbstractLayoutController {
	ArrayList<Notification> notificationList = new ArrayList<>();
	DataBaseController db = DataBaseController.getObjet();
	 
	
	Boolean listinging_show = false;
	
	final String ALL_NOTIFICATIONS = "all_notifications";
	final String SEEN_NOTIFICATIONS = "seen_notifications";
	final String NEW_NOTIFICATIONS = "new_notifications";

	String getNotificationByType = NEW_NOTIFICATIONS;

	@FXML
	private TableView<Notification> notificationTableView;
	
    @FXML
	private TableColumn<Notification, String> colcontenu;
    
	@FXML
	private TableColumn<Notification, String> nomCellTable;

	@FXML
	private TableColumn<Notification, String> preNomCellTable;

	@FXML
	private TableColumn<Notification, String> typeCellTable;

	@FXML
	private TableColumn<Notification, LocalDate> dateCellTable;

	@FXML
	private TableColumn<Notification, String> vuCellTable;

	@FXML
	private TableColumn<Notification, CheckBox> checkCellTable;

	@FXML
	private MenuButton menuNotifType;

	@FXML
	private Button addBtn;

	

	@FXML
	private Button seenBtn;

	@FXML
	private Button removeBtn;
	private Stage stage;
	

	@FXML
	void addAction(ActionEvent event) {
		RouteController routeController = RouteController.getRouteController();
		Stage stage = routeController.getPage("addNotificationPage");
		stage.show();
	}

	@FXML
	void removeAction(ActionEvent event) {
		Notification notif = getSelectedItem();

		db.deleteNotificaiton(notif.id);

		loadNotificationResetTable();
	}

	@FXML
	void seenAction(ActionEvent event) {
		Notification notif = getSelectedItem();

		notif.deja_vu = Notification.TRUE;

		db.updateNotification(notif);

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
			notificationList = db.getAllNotifications();
		} else if (getNotificationByType == SEEN_NOTIFICATIONS) {
			notificationList = db.getAllSeenNotifications();
		} else {
			notificationList = db.getAllNewNotifications();
		}
	}

	private Notification getSelectedItem() {
		if (isOneSelected()) {
			for (Notification notificationItem : notificationList) {
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
		for (Notification notificationItem : notificationList) {
			if (notificationItem.checkBox.selectedProperty().get() == true) {
				return true;
			}
		}
		return false;
	}

	private Boolean isMultipleSelected() {
		int count = 0;
		for (Notification notificationItem : notificationList) {
			if (notificationItem.checkBox.selectedProperty().get() == true) {
				count++;
				if (count > 1)
					return true;
			}
		}
		return false;
	}

	private void initNotificationCheckBox() {
		for (Notification notificationItem : notificationList) {
			notificationItem.checkBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					if (isOneSelected()) {
						addBtn.setDisable(true);
						
						removeBtn.setDisable(false);
						seenBtn.setDisable(false);
					} else {
						addBtn.setDisable(false);
						
						removeBtn.setDisable(true);
						seenBtn.setDisable(true);
					}

					if (isMultipleSelected()) {
					
						addBtn.setDisable(true);
						seenBtn.setDisable(true);
						removeBtn.setDisable(true);
					}

				}
			});
		}

	}
	

	private void loadTableNotification() {
		initNotificationCheckBox();

		ObservableList<Notification> list = FXCollections.observableArrayList(notificationList);

		//System.out.println(list.size());
		nomCellTable.setCellValueFactory(new PropertyValueFactory<Notification, String>("nom"));
		preNomCellTable.setCellValueFactory(new PropertyValueFactory<Notification, String>("prenom"));
		typeCellTable.setCellValueFactory(new PropertyValueFactory<Notification, String>("type"));
		dateCellTable.setCellValueFactory(new PropertyValueFactory<Notification, LocalDate>("date_show"));
		vuCellTable.setCellValueFactory(new PropertyValueFactory<Notification, String>("deja_vu"));
		colcontenu.setCellValueFactory(new PropertyValueFactory<Notification, String>("contenu"));
		checkCellTable.setCellValueFactory(new PropertyValueFactory<Notification, CheckBox>("checkBox"));

		notificationTableView.setItems(list);

	}
	

	// Navigation

	@FXML
	private void goToNotificationPage() {

	}

	@FXML
	private void goToEnseignantPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		Stage notifacationPage = routeController.getPage("notificationPage");
		notifacationPage.close();
		
		Stage thisPage = routeController.getPage("EnseignantPage");
		thisPage.show();
	}

	@FXML
	private void goToCongePage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		Stage thisPage = routeController.getPage("notificationPage");
		thisPage.close();
		

		Stage notifacationPage = routeController.getPage("congePage");
		notifacationPage.show();
	}
	
	@FXML 
	private void showDepPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();
		Stage stage = routeController.getPage("depPage");
		stage.show();
	}
	@FXML 
	private void goToEchlonPage() {
		loadWhenShow();
		RouteController routeController = RouteController.getRouteController();

		Stage thisPage = routeController.getPage("notificationPage");
		thisPage.close();
		

		Stage echlonPage = routeController.getPage("echlonPage");
		echlonPage.show();
	}
	//////////////
	void loadWhenShow() {
		if(listinging_show == false) {
			listinging_show = true;
			RouteController route = RouteController.getRouteController();
			Stage thisPage = route.getPage("notificationPage");
			
			thisPage.setOnShowing((ev)->{
				loadNotificationResetTable();
			});
		}
	}
	   @FXML
	    void exit(ActionEvent event) {
		   System.exit(0);
	    }
	@FXML 
	private void showEchlonPage() {}
	

}

