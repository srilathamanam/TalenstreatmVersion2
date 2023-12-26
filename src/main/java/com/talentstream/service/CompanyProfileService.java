package com.talentstream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
 
import com.talentstream.dto.CompanyProfileDTO;
import com.talentstream.entity.CompanyProfile;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.CompanyProfileRepository;
import com.talentstream.repository.JobRecruiterRepository;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class CompanyProfileService {
 
    private final CompanyProfileRepository companyProfileRepository;

    @Autowired
   	JobRecruiterRepository jobRecruiterRepository;
 
    @Autowired
    public CompanyProfileService(CompanyProfileRepository companyProfileRepository) {
        this.companyProfileRepository = companyProfileRepository;
    }
 
    public String saveCompanyProfile(CompanyProfileDTO companyProfileDTO, Long jobRecruiterId) throws Exception {    	
        JobRecruiter jobRecruiter = jobRecruiterRepository.findByRecruiterId( jobRecruiterId);
       	 if(jobRecruiter==null)	
       		 throw new CustomException("Recruiter not found for ID: " + jobRecruiterId, HttpStatus.NOT_FOUND);
       	 else
    	    	{
       			
    	    		//CompanyProfile existingProfile = companyProfileRepository.findByJobRecruiter(jobRecruiterId);
    	    		//System.out.println(jobRecruiter.getCompanyname());
    	        if (!companyProfileRepository.existsByJobRecruiterId(jobRecruiterId))
    	        {
    	    	    CompanyProfile companyProfile= convertDTOToEntity(companyProfileDTO);
    	        	companyProfile.setJobRecruiter(jobRecruiter);	        
    	        	companyProfileRepository.save(companyProfile);
    	        //	System.out.println("profile saved sucesfully");
    	            return "profile saved sucessfully";
    	        }
    	        else {
    	        	System.out.println("profile  already exists");
    	        	throw new CustomException("CompanyProfile was already updated.", HttpStatus.OK);
    	        }
    	    	}
        	   	
       }
 
    public Optional<CompanyProfileDTO> getCompanyProfileById(Long id) {
        Optional<CompanyProfile> companyProfile = companyProfileRepository.findById(id);
        return companyProfile.map(this::convertEntityToDTO);
    }
 
    private CompanyProfileDTO convertEntityToDTO(CompanyProfile companyProfile) {
        CompanyProfileDTO dto = new CompanyProfileDTO();
         dto.setId(companyProfile.getId());
        dto.setCompanyName(companyProfile.getCompanyName());
        dto.setWebsite(companyProfile.getWebsite());
        dto.setPhoneNumber(companyProfile.getPhoneNumber());
        dto.setEmail(companyProfile.getEmail());
        dto.setHeadOffice(companyProfile.getHeadOffice());      
        dto.setSocialProfiles(companyProfile.getSocialProfiles());
        return dto;
    }
 
    
    private CompanyProfile convertDTOToEntity(CompanyProfileDTO companyProfileDTO) {
        CompanyProfile entity = new CompanyProfile();
  
        entity.setId(companyProfileDTO.getId());
        entity.setCompanyName(companyProfileDTO.getCompanyName());
        entity.setWebsite(companyProfileDTO.getWebsite());
        entity.setPhoneNumber(companyProfileDTO.getPhoneNumber());
        entity.setEmail(companyProfileDTO.getEmail());
        entity.setHeadOffice(companyProfileDTO.getHeadOffice());
        entity.setSocialProfiles(companyProfileDTO.getSocialProfiles());
              return entity;
    }

}