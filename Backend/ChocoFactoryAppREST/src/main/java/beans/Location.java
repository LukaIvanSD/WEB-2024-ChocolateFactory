package beans;

import beans.CartArticle.DeletionStatus;

public class Location {

	private int Id;
	private String postcode;
	private String city;
	private double latitude;
	private double longitude;
	private String address;
	private DeletionStatus deletionStatus;
	
	public Location() {
		
	}
	
	
	public Location(int id, double longitude, double latitude, String address, String deletionStatus,String postcode,String city) {
		super();
		Id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
		this.postcode=postcode;
		this.city=city;
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
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}


	public String getPostcode() {
		return postcode;
	}


	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}
}
