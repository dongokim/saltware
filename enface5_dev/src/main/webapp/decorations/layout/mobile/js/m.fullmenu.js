	$(document).ready(function() {
		
		// 사용자의 브라우저 크기를 체크한다.
		$("#height").val($(window).height());
		$("#width").val($(window).width());
		
		// 모아보기 메뉴를 세팅한다.
		//fullmenuSet();
		
		// 모아보기 비활성화
		$(".full_menu_wrap").attr("style","display:none");
				
		// 메인화면에 클릭이벤트 연결
		// 클릭시 취소 효과
		$(".wrap_slide_modal").click(function(){
			// 화면을 맨 위로 올린다.
			$("html,body").animate({scrollTop:0},0);
			fullmenu(false);
		});
	});

	//true = 열기 , false = 닫기
	function fullmenu(value){
		if(value){
			$(".full_menu_wrap").height($(".wrap_slide_modal").height());
			// 모아보기 활성화
			$(".full_menu_wrap").attr("style","display:block; width: 270px;");
			// 메뉴버튼 활성화
			$("#fullMenu").css("display", "none");
			$("#fullMenu_dis").css("display", "block");
			// 메인화면을 왼쪽으로 민다
			$("#wrap").animate({"left": "-=270px" }, "fast" );		
			// 메인화면을 색상을 바꿈
			$(".wrap_slide_modal").fadeTo(200, 0.01);
			// 브라우저 사이즈에 맞춰 여백 아래쪽을 채운다.
			$("#wrap").height($("html").height());
			$(".full_menu_wrap").height($("html").height());	
			
		}else{
			// 메뉴버튼 활성화
			$("#fullMenu_dis").css("display", "none");
			$("#fullMenu").css("display", "block");
			// 메인화면을 원위치 시킨다.
			$("#wrap").animate({"left": "+=270px" }, "fast", function(){
				// 모아보기 비활성화
				$(".full_menu_wrap").attr("style","display:none");
				// 메인화면 스크롤을 생성한다.
				$("#wrap").attr("style","overflow: auto; ");
			});
			
			// 메인화면 색상을 원래대로 바꿔놓는다.
			$(".wrap_slide_modal").css({"display":"none"});
			
			$("#wrap").height($("html").height());
			$(".full_menu_wrap").height($("html").height());
			
		}
	}
	
	function showMenuList(menuId){
		if($('#menu_' + menuId).hasClass('active')){
			if($('#' + menuId + '_ul').css('display') != 'none'){
				$('#' + menuId + '_ul').css('display', 'none');
			}
			$('#menu_' + menuId).removeClass('active');
		}
		else {
			$(".menu_depth2").css('display', 'none');
			$(".more_box").css("display", "none");
			$('.hiddenSubMenu').css('display', 'none');
			
			$("#more_box_"+menuId+"").css("display", "block");
			$("#" + menuId + "_ul").css('display', 'block');
			
			$('.menu_depth1_li').removeClass('active');
			$('#menu_' + menuId).addClass('active');
		}
		// 사이즈를 재조정한다.	
		$(window).resize();
	}
	
	//브라우저 사이즈 조정시 불려짐
	$(window).resize(function(){
		$("#wrap").height($("html").height());
		$(".full_menu_wrap").height($("html").height());	
				
		// 만약 메인페이지일경우, 사이즈 3단 사이즈 조정을 위해 아래 메서드를 호출한다.
		try {
			var url = location.href;
			if(url.match("mobile_home")!="null"){
				document.IframePortlet_14181.heightResize($(window).height(),$(window).width());
			}
		} catch(e) {
		}
	});
	
	function iframeController(value){
		$("#IframePortlet_14181").attr("style","height:"+(value)+"px");
	}
	
	//더보기 버튼을 컨트롤한다.
	function showMoreList(menuId){
		$(".more_box").css("display", "block");
		$("#more_box_"+menuId+"").css("display", "none");
		$("#" + menuId + "_ul .subMenuLink_li").css('display', 'block');
		
		// 사이즈를 재조정한다.	
		$(window).resize();
	}

	// 모아보기 메뉴를 세팅한다.
	function fullmenuSet(){
		var url = "/link/mobile/mobileList.face";
		
		// 한글 제목, 영어 제목, 숨김여부, 주소한글, 주소영문, 아이콘Url 
		$.ajax({	
			type: "GET",
			url: url,
			success : function(data){
				for(var i = 0 ; i < data.length ; i++){
					var menu = data[i];
					
					var isPass = false;
					switch(menu.linkId){
						//모아보기에서 공지사항과 게시판은 메뉴 캐시에 있는 것을 써야하기 때문에 패스
						case 163: case 201: isPass = true; break;
						default: break;
					}
					
					if(isPass) continue;
					// 포탈 관리자가 임의로 메뉴를 만들었다면 메뉴를 세팅한다.
					var table = '';
					table += '<li class="menu_depth1_li" id="mobileMenu_' + menu.linkId + '" >';																																										
					table += '<span class="bul2"></span><a class="menu_depth1_a" href="' + menu.mobileUrl + '" onclick="" name="title" target="' + menu.target + '">' + menu.linkNm + '</a>';
					table += '</li>';
					$("#moa").append(table);

				    // 사용여부가 Y 이면 메뉴를 세팅하고 N 이면 세팅하지 않는다.
					if (menu.mhdnFlg=='N') {
					   	$("#" + menu.linkNm + "").attr("style","display:none");
					}
				}
			}// end success
		});//end ajax
	}//end function

	function goMenu(url){
		//window.location.href = url;
	}
