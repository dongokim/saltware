package com.saltware.enface.tool.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.tool.service.ToolManager;
import com.saltware.enview.exception.BaseException;

/**
 * 
 *  ToolManagerImpl
 *  최종 수정일: 2011년 3월 29일
 *  작성자: 김성욱
 * 
 */
public class ToolManagerImpl implements ToolManager
{
    private static final Log log = LogFactory.getLog(ToolManagerImpl.class);
    
    private ToolManagerDAO toolManagerDAO = null;
    
    public ToolManagerImpl() {
    	
    }
    
	public List getZipCodes(String dong, String langKnd) 
	{
		List zipCodes = null;
		
    	try {
    		Map paramMap = new HashMap();
    		paramMap.put("dong", dong);
    		paramMap.put("langKnd", langKnd);
    		
    		zipCodes = toolManagerDAO.findZipCodes(paramMap);
//    	}
//    	catch (BaseException e)   {
//            log.error( e.getMessage(), e);
        } finally {
        	
        }

    	return zipCodes;
	}
}
