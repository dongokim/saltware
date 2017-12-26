<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,java.util.ArrayList" %>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enboard.vo.BoardVO,com.saltware.enboard.vo.BulletinVO,com.saltware.enboard.vo.BltnPollVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="${pageContext.request.contextPath}/decorations/layout/tile/css/style.css" rel="stylesheet" type="text/css" />

<style type="text/css">
  body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
  }
</style>

<c:set var="loginInfo" value="${secPmsnVO.loginInfo}"/>
<%--jsp:useBean id="loginInfo" type="java.util.Map"/--%>

<c:if test="${boardVO.extUseYn == 'Y'}">
  <c:set var="extnMapper" value="${boardVO.bltnExtnMapper}"/>
  <%--jsp:useBean id="extnMapper" type="com.saltware.enboard.integrate.DefaultBltnExtnMapper"/--%>
  <c:if test="${!empty bltnVO.bltnExtnVO}">
	<c:set var="extnVO" value="${bltnVO.bltnExtnVO}"/>
	<%--jsp:useBean id="extnVO" type="com.saltware.enboard.integrate.DefaultBltnExtnVO"/--%>
  </c:if>
</c:if>
  <table width="69%" border="0" cellspacing="0" cellpadding="0" style="float: left;">
	  <tr>
	    <td>
	        <div id="EnviewContentsArea">
	         <form name="editForm" onsubmit="return false">
				  <table>
						<tr>
							<td>
								<table style="border-bottom-style: none; border-top-style: none; border-top-width: 0px;">
									<tr>
										<td style="border-bottom-style: none; border-top-style: none; border-top-width: 0px;">
											<img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/banner.png" width="400px" height="650px">
										</td>
									</tr>
								</table>
							</td>
							<td>
								 <p style="font-weight: bold; float: left; margin: 10px;"> 내용입력</p>
								 <table style="margin: 0;">
										<tr>
											<th scope="row" class="L first" onfocus="">제목</th>
											<td colspan="5"><label for="bltnSubj" style="display:none;"></label><input type="text" id="bltnSubj" class="subject" maxlength="120"/></td>
										</tr>
										<tr>
											<th scope="row" class="L first">주관자</th>
											<td colspan="5"><label for="subject" style="display:none;"></label>
												<%--익명게시판이 아닌 경우--%>
												  <c:if test="${boardVO.anonYn == 'N'}">
													<input type="text" maxlength="30" name="userNick" class="subject" style="display: none;" readonly
														   <c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${secPmsnVO.userNick}"/>"</c:if>
												           <c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.userNick}"/>"</c:if>
													/>
												  </c:if>
												  <%--익명게시판인 경우--%>
												  <c:if test="${boardVO.anonYn == 'Y'}">
												    <input type="text" maxlength="30" name="userNick" style="width:300px; display: none;" class="subject"/>
												  </c:if>
										  		  <%--실명인증 게시판인 경우--%>
												  <c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}">
												    <input type="text" maxlength="30" name="userNick" style="width:300px; display: none;" class="subject" readonly value='<%=(String)session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))%>'/>
												  </c:if>
												  
												 <input type="text" id="bltnSubja" class="subject" />
											</td>
										</tr>
										<tr>
											<th scope="row" class="L first">일시장소</th>
											<td colspan="5"><label for="subject" style="display:none;"></label><input type="text" id="subject" class="subject" /></td>
										</tr>
										<tr>
											<th scope="row" class="L first">게시기간</th>
											<td colspan="5">
												<fieldset>
														<label for="date_01"></label><input type="text" id="date_01" class="txt_70" value="2014.01.01" style="color:#9b9b9b; text-align:center;" /><span><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/icon_calender.gif" alt="달력" /></span> ~ <label for="date_02"></label><input type="text" id="date_02" class="txt_70" value="2014.01.31" style="color:#9b9b9b; text-align:center;"/><span><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/icon_calender.gif" alt="달력" /></span>
												</fieldset>                    
											</td>
										</tr>
										<tr>
											<th scope="row" class="L first">URL</th>
											<td colspan="5"><label for="subject" style="display:none;"></label><input type="text" id="subject" class="subject" /></td>
										</tr>
										<tr>
											<th scope="row" class="L first">배너위치</th>
											<td>
												<input type="radio" name="conttxt" value="일반 TEXT" id="conttxt_0" />
												<label for="conttxt_0">상단</label>
												<input type="radio" name="conttxt" value="HTML 편집기" id="conttxt_1" />
												<label for="conttxt_1">하단</label>
											</td>
										</tr>
										<tr style="display: none;">
											<th scope="row" class="L first">내용</th>
											<td colspan="5">
													<textarea id=editorCntt style="width:99%;height:100px">ㅇㅇ<c:out value="${bltnVO.bltnCntt}"/></textarea>
		    										<input type=hidden name=bltnOrgCntt>
											</td>
										 </tr>
								</table>
								<div style="float: left; margin-top: 10px;">
									<a onclick="javascript:surveyParticipation();" class="btn_black"><span>미리보기</span></a>
								</div>
								<div style="margin-top: 50px;">
										<img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/banner_imgArea.png">
								</div>
								<div class="btnArea" style="margin-bottom: 190px;">
									<div class="rightArea"><a onclick="ebEdit.save()" class="btn_gray"><span>저장</span></a> 
									<a onclick="ebEdit.list()" class="btn_gray"><span>목록</span></a></div>
								</div>
							</td>
							</tr>
					</table>
				
				<input type=hidden name=delBoardId value="<c:out value="${boardVO.boardRid}"/>">
	        	<input type=hidden name=unDelList>
	        	<input type=hidden name=delList>
	        	<input type=hidden name=subId value=sub01>
	        	
				</form>
			</div>
		</td>
	  </tr>
</table>