package obj;

import dao.GroupsDAO;
import dao.SurveysDAO;
import dao.OccupationsDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processSurveys.ProcessCreditScore;
import processSurveys.ProcessMarried;
import processSurveys.ProcessOccupations;

/**
 * 
 * @author James Hammond, SSR5
 * Create Date: 09/28/2014
 * 
 * Edited by:				Date:			Description:
 * James Hammond, SSR5		10/01/2014		Created methods String generateGpa(),String generateGender(), and String generatePrefJob().
 * James Hammond, SSR5		10/02/2014		Created random generator method for education.
 * James Hammond, SSR5		10/03/2014		Finished createGroupSurveys() and deleteGroupSurveys(). Created getTestGroup().
 * James Hammond. SSR5		10/07/2014		Class name changed from "CreateTestGroup" to "TestGroupGenerator"
 * James Hammond, SSR5		10/23/2014		Modified createGroupSurveys method to accept two string parameters.
 * 
 */

/**
 * 
 * This class will be able to create and delete a test group 
 * filled with test surveys that will have default information
 * in inconsequential fields and random information in 
 * fields needed for processing methods.
 *  
 * This class was created for the purposes of testing the
 * survey processing classes.
 * 
 */
public class TestGroupGenerator {
	
	// properties for testGroup1 creation
	
	String groupName;
	String created;
	String modified;
	String school;
	String teacher;
	String classPeriod;
	String surveyStartDate;
	String surveyEndDate;
	String eventDate;
	String studentAccessCode;
	
	List<Survey> testGroupSurveys;
	
	// Create GroupsDAO object to be used with createGroup() 
	// and deleteGroup() methods.
	GroupsDAO testGroupDao = new GroupsDAO();
	
	// Create a SurveysDAO object to be used with 
	// generateSurveys() method.
	SurveysDAO testSurveysDao = new SurveysDAO();
	
	// Create an OccupationDAO object to be used with 
	// generateSurveys() method.
	OccupationsDAO testOccupationDao = new OccupationsDAO();
	
	/**
	 * Group createGroup()
	 * This is for creating a test group to be filled with 
	 * test surveys.
	 * Fill properties of testGroup1 with values pulled from 
	 * the data base.
	 * 
	 * UPDATE: 10/02/2014 **
	 * This method will now use method List createSurveys(int),
	 * This was done to make it easier to assign the test 
	 * surveys to the correct group every time it is created.
	 * No longer returns Group object, now Returns List<Survey>
	 * 
	 * UPDATE: 10/23/2014 **
	 * This method now takes two string parameters.
	 * This is to allow for easier creation of multiple
	 * test groups.
	 * @param name
	 * 		The desired name of the group to be created
	 * @param accessCode
	 * 		The desired access code for the group to be created
	 */
	public List<Survey> createGroupSurveys(String name, String accessCode){
		// assign group values
		
		// add group data to the database
		int rows = testGroupDao.insert(name, created, modified, school, 
				teacher, classPeriod, surveyStartDate, surveyEndDate,
				eventDate, accessCode);
		System.out.println("Inserted "+ rows + "rows.");
		// create a group object with GroupsDAO.find(String) method
		Group testGroup = new Group();
		testGroup = testGroupDao.find(name);
		// display group data on console
		testGroup.display();
		// Get the group id to use with createSurveys(int) method.
		int groupId = testGroup.getId();
		// Run the createSurveys(int) method
		// Pass in 'groupId' to ensure surveys are all associated
		// with the correct group
		testGroupSurveys = generateSurveys(groupId);
		
		return testGroupSurveys;
		
	}// end create group
	
	/**
	 * List<Survey> getGroupSurveys() method
	 * This will return the test surveys from the database
	 * in the form of a Survey ArrayList.
	 * This is for the purpose of deleting, or checking for
	 * the existence of a test group.
	 * @return List<Survey> surveys
	 * 
	 * UPDATE: 10/23/2014
	 * Modified method so that it may be used to retrieve
	 * any desired group based on the group's name.
	 * @param name
	 * 			The name of the group
	 */
	public List<Survey> getTestGroup(String name){
		Group group = testGroupDao.find(name);
		String id = group.getId()+"";
		System.out.println(id);
		List<Survey> surveys = new ArrayList<>();
		surveys = testSurveysDao.search("groupID", id);
		
		for(Survey survey : surveys){
			survey.display();
		}
		
		return surveys;
	}// end getTestGroup(String)
	
