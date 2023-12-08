package com.talentstream.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;



import lombok.Getter;
import lombok.Setter;
 
@Getter
@Setter
@Entity
public class Alerts {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alerts_id")
	private long alertsId;
//	@ManyToOne
//	@JoinColumn(name = "applyJobId")
//	private ApplyJob applyJob;
	private String companyName;
	private String status;
	@Column(columnDefinition = "DATE")
	private LocalDate changeDate;
	@ManyToOne
	@JoinColumn(name = "applicant_id")
	private Applicant applicant;
}