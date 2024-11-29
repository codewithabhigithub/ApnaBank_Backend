package com.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	public User findByMobileNumber(String mobileNumber);

	public User findByEmail(String email);
}
