package processSurveys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import obj.Occupation;
import obj.Survey;
import dao.OccupationsDAO;
import dao.SurveysDAO;


public class CustodyChildSupportMales {
	private SurveysDAO sd = new SurveysDAO();
	private OccupationsDAO od = new OccupationsDAO();
	private List<Survey> lstDivMalesWithChild = new ArrayList<>(); 
	private List<Survey> lstDivMalesReceiving = new ArrayList<>();
	private List<Survey> lstDivMalesPaying = new ArrayList<>();
	private Random randomGenerator = new Random(); 
	private int randomMale; 
	private float divMalesPayingRatio = .9f;
	int numberMalesPaying = 0;
	int numberMalesReceiving = 0;
	
	public List<Survey> doProcess(List<Survey> surveysList) {
		System.out.println("Entering CustodyChildSupportMales.doProcess() method.");
		// populate the various survey lists from main survey list
		//Can use any survey in surveysList to get Group ID (all grp id's are same)
		int grpID = surveysList.get(0).getId();
		clearLists();
		
		//set childSupport to '0' for all surveys in group 
		for (Survey survey : surveysList) { 
			survey.setChildSupport(0.0); 
			sd.update(survey); 
			} // end for loop 
		//Populate the "divorced with children" list men. 
		for (Survey survey : surveysList) { 
			if ( (survey.getMarried().equals("Divorced")) && (survey.getNumChild() > 0) ) { 
			// Get surveys for all divorced men with children in group 
			    if (survey.getGender().equals("Male")) { 
			    lstDivMalesWithChild.add(survey); 
			    }
			}
		}
		System.out.println(lstDivMalesWithChild.size()); 
		
		// Needed number of Divorced Males Paying child support (now 90%)
		numberMalesPaying = Math.round(lstDivMalesWithChild.size()*divMalesPayingRatio);
		// Needed number of Divorced Males Receiving child support (now 10%)
		numberMalesReceiving = lstDivMalesWithChild.size() - numberMalesPaying;
		
		System.out.println(numberMalesPaying + "," + numberMalesReceiving);
		
		//Populate List of Divorced Males Paying Child Support
		while(lstDivMalesPaying.size() < numberMalesPaying){
			randomMale = randomGenerator.nextInt(lstDivMalesWithChild.size());
			Survey survey = lstDivMalesWithChild.get(randomMale);
			lstDivMalesWithChild.remove(survey);
			lstDivMalesPaying.add(survey);
		}
		System.out.println(lstDivMalesPaying.size());
		
		//Populate List of Divorced Males Receiving Child Support
		while(lstDivMalesReceiving.size() < numberMalesReceiving){
		    randomMale = randomGenerator.nextInt(lstDivMalesWithChild.size());
			Survey survey = lstDivMalesWithChild.get(randomMale);
			lstDivMalesWithChild.remove(survey);
			lstDivMalesReceiving.add(survey);
		}
		System.out.println(lstDivMalesReceiving.size());
		
		// Set child support for Divorced Males Paying Support
		for (Survey survey : lstDivMalesPaying){
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
		
		// Set child support for Divorced Males Receiving Support
		for (Survey survey : lstDivMalesReceiving){
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
		lstDivMalesWithChild.clear();
		lstDivMalesReceiving.clear();
		lstDivMalesPaying.clear();
	}
//  ========================  MAIN METHOD  ==================== 
	 public static void main(String[] args) {
	    List<Survey> lstSurvey = new ArrayList<Survey>();
       //Create SurveysDAO & Survey Objs and Validate Login
       SurveysDAO sd = new SurveysDAO();
       lstSurvey = sd.search("groupID", "1");
       lstSurvey = new CustodyChildSupportMales().doProcess(lstSurvey); 
	} //end main()	
}