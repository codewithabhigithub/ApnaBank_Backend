package com.src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.Locker;
import com.src.model.LockerApplication;
import com.src.service.ILockerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("lockerCustomer")
public class CustomerController {

	@Autowired
	private ILockerService service;
	
	@GetMapping("/apply/{duration}")
	public ResponseEntity<?> apply(@PathVariable("duration") int duration, HttpServletRequest request) {
		long cif=service.findCifByMobileNumber(request.getAttribute("username").toString());
		//Locker locker=service.findLockerByCif(service.findCifByMobileNumber(cif));
		Locker locker=service.findLockerByCif(cif);
		if(locker!=null || duration>15)
			return new ResponseEntity<String>("Already locker assigned or Incorrect duration",HttpStatus.BAD_REQUEST);
		LockerApplication application=service.apply(duration,cif);
		if(application!=null)
		return new ResponseEntity<LockerApplication>(application,HttpStatus.OK);
		else return new ResponseEntity<String>("Already Applied",HttpStatus.OK);
	}
//	@PutMapping("/payment")
//	public ResponseEntity<?> payment(HttpServletRequest request) {
//		long cif=service.findCifByMobileNumber(request.getAttribute("username").toString());
//		Locker locker=service.findLockerByCif(cif);
//		//System.out.println(cif+" "+locker);
//		if(locker==null) {
//			LockerApplication application=service.payment(service.findApplicationByCif(cif).getCharges(), cif);
//			if(application!=null) {
//				return new ResponseEntity<LockerApplication>(application,HttpStatus.OK);
//			}else {
//				return new ResponseEntity<String>("insufficient fund",HttpStatus.BAD_REQUEST);
//			}
//		}else {
//			return new ResponseEntity<String>("locker already assigned",HttpStatus.BAD_REQUEST);
//		}
//	}
	@GetMapping("/status")
	public ResponseEntity<LockerApplication> getApplicationStatus(HttpServletRequest request) {
		long cif=service.findCifByMobileNumber(request.getAttribute("username").toString());
		return new ResponseEntity<LockerApplication>(service.status(cif),HttpStatus.OK);
	}
}