	/**
	 * deleteGroupSurveys(List<Survey>)
	 * This is meant to clear the group data
	 * Removes a desired groups data, from the database.
	 * Pass in the group object to be deleted.
	 * Uses delete method from GroupsDAO.
	 * @param testGroup
	 */
	public void deleteGroupSurveys(List<Survey> surveys){
		
		// retrieve groupId from test surveys
		int groupID = surveys.get(1).getGroupID();
		// Create groupDAO object to retrieve info from database
		GroupsDAO testGroupDao = new GroupsDAO();
		// Create SurveysDAO object to edit Database info of surveys
		SurveysDAO testSurveysDao = new SurveysDAO();
		// Retrieve group with same ID as the Surveys groupID
		// Set group from database to new group object
		Group testGroup = testGroupDao.find(groupID);
		// Use deleteGroupSurveys function from SurveysDAO
		testSurveysDao.deleteGroupSurveys(groupID);
		// Use delete function from GroupsDAO
		testGroupDao.delete(testGroup);
		
	}// end delete group
	
// ============= START RANDOM GENERATOR METHODS ============ //
	/**
	 * double generateGpa()
	 * This method will return a random gpa to be used
	 * with the generateSurveys() method.
	 */
	private double generateGpa(){
		// create double variable 'gpa'
		double randomGpa;
		// create Random variable 'random'.
		Random random = new Random();
		// should return a double between 0.5 and 4.0
		randomGpa = 0.5 + (4.0 - 0.5) * random.nextDouble();
		// round the double to 2 decimal places.
		/*
		 * There was a problem with the round function when I 
		 * attempted to divide by 100 on the same line. Instead
		 * of returning 2.34, 0.56, 1.23, 3.56 etc.. it would
		 * return 3.0, 2.0, 1.0, 3.0, 0.0 etc..
		 * This issue was resolved by placing the division
		 * operation on a separate line. 
		 * I honestly have no idea why doing that fixed it.
		 */
		randomGpa = Math.round(randomGpa*100);
		randomGpa = randomGpa/100;
		// return double variable 'gpa'
		return randomGpa;
	} // end double gpaGenerator() method.
	
	/**
	 * String generateGender() method
	 * This method will return a String variable that
	 * will randomly be assigned as either "Male" or "Female".
	 * 
	 * This method is intended for use with the
	 * generateSurveys() method.
	 * 
	 * @return String gender
	 */
	private String generateGender(){
		
		// Create String variable 'gender'.
		String gender;
		// Create Random variable 'random'.
		Random random = new Random();
		// Randomly picks a number between 0 and 1 and
		// assigns that number to int variable 'x'.
		int x = random.nextInt(2);
		// Use if/else statement to
		// pick gender based on variable 'x'.
		if(x == 0)
			gender = "Male";
		else
			gender = "Female";
		// reset int variable 'x'.
		x=0;
		// return String variable 'gender'.
		return gender;
		
	} // end String genderGenerator() method.
	
	/**
	 * String generatePrefJob() method
	 * This method will return a name of a random
	 * occupation in the form of a String.
	 * 
	 * This method is intended for use with the 
	 * generateSurveys() method.
	 * @return String prefJob
	 */
	private String generatePrefJob(){
		
		// Create Random() object.
		Random random = new Random();
		// Create Occupation() object.
		Occupation randOccupation = new Occupation();
		// Create OccupationsDAO() object.
		OccupationsDAO testOccupationDao = new OccupationsDAO();
		// Create List<Occupation> ArrayList<>() object.
		List<Occupation> jobList = new ArrayList<>(); 
		// Run findAllOccupations() method from OccupationsDAO() class,
		// in order to fill jobList ArrayList<>() object.
		jobList = testOccupationDao.findAllOccupations();
		// Select a random number from jobList.size().
		// Assign that random number to variable 'x'.
		int x = random.nextInt(jobList.size());
		// Use ArrayList<>().get(int) method to retrieve 
		// the Occupation object located at index 'x'
		// in the jobList ArrayList<>().
		randOccupation = jobList.get(x);
		// create String variable 'prefJob'.
		String prefJob;
		// use Occupation object method 'getName()' to return
		// the Occupation's String variable 'name'.
		// Assign the returned name to String variable 'prefJob'
		prefJob = randOccupation.getName();
		// return prefJob
		return prefJob;
			
	}// end generatePreferredJob() method
	
