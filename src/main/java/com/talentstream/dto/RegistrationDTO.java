package com.talentstream.dto;

import lombok.Data;


public class RegistrationDTO {
	 private String name;
	    private String email;
	    private String mobilenumber;
	    private String password;

	private String appicantStatus="Active";
	
	    public String getAppicantStatus() {
			return appicantStatus;
		}
		public void setAppicantStatus(String appicantStatus) {
			this.appicantStatus = appicantStatus;
		}
	
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
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	    
}

