package net.nutrima.nutrimaprotogui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nutrima.aws.RestaurantMenuItem;

public class LambdaRespAll implements Serializable {
	private  Map<Business, List<RestaurantMenuItem>> restaurantFullMenuMap;
	private  Map<Business, List<RestaurantMenuItem>> restaurantFullMenuMapFiltered;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFiltered;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredEntree;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredAppetizer;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredPizza;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredBaked;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredBeverage;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredSalad;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredSandwitch;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredFastFood;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredPasta;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredSoup;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredTopping;
	private  ArrayList<RestaurantMenuItem> plateNamesPMFilteredDessert;

	public LambdaRespAll(Globals g) {
		this.restaurantFullMenuMap = g.getRestaurantFullMenuMap();
		this.restaurantFullMenuMapFiltered = g.getRestaurantFullMenuMapFiltered();
		this.plateNamesPMFiltered = g.getPlateNamesPMFiltered();
		this.plateNamesPMFilteredEntree = g.getPlateNamesPMFilteredEntree();
		this.plateNamesPMFilteredAppetizer = g.getPlateNamesPMFilteredAppetizer();
		this.plateNamesPMFilteredPizza = g.getPlateNamesPMFilteredPizza();
		this.plateNamesPMFilteredBaked = g.getPlateNamesPMFilteredBaked();
		this.plateNamesPMFilteredBeverage = g.getPlateNamesPMFilteredBeverage();
		this.plateNamesPMFilteredSalad = g.getPlateNamesPMFilteredSalad();
		this.plateNamesPMFilteredSandwitch = g.getPlateNamesPMFilteredSandwitch();
		this.plateNamesPMFilteredFastFood = g.getPlateNamesPMFilteredFastFood();
		this.plateNamesPMFilteredPasta = g.getPlateNamesPMFilteredPasta();
		this.plateNamesPMFilteredSoup = g.getPlateNamesPMFilteredSoup();
		this.plateNamesPMFilteredTopping = g.getPlateNamesPMFilteredTopping();
		this.plateNamesPMFilteredDessert = g.getPlateNamesPMFilteredDessert();
	}

	public LambdaRespAll() {

	}

	public Map<Business, List<RestaurantMenuItem>> getRestaurantFullMenuMap() {
		return restaurantFullMenuMap;
	}

	public void setRestaurantFullMenuMap(
			Map<Business, List<RestaurantMenuItem>> restaurantFullMenuMap) {
		this.restaurantFullMenuMap = restaurantFullMenuMap;
	}

	public Map<Business, List<RestaurantMenuItem>> getRestaurantFullMenuMapFiltered() {
		return restaurantFullMenuMapFiltered;
	}

	public void setRestaurantFullMenuMapFiltered(
			Map<Business, List<RestaurantMenuItem>> restaurantFullMenuMapFiltered) {
		this.restaurantFullMenuMapFiltered = restaurantFullMenuMapFiltered;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFiltered() {
		return plateNamesPMFiltered;
	}

	public void setPlateNamesPMFiltered(
			ArrayList<RestaurantMenuItem> plateNamesPMFiltered) {
		this.plateNamesPMFiltered = plateNamesPMFiltered;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredEntree() {
		return plateNamesPMFilteredEntree;
	}

	public void setPlateNamesPMFilteredEntree(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredEntree) {
		this.plateNamesPMFilteredEntree = plateNamesPMFilteredEntree;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredAppetizer() {
		return plateNamesPMFilteredAppetizer;
	}

	public void setPlateNamesPMFilteredAppetizer(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredAppetizer) {
		this.plateNamesPMFilteredAppetizer = plateNamesPMFilteredAppetizer;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredPizza() {
		return plateNamesPMFilteredPizza;
	}

	public void setPlateNamesPMFilteredPizza(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredPizza) {
		this.plateNamesPMFilteredPizza = plateNamesPMFilteredPizza;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredBaked() {
		return plateNamesPMFilteredBaked;
	}

	public void setPlateNamesPMFilteredBaked(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredBaked) {
		this.plateNamesPMFilteredBaked = plateNamesPMFilteredBaked;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredBeverage() {
		return plateNamesPMFilteredBeverage;
	}

	public void setPlateNamesPMFilteredBeverage(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredBeverage) {
		this.plateNamesPMFilteredBeverage = plateNamesPMFilteredBeverage;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredSalad() {
		return plateNamesPMFilteredSalad;
	}

	public void setPlateNamesPMFilteredSalad(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredSalad) {
		this.plateNamesPMFilteredSalad = plateNamesPMFilteredSalad;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredSandwitch() {
		return plateNamesPMFilteredSandwitch;
	}

	public void setPlateNamesPMFilteredSandwitch(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredSandwitch) {
		this.plateNamesPMFilteredSandwitch = plateNamesPMFilteredSandwitch;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredFastFood() {
		return plateNamesPMFilteredFastFood;
	}

	public void setPlateNamesPMFilteredFastFood(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredFastFood) {
		this.plateNamesPMFilteredFastFood = plateNamesPMFilteredFastFood;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredPasta() {
		return plateNamesPMFilteredPasta;
	}

	public void setPlateNamesPMFilteredPasta(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredPasta) {
		this.plateNamesPMFilteredPasta = plateNamesPMFilteredPasta;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredSoup() {
		return plateNamesPMFilteredSoup;
	}

	public void setPlateNamesPMFilteredSoup(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredSoup) {
		this.plateNamesPMFilteredSoup = plateNamesPMFilteredSoup;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredTopping() {
		return plateNamesPMFilteredTopping;
	}

	public void setPlateNamesPMFilteredTopping(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredTopping) {
		this.plateNamesPMFilteredTopping = plateNamesPMFilteredTopping;
	}

	public ArrayList<RestaurantMenuItem> getPlateNamesPMFilteredDessert() {
		return plateNamesPMFilteredDessert;
	}

	public void setPlateNamesPMFilteredDessert(
			ArrayList<RestaurantMenuItem> plateNamesPMFilteredDessert) {
		this.plateNamesPMFilteredDessert = plateNamesPMFilteredDessert;
	}



}