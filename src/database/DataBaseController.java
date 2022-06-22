package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;



import model.Conge;
import model.Departement;
import model.Diplome;
import model.Echlon;
import model.EchlonDate;
import model.EchlonNotification;
import model.Enseignant;
import model.EnseignantPermanent;
import model.EnseignantStagerer;
import model.Grade;
import model.Historiqu;
import model.HistoriqueMandat;
import model.Mandat;
import model.Notification;

public class DataBaseController {
	
	Connection con;
	Statement statement ;
	
	public Boolean isConnected = false;

	private static final DataBaseController db = new DataBaseController();
	
	String url = "jdbc:mysql://localhost:3306/univ_database?allowMultiQueries=TRUE";	
	String database_name = "univ_database"; 
	String uname = "root";
	String upassword = "261266";
	
	

	// Constructor
	private DataBaseController() {
		if(isConnected() == false) {
			connect();
		}
	}

	
	static public DataBaseController getObjet() {
		return db;
	}
	
	
	
	public Boolean isConnected() {
		return isConnected;
	}
	
	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 con = DriverManager.getConnection(url,uname,upassword);
			 statement = con.createStatement();
			isConnected = true;	
			
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			isConnected = false;
			
		}catch (Exception e) {
			e.printStackTrace();
			isConnected = false;		
		}		
	}
	
	// Database AREA
	// ---------------------------------------------------------
	public void createAllTables() {
		createTables();
	}

	public void dropAllTable() {
		dropAlltables();
	}

	// Departement CRUD [CREATE,READ,UPDATE,DELETE]
	public void createDepartmenetTable() {
		String query = "create table Departement(\n" + "	mat INT NOT NULL AUTO_INCREMENT,\n"
				+ "	intitule VARCHAR(100) NOT NULL,\n" + "    PRIMARY KEY ( mat )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Departement getDepartmenetByMatricule(int mat_query) {
		Departement dep = null;

		String query = "SELECT * FROM Departement WHERE mat=" + mat_query + " ;";

		try {
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				int mat = rs.getInt("mat");
				String intitule = rs.getString("intitule");

				dep = new Departement(mat, intitule);
			}

			return dep;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Departement> getAllDepartments() {
		ArrayList<Departement> depList = new ArrayList<Departement>();

		String query = "SELECT * FROM Departement ;";

		try {
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				int mat = rs.getInt("mat");
				String intitule = rs.getString("intitule");

				depList.add(new Departement(mat, intitule));
			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return depList;
		}
	}

	public void insertDepartmenet(Departement departement) {
//		String query = "INSERT INTO Departement(mat,intitule)" + " VALUES(" + dep.mat + ",'" + dep.intitule + "');";

		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO Departement(intitule) VALUES(?);");
			ps.setString(1, departement.intitule);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDepartmenet(Departement dep) {
		String query = "UPDATE Departement " + "SET intitule='" + dep.intitule + "'" + " WHERE mat=" + dep.mat + ";";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteDepartment(int mat) {
		String query = "DELETE FROM Departement WHERE mat=" + mat + ";";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Diplome
//	public void createDiplomeTable() {
//	}

	public Diplome getDiplomeByMatricule(int mat) {
		Diplome diplome = null;

		try {

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Diplome WHERE id=?;");

			ps.setInt(1, mat);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				diplome = new Diplome();

				diplome.mat = rs.getInt("mat");
				diplome.enseignant_mat = rs.getInt("enseignant_mat");
//				diplome.code_dip = rs.getInt("code_dip");
				diplome.intitule = rs.getString("intitule");
				diplome.date_obtention = dateToLocaDate(rs.getDate("date_obtention"));
				diplome.specialite = rs.getString("specialite");

			}

			return diplome;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Diplome> getAllDiplomes() {
		ArrayList<Diplome> diplomeList = new ArrayList<>();

		try {

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Diplome;");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Diplome diplome = new Diplome();

				diplome.mat = rs.getInt("mat");
				diplome.enseignant_mat = rs.getInt("enseignant_mat");
//				diplome.code_dip = rs.getInt("code_dip");
				diplome.intitule = rs.getString("intitule");
				diplome.date_obtention = dateToLocaDate(rs.getDate("date_obtention"));
				diplome.specialite = rs.getString("specialite");

				diplomeList.add(diplome);

			}

			return diplomeList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertDiplome(Diplome diplome) {

		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO Diplome "
					+ "(enseignant_mat, intitule, date_obtention, specialite)" + " VALUES(?,?,?,?);");

			ps.setInt(1, diplome.enseignant_mat);
//			ps.setInt(2, diplome.code_dip);
			ps.setString(2, diplome.intitule);
			ps.setString(3, getMysqlDate(diplome.date_obtention));
			ps.setString(4, diplome.specialite);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateDiplome(Diplome diplome) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Diplome SET " + "enseignant_mat=?,"
					+ " intitule=?," + " date_obtention=?," + " specialite=?" + " WHERE mat=?;");

			ps.setInt(1, diplome.enseignant_mat);
//			ps.setInt(2, diplome.code_dip);
			ps.setString(2, diplome.intitule);
			ps.setString(3, getMysqlDate(diplome.date_obtention));
			ps.setString(4, diplome.specialite);
			ps.setInt(5, diplome.mat);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Mandat
//	public void createMandatTable() {
//		String query = "create table Mandat(\n" + "	id INT NOT NULL AUTO_INCREMENT," + "intitule VARCHAR(100) NOT NULL,"
//				+ "	date_mandat DATE NOT NULL,\n" + "	fin_mandat DATE NOT NULL,\n" + "    PRIMARY KEY ( id )\n"
//				+ ");";
//
//		try {
//			statement.executeUpdate(query);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

//	public void insertAllMandat() {
//		String query = "insert into Mandat VALUES "
//				+ "(?,?,?,?),"
//				+ "(?,?,?,?),"
//				+ "(?,?,?,?),"
//				+ "(?,?,?,?),"
//				+ "(?,?,?,?)"
//				;
//		
//		try {
//			PreparedStatement pr = con.prepareStatement(query);
//			pr.in
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public Mandat getMandatById(int id) {
		Mandat mandat = null;

		String query = "SELECT * FROM Mandat WHERE id=" + id + " ;";

		try {
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				int mat = rs.getInt("id");
				String intitule = rs.getString("intitule");
				Date date_mandat = rs.getDate("date_mandat");
				Date fin_mandat = rs.getDate("fin_mandat");
				mandat = new Mandat(mat, intitule, dateToLocaDate(date_mandat), dateToLocaDate(fin_mandat));
			}

			return mandat;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Mandat> getAllMandats() {
		ArrayList<Mandat> mandatList = new ArrayList<Mandat>();

		String query = "SELECT * FROM Mandat ;";

		try {
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				int id = rs.getInt("id");
				String intutle = rs.getString("intitule");
				Date date_mandat = rs.getDate("date_mandat");
				Date fin_mandat = rs.getDate("fin_mandat");

				mandatList.add(new Mandat(id, intutle, dateToLocaDate(date_mandat), dateToLocaDate(fin_mandat)));
			}

			return mandatList;

		} catch (Exception e) {
			e.printStackTrace();
			return mandatList;
		}
	}

	public void insertMandat(Mandat mandat) {

		try {
			PreparedStatement ps = con
					.prepareStatement("insert into Mandat (intitule,date_mandat,fin_mandat) values(?,?,?);");

			ps.setString(1, mandat.intitule);
			ps.setString(2, getMysqlDate(mandat.date_mandat));
			ps.setString(3, getMysqlDate(mandat.fin_mandat));

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateMandat(Mandat mandat) {

		try {
			PreparedStatement ps = con
					.prepareStatement("UPDATE Mandat SET intitule=? , date_mandat=? , fin_mandat=? WHERE id=?;");

			ps.setString(1, mandat.intitule);
			ps.setString(2, getMysqlDate(mandat.date_mandat));
			ps.setString(3, getMysqlDate(mandat.fin_mandat));
			ps.setInt(4, mandat.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteMandat(int id) {

		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Mandat WHERE id=?;");

			ps.setInt(1, id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getLastMandat() {
		String query = "SELECT MAX( id ) AS id FROM Mandat;";
		try {
			int lastId = 0;
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				lastId = rs.getInt("id");
			}

			return lastId;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// ------------------------------------------------------------
	// Enseignant


	public Enseignant getEnseignantByMatricule(int mat) {
		Enseignant enseignant = null;
		try {
			enseignant = new Enseignant();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Enseignant WHERE mat=?;");
			ps.setInt(1, mat);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				enseignant.mat = rs.getInt("mat");
				enseignant.departement_mat = rs.getInt("departement_mat");
				enseignant.mandat_id = rs.getInt("mandat_id");
				enseignant.nom = rs.getString("nom");
				enseignant.prenom = rs.getString("prenom");
				enseignant.sexe = rs.getString("sexe");
				enseignant.dateNaissance = dateToLocaDate(rs.getDate("date_naissance"));
				enseignant.adresse = rs.getString("adress");
				enseignant.email = rs.getString("email");
				enseignant.telephone = rs.getString("telephone");
				enseignant.dateRecrutement = dateToLocaDate(rs.getDate("data_recurtement"));
				enseignant.enseignant_type = rs.getString("enseignant_type");
			}

			return enseignant;
		} catch (Exception e) {
			e.printStackTrace();
			return enseignant;
		}
	}

	public ArrayList<Enseignant> getAllEnseignant() {
		ArrayList<Enseignant> enseignantList = new ArrayList<Enseignant>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Enseignant  ;");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Enseignant enseignant = new Enseignant();
				enseignant.mat = rs.getInt("mat");
				enseignant.departement_mat = rs.getInt("departement_mat");
				enseignant.mandat_id = rs.getInt("mandat_id");
				enseignant.nom = rs.getString("nom");
				enseignant.prenom = rs.getString("prenom");
				enseignant.sexe = rs.getString("sexe");
				enseignant.dateNaissance = dateToLocaDate(rs.getDate("date_naissance"));
				enseignant.adresse = rs.getString("adress");
				enseignant.email = rs.getString("email");
				enseignant.telephone = rs.getString("telephone");
				enseignant.dateRecrutement = dateToLocaDate(rs.getDate("data_recurtement"));
				enseignant.enseignant_type = rs.getString("enseignant_type");

				enseignantList.add(enseignant);
			}
			return enseignantList;
		} catch (Exception e) {
			e.printStackTrace();
			return enseignantList;
		}
	}

	public boolean insertEnseignant(Enseignant enseignant) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO Enseignant "
					+ "(departement_mat, mandat_id, nom," + " prenom, sexe, date_naissance," + " adress, email,"
					+ " telephone, data_recurtement, enseignant_type)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?);");

			ps.setInt(1, enseignant.departement_mat);

			if (enseignant.mandat_id < 1) {
				ps.setNull(2, Types.INTEGER);
			} else {
				ps.setInt(2, enseignant.mandat_id);
			}

			ps.setString(3, enseignant.nom);
			ps.setString(4, enseignant.prenom);
			ps.setString(5, enseignant.sexe);
			ps.setString(6, getMysqlDate(enseignant.dateNaissance));
			ps.setString(7, enseignant.adresse);
			ps.setString(8, enseignant.email);
			ps.setString(9, enseignant.telephone);
			ps.setString(10, getMysqlDate(enseignant.dateRecrutement));
			ps.setString(11, enseignant.enseignant_type);

			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	}

	public void updateEnseignant(Enseignant enseignant) {
		try {
			String query = "UPDATE Enseignant SET" + " departement_mat=? , mandat_id=? , nom=?,"
					+ " prenom=? , sexe=? , date_naissance=? ," + " adress=? , email=? , telephone=?,"
					+ " data_recurtement=? , enseignant_type=?" + " WHERE mat=?;";
			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, enseignant.departement_mat);

			if (enseignant.mandat_id < 1) {
				ps.setNull(2, Types.INTEGER);
			} else {
				ps.setInt(2, enseignant.mandat_id);
			}

			ps.setString(3, enseignant.nom);
			ps.setString(4, enseignant.prenom);
			ps.setString(5, enseignant.sexe);
			ps.setString(6, getMysqlDate(enseignant.dateNaissance));
			ps.setString(7, enseignant.adresse);
			ps.setString(8, enseignant.email);
			ps.setString(9, enseignant.telephone);
			ps.setString(10, getMysqlDate(enseignant.dateRecrutement));
			ps.setString(11, enseignant.enseignant_type);

			ps.setInt(12, enseignant.mat);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getLastMatEnseignant() {
		String query = "SELECT MAX( mat ) AS mat FROM Enseignant;";
		try {
			int lastId = 0;
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				lastId = rs.getInt("mat");
			}

			return lastId;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public ArrayList<Enseignant> searchEnseigant(String prenom) {
		ArrayList<Enseignant> enseignantList = new ArrayList<>();

		String query = "SELECT * FROM Enseignant WHERE prenom LIKE ?";

		try {

			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, prenom.concat("%"));
			

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Enseignant enseignant = new Enseignant();
				enseignant.mat = rs.getInt("mat");
				enseignant.departement_mat = rs.getInt("departement_mat");
				enseignant.mandat_id = rs.getInt("mandat_id");
				enseignant.nom = rs.getString("nom");
				enseignant.prenom = rs.getString("prenom");
				enseignant.sexe = rs.getString("sexe");
				enseignant.dateNaissance = dateToLocaDate(rs.getDate("date_naissance"));
				enseignant.adresse = rs.getString("adress");
				enseignant.email = rs.getString("email");
				enseignant.telephone = rs.getString("telephone");
				enseignant.dateRecrutement = dateToLocaDate(rs.getDate("data_recurtement"));
				enseignant.enseignant_type = rs.getString("enseignant_type");

				enseignantList.add(enseignant);
			}

			return enseignantList;
		} catch (Exception e) {
			e.printStackTrace();
			return enseignantList;
		}
	}
	
	public void deleteEnseignant(int mat) {
		String query = "DELETE FROM Enseignant WHERE mat=" + mat + ";";

		try {
			Enseignant ensg = db.getEnseignantByMatricule(mat);
			
			if(ensg.enseignant_type.equals(Enseignant.STAGERER)) {
				deleteEnseignantStagerer(mat);
			}else {
				deleteEnseignantPermanent(mat);
			}
			
			deleteCongeByEnsgMat(mat);
			deleteDiplome(mat);
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteDiplome(int mat) {
		String query = "DELETE FROM Diplome WHERE enseignant_mat=" + mat + ";";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCongeByEnsgMat(int mat) {
		String query = "DELETE FROM Conge WHERE enseignant_mat=" + mat + ";";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------
	// ------------------------------------------------------
	// ------------------------------------------------------
	// Enseignant Stageiere
	public void createEnseignantStagiereTable() {

	}

	public EnseignantStagerer getEnseignantStagiereByMatricule(int mat) {
		EnseignantStagerer enseignantStagerer = new EnseignantStagerer();
		try {
			Enseignant enseignant = getEnseignantByMatricule(mat);
			if (enseignant == null) {
				throw new Exception();
			}
			enseignantStagerer.getDataFromEnseignant(enseignant);

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Enseignant_stagerer WHERE mat=?;");
			ps.setInt(1, mat);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				enseignantStagerer.debut_stage = dateToLocaDate(rs.getDate("debut_stage"));
				enseignantStagerer.fin_stage = dateToLocaDate(rs.getDate("fin_stage"));
			}

			return enseignantStagerer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<EnseignantStagerer> getAllEnseignantStagiere() {
		ArrayList<EnseignantStagerer> enseignantStagererList = new ArrayList<>();
		try {
			ArrayList<Enseignant> enseignantList = getAllEnseignant();
			if (enseignantList == null) {
				throw new Exception();
			}

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Enseignant_stagerer");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				EnseignantStagerer enseignantStagerer = new EnseignantStagerer();
				enseignantStagerer.debut_stage = dateToLocaDate(rs.getDate("debut_stage"));
				enseignantStagerer.fin_stage = dateToLocaDate(rs.getDate("fin_stage"));
				enseignantStagerer.mat = rs.getInt("mat");

				for (int i = 0; i < enseignantList.size(); i++) {
					Enseignant tempEnseignant = enseignantList.get(i);
					if (tempEnseignant.mat == enseignantStagerer.mat) {
						enseignantStagerer.getDataFromEnseignant(tempEnseignant);
						break;
					}
				}

				if (enseignantStagerer.nom == null)
					throw new Exception();

				enseignantStagererList.add(enseignantStagerer);

			}
			return enseignantStagererList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean insertEnseignantStagiere(EnseignantStagerer enseignantStagerer) {

		try {
			insertEnseignant(enseignantStagerer);

			PreparedStatement ps = con
					.prepareStatement("insert into Enseignant_stagerer (mat,debut_stage,fin_stage) values(?,?,?)");

			int lastId = getLastMatEnseignant();
			ps.setInt(1, lastId);
			ps.setString(2, getMysqlDate(enseignantStagerer.debut_stage));
			ps.setString(3, getMysqlDate(enseignantStagerer.fin_stage));

			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void updateEnseignantStagiere(EnseignantStagerer enseignantStagerer) {
		try {

			Enseignant enseignant = getEnseignantByMatricule(enseignantStagerer.mat);

			if (!enseignant.enseignant_type.equals(Enseignant.STAGERER)) {
				System.out.println(enseignant.enseignant_type);
				throw new Exception("Enseignant is not stagerer");
			}

			updateEnseignant(enseignantStagerer);

			PreparedStatement ps = con.prepareStatement(
					"UPDATE Enseignant_stagerer SET " + " debut_stage=? ," + " fin_stage=? " + " WHERE mat=?;");

			ps.setString(1, getMysqlDate(enseignantStagerer.debut_stage));
			ps.setString(2, getMysqlDate(enseignantStagerer.fin_stage));
			ps.setInt(3, enseignantStagerer.mat);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteEnseignantStagerer(int mat) {
		String query = "DELETE FROM Enseignant_stagerer WHERE mat=" + mat + ";";

		try {
//			deleteEnseignant(mat);
			statement.executeUpdate(query);
		} catch (Exception e) {
//			System.out.println("this is the prblm");
			e.printStackTrace();
		}
	}

	// Enseignant Permanent


	public EnseignantPermanent getEnseignantPermanentByMatricule(int mat) {
		EnseignantPermanent enseignantPermanent = new EnseignantPermanent();
		try {
			Enseignant enseignant = getEnseignantByMatricule(mat);
			if (enseignant == null) {
				throw new Exception();
			}
			enseignantPermanent.getDataFromEnseignant(enseignant);

			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM Enseignant_permanent WHERE mat=?;");
			ps.setInt(1, mat);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				enseignantPermanent.mat = rs.getInt("mat");
				enseignantPermanent.grade = rs.getString("grade");
				enseignantPermanent.echlon = rs.getInt("echlon");
				enseignantPermanent.debut_ech = dateToLocaDate(rs.getDate("debut_ech"));
				enseignantPermanent.fin_ech = dateToLocaDate(rs.getDate("fin_ech"));
				enseignantPermanent.date_confirmation = dateToLocaDate(rs.getDate("date_confirmation"));
				enseignantPermanent.grade_date_decision = dateToLocaDate(rs.getDate("grade_date_decision"));

			}

			return enseignantPermanent;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<EnseignantPermanent> getAllEnseignantPermanent() {
		ArrayList<EnseignantPermanent> enseignantPermanentsList = new ArrayList<>();
		try {
			ArrayList<Enseignant> enseignantList = getAllEnseignant();
			if (enseignantList == null) {
				throw new Exception();
			}

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Enseignant_permanent");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				EnseignantPermanent enseignantPermanent = new EnseignantPermanent();

				enseignantPermanent.mat = rs.getInt("mat");
				enseignantPermanent.grade = rs.getString("grade");
				enseignantPermanent.echlon = rs.getInt("echlon");
				enseignantPermanent.debut_ech = dateToLocaDate(rs.getDate("debut_ech"));
				enseignantPermanent.fin_ech = dateToLocaDate(rs.getDate("fin_ech"));
				enseignantPermanent.date_confirmation = dateToLocaDate(rs.getDate("date_confirmation"));
				enseignantPermanent.grade_date_decision = dateToLocaDate(rs.getDate("grade_date_decision"));

				for (int i = 0; i < enseignantList.size(); i++) {
					Enseignant tempEnseignant = enseignantList.get(i);
					if (tempEnseignant.mat == enseignantPermanent.mat) {
						enseignantPermanent.getDataFromEnseignant(tempEnseignant);
						break;
					}
				}

				if (enseignantPermanent.nom == null)
					throw new Exception();

				enseignantPermanentsList.add(enseignantPermanent);

			}
			return enseignantPermanentsList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean insertEnseignantPermanent(EnseignantPermanent enseignantPermanent) {
		try {
			if(insertEnseignant(enseignantPermanent) == false)return false;	

			int lastId = getLastMatEnseignant();

			PreparedStatement ps = con.prepareStatement("insert into Enseignant_permanent "
					+ "(mat,grade,grade_date_decision,echlon,debut_ech,fin_ech,date_confirmation) "
					+ "values(?,?,?,?,?,?,?)");

			ps.setInt(1, lastId);

			if (enseignantPermanent.grade == null) {
				ps.setNull(2, Types.VARCHAR);
				ps.setNull(3, Types.DATE);
			} else {
				ps.setString(2, enseignantPermanent.grade);
				ps.setString(3, getMysqlDate(enseignantPermanent.grade_date_decision));
			}

			ps.setInt(4, enseignantPermanent.echlon);
			ps.setString(5, getMysqlDate(enseignantPermanent.debut_ech));
			ps.setString(6, getMysqlDate(enseignantPermanent.fin_ech));
			ps.setString(7, getMysqlDate(enseignantPermanent.date_confirmation));

			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void insertEnseignantPermanentPromotion(EnseignantPermanent enseignantPermanent) {
		try {
//			insertEnseignant(enseignantPermanent);

//			int lastId = getLastMatEnseignant();

			PreparedStatement ps = con.prepareStatement("insert into Enseignant_permanent "
					+ "(mat,grade,grade_date_decision,echlon,debut_ech,fin_ech,date_confirmation) "
					+ "values(?,?,?,?,?,?,?)");

			ps.setInt(1, enseignantPermanent.mat);

			if (enseignantPermanent.grade == null) {
				ps.setNull(2, Types.VARCHAR);
				ps.setNull(3, Types.DATE);
			} else {
				ps.setString(2, enseignantPermanent.grade);
				ps.setString(3, getMysqlDate(enseignantPermanent.grade_date_decision));
			}

			ps.setInt(4, enseignantPermanent.echlon);
			ps.setString(5, getMysqlDate(enseignantPermanent.debut_ech));
			ps.setString(6, getMysqlDate(enseignantPermanent.fin_ech));
			ps.setString(7, getMysqlDate(enseignantPermanent.date_confirmation));

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateEnseignantPermanent(EnseignantPermanent enseignantPermanent) {

		try {
			Enseignant enseignant = getEnseignantByMatricule(enseignantPermanent.mat);

			if (!enseignant.enseignant_type.equals(Enseignant.PERMANENT)) {
				System.out.println(enseignant.enseignant_type);
				throw new Exception("Enseignant is not permanent");
			}

			updateEnseignant(enseignantPermanent);

			PreparedStatement ps = con.prepareStatement(
					"UPDATE Enseignant_permanent SET grade=?, grade_date_decision=?, echlon=?, debut_ech=?, fin_ech=? , date_confirmation=?  WHERE mat=?;");

			if (enseignantPermanent.grade == null) {
				ps.setNull(1, Types.VARCHAR);
				ps.setNull(2, Types.DATE);
			} else {
				ps.setString(1, enseignantPermanent.grade);
				ps.setString(2, getMysqlDate(enseignantPermanent.grade_date_decision));
			}

			ps.setInt(3, enseignantPermanent.echlon);
			ps.setString(4, getMysqlDate(enseignantPermanent.debut_ech));
			ps.setString(5, getMysqlDate(enseignantPermanent.fin_ech));
			ps.setString(6, getMysqlDate(enseignantPermanent.date_confirmation));

			ps.setInt(7, enseignantPermanent.mat);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateOnlyEnseignantPermanent(EnseignantPermanent enseignantPermanent) {
		// this method does not change in Enseignant database

		try {
			Enseignant enseignant = getEnseignantByMatricule(enseignantPermanent.mat);

			if (!enseignant.enseignant_type.equals(Enseignant.PERMANENT)) {
				System.out.println(enseignant.enseignant_type);
				throw new Exception("Enseignant is not permanent");
			}

//			updateEnseignant(enseignantPermanent);

			PreparedStatement ps = con.prepareStatement(
					"UPDATE Enseignant_permanent SET grade=?, grade_date_decision=?, echlon=?, debut_ech=?, fin_ech=? , date_confirmation=?  WHERE mat=?;");

			if (enseignantPermanent.grade == null) {
				ps.setNull(1, Types.VARCHAR);
				ps.setNull(2, Types.DATE);
			} else {
				ps.setString(1, enseignantPermanent.grade);
				ps.setString(2, getMysqlDate(enseignantPermanent.grade_date_decision));
			}

			ps.setInt(3, enseignantPermanent.echlon);
			ps.setString(4, getMysqlDate(enseignantPermanent.debut_ech));
			ps.setString(5, getMysqlDate(enseignantPermanent.fin_ech));
			ps.setString(6, getMysqlDate(enseignantPermanent.date_confirmation));

			ps.setInt(7, enseignantPermanent.mat);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void deleteEnseignantPermanent(int mat) {
		String query = "DELETE FROM Enseignant_permanent WHERE mat=" + mat + ";";

		try {
			statement.executeUpdate(query);
//			deleteEnseignant(mat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------
	// Grade


	public Grade getGradeById(int id) {
		Grade grade = null;
		try {
			grade = new Grade();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Grade WHERE id=?;");
			ps.setInt(0, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				grade.id = rs.getInt("id");
				grade.intitule = rs.getString("intitule");
			}

			return grade;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Grade> getAllGrades() {
		ArrayList<Grade> gradesList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Grade;");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Grade grade = new Grade();
				grade.id = rs.getInt("id");
				grade.intitule = rs.getString("intitule");
			}
			return gradesList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertGrade(Grade grade) {
		try {
			PreparedStatement ps = con.prepareStatement("insert into Grade (intitule) values(?)");

			ps.setString(1, grade.intitule);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateGrade(Grade grade) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Grade SET intitule=?  WHERE id=?;");

			ps.setString(1, grade.intitule);
			ps.setInt(2, grade.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Echlon


	public Echlon getEchlonById(int id) {
		Echlon echlon = null;
		try {
			echlon = new Echlon();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Echlon WHERE id=?;");
			ps.setInt(0, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				echlon.id = rs.getInt("id");
				echlon.name_echlon = rs.getString("name_echlon");
			}

			return echlon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Echlon> getAllEchlons() {
		ArrayList<Echlon> echlonList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Echlon;");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Echlon echlon = new Echlon();
				echlon.id = rs.getInt("id");
				echlon.name_echlon = rs.getString("name_echlon");

				echlonList.add(echlon);
			}
			return echlonList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertEchlon(Echlon echlon) {
		try {
			PreparedStatement ps = con.prepareStatement("insert into Echlon (name_echlon) values(?)");

//			ps.setInt(1, echlon.id);
			ps.setString(1, echlon.name_echlon);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateEchlon(Echlon echlon) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Echlon SET name_echlon=?  WHERE id=?;");

			ps.setString(1, echlon.name_echlon);
			ps.setInt(2, echlon.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Echlon Date
	

	public EchlonDate getEchlonDateByMatricule(int id) {
		EchlonDate echlonDate = null;

		try {
			echlonDate = new EchlonDate();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM EchlonDate WHERE id=?;");
			ps.setInt(0, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				echlonDate.id = rs.getInt("id");
				echlonDate.debut_ech = dateToLocaDate(rs.getDate("debut_ech"));
				echlonDate.fin_ech = dateToLocaDate(rs.getDate("fin_ech"));
			}

			return echlonDate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<EchlonDate> getAllEchlonDate() {
		ArrayList<EchlonDate> echlonDateList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM EchlonDate;");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				EchlonDate echlonDate = new EchlonDate();
				echlonDate.id = rs.getInt("id");
				echlonDate.debut_ech = dateToLocaDate(rs.getDate("debut_ech"));
				echlonDate.fin_ech = dateToLocaDate(rs.getDate("fin_ech"));

				echlonDateList.add(echlonDate);
			}
			return echlonDateList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertEchlonDate(EchlonDate echlonDate) {

		try {
			PreparedStatement ps = con
					.prepareStatement("insert into EchlonDate (debut_ech,fin_ech) values(?,?)");

			ps.setString(1, getMysqlDate(echlonDate.debut_ech));
			ps.setString(2, getMysqlDate(echlonDate.fin_ech));

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getLastEchlonDate() {
		String query = "SELECT MAX( id ) AS id FROM EchlonDate;";
		try {
			int lastId = 0;
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				lastId = rs.getInt("id");
			}

			return lastId;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void updateEchlonDate(EchlonDate echlonDate) {
		try {
			PreparedStatement ps = con
					.prepareStatement("UPDATE EchlonDate SET " + "debut_ech=?, " + "fin_ech=? " + " WHERE id=?;");

			ps.setString(1, getMysqlDate(echlonDate.debut_ech));
			ps.setString(2, getMysqlDate(echlonDate.fin_ech));

			ps.setInt(3, echlonDate.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Cong
	public void createCongTable() {
	}

	public Conge getCongByMatricule(int id) {
		Conge conge = null;

		try {

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Conge WHERE id=?;");
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				conge = new Conge();
				conge.id = rs.getInt("id");
				conge.enseignant_mat = rs.getInt("enseignant_mat");
				conge.debut_cong = dateToLocaDate(rs.getDate("debut_cong"));
				conge.fin_cong = dateToLocaDate(rs.getDate("fin_cong"));
				conge.type = rs.getString("type");
			}

			return conge;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Conge getCongByEnseignantMat(int mat) {
		Conge conge = null;

		try {

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Conge enseignant_mat=?;");
			ps.setInt(1, mat);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				conge = new Conge();
				conge.id = rs.getInt("id");
				conge.enseignant_mat = rs.getInt("enseignant_mat");
				conge.debut_cong = dateToLocaDate(rs.getDate("debut_cong"));
				conge.fin_cong = dateToLocaDate(rs.getDate("fin_cong"));
				conge.type = rs.getString("type");
			}

			return conge;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Conge> getAllCongs() {
		ArrayList<Conge> congeList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Conge;");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Conge conge = new Conge();
				conge.id = rs.getInt("id");
				conge.enseignant_mat = rs.getInt("enseignant_mat");
				conge.debut_cong = dateToLocaDate(rs.getDate("debut_cong"));
				conge.fin_cong = dateToLocaDate(rs.getDate("fin_conge"));
				conge.type = rs.getString("type");

				congeList.add(conge);
			}
			return congeList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertCong(Conge conge) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into Conge " + "(enseignant_mat,debut_cong,fin_conge,type) values(?,?,?,?)");

			ps.setInt(1, conge.enseignant_mat);
			ps.setString(2, getMysqlDate(conge.debut_cong));
			ps.setString(3, getMysqlDate(conge.fin_cong));
			ps.setString(4, conge.type);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateCong(Conge conge) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE Conge SET enseignant_mat=?, debut_cong=?, fin_conge=?, type=?  WHERE id=?;");

			ps.setInt(1, conge.enseignant_mat);
			ps.setString(2, getMysqlDate(conge.debut_cong));
			ps.setString(3, getMysqlDate(conge.fin_cong));
			ps.setString(4, conge.type);

			ps.setInt(5, conge.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteConge(int id) {
		String query = "DELETE FROM Conge WHERE id=?;";

		try {
			PreparedStatement pr = con.prepareStatement(query);
			pr.setInt(1, id);

			pr.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Notification
	

	public Notification getNotificationById(int id) {
		Notification notification = null;

		try {
			notification = new Notification();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Notification WHERE id=?;");
			ps.setInt(0, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				notification.id = rs.getInt("id");
				notification.type = rs.getString("type");
				notification.nom = rs.getString("nom");
				notification.prenom = rs.getString("prenom");
				notification.body = rs.getString("body");
				notification.date_show = dateToLocaDate(rs.getDate("date_show"));
				notification.deja_vu = rs.getString("deja_vu");
			}

			return notification;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Notification> getAllNotifications() {
		ArrayList<Notification> notificationList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Notification;");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Notification notification = new Notification();

				notification.id = rs.getInt("id");
				notification.type = rs.getString("type");
				notification.nom = rs.getString("nom");
				notification.prenom = rs.getString("prenom");
				notification.body = rs.getString("body");
				notification.date_show = dateToLocaDate(rs.getDate("date_show"));
				notification.deja_vu = rs.getString("deja_vu");

				notificationList.add(notification);
			}
			return notificationList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Notification> getAllNewNotifications() {
		ArrayList<Notification> notificationList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Notification WHERE date_show <= CURDATE() and  deja_vu=?;");
			ps.setString(1, Notification.FALSE);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Notification notification = new Notification();

				notification.id = rs.getInt("id");
				notification.type = rs.getString("type");
				notification.nom = rs.getString("nom");
				notification.prenom = rs.getString("prenom");
				notification.body = rs.getString("body");
				notification.date_show = dateToLocaDate(rs.getDate("date_show"));
				notification.deja_vu = rs.getString("deja_vu");

				notificationList.add(notification);
			}
			return notificationList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Notification> getAllSeenNotifications() {
		ArrayList<Notification> notificationList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Notification WHERE deja_vu=?;");
			ps.setString(1, Notification.TRUE);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Notification notification = new Notification();

				notification.id = rs.getInt("id");
				notification.type = rs.getString("type");
				notification.nom = rs.getString("nom");
				notification.prenom = rs.getString("prenom");
				notification.body = rs.getString("body");
				notification.date_show = dateToLocaDate(rs.getDate("date_show"));
				notification.deja_vu = rs.getString("deja_vu");

				notificationList.add(notification);
			}
			return notificationList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertNotification(Notification notification) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into Notification " + "(type,nom,prenom,body,date_show,deja_vu) values(?,?,?,?,?,?)");

			ps.setString(1, notification.type);
			ps.setString(2, notification.nom);
			ps.setString(3, notification.prenom);
			ps.setString(4, notification.body);
			ps.setString(5, getMysqlDate(notification.date_show));
			ps.setString(6, notification.deja_vu);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateNotification(Notification notification) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE Notification SET type=?, nom=?, prenom=?, body=?, date_show=?, deja_vu=?  WHERE id=?;");

			ps.setString(1, notification.type);
			ps.setString(2, notification.nom);
			ps.setString(3, notification.prenom);
			ps.setString(4, notification.body);
			ps.setString(5, getMysqlDate(notification.date_show));
			ps.setString(6, notification.deja_vu);

			ps.setInt(7, notification.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteNotificaiton(int id) {
		String query = "DELETE FROM Notification WHERE id=" + id + ";";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Historique
	public void createHistoriqueTable() {
	}

	public Historiqu getHistoriqueById(int id) {
		Historiqu historiqu = null;

		try {
			historiqu = new Historiqu();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Historiqu WHERE id=?;");
			ps.setInt(0, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				historiqu.id = rs.getInt("id");
				historiqu.title = rs.getString("title");
				historiqu.body = rs.getString("body");
				historiqu.date_add = dateToLocaDate(rs.getDate("date_show"));
			}

			return historiqu;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Historiqu> getAllHistorique() {
		ArrayList<Historiqu> historiquList = new ArrayList<>();

		try {

			PreparedStatement ps = con.prepareStatement("SELECT * FROM Historiqu;");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Historiqu historiqu = new Historiqu();

				historiqu.id = rs.getInt("id");
				historiqu.body = rs.getString("body");
				historiqu.title = rs.getString("title");
				historiqu.date_add = dateToLocaDate(rs.getDate("date_add"));

				historiquList.add(historiqu);
			}

			return historiquList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertHistorique(Historiqu historiqu) {

		try {
			PreparedStatement ps = con
					.prepareStatement("insert into Historiqu " + "(title,body,date_add) values(?,?,?)");

			ps.setString(1, historiqu.title);
			ps.setString(2, historiqu.body);
			ps.setString(3, getMysqlDate(historiqu.date_add));

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateHistorique(Historiqu historiqu) {

		try {
			PreparedStatement ps = con
					.prepareStatement("UPDATE Historiqu SET title=?, body=?, date_add=? WHERE id=?;");

			ps.setString(1, historiqu.title);
			ps.setString(2, historiqu.body);
			ps.setString(3, getMysqlDate(historiqu.date_add));

			ps.setInt(4, historiqu.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// HistoriqueMandat
	public void createHistoriqueMandatTable() {
	}

	public HistoriqueMandat getHistoriqueMandatById(int id) {
		HistoriqueMandat historiqueMandat = null;

		try {
			historiqueMandat = new HistoriqueMandat();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM HistoriqueMandat WHERE id=?;");
			ps.setInt(0, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				historiqueMandat.id = rs.getInt("id");
				historiqueMandat.enseignant_mat = rs.getInt("enseignant_mat");
				historiqueMandat.mandat_id = rs.getInt("mandat_id");
				historiqueMandat.debut_mandat = dateToLocaDate(rs.getDate("debut_mandat"));
				historiqueMandat.fin_mandat = dateToLocaDate(rs.getDate("fin_mandat"));
			}

			return historiqueMandat;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<HistoriqueMandat> getAllHistoriqueMandat() {
		ArrayList<HistoriqueMandat> historiqueMandatList = new ArrayList<>();

		try {

			PreparedStatement ps = con.prepareStatement("SELECT * FROM HistoriqueMandat;");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				HistoriqueMandat historiqueMandat = new HistoriqueMandat();

				historiqueMandat.id = rs.getInt("id");
				historiqueMandat.enseignant_mat = rs.getInt("enseignant_mat");
				historiqueMandat.mandat_id = rs.getInt("mandat_id");
				historiqueMandat.debut_mandat = dateToLocaDate(rs.getDate("debut_mandat"));
				historiqueMandat.fin_mandat = dateToLocaDate(rs.getDate("fin_mandat"));

				historiqueMandatList.add(historiqueMandat);
			}

			return historiqueMandatList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<HistoriqueMandat> getAllHistoriqueMandatByEnseignant(int enseignant_mat) {
		ArrayList<HistoriqueMandat> historiqueMandatList = new ArrayList<>();

		try {

			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM HistoriqueMandat  WHERE enseignant_mat=?;");
			ps.setInt(1, enseignant_mat);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				HistoriqueMandat historiqueMandat = new HistoriqueMandat();

				historiqueMandat.id = rs.getInt("id");
				historiqueMandat.enseignant_mat = rs.getInt("enseignant_mat");
				historiqueMandat.mandat_id = rs.getInt("mandat_id");
				historiqueMandat.debut_mandat = dateToLocaDate(rs.getDate("debut_mandat"));
				historiqueMandat.fin_mandat = dateToLocaDate(rs.getDate("fin_mandat"));

				historiqueMandatList.add(historiqueMandat);
			}

			return historiqueMandatList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertHistoriqueMandat(HistoriqueMandat historiqueMandat) {

		try {
			PreparedStatement ps = con.prepareStatement("insert into HistoriqueMandat "
					+ "(enseignant_mat,mandat_id,debut_mandat,fin_mandat) values(?,?,?,?)");

			ps.setInt(1, historiqueMandat.enseignant_mat);
			ps.setInt(2, historiqueMandat.mandat_id);
			ps.setString(3, getMysqlDate(historiqueMandat.debut_mandat));
			ps.setString(4, getMysqlDate(historiqueMandat.fin_mandat));

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateHistoriqueMandat(HistoriqueMandat historiqueMandat) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE HistoriqueMandat SET enseignant_mat=?, mandat_id=?, debut_mandat=?, fin_mandat=?  WHERE id=?;");

			ps.setInt(1, historiqueMandat.enseignant_mat);
			ps.setInt(2, historiqueMandat.mandat_id);
			ps.setString(3, getMysqlDate(historiqueMandat.debut_mandat));
			ps.setString(4, getMysqlDate(historiqueMandat.fin_mandat));

			ps.setInt(5, historiqueMandat.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Helper functions
	public String getMysqlDate(LocalDate localDate) {
       if(localDate==null) return "no date"; 
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String mysqlDateString = formatter.format(date);

		return mysqlDateString;
	}

	public LocalDate dateToLocaDate(Date date) {
		if (date == null)
			return null;
		LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

		return localDate;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public EchlonNotification getEchlonNotificationById(int id) {
		EchlonNotification enotification = null;

		try {
			enotification = new EchlonNotification();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM EchlonNotification WHERE id=?;");
			ps.setInt(0, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				enotification.id = rs.getInt("id");
				enotification.nom = rs.getString("nom");
				enotification.prenom = rs.getString("prenom");
				enotification.date_debut=dateToLocaDate(rs.getDate("date_debut"));
				enotification.date_show = dateToLocaDate(rs.getDate("date_show"));
				enotification.deja_vu = rs.getString("deja_vu");
			}

			return enotification;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<EchlonNotification> getAllEchlonNotification() {
		ArrayList<EchlonNotification> echlonList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM EchlonNotification;");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				EchlonNotification enotification = new EchlonNotification();

				enotification.id = rs.getInt("id");
				enotification.nom = rs.getString("nom");
				enotification.prenom = rs.getString("prenom");
				enotification.date_debut = dateToLocaDate(rs.getDate("date_debut"));
				enotification.date_show = dateToLocaDate(rs.getDate("date_show"));
				enotification.deja_vu = rs.getString("deja_vu");

				echlonList.add(enotification);
			}
			return echlonList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<EchlonNotification> getAllNewEchlonNotification() {
		ArrayList<EchlonNotification> echlonList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM EchlonNotification WHERE date_show <= CURDATE() and  deja_vu=?;");
			ps.setString(1, EchlonNotification.FALSE);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				EchlonNotification notification = new EchlonNotification();

				notification.id = rs.getInt("id");
				notification.nom = rs.getString("nom");
				notification.prenom = rs.getString("prenom");
				notification.date_debut = dateToLocaDate(rs.getDate("date_debut"));
				notification.date_show = dateToLocaDate(rs.getDate("date_show"));
				notification.deja_vu = rs.getString("deja_vu");

				echlonList.add(notification);
			}
			return echlonList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<EchlonNotification> getAllSeenEchlon() {
		ArrayList<EchlonNotification> echlonList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM EchlonNotification WHERE deja_vu=?;");
			ps.setString(1, Notification.TRUE);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				EchlonNotification notification = new EchlonNotification();

				notification.id = rs.getInt("id");
				notification.nom = rs.getString("nom");
				notification.prenom = rs.getString("prenom");
				notification.date_debut = dateToLocaDate(rs.getDate("date_debut"));
				notification.date_show = dateToLocaDate(rs.getDate("date_show"));
				notification.deja_vu = rs.getString("deja_vu");

				echlonList.add(notification);
			}
			return echlonList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertEchlonNotification(EchlonNotification notification) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into EchlonNotification " + "(nom,prenom,date_debut,date_show,deja_vu) values(?,?,?,?,?)");

		
			ps.setString(1, notification.nom);
			ps.setString(2, notification.prenom);
			ps.setString(3, getMysqlDate(notification.date_debut));
			ps.setString(4, getMysqlDate(notification.date_show));
			ps.setString(5, notification.deja_vu);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateEchlonNotification(EchlonNotification notification) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE EchlonNotification SET nom=?, prenom=?, date_debut=?, date_show=?, deja_vu=?  WHERE id=?;");

			
			ps.setString(1, notification.nom);
			ps.setString(2, notification.prenom);
			ps.setString(3, getMysqlDate(notification.date_debut));
			ps.setString(4, getMysqlDate(notification.date_show));
			ps.setString(5, notification.deja_vu);

			ps.setInt(6, notification.id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteEchlonNotification(int id) {
		String query = "DELETE FROM EchlonNotification WHERE id=" + id + ";";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Database creator 
	
	public void createTables() {
		try {
			checkForeigenKey(1);
			createDepartementTable();
			createMandatTable();
			createEnseignantTable();
			createEnseignantStagererTable();
			createEchlonDateTable();
			createEchlonTable();
			createGradeTable();
			createEnseignantPermanentTable();
			createDiplomeTable();
			createCongeTable();
			createNotificationTable();
			createEchlonNotificationTable();
			createHistoriquTable();
			createHistoriqueMandat();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dropAlltables() {
		String query = "DROP TABLE IF EXISTS Departement;" + "DROP TABLE IF EXISTS Mandat;"
				+ "DROP TABLE IF EXISTS Enseignant;" + "DROP TABLE IF EXISTS Enseignant_stagerer;"
				+ "DROP TABLE IF EXISTS Enseignant_permanent;" + "DROP TABLE IF EXISTS EchlonDate;"
				+ "DROP TABLE IF EXISTS Echlon;" + "DROP TABLE IF EXISTS Grade;" + "DROP TABLE IF EXISTS Diplome;"
				+ "DROP TABLE IF EXISTS Conge;" + "DROP TABLE IF EXISTS Notification;"
				+ "DROP TABLE IF EXISTS Historiqu; "+"DROP TABLE IF EXISTS EchlonNotification;" ;
		try {
			checkForeigenKey(0);
			statement.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void checkForeigenKey(int check) {
		try {
			PreparedStatement ps = con.prepareStatement("SET FOREIGN_KEY_CHECKS = ?;");
			ps.setInt(1, check);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void createDepartementTable() {
		String query = "create table IF NOT EXISTS Departement(\n" + "	mat INT NOT NULL AUTO_INCREMENT,\n"
				+ "	intitule VARCHAR(100) NOT NULL,\n" + "    PRIMARY KEY ( mat )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createMandatTable() {
		String query = "create table IF NOT EXISTS Mandat(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "    intitule VARCHAR(100) NOT NULL,\n" + "	date_mandat DATE NOT NULL,\n"
				+ "	fin_mandat DATE NOT NULL,\n" + "    PRIMARY KEY ( id )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createEnseignantTable() {
		String query = "create table IF NOT EXISTS Enseignant(\n" + "	mat INT NOT NULL AUTO_INCREMENT,\n"
				+ "    departement_mat INT NOT NULL,\n" + "    mandat_id INT NULL,\n"
				+ "	nom VARCHAR(100) NOT NULL,\n" + "	prenom VARCHAR(100) NOT NULL,\n"
				+ "	sexe VARCHAR(1) NOT NULL,\n" + "	date_naissance DATE NOT NULL,\n"
				+ "	adress VARCHAR(100) NOT NULL,\n" + "	email VARCHAR(100) NOT NULL,\n"
				+ "	telephone VARCHAR(100) NOT NULL,\n" + "	data_recurtement DATE NOT NULL,\n"
				+ "	enseignant_type VARCHAR(4) NOT NULL,\n"
				+ "	FOREIGN KEY (departement_mat) REFERENCES Departement(mat),\n"
				+ "	FOREIGN KEY (mandat_id) REFERENCES Mandat(id),\n" + "    PRIMARY KEY ( mat ),\n"
				+ "    CHECK  (enseignant_type='perm' OR enseignant_type='stag'),\n"
				+ "    CHECK  (sexe='f' OR sexe='h')\n" + ", UNIQUE(nom,prenom));";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createEnseignantStagererTable() {
		String query = "create table IF NOT EXISTS Enseignant_stagerer(\n" + "	mat INT NOT NULL AUTO_INCREMENT,\n"
				+ "	debut_stage DATE NOT NULL,\n" + "	fin_stage DATE NOT NULL, \n"
				+ "	FOREIGN KEY (mat) REFERENCES Enseignant(mat),\n" + "    PRIMARY KEY ( mat )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createEnseignantPermanentTable() {
		String query = "create table IF NOT EXISTS  Enseignant_permanent(\n"
				+ "    mat INT NOT NULL ,\n"
				+ " 	grade VARCHAR(255) NULL ,\n"
				+ "    date_confirmation DATE NOT NULL,\n"
				+ "	grade_date_decision DATE  NULL,\n"
				+ "    echlon INT NOT NULL ,\n"
				+ "	debut_ech DATE NOT NULL,\n"
				+ "	fin_ech DATE NOT NULL,\n"
				+ "	FOREIGN KEY (mat) REFERENCES Enseignant(mat)\n"
				+ ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createEchlonDateTable() {
		String query = "create table IF NOT EXISTS EchlonDate(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "	debut_ech DATE NOT NULL,\n" + "	fin_ech DATE NOT NULL,\n" + "    PRIMARY KEY ( id )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createEchlonTable() {
		String query = "create table IF NOT EXISTS Echlon(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "	name_echlon VARCHAR(100) NOT NULL,\n" + "    PRIMARY KEY ( id )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createGradeTable() {
		String query = "create table IF NOT EXISTS Grade(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "	intitule VARCHAR(100) NOT NULL,\n" + "    PRIMARY KEY ( id ),\n"
				+ " CHECK  (intitule='mca' OR intitule='mcb' OR intitule='maa' OR intitule='mab' OR intitule='professor'));";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createDiplomeTable() {
		String query = "create table IF NOT EXISTS Diplome(\n" + "	mat INT NOT NULL AUTO_INCREMENT,\n"
				+ "    enseignant_mat INT NOT NULL,\n" + "	intitule VARCHAR(100) NOT NULL,\n"
				+ "	date_obtention  DATE NOT NULL,\n" + "	specialite VARCHAR(100) NOT NULL,\n"
				+ "	FOREIGN KEY (enseignant_mat) REFERENCES Enseignant(mat),\n" + "    PRIMARY KEY ( mat )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createCongeTable() {
		String query = "create table IF NOT EXISTS Conge(\n" + "	id  INT NOT NULL AUTO_INCREMENT,\n"
				+ "    enseignant_mat INT NOT NULL,\n" + "	debut_cong DATE NOT NULL,\n"
				+ "	fin_conge DATE NOT NULL,\n" + "    type VARCHAR(100) NOT NULL,\n"
				+ "	FOREIGN KEY (enseignant_mat) REFERENCES Enseignant(mat),\n" + "    PRIMARY KEY ( id ),\n"
				+ "    CHECK  (type='maladie' OR type='miseDisponibilite' OR type='maternite')\n" + ");\n" + "";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createNotificationTable() {
		String query = "create table IF NOT EXISTS Notification(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "    nom VARCHAR(255) NOT NULL,\n" + "    prenom VARCHAR(255) NOT NULL, \n"
				+ "	type VARCHAR(255) NOT NULL,\n" + "	body VARCHAR(255) NOT NULL,\n" + "	date_show DATE NOT NULL,\n"
				+ "	deja_vu VARCHAR(5) NOT NULL,\n" + "    PRIMARY KEY ( id ),\n"
				+ "    CHECK  (deja_vu='true' OR deja_vu='false')\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void createEchlonNotificationTable() {
	
		String query = "create table IF NOT EXISTS EchlonNotification(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "    nom VARCHAR(255) NOT NULL,\n" + "    prenom VARCHAR(255) NOT NULL, \n"
				+ "	date_debut DATE NOT NULL,\n" + "	date_show DATE NOT NULL,\n"
				+ "	deja_vu VARCHAR(5) NOT NULL,\n" + "    PRIMARY KEY ( id ),\n"
				+ "    CHECK  (deja_vu='true' OR deja_vu='false')\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void createHistoriquTable() {
		String query = "create table IF NOT EXISTS Historiqu(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "	title VARCHAR(255) NOT NULL,\n" + "	body VARCHAR(255) NOT NULL,\n" + "	date_add DATE NOT NULL,\n"
				+ "    PRIMARY KEY ( id )\n" + ");";

		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createHistoriqueMandat() {
		String query = "create table IF NOT EXISTS HistoriqueMandat(\n" + "	id INT NOT NULL AUTO_INCREMENT,\n"
				+ "    enseignant_mat INT NOT NULL,\n" + "    mandat_id INT NULL,\n"
				+ "	debut_mandat DATE NOT NULL,\n" + "    fin_mandat DATE NOT NULL,\n"
				+ "    FOREIGN KEY (enseignant_mat) REFERENCES Enseignant(mat),\n"
				+ "    FOREIGN KEY (mandat_id) REFERENCES Mandat(id),\n" + "    PRIMARY KEY ( id )\n" + ");";
		try {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// Admin 
	
public Boolean userLogin(String user_name,String user_pass) {
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account  where user_name=? and user_pass=md5(?);");
			ps.setString(1, user_name);
			ps.setString(2, user_pass);
			
			ResultSet result = ps.executeQuery();
			
			String userName = null;
			
			while (result.next()) {
				userName = result.getString("user_name");
			}
			
			if(userName==null)return false;
			
			return true;	
			
			
//		System.out.println();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void create_new_user(String user_name,String user_pass) {
		String insert_query = "INSERT INTO user_acount (user_name, user_pass) values (\"";
		
		insert_query += user_name +"\",\"";
		insert_query += user_pass + "\");";	
		
		try {
		statement.executeUpdate(insert_query);
//		System.out.println(a);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
