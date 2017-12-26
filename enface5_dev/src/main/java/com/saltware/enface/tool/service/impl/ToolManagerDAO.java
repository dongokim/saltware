package com.saltware.enface.tool.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class ToolManagerDAO extends SqlMapClientDaoSupport
{
	/*
	 * find zip code
	 * @param paramMap (dong, langKnd)
	 */
	public List findZipCodes(Map paramMap) throws DataAccessException
	{
		return (List)getSqlMapClientTemplate().queryForList("tool.findZipCodes", paramMap);
	}

}