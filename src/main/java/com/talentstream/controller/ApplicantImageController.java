package com.talentstream.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.talentstream.response.FileResponse;
import com.talentstream.service.ApplicantImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/applicant-image")
public class ApplicantImageController {
	
	@Autowired
    private ApplicantImageService applicantImageService;
	
	@Value("${project.photoimage}")
	private String path;
	
    @PostMapping("/{applicantId}/upload")
    public ResponseEntity<FileResponse> fileUpload(@PathVariable Long applicantId,@RequestParam("photo")MultipartFile photo)
    {
    	try {
            String filename = this.applicantImageService.UploadImage(applicantId,  photo);
            return ResponseEntity.ok(new FileResponse(filename, "Image uploaded successfully"));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(null, "Image not uploaded successfully"), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{applicantId}/download")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long applicantId) throws IOException {
        Resource resource = applicantImageService.downloadImage(applicantId);
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		        .contentType(MediaType.IMAGE_JPEG)
		        .body(resource);
    }
 

}