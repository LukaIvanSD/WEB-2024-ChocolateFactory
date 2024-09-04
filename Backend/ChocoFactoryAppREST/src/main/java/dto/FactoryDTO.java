package dto;

import beans.Factory;
import beans.Location;

public class FactoryDTO {
	private int id;
	private String name;
	private String address;
	private String city;
	private String postCode;
	private double longitude;
	private double latitude;
	private double rating;
	private String logoPath;
	
	public FactoryDTO() {}

	public FactoryDTO(int id, String name, String address, String city, double longitude, double latitude,String postCode) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.longitude = longitude;
		this.latitude = latitude;
		this.setPostCode(postCode);
	}
	public FactoryDTO(int id, String name, double longitude, double latitude) {
		super();
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public FactoryDTO(Factory factory,Location location) {
		this.id = factory.getId();
		this.name = factory.getName();
		this.address=location.getAddress();
		this.postCode=location.getPostcode();
		this.city=location.getCity();
		this.longitude = location.getLongitude();
		this.latitude = location.getLatitude();
		this.logoPath=factory.getLogoPath();
		this.rating=factory.getRating();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	
}
