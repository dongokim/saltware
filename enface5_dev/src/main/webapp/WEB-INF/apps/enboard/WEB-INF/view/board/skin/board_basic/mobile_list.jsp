<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<link rel="stylesheet" href="<%=request.getContextPath() %>/kaist_portal/css/mobile/style.css" type="text/css" media="all" />
<link type="text/css" href="${cPath}/kaist/common/css/common.css" rel="stylesheet" />
<link type="text/css" href="${cPath}/kaist/skin/${boardSkin}/css/style.css" rel="stylesheet" /> 
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<section role="main" id="container">
	<article class="content_gray">
		<div class="list_wrap_w">	
			<ul class="ul_list">
				<c:if test="${empty bltnList}">
					<li class="first" >
						<a title="<util:message key='eb.info.desc.not.exist.bltn'/>" href="#">
							<p class="title">
							<util:message key='eb.info.desc.not.exist.bltn'/>
							</p>
							<span class="date"></span>
							<span class="num_ck"></span>
							<span class="writer"></span>
					</a>				
					</li>	
				</c:if>
				<%-- List of Bulletin --%>
       			<c:forEach items="${bltnList}" var="list" varStatus="status">
					<li <c:if test="${status.count == 1 }">class="first"</c:if>>
						<a title="<c:out value="${list.bltnOrgSubj}"/>" href="<%=request.getContextPath()%>/${boardPath }/<c:out value="${list.boardId}"/>/<c:out value="${list.bltnNo}"/>" target="_top">
							<p class="title">
					  	<c:if test="${boardVO.cateYn == 'Y'}">
								<span class="status <c:out value="${fn:toLowerCase(list.cateId)}"/>"  style="display:inline-block;text-shadow: none;"><c:out value="${list.cateNm}"/></span>
						</c:if>
							<c:out value="${list.bltnOrgSubj}"/> 
				                <c:if test="${list.bltnMemoCnt != '0'}">
				                  <span class="num" style="color:red" >(<c:out value="${list.bltnMemoCnt}"/>)</span>
				                </c:if>
								<span class="file"><c:if test="${list.bltnIcon == 'B'}"><img src="<%=request.getContextPath()%>/board/images/board/skin/default/icon_file.png" alt="File" /></c:if></span>
							</p>
							<span class="date"><c:out value="${list.regDatimSF}"/></span>
							<span class="num_ck"><util:message key="eb.text.views"/> : <fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" /></span>
							<span class="writer"><c:out value="${list.userNick}"/></span>
						</a>
						<span class="bu_arr"></span>
					</li>
				</c:forEach>
			</ul>
			<%-- <c:if test="${boardVO.writableYn == 'Y'}">
				<div class="btn_box">
		     		<button type="submit" class="btn medium white"  onClick="ebList.writeBulletin()" ><util:message key="ev.hnevent.label.reg"/></button> 
			    </div>
			</c:if> --%>
			<div id="pageIndex" class="paginate"></div>
		</div>
	</article>
</section>
<script language="javascript">
	var currentPage = <c:out value="${boardSttVO.page}"/>;
	var totalPage   = <c:out value="${boardSttVO.totalPage}"/>;
	var setSize     = 5; <%--하단 Page Iterator에서의 Navigation 갯수--%>
	var imgUrl      = "<%=request.getContextPath()%>/board/images/board/skin/enboard/";
	
	var startPage;    
	var endPage;      
	var cursor;      
	var curList = "";
	var prevSet = "";
	var nextSet = "";
	var firstP  = "";
	var lastP   = "";

	moduloCP = (currentPage - 1) % setSize / setSize ;
	startPage = Math.ceil((((currentPage - 1) / setSize) - moduloCP)) * setSize + 1;
	moduloSP = ((startPage - 1) + setSize) % setSize / setSize;
	endPage   = Math.ceil(((((startPage - 1) + setSize) / setSize) - moduloSP)) * setSize;

	if (totalPage <= endPage) endPage = totalPage;
	
	if(currentPage == 1) prevSet = "<a class=\"pre\"><img src=\"<%=request.getContextPath() %>/kaist_portal/images/mobile/bu_pg_l.gif\" alt=\"이전페이지\"/></a>";
	else prevSet = "<a onclick=\"ebList.next('"+(currentPage-1)+"')\" class=\"pre\"><img src=\"<%=request.getContextPath() %>/kaist_portal/images/mobile/bu_pg_l.gif\" alt=\"이전페이지\"/></a>";
	cursor = startPage;
	while( cursor <= endPage ) {
		if( cursor == currentPage ) 
	   		curList += "<strong>"+currentPage+"</strong>";
		else {
	    	curList += "<a onclick=\"ebList.next('"+cursor+"')\">"+cursor+"</a>";
		}
		cursor++;
	}
	if(cursor == 1) curList += "<strong>"+startPage+"</strong>";
	if(currentPage == totalPage) nextSet = "<a class=\"next\"><img src=\"<%=request.getContextPath() %>/kaist_portal/images/mobile/bu_pg_r.gif\" alt=\"다음페이지\"/></a>";
	else nextSet = "<a onclick=\"ebList.next('"+(currentPage+1)+"')\" class=\"next\"><img src=\"<%=request.getContextPath() %>/kaist_portal/images/mobile/bu_pg_r.gif\" alt=\"다음페이지\"/></a>";
	curList = prevSet + curList + nextSet;
	
	document.getElementById("pageIndex").innerHTML = curList;
	
</script>