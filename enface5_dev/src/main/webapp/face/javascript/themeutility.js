
/**
*   Theme Utility APIs
*   for enFace theme
*   2011. 04. 27.
*   @author secrain
**/

if(! window.enface){
	window.enface = new Object();
}
if(! window.enface.util){
	window.enface.util = new Object();
}

enface.util.dom = function(){
	
}

enface.util.dom.prototype = 
	
{
	contextPath : null,
	
	setContextPath : function(contextPath)
	{
		this.contextPath = contextPath;
	},
	
	
	/**
	 *  create DOM's Attr functions
	 */

	
	/* create attribute id for DOM:all */
	getID: function (id)

	{
		var returnAttr = '';
		if(id == undefined || id == null || id == ''){
			returnAttr = '';
		}else {
			returnAttr = 'id=\'' + id + '\' ';
		}
		return returnAttr;
	},



	/* create attribute href for DOM:a */
	getHref: function (href)

	{
		var returnAttr = '';
		if(href == undefined || href == null || href == ''){
			returnAttr = 'title=\'a:link is null!\'';
		}else{
			returnAttr = 'href=\'' + href + '\' ';
		}
		return returnAttr;
	},



	/* create attribute target for DOM:a */
	getTarget: function (target)

	{
		var returnAttr = '';
		if(target == undefined || target == null || target == ''){
			returnAttr = 'target=\'_blank\' ';
		}else{
			returnAttr = 'target=\'' + target + '\' ';
		}
		return returnAttr;
	},



	/* create attribute Text for DOM */
	getText: function (text)

	{
		var returnAttr = '';
		if(text == undefined || text == null || (text != 0 && text == '')){
			returnAttr = '';
		}else{
			returnAttr = text;
		}
		return returnAttr;
	},



	/* create attribute class for DOM */
	getClass: function (clazz)

	{
		var returnAttr = '';
		if(clazz == undefined || clazz == null || clazz == ''){
			returnAttr = '';
		}else{
			returnAttr = 'class=\'' + clazz + '\' ';
		}
		return returnAttr;
	},



	/* create attribute src for DOM:img */
	getSrc: function (src)

	{
		var returnAttr = '';
		if(src == undefined || src == null || src == ''){
			returnAttr = 'title=\'img:src is null!\'';
		}else{
			returnAttr = 'src=\'' + src + '\' ';
		}
		return returnAttr;
	},



	/* create attribute for for DOM:label */
	getFor: function (fo)

	{
		var returnAttr = '';
		if(fo == undefined || fo == null || fo == ''){
			returnAttr = '';
		}else {
			returnAttr = 'for=\'' + fo + '\' ';
		}
		return returnAttr;
	},



	/* create attribute value for DOM */
	getValue: function (value)

	{
		var returnAttr = '';
		if(value == undefined || value == null || value == ''){
			returnAttr = '';
		}else{
			returnAttr = 'value=\'' + value + '\' ';
		}
		return returnAttr;
	},



	/* create attribute name for DOM */
	getName: function (name)

	{
		var returnAttr = '';
		if(name == undefined || name == null || name == ''){
			returnAttr = '';
		}else{
			returnAttr = 'name=\'' + name + '\' ';
		}
		return returnAttr;
	},

	/* create attribute title for DOM */
	getTitle: function (title)

	{
		var returnAttr = '';
		if(title == undefined || title == null || title == ''){
			returnAttr = '';
		}else{
			returnAttr = 'title=\'' + title + '\' ';
		}
		return returnAttr;
	},

	getWidth: function (width)
	
	{
		var returnAttr = '';
		if(width == undefined || width == null || width == ''){
			returnAttr = '';
		}else{
			returnAttr = 'width=\'' + width + '\' ';
		}
		return returnAttr;
	},
	
	getHeight: function (height)
	
	{
		var returnAttr = '';
		if(height == undefined || height == null || height == ''){
			returnAttr = '';
		}else{
			returnAttr = 'height=\'' + height + '\' ';
		}
		return returnAttr;
	},
	
	getCellspacing: function(cellspacing)

	{
		var returnAttr = '';
		if(cellspacing == undefined || cellspacing == null || cellspacing == ''){
			returnAttr = '';
		}else{
			returnAttr = 'cellspacing=\'' + cellspacing + '\' ';
		}
		return returnAttr;
	},
	
	getCellpadding: function(cellpadding)
	
	{
		var returnAttr = '';
		if(cellpadding == undefined || cellpadding == null || cellpadding == ''){
			returnAttr = '';
		}else{
			returnAttr = 'cellpadding=\'' + cellpadding + '\' ';
		}
		return returnAttr;
	},
	
	getColspan: function (colspan)
	
	{
		var returnAttr = '';
		if(colspan == undefined || colspan == null || colspan == ''){
			returnAttr = '';
		}else{
			returnAttr = 'colspan=\'' + colspan + '\' ';
		}
		return returnAttr;
	},
	
	getRowspan: function (rowspan)
	
	{
		var returnAttr = '';
		if(rowspan == undefined || rowspan == null || rowspan == ''){
			returnAttr = '';
		}else{
			returnAttr = 'rowspan=\'' + rowspan + '\' ';
		}
		return returnAttr;
	},
	
	getBorder: function (border)
	
	{
		var returnAttr = '';
		if(border == undefined || border == null || border == ''){
			returnAttr = '';
		}else{
			returnAttr = 'border=\'' + border + '\' ';
		}
		return returnAttr;
	},
	
	getAlign: function (align)
	
	{
		var returnAttr = '';
		if(align == undefined || align == null || align == ''){
			returnAttr = '';
		}else{
			returnAttr = 'align=\'' + align + '\' ';
		}
		return returnAttr;
		
	},
	
	getBackground: function (backgroundURL)
	
	{
		var returnAttr = '';
		if(backgroundURL == undefined || backgroundURL == null || backgroundURL == ''){
			returnAttr = '';
		}else{
			returnAttr = 'background=\'' + backgroundURL + '\' ';
		}
		return returnAttr;
		
	},
	
	getInputType: function (type)
	
	{
		var returnAttr = '';
		if(type == undefined || type == null || type == ''){
			returnAttr = '';
		}else{
			returnAttr = 'type=\'' + type + '\' ';
		}
		return returnAttr;
	},
	
	getStyle: function (style)
	
	{
		var returnAttr = '';
		if(style == undefined || style == null || style == ''){
			returnAttr = '';
		}else{
			returnAttr = 'style=\'' + style + '\' ';
		}
		return returnAttr;
	},
	
	
	

	/**
	 * create default DOM functions
	 */

	/* create link as DOM:a */
	getLink: function (id, href, target, text, clazz, title, style)

	{
		return $('<a ' + this.getTitle(title) + this.getID(id) + this.getHref(href) + this.getTarget(target) + this.getClass(clazz) + '>' + this.getText(text) + '</a>');
	},




	/* create Image as DOM:img */
	getImage: function (id, src, clazz, width, height, border, title, style)

	{
		return $('<img ' + this.getTitle(title) + this.getWidth(width) + this.getStyle(style) + this.getHeight(height) + this.getBorder(border) + this.getID(id) + this.getSrc(src) + this.getClass(clazz) + '/>');
	},




	/* create Bold Text DOM:b */
	getBoldText: function (id, text, clazz, style)

	{
		return $('<b ' + this.getID(id) + this.getClass(clazz) + this.getStyle(style) + '>' + this.getText(text) + '</b>');
	},




	/* create Label Text DOM:label */
	getLabel: function (id, text, clazz, fo, title, style)

	{
		return $('<label ' + this.getTitle(title) + this.getID(id) + this.getFor(fo) + this.getClass(clazz) + this.getStyle(style) + '>' + this.getText(text) + '</label>');
	},




	/* create Strong Text DOM:strong */
	getStrong: function (id, text, clazz, style)

	{
		return $('<strong ' + this.getID(id) + this.getClass(clazz) + this.getStyle(style) + '>' + this.getText(text) + '</strong>');
	},




	/* create Select DOM:select */
	getSelect: function (id, name, clazz, style)

	{
		return $('<select ' + this.getID(id) + this.getName(name) + this.getClass(clazz) + this.getStyle(style) + '></select>');
	},




	/* create option for select DOM:option */
	getOption: function (value, text, clazz, style)

	{
		return $('<option ' + this.getID(value) + this.getName(value) + this.getValue(value) + this.getClass(clazz) + this.getStyle(style) + '>' + this.getText(text) + '</option>');
	},
	
	
	
	/* create table DOM:table */
	getTable: function (id, width, height, border, cellspacing, cellpadding, clazz, style)
	 
	{
		return $('<table ' + this.getID(id) + this.getWidth(width) + this.getBorder(border) + this.getHeight(height) + this.getCellspacing(cellspacing) + this.getCellpadding(cellpadding) + this.getClass(clazz) + this.getStyle(style) + '></table>');
	},
	
	
	/* create tr for table DOM:tr */
	getTableRow: function (id, width, height, border, clazz, style)
	
	{
		return $('<tr ' + this.getID(id) + this.getWidth(width) + this.getHeight(height) + this.getBorder(border) + this.getClass(clazz) + this.getStyle(style) + '></tr>');
	},
	
	/* create td for tr DOM:td */
	getTableData: function (id, width, height, border, backgroundURL, align, clazz, colspan, rowspan, style)
	
	{
		return $('<td ' + this.getID(id) + this.getWidth(width) + this.getHeight(height) + this.getAlign(align) + this.getBackground(backgroundURL) + this.getBorder(border) + this.getClass(clazz) + this.getColspan(colspan) + this.getRowspan(rowspan) + this.getStyle(style) + '></td>');
	},
	
	
	getUl: function (id, clazz, style)
	
	{
		return $('<ul ' + this.getID(id) + this.getClass(clazz) + this.getStyle(style) + '></ul>');
	},

	
	getLi: function (id, clazz, style)
	
	{
		return $('<li ' + this.getID(id) + this.getClass(clazz) + this.getStyle(style) + '></li>');
	},
	
	
	getSpan: function (id, text, clazz, style)
	
	{
		return $('<span ' + this.getID(id) + this.getClass(clazz) + this.getStyle(style) + '>' + this.getText(text) + '</span>');
	},
	
	getInput: function (id, name, type, clazz, style)
	
	{
		return $('<input ' + this.getID(id) + this.getName(name) + this.getInputType(type) + this.getClass(clazz) + this.getStyle(style) + '></input>');
	}
	
}




