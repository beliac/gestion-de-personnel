package LayoutController;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import LayoutController.AbstractLayoutController;
import controller.RouteController;
import database.DataBaseController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Departement;
import model.Diplome;
import model.EchlonNotification;
import model.Enseignant;
import model.EnseignantPermanent;
import model.EnseignantStagerer;
import model.Mandat;
import model.Notification;

public class TestEnsg extends AbstractLayoutController {
	DataBaseController db = DataBaseController.getObjet();

	@FXML
	private ResourceBundle resources;

	@FXML
	private Scene hello;

	@FXML
	private URL location;

	@FXML
	private TextField textFieldNom;

	@FXML
	private TextField textFieldLieu;

	@FXML
	private TextField textFieldPrenom;

	@FXML
	private DatePicker dateNaissan;

	@FXML
	private RadioButton radioHomme;

	@FXML
	private ToggleGroup radioSexeGroup;

	@FXML
	private RadioButton radioFemme;

	@FXML
	private SplitMenuButton tfsf;

	@FXML
	private TextField textFieldTelephon;

	@FXML
	private TextField textFieldAdresse;

	@FXML
	private TextField textFieldEmail;

	@FXML
	private DatePicker dateConfirmation;

	@FXML
	private SplitMenuButton menuButtonDiplome;

	@FXML
	private TextField textFieldSpecialit;

	@FXML
	private MenuButton menuButtonGrade;

	@FXML
	private Spinner < Integer > spinerEchlon;

	@FXML
	private SplitMenuButton menuButtonMandat;

	@FXML
	private DatePicker dateObtentionMandat;

	@FXML
	private DatePicker tfdateobtentionmandat2;

	@FXML
	DatePicker finStage1;
	
    @FXML
    private TextField echlonYear;

    @FXML
    private TextField echlonMonth;

    @FXML
    private TextField echlonDay;

	@FXML
	private DatePicker dateObtentionEchlon;

	@FXML
	private DatePicker dateObtentionGrade;

	@FXML
	private DatePicker dateDiplomeObtention;

	@FXML
	private SplitMenuButton menuButtonDepartemnt;

	@FXML
	private DatePicker dateRecrutement;

	@FXML
	private SplitMenuButton menuButtonTypeEnsg;

	@FXML
	private DatePicker tfstage2;

	@FXML
	private DatePicker dateStage;

	@FXML
	private Button saveBtn;

	@FXML
	private Button cancelBtn;

	@FXML
	private CheckBox notificationStag;

	@FXML
	private MenuButton departementMenu;

	
	
	
	int departementId;
	String ensg_type = Enseignant.STAGERER;
	boolean selectedMandat = false;
	boolean selectedDiplome = false;

	// -------------------------------------
	// -------------------------------------
	// loading data
	// -------------------------------------
	// -------------------------------------
	
	@Override
	protected void loadData() {
		loadDepartement();

	}

	private void loadDepartement() {
		ArrayList < Departement > departementList = db.getAllDepartments();

		for (Departement departement: departementList) {
			MenuItem menuItem = new MenuItem(departement.intitule);

			menuItem.setOnAction(new EventHandler < ActionEvent > () {

				@Override
				public void handle(ActionEvent arg0) {
					departementMenu.setText(departement.intitule);
				}
			});

			departementMenu.getItems().add(menuItem);
		}

		if (departementList.isEmpty()) {
			departementMenu.setText("Aucun");
			departementId = -1;
		} {
			departementMenu.setText(departementList.get(0).intitule);
			departementId = departementList.get(0).mat;
		}

	}

	// -------------------------------------
	// -------------------------------------
	// loading widget
	// -------------------------------------
	// -------------------------------------
	@Override
	protected void loadWidget() {
		checkEnsgType("Stagiaire");
		//checkDiplome("Aucun");
	}

