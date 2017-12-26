<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enboard.integrate.DefaultBltnExtnMapper,com.saltware.enboard.integrate.DefaultBltnExtnVO"%>
<%@ page import="com.saltware.enview.components.dao.ConnectionContext"%>
<%@ page import="com.saltware.enview.components.dao.ConnectionContextForRdbms"%>
<%@ page import="com.saltware.enboard.security.SecurityMngr"%>
<%@ page import="com.saltware.enboard.vo.BoardVO"%>
<%@ page import="com.saltware.enview.components.dao.DAOFactory"%>
<%@ page import="com.saltware.enboard.dao.AdminDAO"%>
<%@ page import="com.saltware.enboard.form.AdminCateForm"%>

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

<%--BEGIN::현재 게시판이 속한 카테고리의 아들카테고리(바로 한레벨 밑의 카테고리)에 속한 게시판들의 목록을 뿌리는 예제--%>
<%
	ConnectionContext connCtxt = null;
	try {
		connCtxt = new ConnectionContextForRdbms (true);

		BoardVO brdVO = (BoardVO)request.getAttribute ("boardVO");
		AdminDAO admDAO = (AdminDAO)DAOFactory.getInst().getDAO (AdminDAO.DAO_NAME_PREFIX);

		String cateId = admDAO.getCateOfBoard (brdVO.getBoardId(), connCtxt);
		List childCateList = admDAO.getChildCatebase (cateId, true, SecurityMngr.getLocale (request), connCtxt);
		List boardList = new ArrayList();
		AdminCateForm acForm = new AdminCateForm();
		for (int i=0; i<childCateList.size(); i++) {
			acForm.setCateId ((String)childCateList.get(i));
			boardList.addAll (admDAO.getCateBoard (acForm, null, true, connCtxt));
		}
		
		com.saltware.enboard.cache.CacheMngr ebCacheMngr = (com.saltware.enboard.cache.CacheMngr)Enview.getComponentManager().getComponent ("com.saltware.enboard.cache.CacheMngr");
		List directChildBoardList = new ArrayList();
		for (int i=0; i<boardList.size(); i++) {
			directChildBoardList.add (ebCacheMngr.getBoard ((String)boardList.get(i), SecurityMngr.getLocale (request)));
		}
		
		request.setAttribute ("dcBoardList", directChildBoardList);	
	
	} catch (Exception e) {
		if (connCtxt != null) connCtxt.rollback();
		// Ignore...
	} finally {
		if (connCtxt != null) connCtxt.release();
	}
%>
  <table width="69%" style="float: left;">
  <tr>
    <td>
      <div id="EnviewContentsArea">
		<div class="tsearchArea">
			<p>총 : <font color="blue"><b><c:out value="${boardSttVO.totalBltns}"/></b></font>개의 게시물이 있습니다.</p>
			<fieldset>
				 <form name="setSrch" OnSubmit="return ebList.srchBulletin()">
					<div class="sel_100">
						<select class="txt_100" id="srchType" name="srchType">
							<option value="Nick">작성자</option>
							<option value="Subj" selected="true">제목</option>
						</select>
					</div>
					<label for="search"></label><input type="text" id="srchKey" class="txt_150"/>
					<a href="#" class="btn_search" onClick="ebList.srchBulletin()"><span>검색</span></a>
				</form>
			</fieldset>
		</div>
		<!-- searchArea//-->
		
		<!-- cover : layer popup 작동시 사용-->
		<div class="cover">
			<!-- layerpopup-->
			<!-- writeinfor -->
			<div id="writeinfor" class="writeinfor">
				<p class="titleAera">
					<span class="title">작성자정보</span>
					<span class="layerClose"><a href="javascript:closeinfor();"><img src="${pageContext.request.contextPath}/board/images/board/skin/appl/sub/layer_close.gif" alt="닫기" /></a></span>
				</p>
			</div>
			  <form name="list" class="list" onsubmit="return false" >
				  <table width=100% border="0" cellspacing="0" cellpadding="0">
				  <thead>
					    <tr>
				          <c:if test="${boardVO.readMultiYn == 'Y'}">
				          <!--  <th width=22 align="center" background="/board/images/board/skin/enboard/bg_t_title.gif" class="first"><b>!</b></th> -->
				          </c:if>
				
				          <c:if test="${boardVO.ttlNoYn == 'Y'}">
				          <th width="1" align="center"  class="first"><b>번호</b></th>
				          </c:if>
				      
