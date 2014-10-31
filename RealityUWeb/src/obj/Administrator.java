package obj;

/********************************************************************
 *	RealityUWeb: Adminstrator.java
 *  3/11/2014
 *  @author Cookie Monsters
 *  
 *  EDITED BY:					DATE:				DESCRIPTION:
 *  James Hammond, SSR			10/31/2014			Added database methods that call AdministratorsDAO methods. Added property 'master'
 ********************************************************************/
/**
 * The Class Administrator.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.AdministratorsDAO;

public class Administrator {
	// ========================== Properties ===========================
	/*********************************
	 * Declarations
	 ********************************/
	private int id;
	private String username;
	private String password;
	private String fname;
	private String lname;
	private int master;

	// ========================== Constructors ========================
	/**
	 * 
	 */
	public Administrator() {
		this.id = 0;
		this.username = "";
		this.password = "";
		this.fname = "";
		this.lname = "";
		this.master = 0;
	}

	/**
	 * Administrator(int,String,String,String,String,int)
	 * @param id
	 * @param username
	 * @param password
	 * @param fname
	 * @param lname
	 */
	public Administrator(int id, String username, String password,
			String fname, String lname, int master) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.fname = fname;
		this.lname = lname;
		this.master = master;
	}
	
	/**
	 * Administrator(String,String,String,String,int)
	 * @param username
	 * @param password
	 * @param fname
	 * @param lname
	 * @param master
	 * The id is automatically incremented in the database,
	 * this constructor ignores the id property because of this.
	 */
	public Administrator(String username, String password,
			String fname, String lname, int master) {
		this.username = username;
		this.password = password;
		this.fname = fname;
		this.lname = lname;
		this.master = master;
	}

	// ========================== Behaviors ==========================
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname
	 *            the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * @return the lname
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * @param lname
	 *            the lname to set
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	/**
	 * @return master 
	 */
	public int getMaster(){
		return this.master;
	}
	
	/**
	 * @param master
	 * 			the master to set
	 */
	public void setMaster(int master){
		this.master = master;
	}

	// ========================== validateLogin() Method ==================
	public boolean validateLogin(String user, String pw) {
		boolean found = false;
		if (user.equals(getUsername()) && pw.equals(getPassword())) {
			found = true;
		}
		return found;
	} // end validateLogin

	// ======================== DISPLAY METHOD ====================
	public void display() {
		System.out.println("AdminID\t\t= " + getId());
		System.out.println("UserName\t= " + getUsername());
		System.out.println("Password\t= " + getPassword());
		System.out.println("First Name\t= " + getFname());
		System.out.println("Last Name\t= " + getLname());
		System.out.println("Master\t= " + getMaster());
	} // end display()
	
	// =================== DATA ACCESS METHODS ==================== //
	/**
	 * select(int)
	 * @param id
	 * 			The id of the desired administrator
	 * @return Administrator object
	 */
	public void select(int id){
		AdministratorsDAO adminDAO = new AdministratorsDAO();
		Administrator admin = new Administrator();
		admin = adminDAO.find(id);
		
		setId(admin.getId());
		setUsername(admin.getUsername());
		setPassword(admin.getPassword());
		setFname(admin.getFname());
		setLname(admin.getLname());
		setMaster(admin.getMaster());
	}// end select(int)
	
	/**
	 * select(String)
	 * @param userName
	 * 			The username of the desired Administrator
	 * @return Administrator object
	 */
	public void select(String userName){
		AdministratorsDAO adminDAO = new AdministratorsDAO();
		Administrator admin = new Administrator();
		admin = adminDAO.select(userName);
		
		setId(admin.getId());
		setUsername(admin.getUsername());
		setPassword(admin.getPassword());
		setFname(admin.getFname());
		setLname(admin.getLname());
		setMaster(admin.getMaster());
	}// end select(String)
	
	/**
	 * update()
	 * 		Updates the the values of the current Administrator
	 * 	object into the database using the AdministratorsDAO()
	 * 	update(Administrator) method.
	 */
	public void update(){
		AdministratorsDAO adminDAO = new AdministratorsDAO();
		adminDAO.update(this);
	}// end update
	
	/**
	 * insert()
	 * 		Adds the current Adminstrator object into the 
	 * 	database using the AdministratorsDAO()
	 * 	insert(Administrator) method.
	 */
	public void insert(){
		AdministratorsDAO adminDAO = new AdministratorsDAO();
		adminDAO.insert(this);
	}// end insert
	
	/**
	 * delete()
	 * 		Deletes the current Administrator object from the
	 * 	database. Also sets current object's values to 0 or null.
	 */
	public void delete(){
		AdministratorsDAO adminDAO = new AdministratorsDAO();
		adminDAO.delete(this);
		// reset values to clear current Administrator object.
		this.setId(0);
		this.setUsername(null);
		this.setPassword(null);
		this.setFname(null);
		this.setLname(null);
		this.setMaster(0);
		
	}// end delete()
	
	

	// ======================== MAIN METHOD ====================
	public static void main(String[] args) {
	
//	// ============ START insert() TEST =============== //
//		Administrator admin = new Administrator("jham","password","James", "Hammond", 0);
//		admin.insert();
//	// ============ END insert() TEST ============== //
		
//	// ======== START select(int) TEST =========== //	
//		Administrator admin = new Administrator();
//		admin.select(2);
//		admin.display();
//	// ======== END select(int) TEST =========== //
		
//	// =========== START select(String) TEST =========== //
//		Administrator admin = new Administrator();
//		admin.select("jham");
//		admin.display();
//	// ============ END select(String TEST ============ //
	
//	// ============= START update() TEST =============== //
//		Administrator admin = new Administrator();
//		admin.select("jham");
//		admin.setFname("Johnny");
//		admin.update();
//	// ============== END update() TEST ================ //
		
//	// ============== START delete() TEST ============== //
//		Administrator admin = new Administrator();
//		admin.select("jham");
//		admin.delete();
//	// ============== END delete() TEST ================ //
	
		
		
/*
 * THE FOLLOWING IS LEGACY TEST CODE
 * AND IS NOT GARUNTEED TO WORK
 */
//		String fieldName = "username";
//		String fieldUserValue = "test1";
//		String pwdValue = "pwd1";
//		List<Administrator> lstAdmin = new ArrayList<Administrator>();
//		Administrator adm = new Administrator();
//		// Create AdministratorDAO & Administrator Objs and Validate Login
//		AdministratorsDAO adao1 = new AdministratorsDAO();
//		// Returns List of Administrators matching search criteria (even if only
//		// 1)
//		lstAdmin = adao1.search(fieldName, fieldUserValue); // Lookup by
//															// username
//		// Extract single Administrator obj from List
//		// Loop thru Administrator List (should only be 1 obj in list)
//		for (int i = 0; i < lstAdmin.size(); i++) {
//			if (i == 0) {
//				adm = lstAdmin.get(i);
//				adm.display();
//			} else { // more than one obj in list
//				System.out.println("Error - Duplicate Username.");
//			} // end if
//		} // end for
//
//		if (adm.validateLogin(fieldUserValue, pwdValue)) {
//			System.out.println("Yay! Login Valid.");
//		} else {
//			System.out.println("ERROR! Login Invalid.");
//		} // end if

	} // end main()
}
