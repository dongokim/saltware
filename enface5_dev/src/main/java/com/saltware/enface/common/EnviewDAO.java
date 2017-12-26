package com.saltware.enface.common;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;

public class EnviewDAO extends SqlMapClientDaoSupport {
	private String enviewDbType;

	public EnviewDAO() {
		enviewDbType = Enview.getConfiguration().getString("enview.db.type", "oracle");
	}

	PortalConfiguration config = Enview.getConfiguration();

	/**
	 * 
	 * @param commonStatementName
	 * @return
	 */
	public String getStatementName(String commonStatementName) {
		String dbDependentStatemtName = commonStatementName + "." + enviewDbType;
		if (((SqlMapClientImpl) getSqlMapClient()).getMappedStatement(dbDependentStatemtName) != null) {
			return dbDependentStatemtName;

		}
		return commonStatementName;
	}

	public Object queryForObject(String statementName, Object parameterObject) throws DataAccessException {
		return getSqlMapClientTemplate().queryForObject(getStatementName(statementName), parameterObject);
	}

	public Object queryForObject(String statementName, Object parameterObject, Object resultObject) throws DataAccessException {
		return getSqlMapClientTemplate().queryForObject(getStatementName(statementName), parameterObject, resultObject);
	}

	public List queryForList(String statementName, Object parameterObject) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList(getStatementName(statementName), parameterObject);
	}

	public List queryForList(String statementName, Object parameterObject, int skipResults, int maxResults) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList(getStatementName(statementName), skipResults, maxResults);
	}

	public void queryWithRowHandler(String statementName, Object parameterObject, RowHandler rowHandler) throws DataAccessException {
		getSqlMapClientTemplate().queryWithRowHandler(getStatementName(statementName), parameterObject, rowHandler);
	}

	public Map queryForMap(String statementName, Object parameterObject, String keyProperty) throws DataAccessException {
		return getSqlMapClientTemplate().queryForMap(getStatementName(statementName), parameterObject, keyProperty);
	}

	public Map queryForMap(String statementName, Object parameterObject, String keyProperty, String valueProperty) throws DataAccessException {
		return getSqlMapClientTemplate().queryForMap(getStatementName(statementName), parameterObject, keyProperty, valueProperty);
	}

	public Object insert(String statementName, Object parameterObject) throws DataAccessException {
		return getSqlMapClientTemplate().insert(getStatementName(statementName), parameterObject);
	}

	public int update(String statementName, Object parameterObject) throws DataAccessException {
		return getSqlMapClientTemplate().update(getStatementName(statementName), parameterObject);
	}

	public void update(String statementName, Object parameterObject, int requiredRowsAffected) throws DataAccessException {
		getSqlMapClientTemplate().update(getStatementName(statementName), parameterObject, requiredRowsAffected);
	}

	public int delete(String statementName, Object parameterObject) throws DataAccessException {
		return getSqlMapClientTemplate().delete(getStatementName(statementName), parameterObject);
	}

	public void delete(String statementName, Object parameterObject, int requiredRowsAffected) throws DataAccessException {
		getSqlMapClientTemplate().delete(getStatementName(statementName), parameterObject, requiredRowsAffected);
	}

}
