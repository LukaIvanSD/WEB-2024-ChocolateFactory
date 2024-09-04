package beans;

import java.util.List;

public class Cart {
	private int id;
	private int customerId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cart(int id, int customerId) {
		super();
		this.id = id;
		this.customerId = customerId;
	}
	public Cart( int customerId) {
		super();
		this.customerId = customerId;
	}
	
	
}
	
