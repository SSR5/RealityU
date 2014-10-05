package processSurveys;

/****************************************
 *  RealityUWeb: ProcessChildrenDivocedFemales.java
 *  10/5/2014
 *  Evgeniya Koganitskaya
 ***************************************/
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import obj.Survey;
import dao.SurveysDAO;

public class ProcessChildrenDivorcedFemales {
	

/**********	Properties**********/
	private SurveysDAO sd = new SurveysDAO();                        
	private List<Survey> lstDivorcedFemales = new ArrayList<>();     //List of Divorced Females
	private List<Survey> lstDivWithChild = new ArrayList<>();        //List of Divorced Females With Children
	private List<Survey> lstDivNoChild = new ArrayList<>();          //List of Divorced Females With No Children
	private int grpID;                                               //Holds Group ID

//Random generators for selecting random surveys and number of children to add to survey
	private Random randomGenerator = new Random();
	private int randomInt;
	private int randomKids;

	private double percOfDivWithChild = 0.4;                         //Percent of Divorced Females With Children(now 40%)
    private int numFemaleWithChildNeed;                              //Number of Divorced Females With Children Needed
    private int numFemaleWithNoChildNeed;                            //Number of Divorced Females With No Children Needed
	
/**********Behaviors************/
	/**
	 * Do processing to assign Children to Divorced Females.
	 * 
	 * @param the list of Surveys to be processed from a Group
	 * @return the list of Surveys after being processed
	 */
public List<Survey> doProcess(List<Survey> lstSurveys)

{   System.out.println("Entering ProcessChildrenDivorcedFemales.doProcess() method.");	
	clearLists();                                                    //Clear all lists
	
	for (Survey survey : lstSurveys)
	{  
		if (survey.getGender().equals("Female") && survey.getMarried().equals("Divorced")){
		
			    lstDivorcedFemales.add(survey);                    // Populate list of Divorced Females  

		        if (survey.getChildren().equals("Yes"))
		        {
			        lstDivWithChild.add(survey);                    // Populate list of Divorced Females with Children 
		        }
		       
		        else
		        {
			        lstDivNoChild.add(survey);                     // Populate list of Divorced Females with No Children
		        }
		   
		}//End if Female and Divorced
	}//End for loop
	
	// Needed number of Divorced Females With Children(now 40%)
	numFemaleWithChildNeed = (int)Math.round(lstDivorcedFemales.size()*percOfDivWithChild);

	//Needed number of Divorced Females With No Children(now 60%)
	numFemaleWithNoChildNeed = lstDivorcedFemales.size()-numFemaleWithChildNeed;


	
	// If more than 40% of women have children we need to adjust down
	if (lstDivWithChild.size() > numFemaleWithChildNeed)
	{
		adjustChildrenDown();
	}
	// If more than 60% of women have NO children we need to adjust up
	else {
		if (lstDivNoChild.size() > numFemaleWithNoChildNeed) 
		{
			adjustChildrenUp();
		}
	}// end else

	System.out.println("Leaving ProcessChildrenDivorcedFemales.doProcess() method.");
	System.out.println("-------------------------\n");

	
	System.out.println(lstDivorcedFemales.size());
	System.out.println(lstDivWithChild.size());
	System.out.println(lstDivNoChild.size());
	
	
	grpID = lstSurveys.get(0).getId();                                  //Find Group ID
	List <Survey> surveysList = sd.search("groupID", ""+grpID);        //Create list of Surveys for the Group ID
	return surveysList;

} // end doProcess()


public void adjustChildrenUp() {

	/*
	 * While number of divorced with NO kids is too high we randomly
	 * select a divorced woman with NO kids and give her children
	 */
	while (lstDivNoChild.size() > numFemaleWithNoChildNeed) {

		randomInt = randomGenerator.nextInt(lstDivNoChild.size());
		Survey survey = lstDivNoChild.get(randomInt);

		// Randomly assign 1 or 2 kids
		randomKids = randomGenerator.nextInt(2) + 1;
		
		survey.setChildren("Yes");
		survey.setNumChild(randomKids);
		
		// update database
		sd.update(survey);
		lstDivNoChild.remove(survey);                //Remove survey from the list of Divorced Females with No Children
		lstDivWithChild.add(survey);                 //Add survey to the list of Divorced Females with Children
	}//End while

}// End adjustChildrenUp()



public void adjustChildrenDown() {

	/*
	 * While number of divorced WITH kids is too high we randomly
	 * select a divorced woman with kids and take her children away.
	 */
	while (lstDivWithChild.size() > numFemaleWithChildNeed) {

		randomInt = randomGenerator.nextInt(lstDivWithChild.size());
		Survey survey = lstDivWithChild.get(randomInt);
		
		survey.setChildren("No");
		survey.setNumChild(0);
		
		// update database
		sd.update(survey);
		lstDivWithChild.remove(survey);         //Remove survey from the list of Divorced Females with Children
		lstDivNoChild.add(survey);              //Add survey to the list of Divorced Females with No Children
	}//End while
} //End adjustChildrenDown()

   private void clearLists() {                  //Clear all lists

	lstDivorcedFemales.clear();
	lstDivWithChild.clear();
	lstDivNoChild.clear();
}
   public static void main(String[] args)
   {   	   //List<Survey> lstSurvey = new ArrayList<Survey>();
           //Create SurveysDAO & Survey Objs and Validate Login
           //SurveysDAO sd = new SurveysDAO();
           //lstSurvey = sd.search("groupID", "1");
           //System.out.println(lstSurvey.size());
	       //lstSurvey = new ProcessChildrenDivorcedFemales().doProcess(lstSurvey);
	      
   }// End main()
} // End class

