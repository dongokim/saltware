package com.saltware.enface.portlet.board.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.portlet.board.service.MiniBoardService;
import com.saltware.enface.util.DataCacheManager;


@SuppressWarnings("rawtypes")
public class MiniBoardServiceImpl implements MiniBoardService {
	
    private static final Log log = LogFactory.getLog(MiniBoardServiceImpl.class);
    
    DataCacheManager dataCacheManager = new DataCacheManager(100, 60);
    
    private MiniBoardDAO miniBoardDAO = null;
	
	public List findBltn( String boardId) {
		List results = (List)dataCacheManager.get(boardId);
		if( results==null) {
			results = miniBoardDAO.findBltn(boardId);
		}
		return results;
	}
	
	public List findBltnByCategory(String boardId) {
		List results = (List)dataCacheManager.get(boardId);
		if( results==null) {
			results = miniBoardDAO.findBltnByCategory(boardId);
		}
		return results;
	}

	public MiniBoardDAO getMiniBoardDAO() {
		return miniBoardDAO;
	}

	public void setMiniBoardDAO(MiniBoardDAO miniBoardDAO) {
		this.miniBoardDAO = miniBoardDAO;
	}
}
