package beans;

enum Type{
	Gold,
	Silver,
	Bronze,
	Basic
}
public class CustomerType {
	private int Id;
	private Type type;
	private double discount;
	private double pointsForUpgrade;
	
	public CustomerType() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public CustomerType(int id, String type, double discount, double pointsForUpgrade) {
		super();
		Id = id;
		this.type = Type.valueOf(type);
		this.discount = discount;
		this.pointsForUpgrade = pointsForUpgrade;
	}



	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getPointsForUpgrade() {
		return pointsForUpgrade;
	}
	public void setPointsForUpgrade(double pointsForUpgrade) {
		this.pointsForUpgrade = pointsForUpgrade;
	}
	
}
