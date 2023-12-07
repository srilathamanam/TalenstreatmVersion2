package com.talentstream.service;


import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.exception.CustomException;
import com.talentstream.dto.ApplicantProfileDTO;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.util.MultipartFileUtils;

import java.sql.Blob;


@Service
public class ApplicantProfileService {
	 private final ApplicantProfileRepository applicantProfileRepository;
	 	 private final RegisterRepository applicantService;
	
	 	   
	    @Autowired
	    public ApplicantProfileService(ApplicantProfileRepository applicantProfileRepository,RegisterRepository applicantService) {
	        this.applicantProfileRepository = applicantProfileRepository;
	        this.applicantService=applicantService;
	      
	    }
	  
	    public String createOrUpdateApplicantProfile(long applicantId, ApplicantProfileDTO applicantProfileDTO) throws IOException {
	    	Applicant applicant = applicantService.getApplicantById(applicantId);
	    	if(applicant==null)	    	
	    	  	throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
	    	else
	    	{
	    		ApplicantProfile existingProfile = applicantProfileRepository.findByApplicantId(applicantId);
 	        if (existingProfile == null) {	        
 	        	 	ApplicantProfile applicantProfile = convertDTOToEntity(applicantProfileDTO);
	        	applicantProfile.setApplicant(applicant);	        
	            applicantProfileRepository.save(applicantProfile);
	            return "profile saved sucessfully";
	        } else {
	        	throw new CustomException("Profile for this applicant already exists", HttpStatus.BAD_REQUEST);
	        }
	    	}
	    }


	    public ApplicantProfileDTO getApplicantProfileById(long applicantId) {
	    	try
	    	{
	    	ApplicantProfile applicantProfile = applicantProfileRepository.findByApplicantId(applicantId);
	          	return convertEntityToDTO(applicantProfile);
	    	}
	    	catch(CustomException e)
	    	{
	    		throw new CustomException("Failed to get profile for applicant ID: " + applicantId, HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
	    }

	    public void deleteApplicantProfile(long applicantId) {
	    	try {
	            applicantProfileRepository.deleteById((int) applicantId);
	        } catch (Exception e) {
	            throw new CustomException("Failed to delete profile for applicant ID: " + applicantId, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    private ApplicantProfile convertDTOToEntity(ApplicantProfileDTO applicantProfileDTO) {
	        ApplicantProfile applicantProfile = new ApplicantProfile();
	        applicantProfile.setBasicDetails(applicantProfileDTO.getBasicDetails());	       
	        applicantProfile.setSkillsRequired(applicantProfileDTO.getSkillsRequired());
	        applicantProfile.setGraduationDetails(applicantProfileDTO.getGraduationDetails());
	        applicantProfile.setIntermediateDetails(applicantProfileDTO.getIntermediateDetails());
	        applicantProfile.setxClassDetails(applicantProfileDTO.getxClassDetails());
	        applicantProfile.setExperienceDetails(applicantProfileDTO.getExperienceDetails());
	        applicantProfile.setRoles(applicantProfileDTO.getRoles());
	       	        if (applicantProfileDTO.getRoles() == null) {
	            applicantProfile.setRoles("ROLE_JOBAPPLICANT");
	        } else {
	            applicantProfile.setRoles(applicantProfileDTO.getRoles());
	        }
	     
	        return applicantProfile;
		    
	          }
	     	        
	    public static ApplicantProfileDTO convertEntityToDTO(ApplicantProfile applicantProfile) {
	        if (applicantProfile == null) {
	            System.out.println("not exist");
	            return null;
	        }
	        ApplicantProfileDTO applicantProfileDTO = new ApplicantProfileDTO();
	        applicantProfileDTO.setBasicDetails(applicantProfile.getBasicDetails());
	        applicantProfileDTO.setGraduationDetails(applicantProfile.getGraduationDetails());
	        applicantProfileDTO.setIntermediateDetails(applicantProfile.getIntermediateDetails());
	        applicantProfileDTO.setxClassDetails(applicantProfile.getxClassDetails());
	        applicantProfileDTO.setSkillsRequired(applicantProfile.getSkillsRequired());
	        applicantProfileDTO.setExperienceDetails(applicantProfile.getExperienceDetails());
	        applicantProfileDTO.setRoles(applicantProfile.getRoles());
	        	        return applicantProfileDTO;
	    }
	       	        
	   
	      
}

	 