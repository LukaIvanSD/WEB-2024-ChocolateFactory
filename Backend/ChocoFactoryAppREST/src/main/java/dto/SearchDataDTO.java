package dto;

import java.util.List;

import beans.Chocolate;

public class SearchDataDTO {
	private List<String> selectedFactories;
    private List<Chocolate> selectedChocolates;
    private List<String> locations;
    private double rating;

    public List<String> getSelectedFactories() {
        return selectedFactories;
    }

    public void setSelectedFactories(List<String> selectedFactories) {
        this.selectedFactories = selectedFactories;
    }

    public List<Chocolate> getSelectedChocolates() {
        return selectedChocolates;
    }

    public void setSelectedChocolates(List<Chocolate> selectedChocolates) {
        this.selectedChocolates = selectedChocolates;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
