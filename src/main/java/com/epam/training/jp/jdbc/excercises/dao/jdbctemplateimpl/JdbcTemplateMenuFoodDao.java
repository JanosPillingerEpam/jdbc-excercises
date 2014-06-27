package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.MenuFoodDao;

public class JdbcTemplateMenuFoodDao extends JdbcDaoSupport implements MenuFoodDao {

	public JdbcTemplateMenuFoodDao(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void removeMenuFoods(int id) {
		String sql = "delete from menu_food where Menu_ID=?";
		JdbcTemplate jt = new JdbcTemplate(getDataSource());
		jt.update(sql,id);
	}
}
