package processSurveys;
/********************************************************************
 *	RealityUWeb: ProcessChildrenDivorcedMales.java
 *  10/5/2014
 *  Evgeniya Koganitskaya
 ********************************************************************/
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import obj.Survey;
import dao.SurveysDAO;

public class ProcessChildrenDivorcedMales {
	/**********	Properties**********/
	private SurveysDAO sd = new SurveysDAO();                        
	private List<Survey> lstDivorcedMales = new ArrayList<>();            //List of Divorced Males
	private List<Survey> lstDivMalesWithChild = new ArrayList<>();        //List of Divorced Males With Children
	private List<Survey> lstDivMalesNoChild = new ArrayList<>();          //List of Divorced Males With No Children
	private int grpID;                                               //Holds Group ID

//Random generators for selecting random surveys and number of children to add to survey
	private Random randomGenerator = new Random();
	private int randomInt;
	private int randomKids;

	private double percOfDivMalesWithChild = 0.4;                    //Percent of Divorced Males With Children(now 40%)
    private int numMaleWithChildNeed;                              //Number of Divorced Males With Children Needed
    private int numMaleWithNoChildNeed;                            //Number of Divorced Males With No Children Needed
	
/**********Behaviors************/
	/**
	 * Do processing to assign Children to Divorced Males.
	 * 
	 * @param the list of Surveys to be processed from a Group
	 * @return the list of Surveys after being processed
	 */
public List<Survey> doProcess(List<Survey> lstSurveys)

{   System.out.println("Entering ProcessChildrenDivorcedMales.doProcess() method.");	
	clearLists();                                                    //Clear all lists
	
	for (Survey survey : lstSurveys)
	{  
		if (survey.getGender().equals("Male") && survey.getMarried().equals("Divorced")){
		
			    lstDivorcedMales.add(survey);                    // Populate list of Divorced Males  

		        if (survey.getChildren().equals("Yes"))
		        {
			        lstDivMalesWithChild.add(survey);            // Populate list of Divorced Males with Children 
		        }
		       
		        else
		        {
			        lstDivMalesNoChild.add(survey);              // Populate list of Divorced Males with No Children
		        }
		   
		}//End if Male and Divorced
	}//End for loop
	
	// Needed number of Divorced Males With Children(now 40%)
	numMaleWithChildNeed = (int)Math.round(lstDivorcedMales.size()*percOfDivMalesWithChild);

	//Needed number of Divorced Males With No Children(now 60%)
	numMaleWithNoChildNeed = lstDivorcedMales.size()-numMaleWithChildNeed;


	
	// If more than 40% of men have children we need to adjust down
	if (lstDivMalesWithChild.size() > numMaleWithChildNeed)
	{
		adjustChildrenDown();
	}
	// If more than 60% of men have NO children we need to adjust up
	else {
		if (lstDivMalesNoChild.size() > numMaleWithNoChildNeed) 
		{
			adjustChildrenUp();
		}
	}// end else

	System.out.println("Leaving ProcessChildrenDivorcedMales.doProcess() method.");
	System.out.println("-------------------------\n");

	
	System.out.println(lstDivorcedMales.size());
	System.out.println(lstDivMalesWithChild.size());
	System.out.println(lstDivMalesNoChild.size());
	
	grpID = lstSurveys.get(0).getId();                                  //Find Group ID
	List <Survey> surveysList = sd.search("groupID", ""+grpID);        //Create list of Surveys for the Group ID
	return surveysList;

} // end doProcess()


public void adjustChildrenUp() {

	/*
	 * While number of divorced with NO kids is too high we randomly
	 * select a divorced man with NO kids and give him children
	 */
	while (lstDivMalesNoChild.size() > numMaleWithNoChildNeed) {

		randomInt = randomGenerator.nextInt(lstDivMalesNoChild.size());
		Survey survey = lstDivMalesNoChild.get(randomInt);

		// Randomly assign 1 or 2 kids
		randomKids = randomGenerator.nextInt(2) + 1;
		
		survey.setChildren("Yes");
		survey.setNumChild(randomKids);
		
		// update database
		sd.update(survey);
		lstDivMalesNoChild.remove(survey);                //Remove survey from the list of Divorced Males with No Children
		lstDivMalesWithChild.add(survey);                 //Add survey to the list of Divorced Males with Children
	}//End while

}// End adjustChildrenUp()



public void adjustChildrenDown() {

	/*
	 * While number of divorced WITH kids is too high we randomly
	 * select a divorced man with kids and take his children away.
	 */
	while (lstDivMalesWithChild.size() > numMaleWithChildNeed) {

		randomInt = randomGenerator.nextInt(lstDivMalesWithChild.size());
		Survey survey = lstDivMalesWithChild.get(randomInt);
		
		survey.setChildren("No");
		survey.setNumChild(0);
		
		// update database
		sd.update(survey);
		lstDivMalesWithChild.remove(survey);         //Remove survey from the list of Divorced Males with Children
		lstDivMalesNoChild.add(survey);              //Add survey to the list of Divorced Males with No Children
	}//End while
} //End adjustChildrenDown()

   private void clearLists() {                  //Clear all lists

	lstDivorcedMales.clear();
	lstDivMalesWithChild.clear();
	lstDivMalesNoChild.clear();
}
   public static void main(String[] args)
   {   	   //List<Survey> lstSurvey = new ArrayList<Survey>();
           //Create SurveysDAO & Survey Objs and Validate Login
           //SurveysDAO sd = new SurveysDAO();
           //lstSurvey = sd.search("groupID", "1");
           //System.out.println(lstSurvey.size());
	       //lstSurvey = new ProcessChildrenDivorcedMales().doProcess(lstSurvey);
	      
   }// End main()
}// End class
