<%@page import="com.saltware.enface.enboard.vo.BoardMenuVo"%>
<%@page import="com.saltware.enface.enboard.service.BoardMenuManager"%>
<%@page import="com.saltware.enface.util.StringUtil"%>
<%@page import="com.saltware.enview.Enview"%>
<%@page import="com.saltware.enboard.vo.BoardVO"%>
<%@page import="com.saltware.enboard.vo.BoardSttVO"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%
	String userId = EnviewSSOManager.getUserId(request, response);
	
	request.setAttribute("userId", userId);
	
	BoardSttVO boardSttVO = (BoardSttVO) request.getAttribute("boardSttVO");
	BoardVO boardVO = (BoardVO) request.getAttribute("boardVO");
	
	// 게시판 카테고리 표시여부
	boolean cateMode = false;
	BoardMenuManager boardMenuManager = (BoardMenuManager)Enview.getComponentManager().getComponent("com.saltware.enface.enboard.service.BoardMenuManager");
	
	if( !boardVO.getMergeType().equals("A")) {
		String cateId = boardSttVO.getCateId();
		if( ! StringUtil.isEmpty(cateId)) {
			BoardMenuVo boardMenuVo = boardMenuManager.getMenu( cateId);
			if( boardMenuVo != null && boardMenuVo.getDepth()==1) {
				cateMode = true;			
			} else {
				System.out.println("boardMenuVo null || boardMenuVo.getDepth != 1 ");
				cateMode = false;
			}
		} else {
			cateMode = true;			
		}
	}
	request.setAttribute("cateMode", cateMode);
	
//	String viewName = boardSttVO.getViewMode();
	String viewName = null;
	
	if (viewName == null || viewName.isEmpty()) {
		if (boardVO.getBoardType().equals("I")) {
			viewName = "thumbnail";
		} else if (boardVO.getBoardType().equals("E")) {
			viewName = "calendar";
		} else if (boardVO.getBoardType().equals("G")) {
			viewName = "faq";
		} else if (boardVO.getBoardType().equals("H")) {
			viewName = "qna";
		} else if (boardVO.getBoardType().equals("C")) { 
			viewName = "qna";
		} else if (boardVO.getBoardType().equals("B")) { 
			viewName = "blog";
		} else {
			viewName = "board";
		}
	}
	request.setAttribute("viewName", viewName);
	request.setAttribute("isTemplate", boardVO.getBoardId().indexOf("tmpl_") > -1 ? false : true);
