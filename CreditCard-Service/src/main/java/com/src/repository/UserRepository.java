package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

	public User findByMobileNumber(String mobileNumber);
	
}
