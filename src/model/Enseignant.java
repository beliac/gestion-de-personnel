package model;

import java.time.LocalDate;
import java.util.Date;

import javafx.scene.control.CheckBox;

public class Enseignant {
	public static String PERMANENT= "perm";
	public static String STAGERER = "stag";
	
	public int mat;
	public  int departement_mat;
	public int mandat_id;
	public String nom;
	public String prenom;
	public String sexe;
	public LocalDate dateNaissance;
	public String adresse;
	public String email;
	public  String mandat="Aucun";
	public String telephone;
	public LocalDate dateRecrutement;
	public String enseignant_type;
	
	
	public CheckBox checkBox = new CheckBox();
	
	// add to database
	public void ajouterEnseignant() {}
	// delete to database
	public void supprimerEnseignant() {}
	// update to database
	public void modifierEnseignant() {}
	
	
	// constructors
	
	public Enseignant() {}
	
	
	// getSet
	
	public String getNom() {
		return this.nom;
	}
	
	public String getPrenom() {
		return this.prenom;
	}
	
	
	public CheckBox getCheckbox() {
		return this.checkBox;
	}
	
	public String getEnseignant_type() {
		return this.enseignant_type;
	}
	
	public String getTelephone() {
		return this.telephone;
	}
	
	public String getEmail() {
		return this.email;
	}
	public String getMandat() {
		return this.mandat;
	}
	public String toString() {
		return this.nom + " " + this.prenom;
	}
}
