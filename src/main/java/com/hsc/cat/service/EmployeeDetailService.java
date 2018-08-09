package com.hsc.cat.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hsc.cat.TO.EmployeeTO;
import com.hsc.cat.TO.EmployeeUnderManagerDetails;
import com.hsc.cat.TO.ManagerDetails;
import com.hsc.cat.TO.ResponseTO;
import com.hsc.cat.TO.ViewTeamTO;
import com.hsc.cat.VO.EmployeeDetailsVO;
import com.hsc.cat.VO.UpdateSkillVO;
import com.hsc.cat.email.MailSender;
import com.hsc.cat.entity.EmployeeDetails;
import com.hsc.cat.entity.EmployeeSkillEntity;
import com.hsc.cat.entity.ProfileEntity;
import com.hsc.cat.entity.UserDetails;
import com.hsc.cat.enums.ApprovalStatusEnum;
import com.hsc.cat.enums.RoleCategoryEnum;
import com.hsc.cat.map.FetchMapService;
import com.hsc.cat.repository.EmployeeDetailRepository;
import com.hsc.cat.repository.EmployeeSkillRepository;
import com.hsc.cat.repository.ProfileRepository;
import com.hsc.cat.repository.UserRepository;
import com.hsc.cat.utilities.JSONOutputEnum;
import com.hsc.cat.utilities.Roles;



@Service
@Transactional
public class EmployeeDetailService {
	
	private static final Logger LOGGER = (Logger) LogManager.getLogger(EmployeeDetailService.class);

	@Autowired
	private EmployeeDetailRepository employeeDetailRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
    @Qualifier("javasampleapproachMailSender")
	public MailSender mailSender;
	
