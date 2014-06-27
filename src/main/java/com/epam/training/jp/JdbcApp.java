package com.epam.training.jp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.epam.training.jp.jdbc.excercises.domain.Address;
import com.epam.training.jp.jdbc.excercises.domain.Food;
import com.epam.training.jp.jdbc.excercises.helper.DatabaseCreator;
import com.epam.training.jp.jdbc.excercises.service.RestaurantService;
import com.epam.training.jp.jdbc.excercises.spring.SpringConfigurationDataSource;
import com.epam.training.jp.jdbc.excercises.spring.SpringConfigurationJdbcDao;
import com.epam.training.jp.jdbc.excercises.spring.SpringConfigurationService;


public class JdbcApp {

	public static void main(String[] args) {
		JdbcApp app = new JdbcApp();
		app.run();
	}
	
	protected void run() {
		AbstractApplicationContext context = createSpringContext();
		
		RestaurantService restaurantService = context.getBean(RestaurantService.class);
		DatabaseCreator databaseCreator = context.getBean(DatabaseCreator.class);
		
		databaseCreator.createAndPopulateDatabase();
				
		Address address = new Address();
		address.setCity("Budapest");
		address.setCountry("HU");
		address.setStreet("Futo utca");
		address.setZipCode("1085");
				
		restaurantService.save(address);
		
		List<Food> foodList = new ArrayList<>();
		
		for(int i=0;i<5;i++){
			Food food = new Food();
			food.setCalories(i+5);
			food.setName("Uj kaja " + i);
			food.setVegan((i%2==1)?true:false);
			food.setPrice(i+1000);
			foodList.add(food);
		}
		
		restaurantService.save(foodList); //az eredmeny az adatbazisban tekintheto meg. Nincs lekero eljarasunk a foodra.. 
		
		
		System.out.println("Retreived id of saved address: " + address.getId());
		
		System.out.println("Number of foods before delete menu #1: " + restaurantService.getFoodsAvailable(new Date(), "10 MINUTES").size());
		restaurantService.removeMenu(1);
		System.out.println("Number of foods after delete menu #1: " + restaurantService.getFoodsAvailable(new Date(), "10 MINUTES").size());
		
		System.out.println("Price before update food: " + restaurantService.findFoodByName("Tender lion").getPrice());
		restaurantService.updateFoodPriceByName("Tender lion", 7000);
		System.out.println("Price after update food: " + restaurantService.findFoodByName("Tender lion").getPrice());

		System.out.println("Restaurants: " + restaurantService.getAllRestaurantsWithAddress());
		
		restaurantService.removeMenu(1);		
		
		context.close();
		
	}
	
	protected AbstractApplicationContext createSpringContext() {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(
				SpringConfigurationDataSource.class,
				SpringConfigurationJdbcDao.class,
				SpringConfigurationService.class);
		return context;
	}
}
