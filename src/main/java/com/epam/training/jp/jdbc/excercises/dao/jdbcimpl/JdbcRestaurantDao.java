package com.epam.training.jp.jdbc.excercises.dao.jdbcimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.epam.training.jp.jdbc.excercises.dao.RestaurantDao;
import com.epam.training.jp.jdbc.excercises.domain.Address;
import com.epam.training.jp.jdbc.excercises.domain.Food;
import com.epam.training.jp.jdbc.excercises.domain.Restaurant;
import com.epam.training.jp.jdbc.excercises.dto.RestaurantWithAddress;

public class JdbcRestaurantDao extends GenericJdbcDao implements RestaurantDao {

	public JdbcRestaurantDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<Food> getFoodsAvailable(Date date, String restaurantName) {
		List<Food> resultFoodList = new ArrayList<>();
		String sql = "select * from food join menu_food on food.ID = food_ID join menu on Menu_ID=menu.ID join restaurant on Restaurant_ID=restaurant.ID where restaurant.NAME= ? and (? between FROMDATE and TODATE)";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			ps.setDate(2, sqlDate);
			ps.setString(1, restaurantName);
			
			ResultSet rs = ps.executeQuery();			
			
			Food actualFood;
			while (rs.next()) {
				actualFood = new Food();
				actualFood.setId(rs.getInt(1));
				actualFood.setCalories(rs.getInt(2));
				actualFood.setVegan(rs.getBoolean(3));
				actualFood.setName(rs.getString(4));
				actualFood.setPrice(rs.getInt(5));				
				resultFoodList.add(actualFood);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultFoodList;
	}

	@Override
	public List<RestaurantWithAddress> getAllRestaurantsWithAddress() {
		List<RestaurantWithAddress> resultRestaurantList = new ArrayList<>();
		String sql = "select * from restaurant join address on ADDRESS_ID = address.ID;";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ResultSet rs = ps.executeQuery();			
			
			RestaurantWithAddress actualRestaurantWithAddress;
			Restaurant actualRestaurant;
			Address actualAddress;
			while (rs.next()) {
				actualRestaurant = new Restaurant();
				actualRestaurant.setId(rs.getInt("restaurant.ID"));
				actualRestaurant.setName(rs.getString("restaurant.NAME"));
				actualRestaurant.setAddressId(rs.getInt("restaurant.ADDRESS_ID"));

				actualAddress = new Address();
				actualAddress.setId(rs.getInt("address.ID"));
				actualAddress.setCity(rs.getString("address.CITY"));
				actualAddress.setCountry(rs.getString("address.COUNTRY"));
				actualAddress.setStreet(rs.getString("address.STREET"));
				actualAddress.setZipCode(rs.getString("address.ZIPCODE"));	
				
				
				actualRestaurantWithAddress = new RestaurantWithAddress(actualRestaurant,actualAddress);			
				resultRestaurantList.add(actualRestaurantWithAddress);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultRestaurantList;
	}

}