	public EmployeeTO save(EmployeeDetailsVO evo) {
		EmployeeTO employeeTO = new EmployeeTO();
		List<UserDetails> usersAreadyExist=userRepository.findByUsername(evo.getUsername());
		
		if(usersAreadyExist!=null && !usersAreadyExist.isEmpty() && usersAreadyExist.size()==2) {
			employeeTO.setIssue("Username already exists");
			
			 return employeeTO;
		}
		
		else if(usersAreadyExist!=null && !usersAreadyExist.isEmpty() && usersAreadyExist.size()==1 && !usersAreadyExist.get(0).getUsername().equalsIgnoreCase(evo.getUsername())) {
			employeeTO.setIssue("Username already exists");
			 return employeeTO;
		}
		
		//Case when user is already registered with a role and is registering again with the same role
		
		else if(usersAreadyExist!=null && !usersAreadyExist.isEmpty() && usersAreadyExist.size()==1 && usersAreadyExist.get(0).getRole().equalsIgnoreCase(evo.getRole())) {
			employeeTO.setIssue("Username:"+evo.getUsername()+" with " +evo.getRole()+" role already exists!!");
			LOGGER.debug("Username:"+evo.getUsername()+" with " +evo.getRole()+" role already exists!!");
			 return employeeTO;
		}
		
		
		
		/*
		 * Case when user is registered with a role and is registering again with a different role
		 * but firstname or lastname or email mismatch
		 */
		
		List<EmployeeDetails> employees=employeeDetailRepository.findByEmpid(evo.getUsername());
		
		if(employees!=null && employees.size()==2)
		{
			employeeTO.setIssue("Two accounts already exist");
			LOGGER.debug("Two accounts already exist");
			return employeeTO;
		}
		else if(employees!=null && employees.size()==1) {
				if(!employees.get(0).getFirstName().equalsIgnoreCase(evo.getFirstName())) {
				employeeTO.setIssue(evo.getUsername()+" registered earlier with a differnt firstname");
				LOGGER.debug(evo.getUsername()+" registered earlier with a differnt firstname");
				return employeeTO;
			}
			else if(!employees.get(0).getLastName().equalsIgnoreCase(evo.getLastName())) {
				employeeTO.setIssue(evo.getUsername()+" registered earlier with a differnt last name");
				LOGGER.debug(evo.getUsername()+" registered earlier with a differnt last name");
				return employeeTO;
			}
			
			else if(!employees.get(0).getEmail().equalsIgnoreCase(evo.getEmail())) {
				employeeTO.setIssue(evo.getUsername()+" registered earlier with a differnt email");
				LOGGER.debug(evo.getUsername()+" registered earlier with a differnt email");
				return employeeTO;
			}
		}
		
		
	     // Boolean userAlreadyExists=   employeeDetailRepository.exists(evo.getUsername());
	      
	    //  if(userAlreadyExists) return null;
		
		//boolean userAlreadyExists=   employeeDetailRepository.exists(evo.getUsername());
	      //employeeTO.setIssue("Username already exists");
	     // if(userAlreadyExists) return employeeTO;
	      
	      List<EmployeeDetails> emailAlreadyExists=employeeDetailRepository.getEmail(evo.getEmail());
	     
	     if(emailAlreadyExists!=null && !emailAlreadyExists.isEmpty() && emailAlreadyExists.size()==2)
	      { 
	   employeeTO.setIssue("Email already exists in two accounts");
	   LOGGER.debug("Email already exists in two accounts");
	   LOGGER.info("Email already exists in two accounts");
	   return employeeTO;
	      }
	     else if(emailAlreadyExists!=null && !emailAlreadyExists.isEmpty() && emailAlreadyExists.size()==1 && !emailAlreadyExists.get(0).getEmpid().equalsIgnoreCase(evo.getUsername())) {
	    	 employeeTO.setIssue("Email already exists with user:"+emailAlreadyExists.get(0).getEmpid()+" Please rectify:"+evo.getUsername());
	    	 LOGGER.info("Email already exists with user:"+emailAlreadyExists.get(0).getEmpid()+" Please rectify:"+evo.getUsername());
	  	   return employeeTO;
	     }
	      
		EmployeeDetails emp= new EmployeeDetails();
		UserDetails user = new UserDetails();
		ProfileEntity profileEntity=new ProfileEntity();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
			Date d1 = new Date();
			Date d2 = new Date();
			emp.setCreationDate(d1);
			System.out.println(emp.getCreationDate());
			emp.setUpdationDate(d2);
			
			
		
		
		//Setting users
		user.setUsername(evo.getUsername());
		user.setPassword(evo.getPassword());
		user.setRole(evo.getRole());
		
		//Setting EmployeeDetails
		EmployeeDetails saved=null;
		
		
		//emp.setEmpid(evo.getUsername());
		emp.setFirstName(evo.getFirstName());
		emp.setLastName(evo.getLastName());
		emp.setDepartment(evo.getDepartment());
		emp.setSecurityQues1(evo.getSecurityQues1());
		emp.setSecurityQues2(evo.getSecurityQues2());
		emp.setSecurityAns1(evo.getSecurityAns1());
		emp.setSecurityAns2(evo.getSecurityAns2());
		emp.setManagerId(evo.getManagerId());
		emp.setEmail(evo.getEmail());
		emp.setEmpid(evo.getUsername());
		
		
		profileEntity.setFirstname(emp.getFirstName());
		profileEntity.setLastname(emp.getLastName());
		profileEntity.setEmail(emp.getEmail());
		profileEntity.setEmpId(emp.getEmpid());
		profileEntity.setCreationDate(d1);
		
		
		if(evo.getRole().equals(Roles.MANAGER)){ //That is it is a manager
			emp.setApprovalStatus(ApprovalStatusEnum.PENDING.getStatus());
			
			LOGGER.info("Request came to register manager with employee id:"+evo.getUsername());
			LOGGER.debug("Request came to register manager with employee id:"+evo.getUsername());
			
			String from = "catuser1234@gmail.com";
			String to = "knwnobounds@gmail.com";
			String subject = "Request came to register manager!";
			String body = "Request came to register manager with employee id:"+evo.getUsername()+"\nDetails: \nEmployee id: "+evo.getUsername()+"\nFirst Name: "+evo.getFirstName()+"\nLast Name: "+evo.getLastName()+"\nEmail: "+evo.getEmail()+"\nPlease verify: 'http://localhost:8030/verifyManager/'"+evo.getUsername();
			
			mailSender.sendMail(from, to, subject, body); //send email
			  //user.setEmployeeDetails(emp);  //save manager---------
			  emp.setUserDetails(user);
			 userRepository.save(user);
			 //emp.setProfileEntity(profileEntity);
			 saved = employeeDetailRepository.save(emp);
			 
			 LOGGER.info("Manager with employee id:"+evo.getUsername()+ " saved successfully");
			 LOGGER.debug("Manager with employee id:"+evo.getUsername()+ " saved successfully");
			/*ProfileEntity savedProfile=profileRepository.save(profileEntity);
			emp.setProfileEntity(savedProfile);*/
		}
		
//		else{
//			user.setEmployeeDetails(emp);
//			 userRepository.save(user);
//			 saved = employeeDetailRepository.save(emp);
//			emp.setApprovalStatus(ApprovalStatusEnum.NA.getStatus());
//		}
		
		if(evo.getRole().equals(Roles.EMPLOYEE) ) {  //Check if it is a valid manager id
//			if(!employeeDetailRepository.exists(emp.getManagerId())) {
//				System.out.println("Manger details incorrect");
//				//do nothing
//				employeeTO.setIssue("Manger details incorrect");
//			}
			//else if(employeeDetailRepository.exists(emp.getManagerId())) {
				emp.setApprovalStatus(ApprovalStatusEnum.NA.getStatus());
//				/user.setEmployeeDetails(emp);
				 emp.setUserDetails(user);
				 userRepository.save(user);
				 emp.setProfileEntity(profileEntity);
				 saved = employeeDetailRepository.save(emp);
				/* ProfileEntity savedProfile=profileRepository.save(profileEntity);
					emp.setProfileEntity(savedProfile);*/
				 /*user.setEmployeeDetails(emp);
				 userRepository.save(user);
				 saved = employeeDetailRepository.save(emp);
				*/
				//}
		}
//		else {
//		 user.setEmployeeDetails(emp);
//		 userRepository.save(user);
//		 saved = employeeDetailRepository.save(emp);
//		
//		}
//	
		 if (saved != null)
				employeeTO = modelConversion(saved);
		
		return employeeTO;
	}
	
	
	
	
	public List<EmployeeTO> getAllEmployees(){
		List<EmployeeDetails> employees= employeeDetailRepository.findAllEmployees();
		
		
		List<EmployeeTO> employeeTOList = new ArrayList<>();
		for(EmployeeDetails emp:employees) {
			EmployeeTO employeeTO=modelConversion(emp);
			employeeTOList.add(employeeTO);
		}
		
		return employeeTOList;
	}
	
