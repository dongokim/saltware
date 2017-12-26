<%@page import="com.saltware.enface.util.StringUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.saltware.enview.security.EVSubject"%>
<%@page import="com.saltware.enview.Enview"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enboard.vo.BoardVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld"%>
<%
	String nmKor = "";
	String nmEng = "";
	String userId = "";
	if (EVSubject.getUserInfo() != null) {
		nmKor = (String) EVSubject.getUserInfo().getUserInfoMap().get("nm_kor");;
		nmEng = (String) EVSubject.getUserInfo().getUserInfoMap().get("nm_eng");;
		userId = EVSubject.getUserId();
	}

	request.setAttribute("nmKor", nmKor);
	request.setAttribute("nmEng", nmEng);
	request.setAttribute("userId", userId);
	
	// 제재 일자 초기값 1달.	
	DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
	Calendar cal = Calendar.getInstance();
	request.setAttribute("bgnYmd", df.format( cal.getTime()));
	cal.add( Calendar.MONTH, 1);
	request.setAttribute("endYmd", df.format( cal.getTime()));
	
	String viewMode = StringUtil.isNull2(request.getParameter("viewMode"), "");
	request.setAttribute("viewMode", viewMode);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>환자안전포탈</title>
	<meta name="author" content="환자안전포탈"/>
	<meta name="description" content="환자안전포탈"/>
	<meta name="keywords" content="환자안전포탈"/>
	<meta name="Resource-type" content="Document"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/kops/css/default.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/common.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/sub.js"></script>
	<script type="text/javascript">

		$(document).ready(function(){
			try
			{
				parent.fn_childIframeAutoresize(parent.document.getElementById(window.name),parseInt($("#container").height()+100));
				parent.fn_scrollTop();	
			}
			catch(e)
			{console.log(e);}
		});
	
		$(function() {
			
			$('.act_selknowview').click(function(e) {
				e.preventDefault();
				$(this).parent().parent().parent().parent().toggleClass('sel_knowpost');; 
				if($(this).text()=="<util:message key='eb.info.ttl.show.org.bltn' />"){
					$(this).text("<util:message key='eb.info.ttl.hide.org.bltn' />");
				}else{
					$(this).text("<util:message key='eb.info.ttl.show.org.bltn' />");
				}
			});
		});

	</script>
