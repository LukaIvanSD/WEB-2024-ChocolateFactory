package dto;

import beans.Factory;
import beans.Location;
import beans.Manager;
import beans.User;

public class FactoryManagerDTO {
private User user;
private Factory factory;
private Location location;
private Manager manager;
public FactoryManagerDTO(){}

public Location getLocation() {
	return location;
}
public void setLocation(Location location) {
	this.location = location;
}
public Manager getManager() {
	return manager;
}
public void setManager(Manager manager) {
	this.manager = manager;
}
public Factory getFactory() {
	return factory;
}
public void setFactory(Factory factory) {
	this.factory = factory;
}
public FactoryManagerDTO(Manager manager, Factory factory,User user,Location location) {
	super();
	this.manager = manager;
	this.factory = factory;
	this.location=location;
	this.user=user;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}


}
