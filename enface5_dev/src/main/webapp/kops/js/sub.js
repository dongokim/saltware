
jQuery(function(){
	//faq
	$('.board_box .faq a.tit').click(function(event){
		var $target=$(event.target);
		if($target.is('.open')){
			$(this).removeClass('open').next('.cont').slideUp('fast', function(){
				// FAQ 게시판 리사이즈
				parent.fn_childIframeAutoresize(parent.document.getElementById(window.name), parseInt($("#container").height()+100));
				parent.$("#container").height($("#container").height()+100);
			});
		}else{
			$('#board_box .faq a.tit').removeClass('open').next('.cont').slideUp('fast');
			$(this).addClass('open').next('.cont').show('fast', function(){
				// FAQ 게시판 리사이즈
				parent.fn_childIframeAutoresize(parent.document.getElementById(window.name), parseInt($("#container").height()+100));
				parent.$("#container").height($("#container").height()+100);
			});
		};
		return false;
	});

	//userInfo
	$('.board_box .tab_table a.tab').click(function(event){
		var $target=$(event.target);

		if($target.is('#counselTab')){
			// 자문위원 리사이즈 & 인증
			parent.fn_authenticate('Y');
			updateUser_resizeCounsel();
		}else{
			//  리사이즈
			updateUser_resize();
		};
		return false;
	});
});

$(document).ready(function() {
	$(window).scroll(function() {
		if ($(this).scrollTop() < 100) {
			$('#header').addClass('header_va');
		}else {
			$('#header').removeClass('header_va');
		}
	});
	$('#header').addClass('header_va');
	
	//모든 라디오 인풋이 변경되었을 때에 대한 루프
	$( "input[type=radio]" ).change(function() {
		

	//현재 변경된 RADIO인풋의 이름에 해당하는 모든 RADIO에 대한 루프
			$.each($("input[type=radio][name="+$(this).attr("name")+"]"),function(i) {
		//		  alert( i + ": " );
		
		//현재 이름에 해당하는 모든 라디오 박스를 비선택으로 변경하고
		//ov_label을 제거해준다.
		//	$( "label[for='"+$(this).attr("id")+"']" ).removeClass("ov_label");

		//만약 text_radio라는 클래스를 가지고 있는 radio 인풋이라면 해당 텍스트를 readonly로 바꾸고 내용도 삭제한다.
			if($(this).hasClass("text_radio")){
				$.each($(this).siblings(),function(j){
					if($(this).hasClass("text")){
						$(this).attr("readonly",true);
						$(this).val("");
					}
				});
			}
		});

	//현재 변경된 RADIO인풋의 id를 확인해서 해당 id를 for값으로 가지고 있는 label에 ov_label을 addClass해준다.
		$( "label[for='"+$(this).attr("id")+"']" ).addClass("ov_label");
	
	//현재 변경된 RADIO 인풋이 text_radio 클래스를 가지고 있다면 
	//같은 depth의 노드중에 text class를 가지고있는 input박스를 활성화해주고
	//내용을 비운다.
	//##따라서 기타 기능을 사용하려면 text_radio클래스를 해당 radio 인풋에 추가해주고
	//##같은 depth내에 있는 input text에게도 text클래스를 추가해줘야한다.
		if($(this).hasClass("text_radio")){
			$.each($(this).siblings(),function(i){
				if($(this).hasClass("text")){
					$(this).attr("readonly",false);
					$(this).val("");
				}
			});
		}
	});
	////라디오 루프 종료



	//모든 체크박스 인풋이 변경되었을 때에 대한 루프
//	$( "input[type=checkbox]" ).change(function() {
//		if($(this).prop("checked")){
//			//현재 변경된 체크박스인풋의 id를 확인해서 해당 id를 for값으로 가지고 있는 label에 ov_label을 addClass해준다.
//			$( "label[for='"+$(this).attr("id")+"']" ).addClass("ov_label");

//			if($(this).hasClass("text_check")){
//				$.each($(this).siblings(),function(i){
//					if($(this).hasClass("text")){
//						$(this).attr("readonly",false);
//						$(this).val("");
//					}
//				});
//			}
//		}else{
//			$( "label[for='"+$(this).attr("id")+"']" ).removeClass("ov_label");

//			if($(this).hasClass("text_check")){
//				$.each($(this).siblings(),function(i){
//					if($(this).hasClass("text")){
//						$(this).attr("readonly",true);
//						$(this).val("");
//					}
//				});
//			}
//		}
//	});
	////라디오 루프 종료


	$(".main_radio1 label").mousedown(function(){
		$('.radio_ul_box').removeClass('box_on');
        $('.main_radio1 .radio_etc').removeClass('ov_label');
	});

	$(".sub_radio1 label").mousedown(function(){
		$('.radio_ul_box').addClass('box_on');
        $('.main_radio1 .radio_etc').addClass('ov_label');
	});



	$( ".search_box .search_on" ).mousedown( function() {
		$('.search_box form').slideDown();
        $('.search_box .search_on').slideUp();
	});
	$('#tab_menu ul li:last').addClass('last');
	$('#tab_menu ul li:first').addClass('first');



	$( "#hosp_in_11_btn" ).mousedown( function() {


        $(".main_radio1 input[type=radio]").prop("checked",false);
        $(".main_radio1 .ov_label").removeClass("ov_label");
        
        $('.radio_ul_box').addClass('box_on');
        $(this).addClass('ov_label');
	});
	$( "#hosp_in_11_btn.ov_label" ).mousedown( function() {
		$('.radio_ul_box').removeclass('box_on');
        $(this).removeClass('ov_label');
	});

	$( "#cure_11" ).change( function() {

		if($(this).prop("checked")){
			$('.cure_11_box').addClass('box_on');
		    $(this).addClass('ov_label');
		}else{
			$('.cure_11_box').removeClass('box_on');
		    $(this).removeClass('ov_label');
		}
		
	});

		$( "#cure_2" ).change( function() {

			if($(this).prop("checked")){

				$(".check_jundam").find("input[type=checkbox]").each(function(i,j){
					$(this).prop("checked",false);
				});
				$(".check_jundam").find("label").each(function(i,j){
					$(this).removeClass("ov_label");
				});
				$('.cure_11_box').addClass('box_on');

			}else{

			}
	});	
});

