package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.MenuDao;

public class JdbcTemplateMenuDao extends JdbcDaoSupport implements MenuDao {

	public JdbcTemplateMenuDao(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void removeMenu(int id) {
		String sql = "delete from menu where ID=?";
		JdbcTemplate jt = new JdbcTemplate(getDataSource());
		jt.update(sql,id);
	}

	
	 
	
}