%>
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}" />
<c:set var="colspanSize" value="0" />
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
function fn_childIframeAutoresize(arg,childHeight) 
{
	try 
	{
		var ht = childHeight;
		arg.height = 0;
		arg.height = ht;
	} 
	catch(e) 
	{ }
}
$(document).ready(function(){
	try
	{
	var currentPage = <c:out value="${boardSttVO.page}"/>;
var totalPage   = <c:out value="${boardSttVO.totalPage}"/>;
var setSize     = 5; <%--하단 Page Iterator에서의 Navigation 갯수--%>
var imgUrl      = "${pageContext.request.contextPath}/kops/images/common/";

var afpImg = "board_prev_2.png";  //처음페이지
var apsImg = "board_prev_1.png"; // 이전 페이지
var alpImg = "board_next_2.png";  //마지막페이지
var ansImg = "board_next_1.png";  //다음페이지
	
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
	
// 처음페이지 이동 
if (currentPage > 1) {
	cursor = startPage - 1;
	firstP = "<a href='javascript:void(0);' class='prevEnd' title='처음페이지' onclick=ebList.next('1');>";
	firstP += "<span><img src='"+imgUrl+afpImg+"' alt='첫 페이지로 이동'/></span></a>";
} else {
	firstP = "<a href='javascript:void(0);' class='prevEnd' title='처음페이지' onclick=void(0);>";
	firstP += "<span><img src='"+imgUrl+afpImg+"' alt='첫 페이지로 이동'/></span></a>";
}
	
//이전페이지 셋팅
if(currentPage > 1){
	prevSet = "<a href='javascript:void(0);' class='prev' title='이전페이지' onclick=ebList.next('"+ (currentPage - 1) +"');>";
	prevSet += "<span><img src='"+imgUrl+apsImg+"' alt='이전 페이지로 이동' /></span></a>";
} else{
	prevSet = "<a href='javascript:void(0);' class='prev' title='이전페이지' onclick=void(0);>";
	prevSet += "<span><img src='"+imgUrl+apsImg+"' alt='이전 페이지로 이동' /></span></a>";
}
		
cursor = startPage;
if(endPage > 0) {
	while( cursor <= endPage ) {
		if( cursor == 1 && cursor == currentPage) {
			curList += "<a href=\"javascript:void(0);\" class='on'><span>" + cursor + "</span></a>";
		} else if( cursor == currentPage ) {
	   		curList += "<a href=\"javascript:void(0);\" class='on'><span>" + cursor + "</span></a>";
		} else {
			curList += "<a href='javascript:void(0);' onclick=ebList.next('"+cursor+"')><span>"+cursor+"</span></a>";
		}
		
		cursor++;
	}
} else {
	curList += "<a href='javascript:void(0);' class='first on'><span>"+currentPage+"</span></a>";
}
	
//다음페이지 셋팅
if(totalPage > currentPage){
	nextSet = "<a href='javascript:void(0);' class='next' title='다음페이지' onclick=ebList.next('"+ (currentPage + 1) +"');>";
	nextSet += "<span><img src='"+imgUrl+ansImg+"' alt='다음 페이지로 이동' /></span></a>";
} else {
	nextSet = "<a href='javascript:void(0);' class='next' title='다음페이지' onclick=void(0);>";
	nextSet += "<span><img src='"+imgUrl+ansImg+"' alt='다음 페이지로 이동' /></span></a>";
}

// 맨 뒤 페이지로 이동
if ( totalPage > endPage) {
	cursor = endPage + 1;  
	lastP = "<a href='javascript:void(0);' class='nextEnd' onclick=ebList.next('"+totalPage+"');>";
	lastP += "<span><img src='"+imgUrl+alpImg+"' alt='마지막 페이지로 이동' title='마지막페이지'/></span></a>";
} else if ( totalPage = endPage) {
	cursor = endPage + 1;  
	lastP = "<a href='javascript:void(0);'  class='nextEnd' onclick=ebList.next('"+totalPage+"');>";
	lastP += "<span><img src='"+imgUrl+alpImg+"' alt='마지막 페이지로 이동' title='마지막페이지'  /></span></a>";
}else {
	lastP = "<a href='javascript:void(0);' class='nextEnd' onclick=void(0);>";
	lastP += "<span><img src='"+imgUrl+alpImg+"' alt='마지막 페이지로 이동' title='마지막페이지'/></span></a>";
}
	
curList = firstP + prevSet + curList + nextSet + lastP;

//document.getElementById("pageIndex").innerHTML = curList;
 if ($("#pageIndex").length) {
    $("#pageIndex").html(curList);
 }
}
catch(e){console.log(e);}


	//2017.07.27 sskim : iframe resize 추가 
	try
	{
		parent.fn_childIframeAutoresize(parent.document.getElementById(window.name),parseInt($("#container").height()+100));
		parent.fn_scrollTop();	
	}
	catch(e)
	{console.log(e);}
});
	
	function fn_checkPasswd(boardId, bltnNo)
	{
		$("#checkPwForm > #boardId").val(boardId);
		$("#checkPwForm > #bltnNo").val(bltnNo);
		$("#checkPwForm").submit();
	}
	
	function doExportExcel()
	{
    	var param = $('#checkPwForm').serialize();
      	document.getElementById("exportExcelIF").src = "/board/QnAExportExcelForAjax.face?" + param; 
	}
