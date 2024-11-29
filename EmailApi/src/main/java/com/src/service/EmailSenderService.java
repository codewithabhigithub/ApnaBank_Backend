package com.src.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.src.model.EmailReq;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail( SimpleMailMessage message) {
//        = new SimpleMailMessage();
//        message.setFrom("apnabank.info@gmail.com");
//        message.setTo(request.getTo());
//        message.setText(request.getMessage());
//        message.setSubject(request.getSubject());
        mailSender.send(message);
        System.out.println("Mail Send...");


    }

    }
