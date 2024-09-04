package beans;

import java.io.Serializable;
import java.time.LocalDate;

import beans.CartArticle.DeletionStatus;

public class User implements Serializable {
	public enum UserType{
		Administrator,
		Manager,
		Worker,
		Customer
	}
	public enum Sex {
		  Male,
		  Female
	}
	private int id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Sex sex;
	private LocalDate dateOfBirth;
	private UserType userType;
	private DeletionStatus deletionStatus;
	public User() {
	}

	public User(int id,String firstName, String lastName, String username, String password, String sex, LocalDate dateOfBirth,String userType,String deletionStatus) {
		super();
		this.id=id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = Sex.valueOf(sex);
		this.username = username;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.userType=UserType.valueOf(userType);
		this.deletionStatus=DeletionStatus.valueOf(deletionStatus);
	}
	
	public Boolean IsActive()
	{
		return deletionStatus==DeletionStatus.Active;
	}
	
	public Boolean Delete() {
		if(deletionStatus==DeletionStatus.Deleted)
			return false;
		deletionStatus=DeletionStatus.Deleted;
		return true;
	}
	
	public Boolean IsAdmin()
	{
		return userType==UserType.Administrator;
	}
	public Boolean IsManager()
	{
		return userType==UserType.Manager;
	}
	public Boolean IsCustomer()
	{
		return userType==UserType.Customer;
	}
	public Boolean IsWorker()
	{
		return userType==UserType.Worker;
	}
	
	



	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus=deletionStatus;
	}
	public DeletionStatus getDeletionStatus( ) {
		return deletionStatus;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + "]";
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	private static final long serialVersionUID = 6640936480584723344L;
}