	// -------------------------------------
	// -------------------------------------
	// validate data
	// -------------------------------------
	// -------------------------------------
	@Override
	protected boolean runValidators() {
		if (!validatePersonalInfo()) {
			showCongeErrorMsg("personal info fields error");
			return false;
		}

		if (!validateDepartemtnInfo()) {
			showCongeErrorMsg("department info fields error");
			return false;
		}

		if (!validateDiplomInfo()) {
			showCongeErrorMsg("diplome fields error");
			return false;
		}

		if (!validateStageInfo()) {
			showCongeErrorMsg("sategier fields error");
			return false;
		}

		if (!validateMandatInfo()) {
			showCongeErrorMsg("mandat fields error");
			return false;
		}

		if (!validatePermanentInfo()) {
			showCongeErrorMsg("permanent fields error");
			return false;
		}
		
		if (!validateEchlonInfo()) {
			showCongeErrorMsg("echlon fields error");
			return false;
		}

		return true;
	}

	
	private boolean validateEchlonInfo() {
		if (menuButtonTypeEnsg.getText().equals("Stagiaire")) return true;
		
		if(dateObtentionEchlon.getValue()==null)return false;
		if(echlonYear.getText().isEmpty())return false;
		if(echlonMonth.getText().isEmpty())return false;
		if(echlonDay.getText().isEmpty())return false;
		
		try {
			int year = Integer.parseInt(echlonYear.getText());
			int month = Integer.parseInt(echlonMonth.getText());
			int day = Integer.parseInt(echlonDay.getText());
		}catch (Exception e) {
			return false;
		} 
		
		return true;
	}
	
	private boolean validatePersonalInfo() {
		if (textFieldNom.getText().isEmpty()) return false;
		if (textFieldPrenom.getText().isEmpty()) return false;
		if (textFieldLieu.getText().isEmpty()) return false;
		if (dateNaissan.getValue() == null) return false;
		if (textFieldTelephon.getText().isEmpty()) return false;
		if (textFieldAdresse.getText().isEmpty()) return false;
		if (textFieldEmail.getText().isEmpty()) return false;
		if (dateRecrutement.getValue() == null) return false;
		return true;
	}

	private boolean validateDepartemtnInfo() {
		if (departementId < 1) return false;
		return true;
	}

	private boolean validateDiplomInfo() {
		if (menuButtonDiplome.getText().equals("Aucun")) return true;

		if (textFieldSpecialit.getText().isEmpty()) return false;
		if (dateDiplomeObtention.getValue() == null) return false;

		return true;
	}

	private boolean validateStageInfo() {
		if (menuButtonTypeEnsg.getText().equals("Permanent")) return true;

		if (dateStage.getValue() == null) return false;
		if (finStage1.getValue() == null) return false;

		return true;
	}

	private boolean validatePermanentInfo() {
		if (menuButtonTypeEnsg.getText().equals("Stagiaire")) return true;

		if (dateConfirmation.getValue() == null) return false;
		if (dateObtentionGrade.getValue() == null) return false;

		return true;

	}

	private boolean validateMandatInfo() {
		if (menuButtonMandat.getText().equals("Aucun")) return true;

		if (dateObtentionMandat.getValue() == null) return false;

		return true;
	}

	// -------------------------------------
	// -------------------------------------
	// -------------------------------------
	// -------------------------------------
	// Notification Area
	// -------------------------------------
	// -------------------------------------
	private void createNotification() {

		String nom = textFieldNom.getText();
		String prenom = textFieldPrenom.getText();
		String contenu = "Confirmation Stagiaire " + nom + " " + prenom;
		LocalDate dateShow = finStage1.getValue();

		model.Notification notification = new Notification();

		notification.nom = nom;
		notification.prenom = prenom;
		notification.date_show = dateShow;
		notification.body = contenu;
		
		notification.type = "Confirmation Stagiaire";
		notification.deja_vu = Notification.FALSE;

		db.insertNotification(notification);

	}

