package model;

import java.time.LocalDate;
import java.util.Date;

public class EnseignantPermanent extends Enseignant{
	public String grade;
	public int echlon;
	public LocalDate debut_ech;
	public LocalDate fin_ech;
	public LocalDate date_confirmation;
	public LocalDate grade_date_decision;

	public void getDataFromEnseignant(Enseignant enseignant) {
		this.mat = enseignant.mat;
		this.departement_mat = enseignant.departement_mat;
		this.mandat_id = enseignant.mandat_id;
		this.nom = enseignant.nom;
		this.prenom = enseignant.prenom;
		this.sexe = enseignant.sexe;
		this.dateNaissance = enseignant.dateNaissance;
		this.adresse = enseignant.adresse;
		this.email = enseignant.email;
		this.telephone = enseignant.telephone;
		this.dateRecrutement = enseignant.dateRecrutement;
		this.enseignant_type = enseignant.enseignant_type;
	}
	
//	public Grade getGrade() {}
//	public Echlon getEchlon() {}
//	public EchlonDate getEchlonDate() {}
}
