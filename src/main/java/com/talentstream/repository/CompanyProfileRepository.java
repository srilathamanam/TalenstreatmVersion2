package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentstream.dto.CompanyProfileDTO;
import com.talentstream.entity.CompanyProfile;
import com.talentstream.entity.JobRecruiter;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

	JobRecruiter findByEmail(String username); 
	
	List<CompanyProfile> findByJobRecruiter(JobRecruiter jobRecruiter);
	CompanyProfile save(CompanyProfileDTO companyProfileDTO);
}
