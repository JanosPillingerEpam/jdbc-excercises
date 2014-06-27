package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.FoodDao;
import com.epam.training.jp.jdbc.excercises.domain.Food;

public class JdbcTemplateFoodDao extends JdbcDaoSupport implements FoodDao {

	public JdbcTemplateFoodDao(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public Food findFoodByName(String name) {
		String sql = "select * from food where NAME= ?";
		JdbcTemplate jt = new JdbcTemplate(getDataSource());
		Food food = jt.queryForObject(sql,
				new Object[] {name},
				new RowMapper<Food>() {
					public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
						Food food = new Food();
						food.setCalories(rs.getInt("calories"));
						food.setName(rs.getString("name"));
						food.setPrice(rs.getInt("price"));
						food.setVegan(rs.getBoolean("isvegan"));
						food.setId(rs.getInt("id"));
						return food;
					}
		});
		return food;
	}

	@Override
	public void updateFoodPriceByName(String name, int newPrice) {
		String sql = "update food set PRICE=? where NAME=?";
		JdbcTemplate jt = new JdbcTemplate(getDataSource());
		jt.update(sql, new Object[] {newPrice, name});
	}

	@Override
	public void save(List<Food> foods) {
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(getDataSource());
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(foods.toArray());
		String sql = "insert INTO food VALUES(null, :Calories, :Vegan, :Name , :Price)";
		npjt.batchUpdate(sql,batch);
	}
	
}
