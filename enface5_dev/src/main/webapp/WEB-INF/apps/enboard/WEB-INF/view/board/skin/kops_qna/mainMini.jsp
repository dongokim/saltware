<%@page import="kr.ac.snu.mn.vo.BoardMenuVo"%>
<%@page import="com.saltware.enface.util.StringUtil"%>
<%@ page import="com.saltware.enboard.vo.BoardVO"%>
<%@ page import="com.saltware.enboard.vo.BoardSttVO"%>
<%@page import="com.saltware.enview.Enview"%>
<%@page import="kr.ac.snu.mn.service.BoardMenuManager"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%
	String langKnd = (String) pageContext.getSession().getAttribute("langKnd");
	String cPath = request.getContextPath();
	String userId = EnviewSSOManager.getUserId(request, response);
	
	request.setAttribute("userId", userId);
	request.setAttribute("langKnd", langKnd);
	request.setAttribute("cPath", cPath);
	
	BoardSttVO boardSttVO = (BoardSttVO) request.getAttribute("boardSttVO");
	BoardVO boardVO = (BoardVO) request.getAttribute("boardVO");
	
	// 게시판 카테고리 표시여부
	boolean cateMode = false;
	BoardMenuManager boardMenuManager = (BoardMenuManager)Enview.getComponentManager().getComponent("kr.ac.snu.mn.service.BoardMenuManager");
	
	if( !boardVO.getMergeType().equals("A")) {
		String cateId = boardSttVO.getCateId();
		if( ! StringUtil.isEmpty(cateId)) {
			BoardMenuVo boardMenuVo = boardMenuManager.getMenu( cateId);
			if( boardMenuVo != null && boardMenuVo.getDepth()==1) {
				cateMode = true;			
			} else {
				cateMode = false;
			}
		} else {
			cateMode = true;			
		}
	}
	request.setAttribute("cateMode", cateMode);
%>
<link rel="stylesheet" href="${cPath}/snu/css/reset.css">
<link rel="stylesheet" href="${cPath}/snu/css/common.css">
<link rel="stylesheet" href="${cPath}/snu/css/layout.css">
<script type="text/javascript">
	$(document).ready(function () {
		$("#reload_list").click(function () {
			// 새로고침
			location.reload();
		}); 
	});
</script>
<div class="content_center_top">
	<h3><c:out value="${boardVO.boardNm }"/></h3>
	<div class="option">
		<!-- <label><input type="radio" name="filter_option" value="title"/> 제목</label>
		<label><input type="radio" name="filter_option" value="title/text" checked="checked"/> 제목+내용</label> -->
		<button id="reload_list" class="btn_new">새로고침</button>
	</div>
</div>
<div class="content_center_webzine">
	<ul>
		<c:if test="${!empty bltnList }">
			<c:forEach items="${bltnList}" var="list" begin="0" end="14">
			<c:if test="${list.delFlag != 'Y'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
		<!-- 이미지가 없는경우 -->
		<li class="<c:if test="${fn:length(list.thumbImgSrc150) ne fn:indexOf(list.thumbImgSrc150, 'T150') + 4 }">list_img</c:if>"><!-- <li class="list_img"> -->
			<c:if test="${fn:length(list.thumbImgSrc150) ne fn:indexOf(list.thumbImgSrc150, 'T150') + 4 }">
			<div class="thum"><img src="<c:out value="${list.thumbImgSrc150}"/>"  width="100%" height="100%" onerror="<c:out value="${list.thumbImgOnError}"/>" alt="Thumbnail" /></div>
			</c:if>
			<div class="txt">
				<div class="title">
					<a <c:if test="${list.readable == 'true' && boardVO.mergeType != 'A'}">
						href="/enboard/<c:out value="${list.eachBoardVO.boardId}"/>/<c:out value="${list.bltnNo}"/>" 
						target="_top"
					</c:if>" class="text-overflow">[<c:out value="${list.eachBoardVO.boardNm}"/>] <c:out value="${list.bltnOrgSubj}"/></a>
					<!-- <span class="icon_favorite on">즐겨찾기</span> -->
				</div>
				<div class="cont">
					 <c:if test="${boardVO.listCnttYn == 'Y'}">
					 	<c:if test="${fn:length(list.bltnCntt) >= 130 }">
							<c:out value="${fn:substring(list.bltnCntt, 0, 130)}" escapeXml="false" />......
						</c:if>
						<c:if test="${fn:length(list.bltnCntt) < 130 }">
							<c:out value="${list.bltnCntt}" escapeXml="false" />
						</c:if>
					 </c:if>
				</div>
				<div class="info">
					<div class="info_left"><c:out value="${list.userNick}"/></div>
					<div class="info_right"><c:out value="${list.regDatimSF}"/></div>
				</div>
			</div>
		</li>
		<!-- //이미지가 없는경우 -->
			</c:if>
			</c:forEach>
		</c:if>
	</ul>
</div>
<!-- <button class="content_right_btn off">개인콘텐츠 열기</button> -->