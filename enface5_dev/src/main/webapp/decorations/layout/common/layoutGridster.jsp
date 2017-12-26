<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.List"%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@page import="com.saltware.enview.Enview"%>
<%@page import="com.saltware.enview.om.page.impl.FragmentImpl"%>
<%@page import="com.saltware.enview.util.json.JSONObject"%>
<%@page import="com.saltware.enview.util.json.JSONArray"%>
<%@page import="com.saltware.enview.userpreference.UserPreferenceManager"%>
<%@page import="com.saltware.enview.userpreference.service.UserPreferenceVO"%>
<%@page import="com.saltware.enview.userpreference.service.UserPreferenceService"%>
<%@page import="org.apache.pluto.container.om.portlet.InitParam"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="org.apache.pluto.container.om.portlet.PortletDefinition"%>
<%@page import="com.saltware.enview.aggregator.PortletRenderer"%>
<%@page import="com.saltware.enview.aggregator.PortletContent"%>
<%@page import="com.saltware.enview.components.portletregistry.PortletRegistry"%>
<%@page import="com.saltware.enview.container.state.NavigationalState"%>
<%@page import="com.saltware.enview.om.page.FragmentLayout"%>
<%@page import="com.saltware.enview.om.page.Fragment"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%!
	/**
	* 속성값을 가져온다. 먼저 포틀릿의 설정(init-param)을 읽고 없으면 fragment의 preference를 읽는다. 둘다 없으면 defaultValue를 리턴한다.
	*/
	protected String getProperty( Fragment f, String key, String defaultValue) {
		String value = null;
		if( f.getPortlet() != null) {
			InitParam initParam = f.getPortlet().getInitParam( key);
			if( initParam != null) {
				value = initParam.getParamValue();
			}
		} else {
			value = f.getPreferenceValue(key);
		}
		return value != null ? value : defaultValue;
	}

	protected Locale getLocale( HttpServletRequest request) throws Exception {
		return new Locale(EnviewSSOManager.getLangKnd(request));
	}


	protected String getTitle( Fragment f, Locale locale) {
		String _pTitle = f.getPreferenceValue("TITLE");
		if( _pTitle == null ) {
			PortletDefinition portlet  = f.getPortlet();
			if( portlet != null ) {
				if( portlet.getDisplayName( locale ) != null) {
				_pTitle = portlet.getDisplayName( locale ).getDisplayName();
				}
			}
		}
		if( _pTitle==null) {
			_pTitle = f.getName();
		}
		return _pTitle;
	}
	
	
	static class FragmementComparator implements Comparator<JSONObject> {
		public int compare(JSONObject j1, JSONObject j2) {
			try {
				return  (j1.getInt("row") * 1000 + j1.getInt("col")) - (j2.getInt("row") * 1000 + j2.getInt("col"));
			} catch( Exception e) {
				return 0;
			}
		}
	}	
	protected List sort( JSONArray layout) throws Exception {
		ArrayList list = new ArrayList();
		for( int i=0; i< layout.length();i++) {
			list.add( layout.getJSONObject(i));
		}
		Collections.sort(list, new FragmementComparator() );
		return list;
	}
	

%>
<!-- gridster에서 자체적으로 마진을 잡으므로 마진을 없앤다. -->
<style type="text/css">
.p_11{margin:0px 0px;}
.p_21{margin:0px 0px;}
.p_10{margin:0px 0px;}
.p_13{margin:0px 0px;}
.p_20{margin:0px 0px;}

