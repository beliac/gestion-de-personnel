package model;

import java.time.LocalDate;
import java.util.Date;

import javafx.scene.control.CheckBox;

public class Notification {
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public int id;
	public String type;
	public String nom;
	public String prenom;
	public String body;
	public LocalDate date_show;
	public String deja_vu ;
	
	
	public CheckBox checkBox = new CheckBox();
	public LocalDate date_debut;
	
	public String getType() {
		return this.type;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public String getPrenom() {
		return this.prenom;
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
	
	public String getContenu() {
		return this.body;
	}
}
