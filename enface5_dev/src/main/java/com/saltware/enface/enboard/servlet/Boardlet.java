/**
 * Copyright (c) 2011 Saltware, Inc.
 * 
 * http://www.saltware.co.kr
 * 
 * Kolon Science Valley Bldg. 2th. 901, Guro-dong 811, Guro-gu,
 * Seoul, 152-878, South Korea.
 * All Rights Reserved.
 * 
 * This software is the Java based Enterprise Board system for Portal of Saltware, Inc.
 * Making any change or distributing this without permission from us is out of law.
 */
package com.saltware.enface.enboard.servlet; 

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enboard.util.ValidateUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;

import java.io.IOException;

public class Boardlet extends HttpServlet {
	private static Log logger = LogFactory.getLog(Boardlet.class);

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		doPost( request, response);
	}

	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String uri = request.getRequestURI();
		uri = uri.substring( uri.lastIndexOf('/') + 1 );
		
		if( "bltnMngr".equals( uri )) {
			
			HelloBoard.getInst().doService( request, response );
			
		} else if( "fileMngr".equals( uri )) { // 첨부파일관련 요청.
			
			String cmd = request.getParameter("cmd");
			
			if( "upload".equals( cmd )) { // 업로드...
				
				FileUpload.getInst( request ).doService( request, response );
				
			} else if( "down".equals( cmd )) { // 다운로드...
				FileDownload.getInst( request ).doService( request, response );
			} else if( "delete".equals( cmd )) { // 삭제...
				FileDelete.getInst( request ).doService( request, response );
				
			}
		} else {
			
			PortalConfiguration conf = Enview.getConfiguration();
			String downloadPath = conf.getString("board.download.path");
			String pollDownloadPath = conf.getString("poll.download.path");
			
			if (ValidateUtil.isEmpty ( downloadPath)) {
				downloadPath = "/upload/board";
			}
			
			if (ValidateUtil.isEmpty ( pollDownloadPath)) {
				pollDownloadPath = "/upload/poll";
			}
			
			String editorPath = downloadPath + "/editor/";
			String thumbPath = downloadPath + "/thumb/";
			String pollPath = pollDownloadPath + "/";
			
			if (request.getRequestURI().indexOf(editorPath)!=-1 		// 에디터첨부이미지 관련 요청.
				|| request.getRequestURI().indexOf(thumbPath)!=-1		// 썸네일이미지 관련 요청.
				|| request.getRequestURI().indexOf(pollPath)!=-1) {		// 설문이미지 관련 요청.
				FileDownload.getInst( request ).doViewImage(request, response);	
			}
		}
	}
}
