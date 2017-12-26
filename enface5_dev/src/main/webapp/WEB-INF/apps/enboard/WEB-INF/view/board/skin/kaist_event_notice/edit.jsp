<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,java.util.ArrayList" %>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enboard.vo.BoardVO,com.saltware.enboard.vo.BulletinVO,com.saltware.enboard.vo.BltnPollVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<div class="req_h3_box">
	<h3><c:out value="${boardNm}"/></h3>
	<c:if test="${boardSttVO.isOuter == null || boardSttVO.isOuter == 'N'}">
		<span class="req_location"><a onclick="ebUtil.goPage('/portal');" target="_self"><util:message key='eb.title.navi.home'/></a><a onclick="ebUtil.goPage('/portal/default/notice');" target="_self"><util:message key='eb.title.navi.notice'/></a><span><c:out value="${boardNm}"/></span></span>
	</c:if>
</div>	
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}"/>
<c:if test="${boardVO.boardDesc != null && boardVO.boardDesc != '' && !empty boardVO.boardDesc}">
	<div id="boardDescHTML" class="editorNotice"><c:out value="${boardVO.boardDesc}" escapeXml="false"/></div>
</c:if>
<form name="editForm" onsubmit="return false">
	<fieldset>
		<legend>폼제목</legend>
		<table summary="테이블설명" class="req_tbl_02">
			<caption>테이블명</caption>
			<colgroup>
				<col width="120px" />
				<col />
				<col width="120px" />
				<col />
			</colgroup>
			<tbody>
				<tr>
					<th scope="row" class="req_first"><label for=""><util:message key='eb.info.ttl.schedule'/></label></th>
					<td class="req_first" colspan="3">
						<input class="bltnBgnYmd" name="bltnBgnYmd" id="bltnBgnYmd" type="text" <c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.bltnBgnYmdF}"/>"</c:if>>
						<input name="bltnBgnYmdHm" id="bltnBgnYmdHm" type="hidden" value="<c:out value="${bltnVO.bltnBgnYmdDatimF}"/>">
						<select class="pointer" name="bltnBgnHm" id="bltnBgnHm"></select>
						<span class="req_bar">~</span>
						<input class="bltnEndYmd" name="bltnEndYmd" id="bltnEndYmd" type="text" <c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.bltnEndYmdF}"/>"</c:if>>
						<input name="bltnEndYmdHm" id="bltnEndYmdHm" type="hidden" value="<c:out value="${bltnVO.bltnEndYmdDatimF}"/>">
						<select class="pointer" name="bltnEndHm" id="bltnEndHm" ></select>
						<input type="checkbox" id="isAllday" class="pointer check_isAllday" name="isAllday" <c:if test="${bltnVO.isAllday == 'Y'}">checked="checked"</c:if>>
						<label for="isAllday" class="pointer"><util:message key='eb.info.ttl.allday'/></label>
						<%-- 
						<c:if test="${boardVO.bltnOpenYn == 'Y'}">
							<input type="checkbox" id="openYn" class="pointer check_isOpen" name="openYn" <c:if test="${bltnVO.bltnOpenYn == 'Y'}">checked="checked"</c:if>>
							<label for="openYn" class="pointer"><util:message key='eb.info.ttl.outeropen'/></label>
						</c:if>
						--%>
					</td>
				</tr>
					<c:if test="${boardVO.bilingualYn == 'Y' }">
						<c:if test="${boardSttVO.cmd == 'MODIFY'}">
							<c:forEach items="${bltnList}" var="bltn" varStatus="status">
								<tr>
									<th scope="row" class="req_first"><label for="id_title_<c:out value="${bltn.langKnd}"/>"><util:message key='eb.info.ttl.bltnSubj.${bltn.langKnd}'/></label></th>
									<td colspan="3" class="req_first">
										<input id="userNick_<c:out value="${bltn.langKnd}"/>" type="hidden" name="userNick_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.userNick}"/>"/>
										<input id="userOrgName_<c:out value="${bltn.langKnd}"/>" type="hidden" name="userOrgName_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.userOrgNm}"/>"/>
										<input id="id_title_<c:out value="${bltn.langKnd}"/>" style="width:90%" type="text" name="bltnSubj_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.bltnSubj}"/>" maxlength="120"/>
									</td>
								</tr>
								<tr>
									<th scope="row" class="req_first"><label for="id_place_<c:out value="${bltn.langKnd}"/>"><util:message key='eb.info.ttl.place.${bltn.langKnd}'/></label></th>
									<td colspan="3" class="req_first">
										<input id="id_place_<c:out value="${bltn.langKnd}"/>" style="width:90%" type="text" name="bltnPlace_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.bltnPlace}"/>" maxlength="120"/>
									</td>
								</tr>
								<tr>
									<td colspan="4">
										<label class="req_hidden" for="bltnCntt_<c:out value="${bltn.langKnd}"/>"><util:message key='eb.info.ttl.bltnCntt.${bltn.langKnd}'/></label>
										<textarea id="bltnCntt_<c:out value="${bltn.langKnd}"/>" name=editorCntt_<c:out value="${bltn.langKnd}"/> rows="15" style="width:99%"><c:out value="${bltn.bltnCntt}"/></textarea>
										<input type="hidden" name="bltnOrgCntt_<c:out value="${lang.code}"/>" />
									</td>
								</tr>
							</c:forEach>
						</c:if>
						<c:if test="${boardSttVO.cmd != 'MODIFY'}">
							<c:forEach items="${langKndList}" var="lang" varStatus="status">
								<tr>
									<th scope="row" class="req_first"><label for="id_title_<c:out value="${lang.code}"/>"><util:message key='eb.info.ttl.bltnSubj.${lang.code}'/></label></th>
									<td colspan="3" class="req_first">
										<input id="userNick_<c:out value="${lang.code}"/>" type="hidden" name="userNick_<c:out value="${lang.code}"/>"
											<c:if test="${lang.code == 'ko'}">value="<c:out value="${secPmsnVO.userNmKo}"/>"</c:if>
											<c:if test="${lang.code == 'en'}">value="<c:out value="${secPmsnVO.userNmEn}"/>"</c:if>
										/>
										<input id="userOrgName_<c:out value="${lang.code}"/>" type="hidden" name="userOrgName_<c:out value="${lang.code}"/>"
											<c:if test="${lang.code == 'ko'}">value="<c:out value="${secPmsnVO.userOrgNmKo}"/>"</c:if>
											<c:if test="${lang.code == 'en'}">value="<c:out value="${secPmsnVO.userOrgNmEn}"/>"</c:if>
										/>
		                                <input id="id_title_<c:out value="${lang.code}"/>" style="width:90%" type="text" name="bltnSubj_<c:out value="${lang.code}"/>" maxlength="120"/>
		                            </td>
								</tr>
								<tr>
		                            <th scope="row" class="req_first"><label for="id_place_<c:out value="${lang.code}"/>"><util:message key='eb.info.ttl.place.${lang.code}'/></label></th>
		                            <td colspan="3" class="req_first">
										<input id="id_place_<c:out value="${lang.code}"/>" style="width:90%" type="text" name="bltnPlace_<c:out value="${lang.code}"/>" maxlength="120"/>
		                            </td>
		                        </tr>
		                        <tr>
		                            <td colspan="4">
		                            	<label class="req_hidden" for="bltnCntt_<c:out value="${lang.code}"/>"><util:message key='eb.info.ttl.bltnCntt.${lang.code}'/></label>
		                            	<textarea id="bltnCntt_<c:out value="${lang.code}"/>" name=editorCntt_<c:out value="${lang.code}"/> rows="15" style="width:99%"></textarea>
						            	<input type="hidden" name="bltnOrgCntt_<c:out value="${lang.code}"/>" />
		                            </td>
		                        </tr>
							</c:forEach>
						</c:if>
					</c:if>
					<c:if test="${boardVO.bilingualYn == 'N' }">
						<input type="hidden" name="userNick"
							<c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${secPmsnVO.userNm}"/>"</c:if>
							<c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.userNick}"/>"</c:if>
						/>
						<input type="hidden" name="userOrgName"
							<c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${secPmsnVO.userOrgNm}"/>"</c:if>
							<c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.userOrgNm}"/>"</c:if>
						/>
						<tr>
							<th scope="row" class="req_first"><label for="id_title"><util:message key='eb.info.ttl.bltnSubj'/></label></th>
	                           <td colspan="3" class="">
	                           	<input id="id_title" style="width:90%" type="text" name="bltnSubj" value="<c:out value="${bltnVO.bltnSubj}"/>" maxlength="120"/>
	                           </td>
	                       </tr>
						<tr>
                           <th scope="row"><label for="id_place"><util:message key='eb.info.ttl.place'/></label></th>
                           <td colspan="3">
							<input id="id_place" style="width:90%" type="text" name="bltnPlace" value="<c:out value="${bltnVO.bltnPlace}"/>" maxlength="120"/>
                           </td>
                       </tr>
                       <tr>
                           <td colspan="4">
                           	<label class="req_hidden" for="bltnCntt"><util:message key='eb.info.ttl.bltnCntt'/></label>
                           	<textarea id="editorCntt" name=editorCntt rows="15" style="width:99%"><c:out value="${bltnVO.bltnCntt}"/></textarea>
			            	 <input type="hidden" name="bltnOrgCntt" />
                           </td>
                       </tr>
					</c:if>
				</tbody>
			</table>                    
		</fieldset>
	</form>  
      <!-- 파일업로드 양식//-->
      <c:if test="${boardVO.maxFileCnt > '0'}">
	<table summary="테이블설명" class="req_tbl_02" style="margin-top: 0px; board-top: 0px solid #e3e3e3;">
                 <caption>테이블명</caption>
                 <colgroup>
                     <col width="120px" />
                     <col />
                     <col width="120px" />
                     <col />
                 </colgroup>
                 <tbody>
			<tr>
				<th scope="row" class="req_last" rowspan="2"><util:message key='kaist.mobile.board.attachment'/></th>
				<td colspan="3" class="req_last">
					<form name=setUpload method=post enctype="multipart/form-data" target=invisible action="fileMngr?cmd=upload">
						<p class="exp"><util:message key='eb.info.ttl.attach.limit'/></p>
						<p class="exp"><util:message key='eb.info.ttl.attach.guide'/></p>
						<div class="req_file_wrap">
							<input type="text" id="fileText" name="fileText" class="fileText" readonly="readonly" />
							<span id="spanReqFileAdd" class="req_file_wrap_${langKnd}">
								<input type="file" name="filename" id="filename"  class="req_file_add" onchange="javascript:document.getElementById('fileText').value=this.value" />
							</span>
						</div>
						<span class="req_btn_pack small" style="float: left; margin-left: 2px;"><button type="button"  id="btnUploadFile" onclick="ebEdit.uploadFile()" ><util:message key='ev.title.add'/></button></span>
						<input type=hidden name=viewsize readonly="readonly" style=background-color:#eeeeee;width:100%;text-align:center;border:none value='총 파일 크기: 0KB'>
						<input type=hidden name=boardId value="<c:out value="${boardVO.boardRid}"/>">
						<input type=hidden name=totalsize value=0>
						<input type=hidden name=subId value=sub01>
						<input type=hidden name=mode value=attach>
					</form>
					<form name="setFileList" method="post" target="invisible" action="fileMngr?cmd=delete">
						<ol id="uploadFileList">
							<c:if test="${boardSttVO.cmd == 'MODIFY'}">
								<c:set var="file" value="${bltnVO.fileList}"/>
								<c:forEach items="${file}" var="fList">
									<li data="<c:out value="${fList.fileMask}"/>-<c:out value="${fList.fileSize}"/>"><c:out value="${fList.fileName}"/>&nbsp;(<c:out value="${fList.sizeSF}"/>)</li>
								</c:forEach>
							</c:if>
  						</ol>
					  	<span class="req_btn_pack small" style="float: left;"><button type="button" id="btnDeleteFile" onclick="ebEdit.deleteFile()"><util:message key='kaist.mobile.board.delete'/></button></span>
					  	<input type=hidden name=semaphore value="<c:out value="${boardSttVO.semaphore}"/>">
						<input type=hidden name=vaccum>
						<%-- 게시판별 별도 디렉터리에 첨부파일 관리하는 것 때문에 추가 2009.02.25.KWShin. --%>
						<input type=hidden name=delBoardId value="<c:out value="${boardVO.boardRid}"/>">
						<input type=hidden name=unDelList>
						<input type=hidden name=delList>
						<input type=hidden name=subId value=sub01>
					</form>
				</td>
			</tr>
		</tbody>
	</table>
	<span id=uploading style=display:none>
		<span id=uploadStatus></span>
	</span>
</c:if>
<div class="req_btn_wrap" style="border: none; background: none;">
	<div class="pr" style="float: left; margin-top:9px;">
		<span class="req_btn_pack basic"><button type="button" onclick="ebEdit.list()"><util:message key='kaist.mobile.board.list'/></button></span>
	</div>
	<div style="float: right; margin-top:9px;">
		<c:if test="${boardSttVO.cmd == 'MODIFY'}">
			<span class="req_btn_pack dark"><button type="button" onclick="ebEdit.save()"><util:message key='kaist.mobile.board.modification'/></button></span>
		</c:if>
		<c:if test="${boardSttVO.cmd != 'MODIFY'}">
			<span class="req_btn_pack dark"><button type="button" onClick="ebEdit.save()"><util:message key='kaist.mobile.board.registration'/></button></span>
		</c:if>
	</div>
</div>