<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.List,java.util.ArrayList" %>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enboard.vo.BoardVO,com.saltware.enboard.vo.BulletinVO,com.saltware.enboard.vo.BltnPollVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, target-densitydpi=medium-dpi" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/kaist/css/mobile/style.css" type="text/css" media="all">
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<script type="text/javascript">
	function setFileName(obj){
		var orgFileName = obj.value;
		var fileNameSplit = orgFileName.split("\\");
		var fileName = fileNameSplit[fileNameSplit.length-1];
		document.getElementById("selectedFileName").innerHTML = fileName;
	}

	$(document).ready(function() {
		$('#bltnBgnYmd').datepicker({ dateFormat: "yy.mm.dd", buttonImage: "<%=request.getContextPath()%>/kaist/images/portal/req/btn_cal.png" });
		$('#bltnEndYmd').datepicker({ dateFormat: "yy.mm.dd", buttonImage: "<%=request.getContextPath()%>/kaist/images/portal/req/btn_cal.png" });
		
		var hours = 0;
		var mins = 0;
		var hh = '';
		var hhmm = '';
		var hhmmText = '';
		for(var i = 0 ; i < 24 * (60/15); i++){
			var selected = '';
			var selected2 = '';
			hh24 = (hours< 10 ? '0' + hours : hours);
			hh12 = hh24 - 12;
			mm = (mins < 10 ? '0' + mins : mins);
			hh24mm = hh24 + ':' + mm;
			if(hh12 >= 0){
				hh12 = (hh12 < 10 ? '0' + hh12 : hh12);
				hh12mm = hh12 + ':' + mm + ' PM';
			} else hh12mm = hh24 + ':' + mm + ' AM';
			
			<c:if test="${boardSttVO.cmd == 'MODIFY'}">
			if(hh24mm == $('#bltnBgnYmdHm').val().substring(11,16)) selected = 'selected="selected"';
			if(hh24mm == $('#bltnEndYmdHm').val().substring(11,16)) selected2 = 'selected="selected"';
			</c:if>
			$('<option ' + selected + ' value=' + hh24mm + '>' + hh12mm + '</option>').appendTo('#bltnBgnHm');
			$('<option ' + selected2 + ' value=' + hh24mm + '>' + hh12mm + '</option>').appendTo('#bltnEndHm');
			
			mins += 15;
			if(mins >= 60) {
				mins = 0;
				hours++;
			}
		}
		
		$('.check_isAllday').change(function(){
			var $this = $(this);
			if($this.is(':checked')) {
				$('#bltnBgnHm').css('display', 'none');
				$('#bltnEndHm').css('display', 'none');
			} else {
				$('#bltnBgnHm').css('display', '');
				$('#bltnEndHm').css('display', '');
			}
		});
		
	});
</script>
<div class="h2_box">
	<h2 class="hidden"><c:out value="${boardNm}"/></h2>
	<ul class="location">
		<li class="first"><a href="/portal/default/mobile/notice" target="_top"><util:message key="kaist.mobile.fullmenu.notice"/></a></li>
		<li><a onclick="ebEdit.list()"><c:out value="${boardNm}"/></a></li>
	</ul>
	<a href="/portal/default/mobile/notice" target="_top" class="btn_a"><util:message key="kaist.mobile.board.otherboardview"/></a>
