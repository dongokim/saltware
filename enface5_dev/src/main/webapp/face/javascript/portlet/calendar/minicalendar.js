if( ! window.hancer )
    window.hancer = new Object();

if( ! window.hancer.cal )
    window.hancer.cal = new Object();

hancer.cal = function() {
	this.m_ajax = new enview.util.Ajax();
	this.m_contextPath = '';
	this.m_themePath = '';
	this.m_encodeingCallback = encodeURIComponent;
}

hancer.cal.prototype = {
	m_contextPath : null,
	m_themePath : null,
	m_ajax : null,
	m_calendars : [],
	m_calendarIds : [],
	m_calendar : null,
	
//	m_schedules : [],
	m_start: null,
	m_end: null,
	
	m_selectedEvent : null,
	
//	m_scheduleUsers : [],
	m_calendarUsers : [],
	
	m_encodeingCallback : null,
	
	m_validateParams : ['name', 'nameList'],
	
	m_isJointModified : null,
	
	m_skin : 'default',
	
	m_emptyMessage : null,
	
	m_CalendarOption : {

		// display
		defaultView: 'month',
		aspectRatio: 1.35,
		header: {
			left: 'prev title next',
			center: '',
			right: 'more'
		},
		weekends: true,
		weekNumbers: false,
		weekNumberCalculation: 'iso',
		weekNumberTitle: 'W',
		
		// editing
		//editable: false,
		//disableDragging: false,
		//disableResizing: false,
		
		allDayDefault: true,
		ignoreTimezone: true,
		
		// event ajax
		lazyFetching: true,
		startParam: 'start',
		endParam: 'end',
		
		// time formats
		titleFormat: {
			month: 'yyyy. MMMM',
			week: "MMM d[ yyyy]{ '&#8212;'[ MMM] d yyyy}",
			day: 'dddd, MMM d, yyyy'
		},
		columnFormat: {
			month: 'ddd',
			week: 'ddd M/d',
			day: 'dddd M/d'
		},
		timeFormat: { // for event elements
			'': 'h(:mm)t' // default
		},
		
		// locale
		isRTL: false,
		firstDay: 0,
		monthNames: ['01','02','03','04','05','06','07','08','09','10','11','12'],
		monthNamesShort: ['01','02','03','04','05','06','07','08','09','10','11','12'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		buttonText: {
			prev: "<a><img converURL='themePath_src' id='prevMonthButton' src='@themePath@/images/main/sche_arr_pevr.gif' alt='이전달' /></a>",
			next: "<a><img converURL='themePath_src' id='nextMonthButton' src='@themePath@/images/main/sche_arr_next.gif' alt='다음달' /></a>",
			prevYear: "<span class='fc-text-arrow'>&laquo;</span>",
			nextYear: "<span class='fc-text-arrow'>&raquo;</span>",
			today: 'today',
			month: 'month',
			week: 'week',
			day: 'day',
			more: "<a class='btn_rgray24' converURL='contextPath_href' href='@contextPath@/portal/default/link/top_left/schedule.page'><span id='moreButton'>&nbsp;</span></a>"
		},
		
		// jquery-ui theming
		theme: false,
		buttonIcons: {
			prev: 'circle-triangle-w',
			next: 'circle-triangle-e'
		},
		
		//selectable: false,
		unselectAuto: true,
		
		dropAccept: '*',
		
		handleWindowResize: true,
		
		allDayText: 'all-day',
		axisFormat: 'h(:mm)tt'
		
	},
	
	m_viewName : null,
	
	m_isCalListOpen : false,
	m_calSliderFixed : false,
	
	init : function () {
		if(!document.getElementById('calendar')) return;
		
		var viewName = $('#viewName');
		if(viewName) this.m_viewName = viewName.val();
		
		this.m_calendarIds = [];
		this.m_calendars = [];
		this.m_schedules = [];
		enMiniCal.m_timeFormat = { '': 'hh:mmt' };
		enMiniCal.initCalendar();
		
	},
	
	renderEvents : function(calendarId){
		var events = [];
		var isOwner = $('#' + calendarId + '_isOwner').val();
		var isDefault = $('#' + calendarId + '_isDefault').val();
		var isPublic = $('#' + calendarId + '_isPublic').val();
		var param = 'calendarId=' + calendarId;
		param += '&isDefault=' + isDefault;
		param += '&isOwner=' + isOwner;
		param += '&isPublic=' + isPublic;
		param += '&start=' + enMiniCal.m_start;
		param += '&end=' + enMiniCal.m_end;
		param += '&dummy=' + Math.random()*1000;
		param += '&isUserPublic=' + $('#' + calendarId + '_isUserPublic').val();
		this.m_ajax.send("POST", enMiniCal.m_contextPath + "/calendar/scheduleList.hanc", param, false, {
			success: function(data){
				events = data.Data;
			},
			error: function(data, e){
				//alert(data.Reason + '\n잠시 후에 다시 시도 해주십시오.');
			}
		});
		return events;
	},
	
	initCalendar : function(){
		if(!document.getElementById('calendar')) return;
		
		$('input[name=calendarId]').each(function(index){
			var $this = $(this);
			enMiniCal.m_calendarIds[index] = $this.val();
		});
		
		$('#calendar').fullCalendar({
			//options setting
			timeFormat: enMiniCal.m_CalendarOption.timeFormat,
			defaultView: enMiniCal.m_CalendarOption.defaultView,
			header: enMiniCal.m_CalendarOption.header,
			titleFormat: enMiniCal.m_CalendarOption.titleFormat,
			monthNames: enMiniCal.m_CalendarOption.monthNames,
			monthNamesShort: enMiniCal.m_CalendarOption.monthNamesShort,
			dayNames: enMiniCal.m_CalendarOption.dayNames,
			dayNamesShort: enMiniCal.m_CalendarOption.dayNamesShort,
			buttonText: enMiniCal.m_CalendarOption.buttonText,
			allDayText: enMiniCal.m_CalendarOption.allDayText,
			axisFormat: enMiniCal.m_CalendarOption.axisFormat,
					
			editable: true,
			events : function(start, end, callback) {
				var year = $('#calendar').fullCalendar('getDate').format('yyyy-MM').split('-')[0];
				var month = $('#calendar').fullCalendar('getDate').format('yyyy-MM').split('-')[1];
				$.ajax({
					type: "GET",
					url: $('#monthURL').val(),
					data: "year=" + year + "&month=" + month,
					dataType : "json",
					success: function(json){
						var dates = $('#calendar .fc-day');
						var today = new Date().format('yyyy-MM-dd');
						dates.each(function(){
							var $this = $(this);
							var currentDate = new Date($this.attr('data-date'));
							if(today == currentDate.format('yyyy-MM-dd')) {
								$this.addClass('fc-day-selected');
								$this.find('.fc-day-number').addClass('fc-day-selected');
							}
							var scheduleCountList = json.scheduleCountList;
							if (scheduleCountList != null) {
								for(var i = 0; i < scheduleCountList.length; i++){
									if($this.attr('click-binded') == 'T') break;
									var schedule = scheduleCountList[i];
									var startDate = new Date(schedule.startDate);
									var endDate = new Date(schedule.endDate);
									if(startDate <= currentDate && currentDate <= endDate){
										$this.attr('click-binded', 'T');
										$this.addClass('fc-day-exists-schedule');
										$this.find('.fc-day-number').addClass('fc-day-exists-schedule');
										$this.click(function(){
											$('#calendar .fc-day-selected').removeClass('fc-day-selected');
											$this.addClass('fc-day-selected');
											$this.find('.fc-day-number').addClass('fc-day-selected');
											enMiniCal.getScheduleData(new Date($this.attr('data-date')));
										});
										
									}
								}
							}
						});
					}
				});
	    	}
		});
	},
	
	getScheduleData : function(currentDate){
		var year = currentDate.format('yyyy');
		var month = currentDate.format('MM');
		var date = currentDate.format('dd');
		$.ajax({
			type: "GET",
			url: $('#dateURL').val(),
			data: "year=" + year + "&month=" + month + "&date=" + date,
			dataType : "json",
			success: function(json){
				var scheduleHTML = '';
				for(var i = 0; i < Math.min(4, json.scheduleList.length); i++){
					var schedule = json.scheduleList[i];
					scheduleHTML += '<li><span class="title" title="' + schedule.startDatim + ' ~ ' + schedule.endDatim + '">';
					scheduleHTML += '<a href="' + enMiniCal.m_contextPath + '/portal/default/link/top_left/schedule.page">' + schedule .name + '</a></span></li>';
				}
				if(json.scheduleList.length == 0) {
					scheduleHTML += '<li><span class="title" title="'+ enMiniCal.m_emptyMessage +'">';
					scheduleHTML += '<a href="' + enMiniCal.m_contextPath + '/portal/default/link/top_left/schedule.page">'+ enMiniCal.m_emptyMessage +'</a></span></li>';
				}
				$('#scheduleListUl').html(scheduleHTML);
			}
		});
	},
	
	getMessage : function() {
		var retMsg = null;
		var arr = arguments;
		if (arr != null && arr.length > 0) {
			if (!portalPage) {
				portalPage = new enview.portal.Page();
			}
			retMsg = portalPage.getMessageResource (arr[0]);
			if(retMsg == null) {
				alert(arr[0]);
				return "";
			}
			for (var i=0; i<arr.length; i++) {
				var pattern = "{" + i + "}";
				retMsg = retMsg.replace(pattern, arr[i+1]);
			}
		}
		return retMsg;
	},
	
	autoResize : function() {
		if(parent.autoresize_iframe_portlets) parent.autoresize_iframe_portlets();
	}
}

var enMiniCal = new hancer.cal();

$(document).ready(function(){
	enMiniCal.init();
});
