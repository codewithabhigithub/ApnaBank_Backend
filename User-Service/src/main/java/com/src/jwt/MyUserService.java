package com.src.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.src.model.User;
import com.src.service.CustomerServiceImpl;
@Component
public class MyUserService implements UserDetailsService {
    @Autowired
    CustomerServiceImpl service;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=service.findUserByMobileNumber(username);
		System.out.println(user);
		if(user==null) {
			throw new UsernameNotFoundException("not found");
		}else {
			return new MyUserDetails(user);
		}
	}

}
