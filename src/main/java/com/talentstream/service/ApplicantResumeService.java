package com.talentstream.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import com.talentstream.entity.ApplicantResume;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.ApplicantResumeRepository;
import com.talentstream.repository.RegisterRepository;

@Service
public class ApplicantResumeService {
	private final Path root = Paths.get("applicantprofileimages");
	@Autowired
	private ApplicantResumeRepository applicantResumeRepository;
	
	@Autowired
	private  RegisterRepository applicantService;
	
	public String UploadPdf(long applicantId, MultipartFile pdfFile) throws IOException
	{	
		 try {
		        Files.createDirectories(root);
		    } catch (IOException e) {
		        throw new RuntimeException("Could not initialize folder for upload!");
		    }

		    Applicant applicant = applicantService.getApplicantById(applicantId);
		    if (applicant == null)
		        throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
		    else {
		        if (applicantResumeRepository.existsByApplicant(applicant)) {
		            throw new CustomException("PDF already exists for the applicant.", HttpStatus.BAD_REQUEST);
		        }

		        if (pdfFile.getSize() > 1 * 1024 * 1024) {
		            throw new CustomException("File size should be less than 1MB.", HttpStatus.BAD_REQUEST);
		        }

		        String contentType = pdfFile.getContentType();
		        if (!"application/pdf".equals(contentType)) {
		            throw new CustomException("Only PDF file types are allowed.", HttpStatus.BAD_REQUEST);
		        }

		        String name = StringUtils.cleanPath(pdfFile.getOriginalFilename());
		        String fileName = applicantId + "_" + name;
		        String filePath = applicantId + "_" + name;
		        Files.copy(pdfFile.getInputStream(), this.root.resolve(filePath), StandardCopyOption.REPLACE_EXISTING);

		        ApplicantResume applicantResume = new ApplicantResume();
		        applicantResume.setPdfname(fileName);
		        applicantResume.setApplicant(applicant);
		        applicantResumeRepository.save(applicantResume);

		        return name;
		    }
	}
	
public Resource downloadPdf(Long applicantId) throws MalformedURLException {
		
		Applicant applicant = applicantService.findById(applicantId);
        if (applicant == null)
            throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
                ApplicantResume applicantResume = applicantResumeRepository.findById(applicant);
        if (applicantResume == null)
            throw new CustomException("PDF not found for applicant ID: " + applicantId, HttpStatus.NOT_FOUND);
        Path filePath = this.root.resolve(applicantResume.getPdfname());
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }
}
