<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.saltware.enhancer.banner.service.BannerUserVO" %>
<%@ page import="com.saltware.enview.sso.EnviewSSOManager" %>
<%@ page import="java.util.*" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Cache-Control" content="No-Cache" />
	<meta http-equiv="Pragma" content="No-Cache" />
	<meta name="robots" content="NOINDEX" />

	<title>배너(banner)</title>
	<link href="<%=request.getContextPath()%>/hancer/css/banner/core_banner_stuBg.css" rel="stylesheet" type="text/css" />
<%
	String contextname = request.getContextPath();
	String site_name = (String)request.getAttribute("site_name");
	
	String langKnd = (String)request.getAttribute("langKnd");

	List<BannerUserVO> bannerList = null;
	int List_Banner_size = 0 ;

	if(request.getAttribute("bannerList") == null){
		
	}else{
		bannerList = (List<BannerUserVO>)request.getAttribute("bannerList");
		List_Banner_size = bannerList.size();
	}
%>

<script type="text/javascript">
<!--
	var mainbanner_which = 0;
	var mainbanner_timer = [];
	var action = 'f';
	var main_totalSize = <%=List_Banner_size%>;


	function mainbanner_forward(num)
	{
	  mainbanner_which++;
	  if (mainbanner_which > main_totalSize-1 || mainbanner_which < 0) mainbanner_which = 0;
	  for (var i=0;i<main_totalSize;i++) {
		  var fld = document.getElementById("mainbanner_"+i);
		  fld.style.display = (i == mainbanner_which) ? 'block' : 'none';
	  }
	}
	
	function mainbanner_backward()
	{
	  mainbanner_which--;
	  if (mainbanner_which < 0) mainbanner_which = main_totalSize-1;
	  for (var i=0;i<main_totalSize;i++) {
		  var fld = document.getElementById("mainbanner_"+i);
		  fld.style.display = (i == mainbanner_which) ? 'block' : 'none';
	  }
	}

	function mainbanner_direct(num){
		 mainbanner_which++;
		  if (mainbanner_which > main_totalSize-1 || mainbanner_which < 0) mainbanner_which = 0;
		  for (var i=0;i<main_totalSize;i++) {
			  var fld = document.getElementById("mainbanner_"+i);
			  fld.style.display = (i == num) ? 'block' : 'none';
		  }
		  mainbanner_Stopdisplay();
	}
	
	function mainbanner_Stopdisplay() {
	  clearInterval(mainbanner_timer);
	}
	

	function mainbanner_Startdisplay(act) {
	  if (act == 'f')
	  {
		  action = 'f'
		  mainbanner_Stopdisplay();
		  mainbanner_timer = setInterval('mainbanner_forward()',3000);
	  }
	  else
	  {
		  action = 'b'
		  mainbanner_Stopdisplay();
		  mainbanner_timer = setInterval('mainbanner_backward()',3000);
	  }
	}

	function cmainbanner_forward(){
	  if (action == 'b')
	  {
		  action = 'f'
	  }
	  mainbanner_forward();
	  mainbanner_Stopdisplay();
	  mainbanner_Startdisplay('f');
	}

	function cmainbanner_backward()	{
	  if (action == 'f')
	  {
		  action = 'b';
	  }
	  mainbanner_backward();
	  mainbanner_Stopdisplay();
	  mainbanner_Startdisplay('b');
	}
	
	mainbanner_Startdisplay('f');

	//배너리스트 호출
	function PopBanner_List(){
		var PbUrl = "<%=request.getContextPath()%>/banner/bannerList.hanc";
		var PbStatus="toolbar=no,status=no,scrollbars=yes,resizable=yes";

		var top = 0;
		var left = 0;
		var w_width = "860px"; 	//창 넓이
		var w_height = "350px"; 	//창 높이
		var option;
		if(left == 0){
			var x= screen.width/2 - w_width/2; //화면중앙으로위치
			left = x;
		}
		if(top == 0){
			var y= screen.height/2 - w_height/2;
			top = y;
		}
		PbStatus = "top="+top+",left="+left+",width="+w_width+",height="+w_height+","+PbStatus;
		window.open(PbUrl, "_blank", PbStatus);		
	}
