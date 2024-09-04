package dto;

import beans.Chocolate;

public class ChocolateEditRequestDTO {
	private Chocolate chocolateEdit;
	private ChocolateDTO item;
	private Double  price;
	public ChocolateEditRequestDTO() {
		
	}
	
	public ChocolateDTO getItem() {
		return item;
	}
	public void setItem(ChocolateDTO item) {
		this.item = item;
	}
	public Chocolate getChocolateEdit() {
		return chocolateEdit;
	}
	public void setChocolateEdit(Chocolate chocolateEdit) {
		this.chocolateEdit = chocolateEdit;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double  price) {
		this.price = price;
	}
	public Chocolate getUpdatedChocolate()
	{
		chocolateEdit.setId(item.getChocolate().getId());
		chocolateEdit.setDeletionStatus(item.getChocolate().getDeletionStatus());
		chocolateEdit.setFactoryCreatorId(item.getChocolate().getFactoryCreatorId());
		if(chocolateEdit.getCategory().isBlank())
			chocolateEdit.setCategory(item.getChocolate().getCategory());
		if(chocolateEdit.getName().isBlank())
			chocolateEdit.setName(item.getChocolate().getName());
		if(chocolateEdit.getDescription().isBlank())
			chocolateEdit.setDescription(item.getChocolate().getDescription());
		if(chocolateEdit.getImagePath().isBlank())
			chocolateEdit.setImagePath(item.getChocolate().getImagePath());
		if(chocolateEdit.getType().isBlank())
			chocolateEdit.setType(item.getChocolate().getType());
		if(chocolateEdit.getWeight()<=0)
			chocolateEdit.setWeight(item.getChocolate().getWeight());
		return chocolateEdit;
	}
}
