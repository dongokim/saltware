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
	<div class="contents">
		<!-- contents -->
		<h2 class="sub_title">신고가이드</h2>

		<h3>전담인력이란?</h3>
		<p class="pdt10">일정 규모 이상의 병원급 의료기관에서는 환자안전 및 의료 질 향상에 관한 업무를 전담하여 수행하는  환자안전 전담인력을 배치하여야 합니다.</p>
		
		<h4>배치</h4>
		<p class="pdt10">일정 규모 이상의 병원급 의료기관에서는 환자안전 및 의료 질 향상에 관한 업무를 전담하여 수행하는 환자안전 전담인력을 두어야 합니다.</p>
		<div class="board_list mgt10">
			<table summary="전담인력 배치 정보입니다" class="table_t2">
				<caption>전담인력 배치</caption>
				<colgroup>
					<col width="70%" />
					<col width="30%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="col"><label for="b_name" class="must">구분</label></th>
						<th scope="col"><label for="b_name" class="must">전담인력 수</label></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>200병상 이상인 병원급 의료기관(종합병원은 제외)</td>
						<td>1명 이상</td>
					</tr>
					<tr>
						<td>100병상 이상 500병상 미만의 종합병원</td>
						<td>1명 이상</td>
					</tr>
					<tr>
						<td>500병상 이상의 종합병원</td>
						<td>2명 이상</td>
					</tr>
				</tbody>
			</table>
		</div>
			
		<h4>자격기준</h4>
		<ul class="bl_list">
			<li>의사·치과의사·한의사 면허 취득 후 5년 이상 보건의료기관에 근무한 사람</li>
			<li>전문의 자격이 있는 사람</li>
			<li>간호사 면허 취득 후 5년 이상 보건의료기관에서 근무한 사람</li>
		</ul>
			
		<h4>업무</h4>
		<ul class="bl_list">
			<li>환자안전사고 정보의 수집·분석 및 관리·공유</li>
			<li>환자안전사고 예방 및 재발 방지를 위한 보건의료인 교육</li>
			<li>환자와 환자 보호자의 환자안전활동을 위한 교육</li>
			<li>환자안전활동 보고</li>
			<li>환자안전기준의 준수 점검</li>
			<li>환자안전지표의 측정ㆍ점검</li>
			<li>그 밖에 환자안전 및 의료 질 향상을 위하여 보건복지부장관이 특히 필요하다고 인정하는 사항</li>
		</ul>

		<h3>전담인력 배치 신고 절차</h3>
		<h4>전담인력 신고 절차</h4>
		<div class="report_step resister_guide1">
			<dl class="step1 mgb40">
				<dt><span>STEP 01.</span> 회원가입/로그인</dt>
				<dd>회원가입을 작성합니다. <br />(이미 회원가입을 하신 경우, 가입하신 아이디/비밀번호로 로그인합니다.)</dd>
			</dl>
			<dl class="step2 pdt0 mgb40">
				<dt><span>STEP 02.</span> 전담인력 신청</dt>
				<dd>
					<ul class="bl_list">
						<li>정보수정 하단의 “전담인력 신청” 작성합니다. 이미 배치 승인된 전담인력의 경우 보건의료기관장 “제출” 절차 없이 활동이 가능합니다.</li>
						<li>전담인력 배치신청이 처음이거나, 전담인력 배치 경력이 있으시면 “신고하기”의 “전담인력 배치신고”를 작성합니다. ( 해당 보건의료기관장이  최종 “제출”을 해야 환자안전본부에서 검증절차를 진행합니다. )</li>
					</ul>
				</dd>
			</dl>
			<dl class="step3 clear">
				<dt><span>STEP 03.</span> 신청서 제출</dt>
				<dd>해당 보건의료기관의 장이 배치신고내역을 확인하고 최종 “제출” 을 하셔야 합니다. <br />( 참고 :  CASE02  보건의료기관 장 )</dd>
			</dl>
			<dl class="step4">
				<dt><span>STEP 04.</span> 검증후 승인</dt>
				<dd>
					<ul class="bl_list">
						<li>환자안전본부에서 검증절차를 진행합니다.</li>
						<li>배치확정시 SMS (문자) 및 서비스포탈의 사용권한이 변경됩니다.</li>
					</ul>
				</dd>
			</dl>
		</div>

		<h4 class="">보건의료기관의 장, 제출 절차</h4>
		<div class="report_step resister_guide2">
			<dl class="step1">
				<dt><span>STEP 01.</span> 기관회원 가입</dt>
				<dd>“기관회원”으로  회원가입을 작성합니다. <br />회원가입시 “사업자등록증” 과 “인감파일” 을 첨부하여야 합니다.</dd>
			</dl>
			<dl class="step2">
				<dt><span>STEP 02.</span> 기관회원 승인</dt>
				<dd>환자안전본부에서 기관회원정보와 사업자등록증을 확인하여 결과를 SMS로 보내드립니다. <br />( 확인 이전에는 일반회원과 동등한 활동을 하실수 있습니다. )</dd>
			</dl>
			<dl class="step3 clear">
				<dt><span>STEP 03.</span> 신청서 제출</dt>
				<dd>“전담인력 신고 제출”에서  전담인력이 작성한 배치 신고 내역을 확인하시고 배치일자를 기재 한 후 “제출”버튼을 진행하시면 됩니다.</dd>
			</dl>
		</div>
		<!-- //contents -->
	</div>