	/**
	 * String generateEducation() method
	 * This method will randomly generate a string that
	 * represents a student's potential education choice.
	 * 
	 * This method is meant for use with generateSurveys()
	 * @return String education
	 */
	private String generateEducation(){
		// create a Random() object
		Random random = new Random();
		// Create a String[] array containing all the school
		// choices. These aren't stored in the database anywhere,
		// so it has to be typed out manually.
		String[] schoolChoice = {"High School","Technical School",
				"College + Graduate School"," Some College, Bachelor's Degree",
				"On-The-Job Training","Community College","Technical School"};
		// Create String variable 'education' to represent
		// a random student's education choice.
		String education;
		// Create int variable 'x' to use with choosing a 
		// random index from the String[] schoolChoice array.
		int x;
		// Run nextInt(int) function from Random() class.
		// Drop in schoolChoice array length as parameter.
		// Have to add one to length because max index on 
		// Random().nextInt(int) is exclusive.
		x = random.nextInt(schoolChoice.length);
		// Assign the String from array schoolChoice[x] 
		// to the String variable 'education'.
		education = schoolChoice[x];
		// Return the random education choice.
		return education;
	}// end generateEducation
	
	/**
	 * generateMarried() method
	 * This method will randomly return Strings "Yes" or "No".
	 * These strings represent a test student's choice of 
	 * whether or not to be married.
	 * @return String married
	 */
	private String generateMarried(){
		// Create String variable 'married', this will
		// be the return variable.
		String married;
		// Create a Random() object
		Random random = new Random();
		// Make int variable 'x' to use with random
		int x;
		// Use Random().nextInt(2) to choose a number between
		// 0 or 1. 2 is the max index for nextInt(int) which is
		// exclusive.
		x = random.nextInt(2);
		// Use if/else statement to choose yes or no
		// depending on value of int variable 'x'.
		if(x==0)
			married = "Yes";
		else
			married = "No";
		
		return married;
	}// end generateMarried() method
	
	/**
	 * String generateChildren() method
	 * This method will return a String that contains either
	 * "Yes" or "No". This will represent whether a test student
	 * will prefer children or not.
	 * This method is practically identical to generateMarried()
	 * @return String children
	 */
	private String generateChildren(){
		// Create String variable 'children', this will
				// be the return variable.
				String children;
				// Create a Random() object
				Random random = new Random();
				// Make int variable 'x' to use with random
				int x;
				// Use Random().nextInt(2) to choose a number between
				// 0 or 1. 2 is the max index for nextInt(int) which is
				// exclusive.
				x = random.nextInt(2);
				// Use if/else statement to choose yes or no
				// depending on value of int variable 'x'.
				if(x==0)
					children = "Yes";
				else
					children = "No";
				
				return children;
	}// end generateChildren() method
	
	/**
	 * int generateNumChild() method
	 * This method will return a random integer between
	 * 1 and 3(inclusive) that indicates the number of children
	 * desired by a test student.
	 * @return int numChild
	 */
	private int generateNumChild(){
		// Create Random() object.
		Random random = new Random();
		// Create int variable 'max' to represent max children.
		int max;
		// Create int variable 'min' to represent min children.
		int min;
		// assign the maximum and minimum values
		max = 3;
		min = 1;
		// Assign a random number between max and min(inclusive)
		// to int variable 'numChild'. This will be the final
		// number that represents the selected number of children.
		int numChild = random.nextInt((max - min) + 1) + min;
		
		return numChild;
	}// end generateNumChild() method.
	
	/**
	 * This method will return String value "Yes" or "No" which
	 * represents whether or not the test student will use
	 * credit cards.
	 * This method is for use with the generateSurveys() method.
	 * @return String cCards
	 */
	private String generateCcards(){
		// Create String variable 'cCards'. This will be the
		// return variable.
		String cCards;
		// Create a Random() object
		Random random = new Random();
		// Create int variable 'x' to hold random value
		int x;
		// Assign random value between 0 and 1 to 'x'.
		x = random.nextInt(2);
		// Give String variable 'cCards' value of 
		// "Yes" or "No" depending on the value of 'x'.
		if(x==0)
			cCards = "Yes";
		else
			cCards = "No";
		
		return cCards;
	}// end generateCcards() method
	
