<%@page language="java" pageEncoding="UTF-8"%>

<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%  
	try {
		PortletRenderer renderer = (PortletRenderer)request.getAttribute("renderer");
		PortletRegistry registry = (PortletRegistry)request.getAttribute("registry");
		boolean isRoot = (Boolean)request.getAttribute("isRoot");
		Fragment layoutFragment = (Fragment)request.getAttribute("currentFragment");
		if( isRoot ) { %>
		<div id="Enview_Layout_<%=layoutFragment.getId()%>" class="portal-nested-layout">
			<div id='LayoutEditIconArea_<%=layoutFragment.getId()%>' class='root-icon-area' style='display:none;'>
				<a class='portlet-action-button' onclick="javascript:portalPage.showPortletEditor('Enview_Layout_<%=layoutFragment.getId()%>');">
					<img src='<%=request.getContextPath()%>/decorations/layout/images/edit.gif' border='0' align='absmiddle' title='<util:message key="ev.info.icon.editPortlet"/>' style='border:none; cursor:hand;'/> 
				</a>
				<a class='portlet-action-button' onclick="javascript:portalPage.showPortletSelector('Enview_Layout_<%=layoutFragment.getId()%>');" >
					<img src='<%=request.getContextPath()%>/decorations/layout/images/selectPortlet.png' border='0' align='absmiddle' title='<util:message key="ev.info.icon.addPortlet"/>' style='border:none; cursor:hand;'/> 
				</a>
			</div>
<%		} else { %>
		<div id="Enview_Layout_<%=layoutFragment.getId()%>" class="portal-layout">
			<div id='LayoutEditIconArea_<%=layoutFragment.getId()%>' class='root-icon-area' style='display:none;'>
				<img id='zzzzzzz' src='<%=request.getContextPath()%>/decorations/layout/images/move.png' border='0' align='absmiddle' title='<util:message key="ev.info.icon.movePortlet"/>' style='border:none; cursor:hand;'/>
				<a class='portlet-action-button' onclick="javascript:portalPage.showPortletEditor('Enview_Layout_<%=layoutFragment.getId()%>');">
					<img src='<%=request.getContextPath()%>/decorations/layout/images/edit.gif' border='0' align='absmiddle' title='<util:message key="ev.info.icon.editPortlet"/>' style='border:none; cursor:hand;'/> 
				</a>
				<a class='portlet-action-button' onclick="javascript:portalPage.showPortletSelector('Enview_Layout_<%=layoutFragment.getId()%>');" >
					<img src='<%=request.getContextPath()%>/decorations/layout/images/selectPortlet.png' border='0' align='absmiddle' title='<util:message key="ev.info.icon.addPortlet"/>' style='border:none; cursor:hand;'/> 
				</a>
				<a class='portlet-action-button' onclick="javascript:portalPage.removePortlet('Enview_Layout_<%=layoutFragment.getId()%>');" >
					<img src='<%=request.getContextPath()%>/decorations/layout/images/icon_del.gif' border='0' align='absmiddle' title='<util:message key="ev.info.icon.removePortlet"/>' style='border:none; cursor:hand;'/>
				</a>
			</div>
<%    	}
		FragmentLayout fragmentLayout = layoutFragment.getFragmentLayout();
		if( fragmentLayout != null ) {
			Iterator it = fragmentLayout.getColumns().iterator();
			for(int columnIndex=0; it.hasNext(); columnIndex++) {
				Collection column = (Collection)it.next();
//				String columnFloat = fragmentLayout.getColumnFloat(columnIndex);
				String columnFloat = "left";
				String columnWidth = fragmentLayout.getColumnWidth(columnIndex);

%>				    		
				<div id="Enview_Column_<%=layoutFragment.getId()%>_<%=columnIndex%>" class="column" style="float:<%=columnFloat%>; width:<%=columnWidth%>;">
<%					
				Iterator it2 = column.iterator();
				for(int rowIndex=0; it2.hasNext(); rowIndex++) {
					Fragment f = (Fragment)it2.next();
					
					int col = f.getLayoutRow();
					int row = f.getLayoutColumn();
					
					if( "portlet".equals(f.getType()) ) {
						NavigationalState nav = _rc.getPortalURL().getNavigationalState();
						WindowState _ws = nav.getMappedState(f.getId());

						PortletDefinition portlet = f.getPortlet();
						if( portlet == null ) {
							portlet = registry.getPortletDefinitionByUniqueName(f.getName());
							f.setPortlet(portlet);
						}

						String _pTitle = getTitle( f, getLocale( request));
						
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
							if( style != null ) { %>
								<div id="Enview_Portlet_<%=f.getId()%>" class="portlet" style="<%=style%>">
<%		    		    	}
							else { %>
								<div id="Enview_Portlet_<%=f.getId()%>" class="portlet">
<%		    		    	} 

							String titleShow = f.getPreferenceValue("TITLE-SHOW");
							if( titleShow != null && titleShow.equals("true") ) { %>
								<div id='Enview_PortletTitle_<%=f.getId()%>' class='portlet-title' style='display:block;'>
<%							} else { %>
								<div id='Enview_PortletTitle_<%=f.getId()%>' class='portlet-title' style='display:none;'>
<%							}

							String portletActions = portletActionBar(f.getPortlet(), f.getDecoration(), _rc); %>	
								<span id="PortletEditIconArea_<%=f.getId()%>" class="portlet-icon-area" style='display:none;'>
<%							if( "/contentonly".equals(request.getServletPath()) ) { %>
									<img id='zzzzzzz' src='<%=request.getContextPath()%>/decorations/layout/images/move.png' border='0' align='absmiddle' title='<util:message key="ev.info.icon.movePortlet"/>' style='border:none; cursor:hand;'/>
									<a class='portlet-action-button' onclick="javascript:portalPage.showPortletEditor('Enview_Portlet_<%=f.getId()%>');">
										<img src='<%=request.getContextPath()%>/decorations/layout/images/edit.gif' border='0' align='absmiddle' title='<util:message key="ev.info.icon.editPortlet"/>' style='border:none; cursor:hand;'/> 
									</a>
									<a class='portlet-action-button' onclick="javascript:portalPage.removePortlet('Enview_Portlet_<%=f.getId()%>');" >
										<img src='<%=request.getContextPath()%>/decorations/layout/images/icon_del.gif' border='0' align='absmiddle' title='<util:message key="ev.info.icon.removePortlet"/>' style='border:none; cursor:hand;'/>
									</a>
<%							} %>								
								</span>
								<span class="portlet-title-content"><%= _pTitle %></span> 
								<%= portletActions %>
<%		    		    	if( f.getPreferenceValue("MORE_SRC") != null ) { %>
								<span class="portlet-title-more">
									<a href="<%=f.getPreferenceValue("MORE_SRC")%>" target="<%=f.getPreferenceValue("MORE_SRC_TARGET")%>">
										<img src="<%=themePath%>/images/portlet/tabtitle_more.gif" alt="<%=_pTitle%>">
									</a>
								</span>
<%						    } %>
								</div> <!-- portlet-title end -->
<%		    		    			
							if( "minimized".equals(_ws.toString()) == false ) { %>
								<div id="Enview_Portlet_Content_<%=f.getId()%>" class="portlet-content"><%= content %></div>
<%		    		        } %>
							</div> <!-- portlet end -->
<%		    			}
					} 
					else {
						request.setAttribute("isRoot", new Boolean(false));
						request.setAttribute("currentFragment", f);  %>
						<jsp:include page="./layout.jsp" />
<%					}
				} %>
				</div> <!-- portal-layout-column -->
<%			}
			if( fragmentLayout != null ) {
				if( fragmentLayout.getColumns().size() > 1 ) { %> 
					<br style="clear:both;"/>
<%	        	}
			}
		} %>
		</div> <!--portal-layout-->
<%	}
	catch(Exception e)
	{
		Log log = LogFactory.getLog(getClass());
		log.error( e.getMessage(), e);; 
	}
%>
