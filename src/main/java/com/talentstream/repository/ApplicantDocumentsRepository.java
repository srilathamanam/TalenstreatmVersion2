package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentstream.entity.ApplicantDocuments;

@Repository
public interface ApplicantDocumentsRepository extends JpaRepository<ApplicantDocuments, Long> {
}