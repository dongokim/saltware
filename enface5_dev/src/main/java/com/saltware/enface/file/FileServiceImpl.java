package com.saltware.enface.file;

import java.util.List;

import com.saltware.enface.util.PageUtil;
import com.saltware.enface.util.StringUtil;

public class FileServiceImpl implements FileService
{
	private FileDAO fileDAO;
	
	public void setFileDAO(FileDAO fileDAO)
	{
		this.fileDAO = fileDAO;
	}

	@Override
	public int save(FileVO paramVO)
	{
		try
		{
			return this.fileDAO.save(paramVO);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<FileVO> list()
	{
		try
		{
			return this.fileDAO.list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
