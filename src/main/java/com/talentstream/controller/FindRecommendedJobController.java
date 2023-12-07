package com.talentstream.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.JobDTO;
import com.talentstream.dto.RecuriterSkillsDTO;
import com.talentstream.entity.Job;
import com.talentstream.entity.RecuriterSkills;
import com.talentstream.exception.CustomException;
import com.talentstream.service.FinRecommendedJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/recommendedjob")
public class FindRecommendedJobController {
	private final FinRecommendedJobService finJobService;
	 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    @Autowired
    public FindRecommendedJobController(FinRecommendedJobService finJobService) {
        this.finJobService = finJobService;
    }

    @GetMapping("/findrecommendedjob/{applicantId}")
    public  ResponseEntity<List<JobDTO>> recommendJobsForApplicant(@PathVariable String applicantId) {
    	try {
            long applicantIdLong = Long.parseLong(applicantId);
            List<Job> recommendedJobs = finJobService.findJobsMatchingApplicantSkills(applicantIdLong);

            if (recommendedJobs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            } else {
            	 List<JobDTO> jobDTOs = recommendedJobs.stream()
                         .map(job -> convertEntityToDTO(job))
                         .collect(Collectors.toList());

                return ResponseEntity.ok(jobDTOs);
            }
        } catch (NumberFormatException ex) {
            throw new CustomException("Invalid applicant ID format", HttpStatus.BAD_REQUEST);
        } catch (CustomException ce) {
            return ResponseEntity.status(ce.getStatus()).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
    
    private JobDTO convertEntityToDTO(Job job) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setJobTitle(job.getJobTitle());
        jobDTO.setMinimumExperience(job.getMinimumExperience());
        jobDTO.setMaximumExperience(job.getMaximumExperience());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setEmployeeType(job.getEmployeeType());
        jobDTO.setIndustryType(job.getIndustryType());
        jobDTO.setMinimumQualification(job.getMinimumQualification());
        jobDTO.setSpecialization(job.getSpecialization());
        Set<RecuriterSkillsDTO> skillsDTOList = job.getSkillsRequired().stream()
                .map(this::convertSkillsEntityToDTO)
                .collect(Collectors.toSet());
        jobDTO.setSkillsRequired(skillsDTOList);  
        return jobDTO;
    }
    private RecuriterSkillsDTO convertSkillsEntityToDTO(RecuriterSkills skill) {
        RecuriterSkillsDTO skillDTO = new RecuriterSkillsDTO();
        skillDTO.setSkillName(skill.getSkillName());
        skillDTO.setMinimumExperience(skill.getMinimumExperience());
        return skillDTO;
    }
}

