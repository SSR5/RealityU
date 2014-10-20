package obj;
import dao.TeachersDAO;

/**
 * Teacher
 * This class allows for easy modification of teacher objects.
 * Previously both the business objects and data access objects
 * needed to be called in order to edit information in the 
 * database. Now only this class needs to be called in order
 * to modify a teacher's data.
 * 
 * It was done this way to maintain consistency with 
 * legacy code while also allowing for ease of use.
 * 
 * @author Jelani, SSR5			DATE: 10/20/2014
 * 
 * EDITED BY:					DATE:				DESCRIPTION:
 * James Hammond, SSR5			10/20/2014			Added a constructor and several methods that work with TeachersDAO object.
 *
 */

public class Teacher {
	
// ============= PROPERTIES ============================ //
	
	private int id;
	private String firstName;
	private String lastName;
	private String password;
	private String school;

// ================ CONSTRUCTORS ===================== //
	/**
	 * Teacher default constructor
	 * Sets properties to default values
	 */
	public Teacher(){
		setId(0);
		setFname("First Name");
		setLname("Last Name");
		setPassword("Password");
		setSchool("School Name");
	}// end no argument constructor
	
	/**
	 * Teacher 5 arg constructor
	 * Sets all properties to specified values
	 * @param id 
	 * 			int
	 * @param fn
	 * 			String
	 * @param ln
	 * 			String
	 * @param pw
	 * 			String
	 * @param school
	 * 			String
	 */
	public Teacher(int id, String fn, String ln, String pw,
			String school){
		setId(id);
		setFname(fn);
		setLname(ln);
		setPassword(pw);
		setSchool(school);
	}// end 5 argument constructor
	
	/**
	 * Teacher 1 arg constructor
	 * This constructor uses the id parameter to find the 
	 * desired teacher from the database using the TeachersDAO 
	 * object. It then assigns all properties to the values 
	 * retrieved from the database.
	 * @param id
	 * 			int
	 */
	public Teacher(int id){
		select(id);	
	}// end 1 argument constructor
	
	
// ============= START GETTERS/SETTERS ====================== //
	/**
	 * @param id
	 * 			the id to set
	 */
	public void setId(int id){this.id = id;}
	/**
	 * @return the id
	 */
	public int getId(){return this.id;}
	/**
	 * @param fname
	 * 			the first name to set
	 */
	public void setFname(String fname){this.firstName = fname;}
	/**
	 * @return the first name
	 */
	public String getFname(){return this.firstName;}
	/**
	 * @param lname
	 * 			the last name to set
	 */
	public void setLname(String lname){this.lastName = lname;}
	/**
	 * @return the last name
	 */
	public String getLname(){return this.lastName;}
	/**
	 * @param pw
	 * 			the password to set
	 */
	public void setPassword(String pw){this.password = pw;}
	/**
	 * @return the password
	 */
	public String getPassword(){return this.password;}
	/**
	 * @param schoolName
	 * 				the school to set
	 */
	public void setSchool(String schoolName){this.school = schoolName;}
	/**
	 * @return the school
	 */
	public String getSchool(){return this.school;}
// ============= END GETTERS/SETTERS ======================== //

// ================ DISPLAY METHOD ======================= //
	/**
	 * display() method
	 * This method merely displays the current object's
	 * property values to the console.
	 */
	public void display(){
		System.out.println("ID = "+getId());
		System.out.println("First Name = "+getFname());
		System.out.println("Last Name = "+getLname());
		System.out.println("Password = "+getPassword());
		System.out.println("School = "+getSchool());
	}// end display()
	
// ================= DATA EDITING METHODS ====================== //
	/*
	 * The following methods were created to allow coders to
	 * to edit the database without having to call the data
	 * access object.
	 */
	
	/**
	 * select(int) method
	 * This method utilizes the TeachersDAO select(int) method. 
	 * @param id
	 */
	public void select(int id){
		
		// Instantiate a TeachersDAO object
		TeachersDAO dao = new TeachersDAO();
		
		// Instantiate a teacher object using the 
		// TeachersDAO.select(int) method.
		Teacher teacher = dao.select(id);
		
		// Assign all values to this Teacher object
		setId(teacher.getId());
		setFname(teacher.getFname());
		setLname(teacher.getLname());
		setPassword(teacher.getPassword());
		setSchool(teacher.getSchool());
	}
	
	public void update(){		
		TeachersDAO dao = new TeachersDAO();
		dao.update(this);		
	}// end update method
	
	public void insert(){		
		TeachersDAO dao = new TeachersDAO();
		dao.insert(this);		
	}// end insert method
	
	public void delete(){		
		TeachersDAO dao = new TeachersDAO();
		dao.delete(this);
		
		setId(0);
		setFname("First Name");
		setLname("Last Name");
		setPassword("Password");
		setSchool("School");		
	}// end delete method
	
// ================== MAIN METHOD =================== //
	
	public static void main(String[] args){
		
		/*
		 * This main method is for testing purposes.
		 * Many of these tests are reliant on the id number,
		 * which auto increments in the database. Therefore
		 * you will have to check and update the id numbers
		 * entered for tests to match the desired id numbers
		 * inside the database.
		 */
		
//	// --------- START insert() TEST ------------ //
//		Teacher teacher = new Teacher(0,"James","Hammond","123abc","chattCollege");
//		teacher.insert();
//	// ----------- END insert() TEST ------------- //
		
//	// ----------- START select(int) TEST ------------- //
//		Teacher teacher = new Teacher();
//		teacher.select(2);
//		teacher.display();
//	// ----------- END select(int) TEST -------------- //
		
//	// ------------ START update() TEST ------------ // 
//		Teacher teacher = new Teacher();
//		teacher.select(2);
//		teacher.setFname("Johny");
//		teacher.update();
//	// ------------- END update() TEST ------------- //
		
//	// ------------- START delete() TEST ------------ //
//		Teacher teacher = new Teacher();
//		teacher.select(2);
//		teacher.delete();
//	// ------------- END delete() TEST ----------- //
		
	}// end main
			
}// end class
