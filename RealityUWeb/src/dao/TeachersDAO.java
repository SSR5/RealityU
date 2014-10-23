package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import obj.Teacher;
import dao.DbUtil;

/**
 * TeachersDAO
 * This class handles all database interaction required
 * for adding or changing data about Teachers participating
 * in the RealityU events.
 * @author James Hammond, SSR5		DATE: 10/15/2014
 * 
 * EDITED BY:						DATE:			DESCRIPTION:
 * James Hammond, SSR5				10/20/2014		Added selectAll() method. Added main() method for testing purposes. Testing and debugging completed.
 *
 */
public class TeachersDAO implements DAO {

	
	/**
	 * This method makes sure table exists<br>
	 * 
	 * @return Returns True/False
	 */
	@Override
	public boolean checkTable() {
		String tableName = "Teacher";
		boolean found = false;
		found = DbUtil.checkTable(tableName);
		return found;
	}// end checkTable()

	/**
	 * Creates a table if it doesn't exist
	 * 
	 * @return Returns True/False
	 */
	@Override
	public boolean createTable() {
		boolean success = true;
		String tableName = "Teacher";

		// Create SQL Statement
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName
				+ " ('userName' VARCHAR PRIMARY KEY NOT NULL,"
				+ "'firstName' VARCHAR,"
				+ "'lastName' VARCHAR NOT NULL,"
				+ "'password' VARCHAR NOT NULL,"
				+ "'school' VARCHAR NOT NULL)";
				

		success = DbUtil.createTable(tableName, sql);

		return success;
	}// end createTable()
	
	public List<Teacher>selectAll(){
		
		
		//Check Table & Create Table if it doesn't already exist
		boolean success = createTable();
		System.out.println("Check if table exists (create if doesn't exist). Table exists: " + success);
				
		// Variable Declarations
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Teacher>teacherList = new ArrayList<>();


		String sql = "";

		try {
			// Load Driver & Connect to Dbase
			conn = DbUtil.createConnection();

			// Create SQL Statement
			sql = "SELECT * FROM Teacher ";
			stmt = conn.prepareStatement(sql);
			System.out.println("SQL: " + sql);
			// Execute Statement - Get ResultSet by Column Name
			rs = stmt.executeQuery();
			//Process the ResultSet
			while (rs.next()) {
				Teacher teacher = new Teacher();

				teacher.setUserName(rs.getString("userName"));
				teacher.setFname(rs.getString("firstName"));
				teacher.setLname(rs.getString("lastName"));
				teacher.setPassword(rs.getString("password"));
				teacher.setSchool(rs.getString("school"));

				teacherList.add(teacher);
			}// end while
			
			} catch (Exception e) {
					// Handle Errors for Class
					System.out.println("Class Error. Current DB: " + DB + e);
				} finally {
					// Close ResultSet, Query, and Database Connection
					DbUtil.close(rs);
					DbUtil.close(stmt);
					DbUtil.close(conn);
					System.out.println("Closed Resources");
				} // End Try/Catch

		return teacherList;
	}
	
	
	/**
	 * List<Teacher> search(String,String) method
	 * This method returns a list of teacher objects whose 
	 * data corresponds to the String parameters.
	 * This method is meant to make it easier to search for 
	 * teachers matching a specific criteria.
	 * 
	 * Example: List<Teacher> = search("FirstName", "Linda");
	 * The example code above would return a list of all 
	 * teachers with the first name Linda in the database.
	 * 
	 * @param String column
	 * @param String search
	 * @return List<Teacher>
	 */
	public List<Teacher> search(String column, String search) {
		//Check Table & Create Table if it doesn't already exist
		boolean success = createTable();
		System.out.println("Check if table exists (create if doesn't exist). Table exists: " + success);

		// Variable Declarations
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		List<Teacher> teacherList = new ArrayList<Teacher>();

		String sql = "";
		String strWhere = "";

		try {
			// Load Driver & Connect to Dbase
			conn = DbUtil.createConnection();

			//Delete single quotes in SQL string for 'search' parameter for dbase fields that are non-string fields
			if (column != null) {
				//Change for Integer & Double dbase fields
				if (column.equals("userName")) {
					//No single quotes on 'search'
					strWhere = "WHERE " + column + " = " + search;
				} else { //for all String dbase fields
					//Single quotes on 'search'
					strWhere = "WHERE " + column + " = '" + search + "'";
				} //end if
			} //end if

			// Create SQL Statement
			sql = "SELECT * FROM Teacher " + strWhere;
			stmt = conn.prepareStatement(sql);
			System.out.println("SQL: " + sql);

			// Execute Statement - Get ResultSet by Column Name
			rs = stmt.executeQuery();
			
			//Process the ResultSet
			while (rs.next()) {
				Teacher teacher = new Teacher();

				teacher.setUserName(rs.getString("userName"));
				teacher.setSchool(rs.getString("school"));
				teacher.setFname(rs.getString("firstName"));
				teacher.setLname(rs.getString("lastName"));
				teacher.setPassword(rs.getString("password"));

				
				teacherList.add(teacher);
			}
			
		} catch (Exception e) {
			// Handle Errors for Class
			System.out.println("Class Error. Current DB: " + DB + e);
		} finally {
			// Close ResultSet, Query, and Database Connection
			DbUtil.close(rs);
			DbUtil.close(stmt);
			DbUtil.close(conn);
			System.out.println("Closed Resources");
		} // End Try/Catch
		
		return teacherList;
	}// end List<Teacher> search() method
	
