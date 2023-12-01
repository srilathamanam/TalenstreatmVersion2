package com.talentstream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.talentstream.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.JobDTO;
import com.talentstream.entity.Job;
import com.talentstream.service.SavedJobService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/savedjob")
public class SavedJobController {
    final ModelMapper modelMapper = new ModelMapper();
	 @Autowired
	    private SavedJobService savedJobService;
	 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
	    @PostMapping("/applicants/savejob/{applicantId}/{jobId}")
	    public ResponseEntity<String> saveJobForApplicant(
	            @PathVariable long applicantId,
	            @PathVariable long jobId
	    ) {
	    	try {
	            savedJobService.saveJobForApplicant(applicantId, jobId);
	            return ResponseEntity.ok("Job saved successfully for the applicant.");
	        } catch (CustomException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving job for the applicant.");
	        }
	     }

	    
    @GetMapping("/getSavedJobs/{applicantId}")
	    public ResponseEntity<List<JobDTO>> getSavedJobsForApplicantAndJob(
	            @PathVariable long applicantId
	            
	    ) {
    	try {
            List<Job> savedJobs = savedJobService.getSavedJobsForApplicant(applicantId);

            if (savedJobs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<JobDTO> savedJobsDTO = savedJobs.stream()
                    .map(job -> modelMapper.map(job, JobDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(savedJobsDTO);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
}
