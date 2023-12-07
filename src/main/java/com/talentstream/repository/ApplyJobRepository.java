package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.talentstream.entity.AppliedApplicantInfo;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.entity.Applicant;
@Repository
public interface ApplyJobRepository extends JpaRepository<ApplyJob, Long> {
	List<ApplyJob> findByApplicantId(long applicantId);

	@Query("SELECT NEW com.talentstream.entity.AppliedApplicantInfo(" +
		       " aj.applyjobid,a.name, a.email, a.mobilenumber, j.jobTitle, aj.applicantStatus, " +
		       " j.minimumExperience, s.skillName, " +
		       "j.minimumQualification, j.location) " +
		       "FROM ApplyJob aj " +
		       "INNER JOIN aj.applicant a " +
		       "INNER JOIN aj.job j " +
		       "INNER JOIN j.skillsRequired s " +
		       "INNER JOIN j.jobRecruiter r " +
		       "WHERE r.id = :jobRecruiterId")
		List<AppliedApplicantInfo> findAppliedApplicantsInfo(@Param("jobRecruiterId") long jobRecruiterId);
	
	boolean existsByApplicantAndJob(Applicant applicant, Job job);
	
	@Query("SELECT COUNT(ja) FROM ApplyJob ja WHERE ja.job.jobRecruiter.id = :recruiterId")
    long countJobApplicantsByRecruiterId(@Param("recruiterId") Long recruiterId);
	
	long countByApplicantStatus(String applicantStatus);
	
	long countByApplicantStatusIn(List<String> applicantStatusList);
	
	List<ApplyJob> findByJobId(Long jobId);
}