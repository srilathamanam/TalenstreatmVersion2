package com.talentstream.controller;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.JobDTO;
import com.talentstream.dto.ScheduleInterviewDTO;
import com.talentstream.entity.ApplicantJobInterviewDTO;
import com.talentstream.entity.AppliedApplicantInfoDTO;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.entity.ScheduleInterview;
import com.talentstream.service.ApplyJobService;
import com.talentstream.service.ScheduleInterviewService;
import com.talentstream.exception.CustomException;
@RestController       
@RequestMapping("/applyjob")
public class ApplyJobController {
	
	  final ModelMapper modelMapper = new ModelMapper();
	 @Autowired
	    private ApplyJobService applyJobService;
	 @Autowired
	    private ScheduleInterviewService scheduleInterviewService;
	 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
	 
	 @PostMapping("/applicants/applyjob/{applicantId}/{jobId}")
	    public ResponseEntity<String> saveJobForApplicant(
	            @PathVariable long applicantId,
	            @PathVariable long jobId
	    ){
		 try {
	
            String result = applyJobService.ApplicantApplyJob(applicantId, jobId);
            return ResponseEntity.ok(result);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (Exception e) {
          
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
	 }
	 
	 @GetMapping("/appliedapplicants/{jobId}")
	 public ResponseEntity<List<ApplyJob>> getAppliedApplicantsForJob(@PathVariable Long jobId) {
	        try {
	            List<ApplyJob> appliedApplicants = applyJobService.getAppliedApplicantsForJob(jobId);
	            return ResponseEntity.ok(appliedApplicants);
	        } catch (Exception e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	        }
	    }
	    
 @GetMapping("/getAppliedJobs/{applicantId}")
 public ResponseEntity<List<JobDTO>> getAppliedJobsForApplicant(@PathVariable long applicantId) {
     try {
         List<Job> appliedJobs = applyJobService.getAppliedJobsForApplicant(applicantId);

         if (appliedJobs.isEmpty()) {
             return ResponseEntity.noContent().build();
         }

         List<JobDTO> appliedJobsDTO = appliedJobs.stream()
                 .map(job -> modelMapper.map(job, JobDTO.class))
                 .collect(Collectors.toList());

         return ResponseEntity.ok(appliedJobsDTO);
     } catch (CustomException e) {
         return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
     }
 }
 @GetMapping("/recruiter/{jobRecruiterId}/appliedapplicants")
 public ResponseEntity<List<AppliedApplicantInfoDTO>> getAppliedApplicantsForRecruiter(@PathVariable long jobRecruiterId) {
	 try {
	        List<AppliedApplicantInfoDTO> appliedApplicants = applyJobService.getAppliedApplicants(jobRecruiterId);
	        return ResponseEntity.ok(appliedApplicants);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
	    } catch (Exception e) {
	       
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	    }
  
 }
 @PostMapping("/scheduleInterview/{applyJobId}")
 public ResponseEntity<Void> createScheduleInterview(
         @PathVariable Long applyJobId,
         @RequestBody ScheduleInterviewDTO scheduleInterviewDTO) {
	 try {
         ScheduleInterview scheduleInterview = modelMapper.map(scheduleInterviewDTO, ScheduleInterview.class);
         ScheduleInterviewDTO createdInterview = scheduleInterviewService.createScheduleInterview(applyJobId, scheduleInterview);

         return createdInterview != null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }
   
   
   @PostMapping("/recruiters/applyjob-update-status/{applyJobId}/{newStatus}")
   public ResponseEntity<String> updateApplicantStatus(
           @PathVariable Long applyJobId,
           @PathVariable String newStatus) {
	   try {
	        String updateMessage = applyJobService.updateApplicantStatus(applyJobId, newStatus);
	        return ResponseEntity.ok(updateMessage);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	    } catch (Exception e) {
	      
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    }
   }
   @GetMapping("/recruiter/{recruiterId}/interviews/{status}")
   public ResponseEntity<List<ApplicantJobInterviewDTO>> getApplicantJobInterviewInfo(
           @PathVariable("recruiterId") long recruiterId,
           @PathVariable("status") String status) {
	   try {
	        List<ApplicantJobInterviewDTO> interviewInfo = applyJobService.getApplicantJobInterviewInfoForRecruiterAndStatus(recruiterId, status);
	        return ResponseEntity.ok(interviewInfo);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
	    } catch (Exception e) {
	       
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	    }
   }
   
   @GetMapping("/recruiters/applyjobapplicantscount/{recruiterId}")
   public ResponseEntity<Long> countJobApplicantsByRecruiterId( @PathVariable Long recruiterId) {
	   try {
	        long count = applyJobService.countJobApplicantsByRecruiterId(recruiterId);
	        return ResponseEntity.ok(count);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(0L); 
	    } catch (Exception e) {
	     
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); 
	    }
   }
   
   @GetMapping("/recruiters/selected/count")
   public ResponseEntity<Long> countSelectedApplicants() {
       
	   try {
	        long count = applyJobService.countSelectedApplicants();
	        return ResponseEntity.ok(count);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(0L); 
	    } catch (Exception e) {
	      
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); 
	    }
   }
   
   @GetMapping("/recruiters/countShortlistedAndInterviewed")
   public ResponseEntity<Long> countShortlistedAndInterviewedApplicants() {
	   try {
	        long count = applyJobService.countShortlistedAndInterviewedApplicants();
	        return ResponseEntity.ok(count);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(0L); 
	    } catch (Exception e) {
	       
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); 
	    }
   }
   
   @GetMapping("/current-date")
   public ResponseEntity<List<ScheduleInterviewDTO>> getScheduleInterviewsForCurrentDate() {
       try {
           List<ScheduleInterview> scheduleInterviews = scheduleInterviewService.getScheduleInterviewsForCurrentDate();

           if (scheduleInterviews.isEmpty()) {
               return ResponseEntity.noContent().build();
           }
           List<ScheduleInterviewDTO> scheduleInterviewDTOs = scheduleInterviews.stream()
                   .map(interview -> modelMapper.map(interview, ScheduleInterviewDTO.class))
                   .collect(Collectors.toList());

           return ResponseEntity.ok(scheduleInterviewDTOs);
       } catch (CustomException e) {
           return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
       }
   }
   @GetMapping("/getScheduleInterviews/applicant/{applyJobId}/{applicantId}")
   public ResponseEntity<List<ScheduleInterviewDTO>> getScheduleInterviews(
           @PathVariable Long applicantId, @PathVariable Long applyJobId) {
       try {
           List<ScheduleInterview> scheduleInterviews = scheduleInterviewService.getScheduleInterviewsByApplicantAndApplyJob(applicantId, applyJobId);

           if (scheduleInterviews.isEmpty()) {
               return ResponseEntity.noContent().build();
           }

           List<ScheduleInterviewDTO> scheduleInterviewDTOs = scheduleInterviews.stream()
                   .map(interview -> modelMapper.map(interview, ScheduleInterviewDTO.class))
                   .collect(Collectors.toList());
           return ResponseEntity.ok(scheduleInterviewDTOs);
       } catch (CustomException e) {
           return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
       }
   }
 }


