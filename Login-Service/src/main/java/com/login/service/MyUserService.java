package com.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.login.model.MyUserDetails;
import com.login.model.User;
import com.login.repository.UserRepository;
 @Service
public class MyUserService implements UserDetailsService{

	 @Autowired
	private UserRepository repository;
	 User user=null;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		if(username.endsWith(".com")) {
			user=repository.findByEmail(username);
		}else {
		
		 user = repository.findByMobileNumber(username);
		}
		System.out.println(user);
		
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		else {
			
			return new MyUserDetails(user);
		}
		
	}
}
