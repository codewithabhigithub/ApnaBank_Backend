package com.src.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.GiftCard;
import com.src.model.GiftCardsOffer;
import com.src.service.IAccountService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/giftCard")
public class GiftCardController {
	@Autowired
	private IAccountService service;
	@GetMapping("/hello")
	public String  hello() {
		return "Hello";
	}
	
	@PostMapping("/create/{accountNumber}/{discount}")
	public ResponseEntity<?> createGiftCard(@RequestBody GiftCard card,@PathVariable("accountNumber") long accountNumber,@PathVariable("discount") double discount) {
		GiftCard giftCard=service.createGiftCard(card, accountNumber,discount);
		if(giftCard!=null) {
			System.out.println("mohit giftcard");
			return new ResponseEntity<GiftCard>(giftCard,HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Error",HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping("/redeem/{id}")
	public ResponseEntity<?> redeemGiftCard(@PathVariable("id") long id) {
		GiftCard giftCard=service.redeemGiftCard(id);
		if(giftCard!=null) {
			return new ResponseEntity<GiftCard>(giftCard,HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("Already redeemed",HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping("find/giftcards")
	public ResponseEntity<?> findAllGiftCard(HttpServletRequest request) {	
		return new ResponseEntity<List<GiftCard>>(service.findAllGiftCard(request.getAttribute("username").toString()),HttpStatus.OK);
	}
	
	@GetMapping("/findAll/giftCardsOffer")
	public ResponseEntity<?> findAllOffer() {
		return new ResponseEntity<List<GiftCardsOffer>>(service.findAllGiftCardsOffer(),HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> creategiftcardoffer(@RequestBody GiftCardsOffer giftcardsoffer)
	{
		service.createGiftCardsOffer(giftcardsoffer);
		return new ResponseEntity<String>("Created", HttpStatus.OK);
	}
}
