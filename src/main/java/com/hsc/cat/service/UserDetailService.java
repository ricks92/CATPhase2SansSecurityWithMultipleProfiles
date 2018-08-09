package com.hsc.cat.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hsc.cat.TO.ResponseTO;
import com.hsc.cat.VO.ChangePasswordVO;
import com.hsc.cat.VO.ForgetPasswordVO;
import com.hsc.cat.entity.EmployeeDetails;
import com.hsc.cat.entity.UserDetails;
import com.hsc.cat.enums.ApprovalStatusEnum;
import com.hsc.cat.repository.EmployeeDetailRepository;
import com.hsc.cat.repository.UserRepository;
import com.hsc.cat.utilities.Roles;

@Service
@Transactional
public class UserDetailService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmployeeDetailRepository employeeDetailRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
//	
//	public UserTO registerUser(UserDetails userDetails){
//		
//	UserDetails u=	userRepository.save(userDetails);
//		UserTO userTO = modelConversion(u);
//		return userTO;
//	}
//	
//	
//	public UserTO modelConversion(UserDetails userDetails){
//		UserTO userTO = new UserTO();
//		userTO.setUsername(userDetails.getUsername());
//		userTO.setPassword(userDetails.getPassword());
//		userTO.setRole(userDetails.getRole());
//		
//		return userTO;
//	}
	
	/*public boolean validateSecurityQuestion(ValidateSecurityQuestionVO validateSecurityQuestionVO) {
		UserDetails user = userRepository.findOne(validateSecurityQuestionVO.getEmpId());
		EmployeeDetails emp=user.getEmployeeDetails();
		if(emp!=null && (emp.getSecurityQues1().equals(validateSecurityQuestionVO.getSecurityQuestion()) && emp.getSecurityAns1().equals(validateSecurityQuestionVO.getAnswer() ) ) ||  (emp.getSecurityQues2().equals(validateSecurityQuestionVO.getSecurityQuestion() )&& emp.getSecurityAns2().equals(validateSecurityQuestionVO.getAnswer()  )  ) ) 
				
		{
			return true;
		}
		
		return false;
	}
	*/
	
	public ResponseTO checkValidUser(String userName, String password, String role) {
		boolean userAvailable = Boolean.FALSE;
		boolean userPassword = Boolean.FALSE;

		ResponseTO responseTO = new ResponseTO();
		/*responseTO.setResponseCode("0");
		responseTO.setResponseMessage("FAILURE");
		List<UserDetails> userDetailsList = userRepository.findAll();
		Iterator itr = userDetailsList.iterator();
		while (itr.hasNext()) {
			UserDetails user = (UserDetails) itr.next();
			if (user.getUsername().equalsIgnoreCase(userName)) {
				userAvailable = Boolean.TRUE;
				if (user.getPassword().equals(password)) {
					userPassword = Boolean.TRUE;
					if (user.getRole().equalsIgnoreCase(role)) {
						responseTO.setResponseCode("1");
						responseTO.setResponseMessage("SUCCESS");
						EmployeeDetails emp=employeeDetailRepository.findOne(user.getUsername());
						responseTO.setFirstName(emp.getFirstName());
						responseTO.setLastName(emp.getLastName());
						responseTO.setRole(user.getRole());
						responseTO.setUserName(user.getUsername());
						responseTO.setDepartment(emp.getDepartment());
						responseTO.setEmailId(emp.getEmail());
						break;
					} else {

						responseTO.setResponseCode("3");
						responseTO.setResponseMessage("Incorrect Role");
						EmployeeDetails emp=employeeDetailRepository.findOne(user.getUsername());
						//responseTO.setFirstName(emp.getFirstName());
						//responseTO.setLastName(emp.getLastName());
						responseTO.setRole(user.getRole());
						//responseTO.setUserName(user.getUsername());
						break;
					}
				}

			}
		}
		if (userAvailable && userPassword == false) {
			responseTO.setResponseCode("2");
			responseTO.setResponseMessage("Password Incorrect");
			

		}*/
		
		UserDetails user=userRepository.findUserByUsernameAndRole(userName,role);
		if(user==null) {
			responseTO.setResponseCode("0");
			responseTO.setResponseMessage("FAILURE");
			return responseTO;
		}
		if(!(user.getPassword().equals(password))) {
			responseTO.setResponseCode("2");
			responseTO.setResponseMessage("Password Incorrect");
			return responseTO;
		}
		
		if(!(user.getRole().equalsIgnoreCase(role))) {
			responseTO.setResponseCode("3");
			responseTO.setResponseMessage("Incorrect Role");
			return responseTO;
		}
		
		if(user.getUsername().equalsIgnoreCase(userName) && user.getPassword().equals(password) && user.getRole().equalsIgnoreCase(role)) {
			responseTO.setResponseCode("1");
			responseTO.setResponseMessage("SUCCESS");
			//EmployeeDetails emp=employeeDetailRepository.findOne(user.getUsername());
			Query query=entityManager.createQuery("select e from EmployeeDetails e where e.empid=:empid");
			
			query.setParameter("empid", userName);
			
			List<EmployeeDetails> empList=(List<EmployeeDetails>)query.getResultList();
			EmployeeDetails emp=null;
			
			for(EmployeeDetails e:empList) {
				if(user.getRole().equalsIgnoreCase(Roles.EMPLOYEE) && e.getApprovalStatus().equalsIgnoreCase(ApprovalStatusEnum.NA.getStatus())) {
					 emp=e;
					 break;
				}
				else if(user.getRole().equalsIgnoreCase(Roles.MANAGER) && e.getApprovalStatus().equalsIgnoreCase(ApprovalStatusEnum.PENDING.getStatus())) {
					emp=e;
					break;
				}
			}
			
			responseTO.setFirstName(emp.getFirstName());
			responseTO.setLastName(emp.getLastName());
			responseTO.setRole(user.getRole());
			responseTO.setUserName(user.getUsername());
			responseTO.setEmailId(emp.getEmail());
		}
		return responseTO;
	}
	
	
