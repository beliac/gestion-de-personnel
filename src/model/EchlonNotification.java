package model;

import java.time.LocalDate;

import javafx.scene.control.CheckBox;

public class EchlonNotification {
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public int id;
    public String nom;
	public String prenom;
	public LocalDate date_debut;
	public LocalDate date_show;
	public String deja_vu ;
	
	public CheckBox checkBox = new CheckBox();
	

	
	public String getNom() {
		return this.nom;
	}
	
	public String getPrenom() {
		return this.prenom;
	}
	public LocalDate getDate_debut() {
		return this.date_debut;
	}
	
	public String getDeja_vu() {
		return this.deja_vu;
	}
	
	public LocalDate getDate_show() {
		return this.date_show;
	}
	
	public CheckBox getCheckBox() {
		return this.checkBox;
	}
}


