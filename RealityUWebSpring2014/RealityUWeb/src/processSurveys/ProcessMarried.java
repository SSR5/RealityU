package processSurveys;
/********************************************************************
 *	RealityUWeb: ProcessMarried.java
 *  Evgeniya Koganitskaya
 *  10/08/2014
 ********************************************************************/
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import obj.Survey;
import dao.SurveysDAO;

/**
 * The Class ProcessMarried
 */
public class ProcessMarried {	
	
	private SurveysDAO sd = new SurveysDAO();

	private List<Survey> listOfMales = new ArrayList<>();

	private List<Survey> listOfFemales = new ArrayList<>();
	
	private List<Survey> listOfSingleMales = new ArrayList<>();
	
	private List<Survey> listOfSingleFemales = new ArrayList<>();

	private List<Survey> listOfMarriedMales = new ArrayList<>();

	private List<Survey> listOfMarriedFemales = new ArrayList<>();

	private List<Survey> listOfDivorcedMales = new ArrayList<>();

	private List<Survey> listOfDivorcedFemales = new ArrayList<>();

	private float marriedRequirementRatio = .4f; // 40%

	private float divorcedRequirementRatio = .35f; // 35%
	
	int numEachNeeded = 0;
	int numDivFemalesNeeded = 0;
	int numDivMalesNeeded = 0;

	/**
	 * Do processing to assign Marriages/Spouses.
	 * 
	 * @param the list of Surveys to be processed from a Group
	 * @return the list of Surveys after being processed
	 */
	public List<Survey> doProcess(List<Survey> surveysList) {

		System.out.println("Entering ProcessMarried.doProcess() method.");
		// populate the various survey lists from main survey list
		//Can use any survey in surveysList to get Group ID (all grp id's are same)
		int grpID = surveysList.get(0).getId();
		clearLists();
		
		for (Survey survey : surveysList) {

			// Populate gender lists
			//Male
			if (survey.getGender().equals("Male")) {
				listOfMales.add(survey);
				survey.setSpouse(0); // clear the list for processing
				sd.update(survey);
			}
			// Female
			else {
				listOfFemales.add(survey);
				survey.setSpouse(0); // clear the list for processing
				sd.update(survey);
			}	
			// Populate married list
			if (survey.getMarried().equals("Yes")) {
				if (survey.getGender().equals("Male")) // Married Male
					listOfMarriedMales.add(survey);
				else // Married Female
					listOfMarriedFemales.add(survey);
			} // end if married code block			
			// Populate single list
			else
				if (survey.getMarried().equals("No")) {
				    if (survey.getGender().equals("Male")) // Single Male
					    listOfSingleMales.add(survey);
				    else  // Single Female
					    listOfSingleFemales.add(survey);
			      } // end if single code block			    
			// Change divorced to Single
		    else 
				if (survey.getMarried().equals("Divorced")){
					if (survey.getGender().equals("Male")){
						survey.setMarried("No");
						sd.update(survey);
						listOfSingleMales.add(survey);}
					else{
						survey.setMarried("No");
						sd.update(survey);
						listOfSingleFemales.add(survey);}
				}
             } // end for loop

		//Determine number of Male/Female Married Needed to match marriedRequirementRatio		
		if (listOfMales.size() > listOfFemales.size()){
			numEachNeeded = Math.round(listOfFemales.size()*marriedRequirementRatio);}
		else{
			numEachNeeded = Math.round(listOfMales.size()*marriedRequirementRatio);}
				
		// Set Married & Some Divorced if needed to meet married ratio
		
		if (listOfMarriedMales.size() < numEachNeeded){
		setMarriedMalesUp();}
		
		if(listOfMarriedMales.size() > numEachNeeded){
		setMarriedMalesDown();}
		
		if (listOfMarriedFemales.size() < numEachNeeded){
		setMarriedFemalesUp();}
		
		if (listOfMarriedFemales.size() > numEachNeeded){
		setMarriedFemalesDown();}

		// Set spouses
		setSpouses();
	
		//Determine number of Male/Female Divorced Needed to match divorcedRequirementRatio
		numDivMalesNeeded = Math.round(listOfMales.size()*divorcedRequirementRatio);
		numDivFemalesNeeded = Math.round(listOfFemales.size()*divorcedRequirementRatio); 
		
		// Set Divorced, Make sure we have 35% of the group divorced
		if (listOfDivorcedMales.size() < numDivMalesNeeded){
		    setDivorcedMalesUp();}
		
		if (listOfDivorcedMales.size() > numDivMalesNeeded){	
			setDivorcedMalesDown();}
		
		if (listOfDivorcedFemales.size() < numDivFemalesNeeded){
			setDivorcedFemalesUp();}
		
		if (listOfDivorcedFemales.size() > numDivFemalesNeeded){
		setDivorcedFemalesDown();}
		
		//Set children to 0 for Single Males and Females	
		for(Survey survey: listOfSingleMales){
			survey.setChildren("No");
			survey.setNumChild(0);
			sd.update(survey);
		}
		for(Survey survey: listOfSingleFemales){
			survey.setChildren("No");
			survey.setNumChild(0);
			sd.update(survey);
		}
		
		// Test
		System.out.println("Total = "+surveysList.size());
		System.out.println("List of Males size = "+listOfMales.size());
		System.out.println("List of Females size = "+listOfFemales.size());
		System.out.println("List of Married Males size = "+listOfMarriedMales.size());
		System.out.println("List of Married Females size = "+listOfMarriedFemales.size());
		System.out.println("List of Single Males size = "+listOfSingleMales.size());
		System.out.println("List of Single Females size = "+listOfSingleFemales.size());
		System.out.println("List of Divorced Males size = "+listOfDivorcedMales.size());
		System.out.println("List of Divorced Females size = "+listOfDivorcedFemales.size());
		
		System.out.println("Leaving ProcessMarried.doProcess() method.");
		System.out.println("-------------------------\n");
		
		//After all processing get revised list of surveys for this group
		surveysList = sd.search("groupID", ""+grpID);
		return surveysList;
	
	} // end doProcess() method
	
