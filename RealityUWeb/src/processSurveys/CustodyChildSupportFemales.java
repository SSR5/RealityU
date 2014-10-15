package processSurveys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import obj.Occupation;
import obj.Survey;
import dao.OccupationsDAO;
import dao.SurveysDAO;

public class CustodyChildSupportFemales {

	private SurveysDAO sd = new SurveysDAO();
	private OccupationsDAO od = new OccupationsDAO();
	private List<Survey> lstDivFemalesWithChild = new ArrayList<>(); 
	private List<Survey> lstDivFemalesReceiving = new ArrayList<>();
	private List<Survey> lstDivFemalesPaying = new ArrayList<>();
	private Random randomGenerator = new Random(); 
	private int randomFemale; 
	private float divFemalesReceivingRatio = .75f;
	int numberFemalesPaying = 0;
	int numberFemalesReceiving = 0;
	
	public List<Survey> doProcess(List<Survey> surveysList) {
		System.out.println("Entering CustodyChildSupportFemales.doProcess() method.");
		// populate the various survey lists from main survey list
		//Can use any survey in surveysList to get Group ID (all grp id's are same)
		int grpID = surveysList.get(0).getId();
		clearLists();
		
		//set childSupport to '0' for all surveys in group 
		for (Survey survey : surveysList) { 
			survey.setChildSupport(0.0); 
			sd.update(survey); 
			} // end for loop 
		
		//Populate the "divorced with children" list of females. 
		for (Survey survey : surveysList) { 
			if ( (survey.getMarried().equals("Divorced")) && (survey.getNumChild() > 0) ) { 
			// Get surveys for all divorced females with children in group 
			    if (survey.getGender().equals("Female")) { 
			    lstDivFemalesWithChild.add(survey); 
			    }
			}
		}
		System.out.println(lstDivFemalesWithChild.size()); 
		
		// Needed number of Divorced Females Receiving child support (now 75%)
		numberFemalesReceiving = Math.round(lstDivFemalesWithChild.size()*divFemalesReceivingRatio);
		// Needed number of Divorced Females Paying child support (now 25%)
		numberFemalesPaying = lstDivFemalesWithChild.size() - numberFemalesReceiving;
		
		System.out.println(numberFemalesPaying + "," + numberFemalesReceiving);
		
		//Populate List of Divorced Females Paying Child Support
		while(lstDivFemalesPaying.size() < numberFemalesPaying){
			randomFemale = randomGenerator.nextInt(lstDivFemalesWithChild.size());
			Survey survey = lstDivFemalesWithChild.get(randomFemale);
			lstDivFemalesWithChild.remove(survey);
			lstDivFemalesPaying.add(survey);
		}
		System.out.println(lstDivFemalesPaying.size());
		
		//Populate List of Divorced Females Receiving Child Support
		while(lstDivFemalesReceiving.size() < numberFemalesReceiving){
		    randomFemale = randomGenerator.nextInt(lstDivFemalesWithChild.size());
			Survey survey = lstDivFemalesWithChild.get(randomFemale);
			lstDivFemalesWithChild.remove(survey);
			lstDivFemalesReceiving.add(survey);
		}
		System.out.println(lstDivFemalesReceiving.size());
		
		// Set child support for Divorced Females Paying Support
		for (Survey survey : lstDivFemalesPaying){
			int numChild = survey.getNumChild();
			String job = survey.getJob();
			System.out.println(numChild);
			System.out.println(job);
			List<Occupation> jobList = od.search("name", job);
			System.out.println(jobList.size());
			for (Occupation occ: jobList){
				double salary = occ.getAnnGrossSal();
				double support = 0;
				System.out.println(salary);
				if(salary >= 0 && salary < 15000){
				    support = -(175/2*(numChild - 1) + 175);
				}
				else if (salary >= 15000 && salary < 30000){
					support = -(300/2*(numChild - 1) + 300);
				}
				else if (salary >= 30000 && salary < 60000){
					support = -(500/2*(numChild - 1) + 500);
				}
				else if (salary >= 60000){
					support = -(700/2*(numChild - 1) + 700);
				}
				survey.setChildSupport(support);
				sd.update(survey);
				System.out.println(support);
			}
		}
		
		// Set child support for Divorced Females Receiving Support
		for (Survey survey : lstDivFemalesReceiving){
			int numChild = survey.getNumChild();
			String job = survey.getJob();
			System.out.println(numChild);
			System.out.println(job);
			List<Occupation> jobList = od.search("name", job);
			System.out.println(jobList.size());
			for (Occupation occ: jobList){
				double salary = occ.getAnnGrossSal();
				double support = 0;
				System.out.println(salary);
				if(salary >= 0 && salary < 15000){
				    support = (175/2*(numChild - 1) + 175);
				}
				else if (salary >= 15000 && salary < 30000){
					support = (300/2*(numChild - 1) + 300);
				}
				else if (salary >= 30000 && salary < 60000){
					support = (500/2*(numChild - 1) + 500);
				}
				else if (salary >= 60000){
					support = (700/2*(numChild - 1) + 700);
				}
				survey.setChildSupport(support);
				sd.update(survey);
				System.out.println(support);
			}
		}
			
		surveysList = sd.search("groupID", ""+grpID);
		return surveysList;
	}// end doProcess method
	public void clearLists(){
		lstDivFemalesWithChild.clear();
		lstDivFemalesReceiving.clear();
		lstDivFemalesPaying.clear();
	}
//  ========================  MAIN METHOD  ==================== 
	 public static void main(String[] args) {
	    List<Survey> lstSurvey = new ArrayList<Survey>();
       //Create SurveysDAO & Survey Objs and Validate Login
       SurveysDAO sd = new SurveysDAO();
       lstSurvey = sd.search("groupID", "1");
       lstSurvey = new CustodyChildSupportFemales().doProcess(lstSurvey); 
	} //end main()	
}