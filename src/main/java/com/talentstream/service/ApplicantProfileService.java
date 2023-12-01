package com.talentstream.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.talentstream.exception.CustomException;
import com.talentstream.dto.ApplicantProfileDTO;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.RegisterRepository;

@Service
public class ApplicantProfileService {
	 private final ApplicantProfileRepository applicantProfileRepository;
	 	 private final RegisterRepository applicantService;

	    @Autowired
	    public ApplicantProfileService(ApplicantProfileRepository applicantProfileRepository,RegisterRepository applicantService) {
	        this.applicantProfileRepository = applicantProfileRepository;
	        this.applicantService=applicantService;
	    }

	    public String createOrUpdateApplicantProfile(long applicantId, ApplicantProfileDTO applicantProfileDTO) {
	    	
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
	        System.out.println(applicantProfile);
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
	        applicantProfile.setExperienceDetails(applicantProfileDTO.getExperienceDetails());
	        applicantProfile.setRoles(applicantProfileDTO.getRoles());
	        if (applicantProfileDTO.getRoles() == null) {
	            applicantProfile.setRoles("ROLE_JOBAPPLICANT");
	        } else {
	            applicantProfile.setRoles(applicantProfileDTO.getRoles());
	        }
	        return applicantProfile;
	    }
	    private ApplicantProfileDTO convertEntityToDTO(ApplicantProfile applicantProfile) {
	    	if (applicantProfile == null) {
	    		System.out.println("not exist");
	            return null; 
	        }
	        ApplicantProfileDTO applicantProfileDTO = new ApplicantProfileDTO();
	        applicantProfileDTO.setBasicDetails(applicantProfile.getBasicDetails());
	        applicantProfileDTO.setSkillsRequired(applicantProfile.getSkillsRequired());
	        applicantProfileDTO.setExperienceDetails(applicantProfile.getExperienceDetails());
	        applicantProfileDTO.setRoles(applicantProfile.getRoles());
	        return applicantProfileDTO;
	    }
	}

	    
	    /*@RestController
@RequestMapping("/applicant")
public class ApplicantProfileController {

    private final ApplicantProfileService profileService;
    private final ApplicantDocumentsService documentsService;

    @Autowired
    public ApplicantProfileController(ApplicantProfileService profileService, ApplicantDocumentsService documentsService) {
        this.profileService = profileService;
        this.documentsService = documentsService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createApplicantProfile(@RequestBody ApplicantProfile profile,
                                                         @RequestParam("imageFile") MultipartFile imageFile,
                                                         @RequestParam("resumeFile") MultipartFile resumeFile) {
        try {
            // Convert MultipartFile to byte[]
            byte[] imageBytes = imageFile.getBytes();
            byte[] resumeBytes = resumeFile.getBytes();

            // Create ApplicantDocuments entity
            ApplicantDocuments documents = new ApplicantDocuments();
            documents.setImage(imageBytes);
            documents.setResume(resumeBytes);

            // Save documents
            documentsService.saveDocuments(documents);

            // Save profile with reference to documents
            profile.setDocuments(documents);
            profileService.saveApplicantProfile(profile);

            return new ResponseEntity<>("Applicant profile created successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create applicant profile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add other controller methods as needed
}*/
