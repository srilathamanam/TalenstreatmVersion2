package com.talentstream.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.talentstream.dto.JobDTO;
import com.talentstream.entity.Job;
import com.talentstream.repository.JobRepository;
import com.talentstream.exception.CustomException;
@Service
public class ViewJobService {
	@Autowired
    private JobRepository jobRepository;
  
public ResponseEntity<?> getJobDetailsForApplicant(Long jobId) {
	
    final ModelMapper modelMapper = new ModelMapper();
	Job job = jobRepository.findById(jobId).orElse(null);

	if (job != null) {
        JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
        return ResponseEntity.ok(jobDTO);
    } else {
        throw new CustomException("Job with ID " + jobId + " not found.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}
