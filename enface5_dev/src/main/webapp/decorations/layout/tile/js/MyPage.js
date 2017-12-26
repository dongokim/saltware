/**
 * scripts for my page 
 */
$(function() {
	// make sortable
   $(".column-my").sortable({
	   // 포틀릿 이동 시 포틀릿 재배열 
	   update: function(event, ui) {
			var c1 = $(this).attr("id").split("_");
			var layout_id = c1[2];
			var column_index = c1[3];
		   
			var portlets = $(this).children();
			var portletIds = [];
			for( var i=0;i<portlets.length;i++) {
				portletIds.push( $(portlets[i]).attr("id").split('_')[2]);
			}
			if(portletIds.length > 0){
				var param = 'page_path=' + portalPage.getPath() +'&fragment_ids=' + portletIds + '&parent_id=' + layout_id + '&layout_column=' + column_index;
				portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/reorderMyPagePortletForAjax.face", param, false, {
					success: function(data){
						
					}});
			}
	   },
	   // portlet-my만 이동 가능, portlet-fixed는 이동불가
	   items : ".portlet-my",
	   opacity: 0.7,
	   connectWith: ".column-my"
   });
  
//    이동 가능한 포틀릿과 포틀릿 제목의 style 변경
//   $(".portlet-my").addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
//   .find(".portlet-header-my")
//   .addClass("ui-widget-header ui-corner-all")
//   .end()
//   .find(".portlet-content-my");
   
   // 이동 불가능한 포틀릿과 포틀릿 제목의 style 변경
//   $(".portlet-fixed").addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
//				   .find(".portlet-header-my")
//				   .addClass("ui-widget-header ui-corner-all ui-state-disabled")
//				   .end()
//				   .find(".portlet-content-my");
   
				   

   // MORE버튼 클릭 시 페이지 이동 또는 팝업 창 열기 처리 
   $(".portlet-header-my .ui-icon-plus").click(function() {
		var more_src = $(this).attr("more_src");
		var more_target =  $(this).attr("more_target");
		var more_width =  $(this).attr("more_width");
		var more_height =  $(this).attr("more_height");
		
		if( more_src!='') {
			openPortletMore( more_src, more_target, more_width, more_height);
		}
   });
  
   // 삭제 버튼 클릭 시 포틀릿 삭제 처리 
   $(".portlet-header-my .ui-icon-close").click(function() {
		if( confirm(portalPage.getMessageResource("ev.info.remove")) ) {
			var portlet = $(this).parents(".portlet-my:first" );
			var column =  portlet.parents(".column-my:first" );
			
			var p1 = portlet.attr("id").split('_');
			if( p1[0] != 'Enview') return;
			if( p1[1] != 'Portlet') return;
			var portletId = p1[2];
			
			var c1 = column.attr("id").split('_');
			if( c1[0] != 'Enview') return;
			if( c1[1] != 'Column') return;
			var columnId = c1[2];
			var columnIndex = c1[3];
			
			var param = 'page_path=' + portalPage.getPath() +'&fragment_id=' + portletId + '&parent_id=' + columnId + '&layout_column=' + columnIndex;
			portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/removeMyPagePortletForAjax.face", param, false, {
				success: function(data){
					portlet.remove();	
				}});
		}
   });
  
   $(".column-my").disableSelection();
   
});

var openPortletMore = function( src, target, width, height) {
	var opt = '';
	if( width > 0) {
		var x = ( screen.width - width) / 2;
		var y =  ( screen.height - height) / 2;
		opt ="toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,resizable=yes,left=" + x + ",top=" + y + ",width=" + width + ",height=" + height;
	}
//	alert( opt);
	var win = 	window.open(src, target, opt);
	if( win !=null) {
		win.focus();
	}	   
   
   
}
