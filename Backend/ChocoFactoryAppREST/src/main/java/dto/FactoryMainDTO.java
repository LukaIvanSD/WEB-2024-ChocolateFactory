package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beans.Chocolate;
import beans.ChocolateItem;
import beans.Factory;
import beans.Location;

public class FactoryMainDTO {
private Factory factory;
private Location location;

public FactoryMainDTO() {}
public FactoryMainDTO(Factory factory, Location location) {
	super();
	this.factory = factory;
	this.location = location;

}

public Factory getFactory() {
	return factory;
}
public void setFactory(Factory factory) {
	this.factory = factory;
}
public Location getLocation() {
	return location;
}
public void setLocation(Location location) {
	this.location = location;
}


}
