package net.nutrima.nutrimaprotogui;

public class LambdaRequest {
    private String term;
	private double longitude;
	private double latitude;
	private String city;

	public LambdaRequest(String term, double longitude, double latitude, String city) {
        this.term = term;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
	}

    public LambdaRequest() {
    }

    public String getTerm() {
        return term;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getCity() {
        return city;
    }
}