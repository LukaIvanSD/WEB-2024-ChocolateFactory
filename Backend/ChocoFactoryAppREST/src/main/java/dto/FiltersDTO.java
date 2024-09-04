package dto;

import java.util.ArrayList;
import java.util.List;

public class FiltersDTO {
private List<String>categories=new ArrayList<>();
private List<String>types=new ArrayList<>();

public FiltersDTO(){
}
public FiltersDTO(List<String> categories, List<String> types) {
	super();
	this.categories = categories;
	this.types = types;
}
public List<String> getCategories() {
	return categories;
}
public void setCategories(List<String> categories) {
	this.categories = categories;
}
public List<String> getTypes() {
	return types;
}
public void setTypes(List<String> types) {
	this.types = types;
}

}
