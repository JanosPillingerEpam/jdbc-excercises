package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.AddressDao;
import com.epam.training.jp.jdbc.excercises.domain.Address;

public class JdbcTemplateAddressDao extends JdbcDaoSupport implements AddressDao {

	public JdbcTemplateAddressDao(DataSource dataSource) {		
		setDataSource(dataSource);
	}

	@Override
	public void save(Address address) {
		SimpleJdbcInsert insertAddress = 
				new SimpleJdbcInsert(getDataSource()).withTableName("address").usingGeneratedKeyColumns("id");
		Map<String, Object> parameters = new HashMap<String, Object>(5);
		//parameters.put("id", address.getId());
		parameters.put("country", address.getCountry());
		parameters.put("city", address.getCity());
		parameters.put("street", address.getStreet());
		parameters.put("zipcode", address.getZipCode());
		Number id = insertAddress.executeAndReturnKey(parameters);
		address.setId(id.intValue());
	}

}
