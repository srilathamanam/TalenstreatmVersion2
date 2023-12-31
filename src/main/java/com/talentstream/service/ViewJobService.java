package com.talentstream.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.talentstream.dto.JobDTO;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.repository.JobRepository;
import com.talentstream.exception.CustomException;
@Service
public class ViewJobService {
	@Autowired
    private JobRepository jobRepository;
	
	@Autowired
    private ApplyJobService applyJobService;
public ResponseEntity<?> getJobDetailsForApplicant(Long jobId) {
	
    final ModelMapper modelMapper = new ModelMapper();
	Job job = jobRepository.findById(jobId).orElse(null);
 
	if (job != null) {
        JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
        jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
        jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
        jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
        jobDTO.setEmail(job.getJobRecruiter().getEmail());
        jobDTO.setJobStatus(job.getJobStatus());
        return ResponseEntity.ok(jobDTO);
    } else {
        throw new CustomException("Job with ID " + jobId + " not found.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
public ResponseEntity<?> getJobDetailsForApplicant(Long jobId, Long applicantId) {

    final ModelMapper modelMapper = new ModelMapper();
    Job job = jobRepository.findById(jobId).orElse(null);

    if (job != null) {
    	JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
        jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
        jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
        jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
        jobDTO.setEmail(job.getJobRecruiter().getEmail());
        

        ApplyJob applyJob = applyJobService.getByJobAndApplicant(jobId, applicantId);
        if (applyJob != null) {
            jobDTO.setJobStatus("Already Applied");
        } else {
            jobDTO.setJobStatus("Apply now");
        }

        return ResponseEntity.ok(jobDTO);
    } else {
        throw new CustomException("Job with ID " + jobId + " not found.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

}
