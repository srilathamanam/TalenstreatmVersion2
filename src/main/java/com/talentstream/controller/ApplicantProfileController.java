package com.talentstream.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.talentstream.dto.ApplicantProfileDTO;
import com.talentstream.exception.CustomException;
import com.talentstream.service.ApplicantProfileService;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin("*")
@RestController
@RequestMapping("/applicantprofile")
public class ApplicantProfileController {
	private final ApplicantProfileService applicantProfileService;
	
	  private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    @Autowired
    public ApplicantProfileController(ApplicantProfileService applicantProfileService) {
        this.applicantProfileService = applicantProfileService;
		
    }

    @PostMapping("/createprofile/{applicantid}")
    public ResponseEntity<String> createOrUpdateApplicantProfile(@PathVariable long applicantid, @RequestBody ApplicantProfileDTO applicantProfileDTO) throws IOException {
        try {
            String result = applicantProfileService.createOrUpdateApplicantProfile(applicantid, applicantProfileDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (CustomException e) {
        	logger.error("INTERNAL_SERVER_ERROR");
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
   @GetMapping("/getdetails/{applicantid}")
   public ResponseEntity<ApplicantProfileDTO> getApplicantProfileById(@PathVariable long applicantid) {
	   try {
		   ApplicantProfileDTO applicantProfileDTO = applicantProfileService.getApplicantProfileById(applicantid);
           return ResponseEntity.ok(applicantProfileDTO);
       } catch (CustomException e) {
           return ResponseEntity.status(e.getStatus()).body(null);
       }
   }
       @DeleteMapping("/deletedetails/{applicantId}")
       public ResponseEntity<Void> deleteApplicantProfile(@PathVariable int applicantId) {
    	   try {
               applicantProfileService.deleteApplicantProfile(applicantId);
               return ResponseEntity.noContent().build();
           } catch (CustomException e) {
               return ResponseEntity.status(e.getStatus()).build();
           }
       }
}
