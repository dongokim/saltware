package com.saltware.enface.tool.service;

import java.io.Serializable;

public class SimpleUserVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int principal_id;
	private String principal_tpye;
	private String principal_name;
	
	private String short_path;
	
	private String userId;
	private String userGroup;
	private String userName;
	
	private int group_id;
	
	public SimpleUserVO() {
		// TODO Auto-generated constructor stub
	}

	public int getPrincipal_id() {
		return principal_id;
	}

	public void setPrincipal_id(int principal_id) {
		this.principal_id = principal_id;
	}

	public String getPrincipal_tpye() {
		return principal_tpye;
	}

	public void setPrincipal_tpye(String principal_tpye) {
		this.principal_tpye = principal_tpye;
	}

	public String getPrincipal_name() {
		if(principal_name == null){
			principal_name = "";
		}
		return principal_name;
	}

	public void setPrincipal_name(String principal_name) {
		this.principal_name = principal_name;
	}

	public String getShort_path() {
		if(short_path == null){
			short_path = "";
		}
		return short_path;
	}

	public void setShort_path(String short_path) {
		this.short_path = short_path;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	
	public String toString() {
		return short_path + ":" + principal_name + ":" + group_id;
	}
}