<%-- 				      <c:if test="${boardVO.ttlThumbYn == 'Y' || boardVO.listImageYn == 'Y'}">
				          <th width="60" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif" class=""><b>Thumb</b></th>
					      <th width="3" background="/board/images/board/skin/enboard/bg_t_title.gif"><img src="/board/images/board/skin/enboard/img_t_bar.gif" width="3" height="30"></th>
				          </c:if>         
				      
				          <c:if test="${boardVO.ttlCateYn == 'Y'}">
				          <c:if test="${boardVO.cateYn == 'Y'}">
				          <th width="60" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif" class=""><b>Categroy</b></th>
					      <th width="3" background="/board/images/board/skin/enboard/bg_t_title.gif"><img src="/board/images/board/skin/enboard/img_t_bar.gif" width="3" height="30"></th>
				          </c:if>
				          </c:if>
				          
				                         확장필드 Mapper 설정
					      <c:if test="${boardVO.extUseYn == 'Y'}">
						    <c:set var="extnMapper" value="${boardVO.bltnExtnMapper}"/>
							jsp:useBean id="extnMapper" type="com.saltware.enboard.integrate.DefaultBltnExtnMapper"/
						  </c:if>
				                        확장필드 사용예1
					      <c:if test="${boardVO.extUseYn == 'Y'}">
					      <c:if test="${extnMapper.ext_str2Yn == 'Y' && extnMapper.ext_str2TtlYn == 'Y'}">
					      <td width="67" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif"><b><c:out value="${extnMapper.ext_str2Ttl}"/></b></th>
					      <td width="3" background="/board/images/board/skin/enboard/bg_t_title.gif"><img src="/board/images/board/skin/enboard/img_t_bar.gif" width="3" height="30"></th>
					      </c:if>
					      <c:if test="${extnMapper.ext_str3Yn == 'Y' && extnMapper.ext_str3TtlYn == 'Y'}">
					      <th width="67" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif"><b><c:out value="${extnMapper.ext_str3Ttl}"/></b></th>
					      <th width="3" background="/board/images/board/skin/enboard/bg_t_title.gif"><img src="/board/images/board/skin/enboard/img_t_bar.gif" width="3" height="30"></th>
					      </c:if>
					      </c:if>
					      
					      <c:if test="${boardVO.ttlIconYn == 'Y'}">
					      <th width="3" background="/board/images/board/skin/enboard/bg_t_title.gif"></th>
				          </c:if>
					       --%>
					      <th width="1" align="center"  class="first"><b>구분</b></th>
					       
					       
					      <th width="200" align="center"  class="first"><b>제목</b></th>
					      
					      <c:if test="${boardVO.listAtchYn == 'Y'}">
					      <th width="1" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif"><b>첨부파일</b></th>
					      </c:if>
				
				          <%-- 확장필드 사용예2 --%>
					      <c:if test="${boardVO.extUseYn == 'Y'}">
					      <c:if test="${extnMapper.ext_str1Yn == 'Y' && extnMapper.ext_str1TtlYn == 'Y'}">
					      <th width="1" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif"><b><c:out value="${extnMapper.ext_str1Ttl}"/></b></th>
					      </c:if>
					      </c:if>
				
					      <c:if test="${boardVO.ttlNickYn == 'Y'}">
					      <th width="1" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif" class="C"><b>작성자</b></th>
				          </c:if>
					  
					      <c:if test="${boardVO.ttlRegYn == 'Y'}">
					      <th width="1" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif" class="C"><b>작성일</b></th>
				          </c:if>
					      
					      <c:if test="${boardVO.ttlReadYn == 'Y'}">
					      <th width="1" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif" class="C"><b>조회수</b></th>
				          </c:if>