</head>
<body>
<!-- Contents -->
<div id="container" style="margin-top:0px">
	<div class="contents"> 
	<c:forEach items="${bltnVOs}" var="list">
		<c:set var="loginInfo" value="${secPmsnVO.loginInfo}" />
		<c:if test="${boardVO.extUseYn == 'Y'}">
			<c:set var="rsExtnMapper" value="${boardVO.bltnExtnMapper}"/>
		</c:if>
		<input type="hidden" id="clipTemp"/>
		<input type="hidden" id="userId" value="<c:out value="${userId }" />"/>
	
		<!-- contents -->
		<h2 class="sub_title">문의 및 상담</h2>
		<div class="board_box">
			<div class="board_list mgt30">
				<table class="table_t2" summary="번호, 제목, 등록자, 등록일의 정보를 제공합니다.">
					<caption>게시판 리스트</caption>
					<colgroup>
						<col width=""/>
						<col width="10%"/>
						<col width="12%"/>
						<col width="10%"/>
					</colgroup>
					<thead>
					<tr>
						<th scope="col">
							 제목
						</th>
						<th scope="col">
							 등록자
						</th>
						<th scope="col">
							 등록일
						</th>
						<th scope="col">
							 답변여부
						</th>
					</tr>
					</thead>
					<tbody>
						<tr>
							<td class="t_left"><c:out value="${list.bltnSubj}"/></td>
							<td><c:out value="${list.userNick}"/></td>
							<td><c:out value="${list.regDatimSF}"/></td>
							<td>
								<c:if test="${list.bltnReplyCnt == 0}">
									<a href="javascript:void(0);" class="btn_blue">답변중</a>						
								</c:if>
								<c:if test="${list.bltnReplyCnt != 0}">
									<a href="javascript:void(0);" class="btn_gray">답변완료</a>
								</c:if>
							</td>
						</tr>
						<tr>
							<td colspan="4" class="view_cont">
								<div class="cont">
									<c:out value="${list.bltnOrgCntt}" escapeXml="false" />								
								</div>
							</td>
						</tr>
						<c:if test="${list.bltnReplyCnt != 0}">
							<c:forEach items="${list.replyList}" var="rList">
								<c:if test="${rList.bltnLev != '1'}">
									<tr>
										<td colspan="4" class="view_cont bd_no">
											<div class="reply">
												<p class="f_bold c_black">
													<c:out value="${rList.bltnOrgSubj}" escapeXml="false"/>
												</p>
												<p class="date">
													<span><c:out value="${rList.userNick}"/></span>
													<c:out value="${rList.regDatimSF}"/>
												</p>
												<p><c:out value="${rList.bltnOrgCntt}" escapeXml="false" /></p>
												<c:if test="${rList.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
													<c:if test="${boardVO.anonYn == 'N'}"><%--'익명/실명인증 게시판'이 아니면--%>
													  <c:if test="${rList.editable == 'true'}"><%--수정권한이 있는 경우--%>
														<a style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${rList.bltnNo}"/>,true)" target="_self">
														  수정
														</a>&nbsp;
													  </c:if>
													  <c:if test="${rList.editable == 'false'}"><%--수정권한이 없는 경우--%>
														<c:if test="${empty rList.userId}"><%--익명글이면--%>
														  <c:if test="${rList.editableUserId == '_is_admin_'}"><%--관리자인 경우--%>
															<a style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${rList.bltnNo}"/>,true)" target="_self">
															  수정
															</a>&nbsp;
														  </c:if>
														  <c:if test="${rList.editableUserId != '_is_admin_'}"><%--관리자가 아닌 경우--%>
															<c:if test="${boardVO.writableYn == 'Y'}">
															  <a style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${rList.bltnNo}"/>,false)" target="_self">
																수정
															  </a>&nbsp;
															</c:if>
														  </c:if>
														</c:if>
													  </c:if>
													  <c:if test="${rList.deletable == 'true'}"><%--삭제권한이 있는 경우--%>
														<a style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${rList.bltnNo}"/>,true)" target="_self">
														  삭제
														</a>&nbsp;
													  </c:if>
													  <c:if test="${rList.deletable == 'false'}"><%--삭제권한이 없는 경우--%>
														<c:if test="${empty rList.userId}"><%--익명글이면--%>
														  <c:if test="${rList.deletableUserId == '_is_admin_'}"><%--관리자인 경우--%>
															<a style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${rList.bltnNo}"/>,true)" target="_self">
															   삭제
															</a>&nbsp;
														  </c:if>
														  <c:if test="${rList.deletableUserId != '_is_admin_'}"><%--관리자가 아닌 경우--%>
															<c:if test="${boardVO.writableYn == 'Y'}">
															  <a style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${rList.bltnNo}"/>,false)" target="_self">
																 삭제
															  </a>&nbsp;
															</c:if>
														  </c:if>
														</c:if>  
													 </c:if>
												  </c:if>
												</c:if>
											</div>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
			<div class="btn_box mgt40">
				<a href="javascript:void(0);" class="btn_pre" target="_self" onclick="ebRead.actionBulletin('list',-1)" >목록</a>
				<c:if test="${boardVO.replyableYn == 'Y' && boardVO.replyYn == 'Y'}"><%--답글쓰기 권한이 있는 경우--%>
		  			<c:if test="${secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete || _enview_info_.groupId eq 'GRP_BOARD_MANAGER'|| _enview_info_.groupId eq 'GRP_BOARD' }">
               			<a href="javascript:void(0);" target="_self" class="btn_next" onclick="ebRead.actionBulletin('reply',<c:out value="${list.bltnNo}"/>)">답글쓰기</a>
		  			</c:if>
			 		<c:if test="${list.editable == 'true' || _enview_info_.userId eq list.userId }">	
						<c:if test="${list.bltnReplyCnt == 0}">							
							<a href="javascript:void(0);" target="_self" class="btn_next"  onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)">수정</a>
						</c:if>
						<c:if test="${list.bltnReplyCnt != 0}">
							<a href="javascript:void(0);" target="_self" class="btn_next" onclick="alert('답변이 존재하므로 수정이 불가능합니다.');">수정</a>
						</c:if>
					</c:if>
					<c:if test="${list.deletable == 'true' || _enview_info_.userId eq list.userId}">
						<c:if test="${list.bltnReplyCnt == 0}">
							<a href="javascript:void(0);" target="_self" class="btn_next" onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)">삭제</a>
						</c:if>
						<c:if test="${list.bltnReplyCnt != 0}">
							<a href="javascript:void(0);" target="_self" class="btn_next" onclick="alert('답변이 존재하므로 삭제가 불가능합니다.');">삭제</a>
						</c:if>
					</c:if>
          		 </c:if>
			</div>
		</div>
		<!-- //contents -->
	</c:forEach>
	</div>
</div>
</body>

