package com.talentstream.dto;

import java.util.List;

public class JobRecruiterDTO {
	 private Long recruiterId;
	    private String companyname;
	    private String mobilenumber;
	    private String email;
	    private String password;
	   // private List<JobDTO> jobs;
	    private String roles = "ROLE_JOBRECRUITER";
	   // private List<TeamMemberDTO> teamMembers;
   
	    public JobRecruiterDTO() {
	    }

	    public JobRecruiterDTO(Long recruiterId, String companyname, String mobilenumber, String email, String password/*,List<JobDTO> jobs, String roles, List<TeamMemberDTO> teamMembers*/) {
	        this.recruiterId = recruiterId;
	        this.companyname = companyname;
	        this.mobilenumber = mobilenumber;
	        this.email = email;
	        this.password = password;
	       // this.jobs = jobs;
	        this.roles = roles;
	       // this.teamMembers = teamMembers;
	    }

		public Long getRecruiterId() {
			return recruiterId;
		}

		public void setRecruiterId(Long recruiterId) {
			this.recruiterId = recruiterId;
		}

		public String getCompanyname() {
			return companyname;
		}

		public void setCompanyname(String companyname) {
			this.companyname = companyname;
		}

		public String getMobilenumber() {
			return mobilenumber;
		}

		public void setMobilenumber(String mobilenumber) {
			this.mobilenumber = mobilenumber;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRoles() {
			return roles;
		}

		public void setRoles(String roles) {
			this.roles = roles;
		}

}
