<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enview.security.EVSubject"%>
<%
	String langKnd = (String) pageContext.getSession().getAttribute("langKnd");
	String cPath = request.getContextPath();
	String userId = "";
	String groupId = "";
	if (EVSubject.getUserInfo() != null) {
		userId = EVSubject.getUserId();
		groupId = EVSubject.getUserInfo().getGroupId();
	}
	request.setAttribute("userId", userId);
	request.setAttribute("groupId", groupId);
%>
	<script type="text/javascript">
			$(document).ready(function(){
				try{
					parent.fn_enviewDivResize(($(".contents").height()+100));
					parent.fn_scrollTop();	
				}catch(e){console.log(e);}
			});
	</script>
	<!-- Contents -->
		<div class="contents"> 
		
			<!-- contents -->
			<h2 class="sub_title">보고 가이드</h2>
			<dl class="report_guide">
				<dt><h3 class="mgt0">자율보고란?</h3></dt>
				<dd>환자안전사고의 예방 및 의료 질 향상을 위해 개별의료기관의 환자안전 전담인력 또는 기관의 장, 보건의료인, 환자 및 보호자 등 보건의료서비스를 제공하거나 제공받는 사람이 인지하 환자안전사고 내용을 보고학습시스템 운영기관에 보고하는 것입니다.</dd>
			<dd>보고된 환자안전사고 중, 환자안전에 중대한 위해가 발생할 우려가 있는 환자안전사고 등 주의경보발령 대상사건을 관리하고 주의경보 발령 절차에 따라 주의경보 발령하고, 빠르게 전파하여 추후 해당 사고의 예방대책 공유할 수 있습니다.</dd>
			<dd>나아가 환자안전사고의 예방 및 의료 질 향상을 위한 환자안전사고 관리의 척도로써 개별 의료기관 및 환자안전사고 실태를 반영하여 환자안전사고 관리 기준 및 지표를 개발하는 업무에 활용합니다.</dd>
			</dl>
	
			<h3>자율보고 절차</h3>
			<div class="report_step">
				<dl class="step1">
					<dt><span>STEP 01.</span> 자율보고 신고</dt>
					<dd>개별의료기관의 환자안전 전담인력 또는 기관의 장, 보건의료인, 환자 및 보호자등 자율보고 보고서 서식에 따라 사고정보를 작성합니다.</dd>
				</dl>
				<dl class="step2">
					<dt><span>STEP 02.</span> 자율보고 접수</dt>
					<dd>담당자가 보고된 사고 내용을 확인 및 검증합니다. <br />검증이 완료되면 접수번호를 회신하고, 검증시작일로 부터 14일 이내 개인 식별 정보는 모두 삭제됩니다.</dd>
				</dl>
				<dl class="step3 clear">
					<dt><span>STEP 03.</span> 주의경보 발령</dt>
					<dd>중대한 위해가 발생할 우려가 있는 환자안전사고는 빠르게 전파하여 추후 사고의 예방대책을 공유합니다.</dd>
				</dl>
				<dl class="step4">
					<dt><span>STEP 04.</span> 보고자료활용</dt>
					<dd>보고된 환자안전사고를 활용하여 예방 및 질 향상을 위한 기준/지표를 개발하고 통계를 통하여 예방대책을 수립합니다.</dd>
				</dl>
			</div>
	
			<h3>온라인 신고</h3>
			<p class="mgt10">
				<%if(!("GRP_HEALTHCARE".equals(groupId)) && !("GRP_PLACEMENT_DEDICATED".equals(groupId)) && !("GRP_PLACEMENT_COUNSEL".equals(groupId))){ %>
					<a href="${pageContext.request.contextPath}/portal/kops/report/patient.page" target="_self" class="btn_rd">환자보호자용</a>
				<%}%>
				<%if(!("GRP_PLACEMENT_DEDICATED".equals(groupId)) && !("GRP_PLACEMENT_COUNSEL".equals(groupId))){ %>
					<a href="${pageContext.request.contextPath}/portal/kops/report/healthCare.page" target="_self" class="btn_rd mgl20">보건의료인용</a>
				<%}%>
				<%if("GRP_PLACEMENT_DEDICATED".equals(groupId) || "GRP_PLACEMENT_COUNSEL".equals(groupId)){ %>
					<a href="${pageContext.request.contextPath}/portal/kops/report/personnel.page" target="_self" class="btn_rd mgl20">전담인력용</a>
				<%}%>
			</p>
	
			<h3>오프라인 신고 방법</h3>
			<p class="pdt10">환자안전보고서 서식을 다운로드 받아 보고학습시스템 운영기관(의료기관평가인증원)에 이메일, 팩스, 우편 등의 방법으로 접수하시면 됩니다.</p>
			<ul class="report_offline">
				<li><span class="bl_pt1">이메일</span>patientsafety@koiha.or.kr</li>
				<li><span class="bl_pt1">팩   스</span>02-6499-1666</li>
				<li><span class="bl_pt1">주   소</span>서울특별시 영등포구 국회대로76길 10 K.B.C빌딩(여의도동 13-1번지) 8층 의료기관평가인증원 환자안전 담당자 앞</li>
				<li><span class="bl_pt1">양식 다운로드</span>
				<%if(!("GRP_HEALTHCARE".equals(groupId)) && !("GRP_PLACEMENT_DEDICATED".equals(groupId)) && !("GRP_PLACEMENT_COUNSEL".equals(groupId))){ %>
					<a href="${pageContext.request.contextPath}/patientDownLoader.jsp" target="_self" download class="btn_wt">환자보호자용 </a>
				<%}%>
				<%if(!("GRP_PLACEMENT_DEDICATED".equals(groupId)) && !("GRP_PLACEMENT_COUNSEL".equals(groupId))){ %>
					<a href="${pageContext.request.contextPath}/healthCareDownLoader.jsp" target="_self" download class="btn_wt mgl10">보건의료인용 </a>
				<%}%>
				<%if("GRP_PLACEMENT_DEDICATED".equals(groupId) || "GRP_PLACEMENT_COUNSEL".equals(groupId)){ %>
					<a href="${pageContext.request.contextPath}/personnelDownLoader.jsp" target="_self" download class="btn_wt mgl10">전담인력용 </a>
				<%}%>
				</li>
			</ul>
			<!-- //contents -->
		</div>