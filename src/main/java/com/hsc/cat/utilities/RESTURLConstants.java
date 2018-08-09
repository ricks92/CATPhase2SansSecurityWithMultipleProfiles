package com.hsc.cat.utilities;

public class RESTURLConstants {

	public static final String BASE_URL="cat";
	public static final String REGISTER_USER=BASE_URL+"/employees";
	public static final String SKILLS=BASE_URL+"/skills";
	public static final String UPDATE_SKILL=BASE_URL+"/updateSkill";
	public static final String VIEW_SKILL=BASE_URL+"/viewSkill/{empId}/{viewHistory}";
	public static final String FETCH_ALL_EMPLOYEES=BASE_URL+"/employees";
	public static final String GET_MANAGER_DETAILS=BASE_URL+"/getManagerDetails";
	public static final String VERIFY_MANAGER=BASE_URL+"/verifyManager";
	public static final String VIEW_TEAM=BASE_URL+"/viewTeam/{id}";
	//public static final String GET_ALL_SELF_RATED_SKILLS=BASE_URL+"/getAllSelfRatedSkills/{empId}";
	public static final String VALIDATE_SECURITY_QUESTIONS=BASE_URL+"/validateSecurityQuestion";
	public static final String VALIDATE_USER=BASE_URL+"/validateUser";
	public static final String CHANGE_PASSWORD=BASE_URL+"/changePassword";
	public static final String FORGOT_PASSWORD=BASE_URL+"/forgetPassword";
	public static final String CREATE_PROFILE=BASE_URL+"/createProfile";
	public static final String VIEW_PROFILE=BASE_URL+"/profile/{empId}";
	public static final String UPDATE_PROFILE=BASE_URL+"/profile";
	public static final String FETCH_SKILLS_IN_A_CATEGORY=BASE_URL+"/fetchSkillsInACategory/{skillCategory}";   
	public static final String FETCH_SKILLS_IN_A_CATEGORY_SUBCATEGORY=BASE_URL+"/fetchSkillsInACategorySubcategory"; 
	public static final String GET_ALL_PEERS=BASE_URL+"/peers/{empId}"; 
	public static final String GET_ALL_SELF_RATED_SKILLS=BASE_URL+"/getAllSelfRatedSkills/{empId}/{sdlcCategoryNum}";
	public static final String GET_ALL_NOT_SELF_RATED_SKILLS=BASE_URL+"/getAllNotSelfRatedSkills/{empId}/{sdlcCategoryNum}";
	public static final String GET_ALL_PEER_REVIEWED_SKILLS=BASE_URL+"/getAllPeerReviewedSkills";
	public static final String FETCH_MAP3=BASE_URL+"/fetchMapNew3";
	public static final String SECURITY_QUESTIONS=BASE_URL+"/securityQuestions";
	public static final String PIECHART_FOR_COUNT_PER_LEVEL_PER_SKILL_PER_SDLC=BASE_URL+"/getPieChartForCountPerLevelPerSkillPerSDLC";
	public static final String PIECHART_FOR_COUNT_PER_LEVEL_PER_SKILL_COMBO=BASE_URL+"/getPieChartForCountPerLevelPerSkillCombo";
	public static final String FETCH_SKILLS_NOT_IN_A_CATEGORY=BASE_URL+"/fetchSkillsNotInACategory/{role}";
	public static final String RESOURCES_FOR_EACH_PROJECT_ROLE=BASE_URL+"/getResourcesForEachProjectRole";
	public static final String POST_FEEDBACK=BASE_URL+"/feedback";
	
}
