//package com.src.service;
//
//import java.util.Properties;
//
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import org.apache.commons.mail.DefaultAuthenticator;
//import org.apache.commons.mail.Email;
//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.SimpleEmail;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//	public boolean sendEmail1(String subject, String msg, String to) {
//		boolean b=false;
//		String host="smtp.gmail.com";
//		String from="----sender mail------";
//		Properties  properties=System.getProperties();
//		properties.put("mail.smtp.host", host);
//		properties.put("mail.smtp.port", "465");
//		properties.put("mail.smtp.ssl.enable", "true");
//		properties.put("mail.smtp.auth", "true");
//
//		Session session=Session.getInstance(properties,new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(from, "-----password----");
//			}
//		
//		});
//		session.setDebug(true);
//		
//		MimeMessage m =new MimeMessage(session);
//		try {
//			m.setFrom(from);
//			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//			m.setSubject(subject);
//			m.setText(msg);
//			Transport.send(m);
//			System.out.println("MAIL SENT....");
//			b= true;
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			b=false; 
//		}
//		return b;
//		
//	}
//	public boolean sendEmail2(String subject, String msg, String to) {
//	        // Create the email object
//	        Email email = new SimpleEmail();
//			String from="rajeshmorey073@gmail.com";
//
//	        // Set the email host, port, and authentication
//	        email.setHostName("smtp.gmail.com");
//	        email.setSmtpPort(465);
//	        email.setAuthenticator(new DefaultAuthenticator(from, "qhkafntzeonsbatn"));
//
//	        // Enable SSL/TLS for secure connection
//	        email.setSSLOnConnect(true);
//
//	        try {
//	            // Set the email details
//	            email.setFrom(from);
//	            email.setSubject(subject);
//	            email.setMsg(msg);
//	            email.addTo(to);
//
//	            // Send the email
//	            email.send();
//	            System.out.println("Email sent successfully!");
//	            return true;
//	        } catch (EmailException e) {
//	            e.printStackTrace();
//	            return false;
//	        }
//	    }
//}