	/**
	 * This method will return String values of 
	 * "Emergencies Only" or "Non-Emergencies" to indicate
	 * the preferred use of credit cards by a test student.
	 * This method is intended for use with generateSurveys()
	 * @return String cCardUse
	 */
	private String generateCcardUse(){
		// Create String Variable 'cCardUse'
		// This will be the return variable
		String cCardUse;
		// Create Random() object
		Random random = new Random();
		// Create int variable 'x' for use with random.
		int x;
		// Give int variable 'x' random value of 0 or 1
		x = random.nextInt(2);
		// Use if/else to indicate String value of 
		// variable 'cCardUse'. This is determined by value
		// of variable 'x'.
		if(x == 0)
			cCardUse = "Emergencies Only";
		else
			cCardUse = "Non-Emergencies";
		
		return cCardUse;
		
	}// end generateCcardsUse
	
	/**
	 * This method will return a String value that represents
	 * the spending preferences of a student on groceries.
	 * 
	 * @return String groceries
	 */
	private String generateGroceries(){
		// Create String variable, this is the return variable
		String groceries;
		// Create a string array containing the price options
		// for groceries
		String[] priceOptions = {"Low-Priced","Moderately-Priced", "High-Priced"};
		// Create random object to be used when choosing a 
		// random index from the array
		Random random = new Random();
		// create int variable to assign random number to
		int x;
		// Choose random number between 0 and the 'priceOptions[]'
		// array's length.
		x = random.nextInt(priceOptions.length);
		// Set 'groceries' variable to index 'x' of
		// 'priceOptions[]' array.
		groceries = priceOptions[x];
		
		return groceries;
		
		
	}// end String generateGroceries() method
	
	/**
	 * String generateClothing() method
	 * This method will return a string indicating the spending
	 * preferences of a student for clothing.
	 * This method is practically identical to the 
	 * String generateGroceries() method
	 * @return String clothing
	 */
	private String generateClothing(){
		// Create String variable, this is the return variable
		String clothing;
		// Create a string array containing the price options
		// for clothing
		String[] priceOptions = {"Low-Priced","Moderately-Priced", "High-Priced"};
		// Create random object to be used when choosing a 
		// random index from the array
		Random random = new Random();
		// create int variable to assign random number to
		int x;
		// Choose random number between 0 and the 'priceOptions[]'
		// array's length.
		x = random.nextInt(priceOptions.length);
		// Set 'clothing' variable to index 'x' of
		// 'priceOptions[]' array.
		clothing = priceOptions[x];
				
		return clothing;
				
		
	}// end String generate Groceries() method
	
	/**
	 * String generateSave() method
	 * This method will randomly return String 
	 * values that represent how much a test student will
	 * save each month.
	 * This method is for use with the generateSurveys() method
	 * @return String save
	 */
	private String generateSave(){
		// Create Random object
		Random random = new Random();
		// Create String variable 'save', this will be the 
		// return variable.
		String save;
		// Create a String[] array of monthly saving options.
		// These options are not specifically listed in the
		// database, so have to fill array manually.
		String[] saveOptions = {"Under $25","$26-$100","$101-$500",
				"$501-$1,000", "Over $1,000"};
		// Create int 'x' to hold random value between 0 and 
		// the length of the saveOptions array.
		int x;
		x = random.nextInt(saveOptions.length);
		// Set 'save' to the value of index 'x' from
		// the saveOptions array.
		save = saveOptions[x];
		
		return save;
		
		
	}// end String generateCcardsUse() method
	
// ===========  END RANDOM GENERATOR METHODS  ============= //
	
	/**
	 * generateSurveys() will create 24 surveys that belong
	 * to the same test group created with the createGroup
	 * method. The surveys have some similar information, 
	 * with varying values for GPA, Marriage, Schooling and
	 * career choices.
	 * 
	 * This method is meant to create surveys with many
	 * different values in order to test as many possibilities
	 * as possible.
	 * 
	 * This method will return a Survey List to be used
	 * with the processSurveys classes.
	 * 
	 */
	private List<Survey> generateSurveys(int groupID){
		
		// Assign a variable to type student.
		// Could be done inside the for loop if preferred.
		Survey student;
		// Create surveyDAO object for adding survey info
		// to the database
		SurveysDAO surveyDao = new SurveysDAO();
		// Start a loop that repeats 24 times, each repetition
		// will create a new student object and insert it into
		// the database
		List<Survey> surveyList = new ArrayList<>();
		for(int i=0; i<24; i++){
			
		// --- START random generators --- //
			// gender
			String gender = generateGender();
			// gpa
			double gpa = generateGpa();
			// prefJob
			String prefJob = generatePrefJob();
			// education
			String education = generateEducation();
			// married
			String married = generateMarried();
			// children
			String children = generateChildren();
			// numChild
			int numChild = 0;
			if(children.equals("Yes"))
			numChild = generateNumChild();
			// cCards
			String cCards = generateCcards();
			// cCardUse
			String cCardUse = generateCcardUse();
			// groceries
			String groceries = generateGroceries();
			// clothing
			String clothing = generateClothing();
			// save
			String save = generateSave();
			
		// --- END random generators --- //
			
			// Make a new object each time through loop, tag the 
			// incrementing i variable to the end of names to
			// individualize surveys.
			student = new Survey(900+i,"fName"+i,"lName"+i,"01/01/2001",gpa,
						gender, groupID, education, prefJob,"job",
						married, 0, children, numChild, cCards, cCardUse,
						groceries,clothing, "home"+i, "vehicle"+i, 0.0, 0.0,
						save,"fun"+i);
			
			// add the new student survey to the survey list
			surveyList.add(student);
			// add the new survey to the database
			surveyDao.insert(student);
			
		}//end for loop
		// return the surveyList
		return surveyList;
		
	}// end create surveys
	