</style>
<%  
	try {
		PortletRenderer renderer = (PortletRenderer)request.getAttribute("renderer");
		PortletRegistry registry = (PortletRegistry)request.getAttribute("registry");
		boolean isRoot = (Boolean)request.getAttribute("isRoot");
		Fragment layoutFragment = (Fragment)request.getAttribute("currentFragment");
		String layoutId = layoutFragment.getId();
		
		String classSuffixMypage = "my";
		String layoutClass = "gridster";
		String columnClass = "column";
		String portletClass = "portlet-" + classSuffixMypage;
		String portletHeaderClass = "portlet-header-" + classSuffixMypage;
		String portletContentClass = "portlet-content-" + classSuffixMypage;
		
		JSONObject userLayout = null;
		if( !isEditPage) {
			UserPreferenceManager userPreferenceManager = (UserPreferenceManager) Enview.getComponentManager().getComponent("com.saltware.enview.userpreference.UserPreferenceManager");
			String userLayoutData = userPreferenceManager.getPreference( request,  "MY",currentPage.getPath());
			if( userLayoutData!=null) {
				userLayout = new JSONObject( userLayoutData) ;
			}
		}
//		out.println( "userLayout=" + userLayout);
		
%>

		<script type="text/javascript" src="${cPath}/face/javascript/portlet.js"></script>
		<link rel="stylesheet" type="text/css" media="screen, projection" href="${cPath}/decorations/layout/css/jquery.gridster.min.css" />
		<script type="text/javascript" src="${cPath}/decorations/layout/scripts/jquery.gridster.min.js"></script>
		<script type="text/javascript">
			var gridster;
			//----------------------------
			//  페이지 잠금 초기값. true : 잠금상태(조회모드)에서 시작. false 잠기지 않는 상태(편집모드)에서 시작  
			//----------------------------
			var gridsterLocked = false;
			//----------------------------
			var isEditPage = ${isEditPage};
			$(function(){ //DOM Ready

				<c:if test="${isEditPage==true}">
		        var serializer =  function($w, wgd) {
		        	var id = wgd.el[0].id.split('_')[2];
		        	var name = $w.attr('name');
		            return {
		            id: id,
		            col: wgd.col - 1,
		            row: wgd.row - 1
		            }
		        };
		        </c:if>
				<c:if test="${isEditPage!=true}">
		        var serializer =  function($w, wgd) {
		        	var id = wgd.el[0].id.split('_')[2];
		        	var name = $w.attr('name');
		            return {
		            id: id,
		            name: name,
		            col: wgd.col - 1,
		            row: wgd.row - 1
		            }
		        };
		        </c:if>
		        
			    gridster = $(".gridster").gridster({
			        widget_margins: [7, 4],
			        widget_base_dimensions: [257, 132],
			        min_rows : 4,
			        max_rows : 4,
			    	min_cols : 4,
			    	max_cols : 4,
			        widget_selector : ".portlet-my",
			        serialize_params: serializer,
			        draggable: {
			            stop: function(event, ui) {
			            	var fragments = JSON.stringify(gridster.serialize());
		    				var param = 'page_path=' + portalPage.getPath() +'&fragments=' + fragments;
		    				if( isEditPage) {
			    				portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/reorderMyPagePortletForAjax.face", param, false, {
			    					success: function(data){
	    							}
		    					}
			    				);
		    				} else {
		    					saveUserPage();
		    				}
			            }
			        }			        
			    }).data( 'gridster');
			    
			    if( gridsterLocked) {
			    	gridster.disable();
			    	$(".portlet-toolbar .ui-icon-locked").css({ opacity: 1.0 });
			    	$(".portlet-toolbar .ui-icon-unlocked").css({ opacity: 0.5 });
			    } else {
			    	$(".portlet-toolbar .ui-icon-locked").css({ opacity: 0.5 });
			    	$(".portlet-toolbar .ui-icon-unlocked").css({ opacity: 1.0 });
			    }
			
				  // 삭제 버튼 클릭 시 포틀릿 삭제 처리 
				  $(".portlet-header-my-icon .ui-icon-close").click(function() {
					if( confirm(portalPage.getMessageResource("ev.info.remove")) ) {
						var portlet = $(this).parents(".portlet-my:first" );
						
						var p1 = portlet.attr("id").split('_');
						if( p1[0] != 'Enview') return;
						if( p1[1] != 'Portlet') return;
						var portletId = p1[2];
						
						var param = 'page_path=' + portalPage.getPath() +'&fragment_id=' + portletId;
						if( isEditPage) {
							// delete current portlet;
							portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/removeMyPagePortletForAjax.face", param, false, {
								success: function(data){
									gridster.remove_widget( portlet );	
								}});
						} else {
							// 먼저 삭제
							gridster.remove_widget( portlet );
							// 전체 저장
							saveUserPage();
						}
					}
				  });
				
				var saveUserPage = function( handler) {
					// 전체 저장
	            	var fragments = JSON.stringify(gridster.serialize());
					var layoutFragment = "{ name:\"gridster\", fragmentLayout : " + fragments + "}";
					var param = 'page_path=' + portalPage.getPath() +'&fragments=' + layoutFragment;
    				portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/saveUserPageForAjax.face", param, false, {
    					success: function(data){
    						if( handler!=null) {
    							handler();
    						}
						}
					});
				} ;
				
				var showPortletSelector = function() {
					portalPage.getPortletSelector().doShow(null, 'university');
				}
				
				$(".portlet-header-my-icon .ui-icon-plus").click(function() {
					var userPreferenceIsEmpty = <%= userLayout == null ? "true" : "false" %>;
					if( userPreferenceIsEmpty && !isEditPage) {
						saveUserPage( showPortletSelector);
					} else {
						showPortletSelector;
					}
				});
				
				$(".portlet-toolbar .ui-icon-plus").click(function() {
					var userPreferenceIsEmpty = <%= userLayout == null ? "true" : "false" %>;
					if( userPreferenceIsEmpty && !isEditPage) {
						saveUserPage( showPortletSelector);
					} else {
						showPortletSelector();
					}
				});
				
				$(".portlet-toolbar .ui-icon-locked").click(function() {
			    	$(".portlet-toolbar .ui-icon-locked").css({ opacity: 1.0 });
			    	$(".portlet-toolbar .ui-icon-unlocked").css({ opacity: 0.5 });
					gridsterLocked = true;
					gridster.disable();
				});
				
				$(".portlet-toolbar .ui-icon-unlocked").click(function() {
			    	$(".portlet-toolbar .ui-icon-locked").css({ opacity: 0.5 });
			    	$(".portlet-toolbar .ui-icon-unlocked").css({ opacity: 1.0 });
					gridsterLocked = false;
					gridster.enable();
				});
			
				
				$(".portlet-toolbar .ui-icon-refresh").click(function() {
					if( confirm(portalPage.getMessageResource("ev.info.reset")) ) {
						var param = 'page_path=' + portalPage.getPath();
	    				portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/removeUserPageForAjax.face", param, false, {
	    					success: function(data){
	    						document.location.reload();
   							}
    					}
	    				);
					}
				});
				
				$(".portlet-my").hover( function() {
					var header = $(this).children(".portlet-header-my:first" ).children(".portlet-header-my-icon:first" );
					portletHeader = header;
					setTimeout( showPortletHeader, 1000);
				}, function() {
					portletHeader = $(this).children(".portlet-header-my:first" ).children(".portlet-header-my-icon:first" );
					hidePortletHeader();
				})
				
				
			});
			var portletHeader;
			function showPortletHeader() {
				if( portletHeader != null && ! gridsterLocked) {
					portletHeader.show();
				}
			}
			function hidePortletHeader() {
				if( portletHeader != null) {
					portletHeader.hide();
					portletHeader = null;
				}
			}

		</script>

		<link rel="stylesheet" type="text/css" media="screen, projection" href="${cPath}/decorations/layout/css/gridster.css"  />
		<div class="portlet-toolbar">
		<span class='ui-icon ui-icon-unlocked' title="이동"></span>
		<span class='ui-icon ui-icon-locked' title="이동금지"></span>
		<span class='ui-icon ui-icon-plus' title="포틀릿 추가"></span>
		<span class='ui-icon ui-icon-refresh' title="원상복구"></span>
		<c:if test="${isEditPage==true}">
		<img src='<%=request.getContextPath()%>/decorations/layout/images/edit.gif' border='0' align='absmiddle' title='<util:message key="ev.info.icon.editPortlet"/>' style='border:none; cursor:hand;'/> 
		</c:if>
		<c:if test="${isEditPage!=true}">
		</c:if>
		</div>
		
		<div id="Enview_Layout_<%=layoutId%>" class="<%=layoutClass%>" style="float:left">
