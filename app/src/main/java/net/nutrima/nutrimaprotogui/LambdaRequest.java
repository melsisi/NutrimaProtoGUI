package net.nutrima.nutrimaprotogui;

import net.nutrima.engine.CurrentMetrics;
import net.nutrima.engine.UserProfile;

public class LambdaRequest {
    private String term;
	private double longitude;
	private double latitude;
	private String city;
    private UserProfile userProfile;
    private CurrentMetrics currentMetrics;

	public LambdaRequest(String term,
                         double longitude,
                         double latitude,
                         String city,
                         UserProfile uP,
                         CurrentMetrics cM) {
        this.term = term;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
        this.userProfile = uP;
        this.currentMetrics = cM;
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

    public CurrentMetrics getCurrentMetrics() { return currentMetrics; }

    public UserProfile getUserProfile() { return userProfile; }
}