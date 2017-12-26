
jQuery(function(){
	//faq
	$('#board_box .faq a.tit').click(function(event){
		var $target=$(event.target);
		if($target.is('.open')){
			$(this).removeClass('open').next('.cont').slideUp('fast');
		}else{
			$('#board_box .faq a.tit').removeClass('open').next('.cont').slideUp('fast');
			$(this).addClass('open').next('.cont').show();
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
//					  alert( i + ": " );
			
			//현재 이름에 해당하는 모든 라디오 박스를 비선택으로 변경하고
			//ov_label을 제거해준다.
			$( "label[for='"+$(this).attr("id")+"']" ).removeClass("ov_label");
	
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

	$(".main_radio1 label").mousedown(function(){
		$('.radio_ul_box').removeClass('box_on');
        $('.main_radio1 .radio_etc').removeClass('ov_label');
	});

	$(".sub_radio1 label").mousedown(function(){
		$('.radio_ul_box').addClass('box_on');
        $('.main_radio1 .radio_etc').addClass('ov_label');
	});



	$( ".check_design label" ).mousedown( function() {

        $(this).toggleClass( 'ov_label' );
	});


	$( ".search_box .search_on" ).mousedown( function() {
		$('.search_box form').slideDown();
        $('.search_box .search_on').slideUp();
	});
	$('#tab_menu ul li:last').addClass('last');
	$('#tab_menu ul li:first').addClass('first');



	$( "#hosp_in_11_btn" ).mousedown( function() {
		$('.radio_ul_box').addClass('box_on');
        $(this).addClass('ov_label');
	});
	$( "#hosp_in_11_btn.ov_label" ).mousedown( function() {
		$('.radio_ul_box').removeclass('box_on');
        $(this).removeClass('ov_label');
	});

	$( "#cure_11_btn" ).mousedown( function() {
		$('.cure_11_box').addClass('box_on');
        $(this).addClass('ov_label');
	});
	$( "#cure_11_btn.ov_label" ).mousedown( function() {
		$('.cure_11_box"').removeclass('box_on');
        $(this).removeClass('ov_label');
	});

	$( ".ch_box label" ).mousedown( function() {
        $(this).toggleClass('ov_label');
	});


	

	
});
$(document).ready(function() {
	if ($('read_only').hasclass('.ov_label')) {
		$(this).child(".readonly_input").removeAttr("readonly");
	}else {
		$(".readonly_input").attr("readonly",true);
	}
});


