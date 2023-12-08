package com.talentstream.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.talentstream.dto.JobDTO;
import com.talentstream.dto.RecuriterSkillsDTO;
import com.talentstream.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.talentstream.entity.Job;
import com.talentstream.exception.CustomException;
import com.talentstream.service.JobService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/job")  
public class JobController {
	
	  final ModelMapper modelMapper = new ModelMapper();
    private final JobService jobService;
    private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    
    @PostMapping("/recruiters/saveJob/{jobRecruiterId}")
    public ResponseEntity<String> saveJob(@RequestBody @Valid JobDTO jobDTO, @PathVariable Long jobRecruiterId) {
    	try {
            return jobService.saveJob(jobDTO, jobRecruiterId);
        } catch (CustomException ce) {
            return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred.");
        }        
    }
    @GetMapping("/recruiters/viewJobs/{jobRecruiterId}")
    public ResponseEntity<?> getJobsByRecruiter(@PathVariable Long jobRecruiterId) {
    	try {
            List<Job> jobs = jobService.getJobsByRecruiter(jobRecruiterId);

            if (jobs.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            List<JobDTO> jobDTOs = jobs.stream()
            		.map(job -> {
                        JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
                       
                        jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
                        jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
                        jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
                        jobDTO.setEmail(job.getJobRecruiter().getEmail());
                        return jobDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(jobDTOs);
        } catch (CustomException ce) {
            return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred.");
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchJobs(@ModelAttribute JobSearchCriteria searchCriteria) {
        try {
            List<Job> jobs = jobService.searchJobs(searchCriteria);

            if (jobs.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            List<JobDTO> jobDTOs = jobs.stream()
                    .map(job -> modelMapper.map(job, JobDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(jobDTOs);
        } catch (CustomException ce) {
            return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred.");
        }
    }

    
    
    @GetMapping("/recruiters/viewJobs")
    public ResponseEntity<?>  getAllJobs() {
    	try {
            List<Job> jobs = jobService.getAllJobs();
            List<JobDTO> jobDTOs = jobs.stream()
                    .map(job -> convertEntityToDTO(job))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(jobDTOs);
        } catch (CustomException ce) {
            return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred.");
        }
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable Long jobId) {
        try {
            Job job = jobService.getJobById(jobId);

            if (job != null) {
                JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
                return ResponseEntity.ok(jobDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (CustomException ce) {
            return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred.");
        }
    }
   

    @GetMapping("/recruiterscountjobs/{recruiterId}")
    public ResponseEntity<?> countJobsByRecruiter(@PathVariable Long recruiterId) {
        try {
            long jobCount = jobService.countJobsByRecruiterId(recruiterId);
            return ResponseEntity.ok(jobCount);
        } catch (CustomException ce) {
            return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred.");
        }
    }
    private RecuriterSkillsDTO convertSkillsEntityToDTO(RecuriterSkills skill) {
        RecuriterSkillsDTO skillDTO = new RecuriterSkillsDTO();
        skillDTO.setSkillName(skill.getSkillName());
        skillDTO.setMinimumExperience(skill.getMinimumExperience());
        return skillDTO;
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
}
