<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>
<div class="ptl_Fevent2" style="padding:12px 12px 10px 12px;">
  <h2>${boardTitle}</h2>
  <span class="ptl_more"><a href="<c:out value="${moreSrc}"/>">more</a></span>
  <ul>
  	<c:if test="${empty results}" >
   			<li><span class="ptl_title">게시물이 존재하지 않습니다.</span></li>
   		</c:if>
<c:if test="${not empty results}" >
   			<c:forEach items="${results}" var="bltn">
          	<li><a href="<%=request.getContextPath() %><c:out value="${moreSrc}"/>?action=READ&boardId=<c:out value="${boardId}"/>&bltnNo=<c:out value="${bltn.bltnNo}"/>"><span class="ptl_title"><c:out value="${bltn.bltnSubj}"/></span></a></li>
          </c:forEach>
   		</c:if>
    </ul>
</div>
               