	@Scheduled(cron="0 0 0 * * ?")
	public ResponseTO deleteManagers() {
		List<EmployeeDetails> employeesToDelete=employeeDetailRepository.findAllManagersToDelete();
		ResponseTO response = new ResponseTO();
		if(employeesToDelete.isEmpty()) {
			response.setResponseCode(String.valueOf(JSONOutputEnum.SUCCESS.getValue()));
			response.setResponseMessage("No manager is there to purge");
		}
		
		else {
			for(EmployeeDetails e:employeesToDelete) {
				String email=e.getEmail();
				String from = "catuser1234@gmail.com";
				String to = email;
				String subject = "Your request has been rejected!";
				String body =  "Your request has been rejected!";
				
				mailSender.sendMail(from, to, subject, body); //send email
				
				employeeDetailRepository.delete(e);
			}
		}
		
		return response;
	}
	
	
	
	
	public List<ManagerDetails> getAllManager() {

		List<ManagerDetails> managerDetailsList = new ArrayList<ManagerDetails>();
		List<EmployeeDetails> employeeList = employeeDetailRepository.findAllManagers();
		for (EmployeeDetails employeeDetails : employeeList) {
			ManagerDetails managerDetails = new ManagerDetails();
			managerDetails.setEmpId(employeeDetails.getEmpid());
			managerDetails.setFirstName(employeeDetails.getFirstName());
			managerDetails.setLastName(employeeDetails.getLastName());
			managerDetails.setEmailId(employeeDetails.getEmail());
			managerDetailsList.add(managerDetails);
		}

		return managerDetailsList;
	}
	
	
	
