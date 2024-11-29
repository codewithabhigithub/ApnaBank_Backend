package com.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.model.User;
import com.login.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	public User findByMobileNumber(String mobileNumber) {
		
		return repository.findByMobileNumber(mobileNumber);
	}
	public User findByEmail(String email) {
		
		return repository.findByEmail(email);
	}

}
