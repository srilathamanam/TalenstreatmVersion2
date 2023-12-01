package com.talentstream.entity;

import java.util.Date;

public class AppliedApplicantInfoDTO {
	private Long applyjobid;
	  private String name;
	    private String email;
	    private String mobilenumber;
	    private String jobTitle;
	    private String applicantStatus;
	    private int minimumExperience;
	    private String skillName;
	    private String minimumQualification;
	    private String location;
	   
	    
	    public AppliedApplicantInfoDTO() {}

//	    public AppliedApplicantInfoDTO(String name, String email, String mobile, String jobTitle, String applicantStatus, Date scheduleInterviewDate, int minimumExperience, String skillName, String minimumQualification, String location, String recruiterName) {
//	        this.name = name;
//	        this.email = email;
//	        this.mobilenumber = mobile;
//	        this.jobTitle = jobTitle;
//	        this.applicantStatus = applicantStatus;
//	        this.scheduleInterviewDate = scheduleInterviewDate;
//	        this.minimumExperience = minimumExperience;
//	        this.skillName = skillName;
//	        this.minimumQualification = minimumQualification;
//	        this.location = location;
//	        this.recruiterName = recruiterName;
//	    }

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMobilenumber() {
			return mobilenumber;
		}

		public void setMobilenumber(String mobilenumber) {
			this.mobilenumber = mobilenumber;
		}

		public String getJobTitle() {
			return jobTitle;
		}

		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}

		public String getApplicantStatus() {
			return applicantStatus;
		}

		public void setApplicantStatus(String applicantStatus) {
			this.applicantStatus = applicantStatus;
		}

		

		public int getMinimumExperience() {
			return minimumExperience;
		}

		public void setMinimumExperience(int minimumExperience) {
			this.minimumExperience = minimumExperience;
		}

		public String getSkillName() {
			return skillName;
		}

		public void setSkillName(String skillName) {
			this.skillName = skillName;
		}

		public String getMinimumQualification() {
			return minimumQualification;
		}

		public void setMinimumQualification(String minimumQualification) {
			this.minimumQualification = minimumQualification;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public Long getApplyjobid() {
			return applyjobid;
		}

		public void setApplyjobid(Long applyjobid) {
			this.applyjobid = applyjobid;
		}
}
		

		