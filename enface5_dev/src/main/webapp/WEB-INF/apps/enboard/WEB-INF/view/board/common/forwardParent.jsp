<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<%=request.getContextPath() %>/javascript/crossdomain.js"></script>

<% 
if( request.getHeader("referer") != null ) {
	//System.out.println("FORWARD to referer=["+request.getHeader("referer")+"]");
	if( request.getAttribute("script") == null ) { 
		if( request.getAttribute("post") == null ) { 
			//System.out.println("FORWARD to  next=["+request.getAttribute("next")+"]");
			if( "true".equals( request.getAttribute("adminajax"))) {
				//System.out.println("contextName=["+session.getServletContext().getServletContextName()+"]");
				response.sendRedirect((request.getAttribute("next")==null)?"/"+session.getServletContext().getServletContextName():"/"+session.getServletContext().getServletContextName()+"/"+(String)request.getAttribute("next"));
			} else {
%>
				
				<script language=javascript>
			 		parent.location.href='<%=(request.getAttribute("next")==null)? "./":(String)request.getAttribute("next")%>';
				</script>
<% 		
			}
		} else {
			//System.out.println("FORWARD to post=["+request.getAttribute("post")+"]");
%>
			<script language=javascript>
				
				function redirect() {
					document.forwardForm.submit();
				}
				window.onload=redirect;
			</script>
			<form name=forwardForm method=post action='<%=(String)request.getAttribute("post")%>'  target='_parent'>
			<input type=hidden name="boardId"    value="<c:out value="${boardSttVO.boardId}"/>"/>
			<input type=hidden name="cmpBrdId"   value="<c:out value="${boardSttVO.cmpBrdId}"/>"/>
			<input type=hidden name="bltnNo"     value="<c:out value="${boardSttVO.bltnNo}"/>"/>
			<input type=hidden name="srchKey"    value="<c:out value="${boardSttVO.srchKey}"/>"/>
			<input type=hidden name="srchType"   value="<c:out value="${boardSttVO.srchType}"/>"/>
			<input type=hidden name="srchBgnReg" value="<c:out value="${boardSttVO.srchBgnReg}"/>"/>
			<input type=hidden name="srchEndReg" value="<c:out value="${boardSttVO.srchEndReg}"/>"/>
			<input type=hidden name="srchReplYn" value="<c:out value="${boardSttVO.srchReplYn}"/>"/>
			<input type=hidden name="srchMemoYn" value="<c:out value="${boardSttVO.srchMemoYn}"/>"/>
			<input type=hidden name="page"       value="<c:out value="${boardSttVO.page}"/>"/>
			<input type=hidden name="cateId"     value="<c:out value="${boardSttVO.cateId}"/>"/>
			<input type=hidden name="bltnCateId" value="<c:out value="${boardSttVO.bltnCateId}"/>"/>
			<input type=hidden name="cmd"        value="<c:out value="${boardSttVO.cmd}"/>"/>
			<input type=hidden name="onlyReplYn" value="<c:out value="${boardSttVO.onlyReplYn}"/>"/>
			<input type=hidden name="onlyMemoYn" value="<c:out value="${boardSttVO.onlyMemoYn}"/>"/>
			<input type=hidden name="wallUserId" value="<c:out value="${boardSttVO.wallUserId}"/>"/>
			</form>
<%		}
	} else {
		//System.out.println("FORWARD to script=["+request.getAttribute("script")+"]");
%>
			<script language=javascript><%=request.getAttribute("script")%></script>
<%
	} 
} else {
%>
	<script language=javascript>window.open('./','_top');</script>
<% } %>