	/**
	 * Finds a Teacher by userName.
	 * 
	 * @param userName
	 *            : The Teacher userName to search for.
	 * @return Returns a Teacher object with that userName.
	 */
	public Teacher select(String useName) {
		// Check Table & Create Table if it doesn't already exist
		boolean success = createTable();
		System.out
				.println("Check if table exists (create if doesn't exist). Table exists: "
						+ success);

		// Variable Declarations
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Teacher teacher = new Teacher();

		String sql = "";
		String strWhere = "";

		try {
			// Load Driver & Connect to Dbase
			conn = DbUtil.createConnection();

			// Create SQL Statement
			strWhere = "WHERE userName = '" + useName +"'";

			sql = "SELECT * FROM Teacher " + strWhere;
			stmt = conn.prepareStatement(sql);
			System.out.println("SQL: " + sql);

			// Execute Statement - Get ResultSet by Column Name
			rs = stmt.executeQuery();

			// Process the ResultSet
			while (rs.next()) {
				teacher.setUserName(rs.getString("userName"));
				teacher.setSchool(rs.getString("school"));
				teacher.setPassword(rs.getString("password"));
				teacher.setFname(rs.getString("firstName"));
				teacher.setLname(rs.getString("lastName"));
			}
		} catch (Exception e) {
			// Handle Errors for Class
			System.out.println("Class Error. Current DB: " + DB + e);
		} finally {
			// Close ResultSet, Query, and Database Connection
			DbUtil.close(rs);
			DbUtil.close(stmt);
			DbUtil.close(conn);
			System.out.println("Closed Resources");
		} // End Try/Catch

		return teacher;
	} // end Teacher select(String) method
	
	
	/**
	 * Update.
	 * 
	 * @param teacher
	 *            : the Teacher being updated
	 *            
	 */
	public void update(Teacher teacher) {
		// Check Table & Create Table if it doesn't already exist
		boolean success = createTable();
		System.out
				.println("Check if table exists (create if doesn't exist). Table exists: "
						+ success);

		// Variable Declarations
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			// Load Driver & Connect to Dbase
			conn = DbUtil.createConnection();

			// Create SQL Statement
			String sql = "UPDATE Teacher SET school=?, password=?, firstName=?, lastName=? "
					+ "WHERE userName=?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, teacher.getSchool());
			stmt.setString(2, teacher.getPassword());
			stmt.setString(3, teacher.getFname());
			stmt.setString(4, teacher.getLname());
			stmt.setString(5, teacher.getUserName());
			System.out.println("SQL: " + sql);

			// Execute Statement
			int n = stmt.executeUpdate();
		} catch (Exception e) {
			// Handle Errors for Class
			System.out.println("Class Error. Current DB: " + DB + e);
		} finally {
			// Close Query, and Database Connection
			DbUtil.close(stmt);
			DbUtil.close(conn);
			System.out.println("Closed Resources");
		} // End Try/Catch

	}// end update(Teacher)
	
	/**
	 * Insert a new Teacher.
	 * 
	 * @param username
	 *            : The Teacher username
	 * @param password
	 *            : The Teacher password
	 * @param fname
	 *            : The Teacher fname
	 * @param lname
	 *            : The Teacher lname
	 *            
	 */
	public void insert(Teacher teacher) {
		// Check Table & Create Table if it doesn't already exist
		boolean success = createTable();
		System.out
				.println("Check if table exists (create if doesn't exist). Table exists: "
						+ success);

		// Variable Declarations
		Connection conn = null;
		PreparedStatement stmt = null;


		try {

			// Load Driver & Connect to Dbase
			conn = DbUtil.createConnection();

			// Create SQL Statement
			String sql = "INSERT INTO Teacher (userName, school, password, firstName, lastName) "
					+ "VALUES (?,?,?,?,?)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, teacher.getUserName());
			stmt.setString(2, teacher.getSchool());
			stmt.setString(3, teacher.getPassword());
			stmt.setString(4, teacher.getFname());
			stmt.setString(5, teacher.getLname());
			System.out.println("SQL: " + sql);

			// Execute Statement
			int n = stmt.executeUpdate();
		} catch (Exception e) {
			// Handle Errors for Class
			System.out.println("Class Error. Current DB: " + DB + e);
		} finally {
			// Close Query, and Database Connection
			DbUtil.close(stmt);
			DbUtil.close(conn);
			System.out.println("Closed Resources");
		} // End Try/Catch


	}// end insert(Teacher)
	
	/**
	 * Delete an Teacher.
	 * 
	 * @param admin
	 *            : The Teacher to delete
	 *            
	 */
	public void delete(Teacher teacher) {
		// Check Table & Create Table if it doesn't already exist
		boolean success = createTable();
		System.out
				.println("Check if table exists (create if doesn't exist). Table exists: "
						+ success);

		// Variable Declarations
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			// Load Driver & Connect to Dbase
			conn = DbUtil.createConnection();

			// Create SQL Statement
			String sql = "DELETE FROM Teacher WHERE userName = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, teacher.getUserName());
			System.out.println("SQL: " + sql);

			// Execute Statement
			int n = stmt.executeUpdate();

		} catch (Exception e) {
			// Handle Errors for Class
			System.out.println("Class Error. Current DB: " + DB + e);
		} finally {
			// Close Query, and Database Connection
			DbUtil.close(stmt);
			DbUtil.close(conn);
			System.out.println("Closed Resources");
		} // End Try/Catch

	}// end delete(Teacher)
	
