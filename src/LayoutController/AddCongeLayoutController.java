package LayoutController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import controller.RouteController;
import database.DataBaseController;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Conge;
import model.Departement;
import model.Enseignant;
import model.Notification;

public class AddCongeLayoutController {
	DataBaseController db = DataBaseController.getObjet();

	ArrayList<Enseignant> enseignantList = new ArrayList<>();
	Enseignant currentEnseignant ;
	
	boolean isSelected = false;
	String type = "Conge Matirnite";
	
	
	@FXML
	private SplitMenuButton congeTypeMenu;

	@FXML
	private MenuItem MenuButtonMatirnite;

	@FXML
	private MenuItem MenuButtonMiseEnDisponibilite;

	@FXML
	private MenuItem MenuButtonMaladie;

	@FXML
	private DatePicker finConge;

	@FXML
	private DatePicker debutConge;

	@FXML
	private Label enseignantInfo;

	@FXML
	private TextField searchfield;

	@FXML
	private Button searchBtn;

	@FXML
	private ListView<Enseignant> ensgListInfo;

	@FXML
	private Button selectBtn;

	

	@FXML
	private Button ajouterConge1;

	
	
	

	@FXML
	void searchAction(ActionEvent event) {
		//String nom = searchfield.getText();
		String prenom = searchfield.getText();
		
		if((prenom != null && ! prenom.isEmpty())) {
			enseignantList = db.searchEnseigant(prenom);
			loadenseignant();
			updateSelectButtonState();
		}
	}

	@FXML
	void selectAction(ActionEvent event) {
		Enseignant selectedEnseignant = getSelectedItem();
		if(selectedEnseignant != null) {
			currentEnseignant = selectedEnseignant;
			enseignantInfo.setText(selectedEnseignant.toString());
		}
	}

	

	@FXML
	void selctEnseignantEnCongeMaladie(ActionEvent event) {
		type = "Conge Maladie";
		congeTypeMenu.setText(type);
	}

	@FXML
	void selctEnseignantEnCongeMatirnite(ActionEvent event) {
		type = "Conge Matirnite";
		congeTypeMenu.setText(type);
	}

	@FXML
	void selectEnseignantEnMiseEnDisponibilite(ActionEvent event) {
		type = "Conge Mise en Disponibilite";
		congeTypeMenu.setText(type);
	}

	
	@FXML 
	private void ajouterNewConge() {
		if(currentEnseignant != null && debutConge.getValue() != null && finConge.getValue() != null) {
			Conge newConge = new Conge();
			
			newConge.enseignant_mat = currentEnseignant.mat;
			newConge.debut_cong = debutConge.getValue();
			newConge.fin_cong = finConge.getValue();
			newConge.type = type;
			
			db.insertCong(newConge);
			createCongeNotification();
			showCongeAddMsg();
		}
	}
	
	@FXML
	private void cancelPage() {
		RouteController route = RouteController.getRouteController();
		Stage thisPage = route.getPage("addCongePage");
		
		thisPage.close();
	}
	
	
	private void loadenseignant() {
		ObservableList<Enseignant> observableList = FXCollections.observableArrayList(enseignantList);

		ensgListInfo.setItems(observableList);

	}

	
	void updateSelectButtonState() {
		if(enseignantList.isEmpty()) {
			selectBtn.setDisable(true);
		}else {
			selectBtn.setDisable(false);
		}
	}
	
	Enseignant getSelectedItem() {
		Enseignant selectedItem = ensgListInfo.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			return selectedItem ;
		}
		return null;
	}
	
	void showCongeAddMsg() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Conge ajouter avec succe");
		alert.setHeaderText("succes");
		String s ="Conge ajouter avec succe" +" pour Enseignant "+ enseignantInfo.toString();
		alert.setContentText(s);
		alert.show();
	}
	
	
	private void createCongeNotification() {

		String nom = currentEnseignant.nom;
		String prenom = currentEnseignant.prenom;
		String contenu = type + " " + nom + " " + prenom;
		LocalDate dateShow = finConge.getValue();

		model.Notification notification = new Notification();

		notification.nom = nom;
		notification.prenom = prenom;
		notification.date_show = dateShow;
		notification.body = contenu;
		notification.type = type;
		notification.deja_vu = Notification.FALSE;

		db.insertNotification(notification);

	}
	
}