	public void setMarriedMalesUp() {
		Random random = new Random();
		while (listOfMarriedMales.size() < numEachNeeded) {
			Survey survey = listOfSingleMales.get(random.nextInt(listOfSingleMales.size()));
			//Change from Single to Married
			survey.setMarried("Yes");
			sd.update(survey);
			listOfSingleMales.remove(survey);
			listOfMarriedMales.add(survey);
		} // end while
	}

	public void setMarriedMalesDown()	{
		Random random = new Random();
		while (listOfMarriedMales.size() > numEachNeeded) {
			Survey survey = listOfMarriedMales.get(random.nextInt(listOfMarriedMales.size()));
			//Change from Married to Single
			survey.setMarried("No");
			sd.update(survey);
			listOfMarriedMales.remove(survey);
			listOfSingleMales.add(survey);				
		} // end while
	}


	/**
	 * Loop through females and make some married or divorced until get correct number.
	 */
	public void setMarriedFemalesUp() {
		
		Random random = new Random();		
		while (listOfMarriedFemales.size() < numEachNeeded) {
			Survey survey = listOfSingleFemales.get(random.nextInt(listOfSingleFemales.size()));
			//Change from Single to Married
			survey.setMarried("Yes");
			sd.update(survey);
			listOfSingleFemales.remove(survey);
			listOfMarriedFemales.add(survey);
		} // end while
	}
	public void setMarriedFemalesDown(){
		Random random = new Random();
		while (listOfMarriedFemales.size() > numEachNeeded) {
			Survey survey = listOfMarriedFemales.get(random.nextInt(listOfMarriedFemales.size()));
			//Change from Married to Divorced
			survey.setMarried("No");
			sd.update(survey);
			listOfMarriedFemales.remove(survey);
			listOfSingleFemales.add(survey);				
		} // end while
	}

