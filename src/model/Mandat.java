package model;

import java.time.LocalDate;
import java.util.Date;

public class Mandat {

	public int id;
	public String intitule;
	public LocalDate date_mandat;
	public LocalDate fin_mandat;

	public Mandat() {
	}

	public Mandat(int id, String intitule, LocalDate date_mDate, LocalDate fin_manDate) {
		this.id = id;
		this.intitule = intitule;
		this.date_mandat = date_mDate;
		this.fin_mandat = fin_manDate;
	}
	
	public Mandat(String intitule, LocalDate date_mDate, LocalDate fin_manDate) {
		this.intitule = intitule;
		this.date_mandat = date_mDate;
		this.fin_mandat = fin_manDate;
	}
}
