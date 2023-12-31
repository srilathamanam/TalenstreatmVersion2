package com.talentstream.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.ApplicantSkills;
import com.talentstream.entity.Job;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.JobRepository;
import com.talentstream.exception.CustomException;

@Service
public class FinRecommendedJobService {

	@Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicantProfileRepository applicantRepository;
    
    @Autowired
    public FinRecommendedJobService(JobRepository jobRepository, ApplicantProfileRepository applicantRepository) {
        this.jobRepository = jobRepository;
        this.applicantRepository = applicantRepository;
    }
    public List<Job> findJobsMatchingApplicantSkills(long applicantId) {
    	try {
            ApplicantProfile applicantProfile = applicantRepository.findByApplicantId(applicantId);

            if (applicantProfile == null) {
                return Collections.emptyList();
            }

            Set<ApplicantSkills> applicantSkills = applicantProfile.getSkillsRequired();
            Set<String> lowercaseApplicantSkillNames = applicantSkills.stream()
                    .map(skill -> skill.getSkillName().toLowerCase())
                    .collect(Collectors.toSet());

                      List<Job> matchingJobs = jobRepository.findBySkillsRequiredIgnoreCaseAndSkillNameIn(lowercaseApplicantSkillNames);

            return matchingJobs;
        } catch (Exception e) {           
            throw new CustomException("Error while finding recommended jobs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public long countRecommendedJobsForApplicant(long applicantId) {
        try {
        	Optional<ApplicantProfile> optionalApplicant = applicantRepository.findByApplicantIdWithSkills(applicantId);

            if (optionalApplicant.isEmpty()) {
                // Return a specific indicator, for example, -1 to signify that the applicant is not found
                return 0;
            }

            ApplicantProfile applicant = optionalApplicant.get();

            Set<String> lowercaseApplicantSkillNames = applicant.getSkillsRequired().stream()
                    .map(skill -> skill.getSkillName().toLowerCase())
                    .collect(Collectors.toSet());

            List<Job> recommendedJobs = jobRepository.findBySkillsRequiredIgnoreCaseAndSkillNameIn(lowercaseApplicantSkillNames);

            return recommendedJobs.size();
        } catch (Exception e) {
        	 e.printStackTrace(); 
            // Handle exceptions as needed
            throw new CustomException("Error while counting recommended jobs for the applicant", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}








