package com.src.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.Locker;
import com.src.service.ILockerService;

@RestController
@RequestMapping("/lockerAdmin")
public class AdminController {

	@Autowired
	private ILockerService service;
	@GetMapping("/hello")
	public ResponseEntity<String> hello(){
		return new ResponseEntity<String>("Hello",HttpStatus.OK);
	}
	@PostMapping("/create/{count}")
	public ResponseEntity<?> createLockers(@PathVariable("count") int count) {
		return new ResponseEntity<List<Locker>>(service.createLockers(count),HttpStatus.OK);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteLocker(@PathVariable("id") long id) {
		service.deleteLocker(id);
		return new ResponseEntity<String>("DELETED",HttpStatus.OK);
	}
}
