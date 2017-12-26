<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enboard.vo.BoardVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>

<link href="${pageContext.request.contextPath}/decorations/layout/tile/css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
  function surveyParticipation(){
	alert("정상적으로 참여 되었습니다.");
	ebRead.actionBulletin('list',-1);
  }
</script>
<style type="text/css">
  <!--
  body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
  }
  -->
</style>

<c:set var="loginInfo" value="${secPmsnVO.loginInfo}"/>
<c:if test="${boardVO.extUseYn == 'Y'}">
  <c:set var="rsExtnMapper" value="${boardVO.bltnExtnMapper}"/>
</c:if>

<body>
<c:forEach items="${bltnVOs}" var="list"> 
	   	 	<table width="69%" border="0" cellspacing="0" cellpadding="0" style="float: left;">
	   	 	<tr>
	   	 		<td>
					<div id="EnviewContentsArea">
						<table cellpadding="0" cellspacing="0" summary="게시판읽기">
						<caption>게시판읽기</caption>
							<tr>
								<th scope="row" class="L first">제목</th>
								<td colspan="5">
						            <c:if test="${list.bltnBestLevel == '9'}">
						              <c:out value="${boardVO.imgBest9}" escapeXml="false"/>
						              1234
						            </c:if>
						            
						            <c:out value="${list.bltnOrgSubj}" escapeXml="false"/>
								</td>
							</tr>
							<tr>
							 	<th scope="row" class="L">구분</th>
							    <td>진행중</td>
								<th scope="row" class="L first">작성자</th>
								  <c:if test="${boardVO.anonYn == 'N'}"><%--익명게시판이 아니다--%>
						            <td><c:out value="${list.userNick}"/></td>
						          </c:if>
							</tr>
							<tr>
								<th scope="row" class="L">설문기간</th>
								<td><c:out value="${list.regDatimSF}"/> ~ <c:out value="${list.regDatimSF}"/></td>
								<th scope="row" class="L">조회수</th>
								<td><c:out value="${list.bltnReadCnt}"/></td>
							</tr>
							<tr>
								<th scope="row" class="L first">내용</th>
								<td colspan="5">
									<div class="viewArea">
										<%-- <p> <c:out value="${list.bltnOrgCntt}" escapeXml="false"/></p> --%>
										<div id="EnviewContentsArea">
											<ul>
												<li class="first">
													<p class="question">Q. 응답자 유형은 어떻게 되나요? (총 참여인원 100명)</p>
													<ul>
														<li><span class="type">1. 직원</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
														<li><span class="type">2. 임원</span> <span class="graph"></span> <span class="percent">30%(30명)</span> </li>
														<li><span class="type">3. 관리자</span> <span class="graph"></span> <span class="percent">20%(20명)</span> </li>
													</ul>
												</li>
												<li>
													<p class="question">Q. 포털시스템에 추가 되어야할 기능이 있다면 무엇인가요? (총 참여인원 100명)</p>
													<ul>
														<li><span class="type">1. 응답율</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
													</ul>
												</li>
												<li>
													<p class="question">Q. 포털시스템 디자인 중 선호하는 유형을 선택하세요. (총 참여인원 100명)</p>
													<ul>
														<li><span class="type">1. 이미지보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
														<li><span class="type">2. 이미지보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
														<li><span class="type">3. 이미지보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
													</ul>
												</li>
												<li>
													<p class="question">Q. 신용보증재단 홍보 동영상 중 선호하는 유형을 선택하세요. (총 참여인원 100명)</p>
													<ul>
														<li><span class="type">1. 동영상보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
														<li><span class="type">2. 동영상보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
														<li><span class="type">3. 동영상보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
													</ul>
												</li>
											</ul>
										</div>
									</div>
								</td>
							</tr>
						</table>
						<!-- table//--> 
						<!-- btnArea-->
						<div class="btnArea">
							<div class="leftArea">
					            <c:if test="${boardVO.anonYn == 'N'}"><%--'익명/실명인증 게시판'이 아니면--%>
					              <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionBulletin('list',-1)">
								    <span>목록</span>
								  </a>&nbsp;
								<%--   <c:if test="${list.editable == 'true'}">수정권한이 있는 경우
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)">
									  <span>수정</span>
									</a>&nbsp;
					              </c:if>
								  <c:if test="${list.editable == 'false'}">수정권한이 없는 경우
								    <c:if test="${empty list.userId}">익명글이면
								      <c:if test="${list.editableUserId == '_is_admin_'}">관리자인 경우
					                    <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)">
									      <span>수정</span>
									    </a>&nbsp;
					                  </c:if>
								      <c:if test="${list.editableUserId != '_is_admin_'}">관리자가 아닌 경우
										<c:if test="${boardVO.writableYn == 'Y'}">
					                      <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,false)">
									     	 <span>수정</span>
									      </a>&nbsp;
										</c:if>
					                  </c:if>
								    </c:if>
								  </c:if>
					              <c:if test="${list.deletable == 'true'}">삭제권한이 있는 경우
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)">
									  <span>삭제</span>
									</a>&nbsp;
					              </c:if> --%>
