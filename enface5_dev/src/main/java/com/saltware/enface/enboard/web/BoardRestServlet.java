package com.saltware.enface.enboard.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.util.HttpUtil;

/**
 * Servlet implementation class RESTServlet
 */
public class BoardRestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(BoardRestServlet.class);
       
    public BoardRestServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// REQUEST URI에서 BOARD ID와 BLTN NO를 분리한다.
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String servletPath = "/enboard";
		String uriPrefix = contextPath + servletPath;
		String restPath = requestUri.substring( uriPrefix.length()+1);
		
		log.info("** request uri=" + requestUri);
		
		
		// .brd로 끝나면 마지막 요소 삭제
		//		notice/1245/edit.brd 이런 경우
		
		if( restPath.endsWith(".brd")) {
			if( restPath.lastIndexOf("/")==-1) {
				restPath = "";
			} else {
				restPath = restPath.substring( 0, restPath.lastIndexOf("/"));
			}
		} else if(restPath.endsWith("gif") || restPath.endsWith("png") || restPath.endsWith("bmp") || restPath.endsWith("css") || restPath.endsWith("js") || restPath.endsWith("jsp")){
			HttpUtil.sendRedirect( response, requestUri.replaceFirst("/enboard", "/board"));
			return;
		}
		
		String boardId = null;
		String bltnNo = null;
		if( restPath.length() > 0) {
			String[] args = restPath.split("/");
			if(args.length <= 2){
				if( args.length > 0) {
					boardId = args[0];
				}
				if( args.length > 1) {
					bltnNo = args[1];
				}
			}
		}
		
		log.info("** boardId=" + boardId);
		log.info("** bltnNo=" + bltnNo);
		
		
		// .brd로 접근하는 경우 처리
		if( requestUri.endsWith(".brd")) {
			if( requestUri.endsWith("list.brd")) {
				// 목록조회이면 목록조회 REST URL로 보낸다
				if( boardId==null) {
					boardId = request.getParameter("boardId");
				}
				String redirectUrl = contextPath + "/enboard/" + boardId;
				// HTTP 응답분할 취약점 해결. \r, \n 제거
				redirectUrl = redirectUrl.replaceAll("\n", "").replaceAll("\r", "");
				HttpUtil.sendRedirect( response, redirectUrl);
				return;
			} else if( requestUri.endsWith("read.brd")) {
				if( boardId==null) {
					boardId = request.getParameter("boardId");
				}
				if( bltnNo == null) {
					bltnNo = request.getParameter("bltnNo");
				}
				String redirectUrl = contextPath +  "/enboard/" + boardId + "/" + bltnNo;
				log.info( "** redirect [" + requestUri + "] -> [" + redirectUrl + "]");
				// HTTP 응답분할 취약점 해결. \r, \n 제거
				redirectUrl = redirectUrl.replaceAll("\n", "").replaceAll("\r", "");
				HttpUtil.sendRedirect(  response, redirectUrl);
				return;
			} else {
				String forwardUrl = contextPath + "/board/" + requestUri.substring( requestUri.lastIndexOf("/") + 1);
				// REST에 게시판ID가 있고 request에는 없는 경우 Url에 boardId추가
				if( boardId != null && request.getParameter("boardId")==null)  {
					forwardUrl += "?boardId=" + boardId;
				}
				// REST에 게시물번호가 있고 request에는 없는 경우 Url에 bltnNo추가
				if( bltnNo != null && request.getParameter("bltnNo")==null)  {
					forwardUrl += ( forwardUrl.indexOf("?") == -1  ?  "?" : "&") + "bltnNo=" + bltnNo;
				}
				//edit.brd에 cmd가 없으면
				if( requestUri.endsWith("edit.brd") && request.getParameter("cmd") == null) {
					if( bltnNo != null) {
						// 게시물이 있으면 MODIFY
						forwardUrl += ( forwardUrl.indexOf("?") == -1  ?  "?" : "&") + "cmd=MODIFY";
					} else {
						// 게시물이없으면 WRITE
						forwardUrl += ( forwardUrl.indexOf("?") == -1  ?  "?" : "&") + "cmd=WRITE";
					}
				}
				// HTTP 응답분할 취약점 해결. \r, \n 제거
				forwardUrl = forwardUrl.replaceAll("\n", "").replaceAll("\r", "");
				log.info( "** forward [" + requestUri + "] -> [" + forwardUrl + "]");
				RequestDispatcher rd = getServletContext().getRequestDispatcher( forwardUrl);
				rd.forward( request, response);
				return;
			}
		}
		

		String forwardUrl = contextPath;
		if( bltnNo == null) {
			if( request.getParameter("boardId") != null) {
				forwardUrl += "/board/list.brd";
			} else if( boardId != null ){
				forwardUrl += "/board/list.brd?boardId=" + boardId;
			} else {
				forwardUrl += contextPath + request.getRequestURL().toString().replaceAll("/enboard", "/").replaceAll("/" + boardId, "").replace("/" + bltnNo, "");
			}
		} else {
			if(restPath.split("/").length == 2){
				forwardUrl += "/board/read.brd?boardId=" + boardId + "&bltnNo=" + bltnNo + "&cmd=READ";
			}
			else forwardUrl += contextPath + request.getRequestURL().toString().replaceAll("/enboard", "/").replaceAll("/" + boardId, "");
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher( forwardUrl);
		rd.forward( request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet( request, response);
	}
	

}
