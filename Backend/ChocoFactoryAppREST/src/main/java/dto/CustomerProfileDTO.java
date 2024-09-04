package dto;

import beans.Customer;
import beans.CustomerType;
import beans.User;

public class CustomerProfileDTO {

	private User user;
	private Customer customer;
	private CustomerType customerType;
	public CustomerProfileDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerProfileDTO(User user, Customer customer, CustomerType customerType) {
		super();
		this.user = user;
		this.customer = customer;
		this.customerType = customerType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public CustomerType getCustomerType() {
		return customerType;
	}
	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}
	
	
}