<%-- 					              <c:if test="${list.deletable == 'false'}">삭제권한이 없는 경우
								    <c:if test="${empty list.userId}">익명글이면
								      <c:if test="${list.deletableUserId == '_is_admin_'}">관리자인 경우
					                    <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)">
									      <span>삭제</span>
									    </a>&nbsp;
					                  </c:if>
								      <c:if test="${list.deletableUserId != '_is_admin_'}">관리자가 아닌 경우
										<c:if test="${boardVO.writableYn == 'Y'}">
					                      <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,false)">
									       	<span>삭제</span>
									      </a>&nbsp;
										</c:if>
					                  </c:if>
									</c:if>  
								  </c:if> --%>
								</c:if>
							
					            <c:if test="${boardVO.anonYn != 'N'}"><%--'익명/실명인증 게시판'인 경우--%>
								 <%--  <c:if test="${list.editableUserId == '_is_admin_'}">관리자인 경우
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)">
								       	<span>수정</span>
								    </a>&nbsp;
								  </c:if>
								  <c:if test="${list.editableUserId != '_is_admin_'}">관리자가 아닌 경우
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,false)">
								      	<span>수정</span>
								    </a>&nbsp;
								  </c:if>
								  <c:if test="${list.deletableUserId == '_is_admin_'}">관리자인 경우
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)">
								     	<span>삭제</span>
								    </a>&nbsp;
								  </c:if>
								  <c:if test="${list.deletableUserId != '_is_admin_'}">관리자가 아닌 경우
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,false)">
								      	<span>삭제</span>
								    </a>&nbsp;
								  </c:if> --%>
								</c:if>
			          	 </div>
			           	</div>
						<!-- table-->
						<%-- <table class="secondtable" cellpadding="0" cellspacing="0" summary="게시판이동">
						<caption>게시판이동</caption>
							<colgroup>
								<col width="15%" />
								<col width="85%" />
							</colgroup>

						    <c:if test="${!empty list.nextBoardId && !empty list.nextBltnNo}">
							    <tr>
							      <th scope="row" class="L first"><span  class="viewlist">다음글</span> <img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/icon_prevview.gif" alt="다음글" /></th>
								  <c:if test="${list.nextBoardId == 'del_flag_y'}">
									<td> 삭제된 글입니다. </td>
								  </c:if>
								  <c:if test="${list.nextBoardId != 'del_flag_y'}">
								    <td>
									    <a style=cursor:pointer onclick="ebRead.readPrevNextBltn('<c:out value="${list.nextBoardId}"/>','<c:out value="${list.nextBltnNo}"/>')">
									      <c:out value="${list.nextBltnOrgSubj}" escapeXml="false" />
									    </a>
								    </td>
						       <c:out value="${list.nextUserNick}"/><br>
								    <c:out value="${list.nextRegDatimF}"/><br>
								    <c:out value="${list.nextRegDatimSF}"/><br>
								    <c:out value="${list.nextUpdDatimF}"/><br>
								    <c:out value="${list.nextUpdDatimSF}"/><br>
								  </c:if>
								</tr>
							</c:if>
							
						    <c:if test="${!empty list.prevBoardId && !empty list.prevBltnNo}">
						    	<tr>
							      <th scope="row" class="L first"><span class="viewlist">이전글</span> <img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/icon_nextview.gif" alt="이전글" /></th>
								  <c:if test="${list.prevBoardId == 'del_flag_y'}">
									<td> 삭제된 글입니다. </td>
								  </c:if>
								  <c:if test="${list.nextBoardId != 'del_flag_y'}">
								  <td>
									    <a style=cursor:pointer onclick="ebRead.readPrevNextBltn('<c:out value="${list.prevBoardId}"/>','<c:out value="${list.prevBltnNo}"/>')">
									      <c:out value="${list.prevBltnOrgSubj}" escapeXml="false"/>
									    </a>
								   <c:out value="${list.prevUserNick}"/>
								   		<c:out value="${list.prevRegDatimF}"/><br>
								   		<c:out value="${list.prevRegDatimSF}"/><br>
								    	<c:out value="${list.prevUpdDatimF}"/><br>
								   		<c:out value="${list.prevUpdDatimSF}"/><br>
								  </td>
								  </c:if>
								</tr>
							</c:if>
							
						</table> --%>
					</div> <!-- EnviewContentsArea -->
					</td>
				 </tr>
			 </table>
</c:forEach>
</body>