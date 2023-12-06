package com.talentstream.dto;

import java.util.Set;
public class JobDTO {
	 private Long id;
	    private String jobTitle;
	    private int minimumExperience;
	    private int maximumExperience;
	    private double maxSalary;
	    private double minSalary;
	    private String location;
	    private String employeeType;
	    private String industryType;
	    private String minimumQualification;
	    private String specialization;
	    private Set<RecuriterSkillsDTO> skillsRequired;
	    private String jobHighlights;
	    private String description;
	    private String Companyname;
		
		public String getCompanyname() {
			return Companyname;
		}
		public void setCompanyname(String companyname) {
			Companyname = companyname;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getJobTitle() {
			return jobTitle;
		}
		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}
		public int getMinimumExperience() {
			return minimumExperience;
		}
		public void setMinimumExperience(int minimumExperience) {
			this.minimumExperience = minimumExperience;
		}
		public int getMaximumExperience() {
			return maximumExperience;
		}
		public void setMaximumExperience(int maximumExperience) {
			this.maximumExperience = maximumExperience;
		}
		public double getMaxSalary() {
			return maxSalary;
		}
		public void setMaxSalary(double maxSalary) {
			this.maxSalary = maxSalary;
		}
		public double getMinSalary() {
			return minSalary;
		}
		public void setMinSalary(double minSalary) {
			this.minSalary = minSalary;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getEmployeeType() {
			return employeeType;
		}
		public void setEmployeeType(String employeeType) {
			this.employeeType = employeeType;
		}
		public String getIndustryType() {
			return industryType;
		}
		public void setIndustryType(String industryType) {
			this.industryType = industryType;
		}
		public String getMinimumQualification() {
			return minimumQualification;
		}
		public void setMinimumQualification(String minimumQualification) {
			this.minimumQualification = minimumQualification;
		}
		public String getSpecialization() {
			return specialization;
		}
		public void setSpecialization(String specialization) {
			this.specialization = specialization;
		}
		public Set<RecuriterSkillsDTO> getSkillsRequired() {
			return skillsRequired;
		}
		public void setSkillsRequired(Set<RecuriterSkillsDTO> skillsDTOList) {
			this.skillsRequired = skillsDTOList;
		}
		public String getJobHighlights() {
			return jobHighlights;
		}
		public void setJobHighlights(String jobHighlights) {
			this.jobHighlights = jobHighlights;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	    
	    
}