	// -------------------------------------
	// -------------------------------------
	@FXML
	private void sauvgarde() {
		if (runValidators()) {
			String ensgType = menuButtonTypeEnsg.getText();
			if (ensgType.equals("Stagiaire")) {
				saveEnsStage();
			} else if (ensgType.equals("Permanent")) {
				saveEnseignantPermanent();
			}
		}
	}

	private Enseignant loadPersonalInformation() {
		Enseignant enseignant = new Enseignant();

		enseignant.nom = textFieldNom.getText();
		enseignant.departement_mat = departementId;
		enseignant.prenom = textFieldPrenom.getText();
		enseignant.dateNaissance = dateNaissan.getValue();
		enseignant.email = textFieldEmail.getText();
		enseignant.adresse = textFieldAdresse.getText();
		enseignant.telephone = textFieldTelephon.getText();
		enseignant.dateRecrutement = dateRecrutement.getValue();
		enseignant.enseignant_type = Enseignant.STAGERER;

		if (radioHomme.isSelected()) {
			enseignant.sexe = "H";
		} else {
			enseignant.sexe = "F";
		}

		return enseignant;
	}

	private Mandat loadMandat() {
		String mandatType = menuButtonMandat.getText();

		if (mandatType.equals("Aucun")) {
			return null;
		}

		Mandat mandat = new Mandat();
		mandat.intitule = mandatType;
		mandat.date_mandat = dateObtentionMandat.getValue();
		// TODO must be null at first
		mandat.fin_mandat = dateObtentionMandat.getValue().plusYears(3);

		return mandat;
	}

	private EnseignantStagerer loadStageData() {
		EnseignantStagerer enseignantStagerer = new EnseignantStagerer();

		enseignantStagerer.debut_stage = dateStage.getValue();
		enseignantStagerer.fin_stage = finStage1.getValue();

		return enseignantStagerer;
	}

	private Diplome loadDiplomData() {
		Diplome diplome = new Diplome();

		String diplomeName = menuButtonDiplome.getText();

	diplome.intitule = diplomeName;
		diplome.specialite = textFieldSpecialit.getText();
		diplome.date_obtention = dateDiplomeObtention.getValue();

		return diplome;
	}

	private void saveEnsStage() {
		try {

			Enseignant enseignant = loadPersonalInformation();
			EnseignantStagerer enseignantStagerer = loadStageData();
			Diplome diplome = loadDiplomData();
			Mandat mandat = loadMandat();

			enseignant.enseignant_type = Enseignant.STAGERER;

			try {
				enseignantStagerer.getDataFromEnseignant(enseignant);
			} catch(Exception e) {
				e.printStackTrace();
			}

			if (mandat != null) {
				db.insertMandat(mandat);
				int mandatId = db.getLastMandat();
				enseignantStagerer.mandat_id = mandatId;
				createMandatNotification(enseignant, mandat);
			} else {
				enseignantStagerer.mandat_id = -1;
			}

			if(db.insertEnseignantStagiere(enseignantStagerer)) {
				int enseignantStagiereId = db.getLastMatEnseignant();

				if (diplome != null) {

					diplome.enseignant_mat = enseignantStagiereId;

					db.insertDiplome(diplome);

				}

				if (notificationStag.isSelected()) {
					createStageNotification(enseignantStagerer);
				}
				showCongeAddMsg(enseignantStagerer);
			}else {
				showCongeErrorMsg("error save");
			}

			
		} catch(Exception e) {
			e.printStackTrace();
			showCongeErrorMsg("error save");
		}
	}

