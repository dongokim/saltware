	// 전페이지 공통 함수 정의. 
	// 모든 페이지에 선언해줘야한다.
	$(document).ready(function() {
	});

	// English : Korea 에 따라서 현재 페이지 언어를 바꿔준다. 
	function	 langKnd (lang){
		
		if (lang=="ko") { lang = "en"; }else{ lang = "ko"; }
		var url = "/lang/changeLang.face?langKnd="+lang;
		window.location.href=url;
	}// end function
	
	//F5키 이벤트를 받으면 윈도우 사이즈를 재조정한다.
	function noEvent() {
	    if (event.keyCode == 116) {
	        event.keyCode= 2;
	    }
	    else if(event.ctrlKey && (event.keyCode==78 || event.keyCode == 82))
	    {
	    }
	}
	document.onkeydown = noEvent;
	
	// 메인화면 검색창
	function search(){
		var value = $("#srch").val();
		var langKnd = $("#knd_home").val();
			
		if(langKnd == 'ko') {
			if(value==""){ alert("검색어를 입력해주세요."); return false; }
		} else {
			if(value==""){ alert("No search terms were entered."); return false; }
		}
	
		var url = "http://search.kaist.ac.kr/mobile/index.jsp?langCD=" + langKnd + "&searchTerm="
			
		top.location.href = url+value;
		$("#srch").val("");
	}//end function
	
	// 셔틀버스는 셔틀버스 + 주소로 링크를 보낸다. 
	function shuttle(url,knd){
		
		var resultUrl = '';
		if(typeof url=='undefined'){
			resultUrl = $("#shuttleUrl").val();
		}else{
			resultUrl=url;
		}
		top.location.href = resultUrl;
		
//		var langKnd = $("#knd_home").val();
//		if(typeof langKnd == 'undefined'){
//			langKnd = knd;
//		}
//		
//		if(langKnd=="ko"){
//			var title = "?langCD=" + langKnd + "&searchTerm=셔틀버스";
//			top.location.href = resultUrl+title;
//		}else{
//			var title = "?langCD=" + langKnd + "&searchTerm=shuttle";
//			top.location.href = resultUrl+title;
//		}
	}//end function
	
	function iframeResize(){
		
		
		
	}
