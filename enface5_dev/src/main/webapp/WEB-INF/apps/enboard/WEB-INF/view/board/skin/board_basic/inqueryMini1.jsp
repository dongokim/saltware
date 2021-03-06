<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enview.security.EVSubject"%>
<%@ page import="com.saltware.enboard.vo.BoardVO"%>
<%@ page import="com.saltware.enboard.vo.BoardSttVO"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%
	String langKnd = (String) pageContext.getSession().getAttribute("langKnd");
	String cPath = request.getContextPath();
	String userId = "";
	if (EVSubject.getUserInfo() != null) {
		userId = EVSubject.getUserId();
	}
	request.setAttribute("userId", userId);
	request.setAttribute("langKnd", langKnd);
	request.setAttribute("cPath", cPath);
	request.setAttribute("isUseReadYn", Enview.getConfiguration().getBoolean("board.red.flag.use"));
	request.setAttribute("serverName", request.getServerName());
	request.setAttribute("localTarget", "http://local.abc.ac.kr:8081/demo/research/TO-BE.html");
	
	BoardSttVO boardSttVO = (BoardSttVO) request.getAttribute("boardSttVO");
	BoardVO boardVO = (BoardVO) request.getAttribute("boardVO");
%>

<link type="text/css" href="${cPath}/kaist/common/css/common.css" rel="stylesheet" />
<link type="text/css" href="${cPath}/kaist/skin/${boardSkin }/css/style.css" rel="stylesheet" /> 
<script type="text/javascript" src="${cPath}/kaist/common/js/jquery.min.js"></script>
<script type="text/javascript" src="${cPath}/kaist/common/js/jquery-ui.min.js"></script>
	<body>
		<div class="bd_minibd">
			<ul class="bd_minibd_ul1 ">
				<c:if test="${empty bltnList}">
					<li class=" bd_minibd_li1 ">
						<div class=" bd_minibd_div1">
							<a href="#" target="_top" class="bd_minibd_a1">
							<span class="bd_minibd_span2 "><util:message key="eb.info.desc.not.exist.bltn"/></span>
						 </a>
						</div>
					</li>
				</c:if>
				<c:if test="${!empty bltnList}">
					<c:forEach items="${bltnList}" var="list" varStatus="status">
						<li class=" bd_minibd_li1 ">
							<div class=" bd_minibd_div1">
							<c:if test="${empty boardVO.miniTrgtUrl }">
								<c:set var="url" value="/enboard/${boardVO.boardId }/${list.bltnNo}" />
								<c:set var="target" value="_top"/>
							</c:if>
							<c:if test="${!empty boardVO.miniTrgtUrl }">
								<c:set var="url" value="${boardVO.miniTrgtUrl}${fn:indexOf(boardVO.miniTrgtUrl, '?')==-1 ? '?' : '&'}boardPath=/${boardVO.boardId}/${list.bltnNo}" />
								<c:set var="target" value="_top"/>
							</c:if>
								<a href="<c:out value='${url }'/>" target="${target }" class="bd_minibd_a1">
									<c:if test="${boardVO.cateYn == 'Y'}">
									<span class="bd_minibd_span2">
										<c:if test="${!empty bltnCateList1 }">
											<c:out value="${list.cateNm}" escapeXml="false"/>
										</c:if>
										<c:if test="${!empty bltnCateList2 }">
											<c:out value="${list.cateNm2}" escapeXml="false"/>
										</c:if>
									</span>
									</c:if>
									<span class="bd_minibd_<c:if test="${boardVO.cateYn == 'Y'}">cate_</c:if>span1 text_cut">
										<c:out value="${list.bltnOrgSubj}"/>
									</span>
								</a>
							</div>
						</li>
					</c:forEach>
				</c:if>
			</ul>
		</div>
	</body>
