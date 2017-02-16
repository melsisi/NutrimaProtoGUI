package net.nutrima.nutrimaprotogui;

import net.nutrima.engine.CurrentMetrics;
import net.nutrima.engine.UserProfile;

public class LambdaRequest2 {
	private String restaurant;
    private UserProfile userProfile;
    private CurrentMetrics currentMetrics;
    
	public LambdaRequest2(String restaurant, UserProfile userProfile,
			CurrentMetrics currentMetrics) {
		super();
		this.restaurant = restaurant;
		this.userProfile = userProfile;
		this.currentMetrics = currentMetrics;
	}

	public LambdaRequest2(){
		
	}

	public String getRestaurant() {
		return restaurant;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public CurrentMetrics getCurrentMetrics() {
		return currentMetrics;
	}
	
	
}