package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantImage;
import com.talentstream.entity.ApplicantResume;

public interface ApplicantResumeRepository extends JpaRepository<ApplicantResume, Long> {

	boolean existsByApplicant(Applicant applicant);

	ApplicantResume findById(Applicant applicant);

}
