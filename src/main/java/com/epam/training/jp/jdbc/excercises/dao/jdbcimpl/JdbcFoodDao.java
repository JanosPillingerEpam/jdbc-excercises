package com.epam.training.jp.jdbc.excercises.dao.jdbcimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.epam.training.jp.jdbc.excercises.dao.FoodDao;
import com.epam.training.jp.jdbc.excercises.domain.Food;

public class JdbcFoodDao extends GenericJdbcDao implements FoodDao {

	public JdbcFoodDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Food findFoodByName(String name) {
		Food resultFood = new Food();
		String sql = "select * from food where NAME= ?";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, name);

			ResultSet rs = ps.executeQuery();

			if(!rs.next()){
				throw new RuntimeException("no food");
			}else{
				resultFood.setId(rs.getInt(1));
				resultFood.setCalories(rs.getInt(2));
				resultFood.setVegan(rs.getBoolean(3));
				resultFood.setName(rs.getString(4));
				resultFood.setPrice(rs.getInt(5));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultFood;

	}

	@Override
	public void updateFoodPriceByName(String name, int newPrice) {
		String sql = "update food set PRICE=? where NAME=?";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, newPrice);
			ps.setString(2, name);

			if(ps.executeUpdate() != 1){
				throw new RuntimeException("not updated properly");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(List<Food> foods) {

		String sql = "insert INTO food VALUES(null, ?, ?, ?, ?);";

		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			conn.setAutoCommit(false);
			for(Food actualFood : foods){
				ps.setInt(1, actualFood.getCalories());
				ps.setBoolean(2, actualFood.isVegan());
				ps.setString(3, actualFood.getName());
				ps.setInt(4,actualFood.getPrice());
				ps.addBatch();
			}
			
			ps.executeBatch();
			ps.clearBatch();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
