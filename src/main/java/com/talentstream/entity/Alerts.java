package com.talentstream.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity
public class Alerts {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alerts_id")
	private long alertsId;

	private String companyName;
	private String status;
	@Column(columnDefinition = "DATE")
	private LocalDate changeDate;
	@ManyToOne
	@JoinColumn(name = "applicant_id")
	private Applicant applicant;
	public long getAlertsId() {
		return alertsId;
	}
	public void setAlertsId(long alertsId) {
		this.alertsId = alertsId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(LocalDate changeDate) {
		this.changeDate = changeDate;
	}
	public Applicant getApplicant() {
		return applicant;
	}
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}
	
	
}