<%@page language="java" pageEncoding="UTF-8"%>
<%@ include file="../initLayoutDecorators.jsp" %>
<%@page import="com.saltware.enview.aggregator.PortletRenderer"%>
<%@page import="com.saltware.enview.components.portletregistry.PortletRegistry"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%  
	try {
		PortletRenderer renderer = (PortletRenderer)request.getAttribute("renderer");
		PortletRegistry registry = (PortletRegistry)request.getAttribute("registry");
//		System.out.println("############################# isRoot=" + request.getAttribute("isRoot"));
		boolean isRoot = (Boolean)request.getAttribute("isRoot");
		Fragment layoutFragment = (Fragment)request.getAttribute("currentFragment");
		
		String classSuffixMypage = "my";
		String layoutClass = "portal-layout";
		String columnClass = "column";
		String portletClass = "portlet-" + classSuffixMypage;
		String portletHeaderClass = "portlet-header-" + classSuffixMypage;
		String portletContentClass = "portlet-content-" + classSuffixMypage;

		if( isRoot == false ) {
			layoutClass = "portal-nested-layout";
		}
		boolean isColumnMovable = (layoutFragment.getActionMask() & 0x00000010) > 0 ;
		// 레이아웃 fragment에 이동권한이 있는지의 여부 파악 
		if( isColumnMovable ) {
			columnClass = "column-my";
		}	else {
			columnClass = "column-fixed";
			
//			portletClass = portletClass + "-fixed";
//			portletHeaderClass = portletHeaderClass + "-none";
		}
%>
		<div id="Enview_Layout_<%=layoutFragment.getId()%>" class="<%=layoutClass%>">
<%  
		FragmentLayout fragmentLayout = layoutFragment.getFragmentLayout();
		if( fragmentLayout != null ) {
			Iterator it = fragmentLayout.getColumns().iterator();
			for(int columnIndex=0; it.hasNext(); columnIndex++) {
				Collection column = (Collection)it.next();
				String columnFloat = fragmentLayout.getColumnFloat(columnIndex);
				String columnWidth = fragmentLayout.getColumnWidth(columnIndex);

%>				    		
				<div id="Enview_Column_<%=layoutFragment.getId()%>_<%=columnIndex%>" class="<%=columnClass%>" style="float:<%=columnFloat%>; width:<%=columnWidth%>;">
<%					
				Iterator it2 = column.iterator();
				for(int rowIndex=0; it2.hasNext(); rowIndex++) {
					Fragment f = (Fragment)it2.next();
					
					int col = f.getLayoutRow();
					int row = f.getLayoutColumn();
					
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

						String _pTitle = f.getPreferenceValue("TITLE");
						if( _pTitle == null ) {
							portlet = f.getPortlet();
							if( portlet != null ) {
								_pTitle = portlet.getDisplayName( _rc.getLocale() ).getDisplayName();
							}
							else {
								_pTitle = f.getName();
							}
						}
						
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
							if( isColumnMovable && isPortletMovable) {
								portletClass = "portlet-my";
							} else {
								portletClass = "portlet-fixed";
							}
							%>
							<div id="Enview_Portlet_<%=f.getId()%>" class="<%=portletClass%>" <%= style !=null ? "style='" + style + "'" : "" %>>
							<div class="<%=portletHeaderClass%>">
							<% if( isColumnMovable && isPortletRemovable ) { %><span class='ui-icon ui-icon-close'></span><%}%>
							<% if( f.getPreferenceValue("MORE-SRC") != null ) { %><span class='ui-icon ui-icon-plus' more_src="<%=f.getPreferenceValue("MORE-SRC")%>" more_target="<%=f.getPreferenceValue("MORE-TARGET")%>" more_width="<%=f.getPreferenceValue("MORE-WIDTH")%>" more_height="<%=f.getPreferenceValue("MORE-HEIGHT")%>"></span><%}%>
							<%=_pTitle %>
							</div><%		    		    			
							if( "minimized".equals(_ws.toString()) == false ) { %><div id="Enview_Portlet_Content_<%=f.getId()%>" class="<%=portletContentClass%>"><%= content %></div>
<%		    		        } %>
							</div> <!-- portlet end -->
<%		    			}
					} 
					else {
						request.setAttribute("isRoot", new Boolean(false));
						request.setAttribute("currentFragment", f);  %>
						<jsp:include page="./layoutMy.jsp" />
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
<%	}catch(Exception e)	{
		out.println( "Exception on layoutMy.jsp : " + e.getMessage()); 
	}
%>
