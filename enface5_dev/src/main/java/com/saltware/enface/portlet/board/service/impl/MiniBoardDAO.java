package com.saltware.enface.portlet.board.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class MiniBoardDAO extends SqlMapClientDaoSupport {

	public List findBltn(String boardId) throws DataAccessException {
		Map paramMap = new HashMap();
		paramMap.put("boardId", boardId);
		return getSqlMapClientTemplate().queryForList("miniBoard.findBltn", paramMap);
	}
	
	public List findBltnByCategory(String boardId) throws DataAccessException {
		Map paramMap = new HashMap();
		paramMap.put("boardId", boardId);
		return getSqlMapClientTemplate().queryForList("miniBoard.findBltnByCategory", paramMap);
	}

}
