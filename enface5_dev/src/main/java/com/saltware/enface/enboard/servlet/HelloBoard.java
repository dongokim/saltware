package com.saltware.enface.enboard.servlet;

import java.io.*;

import javax.servlet.*; 
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enboard.dao.MileageDAO;
import com.saltware.enboard.form.AdminMileForm;
import com.saltware.enboard.security.SecurityMngr;
import com.saltware.enboard.util.ValidateUtil;
import com.saltware.enview.components.dao.ConnectionContext;
import com.saltware.enview.components.dao.ConnectionContextForRdbms;
import com.saltware.enview.components.dao.DAOFactory;

public class HelloBoard {
	
	private static HelloBoard singleton = null;
	private static Log logger = LogFactory.getLog( HelloBoard.class );

	public static HelloBoard getInst() {
        try {
            synchronized( HelloBoard.class ) {
                if( singleton == null )
                    singleton = new HelloBoard();
            }
        } catch(Exception e) {
            logger.error( e.getMessage(), e);;
        }
        return singleton;
	}
	
	public void doService( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		String next = null;

		try {
			String cmd     = request.getParameter("cmd");
			// BOARD-LIST 또는 BOARD-READ 요청이다.090601.KWShin.
			if( ValidateUtil.isEmpty( cmd )) { 
				String uri     = request.getRequestURI();
				String context = uri.substring( 0, uri.lastIndexOf('/'));
				
				String boardId = request.getParameter("boardId");
				String bltnNo  = request.getParameter("bltnNo");
				
				if( !ValidateUtil.isEmpty( boardId )) {
					if( !ValidateUtil.isEmpty( bltnNo )) {
						next = context + "/read.brd?boardId="+boardId+"&bltnNo="+bltnNo+"&cmd=READ&page=1&categoryId=-1";
					} else {
						next = context + "/list.brd?boardId="+boardId;
					}
				}
				if( next != null ) response.sendRedirect( next );

			} else {
				// 마일리지 적립
				if( "mile".equals( cmd )) {
					ConnectionContext connCtxt = null;
					try {
						if( SecurityMngr.isLogin( request )) {
							String mileCd = request.getParameter( "mileCd" );
							connCtxt = new ConnectionContextForRdbms( true );
						
							MileageDAO mileDAO = (MileageDAO)DAOFactory.getInst().getDAO( MileageDAO.DAO_NAME_PREFIX );
							
							AdminMileForm amForm = new AdminMileForm();
							amForm.setMileCd   (mileCd );
							amForm.setLoginInfo(SecurityMngr.getLoginInfo(request));
							int depositPnt = mileDAO.saveMileage (amForm, connCtxt);
							
							connCtxt.commit();
						
							responseSimpleRslt( response, String.valueOf( depositPnt ));

						} else {
							
							responseSimpleRslt( response, "-1");
						}
					} catch( Exception e ) {
						if( connCtxt != null ) connCtxt.rollback();
						responseSimpleRslt( response, "-9" );
						logger.error( e.getMessage(), e);;
					} finally {
						if( connCtxt != null ) connCtxt.release();
					}
				// BOARD-EDIT 직접 호출 
				} else if( "write".equals( cmd )) {
					String uri     = request.getRequestURI();
					String context = uri.substring( 0, uri.lastIndexOf('/'));
					
					String boardId = request.getParameter("boardId");
					
					next = context + "/edit.brd?boardId="+boardId+"&cmd=WRITE&page=1&categoryId=-1";
					response.sendRedirect(next);
				// 컨텐츠이용만족도 평가
				/*
				} else if( "cnttRcmd".equals( cmd )) {
					// reTag='Y'가 올라오면, 이미 평가했지만 재평가를 하겠다는 의미이다.
					ConnectionContext connCtxt = null;
					try {
						if( SecurityMngr.isLogin( request )) {
							String sysCd   = request.getParameter("sCd");
							String cnttId  = request.getParameter("cId");
							String ownerId = request.getParameter("oId");
							String rcmdPnt = request.getParameter("pnt");
							String reTag   = request.getParameter("reTag");
							connCtxt = new ConnectionContextForRdbms( true );
						
							MileageDAO rcmdDAO = (MileageDAO)DAOFactory.getInst().getDAO( MileageDAO.DAO_NAME_PREFIX );
							UserEVO userEss = (UserEVO)request.getSession().getAttribute(IConstants.SESSION_ATTR_USERESS);

							boolean already = false;
							if (!("Y".equals(reTag))) // 재평가라면 굳이 체크할 필요없다.
								already = rcmdDAO.checkBoardRcmd(sysCd, cnttId, userEss, connCtxt);
							
							if (already) {
								responseSimpleRslt (response, "already");
							} else {
								rcmdDAO.saveBoardRcmd(sysCd, cnttId, userEss, request.getRemoteAddr(), ownerId, Integer.parseInt(rcmdPnt), connCtxt);

								connCtxt.commit();
								
								responseSimpleRslt (response, "OK!");
							}
						} else {
							responseSimpleRslt (response, "notLogined");
						}
					} catch (Exception e) {
						if (connCtxt != null) connCtxt.rollback();
						log.error( e.getMessage(), e);;
						responseSimpleRslt (response, "systemError");
					} finally {
						if (connCtxt != null) connCtxt.release();
					}
					*/
				}
			}
		} catch( Exception e ) {
			System.out.println( e );
		}
	}
	
	private boolean responseSimpleRslt( HttpServletResponse response, String rsltStr ) throws Exception {
		try {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter p = response.getWriter();
			p.println( rsltStr );
			p.flush(); // JEUS에서는 close()만해서는 안되고 flush()를 해주어야만 한다. 안 그러면 응답이 안나간다.
			p.close();
		} catch( Exception e ) {
			throw e;
		}
		return true;
	}
}