</div>
<section role="main" id="container">
	<article class="content_gray">
		<form name="editForm" onsubmit="return false">
			<fieldset>
				<table>
					<tr>
						<td class="req_first" colspan="4">
							<label for="bltnBgnYmd" class="req_hidden">기간 시작</label>
							<input style="width:80px" name="bltnBgnYmd" id="bltnBgnYmd" type="text"
							<c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.bltnBgnYmdF}"/>"</c:if>
							<c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${boardSttVO.start}"/>"</c:if>
							>
							<input name="bltnBgnYmdHm" id="bltnBgnYmdHm" type="hidden" value="<c:out value="${bltnVO.bltnBgnYmdDatimF}"/>">
							<select class="pointer" name="bltnBgnHm" id="bltnBgnHm" ></select>
							
							<span class="req_bar">~</span>
							
							<input style="width:80px"  name="bltnEndYmd" id="bltnEndYmd" type="text" 
							<c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.bltnEndYmdF}"/>"</c:if>
							<c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${boardSttVO.end}"/>"</c:if>
							>
							<input name="bltnEndYmdHm" id="bltnEndYmdHm" type="hidden" value="<c:out value="${bltnVO.bltnEndYmdDatimF}"/>">
							<select class="pointer" name="bltnEndHm" id="bltnEndHm" ></select>
							<br></br>
							<input type="checkbox" id="isAllday" class="pointer check_isAllday" name="isAllday" <c:if test="${bltnVO.isAllday == 'Y'}">checked="checked"</c:if>>
							<label for="isAllday" class="pointer">하루종일</label>
							<c:if test="${boardVO.bltnOpenYn == 'Y'}">
								<input type="checkbox" id="openYn" class="pointer check_isOpen" name="openYn" <c:if test="${bltnVO.bltnOpenYn == 'Y'}">checked="checked"</c:if>>
								<label for="openYn" class="pointer">외부 공개</label>
							</c:if>
						</td>
					</tr>
				</table>
				<c:if test="${boardVO.bilingualYn == 'Y' }">
					<div class="write_body">
						<c:if test="${bsForm.cmd == 'MODIFY'}">
							<c:forEach items="${bltnList}" var="bltn" varStatus="status">
								<div class="bltn_wrap <c:if test="${status.last}">bltn_wrap_last</c:if>">
									
									<p class="title_box" style="margin-top: 10px">
										<label for="id_title_<c:out value="${bltn.langKnd}"/>" title="<util:message key='eb.info.ttl.bltnSubj.${bltn.langKnd}'/>"><util:message key='eb.info.ttl.bltnSubj.${bltn.langKnd}'/></label>
										<input id="userNick_<c:out value="${bltn.langKnd}"/>" type="hidden" name="userNick_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.userNick}"/>"/>
										<input id="userOrgName_<c:out value="${bltn.langKnd}"/>" type="hidden" name="userOrgName_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.userOrgNm}"/>"/>
		                                <input id="id_title_<c:out value="${bltn.langKnd}"/>" style="width:90%" type="text" name="bltnSubj_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.bltnSubj}"/>" maxlength="128"/>
									</p>
									<p>
										<label for="id_place_<c:out value="${bltn.langKnd}"/>" title="<util:message key='eb.info.ttl.place.${bltn.langKnd}'/>"><util:message key='eb.info.ttl.place.${bltn.langKnd}'/></label>
										<input id="id_place_<c:out value="${bltn.langKnd}"/>" style="width:90%" type="text" name="bltnPlace_<c:out value="${bltn.langKnd}"/>" value="<c:out value="${bltn.bltnPlace}"/>" maxlength="128"/>
									</p>
									<p class="txt_box">
										<label class="req_hidden" for="bltnCntt_<c:out value="${bltn.langKnd}"/>" title="<util:message key='eb.info.ttl.bltnCntt.${bltn.langKnd}'/>"><util:message key='eb.info.ttl.bltnCntt.${bltn.langKnd}'/></label>
		                                <textarea id="bltnCntt_<c:out value="${bltn.langKnd}"/>" name=editorCntt_<c:out value="${bltn.langKnd}"/> rows="15" style="width:99%"><c:out value="${bltn.bltnCntt}"/></textarea>
		                                <input type="hidden" name="bltnOrgCntt_<c:out value="${lang.code}"/>" />
									</p>
									
								</div>
							</c:forEach>
						</c:if>
						<c:if test="${bsForm.cmd != 'MODIFY'}">
							<c:forEach items="${langKndList}" var="lang" varStatus="status">
								<div class="bltn_wrap">
									<p class="title_box" style="margin-top: 10px">
										<label for="id_title_<c:out value="${lang.code}"/>" title="<util:message key='eb.info.ttl.bltnSubj.${lang.code}'/>"><util:message key='eb.info.ttl.bltnSubj.${lang.code}'/></label>
										<input id="userNick_<c:out value="${lang.code}"/>" type="hidden" name="userNick_<c:out value="${lang.code}"/>"
											<c:if test="${lang.code == 'ko'}">value="<c:out value="${secPmsnVO.userNmKo}"/>"</c:if>
											<c:if test="${lang.code == 'en'}">value="<c:out value="${secPmsnVO.userNmEn}"/>"</c:if>
										/>
										<input id="userOrgName_<c:out value="${lang.code}"/>" type="hidden" name="userOrgName_<c:out value="${lang.code}"/>"
											<c:if test="${lang.code == 'ko'}">value="<c:out value="${secPmsnVO.userOrgNmKo}"/>"</c:if>
											<c:if test="${lang.code == 'en'}">value="<c:out value="${secPmsnVO.userOrgNmEn}"/>"</c:if>
										/>
		                                <input id="id_title_<c:out value="${lang.code}"/>" style="width:90%" type="text" name="bltnSubj_<c:out value="${lang.code}"/>" maxlength="128"/>
									</p>
								
									<p class="title_box" style="margin-top: 10px">
										<label for="id_place_<c:out value="${lang.code}"/>" title="<util:message key='eb.info.ttl.place.${lang.code}'/>"><util:message key='eb.info.ttl.place.${lang.code}'/></label>
										<input id="id_place_<c:out value="${lang.code}"/>" style="width:90%" type="text" name="bltnPlace_<c:out value="${lang.code}"/>" maxlength="128"/>
									</p>
									<p class="txt_box">
										<label class="req_hidden" for="bltnCntt_<c:out value="${lang.code}"/>" title="<util:message key='eb.info.ttl.bltnCntt.${lang.code}'/>"><util:message key='eb.info.ttl.bltnCntt.${lang.code}'/></label>
		                            	<textarea id="bltnCntt_<c:out value="${lang.code}"/>" name=editorCntt_<c:out value="${lang.code}"/> rows="15" style="width:99%"></textarea>
						            	<input type="hidden" name="bltnOrgCntt_<c:out value="${lang.code}"/>" />
									</p>
								</div>
							</c:forEach>
						</c:if>
					</div>
				</c:if>
				<c:if test="${boardVO.bilingualYn == 'N' }">
					<div class="write_body">
						<div class="bltn_wrap" >
							<input type="hidden" name="userNick"
								<c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${secPmsnVO.userNick}"/>"</c:if>
								<c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.userNick}"/>"</c:if>
							/>
							<input type="hidden" name="userOrgName"
								<c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${secPmsnVO.userOrgNm}"/>"</c:if>
								<c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.userOrgName}"/>"</c:if>
							/>
							<p class="title_box" style="margin-top: 10px">
								<label for="id_title" title="<util:message key='eb.info.ttl.bltnSubj'/>"><util:message key='eb.info.ttl.bltnSubj'/></label>	
								<input id="id_title" style="width:90%" type="text" name="bltnSubj" value="<c:out value="${bltnVO.bltnSubj}"/>" maxlength="128"/>
							</p>
							<p class="title_box" style="margin-top: 10px">
								<label for="id_place" title="<util:message key='eb.info.ttl.place'/>"><util:message key='eb.info.ttl.place'/></label>
								<input id="id_place" style="width:90%" type="text" name="bltnPlace" value="<c:out value="${bltnVO.bltnPlace}"/>" maxlength="128"/>
							</p>
							<p class="txt_box">
								<label class="req_hidden" for="bltnCntt" title="<util:message key='eb.info.ttl.bltnCntt'/>"><util:message key='eb.info.ttl.bltnCntt'/></label>
                            	<textarea id="editorCntt" name=editorCntt rows="15" style="width:99%"><c:out value="${bltnVO.bltnCntt}"/></textarea>
				            	 <input type="hidden" name="bltnOrgCntt" />
							</p>
						</div>
					</div>
				</c:if>
				<div class="btn_box" style="width: 95%; text-align: right;">
					<button type="button" class="btn medium white" onclick="ebEdit.list()"><util:message key="kaist.mobile.board.list"/></button>
		        	<button type="button" class="btn medium white" onclick="ebEdit.save()"><util:message key="kaist.mobile.board.registration"/></button>
		        </div>
			</fieldset>
		</form>
		<form name=setUpload method=post enctype="multipart/form-data" target=invisible action="fileMngr?cmd=upload">
			<input type=hidden name=viewsize readonly="readonly" style=background-color:#eeeeee;width:100%;text-align:center;border:none value='총 파일 크기: 0KB'>
			<input type=hidden name=boardId value="<c:out value="${boardVO.boardRid}"/>">
			<input type=hidden name=totalsize value=0>
			<input type=hidden name=subId value=sub01>
			<input type=hidden name=mode value=attach>
			<ol id="uploadFileList" style="display: none">
				<c:if test="${boardSttVO.cmd == 'MODIFY'}">
					<c:set var="file" value="${bltnVO.fileList}"/>
					<c:forEach items="${file}" var="fList">
						<li data="<c:out value="${fList.fileMask}"/>-<c:out value="${fList.fileSize}"/>"><c:out value="${fList.fileName}"/>&nbsp;(<c:out value="${fList.sizeSF}"/>)</li>
					</c:forEach>
				</c:if>
			</ol>
		</form>
	</article>
</section>