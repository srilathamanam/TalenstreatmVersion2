package com.talentstream.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.talentstream.service.ViewJobService;
import com.talentstream.dto.JobDTO;
import com.talentstream.exception.CustomException;

@RestController
@RequestMapping("/viewjob")
public class ViewJobController {
	 
	@Autowired
    private ViewJobService viewJobService;
	private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    //@GetMapping("/applicant/viewjob/{jobId}/{applicantId}")
	   @GetMapping("/applicant/viewjob/{jobId}")
	    public ResponseEntity<JobDTO> getJobDetailsForApplicant(
	                      @PathVariable Long jobId) {
	    	
		   try {
			   JobDTO jobDetails = viewJobService.getJobDetailsForApplicant(jobId);
		        return ResponseEntity.ok(jobDetails);
		    } catch (CustomException e) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		    }
	 
		   }  
}
