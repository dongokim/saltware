package com.saltware.enface.file;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

@SuppressWarnings("deprecation")
public class FileDAO extends SqlMapClientDaoSupport{

	public int save(FileVO paramVO)
	{
		try
		{
			return this.getSqlMapClient().update("FileDAO.save", paramVO);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<FileVO> list()
	{
		try
		{
			return this.getSqlMapClient().queryForList("FileDAO.list");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
