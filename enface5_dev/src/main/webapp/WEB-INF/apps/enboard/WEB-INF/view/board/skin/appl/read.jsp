<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enboard.vo.BoardVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>

<link href="${pageContext.request.contextPath}/decorations/layout/tile/css/style.css" rel="stylesheet" type="text/css" />

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
	   	 	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="float: left;">
	   	 	<tr>
	   	 		<td>
					<div id="EnviewContentsArea">
						<table cellpadding="0" cellspacing="0" summary="게시판읽기">
						<caption>게시판읽기</caption>
							<colgroup>
								<col width="15%" />
								<col width="18%" />
								<col width="12%" />
								<col width="21%" />
								<col width="12%" />
								<col width="22%" />
							</colgroup>
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
								<th scope="row" class="L first">작성자</th>
								  <c:if test="${boardVO.anonYn == 'N'}"><%--익명게시판이 아니다--%>
						            <td><c:out value="${list.userNick}"/></td>
						          </c:if>
								<th scope="row" class="L">작성일</th>
								<td><c:out value="${list.regDatimSF}"/></td>
								<th scope="row" class="L">조회</th>
								<td><c:out value="${list.bltnReadCnt}"/></td>
							</tr>
							<tr>
								<th scope="row" class="L first">내용</th>
								<td colspan="5">
									<div class="viewArea L">
										<p> <c:out value="${list.bltnOrgCntt}" escapeXml="false"/></p>
									</div>
								</td>
							</tr>
							<tr>
								<th scope="row" class="L first">첨부</th>
								<td colspan="5" style="text-align: left; line-height: 15px;">
								<c:set var="rsfile" value="${list.fileList}"/>
							        <c:if test="${!empty rsfile}">
							              <c:forEach items="${rsfile}" var="fList">   
									        <span class="addfile"><c:out value="${fList.downloadLink}" escapeXml="false"/>
									        (<c:out value="${fList.sizeSF}"/>)(<c:out value="${fList.downCntCF}"/>)</span> <br>
									       <%--  <span class="addfile"><c:out value="${fList.downopenLink}" escapeXml="false"/>
									        (<c:out value="${fList.sizeSF}"/>)(<c:out value="${fList.downCntCF}"/>)</span>
									        <span class="addfile"><c:out value="${fList.downloadCntLink}" escapeXml="false"/>
									        (<c:out value="${fList.sizeSF}"/>)(<c:out value="${fList.downCntCF}"/>)</span>
									        <span class="addfile"><c:out value="${fList.downopenCntLink}" escapeXml="false"/>
									        (<c:out value="${fList.sizeSF}"/>)(<c:out value="${fList.downCntCF}"/>)</span> --%>
							              </c:forEach>
							        </c:if>
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
								  <c:if test="${list.editable == 'true'}"><%--수정권한이 있는 경우--%>
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)">
									  <span>수정</span>
									</a>&nbsp;
					              </c:if>
								  <c:if test="${list.editable == 'false'}"><%--수정권한이 없는 경우--%>
								    <c:if test="${empty list.userId}"><%--익명글이면--%>
								      <c:if test="${list.editableUserId == '_is_admin_'}"><%--관리자인 경우--%>
					                    <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)">
									      <span>수정</span>
									    </a>&nbsp;
					                  </c:if>
								      <c:if test="${list.editableUserId != '_is_admin_'}"><%--관리자가 아닌 경우--%>
										<c:if test="${boardVO.writableYn == 'Y'}">
					                      <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,false)">
									     	 <span>수정</span>
									      </a>&nbsp;
										</c:if>
					                  </c:if>
								    </c:if>
								  </c:if>
					              
					              <c:if test="${list.deletable == 'true'}"><%--삭제권한이 있는 경우--%>
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)">
									  <span>삭제</span>
									</a>&nbsp;
					              </c:if>
					              <c:if test="${list.deletable == 'false'}"><%--삭제권한이 없는 경우--%>
								    <c:if test="${empty list.userId}"><%--익명글이면--%>
								      <c:if test="${list.deletableUserId == '_is_admin_'}"><%--관리자인 경우--%>
					                    <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)">
									      <span>삭제</span>
									    </a>&nbsp;
					                  </c:if>
								      <c:if test="${list.deletableUserId != '_is_admin_'}"><%--관리자가 아닌 경우--%>
										<c:if test="${boardVO.writableYn == 'Y'}">
					                      <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,false)">
									       	<span>삭제</span>
									      </a>&nbsp;
										</c:if>
					                  </c:if>
									</c:if>  
								  </c:if>
								</c:if>
							
					            <c:if test="${boardVO.anonYn != 'N'}"><%--'익명/실명인증 게시판'인 경우--%>
								  <c:if test="${list.editableUserId == '_is_admin_'}"><%--관리자인 경우--%>
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)">
								       	<span>수정</span>
								    </a>&nbsp;
								  </c:if>
								  <c:if test="${list.editableUserId != '_is_admin_'}"><%--관리자가 아닌 경우--%>
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,false)">
								      	<span>수정</span>
								    </a>&nbsp;
								  </c:if>
								  <c:if test="${list.deletableUserId == '_is_admin_'}"><%--관리자인 경우--%>
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)">
								     	<span>삭제</span>
								    </a>&nbsp;
								  </c:if>
								  <c:if test="${list.deletableUserId != '_is_admin_'}"><%--관리자가 아닌 경우--%>
					                <a class="btn_gray" style=cursor:pointer onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,false)">
								      	<span>삭제</span>
								    </a>&nbsp;
								  </c:if>
								</c:if>
			          	 </div>
			           	</div>
						<!-- table-->
						<table class="secondtable" cellpadding="0" cellspacing="0" summary="게시판이동">
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
						      <%--  <c:out value="${list.nextUserNick}"/><br>
								    <c:out value="${list.nextRegDatimF}"/><br>
								    <c:out value="${list.nextRegDatimSF}"/><br>
								    <c:out value="${list.nextUpdDatimF}"/><br>
								    <c:out value="${list.nextUpdDatimSF}"/><br> --%>
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
								   <%-- <c:out value="${list.prevUserNick}"/>
								   		<c:out value="${list.prevRegDatimF}"/><br>
								   		<c:out value="${list.prevRegDatimSF}"/><br>
								    	<c:out value="${list.prevUpdDatimF}"/><br>
								   		<c:out value="${list.prevUpdDatimSF}"/><br> --%>
								  </td>
								  </c:if>
								</tr>
							</c:if>
							
						</table>
						<!-- table//--> 
						<!-- table-->
						<table class="secondtable" cellpadding="0" cellspacing="0" summary="댓글등록영역">
						<caption>댓글등록영역</caption>
							<colgroup>
								<col width="15%" />
								<col width="85%" />
							</colgroup>
							<tr>
						        <th scope="row" class="L first">댓글(<c:out value="${fn:length(list.memoList)}"/>)</th>
						        <%-- Write memo. 댓글) --%>
									<c:if test="${boardVO.memoYn == 'Y' && boardVO.memoWritableYn == 'Y'}">
							        <form name=memoForm_<c:out value="${list.bltnNo}"/>>
											  <c:if test="${boardVO.anonYn == 'N'}"><%--익명게시판이 아닌 경우--%>
												<br><input type="hidden" name=userNick size=10 value='<c:out value="${secPmsnVO.userNick}"/>' readonly>
											  </c:if>
											  <c:if test="${boardVO.anonYn == 'Y'}"><%--익명게시판인 경우--%>
												<br><input type="hidden" name=userNick size=10>
											  </c:if>
											  <c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}"><%--실명인증 게시판인 경우--%>
												작성자<br>
												<%-- <input type="text" name="userNick" size="10" readonly 
													   value='<%=(session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))==null) ? "" : (String)session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))%>'
												> --%>
											  </c:if>
							                <td>
							                  <label for="subject" style="display:none;"></label><input type="text" id="subject" name=memoCntt class="memo_subject" value="50자(100byte) 이내로 댓글을 달아주시기 바랍니다." style="color:#caccce;" onfocus="this.value='';" />
							                  <a name="memoBttn" class="btn_gray"
											    <c:if test="${boardVO.memoWritableYn == 'Y'}">
												  <c:if test="${list.memoWritable || secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete}">
												    onclick="ebRead.actionBulletin('write-memo',<c:out value="${list.bltnNo}"/>);return false;"
												  </c:if>
												  <c:if test="${!list.memoWritable && !(secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete)}">
												    onclick="alert('작성자가 댓글쓰기를 허용하지 않았습니다.');return false;"
												  </c:if>
												</c:if>
											    <c:if test="${boardVO.memoWritableYn == 'N'}">
												  <c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}">
												    onclick="alert('댓글을 작성할 수 없습니다. 실명인증을 받으십시오.');return false;"
												  </c:if>
												  <c:if test="${boardVO.anonYn != 'R' || boardVO.anonYn != 'P'}">
												    onclick="alert('댓글을 작성할 수 없습니다. 로그인 하십시오.');return false;"
												  </c:if>
												</c:if>
											  />
							                  <span>등록</span>
							                  </a>
							                  </td>
							          </form>
									</c:if>
								</tr>
							</tr>
						</table>
						
						<table class="secondtable" cellpadding="0" cellspacing="0" summary="댓글리스트영역">
							<caption>댓글리스트영역</caption>
								<colgroup>
									<col width="15%" />
									<col width="85%" />
								</colgroup>
							<tr>
					        <c:if test="${!empty list.memoList}">
								<c:set var="memo" value="${list.memoList}"/>
								<jsp:useBean id="memo" type="java.util.List"/>
						              <c:forEach items="${memo}" var="mList">
						              <tr>
										<%-- <c:if test="${mList.memoLev != '1'}">
										      <span style=visibility:hidden><img src='' height=1 width=<c:out value="${mList.memoLevLen}"/>></span>
											  <c:out value="${boardVO.imgRe}" escapeXml="false"/>
										    </c:if> --%>
					                        <c:if test="${boardVO.anonYn == 'N'}">
					                          <th scope="row" class="L first"><c:out value="${mList.userNick}" escapeXml="false"/></th>
					                        </c:if>
					                        
						                    <td>
						                    	<span class="comment">
													<c:out value="${mList.memoCntt}" escapeXml="false"/>
						                    	</span>
							                    <c:if test="${mList.deletable == 'true'}">
												  <a onclick="ebRead.actionBulletin('delete-memo','<c:out value="${mList.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>')"/><img src="${pageContext.request.contextPath}/board/images/board/skin/appl/sub/icon_close.gif" alt="삭제" /></a>
						                        </c:if>
						                    </td>
						                 </tr>
						              </c:forEach>
					        </c:if>
				        </tr>
				        </table>
					</div> <!-- EnviewContentsArea -->
					</td>
				 </tr>
			 </div>
			 </table>
</c:forEach>
</body>