<%-- 					  <%
							if (Enview.getConfiguration().getBoolean("board.red.flag.use")) {
						  %>
					      <th width="50" align="center" background="/board/images/board/skin/enboard/bg_t_title.gif" class="C"><b>읽음여부</b></th>
						  <%
							}
						  %> --%>
					    </tr>
				    </thead>
			
			        <%-- Bulletins are not exist.. --%>
			        <c:if test="${empty bltnList}">
				        <tr height=22>
				          <td align="center" colspan=20>등록된 게시물이 존재하지 않습니다.<input type=hidden name=chk></td>
				        </tr>
			        </c:if>
			
			        <%-- List of Bulletin --%>
			        <c:forEach items="${bltnList}" var="list">         
			        <!--  <tr><td height="1" colspan="28" bgcolor="#dbdee7" ></td></tr> -->
			        <tr onMouseOver=this.style.backgroundColor='#E8ECF9' onMouseOut=this.style.backgroundColor=''>
			          <c:if test="${boardVO.readMultiYn == 'Y'}">
			          <td class="C">
					    <c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
			              <span style=cursor:pointer onclick=ebList.checkBulletin(this) id=box name=box checkeditem=N  value=<c:out value="${list.bltnNo}"/>>
			                <c:out value="${boardVO.imgXox}" escapeXml="false"/>
			              </span>
						</c:if>
			          </td>
			          <!-- <td width="3" nowrap></td> -->
			          </c:if>
			    
			          <c:if test="${boardVO.ttlNoYn == 'Y'}">
			          <td class="C">
			            <c:if test="${list.selected == 'false'}">
			              <c:if test="${list.boardRow == '0'}"><c:out value="${boardVO.imgNotice}" escapeXml="false"/></c:if>
			              <c:if test="${list.boardRow != '0'}"><c:out value="${list.boardRow}"/></c:if>
			              <%--c:if test="${list.boardRow != '0'}"><c:out value="${list.bltnGn}"/></c:if--%>
			            </c:if>
			            <c:if test="${list.selected != 'false'}">
			              <%-- <c:out value="${boardVO.imgSelected}" escapeXml="false"/> --%>
			               <img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/imgSelected.gif">
			            </c:if>
			          </td>
			         <!--  <td width="3" nowrap></td> -->
			          </c:if>
			    
			     	 <td class="C"><b>진행중</b></td>
			          <c:if test="${boardVO.ttlThumbYn == 'Y' || boardVO.listImageYn == 'Y'}">
			          <td class="C" width="<c:out value="${boardVO.thumbSize}"/> height="<c:out value="${boardVO.thumbSize}"/>">
					    <c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
						  <img src="<c:out value="${list.thumbImgSrc50}"/>" onerror="<c:out value="${list.thumbImgOnError}"/>"
							   align="absmiddle" style="border:1px #dddddd solid" 
							   onload="ebList.imageResize(this,<c:out value="${boardVO.thumbSize}"/>,<c:out value="${boardVO.thumbSize}"/>)"
						  >
						</c:if>
			          </td>
			          <!-- <td width="3" nowrap></td> -->
			          </c:if>
			
			          <c:if test="${boardVO.ttlCateYn == 'Y'}">
			          <c:if test="${boardVO.cateYn == 'Y'}">
			          <td class="C">
					    <c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
					      <c:out value="${list.cateNm}" escapeXml="false"/>
						</c:if>
					  </td>
			         <!--  <td width="3" nowrap></td> -->
			          </c:if>
			          </c:if>
			
					 <%--  확장필드 데이터 설정 
				      <c:if test="${boardVO.extUseYn == 'Y'}">
					    <c:set var="extnVO" value="${list.bltnExtnVO}"/>
					  </c:if>
					  
			                        확장필드 사용예1 
				      <c:if test="${boardVO.extUseYn == 'Y'}">
				        <c:if test="${extnMapper.ext_str2Yn == 'Y' && extnMapper.ext_str2TtlYn == 'Y'}">
				          <td id="i<c:out value="${list.bltnNo}"/>" name="i<c:out value="${list.bltnNo}"/>" align=center>
							<c:if test="${list.delFlag == 'N'}">삭제표시되지 않은 정상적인 글일때만
				              <c:if test="${!empty list.bltnExtnVO}"><c:out value="${extnVO.ext_str2}"/></c:if>
							</c:if>
				          </td>
			              <td width="3" nowrap></td>
				        </c:if>
				        <c:if test="${extnMapper.ext_str3Yn == 'Y' && extnMapper.ext_str3TtlYn == 'Y'}">
				          <td id="i<c:out value="${list.bltnNo}"/>" name="i<c:out value="${list.bltnNo}"/>" align=center>
							<c:if test="${list.delFlag == 'N'}">삭제표시되지 않은 정상적인 글일때만
				              <c:if test="${!empty list.bltnExtnVO}"><c:out value="${extnVO.ext_str3}"/></c:if>
							</c:if>
				          </td>
			              <td width="3" nowrap></td>
				        </c:if>
				      </c:if> --%>
			
			          <%-- <c:if test="${boardVO.ttlIconYn == 'Y'}">문서아이콘을 보여준다.
			          <td class="table_list_c">
					    <c:if test="${!empty list.bltnBestLevel && list.bltnBestLevel > 0}">최고답변으로선정되었다
			              <c:if test="${list.bltnBestLevel == '9'}"><c:out value="${boardVO.imgBest9}" escapeXml="false"/></c:if>
			              <c:if test="${list.bltnBestLevel == '8'}"><c:out value="${boardVO.imgBest8}" escapeXml="false"/></c:if>
			              <c:if test="${list.bltnBestLevel == '7'}"><c:out value="${boardVO.imgBest7}" escapeXml="false"/></c:if>
			            </c:if>
						<c:if test="${empty list.bltnBestLevel || list.bltnBestLevel == 0}">
			              <c:if test="${boardVO.permitYn == 'Y' && list.bltnPermitYn == 'N'}">승인기능을 사용하는데 승인이 되질 않았다.
						    <c:out value="${boardVO.imgProhibit}" escapeXml="false"/>
						  </c:if>
			              <c:if test="${boardVO.permitYn == 'N' || list.bltnPermitYn != 'N' }">
			                <c:if test="${list.bltnIcon == 'A'}"><c:out value="${boardVO.imgDoc}" escapeXml="false"/></c:if>
			                <c:if test="${list.bltnIcon == 'B'}"><c:out value="${boardVO.imgFile}" escapeXml="false"/></c:if>
			                <c:if test="${list.bltnIcon == 'C'}"><c:out value="${boardVO.imgPoll}" escapeXml="false"/></c:if>
			                <c:if test="${list.bltnIcon == 'D'}"><c:out value="${boardVO.imgSecret}" escapeXml="false"/></c:if>
						  </c:if>
			            </c:if>
			          </td>
			          </c:if> --%>

			          <td class="L">
			            <c:if test="${list.bltnLev != '1'}">
			              <span style=visibility:hidden><img height=1 width=<c:out value="${list.bltnLevLen}"/>></span>
			              <c:out value="${boardVO.imgRe}" escapeXml="false"/>
			            </c:if>
						<c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
			              <c:if test="${boardVO.ttlPntYn == 'Y'}">
			                <c:if test="${list.betPnt > '0'}"><font color=#A51818>$<c:out value="${list.betPnt}"/></font></c:if>
			              </c:if>
					 	  <a style=cursor:pointer OnMouseOver=this.style.textDecoration='underline' OnMouseOut=this.style.textDecoration='none'
							    <c:if test="${list.readable == 'true'}">
							      <%--게시판이 가상게시판일 수도 있으므로, 게시물이 아니라 게시물이 속한 게시판의 boardId 를 이용하여 읽기화면으로 가야 한다.--%>
								  <c:if test="${boardVO.mergeType == 'A'}">
							        onclick="ebList.readBulletin('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
								  </c:if>
								  <c:if test="${boardVO.mergeType != 'A'}">
							        onclick="ebList.readBulletin('<c:out value="${list.eachBoardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
								  </c:if>
							    </c:if>
							    <c:if test="${list.readable == 'false'}">
								  onclick="alert('작성자가 글읽기를 허용하지 않았습니다');"
							    </c:if>
							  >
						    <%--c:out value="${list.bltnOrgSubj}" escapeXml="false"/--%>
						    <c:out value="${list.bltnOrgSubj}"/>
							<%--게시기간설정기능을 사용하는 게시판의 공지글이고 현재 관리자/운영진/게시판관리자이면 게시기간을 출력한다.--%>
							<c:if test="${boardVO.termYn == 'Y' && list.boardRow == '0' && (secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete)}">
							  <br>게시시작일:<c:out value="${list.bltnBgnYmdF}"/> 게시종료일:<c:out value="${list.bltnEndYmdF}"/>
							</c:if>
			              </a>
			              <c:if test="${boardVO.ttlReplyYn == 'Y'}">
			                <c:if test="${list.bltnReplyCnt != '0'}">
			                  <c:out value="${boardVO.imgReCnt}" escapeXml="false"/>
			                  <font color="#CC4848">(<c:out value="${list.bltnReplyCnt}"/>)</font>
			                </c:if>
			              </c:if>
			              <c:if test="${boardVO.ttlMemoYn == 'Y'}">
			                <c:if test="${list.bltnMemoCnt != '0'}">
			                  <c:out value="${boardVO.imgMemo}" escapeXml="false"/>
			                  <font color="#CC4848">(<c:out value="${list.bltnMemoCnt}"/>)</font>
			                </c:if>
			              </c:if>
			              <c:if test="${boardVO.ttlNewYn == 'Y'}">
			                <c:if test="${list.recentBltn == 'Y'}">
			                	<%-- <c:out value="${boardVO.imgNew}" escapeXml="false"/> --%>
			                	<img src="/decorations/layout/tile/images/sub/imgNew.gif">
			                </c:if>
			              </c:if>
			              <c:if test="${boardVO.listCnttYn == 'Y'}">
			              <table border=0 cellspacing=0 cellpadding=0 width=100% style="table-layout:fixed">
			                <tr>
			                  <td width=22></td>
			                  <td style="padding-left:5px;word-wrap:break-word;word-break:break-all"><c:out value="${list.bltnCntt}"/></td>
			                </tr>
			              </table>
			              </c:if>
						</c:if>
						<c:if test="${list.delFlag == 'y'}"><%--삭제표시된 답글있는 글--%>
						  삭제된 글입니다.
						</c:if>
			          </td>
				      <c:if test="${boardVO.listAtchYn == 'Y'}">
			            <td width="3" nowrap></td>
						<c:if test="${list.fileDownable}">
				          <td id=i<c:out value="${list.bltnNo}"/> name=i<c:out value="${list.bltnNo}"/> align=center>
							<c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
			                  <c:if test="${!empty list.fileList}">
				    	        <c:forEach items="${list.fileList}" var="fList">
						          <c:out value="${fList.downloadImgLink}" escapeXml="false"/>
				                </c:forEach>
			                  </c:if>
							</c:if>
				          </td>
						</c:if>
				      </c:if>
			
			          <%-- 확장필드 사용예2 --%>      
				      <c:if test="${boardVO.extUseYn == 'Y' && !empty list.bltnExtnVO}">
				        <c:if test="${extnMapper.ext_str1Yn == 'Y' && extnMapper.ext_str1TtlYn == 'Y'}">
			              <td width="3" nowrap></td>
				          <td id=i<c:out value="${list.bltnNo}"/> name=i<c:out value="${list.bltnNo}"/> align=center>
							<c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
				              <c:out value="${extnVO.ext_str1}"/>
							</c:if>
				          </td>
				        </c:if>
				      </c:if>
			
			          <c:if test="${boardVO.ttlNickYn == 'Y'}">
			         <!--  <td width="3" nowrap></td> -->
			          <td class="C">
						<c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
					      <c:out value="${list.userNick}"/>
						</c:if>
					  </td>
			          </c:if>
			      
			          <c:if test="${boardVO.ttlRegYn == 'Y'}">
			         <!--  <td width="3" nowrap></td> -->
			          <td class="C">
						<c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
					      <c:out value="${list.regDatimSF}"/> ~ <c:out value="${list.regDatimSF}"/>
						</c:if>
					  </td>
			          </c:if>
			      
			          <c:if test="${boardVO.ttlReadYn == 'Y'}">
			          <!-- <td width="3" nowrap></td> -->
			          <td class="C">
						<c:if test="${list.delFlag == 'N'}"><%--삭제표시되지 않은 정상적인 글일때만--%>
			              <c:if test="${list.bltnReadCnt >= boardVO.raiseCnt}"><b><font color=<c:out value="${boardVO.raiseColor}"/>></c:if>
			              <c:out value="${list.bltnReadCnt}"/>&nbsp;
			              <c:if test="${list.bltnReadCnt >= boardVO.raiseCnt}"></b></font></c:if>
						</c:if>
			          </td>
			          </c:if>
					 <%--BEGIN::게시물 읽음여부를 표시할 때만--%>
			         <!--  <td width="3" nowrap></td> -->
			         <%--  <td class="C">
						<c:if test="${list.delFlag == 'N'}">삭제표시되지 않은 정상적인 글일때만
					      <c:if test="${empty list.firstReadDatim}">N</c:if>
					      <c:if test="${!empty list.firstReadDatim}">Y</c:if>
						</c:if>
					  </td> --%>
					  <%--END::게시물 읽음여부를 표시할 때만--%>
			        </tr>
			        </c:forEach>
			        <%-- End of List of Bulletin --%>
			       <!--  <tr><td height="2" colspan="28" bgcolor="#dbdee7" ></td></tr> -->
			      </table>
			      </form>
		</div>
		<!-- cover//-->
		<!-- tcontrol-->
		<div class="tcontrol">
			<!-- listcounts-->
			<div class="listcounts">
				<div class="sel_100">
					<select class="txt_100">
						<option>10건씩보기</option>
						<option>30건씩보기</option>
						<option>50건씩보기</option>
						<option>70건씩보기</option>
					</select>
				</div>
			</div>
			<!-- listcounts//-->
		</div>
		<!-- tcontrol//-->
		<!-- btnArea-->
	</div>
    </td>
  </tr>
  <tr>
    <td align="right">
      <table width=100% border="0" cellspacing="0" cellpadding="0">
        <tr>
          <c:if test="${boardVO.readMultiYn == 'Y'}">
	          <td width=22 align=center onClick=ebList.readBulletin('<c:out value="${boardVO.boardId}"/>',-1) style=cursor:pointer>
	            <c:out value="${boardVO.imgAllsee}" escapeXml="false"/>
	          </td>
          </c:if>
          

		  
		  <td align="right" valign=middle>
		  
		    <div class="paging">
          	  <td id="pageIndex" align="center" class="board_num"></td>
		    </div>
		  
			<c:if test="${boardVO.writableYn == 'Y'}">
			  <c:if test="${boardVO.mergeType == 'A'}">
			  	<div class="btnArea">
					<div class="rightArea"><a onClick="ebList.writeBulletin()" class="btn_gray"><span>등 록</span></a></div>
				</div>
				<%-- <a style=cursor:pointer><c:out value="${boardVO.imgWrite}" escapeXml="false"/></a> --%>
			  </c:if>
			</c:if>
			<c:if test="${boardVO.writableYn == 'N'}">
			  <c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}">
			    <c:if test="${boardVO.mergeType == 'A'}">
				  <a onClick=ebList.writeBulletin() style=cursor:pointer><c:out value="${boardVO.imgWrite}" escapeXml="false"/></a>
			    </c:if>
			  </c:if>
			</c:if>
		  </td>
        </tr>
      </table>
    </td>
  </tr>
  </table>

