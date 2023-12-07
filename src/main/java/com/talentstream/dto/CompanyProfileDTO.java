package com.talentstream.dto;

import java.util.List;

public class CompanyProfileDTO {
	private Long id;
    private String companyName;
    private String website;
    private String phoneNumber;
    private String email;
    private String headOffice;
    private byte[] logo;
    private List<String> socialProfiles;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHeadOffice() {
		return headOffice;
	}
	public void setHeadOffice(String headOffice) {
		this.headOffice = headOffice;
	}
	public List<String> getSocialProfiles() {
		return socialProfiles;
	}
	public void setSocialProfiles(List<String> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
  	
    
}