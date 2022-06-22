package model;

import java.time.LocalDate;
import java.util.Date;

public class EnseignantStagerer extends Enseignant {
	public LocalDate debut_stage;
	public LocalDate fin_stage;

	public void getDataFromEnseignant(Enseignant enseignant) throws Exception {
		if(! enseignant.enseignant_type.equals(Enseignant.STAGERER)) {
			throw new Exception("Enseignant is not stagerer");
		}
		
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
}
