package com.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.model.CustomerDetails;
import com.login.model.User;
import com.login.repository.CustomerDeatilsRepository;
import com.login.repository.UserRepository;


@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private CustomerDeatilsRepository customerDeatilsRepository;
	@Autowired
	private UserRepository ur;
	@Autowired
	private PasswordEncoder encode;
	
	SimpleMailMessage message = new SimpleMailMessage();
	
	
	CustomerDetails customerDetails=null;
	
	public CustomerDetails saveCustomer(CustomerDetails customer) {
		customerDetails = customerDeatilsRepository.findByAadharNumber(customer.getAadharNumber());
		if (customerDetails == null) {
			customer.setVerificationStatus(false);
			customerDeatilsRepository.save(customer);
			return customer;
		}else {
			return customerDetails;
		}
	}

	@Override
	public String forgotpassword(String pass, String mob) {
		User ur1=ur.findByMobileNumber(mob);
		ur1.setPassword(encode.encode(pass));
		ur.save(ur1);
		return "updated";
	}

	
}
