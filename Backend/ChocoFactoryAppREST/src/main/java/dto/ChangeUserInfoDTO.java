package dto;

import java.time.LocalDate;

import beans.User.Sex;

public class ChangeUserInfoDTO {

	private String firstName;
	private String lastName;
	private String password;
	private Sex sex;
	private LocalDate dateOfBirth;	
	
	public ChangeUserInfoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ChangeUserInfoDTO(String firstName, String lastName, String password, Sex sex,
			LocalDate dateOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.sex = sex;
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	
}
