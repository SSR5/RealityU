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

import obj.Administrator;
import obj.Survey;
import obj.Teacher;
import dao.DbUtil;

/**
 * TeachersDAO
 * This class handles all database interaction required
 * for adding or changing data about Teachers participating
 * in the RealityU events.
 * @author James Hammond, SSR5		DATE: 10/15/2014
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
				+ " ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "'firstName' VARCHAR NOT NULL,"
				+ "'lastName' VARCHAR NOT NULL,"
				+ "'password' VARCHAR NOT NULL,"
				+ "'school' VARCHAR NOT NULL)";
				

		success = DbUtil.createTable(tableName, sql);

		return success;
	}// end createTable()
	
	
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
				if (column.equals("id")) {
					//No single quotes on 'search'
					strWhere = "WHERE " + column + " = " + search;
				} else { //for all String dbase fields
					//Single quotes on 'search'
					strWhere = "WHERE " + column + " = '" + search + "'";
				} //end if
			} //end if

			// Create SQL Statement
			sql = "SELECT * FROM Survey " + strWhere;
			stmt = conn.prepareStatement(sql);
			System.out.println("SQL: " + sql);

			// Execute Statement - Get ResultSet by Column Name
			rs = stmt.executeQuery();
			
			//Process the ResultSet
			while (rs.next()) {
				Teacher teacher = new Teacher();

				teacher.setId(rs.getInt("id"));
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
	 * Finds an Administrator by id.
	 * 
	 * @param id
	 *            : The Administrator id to search for.
	 * @return Returns an Administrator object with that id.
	 */
	public Teacher select(int id) {
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
			strWhere = "WHERE id = " + id;

			sql = "SELECT * FROM Teacher " + strWhere;
			stmt = conn.prepareStatement(sql);
			System.out.println("SQL: " + sql);

			// Execute Statement - Get ResultSet by Column Name
			rs = stmt.executeQuery();

			// Process the ResultSet
			while (rs.next()) {
				teacher.setId(rs.getInt("id"));
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
	} // end Teacher select(int) method
	
	
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
			String sql = "UPDATE Teacher SET school=?, password=?, fname=?, lname=? "
					+ "WHERE id=?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, teacher.getSchool());
			stmt.setString(2, teacher.getPassword());
			stmt.setString(3, teacher.getFname());
			stmt.setString(4, teacher.getLname());
			stmt.setInt(5, teacher.getId());
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
	 * Insert a new Administrator.
	 * 
	 * @param username
	 *            : The Administrator username
	 * @param password
	 *            : The Administrator password
	 * @param fname
	 *            : The Administrator fname
	 * @param lname
	 *            : The Administrator lname
	 * @return Returns an integer:<br>
	 *         0: Failure<br>
	 *         1: Success
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
			String sql = "INSERT INTO Administrator (school, password, fname, lname) "
					+ "VALUES (?,?,?,?)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, teacher.getSchool());
			stmt.setString(2, teacher.getPassword());
			stmt.setString(3, teacher.getFname());
			stmt.setString(4, teacher.getLname());
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
	 * Delete an Administrator.
	 * 
	 * @param admin
	 *            : The Administrator to delete
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
			String sql = "DELETE FROM Administrator WHERE id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, teacher.getId());
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
	
	
	
	
}// end class
