package com.talentstream.entity;

public class PasswordRequest {
	public PasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String newpassword;
    private String oldpassword;
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public String getOldpassword() {
		return oldpassword;
	}
	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}
}