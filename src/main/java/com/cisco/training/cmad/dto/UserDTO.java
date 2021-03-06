package com.cisco.training.cmad.dto;

import java.io.Serializable;

public class UserDTO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String userName;
    private String first;
    private String last;
    private String email;
private String siteId;
    private String password;
    private String companyId;
    private String deptId;
    private String companyName;
    private String subdomain;
    private String deptName;
    private Boolean isCompany;
    
public String getDeptName() {
return deptName;
}

public void setDeptName(String deptName) {
this.deptName = deptName;
}

public Boolean getIsCompany() {
return isCompany;
}

public void setIsCompany(Boolean isCompany) {
this.isCompany = isCompany;
}

public String getCompanyId() {
return companyId;
}
public void setCompanyId(String companyId) {
this.companyId = companyId;
}
public String getDeptId() {
return deptId;
}
public void setDeptId(String deptId) {
this.deptId = deptId;
}
public String getCompanyName() {
return companyName;
}
public void setCompanyName(String companyName) {
this.companyName = companyName;
}
public String getSubdomain() {
return subdomain;
}
public void setSubdomain(String subdomain) {
this.subdomain = subdomain;
}
public String getId() {
return id;
}
public void setId(String id) {
this.id = id;
}
public String getPassword() {
return password;
}
public void setPassword(String password) {
this.password = password;
}
public String getUserName() {
return userName;
}
public void setUserName(String userName) {
this.userName = userName;
}
public String getFirst() {
return first;
}
public void setFirst(String first) {
this.first = first;
}
public String getLast() {
return last;
}
public void setLast(String last) {
this.last = last;
}
public String getEmail() {
return email;
}
public void setEmail(String email) {
this.email = email;
}

public String getSiteId() {
return siteId;
}
public void setSiteId(String siteId) {
this.siteId = siteId;
}

@Override
public String toString() {
return "UserDTO [id=" + id + ", userName=" + userName + ", first="
+ first + ", last=" + last + "]";
}
    
}
