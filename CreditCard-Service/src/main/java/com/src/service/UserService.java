package com.src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.src.model.User;
import com.src.repository.UserRepository;
@Service
public class UserService implements IUserService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User findByMobileNumber(String mobieleNumber) {
		
		return userRepository.findByMobileNumber(mobieleNumber);
	}

}
