
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
	//tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.maintab:first').attr('class', 'maintab '+myClass);
		//tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});

//Portlet_tab_main
jQuery(function($){
	var tab = $('.Portlet_tab_main');
	tab.removeClass('js_off');
	//tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.Portlet_tab_main:first').attr('class', 'Portlet_tab_main '+myClass);
		//tab.css('height', t.next('ul').height());
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

//portmini
jQuery(function($){
	var tab = $('.WfindNotice');
	tab.removeClass('js_off');
	//tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.WfindNotice:first').attr('class', 'WfindNotice '+myClass);
		//tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});


//portNotice
jQuery(function($){
	var tab = $('.Portlet_tab_normal');
	tab.removeClass('js_off');
	//tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.Portlet_tab_normal:first').attr('class', 'Portlet_tab_normal '+myClass);
		//tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});



// portmini
jQuery(function($){
	var tab = $('.Portlet_title_tab_normal');
	tab.removeClass('js_off');
	//tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.Portlet_title_tab_normal:first').attr('class', 'Portlet_title_tab_normal '+myClass);
		//tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});

// portmini02
jQuery(function($){
	var tab = $('.portmini02');
	tab.removeClass('js_off');
	//tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.portmini02:first').attr('class', 'portmini02 '+myClass);
		//tab.css('height', t.next('ul').height());
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
});

//portmini
jQuery(function($){
	var tab = $('.Portlet_tab_mini');
	tab.removeClass('js_off');
	//tab.css('height', tab.find('>ul>li>ul:visible').height());
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.Portlet_tab_mini:first').attr('class', 'Portlet_tab_mini '+myClass);
		//tab.css('height', t.next('ul').height());
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


/* 롤오버 */

  function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
