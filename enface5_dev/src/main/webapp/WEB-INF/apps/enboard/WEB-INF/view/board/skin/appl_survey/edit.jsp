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
			<table cellpadding="0" cellspacing="0" summary="게시판등록">
			<caption>게시판등록</caption>
				 <colgroup>
					<col width="15%" />
					<col width="22%" />
					<col width="12%" />
					<col width="21%" />
					<col width="12%" />
					<col width="20%" />
				 </colgroup>
		 <form name="editForm" onsubmit="return false">
			<tr>
				<th scope="row" class="L first">제목</th>
				<td colspan="5"><label for="subject" style="display:none;"></label><input type="text" id="bltnSubj" class="subject" value="<c:out value="${bltnVO.bltnSubj}"/>" maxlength="120" /></td>
			</tr>
		   <c:if test="${boardVO.noticeYn == 'Y' && secPmsnVO.ableNotice == 'true'}">
		        <tr>
		       	  <th scope="row" class="L first">공지</th>
		         <td colspan="5">
			         <fieldset>
			            <input type="radio" name=bltnTopTag id=bltnTopTag <c:if test="${bltnVO.bltnTopTag == 'T'}">checked</c:if> value="T" onclick="ebUtil.setCheckedValue(document.editForm.bltnTopTag, 'T')"/>&nbsp;사용
					    <span style=visibility:hidden><img src='' height=1 width=30></span>
			            <input type="radio" name=bltnTopTag id=bltnTopTag <c:if test="${bltnVO.bltnTopTag == 'N'}">checked</c:if> value="N" onclick="ebUtil.setCheckedValue(document.editForm.bltnTopTag, 'N')"/>&nbsp;사용안함
					    <span style=visibility:hidden><img src='' height=1 width=30></span>
					    <label for="date_01"></label><input type="text" id="date_01" class="txt_70" value="2014.01.01" style="color:#9b9b9b; text-align:center;" /> ~ <label for="date_02"></label><input type="text" id="date_02" class="txt_70" value="2014.01.31" style="color:#9b9b9b; text-align:center;"/> <span><img src="${pageContext.request.contextPath}/board/images/board/skin/appl/icon_calender.gif" alt="달력" /></span>
			         </fieldset>        
		          </td>
		        </tr>
	        </c:if>
			<tr>
				<th scope="row" class="L first">작성자</th>
				<td>
					  <c:if test="${boardVO.anonYn == 'N'}">
					  <input type="text" maxlength="30" name="userNick" style="display: none;" class="form" readonly
						   <c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${secPmsnVO.userNick}"/>"</c:if>
				           <c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.userNick}"/>"</c:if>
					  />
					  </c:if>
					  <%--익명게시판인 경우--%>
					  <c:if test="${boardVO.anonYn == 'Y'}">
					    <input type="text" maxlength="30" name="userNick" style="width:300px" class="form"/>
					  </c:if>
			  		  <%--실명인증 게시판인 경우--%>
					  <c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}">
					    <input type="text" maxlength="30" name="userNick" style="width:300px" class="form" readonly value='<%=(String)session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))%>'/>
					  </c:if>
					  
					  <label name="userNick">
						   <c:if test="${boardSttVO.cmd != 'MODIFY'}"><c:out value="${secPmsnVO.userNick}"/></c:if>
				           <c:if test="${boardSttVO.cmd == 'MODIFY'}"><c:out value="${bltnVO.userNick}"/></c:if>
				      </label>
				</td>
				<th scope="row" class="L">작성일</th>
				<td>
                     <label id="wrdate"></label>
                     <script type="text/javascript">
					    var d = new Date();
					    var month = d.getMonth()+1;
					    var day = d.getDate();
					    var year = d.getFullYear();
					    var date = year + "." + month + "." + day;
					    document.getElementById("wrdate").innerText = date;
					  /*document.getElementById("date_01").innerText = date;
					    document.getElementById("date_02").innerText = date; */
					</script>
				</td>
			</tr>
			<tr>
				<th scope="row" class="L first">내용</th>
				<td colspan="7">
					<span class="fieldset">
						<fieldset>
								<input type="radio" name="conttxt" value="일반 TEXT" id="conttxt_0" />
								<label for="conttxt_0">일반 TEXT</label>
								<input type="radio" name="conttxt" value="HTML 편집기" id="conttxt_1" />
								<label for="conttxt_1">HTML 편집기</label>
						</fieldset>
					</span>
						<textarea id=editorCntt style="width:99%;height:100px"><c:out value="${bltnVO.bltnCntt}"/></textarea>
    					<input type=hidden name=bltnOrgCntt>
				</td>
			</tr>
			  	<input type=hidden name=delBoardId value="<c:out value="${boardVO.boardRid}"/>">
	        	<input type=hidden name=unDelList>
	        	<input type=hidden name=delList>
	        	<input type=hidden name=subId value=sub01>
		    </form>
			<%-- Secret --%>
			<c:if test="${boardVO.secretYn == 'Y' && secPmsnVO.isLogin == 'true'}">
		        <tr><td height="1" colspan="3" bgcolor="#dbdee7"></td></tr>          
		        <tr>
		          <td height="25" align="center" background="${pageContext.request.contextPath}/images/board/skin/<c:out value="${boardVO.boardSkin}"/>/bg_t_title2.gif" bgcolor="#FFFFFF" class="table_title_l">비밀글여부</td>
		          <td background="${pageContext.request.contextPath}/images/board/skin/<c:out value="${boardVO.boardSkin}"/>/img_t_bar2.gif" ></td>
		          <td class="table_list_l">
		            <input type=checkbox name=bltnSecretYn <c:if test="${bltnVO.bltnSecretYn == 'Y'}">checked</c:if>>&nbsp;Y/N
		          </td>
		        </tr>
	        </c:if>
			<tr>
				<th scope="row" class="L first">첨부</th>
				<td colspan="5">
					<div class="addFile">
						<form name=setFileList method=post target=invisible action="fileMngr?cmd=delete">
							<div class="filelist">
						          <%-- 기존 select 태그 방식 --%>
					              <select name="list" multiple="multiple" id="select">
					          	 	<option>파일은 10개까지 첨부할 수 있으며, 전체 용량은 100Mbyte를 넘을 수 없습니다.</option>
					              <c:if test="${boardSttVO.cmd == 'MODIFY'}">
								  <c:set var="file" value="${bltnVO.fileList}"/>
					              <c:forEach items="${file}" var="fList">
					                <option value="<c:out value="${fList.fileMask}"/>-<c:out value="${fList.fileSize}"/>"><c:out value="${fList.fileName}"/>&nbsp;(<c:out value="${fList.sizeSF}"/>)</option>
					               </c:forEach>
					              </c:if>
					              </select>
						          <input type=hidden name=semaphore value="<c:out value="${boardSttVO.semaphore}"/>">
						          <input type=hidden name=vaccum>

						          <%-- 게시판별 별도 디렉터리에 첨부파일 관리하는 것 때문에 추가 2009.02.25.KWShin. --%>
						          <input type=hidden name=delBoardId value="<c:out value="${boardVO.boardRid}"/>">
						          <input type=hidden name=unDelList>
						          <input type=hidden name=delList>
						          <input type=hidden name=subId value=sub01>
							</div>
						</form>
						<form name=setUpload method=post enctype="multipart/form-data" target=invisible action="fileMngr?cmd=upload">
							<input type=file name=filename style="width:350px"  id="fileField"  class="file_input" OnMouseOver=this.style.backgroundColor='#efebef' OnMouseOut=this.style.backgroundColor='' OnKeyDown="this.blur()">
							<div class="rightArea">
								<input type=hidden name=viewsize readonly style=background-color:#eeeeee;width:100%;text-align:center;border:none value='총 파일 크기: 0KB'>
					            <input type=hidden name=boardId value="<c:out value="${boardVO.boardRid}"/>">
					            <input type=hidden name=totalsize value=0>
					            <input type=hidden name=subId value=sub01>
					            <input type=hidden name=mode value=attach>
								<a onclick="ebEdit.uploadFile()" class="btn_black"><span>파일추가+</span></a> 
								<a onclick="ebEdit.deleteFile()" class="btn_black"><span>파일삭제-</span></a>
							</div>
						</form>
					</div>
				</td>
			</tr>
			<tr style="display: none;">
	          <td>
			    <span id=uploading style="display:none";>
				  <c:out value="${boardVO.imgLoading}" escapeXml="false"/>
				  <span id=uploadStatus></span>
				</span>
			  </td>
	        </tr>
			</table>
			<!-- table//-->  
			<!-- btnArea-->
			<div class="btnArea">
				<div class="rightArea">
					<a onclick="ebEdit.list()" class="btn_gray"><span>취 소</span></a> 
					<a onclick="ebEdit.save()" class="btn_gray"><span>등 록</span></a>
				</div>
			</div>
		</div>
	</td>
  </tr>
</table>