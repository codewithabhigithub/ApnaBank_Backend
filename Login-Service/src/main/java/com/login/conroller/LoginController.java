package com.login.conroller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.login.model.CustomerDetails;
import com.login.model.LoginClass;
import com.login.model.User;
import com.login.repository.UserRepository;
import com.login.service.ICustomerService;
import com.login.service.UserService;
import com.login.util.JwtUtil;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

	Logger logger=LogManager.getLogger(LoginController.class);
	int val;
	@Autowired
	public AuthenticationManager authenticationManager;
	@Autowired
	public JwtUtil jwtUtil;
	@Autowired
	private UserService service;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private UserRepository repository;
	
	User user=null;
	
	@CrossOrigin(origins = "*")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginClass loginClass) {
		System.out.println("IN LOGIN");
		SimpleMailMessage message = new SimpleMailMessage();
		if (authenticationManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(loginClass.getUsername(), loginClass.getPassword()))
				.isAuthenticated()) {
			if(loginClass.getUsername().endsWith(".com")) {
				user=service.findByEmail(loginClass.getUsername());
			}else {
			
			 user = service.findByMobileNumber(loginClass.getUsername());
			}
			  message.setFrom("apnabank.info@gmail.com");
			  message.setTo(user.getEmail()); String
			  msg="Dear Customer,\nWe hope this email finds you well. We are reaching out to inform you about a recent login activity on your Apna Bank account.\n If this login was authorized by you, kindly disregard this message.\r\n"
			  + "\r\n" +
			  "However, if you did not initiate this login. We strongly recommend taking immediate action to protect your account. Please Contact our Customer Support: +1245 987357 or visit bank manager immediately.\n "
			  + "\r\n"
			  +"Please note that Apna Bank will never request your personal or account information via email. If you receive any emails asking for sensitive information or account details, please report it to our Customer Support immediately.\r\n"
			  + "\r\n" +
			  "Thank you for your attention to this matter. We are committed to providing you with a secure banking experience and appreciate your cooperation in keeping your Apna Bank account safe.\r\n"
			  + "\r\n" + "Sincerely,\r\n" + "Apna Bank Customer Support";
			  message.setText(msg);
			  message.setSubject("Alert: Recent Login Activity on Your Apna Bank Account."
			  ); 
//			  mailSender.send(message);
//			  LocalDateTime currentDateTime = LocalDateTime.now();
//
//		        // Given date for comparison
//		        String givenDateTimeString = "2023-07-25 12:34:56"; // Replace this with your desired date and time
//		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		        LocalDateTime givenDateTime = LocalDateTime.parse(givenDateTimeString, formatter);
//
//		        // Compare the two date-times for equality
//		        if (currentDateTime.equals(givenDateTime)) {
//		            System.out.println("The current time is equal to the given date.");
//		        } else {
//		        	givenDateTime=currentDateTime;
//		        	user.setCoin(Integer.toString(Integer.parseInt(user.getCoin())+10));
//		            System.out.println("The current time is not equal to the given date.");
//		        }
			HashMap< String, String> map=new HashMap<>();
			String[] role=user.getRole().split(",");
			
			map.put("role", role[0]);
			map.put("jwt",jwtUtil.generateToken(user.getMobileNumber()) );
//			map.put("coin",user.getCoin());
//			System.out.println("out");
			logger.info("Access granted to user "+user.getMobileNumber());
//			System.out.println("data");
			 return new ResponseEntity<HashMap<String, String>>(map,HttpStatus.OK);
		} else {
			logger.error("Invalid Login, Data not matched");
			return new ResponseEntity<String>("invalid login",HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/forgot/{mobileNumber}/{password}")
	public ResponseEntity<?> forgot(@PathVariable("password") String pass,@PathVariable("mobileNumber") String mob) {
		if("updated"==customerService.forgotpassword(pass,mob))
		{
			User us=service.findByMobileNumber(mob);
			logger.info("Password Updated Successfully for user "+ us.getMobileNumber());
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("apnabank.info@gmail.com");
			message.setTo(us.getEmail());
			message.setText("Dear Customer,\n Your password updated Successfully");
			message.setSubject("Password Update");
			mailSender.send(message);
			return new ResponseEntity<String>("Password Updated Successfully", HttpStatus.OK);
		}
		logger.error("Invalid Update request");
		return new ResponseEntity<String>("Something went wrong! try again",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/sendmail/{msg}/{mail}/{subject}")
	public ResponseEntity<?> sendmail(@PathVariable("msg") String msg,@PathVariable("mail") String mail,@PathVariable("subject") String subject )
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apnabank.info@gmail.com");
		message.setTo(mail);
		message.setText(msg);
		message.setSubject(subject);
		mailSender.send(message);
		return new ResponseEntity<String>("Mail send", HttpStatus.OK);
	}
	
	@PostMapping("/otp/{mobile}")
	public ResponseEntity<?> otp(@PathVariable("mobile") String mobile)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		Random rand = new Random();
		 val = rand.nextInt(9000) + 1000;
		message.setFrom("apnabank.info@gmail.com");
		  message.setTo(repository.findByMobileNumber(mobile).getEmail()); String
		  msg="Dear Customer, Your One Time Password is "+val;
		  message.setText(msg);
		  message.setSubject("OTP message."
		  ); 
		  mailSender.send(message);
			logger.info("OTP send to the customer "+ mobile);

		  return new ResponseEntity<Integer>(val,HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody CustomerDetails customerDetails) {
		customerDetails.setAccountType("DIGITAL");
		CustomerDetails cs=customerService.saveCustomer(customerDetails);
		if(cs!=null)
		{
			
			logger.info(customerDetails.getMobileNumber()+" User successfully register ");
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("apnabank.info@gmail.com");
			message.setTo(customerDetails.getEmail());
			message.setText("Dear Customer,\n Your have Successfully register in apna bank"
					+ "\n We will update you one approved");
			message.setSubject("Customer Registeration");
			mailSender.send(message);
			return new ResponseEntity<CustomerDetails>(cs, HttpStatus.OK);
		}
		logger.error("Having some to save data of customer "+ customerDetails.getMobileNumber());

		return new ResponseEntity<String>("Having some to save data", HttpStatus.OK);
		
	}

}
