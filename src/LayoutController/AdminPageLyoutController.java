package LayoutController;


import database.DataBaseController;

import javax.management.relation.RelationTypeSupport;

import controller.RouteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Enseignant;

public class AdminPageLyoutController {
	DataBaseController db = DataBaseController.getObjet();

	@FXML
	private AnchorPane pane;

	@FXML
	private AnchorPane pane1;

	@FXML
	private Button b1;

	@FXML
	private Button b2;

	@FXML
	private Label l1;

	@FXML
	private Label L2;

	@FXML
	private Label L3;

	@FXML
	private TextField pseudoo;

	@FXML
	private PasswordField textPassword;

	@FXML
	private Label message;

	@FXML
	void handleButtonAction(ActionEvent event) {
		String user_name = pseudoo.getText();
		String user_pass = textPassword.getText();

		if (user_name == null || user_name.isEmpty()) {
			showLoginErrorMsg();
			return;
		}
		
		if (user_pass == null || user_pass.isEmpty()) {
			showLoginErrorMsg();			
			return;
		}

		
		if(db.userLogin(user_name,user_pass)) {
			RouteController routeController = RouteController.getRouteController();
			
			Stage notificationPage = routeController.getPage("notificationPage");
			Stage loginPage = routeController.getPage("loginPage");
			
			loginPage.hide();
			
			notificationPage.show();
			
		}else {
			showLoginErrorMsgUserNotExist();
		}
	}

	@FXML
	void handleCloseButtonAction(ActionEvent event) {
		System.exit(0);
	}
	
	
	

	
	void showLoginErrorMsg() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erreur");
		alert.setHeaderText("echec");
		String s =" Veuillez saisir vos informations";
		alert.setContentText(s);
		alert.show();
	}
	
	void showLoginErrorMsgUserNotExist() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erreur");
		alert.setHeaderText("echec");
		String s ="Ce user n'exist pas";
		alert.setContentText(s);
		alert.show();
	}
	 
	
}
