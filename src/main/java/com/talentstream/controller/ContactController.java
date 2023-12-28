package com.talentstream.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
 
import com.talentstream.entity.ContactDetails;
import com.talentstream.service.ContactService;
 
@RestController
public class ContactController {
 
	@Autowired
	private ContactService contactService;
	@PostMapping("/send-message")
	public ResponseEntity<String> sendMessage(@RequestBody ContactDetails contactDetails){
		contactService.saveContactDetails(contactDetails);
		return ResponseEntity.ok("Message sent successfully");
	}
	@GetMapping("/get-messages")
	public ResponseEntity<List<ContactDetails>> getMessages(){
		List<ContactDetails> contactDetails=contactService.getMessages();
		return ResponseEntity.ok(contactDetails);
	}
}
