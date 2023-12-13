package com.talentstream.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentstream.entity.ContactDetails;
import com.talentstream.repository.ContactDetailsRepository;

@Service
public class ContactService {

	@Autowired
	private ContactDetailsRepository contactDetailsRepository;

	
	public void saveContactDetails(ContactDetails contactDetails) {
		// TODO Auto-generated method stub
		contactDetailsRepository.save(contactDetails);
	}

	
	public List<ContactDetails> getMessages() {
		// TODO Auto-generated method stub
		return contactDetailsRepository.findAll();
	}
}
