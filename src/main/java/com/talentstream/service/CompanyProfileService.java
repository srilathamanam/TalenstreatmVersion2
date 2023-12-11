package com.talentstream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
 
import com.talentstream.dto.CompanyProfileDTO;
import com.talentstream.entity.CompanyProfile;
import com.talentstream.entity.JobRecruiter;
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
 
    public CompanyProfile saveCompanyProfile(CompanyProfileDTO companyProfileDTO, Long jobRecruiterId) throws Exception {
    	JobRecruiter jobRecruiter = jobRecruiterRepository.findByRecruiterId( jobRecruiterId);
	   	 if (jobRecruiter != null) {	        
	            CompanyProfile companyProfile = convertDTOToEntity(companyProfileDTO);	           
	            companyProfile.setJobRecruiter(jobRecruiter);
	            return companyProfileRepository.save(companyProfile);
	        } 
	   	 else {
	            throw new Exception("JobRecruiter with ID " + jobRecruiterId + " not found.");
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