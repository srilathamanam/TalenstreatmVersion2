package com.talentstream.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantImage;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.ApplicantImageRepository;
import com.talentstream.repository.RegisterRepository;
import java.nio.file.Path;

@Service
public class ApplicantImageService {
	
	private final Path root = Paths.get("applicantprofileimages");
	@Autowired
	private ApplicantImageRepository applicantImageRepository;
	
	@Autowired
	private  RegisterRepository applicantService;
	
	public String UploadImage(long applicantId, MultipartFile imageFile) throws IOException
	{		
		try {
		      Files.createDirectories(root);
		    } catch (IOException e) {
		      throw new RuntimeException("Could not initialize folder for upload!");
		    }
		
		
		Applicant applicant = applicantService.getApplicantById(applicantId);
    	if(applicant==null)	    	
    	  	throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
    	else
    	{
    		 if (applicantImageRepository.existsByApplicant(applicant)) {
    	            throw new CustomException("An image already exists for the applicant.", HttpStatus.BAD_REQUEST);
    	        }
    		 
    		 if (imageFile.getSize() > 1 * 1024 * 1024) {
    	            throw new CustomException("File size should be less than 1MB.", HttpStatus.BAD_REQUEST);
    	        }
    		 String contentType = imageFile.getContentType();
    	        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
    	            throw new CustomException("Only JPG and PNG file types are allowed.", HttpStatus.BAD_REQUEST);
    	        }
    	String name=StringUtils.cleanPath(imageFile.getOriginalFilename());    			
		String fileName = applicantId + "_" + name;        
		String filePath=applicantId + "_" + name;		 
		Files.copy(imageFile.getInputStream(),this.root.resolve(filePath),StandardCopyOption.REPLACE_EXISTING);
		ApplicantImage applicantImage = new ApplicantImage();
        applicantImage.setImagename(fileName);
        applicantImage.setApplicant(applicant);
        applicantImageRepository.save(applicantImage);
        
		return name;
    	}
	}

	public Resource downloadImage(Long applicantId) throws MalformedURLException {
		
		Applicant applicant = applicantService.findById(applicantId);
        if (applicant == null)
            throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
                ApplicantImage applicantImage = applicantImageRepository.findById(applicant);
        if (applicantImage == null)
            throw new CustomException("Image not found for applicant ID: " + applicantId, HttpStatus.NOT_FOUND);
        Path filePath = this.root.resolve(applicantImage.getImagename());
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }
}
    
    
