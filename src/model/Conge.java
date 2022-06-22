package model;

import java.time.LocalDate;
import java.util.Date;

import javafx.scene.control.CheckBox;

public class Conge {
	public static String MALADIE="maladie";
	public static String MISE_DIS="miseDisponibilite";
	public static String MATERNITE="maternite";
	
	public int id;
	public int enseignant_mat;
	public LocalDate debut_cong;
	public LocalDate fin_cong;
	public String type;
	
	public String nom ;
	public String prenom;
	
	public CheckBox checkBox = new CheckBox();
	
	
	public String getNom() {
		return this.nom;
	}
	
	public String getPrenom(){
		return this.prenom;
	}
	
	public void getNomPrenomEnsg(Enseignant enseignant) {
		this.nom = enseignant.nom;
		this.prenom = enseignant.prenom;
	}
	
	
	public CheckBox getCheckBox() {
		return this.checkBox;
	}
	
	public LocalDate getDebut_cong() {
		return this.debut_cong;
	}
	
	public LocalDate getFin_cong() {
		return this.fin_cong;
	}
	
	public String getType() {
		return this.type;
	}
	
	
}

