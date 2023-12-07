package com.talentstream.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantJobInterviewDTO;
import com.talentstream.entity.AppliedApplicantInfo;
import com.talentstream.entity.AppliedApplicantInfoDTO;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.repository.ApplyJobRepository;
import com.talentstream.repository.JobRepository;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.repository.ScheduleInterviewRepository; 
import jakarta.persistence.EntityNotFoundException;
import com.talentstream.exception.CustomException;
 
@Service
public class ApplyJobService {
	 @Autowired
	   private ApplyJobRepository applyJobRepository;	
	  
	 @Autowired
	   private ScheduleInterviewRepository scheduleInterviewRepository;	
	 	    
	    @Autowired
	    private JobRepository jobRepository;
	    
	    @Autowired
	    private RegisterRepository applicantRepository;
 
	    public String ApplicantApplyJob(long  applicantId, long jobId) {
	    	
	    	try {
	            Applicant applicant = applicantRepository.findById(applicantId);
	            Job job = jobRepository.findById(jobId).orElse(null);

	            if (applicant == null || job == null) {
	                throw new CustomException("Applicant ID or Job ID not found", HttpStatus.NOT_FOUND);
	            }

	            if (applyJobRepository.existsByApplicantAndJob(applicant, job)) {
	                throw new CustomException("Job has already been applied by the applicant", HttpStatus.BAD_REQUEST);
	            }

	            ApplyJob applyJob = new ApplyJob();
	            applyJob.setApplicant(applicant);
	            applyJob.setJob(job);
	            applyJobRepository.save(applyJob);

	            return "Job Applied Successfully";
	        } catch (CustomException ex) {
	            throw ex; 
	        } catch (Exception e) {
	            throw new CustomException("An error occurred while applying for the job", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    public List<ApplyJob> getAppliedApplicantsForJob(Long jobId) {
	    	 try {
	             return applyJobRepository.findByJobId(jobId);
	         } catch (Exception e) {
	             throw new CustomException("Failed to retrieve applied applicants for the job", HttpStatus.INTERNAL_SERVER_ERROR);
	         }
	    }
 
	public List<Job> getAppliedJobsForApplicant(long applicantId) {
		List<Job> result = new ArrayList<>();      
	     
	      try {
	          List<ApplyJob> appliedJobs = applyJobRepository.findByApplicantId(applicantId);
 
	          for (ApplyJob appliedJobs1 : appliedJobs) {
	              result.add(appliedJobs1 .getJob());
	          }
 
	      } catch (Exception e) {
	    	  throw new CustomException("Failed to get applied jobs for the applicant", HttpStatus.INTERNAL_SERVER_ERROR);
	      }
 	      return result;
	  }

 
public List<AppliedApplicantInfoDTO> getAppliedApplicants(long jobRecruiterId) {
List<AppliedApplicantInfo> appliedApplicants = applyJobRepository.findAppliedApplicantsInfo(jobRecruiterId);
 
List<AppliedApplicantInfoDTO> dtoList = new ArrayList<>();
for (AppliedApplicantInfo appliedApplicantInfo : appliedApplicants) {
    AppliedApplicantInfoDTO dto = mapToDTO(appliedApplicantInfo);
    dtoList.add(dto);
}
 
return dtoList;
}
 
 
 
private AppliedApplicantInfoDTO mapToDTO(AppliedApplicantInfo appliedApplicantInfo) {
    AppliedApplicantInfoDTO dto = new AppliedApplicantInfoDTO();
    dto. setApplyjobid(appliedApplicantInfo.getApplyjobid());
    dto.setName(appliedApplicantInfo.getName());
    dto.setEmail(appliedApplicantInfo.getEmail());
    dto.setMobilenumber(appliedApplicantInfo.getMobilenumber());
    dto.setJobTitle(appliedApplicantInfo.getJobTitle());
    dto.setApplicantStatus(appliedApplicantInfo.getApplicantStatus());    
    dto.setMinimumExperience(appliedApplicantInfo.getMinimumExperience());
    dto.setSkillName(appliedApplicantInfo.getSkillName());
    dto.setLocation(appliedApplicantInfo.getLocation());
    dto.setLocation(appliedApplicantInfo.getLocation()); 
      return dto;
}
 
 

 
 
public String updateApplicantStatus(Long applyJobId, String newStatus) {
    ApplyJob applyJob = applyJobRepository.findById(applyJobId)
            .orElseThrow(() -> new EntityNotFoundException("ApplyJob not found"));
 
    applyJob.setApplicantStatus(newStatus);
    applyJobRepository.save(applyJob);
 
    return "Applicant status updated to: " + newStatus;
}
 
public List<ApplicantJobInterviewDTO> getApplicantJobInterviewInfoForRecruiterAndStatus(
        long recruiterId, String applicantStatus) {
	try {
        return scheduleInterviewRepository.getApplicantJobInterviewInfoByRecruiterAndStatus(recruiterId, applicantStatus);
    } catch (Exception e) {
        throw new CustomException("Failed to retrieve applicant job interview info", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
 
public long countJobApplicantsByRecruiterId(Long recruiterId) {
	try {
        return applyJobRepository.countJobApplicantsByRecruiterId(recruiterId);
    } catch (Exception e) {
        throw new CustomException("Failed to count job applicants for the recruiter", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
 
public long countSelectedApplicants() {   
	 try {
         return applyJobRepository.countByApplicantStatus("selected");
     } catch (Exception e) {
         throw new CustomException("Failed to count selected applicants", HttpStatus.INTERNAL_SERVER_ERROR);
     }
}
 
public long countShortlistedAndInterviewedApplicants() {
	try {
        List<String> desiredStatusList = Arrays.asList("shortlisted", "interviews");
        return applyJobRepository.countByApplicantStatusIn(desiredStatusList);
    } catch (Exception e) {
        throw new CustomException("Failed to count shortlisted and interviewed applicants", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}

	    