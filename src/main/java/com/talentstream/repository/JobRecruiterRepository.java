package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentstream.entity.JobRecruiter;
@Repository

public interface JobRecruiterRepository extends JpaRepository<JobRecruiter, Long> {

	JobRecruiter findByEmail(String email);
    // Additional query methods can be defined here if needed

	boolean existsByEmail(String email);	
		JobRecruiter findByRecruiterId(Long id);
	 @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CompanyProfile c WHERE c.jobRecruiter.recruiterId = :recruiterId")
	    boolean existsByJobRecruiterId(@Param("recruiterId") Long recruiterId);
}