	private EnseignantPermanent loadEnseignantPermanent() {
		Enseignant enseignant = loadPersonalInformation();
		enseignant.enseignant_type = Enseignant.PERMANENT;
		Mandat mandat = loadMandat();

		EnseignantPermanent enseignantPermanent = new EnseignantPermanent();

		enseignantPermanent.getDataFromEnseignant(enseignant);

		enseignantPermanent.date_confirmation = dateConfirmation.getValue();

		if (mandat != null) {
			db.insertMandat(mandat);
			int mandatId = db.getLastMandat();
			enseignantPermanent.mandat_id = mandatId;
			createMandatNotification(enseignant, mandat);
		} else {
			enseignantPermanent.mandat_id = -1;
		}
		
		String grade = menuButtonGrade.getText();
		
		if (!grade.equals("Aucun")) {
			enseignantPermanent.grade = grade;
			enseignantPermanent.grade_date_decision = dateObtentionGrade.getValue();
		}
		

		enseignantPermanent.echlon = spinerEchlon.getValue();
		
		
		int year = Integer.parseInt(echlonYear.getText());
		int month= Integer.parseInt(echlonMonth.getText());
		int day= Integer.parseInt(echlonDay.getText());
		
		LocalDate debut_ech  = dateObtentionEchlon.getValue();
		
		LocalDate fin_ech = debut_ech.plusYears(year);
		fin_ech = fin_ech.plusMonths(month);
		fin_ech = fin_ech.plusDays(day);
		
		enseignantPermanent.debut_ech = debut_ech;
		enseignantPermanent.fin_ech = fin_ech;

		return enseignantPermanent;
	}