// =========================== MAIN METHOD ========================= //
	
	public static void main(String[] args){
//	// -------- START createTable() TEST --------- //
//		TeachersDAO dao = new TeachersDAO();
//		System.out.println(dao.createTable());
//	// -------- END createTable() TEST ---------- //
		
//	// ---------- START insert(Teacher) TEST ---------- //
//		Teacher teacher = new Teacher();
//		TeachersDAO dao = new TeachersDAO();
//		dao.insert(teacher);
//	// ---------- END insert(Teacher) TEST ------------ //
	
//	// ----------- START select(String) TEST -------------- //
//		TeachersDAO dao = new TeachersDAO();
//		Teacher teacher = dao.select("userName");
//		teacher.display();
//	// ------------ END select(String) TEST -------------- //
		
//	// ------------ START update(Teacher) TEST ------------- //
//		TeachersDAO dao = new TeachersDAO();
//		Teacher teacher = dao.select("userName");
//		teacher.setFname("James");
//		dao.update(teacher);
//	// ------------- END update(Teacher) TEST -------------- //
		
//	// ------------- START delete(Teacher) TEST ------------ //
//		TeachersDAO dao = new TeachersDAO();
//		Teacher teacher = dao.select("userName");
//		dao.delete(teacher);
//	// ------------- END delete(Teacher) TEST ------------- //
		
	}// end main
	
	
}// end class
