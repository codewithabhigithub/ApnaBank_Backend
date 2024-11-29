package com.src.controller;

import java.util.List;

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
import com.src.model.LockerHistory;
import com.src.service.ILockerService;

@RestController
@RequestMapping("/lockerManager")
public class ManagerController {

	@Autowired
	private ILockerService service;
	@GetMapping("/allEmpty")
	public ResponseEntity<List<Locker>> allEmptyLockers() {
		return new ResponseEntity<List<Locker>>(service.findAllEmptyLockers(),HttpStatus.OK);
	}
	@GetMapping("/allAssigned")
	public ResponseEntity<List<Locker>> allAssignedLockers() {
		return new ResponseEntity<List<Locker>>(service.findAllAssignedLockers(),HttpStatus.OK);
	}
	@GetMapping("/allVerified")
	public ResponseEntity<?> allVerifiedApplications() {
		return new ResponseEntity<List<LockerApplication>>(service.findAllVerifiedApplication(),HttpStatus.OK);
	}
	
//	@PostMapping("/assign/{cif}/{lockerId}")
//	public ResponseEntity<?> assignLocker(@PathVariable("cif") long cif,@PathVariable("lockerId") long lockerId) {
//		LockerApplication application=service.findApplicationByCif(cif);
//		if(application!=null) {
//			LockerHistory lockerHistory=service.assignLocker(application, lockerId);
//			if(lockerHistory!=null)
//				return new ResponseEntity<LockerHistory>(lockerHistory,HttpStatus.OK);
//			else
//				return new ResponseEntity<String>("check again payment,locker availability ",HttpStatus.BAD_REQUEST);
//		}else
//			return new ResponseEntity<String>("application not found",HttpStatus.BAD_REQUEST);
//	}
	@PutMapping("/remove/{lockerId}/{cif}")
	public ResponseEntity<?> removeLocker(@PathVariable("lockerId") long lockerId,@PathVariable("cif") long cif) {
		Locker locker=service.removeLocker(lockerId, cif);
		if(locker!=null)
			return new ResponseEntity<Locker>(locker,HttpStatus.OK);
		else
			return new ResponseEntity<String>("Locker not assigned",HttpStatus.NOT_FOUND);
	}
	
}
