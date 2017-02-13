package net.nutrima.nutrimaprotogui;

import net.nutrima.aws.RestaurantMenuItem;

public class LambdaRespMenues {
	
	RestaurantMenuItem[] fullMenuList;
	RestaurantMenuItem[] filteredMenuList;
	
	public LambdaRespMenues(RestaurantMenuItem[] fullMenuList,
			RestaurantMenuItem[] filteredMenuList) {
		this.fullMenuList = fullMenuList;
		this.filteredMenuList = filteredMenuList;
	}
	
	public LambdaRespMenues() {
		
	}

	public RestaurantMenuItem[] getFullMenuList() {
		return fullMenuList;
	}

	public void setFullMenuList(RestaurantMenuItem[] fullMenuList) {
		this.fullMenuList = fullMenuList;
	}

	public RestaurantMenuItem[] getFilteredMenuList() {
		return filteredMenuList;
	}

	public void setFilteredMenuList(RestaurantMenuItem[] filteredMenuList) {
		this.filteredMenuList = filteredMenuList;
	}
	
	
}