	// ************************************************
	// ************************************************
	
	/**
	 * Sets the spouses for the group.
	 */
	public void setSpouses() {
		// TODO: Try to match similar income (Still to be done)
		
		Survey marriedMale = null;
		Survey marriedFemale = null;

			for (int i = 0; i < listOfMarriedMales.size(); i++) {
				
				marriedMale = listOfMarriedMales.get(i);
				
				if ( listOfMarriedFemales.size() > i ) {
					
					// Set spouse from parallel List. This is like randomly placing spouses together
					// since Surveys will be filled out by students in random order. 
					marriedFemale = listOfMarriedFemales.get(i);
					//Set Married Spouse value as Survey ID #
					marriedMale.setSpouse(marriedFemale.getId());
					sd.update(marriedMale);
					marriedFemale.setSpouse(marriedMale.getId());
					sd.update(marriedFemale);

				} else { break; } // no point in looping through the rest if no more eligible females altho # should be =
			} // end for Loop	
			
	} // end setSpouses() method

	/**
	 * Loop through males and make some divorced.
	 */
	public void setDivorcedMalesUp() {
		Random rndDivMale = new Random();
			while (listOfDivorcedMales.size() < numDivMalesNeeded) {
				Survey survey = listOfSingleMales.get(rndDivMale.nextInt(listOfSingleMales.size()));
				survey.setMarried("Divorced");
				sd.update(survey);
				listOfSingleMales.remove(survey);
				listOfDivorcedMales.add(survey);
			} // end while loop
	}
	public void setDivorcedMalesDown(){
		Random rndDivMale = new Random();
			while (listOfDivorcedMales.size() > numDivMalesNeeded) {
				Survey survey = listOfDivorcedMales.get(rndDivMale.nextInt(listOfDivorcedMales.size()));
				survey.setMarried("No");
				sd.update(survey);
				listOfDivorcedMales.remove(survey);
				listOfSingleMales.add(survey);
			} // end while
	} 

	/**
	 * Loop through females and make some divorced.
	 */
	public void setDivorcedFemalesUp() {

		Random rndDivFemale = new Random();
		while (listOfDivorcedFemales.size() < numDivFemalesNeeded) {
			Survey survey = listOfSingleFemales.get(rndDivFemale.nextInt(listOfSingleFemales.size()));
			survey.setMarried("Divorced");
			sd.update(survey);
			listOfSingleFemales.remove(survey);
			listOfDivorcedFemales.add(survey);
		} // end while loop
	}
	public void setDivorcedFemalesDown(){
		Random rndDivFemale = new Random();
		while (listOfDivorcedFemales.size() > numDivFemalesNeeded) {
			Survey survey = listOfDivorcedFemales.get(rndDivFemale.nextInt(listOfDivorcedFemales.size()));
			//if (survey.getMarried().equals("Divorced")) {
				survey.setMarried("No");
				sd.update(survey);
				listOfDivorcedFemales.remove(survey);
				listOfSingleFemales.add(survey);
			//} // end if
		} // end while
	} 
	
	private void clearLists() {
		listOfMales.clear();
		listOfFemales.clear();
		listOfSingleMales.clear();
		listOfSingleFemales.clear();
		listOfMarriedMales.clear();
		listOfMarriedFemales.clear();
		listOfDivorcedMales.clear();
		listOfDivorcedFemales.clear();
	}
	
	
//  ========================  MAIN METHOD  ==================== 
	 public static void main(String[] args) {
	    List<Survey> lstSurvey = new ArrayList<Survey>();
       //Create SurveysDAO & Survey Objs and Validate Login
       SurveysDAO sd = new SurveysDAO();
       lstSurvey = sd.search("groupID", "1");
       lstSurvey = new ProcessMarried().doProcess(lstSurvey);
       

	} //end main()	

} //end class
