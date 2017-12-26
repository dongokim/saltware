<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.lang.StringBuffer" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%
	//System.out.println("############# host=" + request.getLocalAddr());
	StringBuffer buff = new StringBuffer();
	buff.append("<menu>");
	buff.append("<menuItem>");
	buff.append("<menuName>사용자관리</menuName>");
	buff.append("<menuAddr>/enview/portal/admin/access/usermng.page</menuAddr>");
	buff.append("</menuItem>");
	buff.append("<menuItem>");
	buff.append("<menuName>사이트관리</menuName>");
	buff.append("<menuAddr>/enview/portal/admin/contentmng/site.page</menuAddr>");
	buff.append("</menuItem>");
	buff.append("<menuItem>");
	buff.append("<menuName>게시판 관리</menuName>");
	buff.append("<menuAddr>/enview/portal/admin/bbsmng/bbsmain.page</menuAddr>");
	buff.append("</menuItem>");
	buff.append("<menuItem>");
	buff.append("<menuName>게시판 테스트</menuName>");
	buff.append("<menuAddr>/enview/board/list.brd?boardId=testBBS999</menuAddr>");
	buff.append("</menuItem>");
	buff.append("</menu>");
	//System.out.println("############# xml test=" + buff.toString());
%>
<%=buff.toString()%>