public ResponseTO updatePassword(ChangePasswordVO changePasswordVO) {
		
		int updatdRow=0;
		ResponseTO responseTO=new ResponseTO();
		String newPassword=changePasswordVO.getPassword();
		String userName=changePasswordVO.getUserName();
		if(null!=newPassword && null!=userName && !newPassword.isEmpty() && !userName.isEmpty()) {
		 updatdRow =userRepository.updatePasswordInDB(userName,newPassword);
		 if(updatdRow>0) {
			 responseTO.setResponseCode("1");
			 responseTO.setResponseMessage("SUCCESS");
		 }else {
			
				 responseTO.setResponseCode("0");
				 responseTO.setResponseMessage("FAILURE");
			
		 }
			
		}else {
			responseTO.setResponseCode("5");
			responseTO.setResponseMessage("Invalid Parameter");
		}
		
		return responseTO;
	}
	
	

public ResponseTO forgetPasswordForAll(ForgetPasswordVO forgetPasswordVO) {
	int updatdRow=0;
	ResponseTO responseTO=new ResponseTO();
	 String empId=forgetPasswordVO.getEmpId();
	 String securityQuestion=forgetPasswordVO.getSecurityQuestion();
	 String answer=forgetPasswordVO.getAnswer();
	 String role=forgetPasswordVO.getRole();
	 
	 if(validateParameter(empId) && validateParameter(securityQuestion)  && validateParameter(answer) ) {
		 
		 UserDetails user=userRepository.findUserByUsernameAndRole(empId, role);
		 EmployeeDetails employeeDetails=employeeDetailRepository.findByUsernameAndRole(empId, role);
		 if(null!=employeeDetails) {
			 if((employeeDetails.getSecurityQues1().equalsIgnoreCase(securityQuestion) && employeeDetails.getSecurityAns1().equals(answer)) ||
					 (employeeDetails.getSecurityQues1().equalsIgnoreCase(securityQuestion) && employeeDetails.getSecurityAns1().equals(answer)) ) {
				 responseTO.setResponseCode("1");
				 responseTO.setResponseMessage("SUCCESS");
			 }else {
				 responseTO.setResponseCode("0");
				 responseTO.setResponseMessage("FAILURE");
			 }
		 }else{
				responseTO.setResponseCode("9");
				responseTO.setResponseMessage("No such employee exist");
			 
		 }
	 }else {
			responseTO.setResponseCode("5");
			responseTO.setResponseMessage("Invalid Parameter");
	 }

	return responseTO;
}




private boolean validateParameter(String parameter) {
	boolean result=Boolean.FALSE;
	
	if(null!=parameter && !parameter.isEmpty()) {
		result=Boolean.TRUE;
	}
	
	return result;
	
}

}
