package com.src.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.src.model.EmployeeDetails;
import com.src.model.User;
import com.src.model.updatePassword;
import com.src.service.IEmployeeService;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/admin")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
	
	Logger logger=LogManager.getLogger(AdminController.class);
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private JavaMailSender mailSender;

	public boolean sendmail(String msg,String mail,String subject )
	{
		System.out.println("mail send2");
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("apnabank.info@gmail.com");
		
		message.setFrom(mail);
		message.setText(msg);
		message.setSubject(subject);
		System.out.println(mailSender);
//		mailSender.send(message);
		System.out.println("mail send");
		return true;
	}
	
	@PostMapping("/register/admin") // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> saveAdmin(@RequestBody EmployeeDetails employee) {
		try {
			employeeService.saveAdmin(employee);
			logger.info(employee.getMobileNumber()+" Admin successfully register ");
			sendmail("Dear Admin,\n You have Successfully register in apna bank as ADMIN",employee.getEmail(),"Admin Registeration");
			
			return new ResponseEntity<String>("Admin added successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Having some issue to save the admin\n"+e.getMessage());
			return new ResponseEntity<String>("ERROR", HttpStatus.CONFLICT);
		}
	}
	@CrossOrigin(origins = "*")
	@PostMapping("/update/password")
	public ResponseEntity<?> updatepass(@RequestBody updatePassword upass) {
		
		return new ResponseEntity<String>("Password upadated",HttpStatus.OK);
	}

	@PostMapping("/register/manager")
	public ResponseEntity<?> saveManager(@RequestBody EmployeeDetails employee) {
		try {
			
			employeeService.saveManager(employee);
			logger.info(employee.getMobileNumber()+" Manager successfully registered by admin ");
			sendmail("Dear Manager,\n You have Successfully register in apna bank as MANAGER",employee.getEmail(),"Manager Registeration");
			
			return new ResponseEntity<String>("ADDED", HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Having some issue to save the Manager\n"+e.getMessage());

			return new ResponseEntity<String>("ERROR", HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/update/manager")
	public ResponseEntity<?> updateManager(@RequestBody EmployeeDetails employee) {
		try {
			employeeService.updateManager(employee);
			logger.info(employee.getMobileNumber()+" Manager successfully updated by admin ");
			sendmail("Dear Manager,\n Your data Successfully updated",employee.getEmail(),"Manager Updated");
			
			return new ResponseEntity<String>("UPDATED", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Having some issue to update the Manager\n"+e.getMessage());
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/update/admin")
	public ResponseEntity<?> updateAdmin(@RequestBody EmployeeDetails employee) {
		try {
			employeeService.updateAdmin(employee);
			logger.info(employee.getMobileNumber()+" Admin successfully updated");
			sendmail("Dear Admin,\n Your data Successfully updated",employee.getEmail(),"Admin Updated");
			
			return new ResponseEntity<String>("UPDATED", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Having some issue to update the Admin\n"+e.getMessage());
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/manager/{employeeId}")
	public ResponseEntity<?> deleteManager(@PathVariable("employeeId") long employeeId) {
		try {
			employeeService.deleteManager(employeeId);
			return new ResponseEntity<String>("DELETED", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/admin/{employeeId}")
	public ResponseEntity<?> deleteAdmin(@PathVariable("employeeId") long employeeId) {
		try {
			employeeService.deleteAdmin(employeeId);
			return new ResponseEntity<String>("DELETED", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/find/manager/{mobileNumber}")
	public ResponseEntity<?> getManagerUserDetailsByMobileNumber(@PathVariable("mobileNumber") String mobileNumber) {
		try {
			User user = employeeService.findUserByMobileNumber(mobileNumber);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/find/worker/{mobileNumber}")
	public ResponseEntity<?> getManagerEmployeeDetailsByEmployeeId(@PathVariable("mobileNumber") String mobileNumber) {
		try {
			EmployeeDetails employeeDetails = employeeService.findEmployeeByMobileNumber(mobileNumber);
			if(employeeDetails!=null)
			return new ResponseEntity<EmployeeDetails>(employeeDetails, HttpStatus.OK);
			else
				throw new Exception();
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/find/all")
	public ResponseEntity<?> findAllWorker() {
		try {
			List<EmployeeDetails> employeeDetails = employeeService.findAllEmployee();
		
			return new ResponseEntity<List<EmployeeDetails>>(employeeDetails, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/profile")
	public ResponseEntity<?> profile(HttpServletRequest request) {
		EmployeeDetails details=employeeService.findEmployeeByMobileNumber(request.getAttribute("username").toString());
			return new ResponseEntity<EmployeeDetails>(employeeService.findById(details.getEmployeeId()),HttpStatus.OK);
	}
	@PutMapping("/update/worker")
	public ResponseEntity<?> update(@RequestBody EmployeeDetails details) {
		try {
			logger.info(details.getMobileNumber()+" Admin successfully updated in database");
			SimpleMailMessage message = new SimpleMailMessage();
			
			message.setFrom("apnabank.info@gmail.com");
			
			message.setTo(details.getEmail());
			message.setText("Dear Worker,\n Your data Successfully updated");
			message.setSubject("Worker Updated");
//			System.out.println(mailSender);
			mailSender.send(message);
//			sendmail("Dear Worker,\n Your data Successfully updated",details.getEmail(),"Worker Updated");
			System.out.println("mail send2");
			return new ResponseEntity<EmployeeDetails>(employeeService.updateWorker(details),HttpStatus.OK);
		}catch (Exception e) {
//			logger.error("Having some issue to update the worker\n"+e.getMessage());
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
		
	}
}
