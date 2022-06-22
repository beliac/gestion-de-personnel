package model;

public class Departement {
	public int mat;
	public String intitule;
	
	// Constructors
	// ----------------------------------------------------

	public Departement(int mat, String intitule) {
		this.mat = mat;
		this.intitule = intitule;
	}

	
	public String getIntitule() {
		return this.intitule;
	}
	
	public String toString() {
		return this.intitule;
	}
	
	
	public Departement() {}
	
	
	
}

