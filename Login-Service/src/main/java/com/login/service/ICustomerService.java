package com.login.service;

import java.util.List;

import com.login.model.CustomerDetails;
import com.login.model.ForgotDetails;
import com.login.model.User;


public interface ICustomerService {
	
	public CustomerDetails saveCustomer(CustomerDetails customer);
	public String forgotpassword(String pass, String mob);

	


	
}