</script>
<form id="checkPwForm" name="checkPwForm" method="post" action="/board/checkPwView.face">
	<input type="hidden" id="boardId" name="boardId" value='<c:out value="${boardVO.boardId}"/>'/>
	<input type="hidden" id="bltnNo" name="bltnNo"/>
</form>

<div id="container" style="margin-top:0px">
	<div class="contents">
	
		<!-- contents -->
		<h2 class="sub_title"><c:out value="${boardVO.boardNm}"/></h2>
		<div class="board_box">
			<p class="t_right">
			<c:if test="${ secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete || _enview_info_.groupId eq 'GRP_BOARD_MANAGER' || _enview_info_.groupId eq 'GRP_BOARD' }">
				<a href="javascript:doExportExcel();" class="btn_gray_s" target="_self">엑셀저장</a></p> <!-- 버튼추가_2017-10-24 -->
			</c:if>
			<div class="board_list mgt10">
				<table class="table_t2" summary="번호, 제목, 등록자, 등록일의 정보를 제공합니다.">
				<caption>게시판 리스트</caption>
				<colgroup>
					<col width="6%">
					<col width="">
					<col width="10%">
					<col width="12%">
					<col width="10%">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">번호</th>
						<th scope="col">제목</th>
						<th scope="col">등록자</th>
						<th scope="col">등록일</th>
						<th scope="col">조회수</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty bltnList}">
						<tr>
							<td colspan="5">해당하는 게시물이 존재하지 않습니다.</td>
						</tr>
					</c:if>
					<c:if test="${!empty bltnList}">
						<c:forEach items="${bltnList}" var="list" varStatus="status">
							<tr>
								<td><c:out value="${list.boardRow}"/></td>
                                <td class="t_left">
									<c:if test="${ _enview_info_.userId ne null }">
										<c:if test="${ secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete || _enview_info_.groupId eq 'GRP_BOARD_MANAGER' || _enview_info_.groupId eq 'GRP_BOARD' }">
											<a href="javascript:ebList.readBulletin('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>');" target="_self">
										</c:if>
										<c:if test="${ !secPmsnVO.isAdmin && !secPmsnVO.isSysop && !secPmsnVO.ableDelete && _enview_info_.groupId ne 'GRP_BOARD_MANAGER' && _enview_info_.groupId ne 'GRP_BOARD' }">
											<c:if test="${ _enview_info_.userId eq list.userId }">
												<a href="javascript:fn_checkPasswd('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>');" target="_self">
											</c:if>
											<c:if test="${ _enview_info_.userId ne list.userId }">
												<a href="javascript:void(0);" onclick="javascript:alert('해당 게시물의 접근권한이 없습니다.');" target="_self">
											</c:if>
										</c:if>
											<c:out value="${list.bltnOrgSubj}"/>
											</a>
									</c:if>
									<c:if test="${ _enview_info_.userId eq null }">
										<a href="javascript:void(0);" onclick="javascript:alert('해당 게시물의 접근권한이 없습니다.');" target="_self">
											<c:out value="${list.bltnOrgSubj}"/>
										</a>
									</c:if>
								</td>
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
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
		</div>
		<div class="boardNavigation">
			<div class="pagination a2" id="pageIndex">
				<div id="pageIndex">
				</div>
			</div>
		</div>
		
		<c:if test="${boardVO.writableYn == 'Y'}">
	  		<c:if test="${boardVO.mergeType == 'A'}">
				<div class="btn_box mgt40">
					<a href="javascript:void(0);" onclick="javascript:ebList.writeBulletin();" class="btn_next" target="_self">질문하기</a>
				</div>
			</c:if>
		</c:if>

		<%if(userId == null || userId.equals("null") || userId.equals("NULL")){ %>
				<div class="btn_box mgt40">
					<a href="javascript:void(0);" onclick="javascript:alert('로그인 후 등록이 가능합니다.');" class="btn_next" target="_self">질문하기</a>
				</div>
		<%} %>
	</div>
</div>
<iframe id='exportExcelIF' frameborder=0 width=0 height=0></iframe>	
