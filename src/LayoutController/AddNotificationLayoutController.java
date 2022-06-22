package LayoutController;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


import database.DataBaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Enseignant;
import model.Notification;

public class AddNotificationLayoutController implements Initializable{
	DataBaseController db = DataBaseController.getObjet();
	ArrayList<Enseignant> enseignantList = new ArrayList<>();
	Enseignant currentEnseignant ;
	boolean isSelected = false;
	@FXML
	private Label enseignantInfo;
    @FXML
    private TextField nameNotification;

    @FXML
    private TextField prenomNotification;

    @FXML
    private SplitMenuButton listButtonTypeNotification;

    @FXML
    private DatePicker dateNotification;

    @FXML
    private TextArea contenuTextArea;

    @FXML
    private Button declencherNotification;
    @FXML
    private TextField searchfield;

    @FXML
    private Button searchBtn;

    @FXML
    private ListView<Enseignant> ensgListInfo;

    @FXML
    private Button selectBtn;
    
    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    String type,notification;
    
   @FXML
	void selctNotificationCongeMaladie(ActionEvent event) {
		type = "Conge Maladie";
		listButtonTypeNotification.setText(type);
	}

	@FXML
	void selctNotificatioCongeMatirnite(ActionEvent event) {
		type = "Conge Matirnite";
		listButtonTypeNotification.setText(type);
	}

	@FXML
	void selctNotificationMiseEnDispo(ActionEvent event) {
		type = "Conge Mise en Disponibilite";
		listButtonTypeNotification.setText(type);
	}
	 @FXML
		void selctNotificationmandat(ActionEvent event) {
			type = "Mandat";
			listButtonTypeNotification.setText(type);
		}

		@FXML
		void selctNotificationechlon(ActionEvent event) {
			type = "Echlon";
			listButtonTypeNotification.setText(type);
		}

		@FXML
		void selctNotificationcostum(ActionEvent event) {
			type = "Au choix";
			listButtonTypeNotification.setText(type);
		}
		
		@FXML
		void confirmationDeStage(ActionEvent event) {
			type = "Confirmation de stage";
			listButtonTypeNotification.setText(type);
		}

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
	

    @FXML
    void selectNoticationMandat(ActionEvent event) {
    	type="Mandat";
    	
    	listButtonTypeNotification.setText(type);
    }

    @FXML
    void selectNotificationAllTypesConge(ActionEvent event) {
         type="Conge";
         listButtonTypeNotification.setText(type);
    }

    @FXML
    void selectNotificationConfirmationStage(ActionEvent event) {
    	type="Confirmation Stagiaire";
        listButtonTypeNotification.setText(type);
    }

    @FXML
    void selectNotificationCongeMaladie(ActionEvent event) {
    	type="Maladie";
        listButtonTypeNotification.setText(type);
    }

    @FXML
    void selectNotificationCongeMatirnite(ActionEvent event) {
    	type="Matirnite";
        listButtonTypeNotification.setText(type);
    }

    @FXML
    void selectNotificationCongeMiseEnDispo(ActionEvent event) {
    	type="Conge Mise en disponibilite";
        listButtonTypeNotification.setText(notification);
    }

    @FXML
    void selectNotificationEchlon(ActionEvent event) {
    	type="Echlon";
        listButtonTypeNotification.setText(notification);
    }
    
    
    @FXML 
    private void save() {
    	loadDataAndSave();
    }
    
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		type="Conge Mise en disponibilite";
		
	}
	
	

	
	private void loadDataAndSave() {
		String nom = currentEnseignant.nom;
		String prenom = currentEnseignant.prenom;
		//String contenu = type + " " + nom + " " + prenom;
		String contenu = contenuTextArea.getText();
		LocalDate dateShow = dateNotification.getValue();
		
		model.Notification notification = new Notification();  
		
		notification.nom = nom;
		notification.prenom = prenom;
		notification.date_show = dateShow;
		//notification.body = contenu;
		notification.body=contenu;
		
		notification.type = type;
		notification.deja_vu = Notification.FALSE;
		
		db.insertNotification(notification);
		
	}

}
