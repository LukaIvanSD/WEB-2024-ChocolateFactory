package beans;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import beans.CartArticle.DeletionStatus;

public class Factory {
	public enum FactoryStatus{
		Open,
		Closed
	}
	private int id;
	private String name;
	private LocalTime openFrom;
	private LocalTime openTo;
	private FactoryStatus status;
	private int locationId;
	private String logoPath;
	private double rating;
	private DeletionStatus deletionStatus;
	
	public Factory() {
		
	}
	
	public Factory(int id, String name, LocalTime openFrom, LocalTime openTo, int locationId, String logoPath,
			double rating, String deletionStatus) {
		super();
		this.id = id;
		this.name = name;
		this.openFrom = openFrom;
		this.openTo = openTo;
		this.locationId = locationId;
		this.logoPath = logoPath;
		this.rating = rating;
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
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
	public LocalTime getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(LocalTime openFrom) {
		this.openFrom = openFrom;
	}
	public LocalTime getOpenTo() {
		return openTo;
	}
	public void setOpenTo(LocalTime openTo) {
		this.openTo = openTo;
	}
	public FactoryStatus getStatus() { 
		  LocalTime now = LocalTime.now();
		  if (openTo.isBefore(openFrom)) {
			    if ((now.isAfter(openFrom) || now.equals(openFrom)) || (now.isBefore(openTo) || now.equals(openTo))) {
			        return FactoryStatus.Open;
			    }
			} else {
			    if ((now.isAfter(openFrom) && now.isBefore(openTo)) || openTo.equals(openFrom)) {
			        return FactoryStatus.Open;
			    }
			}
			return FactoryStatus.Closed;
	}
	public Boolean IsClosed()
	{
		return getStatus()==FactoryStatus.Closed;
	}
	public Boolean IsOpen()
	{
		return getStatus()==FactoryStatus.Open;
	}
	public void setStatus(FactoryStatus status) {
		this.status = status;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public String getLogoPath() {
		return logoPath;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}
}
