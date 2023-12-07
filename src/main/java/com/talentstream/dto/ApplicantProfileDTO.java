package com.talentstream.dto;


import java.util.List;
import java.util.Set;
import com.talentstream.entity.BasicDetails;
import com.talentstream.entity.ExperienceDetails;
import com.talentstream.entity.GraduationDetails;
import com.talentstream.entity.IntermediateDetails;
import com.talentstream.entity.XClassDetails;
import com.talentstream.entity.ApplicantSkills;

public class ApplicantProfileDTO {
	private BasicDetails basicDetails;
    private XClassDetails xClassDetails;
    private IntermediateDetails intermediateDetails;
    private GraduationDetails graduationDetails;
    private Set<ApplicantSkills> skillsRequired;
    private List<ExperienceDetails> experienceDetails;  
    private String roles;
	public BasicDetails getBasicDetails() {
		return basicDetails;
	}
	public void setBasicDetails(BasicDetails basicDetails) {
		this.basicDetails = basicDetails;
	}
	public XClassDetails getxClassDetails() {
		return xClassDetails;
	}
	public void setxClassDetails(XClassDetails xClassDetails) {
		this.xClassDetails = xClassDetails;
	}
	public IntermediateDetails getIntermediateDetails() {
		return intermediateDetails;
	}
	public void setIntermediateDetails(IntermediateDetails intermediateDetails) {
		this.intermediateDetails = intermediateDetails;
	}
	public GraduationDetails getGraduationDetails() {
		return graduationDetails;
	}
	public void setGraduationDetails(GraduationDetails graduationDetails) {
		this.graduationDetails = graduationDetails;
	}
	public Set<ApplicantSkills> getSkillsRequired() {
		return skillsRequired;
	}
	public void setSkillsRequired(Set<ApplicantSkills> skillsRequired) {
		this.skillsRequired = skillsRequired;
	}
	public List<ExperienceDetails> getExperienceDetails() {
		return experienceDetails;
	}
	public void setExperienceDetails(List<ExperienceDetails> experienceDetails) {
		this.experienceDetails = experienceDetails;
	}
			
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}   
    
}
