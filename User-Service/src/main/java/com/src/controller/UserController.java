package com.src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.exception.CustomerNotFoundException;
import com.src.model.CustomerDetails;
import com.src.model.User;
import com.src.service.ICustomerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/customer")
public class UserController {

	@Autowired
	private ICustomerService customerService;
	
	@GetMapping("/profile")
	public CustomerDetails showDetails(HttpServletRequest request) throws CustomerNotFoundException {
		String username=request.getAttribute("username").toString();
		return customerService.findCustumerDetailsByMobile(username);
	}
//	@PostMapping("/changePassword")
//	public String changePassword(HttpServletRequest request,@RequestBody String password) throws CustomerNotFoundException {
//		boolean flag=customerService.changePassword(request.getAttribute("username").toString(), password);
//		if(flag)
//			return "password changed success";
//		return "error to set password";
//	}
	
	
}
