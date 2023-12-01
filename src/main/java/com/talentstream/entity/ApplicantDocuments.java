package com.talentstream.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ApplicantDocuments {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Lob
	    @Column(name = "image", columnDefinition = "BLOB")
	    private byte[] image;

	    @Lob
	    @Column(name = "resume", columnDefinition = "BLOB")
	    private byte[] resume;
}