	private void saveEnseignantPermanent() {
		try {

			EnseignantPermanent enseignantPermanent = loadEnseignantPermanent();

		if(	db.insertEnseignantPermanent(enseignantPermanent)) {
			createEchlonNotification();
			showCongeAddMsg(enseignantPermanent);			
		}else {
			showCongeErrorMsg("Error ");
		}
		} catch(Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@FXML
	void menuItemAction(ActionEvent event) {
		MenuItem menuItem = (MenuItem) event.getSource();

		switch (menuItem.getText()) {
	/*	case "Informatique":
			menuButtonDepartemnt.setText("Informatique");
			break;
		case "Physique":
			menuButtonDepartemnt.setText("Physique");
			break;
		case "Mathematique":
			menuButtonDepartemnt.setText("Mathematique");
			break;
		case "Chimique":
			menuButtonDepartemnt.setText("Chimique");
			break;
		case "Genie civile ":
			menuButtonDepartemnt.setText("Genie civile ");
			break;
		case "Genie procédé":
			menuButtonDepartemnt.setText("Genie procédé");
			break;
		case "Genie mecanique":
			menuButtonDepartemnt.setText("Genie mecanique");
			break;
		case "Genie électrique":
			menuButtonDepartemnt.setText("Genie électrique");
			break;
		case "SM":
			menuButtonDepartemnt.setText("SM");
			break;*/

			// Diplome
		case "Doctorat":
			menuButtonDiplome.setText("Doctorat");
			break;
		case "Magistaire":
			menuButtonDiplome.setText("Magistaire");
			break;

			// type Ensg
		case "Stagiaire":
			menuButtonTypeEnsg.setText("Stagiaire");
			checkEnsgType("Stagiaire");
			break;
		case "Permanent":
			menuButtonTypeEnsg.setText("Permanent");
			checkEnsgType("Permanent");
			break;
			// Grade
		case "MAB":
			menuButtonGrade.setText("MAB");
			break;
		case "MAA":
			menuButtonGrade.setText("MAA");
			break;
		case "MCB":
			menuButtonGrade.setText("MCB");
			break;
		case "MCA":
			menuButtonGrade.setText("MCA");
			break;
		case "Professeur":
			menuButtonGrade.setText("Professeur");
			break;
			// Mandat
		case "doyen":
			menuButtonMandat.setText("doyen");
			break;
		case "chef departement":
			menuButtonMandat.setText("chef departement");
			break;
		case "Adjoin1":
			menuButtonMandat.setText("Adjoin1");
			break;
		case "Adjoin2":
			menuButtonMandat.setText("Adjoin2");
			break;
		default:
			break;
		}
	}

	private void checkEnsgType(String text) {
		boolean isStagiaire;
		if (text.equals("Stagiaire")) {
			isStagiaire = true;
		} else {
			isStagiaire = false;
		}

		// is stagiaire
		notificationStag.setDisable(!isStagiaire);
		dateStage.setDisable(!isStagiaire);
		finStage1.setDisable(!isStagiaire);

		// is not stagiaire 
		dateConfirmation.setDisable(isStagiaire);
		spinerEchlon.setDisable(isStagiaire);
		menuButtonGrade.setDisable(isStagiaire);
		dateObtentionGrade.setDisable(isStagiaire);
		dateObtentionEchlon.setDisable(isStagiaire);
		echlonDay.setDisable(isStagiaire);
		echlonMonth.setDisable(isStagiaire);
		echlonYear.setDisable(isStagiaire);
	}

	@FXML
	void aucunDiplome(ActionEvent event) {
		menuButtonDiplome.setText("Aucun");
	}


	@FXML
	void aucunPoste(ActionEvent event) {
		menuButtonMandat.setText("Aucun");
	}

	@FXML
	void back(ActionEvent event) {
		RouteController routeController = RouteController.getRouteController();
		Stage stage = routeController.getPage("addEnsgPage");
		stage.hide();
	}

	void showCongeAddMsg(Enseignant ens) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Enseignant ajouté avec succès");
		alert.setHeaderText("succes");
		String s = "Conge ajouter avec succee" + " pour Enseignant " + ens.toString();
		alert.setContentText(s);
		alert.show();
	}

	void showCongeErrorMsg(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("il y a des erreurs dans les champs");
		alert.setHeaderText("echec");
		String s = msg;
		alert.setContentText(s);
		alert.show();
	}

	private void createStageNotification(EnseignantStagerer ensg) {

		String nom = ensg.nom;
		String prenom = ensg.prenom;
		String contenu = "Ensignant confirmation" + " " + nom + " " + prenom;
		LocalDate dateShow = ensg.fin_stage;

		model.Notification notification = new Notification();

		notification.nom = nom;
		notification.prenom = prenom;
		notification.date_show = dateShow;
		notification.body = contenu;
		notification.type = "confirmation stage";
		notification.deja_vu = Notification.FALSE;

		db.insertNotification(notification);

	}
	//	
	private void createMandatNotification(Enseignant engs, Mandat mandat) {

		String nom = engs.nom;
		String prenom = engs.prenom;
		String contenu = "mandat " + " " + nom + " " + prenom;
		LocalDate dateShow = mandat.fin_mandat;

		model.Notification notification = new Notification();

		notification.nom = nom;
		notification.prenom = prenom;
		notification.date_show = dateShow;
		notification.body = contenu;
		notification.type = "fin mandat";
		notification.deja_vu = Notification.FALSE;

		db.insertNotification(notification);

	}
	
	
	private void createEchlonNotification() {
		EchlonNotification echlonNotification = new EchlonNotification();
		
		int year = Integer.parseInt(echlonYear.getText());
		int month= Integer.parseInt(echlonMonth.getText());
		int day= Integer.parseInt(echlonDay.getText());
		
		LocalDate debut_ech  = dateObtentionEchlon.getValue();
		
		LocalDate fin_ech = debut_ech.plusYears(year);
		fin_ech = fin_ech.plusMonths(month);
		fin_ech = fin_ech.plusDays(day);
		
		echlonNotification.nom = textFieldNom.getText();
		echlonNotification.prenom = textFieldPrenom.getText();
		echlonNotification.date_debut = debut_ech;
		echlonNotification.date_show = fin_ech;
		echlonNotification.deja_vu = EchlonNotification.FALSE;	
		
		db.insertEchlonNotification(echlonNotification);
	}
}