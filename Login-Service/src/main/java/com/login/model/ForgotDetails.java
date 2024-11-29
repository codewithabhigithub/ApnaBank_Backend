package com.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForgotDetails {
	String username;
	String newpassword;
	String mobileNumber;
	String password;
	String confirmPassword;

}
