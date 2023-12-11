package com.talentstream.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import com.talentstream.exception.CustomException;
import com.talentstream.exception.UnsupportedFileTypeException;
import com.talentstream.service.ApplicantImageService;

@RestController
@RequestMapping("/applicant-image")
public class ApplicantImageController {
	
	@Autowired
    private ApplicantImageService applicantImageService;
	
	@Value("${project.photo-image-folder}")
	private String path;
	
    @PostMapping("/{applicantId}/upload")
    public String fileUpload(@PathVariable Long applicantId,@RequestParam("photo")MultipartFile photo) 
    {
    	 try {
    	        String filename = this.applicantImageService.UploadImage(applicantId, photo);
    	        return "Image uploaded successfully. Filename: " + filename;
    	    } catch (CustomException ce) {
    	        return ce.getMessage();
    	    } catch (UnsupportedFileTypeException e) {
    	        return "Only JPG and PNG file types are allowed.";
    	    } catch (MaxUploadSizeExceededException e) {
    	        return "File size should be less than 1MB.";
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return "Image not uploaded successfully";
    	    }
    }
    
    @GetMapping("/{applicantId}/download")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long applicantId) throws IOException {
    	try {
    	Resource resource = applicantImageService.downloadImage(applicantId);
    	String contentType = Files.probeContentType(resource.getFile().toPath());
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		        .contentType(MediaType.parseMediaType(contentType))
		        .body(resource);
    } catch (CustomException ce) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
 
    }
}