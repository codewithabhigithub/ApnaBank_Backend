package com.src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.EmailReq;
import com.src.service.EmailSenderService;

@RestController
@RequestMapping("/email")
public class EmailSenderController {

	@Autowired
	private EmailSenderService service;
	@GetMapping("/send")
	public void sendEmail(@RequestBody SimpleMailMessage message) {
		service.sendSimpleEmail(message);	
	}
	@GetMapping("/hello")
	public String name() {
		return "hello";
	}
}