//-->
</script>
</head>
<body style='overflow-x:hidden; background-image: none;' >
	<div class="p_10 bannerzone">
<!-- 	<h2>배너<a href="javascript:PopBanner_List()"><span>더보기</span></a></h2> -->
		<c:if test="${empty bannerList}">
			<!-- 배너를 등록해 주세요 -->
			<div class="bannerimage">
				<p class="num">
					<a href="#" class="page"><img src="${themePath}/images/main/li_on.png" alt="첫번째페이지" /></a>
				</p>
				<p>
					<div class="type01"id="roll_banner"> <div><dl><dt></dt><dd class="place"><util:message key='ev.prop.event.nobanner'/></dd><dd class="period"></dd></dl></div></div>
				</p>
			</div>
		</c:if>
		
		<c:if test="${!empty bannerList}">
			<c:forEach items="${bannerList}" var="item" varStatus="status" begin="0" end="${fn:length(bannerList)}">
				<!-- 이미지 패스의 유무에 따라 이미지 링크 url 변수화를 먼저함. -->
				<c:set value="<%=contextname %>" var="contextnm"></c:set>
				
				<c:if test="${item.bannerImagePath == null}">
					<c:set value="#fff" var="URL1"></c:set>
				</c:if>
				<c:if test="${item.bannerImagePath != null}">
					<c:set value="${contextnm}${item.bannerImagePath}" var="URL1"></c:set>
				</c:if>		
				<!-- ---------------------------------------------------- -->
						<div class="bannerimage" id="mainbanner_<c:out value='${status.index}'/>"  style=" <c:if test="${item.bannerImagePath == null }">background-color:${URL1 };</c:if><c:if test="${status.index != 0 }">display:none; </c:if>">
								<c:if test="${item.bannerImagePath != null }">
									<a href = ${item.bannerLink} target = "_blank" >
										<img src="${URL1}" style="position: absolute; z-index: auto; width: 222px; height: 77px;">
									</a>
								</c:if>
								
								<c:if test="${item.bannerImagePath == null }">
									<a href = ${item.bannerLink} target = "_blank">
										<c:out value ="${item.bannerSrc}" escapeXml="false"/>
									</a>
								</c:if>
								
								<p class="num">
									<c:forEach items="${bannerList}" var="item" varStatus="status1" begin="0" end="${fn:length(bannerList)}" >
	                    				<a href="javascript:mainbanner_direct(${status1.index })" class="page">
	                    					<c:choose>
	                    						<c:when test="${status1.index == status.index }">
	                    							<img src="${themePath}/images/main/li_on.png" alt="두번째페이지" />
	                    							<%-- <img id="subimage_<c:out value='${status.index}'/>" src="<%=request.getContextPath() %>/hancer/images/eventCommon/user/stud/li_on.png" alt="${status.count }페이지" /> --%>
	                    						</c:when>
	                    						<c:otherwise>
	                    						<img src="${themePath}/images/main/li_off.png" alt="첫번째페이지" />
	                    							<%-- <img id="subimage_<c:out value='${status.index}'/>" src="<%=request.getContextPath() %>/hancer/images/eventCommon/user/stud/li_off.png" alt="${status.count }페이지" /> --%>
	                    						</c:otherwise>
	                    					</c:choose>
	                    				</a>
	                    				
									</c:forEach>
										 <%-- <a href="javascript:mainbanner_Stopdisplay()" class="page"><img src="<%=request.getContextPath() %>/hancer/images/eventCommon/user/stud/btn_pause.png" alt="일시정지" /></a>
	                  					 <a href="javascript:mainbanner_Startdisplay('f')" class="page"><img src="<%=request.getContextPath() %>/hancer/images/eventCommon/user/stud/btn_play.png" alt="시작" /></a> --%>
								</p>
						</div>
			</c:forEach>
		</c:if>
	</div>
</body>
</html>