	public static void main(String[] args){
		// use (Ctrl + /) in order to 
		// comment/uncomment selected lines
		
	
//	// --- START create/delete group TEST --- //
//		// test create group and delete group.
//		// should add a group to the database
//		Group testGroup = ctg.createGroup();
//		// should delete test group from the database
//		ctg.deleteGroup(testGroup);
//	// --- END create/delete group TEST --- //
				
//	// --- START random gender TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String gender = testGroup.generateGender();
//		System.out.println(gender);
//	// --- END random gender TEST --- //
		
//	// --- START random gpa TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		double x = testGroup.generateGpa();
//		System.out.println(""+x);
//	// --- END random gpa TEST --- //
		
//	// --- START random job TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String prefJob = testGroup.generatePreferredJob();
//		System.out.println(prefJob);		
//	// --- END random job TEST ---- //
		
//	// --- START random education TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String education = testGroup.generateEducation();
//		System.out.println(education);
//	// --- END random education TEST --- //
		
//	// --- START random married TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String married = testGroup.generateMarried();
//		System.out.println(married);
//	// --- END random married TEST --- //
		
//	// --- START random children TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String children = testGroup.generateChildren();
//		System.out.println(children);
//	// --- END random children TEST --- //
		
//	// --- START random number of children TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		int numChild = testGroup.generateNumChild();
//		System.out.println(""+numChild);
//	// --- END random number of children TEST --- //
		
//	// --- START random cCards TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String cCards = testGroup.generateCcards();
//		System.out.println(cCards);	
//	// --- END random cCards TEST
		
//	// --- START random cCard use TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String cCardUse = testGroup.generateCcardUse();
//		System.out.println(cCardUse);
//	// --- END random cCard use TEST --- //
		
//	// --- START random save TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String save = testGroup.generateSave();
//		System.out.println(save);
//	// --- END random save TEST --- //
		
//	// --- START random groceries TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String groceries = testGroup.generateGroceries();
//		System.out.println(groceries);
//	// --- END random groceries TEST --- //
		
//	// --- START random clothing TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		String clothing = testGroup.generateClothing();
//		System.out.println(clothing);
//	// --- END random clothing TEST --- //
		
//	// --- START createGroupSurveys() TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		List<Survey> testSurveys;
//		testSurveys = testGroup.createGroupSurveys("TestGroup1","Test123");
//		// display surveys to console with for each loop
//		for( Survey surveys : testSurveys){
//			surveys.display();
//		}// end for each
//	// --- END createGroupSurveys() TEST --- //
		
//	// --- START getTestGroup() TEST --- //
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		List<Survey> testSurveys = testGroup.retrieveTestGroup();
//		for(Survey survey : testSurveys){
//			survey.display();
//		}// end for each
//	// --- END retrieveTestGroup() TEST --- //

//	// --- START deleteGroupSurvey(Survey) TEST --- //
//		
//		TestGroupGenerator testGroup = new TestGroupGenerator();
//		List<Survey> testSurveys = testGroup.getTestGroup("TestGroup1");	
//		testGroup.deleteGroupSurveys(testSurveys);
//		
//	// --- END deleteGroupSurvey(Survey) TEST --- //
		


	
//		TestGroupGenerator tgg = new TestGroupGenerator();
//		tgg.createGroupSurveys("test1", "abc123");
//		List<Survey> testGroup = tgg.getTestGroup("test1");
//		tgg.deleteGroupSurveys(testGroup);
		
	}// end main

}// end class

