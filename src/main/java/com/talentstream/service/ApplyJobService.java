package com.talentstream.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.talentstream.entity.Alerts;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantJobInterviewDTO;
import com.talentstream.entity.ApplicantStatusHistory;
import com.talentstream.entity.AppliedApplicantInfo;
import com.talentstream.entity.AppliedApplicantInfoDTO;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.repository.AlertsRepository;
import com.talentstream.repository.ApplicantStatusHistoryRepository;
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
	    
	    @Autowired
		private ApplicantStatusHistoryRepository statusHistoryRepository;
	    @Autowired
	    private AlertsRepository alertsRepository;
	    @Autowired
	    private JavaMailSender javaMailSender;
 
	    public String ApplicantApplyJob(long  applicantId, long jobId) {
	    	
	    	try {
	            Applicant applicant = applicantRepository.findById(applicantId);
	            Job job = jobRepository.findById(jobId).orElse(null);

	            if (applicant == null || job == null) {
	                throw new CustomException("Applicant ID or Job ID not found", HttpStatus.NOT_FOUND);
	            }

	            else{
	            	if (applyJobRepository.existsByApplicantAndJob(applicant, job)) {
	                       	return "Job has already been applied by the applicant";
	            	}else {
	            		ApplyJob applyJob = new ApplyJob();
	    	            applyJob.setApplicant(applicant);
	    	            applyJob.setJob(job);
	    	            applyJobRepository.save(applyJob);
	    	            
	    	            // Increment alert count
	    		        incrementAlertCount(applyJob.getApplicant());
	    		        
	    		        //SaveStatusHistory
	    	            saveStatusHistory(applyJob, applyJob.getApplicantStatus());
	    	            Job jobs=applyJob.getJob();
	    	            if(jobs!=null) {
	    	            	JobRecruiter recruiter=jobs.getJobRecruiter();
	    	            	if(recruiter!=null) {
	    	            		String companyName=recruiter.getCompanyname();
	    	            		if(companyName!=null) {
	    	            			String cN=recruiter.getCompanyname();
	    	            			//SendAlerts
	    	            			sendAlerts(applyJob,applyJob.getApplicantStatus(),cN);
	    	            			return "Job applied successfully";
	    	            		}
	    	            	}
	    	            }return "Company information not found for the given ApplyJob";
	            		}
	            	}
	            }catch (CustomException ex) {
	                    throw ex;
	             } catch (Exception e) {
	                    throw new CustomException("An error occurred while applying for the job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	             }
	      }
	            
	    //This method is to increment count of alerts whenever recruiter updating the status
	    private void incrementAlertCount(Applicant applicant) {
			// TODO Auto-generated method stub
	    	if (applicant != null) {
	            int currentAlertCount = applicant.getAlertCount();
	            applicant.setAlertCount(currentAlertCount + 1);
	            applicantRepository.save(applicant);
	        }
		}

	    //This method is to display alerts whenever we click on Alerts
		private void sendAlerts(ApplyJob applyJob, String applicantStatus, String cN) {
			// TODO Auto-generated method stub
	    	Alerts alerts=new Alerts();
			alerts.setApplyJob(applyJob);
			alerts.setCompanyName(cN);
			alerts.setStatus(applicantStatus);
			alerts.setChangeDate(LocalDate.now());
			alertsRepository.save(alerts);
			// Send email to the applicant
	        sendEmailToApplicant(applyJob.getApplicant().getEmail(), cN, applicantStatus);
		}
		
		//This method is to send interview status to the applicant mail id
		private void sendEmailToApplicant(String toEmail, String cN, String applicantStatus) {
			// TODO Auto-generated method stub
			try {
				SimpleMailMessage message=new SimpleMailMessage();
				// Set email properties
				message.setTo(toEmail);
				message.setSubject("Job Alert Notification");
				// Customize your email content
	            String content = "Dear Applicant,\n\n"
	                    + "Your job application status has been updated to: " + applicantStatus + "\n"
	                    + "Company: " + cN + "\n\n"
	                    + "Thank you.\n\n"
	                    + "Best regards,\n"
	                    + "Your Company Name";

	            message.setText(content);
	            
	            // Send the email
	            javaMailSender.send(message);
	        } catch (Exception e) {
	            // Handle exceptions, log, and consider appropriate error handling
	        	e.printStackTrace();
	        }
		}

		//This method is to save the track of statuses that updated by recruiter
		private void saveStatusHistory(ApplyJob applyJob, String applicationStatus) {
			// TODO Auto-generated method stub
			ApplicantStatusHistory statusHistory=new ApplicantStatusHistory();
			statusHistory.setApplyJob(applyJob);
			statusHistory.setStatus(applicationStatus);
			statusHistory.setChangeDate(LocalDateTime.now());
			statusHistoryRepository.save(statusHistory);
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
    Job job=applyJob.getJob();
    if(job!=null) {
    	JobRecruiter recruiter=job.getJobRecruiter();
    	if(recruiter!=null) {
    		String companyName=recruiter.getCompanyname();
    		if(companyName!=null) {
    			applyJob.setApplicantStatus(newStatus);
    		    applyJobRepository.save(applyJob);
    		    //Increment alert count
    			incrementAlertCount(applyJob.getApplicant());
    			// Save status history
    		    saveStatusHistory(applyJob, applyJob.getApplicantStatus());
    		    //Send alerts
    		    sendAlerts(applyJob,applyJob.getApplicantStatus(),companyName);
    		    return "Applicant status updated to: " + newStatus;
    		}
    	}
    }
    return "Company information not found for the given ApplyJob";    
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

//This method is to get list of statuses related to particular job
public List<ApplicantStatusHistory> getApplicantStatusHistory(long applyJobId) {
	// TODO Auto-generated method stub
	return statusHistoryRepository.findByApplyJob_ApplyJobIdOrderByChangeDateDesc(applyJobId);
}

//This method is to get alerts sent by recruiter
public List<Alerts> getAlerts(long applyJobId) {
	// TODO Auto-generated method stub
	return alertsRepository.findByApplyJob_ApplyJobIdOrderByChangeDateDesc(applyJobId);
}

//This method is to reset count of alerts to zero once after reading all the alert messages.
public void resetAlertCount(long applyJobId) {
	// TODO Auto-generated method stub
	try {
        ApplyJob applyJob = applyJobRepository.findById(applyJobId)
                .orElseThrow(() -> new EntityNotFoundException("Apply job not found"));

        applyJob.getApplicant().setAlertCount(0);
        applyJobRepository.save(applyJob);
    } catch (Exception e) {
        // Handle exceptions, log, and consider appropriate error handling
    	e.printStackTrace();
    }
}
}

	    