<%  
	if( userLayout==null) {
		FragmentLayout fragmentLayout = layoutFragment.getFragmentLayout();
		if( fragmentLayout != null ) {
			Iterator it = fragmentLayout.getColumns().iterator();
			for(int columnIndex=0; it.hasNext(); columnIndex++) {
				Collection column = (Collection)it.next();
				Iterator it2 = column.iterator();
				for(int rowIndex=0; it2.hasNext(); rowIndex++) {
					Fragment f = (Fragment)it2.next();
					
					String col = (f.getLayoutColumn() + 1) + "";;
					String row = (f.getLayoutRow()+1) + "";
					
					if( "portlet".equals(f.getType()) ) {
						boolean isPortletRemovable = (f.getActionMask() & 0x00000001) > 0;
						boolean isPortletMovable = (f.getActionMask() & 0x00000010) > 0 ;						
						
						NavigationalState nav = _rc.getPortalURL().getNavigationalState();
						WindowState _ws = nav.getMappedState(f.getId());

						PortletDefinition portlet = f.getPortlet();
						if( portlet == null ) {
							portlet = registry.getPortletDefinitionByUniqueName(f.getName());
							f.setPortlet(portlet);
						}

						String _pTitle = getTitle( f, getLocale( request));
						String _pName = f.getName();
						
						//-- 포틀릿의 상대적인 너비. 1 = 정사각형
						String _pSizex = getProperty(f, "SIZEX", "1");
						//-- 포틀릿의 상대적인  높이. 1 = 정사각형 포틀릿의 절반 높이
						String _pSizey = getProperty(f, "SIZEY", "2");
						
						Map contentBuffer = new HashMap();
						renderer.renderNow(f, _rc, contentBuffer); // 20120224 Modified
						String content = "";

						if( f.getOverriddenContent() != null && f.getOverriddenContent().length() > 0) {
							content = f.getOverriddenContent(); // 20120224 Modified
						} else {
							PortletContent portletContent = (PortletContent)contentBuffer.get("content");
							if( portletContent != null ) {
								content=  portletContent.getContent(); // 20120224 Modified
							}
						}
						
						if( content.length() > 0 ) { // 20120224 Modified
							String style = f.getPreferenceValue("STYLE");
							portletClass = "portlet-my";
							%>
							<div id="Enview_Portlet_<%=f.getId()%>" class="<%=portletClass%>" <%= style !=null ? "style='" + style + "'" : "" %> name="<%=_pName %>" data-row="<%=row%>" data-col="<%=col%>" data-sizex="<%=_pSizex%>" data-sizey="<%=_pSizey%>">
							<div class="<%=portletHeaderClass%>">
							<div class="portlet-header-my-icon">
							<span class='ui-icon ui-icon-close' title='포틀릿 삭제'></span>
							<span class='ui-icon ui-icon-plus' title="포틀릿 추가"></span>
							</div>
							&nbsp;
							<!-- 
							<% if( isPortletRemovable ) { %><span class='ui-icon ui-icon-close' title="포틀릿 삭제"></span><%}%>
							<span class='ui-icon ui-icon-plus' title="포틀릿 추가"></span>
							<span style="margin-left:5px;margen-right:5px"><%=_pTitle %></span>
							 -->
							</div>
							<%
								if( "minimized".equals(_ws.toString()) == false) { %><div id="Enview_Portlet_Content_<%=f.getId()%>" class="<%=portletContentClass%>"><%= content %></div><%
								}
							%></div> <!-- portlet end -->
<%		    			}
					}
				} %>
				<%--
				</div> <!-- portal-layout-column -->
				 --%>
<%			}
		} 
		
	} else {
//		System.out.println( userLayout);
		JSONObject p;
		Fragment f;
		JSONArray fragmentLayout = userLayout.getJSONArray("fragmentLayout");
		List layouts = sort( fragmentLayout ); 
		for( int i=0; i < layouts.size(); i++) {
			p  = (JSONObject)layouts.get(i);
			String _pName = p.getString("name");
			String _pId;
			try {
				_pId = p.getString("id");
			} catch(Exception e) {
				_pId = System.currentTimeMillis()+"";
			}
//			System.out.println( p);
			PortletDefinition pd = registry.getPortletDefinitionByUniqueName( _pName);
			
			f = new FragmentImpl();
			f.setType( f.PORTLET);
			f.setId( _pId);
			f.setName(_pName);
			f.setPortlet( pd);
			NavigationalState nav = _rc.getPortalURL().getNavigationalState();
			WindowState _ws = nav.getMappedState(f.getId());
			
			String _pTitle = getTitle(f, getLocale( request));
			//-- 포틀릿의 상대적인 너비. 1 = 정사각형
			String sizex = getProperty(f, "SIZEX", "1");
			//-- 포틀릿의 상대적인  높이. 1 = 정사각형 포틀릿의 절반 높이
			String sizey = getProperty(f, "SIZEY", "2");
			String col = (p.getInt("col") + 1) + "";
			String row = (p.getInt("row") + 1) + "";
			
			Map contentBuffer = new HashMap();
			renderer.renderNow( f, _rc, contentBuffer); // 20120224 Modified
			String content = "";
			PortletContent portletContent = (PortletContent)contentBuffer.get("content");
			if( portletContent != null ) {
				content=  portletContent.getContent(); // 20120224 Modified
			}
			
			if( content.length() > 0 ) { // 20120224 Modified
				%>
				<div id="Enview_Portlet_<%=f.getId()%>" class="<%=portletClass%>" name="<%=_pName %>" data-row="<%=row%>" data-col="<%=col%>" data-sizex="<%=sizex%>" data-sizey="<%=sizey%>">
				<div class='portlet-header-my'>
				<div class="portlet-header-my-icon">
				<span class='ui-icon ui-icon-close' title='포틀릿 삭제'></span>
				<span class='ui-icon ui-icon-plus' title="포틀릿 추가"></span>
				</div>&nbsp;
				</div>
				<div id="Enview_Portlet_Content_<%=_pId%>" class="<%=portletContentClass%>"><%= content %></div>
				</div> <!-- portlet end -->
				<%
   			}
		}
	}
		%>
		</div> <!--portal-layout-->
<%	}
	catch(Exception e) {
		Log log = LogFactory.getLog("com.saltware.enview");
		log.error( e, e);
	}
%>