<script language="javascript">
	var currentPage = <c:out value="${boardSttVO.page}"/>;
	var totalPage   = <c:out value="${boardSttVO.totalPage}"/>;
	var setSize     = 10; <%--하단 Page Iterator에서의 Navigation 갯수--%>
	var imgUrl      = "/decorations/layout/tile/images/sub/";
	var color       = "808080";
	
	var afpImg = "imgFirstActive.gif";
	var pfpImg = "imgFirstPassive.gif";
	var alpImg = "imgLastActive.gif";
	var plpImg = "imgLastPassive.gif";
	var apsImg = "imgPrev10Active.gif";
	var ppsImg = "imgPrev10Passive.gif";
	var ansImg = "imgNext10Active.gif";
	var pnsImg = "imgNext10Passive.gif";
	
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
	if (currentPage > setSize) {
		firstP = "<font class='noline' onclick=ebList.next('1') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		firstP += " onmouseout=this.style.textDecoration='none'> <img src='"+imgUrl+afpImg+"' align=absmiddle border=0 alt='맨 앞 페이지로 가기'></font>";
	    cursor = startPage - 1;
	    prevSet = "<font class='noline' onclick=ebList.next('"+cursor+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		prevSet += " onmouseout=this.style.textDecoration='none'><img src='"+imgUrl+apsImg+"' align=absmiddle border=0 alt='열 페이지 앞으로 가기'></font>";
	} else {
		firstP  = "<img src='"+imgUrl+pfpImg+"' align=absmiddle border=0>"; 
		prevSet = "<img src='"+imgUrl+ppsImg+"' align=absmiddle border=0>"; 
	}
		
	cursor = startPage;
	while( cursor <= endPage ) {
		if( cursor == currentPage ) 
	   		curList += "<font color=#"+color+">"+currentPage+"</font>&nbsp;";
		else {
	    	curList += "<font onclick=ebList.next('"+cursor+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
			curList += " onmouseout=this.style.textDecoration='none'>"+cursor+"</font>&nbsp;";
		}
		cursor++;
	}
		     
	if ( totalPage > endPage) {
		lastP = "<font onclick=ebList.next('"+totalPage+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		lastP += " onmouseout=this.style.textDecoration='none'><img src='"+imgUrl+alpImg+"' align=absmiddle border=0 alt='맨 끝 페이지로 가기'></font>";
		cursor = endPage + 1;  
		nextSet = "<font onclick=ebList.next('"+cursor+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		nextSet += " onmouseout=this.style.textDecoration='none'><img src='"+imgUrl+ansImg+"' align=absmiddle border=0 alt='열 페이지 뒤로 가기'></font>";
	} else {
		lastP  = "<img src='"+imgUrl+plpImg+"' align=absmiddle border=0>"; 
		nextSet = "<img src='"+imgUrl+pnsImg+"' align=absmiddle border=0>"; 
	}
	
	curList = firstP +"&nbsp;"+ prevSet +"&nbsp;&nbsp;"+ curList +"&nbsp;&nbsp;"+ nextSet +"&nbsp;"+ lastP;
	document.getElementById("pageIndex").innerHTML = curList;
</script>
