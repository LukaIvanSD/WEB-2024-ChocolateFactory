package dto;

import java.util.ArrayList;
import java.util.List;

public class FilterRequestDTO {
	private List<FactoryDTO>factories;
	private List<String>types;
	private List<String>categories;
	private Boolean onlyOpen;
	public FilterRequestDTO() {}
	
	public FilterRequestDTO(List<FactoryDTO> factories, List<String> types, List<String> categories,Boolean onlyOpen) {
		super();
		this.factories = factories;
		this.types = types;
		this.categories = categories;
		this.setOnlyOpen(onlyOpen);
	}

	public List<FactoryDTO> getFactories() {
		return factories;
	}
	public void setFactories(List<FactoryDTO> factories) {
		this.factories = factories;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public Boolean getOnlyOpen() {
		// TODO Auto-generated method stub
		return onlyOpen;
	}

	public void setOnlyOpen(Boolean onlyOpen) {
		this.onlyOpen = onlyOpen;
	}
	
}
