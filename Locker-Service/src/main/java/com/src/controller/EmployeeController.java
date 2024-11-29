package com.src.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.Locker;
import com.src.model.LockerApplication;
import com.src.model.LockerHistory;
import com.src.repository.LockerRepository;
import com.src.service.ILockerService;

@RestController
@RequestMapping("/lockerEmployee")
public class EmployeeController {

	@Autowired
	private ILockerService service;
	@GetMapping("/allApplied")
	public ResponseEntity<?> AllAppliedApplication() {
		return new ResponseEntity<List<LockerApplication>>(service.findAllAppliedApplication(),HttpStatus.OK);
	}
	@GetMapping("/assign/{id}")
	public ResponseEntity<?> assignLocker(@PathVariable("cif") long cif) {
		Locker locker=service.findLockerByCif(cif);
//		System.out.println(locker);
		
		if(locker==null) {
//			System.out.println("in if");
			LockerApplication application=service.findApplicationByCif(cif);
//			System.out.println(application);
			LockerHistory history=service.assignLocker(application);
			System.out.println(history);
			if(history==null) {
				return new ResponseEntity<String>("All lockers are occupied",HttpStatus.BAD_REQUEST);
			}else {
				service.payment(history.getCharges(), history.getCif());
				return new ResponseEntity<LockerHistory>(history,HttpStatus.OK);
			}
		}else {
			return new ResponseEntity<String>("Locker already assigned",HttpStatus.CONFLICT);
		}
			
	}
	
	@Autowired
	LockerRepository lockerRepository;
	@GetMapping("/create")
	public void createLocker(){
		
		for(int i=0;i<100;i++) {
			Locker locker=Locker.builder().status("empty").build();
			locker.setLockerNumber("lockernumber:"+(i+1));
		lockerRepository.save(locker);
		}
	}
		
	
	
	@GetMapping("/reject/{id}")
	public ResponseEntity<?> reject(@PathVariable("id") long id) {
		service.reject(id);
		return new ResponseEntity<String>("Deleted",HttpStatus.OK);
	}
}