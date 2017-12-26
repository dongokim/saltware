<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<link rel="stylesheet" href="<%=request.getContextPath() %>/kaist_portal/css/mobile/style.css" type="text/css" media="all" />
<link type="text/css" href="${cPath}/kaist/common/css/common.css" rel="stylesheet" />
<link type="text/css" href="${cPath}/kaist/skin/${boardSkin }/css/style.css" rel="stylesheet" /> 
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]--> 
<script type="text/javascript">
$(document).ready(function () {
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
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}" />
<c:forEach items="${bltnVOs}" var="list">
	<section role="main" id="container">
		<article class="content_gray">
			<div class="bord_wrap">
				<div class="bord_top">
					<p class="title" title="<c:out value="${list.bltnOrgSubj}" escapeXml="false"/>"><c:out value="${list.bltnOrgSubj}" escapeXml="false"/>
				  	<c:if test="${boardVO.cateYn == 'Y'}">
							<span class="status <c:out value="${fn:toLowerCase(list.cateId)}"/>"  style="display:inline-block;text-shadow: none;"><c:out value="${list.cateNm}"/></span>
					</c:if>
					</p>
					<span class="bltnInfo">
						<span class="date"><c:out value="${list.regDatimSF}" /></span>
						<span class="num_ck"><util:message key="eb.text.views"/> : <c:out value="${list.bltnReadCnt}" /></span>
						<span class="writer"><a id="mailLink_<c:out value="${list.userId}" />" title="send mail" style="cursor: pointer;"><c:out value="${list.userNick}" /></a></span>
						<span class="phone"><c:out value="${list.offcTel }"/></span>
					</span>				
					<c:set var="rsfile" value="${list.fileList}" />
					<c:if test="${not empty list.fileList}">
						<dl class="file_box">
							<c:forEach items="${rsfile}" var="fList" varStatus="status">
								<c:if test="${fList.atchType != 'I' }">
									<dd><a href="<c:out value="${fList.downloadUrl}"/>" target="download" class="req_file ellipsis" title="<c:out value="${fList.fileName}"/>"><c:out value="${fList.fileName}"/><c:if test="${fList.fileSize>0}">&nbsp;(<c:out value="${fList.sizeSF}"/>)</c:if></a>
									<c:if test="${langKnd == 'ko' }">
										<c:if test="${!empty fList.previewUrl }">
											<c:if test="${!empty sessionScope.isApp }">
												<a href="<c:out value="${fList.previewUrl}"  escapeXml="false"/>" target="_blank" title="<c:out value="${fList.fileName}"/>" class="btn_mobile_preview">
													<img src="<%=request.getContextPath()%>/kaist_portal/images/mobile/kor_preview.png" width="" class="img_download_mobile" alt="Preview" />
												</a>
											</c:if>
											<c:if test="${empty sessionScope.isApp }">
												<a href="javascript:void(0);" target="_self" onclick="ebUtil.goPreview('<c:out value="${fList.previewUrl}"  escapeXml="false"/>');" title="<c:out value="${fList.fileName}"/>" class="btn_mobile_preview">
													<img src="<%=request.getContextPath()%>/kaist_portal/images/mobile/kor_preview.png" class="img_download_mobile" alt="Preview"/>
												</a>
											</c:if>
										</c:if>
										<a href="<c:out value="${fList.downloadUrl}"/>" target="download" title="<c:out value="${fList.fileName}" escapeXml="false"/>" class="btn_mobile_download">
											<img src="<%=request.getContextPath()%>/kaist_portal/images/mobile/kor_download.png" class="img_download_mobile" alt="Download"/>
										</a>
									</c:if>
									<c:if test="${langKnd != 'ko' }">
										<c:if test="${!empty fList.previewUrl }">
											<c:if test="${!empty sessionScope.isApp }">
												<a href="<c:out value="${fList.previewUrl}"  escapeXml="false"/>" target="_blank" title="<c:out value="${fList.fileName}"/>" class="btn_mobile_preview">
													<img src="<%=request.getContextPath()%>/kaist_portal/images/mobile/eng_preview.png" class="img_download_mobile" alt="Preview"/>
												</a>
											</c:if>
											<c:if test="${empty sessionScope.isApp }">
												<a href="javascript:void(0);" target="_self" onclick="ebUtil.goPreview('<c:out value="${fList.previewUrl}"  escapeXml="false"/>');" title="<c:out value="${fList.fileName}"/>" class="btn_mobile_preview">
													<img src="<%=request.getContextPath()%>/kaist_portal/images/mobile/eng_preview.png" class="img_download_mobile" alt="Preview"/>
												</a>
											</c:if>
										</c:if>
										<a href="<c:out value="${fList.downloadUrl}"/>" target="download" title="<c:out value="${fList.fileName}" escapeXml="false"/>" class="btn_mobile_download">
											<img src="<%=request.getContextPath()%>/kaist_portal/images/mobile/eng_download.png" class="img_download_mobile" alt="Download"/>
										</a>
									</c:if>
									</dd>
								</c:if>
							</c:forEach>
						</dl>
					</c:if>
				</div>
				<table style="width: 100%;">
					<tr style="width: 100%;">
						<td style="width: 100%; overflow:hidden;">
							<div class="bord_body" style="width: 96%; padding: 0 2% 0 2%;">
								<c:out value="${list.bltnOrgCntt}" escapeXml="false" />	
							</div>
						</td>
					</tr>
				</table>
				<c:if test="${boardVO.evalYn == 'Y'}">
		        <c:set var="evalLevel" value="${boardVO.evalLevelList}"/>
		        <c:if test="${!empty evalLevel}">
				<div class="rating">
				  	<table summary="">
				  		<caption></caption>
				  		<colgroup>
				  			<col width="*" />
				  		</colgroup>
				  		
				  		<tr>
				  			<td>
				  				<div>
				  					<c:if test="${sessionScope.langKnd eq 'ko' }">	
				                	<span>현재 <span class="accent"><c:out value="${list.bltnEvalCntCF}"/></span> 분이 평가 하셨습니다. </span>
				                	<span class="">
				                		총점 : <span class="accent"><c:out value="${list.bltnEvalSumCF}"/></span>  
				                		/ 평균 : <span class="accent"><c:out value="${list.bltnEvalAvgCF}"/></span>
									</span>
				                	</c:if>
				                	<c:if test="${sessionScope.langKnd ne 'ko' }">	
				                	<span>Now <span class="accent"><c:out value="${list.bltnEvalCntCF}"/></span> Peoples are Evaluation this Post </span>
				                	<span class="">
				                		Total : <span class="accent"><c:out value="${list.bltnEvalSumCF}"/></span>  
				                		/ Avg. : <span class="accent"><c:out value="${list.bltnEvalAvgCF}"/></span>
									</span>
                					</c:if>
				  				</div>
				  				<div>
				  					<c:if test="${list.bltnEvaled == 'Y'}">
				            			<c:forEach items="${evalLevel}" var="levelList">
				            				<c:if test="${levelList.code == list.bltnEvalPnt}">
				            				<span>
				            					<c:if test="${sessionScope.langKnd eq 'ko' }">
				            					이미 <%-- '<c:out value="${levelList.codeName}"/>' (으)로  --%>평가하셨습니다.
				            					</c:if>
				            					<c:if test="${sessionScope.langKnd ne 'ko' }">
				            					You already Evaluation this Post
				            					</c:if>
				            				</span>
				            				</c:if>
				            			</c:forEach>
				            		</c:if>
				            		<c:if test="${list.bltnEvaled == 'N'}">
				            			<c:forEach items="${evalLevel}" var="levelList" varStatus="status">
				            			<span>
				            				<input name="pnt" type="radio" class="sel_radio" id="bd_basicview_sel${status.index }" value="<c:out value="${levelList.code}"/>"/>
				            				<label for="bd_basicview_sel${status.index }"><c:out value="${levelList.codeName}"/></label>
				            			</span>
				            			</c:forEach>
				            		</c:if>
				  				</div>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td>
				  				<div class="btn_box">
				  					<c:if test="${list.bltnEvaled == 'N'}">
						            <a href="#" onclick="ebRead.actionBulletin('eval','<c:out value="${list.bltnNo}"/>')" target="_self" class="btn medium white">
										<util:message key="eb.info.ttl.do.recommend" />
									</a>
									</c:if>
				  				</div>
				  			</td>
				  			
				  		</tr>
				  		
				  	</table>
				</div>
				</c:if>
				</c:if>
				<div class="comment">
					<div>
						<div class="btn_box_left">
							<c:if test="${boardVO.memoYn == 'Y' && boardVO.memoWritableYn == 'Y'}">
		  					<a href="#" class="btn medium white" onclick="$('.act_comment_view').toggle(); return false;">
		  						<util:message key="eb.info.ttl.memo" /> 
		  						<span class="accent">
		  							<c:if test="${!empty list.memoList}">
										<c:out value="${fn:length(list.memoList) }"/>
									</c:if>
									<c:if test="${empty list.memoList}">
										0
									</c:if>
								</span>
		  					</a>
		  					</c:if>
		  				</div>
		  				<div class="btn_box_right">
		  					<!-- <a href="#" class="btn medium white">좋아요 </a> -->
		  				</div>
		  				<div class="btn_box_right">
		  					<!-- <a href="#" class="btn medium white">신고하기 </a> -->
		  				</div>
					</div>
					
					<div class="clear"></div>
					
					<div class="comment_box act_comment_view">
						<c:if test="${!empty list.memoList}">
						<c:set var="memo" value="${list.memoList}"/>
						<c:forEach items="${memo}" var="mList">
						<div class="comment_line">
							<div class="">
								<span class=date><c:out value="${mList.regDatimF}"/></span> 
								<span class=writer> 
									<a title="send mail" id=mailLink_pnlee0416 style="cursor: pointer">
										<c:out value="${mList.userNick}" escapeXml="false"/>
										<c:if test="${fn:length(mList.userOrgName) > 0}">(<c:out value="${mList.userOrgName}"/>)</c:if>
									</a> 
								</span> 
								<!-- <span class="phone"></span> -->
							</div>
							<div class="comment_contents">
								<span><c:out value="${mList.memoCntt}" escapeXml="false"/></span>
							</div>
							<div class="btn_box_left">
			  					<!-- <a href="#" class="btn medium white">수정</a> -->
			  					<c:if test="${mList.deletable == 'true'}">
			  					<a href="#" class="btn medium white" onclick="ebRead.actionBulletin('delete-memo','<c:out value="${mList.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>')"><util:message key='ev.hnevent.label.delete'/></a>
			  					</c:if>
			  				</div>
			  				<div class="clear"></div>
						</div>
						</c:forEach>
						</c:if>
						
						<c:if test="${boardVO.memoYn == 'Y' && boardVO.memoWritableYn == 'Y'}">
						<form name="memoForm_<c:out value="${list.bltnNo}"/>">
						<div class="comment_line">
							<table summary="" class="comment_writebox">
								<caption></caption>
								<colgroup>
									<col width="*" />
									<col width="100px" />
								</colgroup>
								<tr>
									<td colspan="2">
										<span class="">
											<c:out value="${secPmsnVO.userNm}"/>
											<input type="hidden" name="userNick" value="<c:out value="${secPmsnVO.userNm}"/>"/>
										</span>
									</td>
								</tr>
								<tr>
									<td>
										<textarea id="tarea_1" rows="3" name="memoCntt" class="texta"></textarea>
									</td>
									<td>
										<a href="#" class="btn big white "><util:message key="cf.title.comment"/></a>
									</td>
								</tr>
							</table>
						</div>
						</form>
						</c:if>
					</div>
				</div>
				<c:if test="${secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete || list.userId == secPmsnVO.loginId}">
				<c:if test="${not empty  badBltnList}">
				<div class="declaration">
					<p class=""><util:message key="eb.prop.badBltn.history" /></p>
					<div class="content">
						<c:forEach items="${badBltnList}" var="badBltn" varStatus="status">
					  	<p>
					  		<c:if test="${secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete || (list.userId == secPmsnVO.loginId && badBltn.statCd !='R') }">
					  		<span><c:out value="${badBltn.badRegUserNm}" /></span>
					  		</c:if>
					  		<span><fmt:formatDate value="${badBltn.badRegDatim }" pattern="yyyy.MM.dd" /></span>
					  		<span>[<c:out value="${badBltn.statDesc}" />]<c:out value="${badBltn.badCdDesc}" /></span>
					  		<span><c:out value="${badBltn.badMemo}" /></span>
					  	</p>
					  	</c:forEach>
					</div>
				</div>
				</c:if>
				</c:if>
				
				<c:if test="${list.bltnGq eq 0 }">
		 			<c:if test="${boardVO.readReplyCnttYn == 'Y'}">
	 					<c:if test="${!empty list.replyList}">
	 						<c:set var="replyBltn" value="${list.replyList}"/>
	 						<c:set var="isBest" value="0"/>
			 				<c:forEach items="${replyBltn}" var="rbList">
			 					<c:if test="${rbList.bltnGq != '0'}">
					 			<%-- 채택된 녀석만 보여준다 --%>
					 			<c:if test="${rbList.bltnBestLevel == '9'}">
					 			<c:set var="isBest" value="9"/>
	 			<div class="answer_wrap">
	 				<div class="answer">
	 					<div class="answer_line">
							<div class="subject ">
								<span class="answer_icon"><util:message key="eb.info.ttl.adoption" /></span>
								<div class="title">
									<c:out value="${rbList.bltnOrgSubj}" escapeXml="false"/>
								</div>
							</div>
						</div>
						<div class="answer_line">
							<div>
								<span class=date><c:out value="${rbList.regDatimSF}"/></span> 
								<span class=writer> 
									<a title="send mail" id=mailLink_pnlee0416 ><c:out value="${rbList.userNick}"/></a> 
								</span> 
								<span class="phone"><c:out value="${rbList.offcTel}"/></span>
							</div>
						</div>
						<div class="answer_line">
							<div class="content">
								<c:out value="${rbList.bltnOrgCntt}" escapeXml="false"/>
							</div>
						</div>
	 				</div>
	 			</div>
					 			</c:if>
					 			</c:if>
			 				</c:forEach>
			 				
 				<div class="answer_wrap">
 					<span class="accent">${fn:length(replyBltn)-1 }</span>
 					<span><util:message key="eb.info.qna.answers" /></span>
 							<c:forEach items="${replyBltn}" var="rbList">
 							<c:if test="${rbList.bltnGq != '0'}">
					<div class="answer <c:if test="${rbList.bltnBestLevel == '9'}" >sel_knowpost</c:if>">
	 					<div class="answer_line">
							<div class="subject ">
								<div class="title">
									<c:out value="${rbList.bltnOrgSubj}" escapeXml="false"/>
								</div>
								<c:if test="${rbList.bltnBestLevel == '9'}" >
								<span class="answer_guide">
									<c:if test="${sessionScope.langKnd eq 'ko' }" >
									질문자에게 채택된 답변입니다. 
									</c:if>
									<c:if test="${sessionScope.langKnd ne 'ko' }" >
									This post was selected as the best answer.
									</c:if>
									<a href="#orgPost_${rbList.bltnNo }" class="act_selknowview" target="_self"><util:message key='eb.info.ttl.show.org.bltn' /></a>
								</span>
								</c:if>
							</div>
						</div>
						<div class="answer_line">
							<div>
								<span class=date><c:out value="${rbList.regDatimSF}"/></span> 
								<span class=writer> 
									<a title="send mail" id=mailLink_pnlee0416 ><c:out value="${rbList.userNick}"/></a> 
								</span> 
								<span class="phone"><c:out value="${rbList.offcTel}"/></span>
							</div>
						</div>
						<div class="answer_line">
							<div class="content">
								<c:out value="${rbList.bltnOrgCntt}" escapeXml="false"/>
							</div>
						</div>
	 				</div>
 							</c:if>
 							</c:forEach>
 				</div>
		 				</c:if>
			 		</c:if>
		 		</c:if>
		 		
			</div>
			
			<div class="btn_box">
				<a type="button" class="btn medium white" href="<%=request.getContextPath()%>/${boardPath }/<c:out value="${list.boardId}"/>" target="_top">
					<util:message key="ev.hnevent.label.list"/>
				</a>
				<%-- <c:if test="${boardVO.anonYn == 'N'}">
					<c:if test="${list.editable == 'true'}">
						<button type="submit" class="btn medium white" onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)"><util:message key="eb.info.btn.edit"/></button>
					</c:if>
					<c:if test="${list.editable == 'false'}">
						<c:if test="${list.editableUserId == '_is_admin_'}">
							<button type="submit" class="btn medium white" onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)"><util:message key="eb.info.btn.edit"/></button>
						</c:if>
					</c:if>
				</c:if> --%>
	        	<%-- <c:if test="${list.deletable == 'true'}">
					<a onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)" class="btn medium white"><util:message key="eb.info.btn.delete"/></a>
				</c:if>
				<c:if test="${list.deletable == 'false'}">
					<c:if test="${list.deletableUserId == '_is_admin_'}">
						<a onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)" class="btn medium white"><util:message key="eb.info.btn.delete"/></a>
					</c:if>
				</c:if> --%>
			</div>
		</article>
	</section>
	<script type="text/javascript">
		$.ajax({
			url: '<%=request.getContextPath()%>/kapi/info.face?bn=<c:out value="${list.bltnNo}"/>',
			success : function(data){
				$('#mailLink_<c:out value="${list.userId}" />').attr('href', 'mailto:'+data);
			}
		});
	</script>
</c:forEach>