var enfaceDOMUtil = new enface.util.dom();


enface.util.custom = function (){
	
}

enface.util.custom.prototype = 
{
		

	/**
	 * create customized DOM functions
	 */
	/* create TopLogo Link */
	initTopLogo: function (objID, hrefURL, imgURL, imgWidth, imgHeight, imgBorder, linkClass, imgClass)

	{
		var link = enfaceDOMUtil.getLink('topLogoLink', hrefURL, '_self', '', linkClass);
		var img = enfaceDOMUtil.getImage('topLogoImg', imgURL, imgClass);
		if(imgWidth != undefined){
			img.attr('width', imgWidth);
		}
		if(imgHeight != undefined){
			img.attr('height', imgHeight);
		}
		if(imgBorder != undefined){
			img.attr('border', imgBorder);
		}
		
		img.appendTo(link);
		link.appendTo('#' + objID);
	},


	/* create new note's count */
	initNewNoteCount: function (contextPath, objID, iconImgURL, name, unit)

	{
		var noteCount = 0;
		var enviewAjax = new enview.util.Ajax();
		enviewAjax.send('POST', contextPath + '/theme/ajaxTheme.face', 'method=newNoteCount',
				true, 
				{
					success: function(html){
						notCount = html;
					}
				});
		var newNoteIcon = enfaceDOMUtil.getImage('newNoteIcon', iconImgURL).attr('hspace', 4);
		var nameText = enfaceDOMUtil.getBoldText('newNoteText', name);
		var link = enfaceDOMUtil.getLink('noteLink', contextPath + '/note/note.face', 'noteLink', noteCount, 'mem');
		var unitLabel = enfaceDOMUtil.getLabel('noteUnitLabel',unit);
		
		newNoteIcon.appendTo('#' + objID);
		nameText.appendTo('#' + objID);
		link.appendTo('#' + objID);
		unitLabel.appendTo('#' + objID);
	},




	/* create user info change link */
	initChangeUserInfoLink: function (contextPath, objID, buttonImgURL)

	{
		var link = enfaceDOMUtil.getLink('changeUserInfoLink', contextPath + '/user/ajaxChangeInfo.face',  'changeUserInfoLink');
		var img = enfaceDOMUtil.getImage('changeUserInfoButton', buttonImgURL);
		img.attr('style', 'cursor:hand');
		img.appendTo(link);
		link.appendTo('#' + objID);
	},




	/* create logout button */
	initLogoutButton: function (contextPath, objID, buttonImgURL)

	{
		var link = enfaceDOMUtil.getLink('logoutLink', contextPath + '/user/logout.face', '_self');
		var img = enfaceDOMUtil.getImage('logoutButton', buttonImgURL);
		img.attr('style', 'cursor:hand');
		img.appendTo(link);
		link.appendTo('#' + objID);
	},



	/* create welcome label */
	initWelcomeLabel: function (objID, nameText, welcomeText)

	{
		var nameLabel = enfaceDOMUtil.getStrong('nameLabel', nameText);
		var welcomeLabel = enfaceDOMUtil.getLabel('welcomeLabel', welcomeText);
		nameLabel.appendTo('#' + objID);
		welcomeLabel.appendTo('#' + objID);
	},




	/* create Group select */
	initGroupSelect: function (objID, defaultText, groups, clazz)

	{
		var groupSelect = enfaceDOMUtil.getSelect('groupSelect', 'groupSelect', clazz);
		var groupOption = null;
		var selectedOption = enfaceDOMUtil.getOption('groupDefault', '가입한 동호회');
		selectedOption.attr('selected','selected');
		selectedOption.appendTo(groupSelect);
		var eGroups = eval(groups);
		for(var i = 0 ; i < eGroups.length ; i++){
			enfaceDOMUtil.getOption(eGroups[i].groupId, eGroups[i].groupName).appendTo(groupSelect);
		}
		groupSelect.appendTo('#' + objID);
	},


	/* get Joined groups by user */
	getJoinGroups: function (contextPath)

	{
		var enviewAjax = new enview.util.Ajax();
		var groups = null;
		
		enviewAjax.send('POST', contextPath + '/theme/ajaxTheme.face', 'method=joinGroups',
				false, /* must be synchronized */ 
				{
					success: function(json){
						groups = json;
					}
				});
		return groups;
	},
	
	
	initNaviTable: function (objID, navis, lineURL)
	
	{
		var eNavis = eval(navis);
		
		var nav_table = enfaceDOMUtil.getTable('nav_table');
		var nav_row1 = enfaceDOMUtil.getTableRow('nav_row1');
		
		for(var i = 0 ; i < eNavis.length ; i++){
			
			var nav_fixm = enfaceDOMUtil.getTableData('nav_fixm' + (i+1));
			
			if(eNavis[i].linkUrl != undefined && eNavis[i].linkUrl != null && eNavis[i].linkUrl != ''){
				if(eNavis[i].imgUrl != undefined && eNavis[i].imgUrl != null && eNavis[i].imgUrl != ''){
					var nav_fixm_link = enfaceDOMUtil.getLink('nav_fixm_link' + eNavis[i].index, eNavis[i].linkUrl, '_self');
					var nav_fixm_img = enfaceDOMUtil.getImage('nav_fixm_img' + eNavis[i].index, eNavis[i].imgUrl);
					nav_fixm_img.appendTo(nav_fixm_link);
					nav_fixm_link.appendTo(nav_fixm);
					nav_fixm.appendTo(nav_row1);
				}else {
					var nav_fixm_link = enfaceDOMUtil.getLink('nav_fixm_link' + eNavis[i].index, eNavis[i].linkUrl, '_self', eNavis[i].text);
					nav_fixm_link.appendTo(nav_fixm);
					nav_fixm.appendTo(nav_row1);
				}
				
				if(i < length-1){
					var nav_line = enfaceDOMUtil.getTableData('nav_line' + (i+1));
					var nav_line_img = enfaceDOMUtil.getImage('nav_line_img' + (i+1), lineURL);
					nav_line_img.appendTo(nav_line);
					nav_line.appendTo(nav_row1);
				}
				
			}else{
				if(eNavis[i].imgUrl != undefined && eNavis[i].imgUrl != null && eNavis[i].imgUrl != ''){
					var nav_fixm_img = enfaceDOMUtil.getImage('nav_fixm_img' + eNavis[i].index, eNavis[i].imgUrl);
					nav_fixm_img.appendTo(nav_fixm);
					nav_fixm.appendTo(nav_row1);
				}
				if(i < length-1){
					var nav_line = enfaceDOMUtil.getTableData('nav_line' + (i+1));
					var nav_line_img = enfaceDOMUtil.getImage('nav_line_img' + (i+1), lineURL);
					nav_line_img.appendTo(nav_line);
					nav_line.appendTo(nav_row1);
				}
			}
		}
		nav_row1.appendTo(nav_table);
		nav_table.appendTo('#' + objID);
	},
	
	
	
	initMainImage: function(objID, imgURL, width, height)
	
	{
		
		var table = enfaceDOMUtil.getTable('mainImgTable', '100%');
		var tr = enfaceDOMUtil.getTableRow('mainImgTableRow');
		var td = enfaceDOMUtil.getTableData('mainImgTableData');
		var img = enfaceDOMUtil.getImage('mainImage', imgURL, '', width, height);
		
		img.appendTo(td);
		td.appendTo(tr);
		tr.appendTo(table);
		table.appendTo('#' + objID);
	},
	
	
	initQuickMenu: function (objID, qiuckMenus, themePath)
	
	{
		var qJSON = eval(qiuckMenus);
		
		var imgPath = themePath + '/images/quick/quick_';
		
		var table = enfaceDOMUtil.getTable('quickMenuTable','','','0','0','0');
		var topTr = enfaceDOMUtil.getTableRow('quickMenuTopTR');
		var topTd = enfaceDOMUtil.getTableData('quickMenuTopTD');
		var topImg = enfaceDOMUtil.getImage('quickMenuTopImg', imgPath + 'top.gif');
		
		topImg.appendTo(topTd);
		topTd.appendTo(topTr);
		topTr.appendTo(table);
		
		for(var i = 0 ; i < qJSON.length ; i++){
			var tr = enfaceDOMUtil.getTableRow('quickMenuTR' + i);
			var td = enfaceDOMUtil.getTableData('quickMenuTD' + i, '','','', imgPath + 'bg.gif', 'center');
			
			var temp = qJSON[i].url.split("/");
			var linkPage = temp[temp.length - 1];
			var pageIndex = linkPage.indexOf(".");
			var id = linkPage.substring(0, pageIndex > -1 ? pageIndex : linkPage.length);
			var link = enfaceDOMUtil.getLink(id + 'Link', qJSON[i].url, qJSON[i].target);
			var img = enfaceDOMUtil.getImage(id + 'Img', qJSON[i].icon, '', '42', '50', '0', qJSON[i].title);
			img.mouseover(function(){
				$this = $(this);
				$this.attr('src', $this.attr('src').replace('.gif', '_on.gif'));
			});
			img.mouseout(function(){
				$this = $(this);
				$this.attr('src', $this.attr('src').replace('_on', ''));
			});
			img.appendTo(link);
			link.appendTo(td);
			td.appendTo(tr);
			tr.appendTo(table);
			
			if(i != qJSON.length-1 ){
				var tr2 = enfaceDOMUtil.getTableRow('quickMenuLineTR');
				var td2 = enfaceDOMUtil.getTableData('quickMenuLineTD','','','', imgPath + 'bg.gif', 'center');
				var img2 = enfaceDOMUtil.getImage(id + 'LineImg', imgPath+'line.gif','','','0');
				img2.appendTo(td2);
				td2.appendTo(tr2);
				tr2.appendTo(table);
			}
		}
		
		var botTr = enfaceDOMUtil.getTableRow('quickMenuBotTR');
		var botTd = enfaceDOMUtil.getTableData('quickMetnuBotTD');
		var botImg = enfaceDOMUtil.getImage('quickMenuBotImg', imgPath + 'bt.gif');
		
		botImg.appendTo(botTd);
		botTd.appendTo(botTr);
		botTr.appendTo(table);
		
		table.appendTo('#' + objID);
		
	},
	
	
	
	initTopMenu: function (objID, topMenus, themePath)
	
	{
		var imgPath = themePath + '/images/topmenu_';
		
		var menuTable = enfaceDOMUtil.getTable('topMenuTable', '700', '38', '0', '0', '0');
		var tr = enfaceDOMUtil.getTableRow('topMenuTR');
		
		var leftTd = enfaceDOMUtil.getTableData('topMenuLeftTD', '15');
		var leftImg = enfaceDOMUtil.getImage('topMenuLeftImg', imgPath + 'left.gif'); 
		leftImg.appendTo(leftTd);
		leftTd.appendTo(tr);
		
		var topLink;
		var topSpan; 
		var topLi;
		var ul = enfaceDOMUtil.getUl('topMenuUl', 'menu2');
		var bgTd1 = enfaceDOMUtil.getTableData('topMenuBgTd', '','','', imgPath + 'bg.gif');
		
		var tJSON = eval(topMenus);
		for(var i = 0 ; i < tJSON.length ; i++){
			var topmenu = tJSON[i];
			
			if(i > 0){
				var li = enfaceDOMUtil.getLi('topMenuLi', 'line');
				var img = enfaceDOMUtil.getImage('topMenuLineImg', imgPath + 'line.gif');
				img.appendTo(li);
				li.appendTo(ul);
			}
			if(topmenu.type == 'folder'){
				if( topmenu.linkUrl != null){
					if(startsWith(topmenu.linkUrl, 'javascript')) {
						topLi = enfaceDOMUtil.getLi('', 'top');
						topLink = enfaceDOMUtil.getLink('products', topmenu.linkUrl,'','','top_link');
						topSpan = enfaceDOMUtil.getSpan('', topmenu.linkTitle, 'top');
						
						topSpan.appendTo(topLink);
						topLink.appendTo(topLi);
						topLi.appendTo(ul);
					}
					else{
						if( topmenu.linkTarget.indexOf("_") == 0 ) {
							topLi = enfaceDOMUtil.getLi('', 'top');
							topLink = enfaceDOMUtil.getLink('products', '/enview/portal' + topmenu.linkUrl, topmenu.linkTarget,'','top_link');
							topSpan = enfaceDOMUtil.getSpan('', topmenu.linkTitle, 'top');
							
							topSpan.appendTo(topLink);
							topLink.appendTo(topLi);
							topLi.appendTo(ul);
							
						} else {
							topLi = enfaceDOMUtil.getLi('', 'top');
							topLink = enfaceDOMUtil.getLink('products', topmenu.linkUrl + '$sid', '','','top_link');
							topSpan = enfaceDOMUtil.getSpan('', topmenu.linkTitle, 'top');
							
							topSpan.appendTo(topLink);
							topLink.appendTo(topLi);
							topLi.appendTo(ul);
						}
					}
				}
			}
			else if(topmenu.type == 'link') {
				if( topmenu.linkUrl != null){
					if(startsWith(topmenu.linkUrl, 'javascript') ) {
						topLi = enfaceDOMUtil.getLi('', 'top');
						topLink = enfaceDOMUtil.getLink('products', topmenu.linkUrl, '','','top_link');
						topSpan = enfaceDOMUtil.getSpan('', topmenu.linkTitle, 'top');
						topSpan.appendTo(topLink);
						topLink.appendTo(topLi);
						topLi.appendTo(ul);
					
					} else { 
						if( topmenu.linkTarget.indexOf("_") == 0 ) {
							topLi = enfaceDOMUtil.getLi('', 'top');
							topLink = enfaceDOMUtil.getLink('products', topmenu.linkUrl, topmenu.target,'','top_link');
							topSpan = enfaceDOMUtil.getSpan('', topmenu.linkTitle, 'top');
							topSpan.appendTo(topLink);
							topLink.appendTo(topLi);
							topLi.appendTo(ul);
						} else { 
							topLi = enfaceDOMUtil.getLi('', 'top');
							topLink = enfaceDOMUtil.getLink('products', topmenu.linkUrl + '$sid', topmenu.target,'','top_link');
							topSpan = enfaceDOMUtil.getSpan('', topmenu.linkTitle, 'top');
							topSpan.appendTo(topLink);
							topLink.appendTo(topLi);
							topLi.appendTo(ul);
						}
					}
				}
			}
		}
		ul.appendTo(bgTd1);
		bgTd1.appendTo(tr);
		
		var rightTd = enfaceDOMUtil.getTableData('topRightTD', '15');
		var rightImg = enfaceDOMUtil.getImage('topRightImg', imgPath + 'right.gif');
		rightImg.appendTo(rightTd);
		rightTd.appendTo(tr);
		tr.appendTo(menuTable);
		
		menuTable.appendTo('#' + objID);
		
		var grayImgPath = themePath + '/images/top_gray_';
		var grayTable = enfaceDOMUtil.getTable('topGrayTable', '700','10','0','0','0');
		var grayTr = enfaceDOMUtil.getTableRow('topGrayTR');
		
		var grayTd1 = enfaceDOMUtil.getTableData('', '15');
		var grayImg1 = enfaceDOMUtil.getImage('', grayImgPath + 'left.gif');
		grayImg1.appendTo(grayTd1);
		grayTd1.appendTo(grayTr);
		
		var grayTd2 = enfaceDOMUtil.getTableData('', '670', grayImgPath + 'bg.gif');
		grayTd2.appendTo(grayTr);
		
		var grayTd3 = enfaceDOMUtil.getTableData('', '15');
		var grayImg2 = enfaceDOMUtil.getImage('', grayImgPath + 'right.gif');
		grayImg2.appendTo(grayTd3);
		grayTd3.appendTo(grayTr);
		
		grayTr.appendTo(grayTable);
		
		grayTable.appendTo('#' + objID);
	},
	
	
	initSearchBar: function (objID, themePath)
	
	{
		var imgPath = themePath + '/images/search_';
		var btnImgPath = themePath + '/images/btn_';
		
		var outerTable = enfaceDOMUtil.getTable('searchBarSpan', '700', '', '0','0','0');
		
		var outerTopTr = enfaceDOMUtil.getTableRow('topTR');
		var outerTopTd = enfaceDOMUtil.getTableData('topTd');
		var topImg = enfaceDOMUtil.getImage('topImg', imgPath + 'top.gif');
		
		var contentTr = enfaceDOMUtil.getTableRow('contentTR');
		var bgTd = enfaceDOMUtil.getTableData('bgTD', '', '', '', imgPath + 'bg.gif', '', '', '', '', 'padding: 1px 15px 1px 15px')
		
		var contentTable = enfaceDOMUtil.getTable('contentTable', '100%', '', '0', '0', '0');
		var titleTr = enfaceDOMUtil.getTableRow('titleTR');
		var titleTd = enfaceDOMUtil.getTableData('titleTD', '102', '', '', '', 'left');
		var titleImg = enfaceDOMUtil.getImage('titleImg', imgPath + 'title.gif');
		
		var contentTd = enfaceDOMUtil.getTableData('', '', '', '', '', 'left');
		
		var innerTable = enfaceDOMUtil.getTable('', '100%', '', '0', '0', '0');		
		var innerTr = enfaceDOMUtil.getTableRow('');
		var leftTd = enfaceDOMUtil.getTableData('', '15');
		var leftImg = enfaceDOMUtil.getImage('', imgPath + 'left_s.gif');

		var bg_sTd1 = enfaceDOMUtil.getTableData('', '', '', '', imgPath + 'bg_s.gif');
		var span = enfaceDOMUtil.getSpan('', '', '', 'padding:0 3 0 0');
		
		var catSelect = enfaceDOMUtil.getSelect('', 'select5', 'search_input2');
		var catOption1 =enfaceDOMUtil.getOption('', 'Select Category');
		
		var searchInput = enfaceDOMUtil.getInput('', 'goodsName', 'text', 'search_input');
		
		var bg_sTd2 = enfaceDOMUtil.getTableData('', '40', '', '', imgPath + 'bg_s.gif');
		var submitImg = enfaceDOMUtil.getImage('', btnImgPath + 'search.gif', '', '', '', '', '', 'cursor:hand');
		
		var rightTd = enfaceDOMUtil.getTableData('', '15');
		var rightImg = enfaceDOMUtil.getImage('', imgPath + 'right_s.gif');
		
		var detailTd = enfaceDOMUtil.getTableData('', '96', '', '', '', 'right');
		var detailImg = enfaceDOMUtil.getImage('', btnImgPath + 'searchd.gif', '', '', '', '', '', 'cursor:hand');
		
		var btTr = enfaceDOMUtil.getTableRow();
		var btTd = enfaceDOMUtil.getTableData();
		var btImg = enfaceDOMUtil.getImage('', imgPath + 'bt.gif');
		
		topImg.appendTo(outerTopTd);
		outerTopTd.appendTo(outerTopTr);
		outerTopTr.appendTo(outerTable);
		
		leftImg.appendTo(leftTd);
		leftTd.appendTo(innerTr);
		
		catOption1.appendTo(catSelect);
		catSelect.appendTo(span);
		searchInput.appendTo(span);
		span.appendTo(bg_sTd1);
		bg_sTd1.appendTo(innerTr);
		
		
		submitImg.appendTo(bg_sTd2);
		bg_sTd2.appendTo(innerTr);
		
		rightImg.appendTo(rightTd);
		rightTd.appendTo(innerTr);
		
		innerTr.appendTo(innerTable);
		
		innerTable.appendTo(contentTd);
		
		titleImg.appendTo(titleTd);
		titleTd.appendTo(titleTr);
		titleTr.appendTo(contentTable);
		
		contentTd.appendTo(titleTr);
		
		detailImg.appendTo(detailTd);
		detailTd.appendTo(titleTr);
		
		titleTr.appendTo(contentTable);
		
		contentTable.appendTo(bgTd);
		bgTd.appendTo(contentTr);
		contentTr.appendTo(outerTable);
		
		btImg.appendTo(btTd);
		btTd.appendTo(btTr);
		btTr.appendTo(outerTable);
		
		outerTable.appendTo('#' + objID);
		
	}
}


function startsWith(text, startswith){
	if(text.indexOf(startswith) == 0){
		return true;
	}
	return false;
}