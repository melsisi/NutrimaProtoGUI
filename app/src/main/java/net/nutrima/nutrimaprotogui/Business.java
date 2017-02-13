package net.nutrima.nutrimaprotogui;

/**
 * Created by melsisi on 9/10/2016.
 */
public class Business {
    private String name;
    private LatLng coordinates;
    private String phone;
    private String address;
    private String imageUrl;
    private String ratingImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRatingImageUrl() {
        return ratingImageUrl;
    }

    public void setRatingImageUrl(String ratingImageUrl) {
        this.ratingImageUrl = ratingImageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Business other = (Business) obj;
        if(!other.name.equals(this.name))
            return false;
        if(!other.address.equals(this.address))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + address.hashCode();
    }

}
