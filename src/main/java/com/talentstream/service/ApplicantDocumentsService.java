package com.talentstream.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.talentstream.entity.ApplicantDocuments;
import com.talentstream.repository.ApplicantDocumentsRepository;

public class ApplicantDocumentsService {
	private final ApplicantDocumentsRepository documentsRepository;

    @Autowired
    public ApplicantDocumentsService(ApplicantDocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    public void saveDocuments(ApplicantDocuments documents) {
        documentsRepository.save(documents);
    }
}
