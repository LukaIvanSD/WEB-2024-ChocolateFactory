package beans;

import beans.CartArticle.DeletionStatus;


public class Chocolate {

	private int id;
	private String name;
	private String category;
	private String type;
	private double weight;
	private String description;
	private String imagePath;
	private int factoryCreatorId;
	private DeletionStatus deletionStatus;
	
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}
	public Chocolate() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Chocolate(int id, String name, String category, String type, double weight,
			String description, String imagePath, String deletionStatus,int factoryCreatorId) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.type = type;
		this.weight = weight;
		this.description = description;
		this.imagePath = imagePath;
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
		this.factoryCreatorId=factoryCreatorId;
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
	public int getFactoryCreatorId() {
		return factoryCreatorId;
	}
	public void setFactoryCreatorId(int factoryCreatorId) {
		this.factoryCreatorId = factoryCreatorId;
	}
	
	
}
