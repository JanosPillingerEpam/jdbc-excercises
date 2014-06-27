package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.RestaurantDao;
import com.epam.training.jp.jdbc.excercises.domain.Address;
import com.epam.training.jp.jdbc.excercises.domain.Food;
import com.epam.training.jp.jdbc.excercises.domain.Restaurant;
import com.epam.training.jp.jdbc.excercises.dto.RestaurantWithAddress;

public class JdbcTemplateRestaurantDao extends JdbcDaoSupport implements RestaurantDao {

	public JdbcTemplateRestaurantDao(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public List<Food> getFoodsAvailable(Date date, String restaurantName) {
		String sql = "select * from food join menu_food on food.ID = food_ID join menu on Menu_ID=menu.ID join restaurant on Restaurant_ID=restaurant.ID where restaurant.NAME= ? and (? between FROMDATE and TODATE)";
		JdbcTemplate jt = new JdbcTemplate(getDataSource());
		return jt.query(sql, new Object[] {restaurantName, date},new FoodMapper());
	}

	@Override
	public List<RestaurantWithAddress> getAllRestaurantsWithAddress() {
		String sql = "select * from restaurant join address on ADDRESS_ID = address.ID;";
		JdbcTemplate jt = new JdbcTemplate(getDataSource());
		return jt.query(sql, new RestaurantWithAddressMapper());
	}
	
	private static final class FoodMapper implements RowMapper<Food> {
		
		public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
			Food food = new Food();
			food.setCalories(rs.getInt("calories"));
			food.setName(rs.getString("name"));
			food.setPrice(rs.getInt("price"));
			food.setVegan(rs.getBoolean("isvegan"));
			return food;
		}
		
	}
	
	private static final class RestaurantWithAddressMapper implements RowMapper<RestaurantWithAddress> {
		
		public RestaurantWithAddress mapRow(ResultSet rs, int rowNum) throws SQLException {
			Restaurant restaurant = new Restaurant();
			Address address = new Address();
			
			restaurant.setId(rs.getInt("restaurant.id"));
			restaurant.setAddressId(rs.getInt("address_id"));
			restaurant.setName(rs.getString("restaurant.name"));
			address.setCity(rs.getString("city"));
			address.setCountry(rs.getString("country"));
			address.setStreet(rs.getString("street"));
			address.setZipCode(rs.getString("zipcode"));
			
			RestaurantWithAddress restaurantWithAddress = new RestaurantWithAddress(restaurant, address);
			return restaurantWithAddress;
		}
		
	}
	



}
