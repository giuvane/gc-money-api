package br.edu.utfpr.gcmoney.api.model.sr;

import java.time.LocalDate;

public class Person {

	private Double id;
	private String name;
	private Boolean admin;
	private String mail;
	private String phone;
	private String createdAt;
	private String expireAt;
	private LocalDate birth;
	public Double getId() {
		return id;
	}
	public void setId(Double id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getExpireAt() {
		return expireAt;
	}
	public void setExpireAt(String expireAt) {
		this.expireAt = expireAt;
	}
	public LocalDate getBirth() {
		return birth;
	}
	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}
	
}
