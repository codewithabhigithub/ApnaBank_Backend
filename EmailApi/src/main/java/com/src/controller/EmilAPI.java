//package com.src.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.src.model.EmailReq;
//import com.src.service.EmailService;
//
//@RestController
//public class EmilAPI {
//
//	@Autowired
//	private EmailService service;
//	@GetMapping("/welcome")
//	public String name() {
//		return "WELCOME........";
//	}
//	@PostMapping("/mail")
//	public ResponseEntity<?> send(@RequestBody EmailReq request) {
//		System.out.println(request);
//		//boolean b= service.sendEmail1(request.getSubject(), request.getMessage(), request.getTo());
//		boolean b= service.sendEmail2(request.getSubject(), request.getMessage(), request.getTo());
//		if(b)
//		return ResponseEntity.ok("Done");
//		return new ResponseEntity<String>("not send",HttpStatus.BAD_REQUEST);
//				
//
//	}
//}