// Tab

function initTabMenu(tabContainerID) {
	try{
		var tabContainer = document.getElementById(tabContainerID);
		var tabAnchor = tabContainer.getElementsByTagName("a");
		for(i=0; i<tabAnchor.length; i++) {
			if (tabAnchor.item(i).className == "tab")
				thismenu = tabAnchor.item(i);
			else
				continue;

			thismenu.container = tabContainer;
			thismenu.targetEl = document.getElementById(tabAnchor.item(i).href.split("#")[1]);
			thismenu.targetEl.style.display = "none";
			thismenu.imgEl = thismenu.getElementsByTagName("img").item(0);
			thismenu.onclick = function tabMenuClick() {
				currentmenu = this.container.current;
				if (currentmenu == this)
					return false;

				if (currentmenu) {
					currentmenu.targetEl.style.display = "none";
					if (currentmenu.imgEl) {
						currentmenu.imgEl.src = currentmenu.imgEl.src.replace("_on.png", ".png");
					}
					currentmenu.className = currentmenu.className.replace(" on", "");
				}
				
				this.targetEl.style.display = "block";
				if (this.imgEl) {
					this.imgEl.src = this.imgEl.src.replace(".png", "_on.png");
				}
				this.className += " on";
				this.container.current = this;

				if(tabContainerID == "tabMovie"){
					try{
					//자문위원 본인인증 체크 : 2017.11.21 김하영 추가 ( 개발 소스 )
					var tabId = this.href.split("#")[1];
					if(tabId == 'tab3'){
							parent.fn_authenticate('Y');
					}
					$('#userTab').val($(this).attr("href"));
					updateUser_resize();
					}catch(e){alert(e);}
				}

				return false;
			};
			if (!thismenu.container.first) thismenu.container.first = thismenu;
		}
		if (tabContainer.first) tabContainer.first.onclick();
	}catch(e){}
}