	public boolean updateApprovalStatus(String empId) {
		boolean result = Boolean.FALSE;
		int updatdRow = employeeDetailRepository.updateManagersApprovalStatus(empId);
		if (updatdRow > 0) {
			result = Boolean.TRUE;
		}

		return result;
	}

	
	
	public ViewTeamTO getEmployeeUnderManager(String managerId) {

		ViewTeamTO viewTeamTO = new ViewTeamTO();
		if (null != managerId) {
			List<EmployeeDetails> employeeDetailsList = employeeDetailRepository.findEmployeeUnderManager(managerId);
			if (employeeDetailsList != null) {
				List<EmployeeUnderManagerDetails> listOfEmployee = new ArrayList<EmployeeUnderManagerDetails>();
				for (EmployeeDetails employeeDetails : employeeDetailsList) {
					EmployeeUnderManagerDetails employeeUnderManagerDetails = new EmployeeUnderManagerDetails();
					employeeUnderManagerDetails.setEmpId(employeeDetails.getEmpid());
					employeeUnderManagerDetails.setFirstName(employeeDetails.getFirstName());
					employeeUnderManagerDetails.setLastName(employeeDetails.getLastName());
					employeeUnderManagerDetails.setDepartment(employeeDetails.getDepartment());
					employeeUnderManagerDetails.setEmailId(employeeDetails.getEmail());
					listOfEmployee.add(employeeUnderManagerDetails);
				}
				if (listOfEmployee != null) {
					viewTeamTO.setListOfEmployee(listOfEmployee);
					viewTeamTO.setResponseCode("1");
					viewTeamTO.setResponseMessage("SUCCESS");
				} /*else {
					viewTeamTO.setResponseCode("8");
					viewTeamTO.setResponseMessage("No employee exists against this managerId");
				}*/
			} else {
				viewTeamTO.setResponseCode("8");
				viewTeamTO.setResponseMessage("No employee exists against this managerId");

			}
		} else {
			viewTeamTO.setResponseCode("5");
			viewTeamTO.setResponseMessage("Invalid Parameter");
		}

		return viewTeamTO;

	}
	
	
	public List<EmployeeTO> getAllPeers(String empId){
		
		List<EmployeeDetails> employeesWithGivenId=employeeDetailRepository.findByEmpid(empId);
		
		if(employeesWithGivenId==null || employeesWithGivenId.isEmpty()) {
			return null;
		}
		List<EmployeeTO> employeeTOList = new ArrayList<>();
		List<EmployeeDetails> employeeList = employeeDetailRepository.getAllPeers(empId);
		
		for(int i=0;i<employeeList.size();i++) {
			EmployeeDetails employeeDetails=employeeList.get(i);
			UserDetails user=userRepository.findUserByUsernameAndRole(empId, Roles.EMPLOYEE);
			//We don't want managers as peers
			if(!(user.getRole().equalsIgnoreCase("ROLE_EMPLOYEE"))){
				continue;
			}
			EmployeeTO employeeTO=modelConversion(employeeDetails);
			employeeTOList.add(employeeTO);
		}
		
		return employeeTOList;
	}
	
	
	public EmployeeTO modelConversion(EmployeeDetails e){
		EmployeeTO employeeTO = new EmployeeTO();
		employeeTO.setId(e.getId());
		employeeTO.setEmpid(e.getEmpid());
		employeeTO.setFirstName(e.getFirstName());
		employeeTO.setLastName(e.getLastName());
		employeeTO.setDepartment(e.getDepartment());
		employeeTO.setCreationDate(e.getCreationDate());
		employeeTO.setUpdationDate(e.getUpdationDate());
		employeeTO.setSecurityQues1(e.getSecurityQues1());
		employeeTO.setSecurityQues2(e.getSecurityQues2());
		employeeTO.setSecurityAns1(e.getSecurityAns1());
		employeeTO.setSecurityAns2(e.getSecurityAns2());
		employeeTO.setManagerId(e.getManagerId());
		employeeTO.setApprovalStatus(e.getApprovalStatus());
		employeeTO.setEmail(e.getEmail());
		return employeeTO;
	}
	
	
	
	
}
