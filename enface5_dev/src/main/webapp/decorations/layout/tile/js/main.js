
//링크된 이미지 점선 없애기/////////////////////////////////////////////////////////////////////////
function bluring(){
        if(event.srcElement.tagName=="A"||event.srcElement.tagName=="IMG")
            document.body.focus();
    }
    document.onfocusin=bluring;


//전체메뉴
$(document).ready(function () {
	$('.allmArea p a').click(function () {
		$('.allmArea .btn_on').toggle();
		$('.allmArea .btn_off').toggle();
		$('#allmenu').toggle();
	});
});



//maintab
jQuery(function($){
	var tab = $('.maintab');
	tab.removeClass('js_off');
	tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.maintab:first').attr('class', 'maintab '+myClass);
		tab.css('height', t.next('ul').height());

	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});


//WfindNotice
/*jQuery(function($){
	var tab = $('.WfindNotice');
	tab.removeClass('js_off');
	tab.css('height', tab.find('>ul>li>li:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.WfindNotice:first').attr('class', 'WfindNotice '+myClass);
		tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});
*/

//Portlet_tab_mini
jQuery(function($){
	var tab = $('.Portlet_tab_mini');
	tab.removeClass('js_off');
	tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.Portlet_tab_mini:first').attr('class', 'Portlet_tab_mini '+myClass);
		tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});


//Portlet_tab_normal
jQuery(function($){
	var tab = $('.Portlet_tab_normal');
	tab.removeClass('js_off');
	tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.Portlet_tab_normal:first').attr('class', 'Portlet_tab_normal '+myClass);
		tab.css('height', t.next('ul').height());
		/*
		var url = t.attr('url');
		if( url != null) {
			$.ajax({
				type: "GET",
				url: url,
				dataType : "text",
				success: function(data){
					alert( data);
				}
			});
		}
		*/
	}

	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});


//portmini
jQuery(function($){
	var tab = $('.portmini');
	tab.removeClass('js_off');
	tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.portmini:first').attr('class', 'portmini '+myClass);
		tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});

//Portlet_title_tab_normal
jQuery(function($){
	var tab = $('.Portlet_title_tab_normal');
	tab.removeClass('js_off');
	tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.Portlet_title_tab_normal:first').attr('class', 'Portlet_title_tab_normal '+myClass);
		tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});


//schelist_popup
function openschelist(){
	$('#schelist').css('display', 'block');	
}

function closeschelist(){
	$('#schelist').css('display', 'none');
}

//marklist_popup
function openmarklist(){
	$('#marklist').css('display', 'block');	
}

function closemarklist(){
	$('#marklist').css('display', 'none');
}

//member_popup
function openmember(){
	$('#member').css('display', 'block');	
}

function closemember(){
	$('#member').css('display', 'none');
}



//layer_popup
//작성자정보
function openinfor(){
	$('#writeinfor').css('display', 'block');	
}

function closeinfor(){
	$('#writeinfor').css('display', 'none');
}

//스케줄상세일정
function openSched(){
	$('#Schedetail').css('display', 'block');	
}

function closeSched(){
	$('#Schedetail').css('display', 'none');
}

