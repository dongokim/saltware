<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.saltware.enview.userpreference.UserPreferenceManager"%>
<%@page import="java.util.Locale"%>
<%@page import="com.saltware.enview.om.page.impl.FragmentImpl"%>
<%@page import="com.saltware.enview.util.json.JSONObject"%>
<%@page import="com.saltware.enview.util.json.JSONArray"%>
<%@page import="com.saltware.enview.userpreference.service.UserPreferenceVO"%>
<%@page import="com.saltware.enview.Enview"%>
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
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
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
				_pTitle = portlet.getDisplayName( locale ).getDisplayName();
			}
			else {
				_pTitle = f.getName();
			}
		}
		return _pTitle;
	}

%>

<%  
	try {
		PortletRenderer renderer = (PortletRenderer)request.getAttribute("renderer");
		PortletRegistry registry = (PortletRegistry)request.getAttribute("registry");
//		System.out.println("############################# isRoot=" + request.getAttribute("isRoot"));
		boolean isRoot = (Boolean)request.getAttribute("isRoot");
		Fragment layoutFragment = (Fragment)request.getAttribute("currentFragment");
		
		String classSuffixMypage = "my";
		String layoutClass = "gridster";
		String columnClass = "column";
		String portletClass = "portlet-" + classSuffixMypage;
		String portletHeaderClass = "portlet-header-" + classSuffixMypage;
		String portletContentClass = "portlet-content-" + classSuffixMypage;
		
		UserPreferenceManager userPreferenceManager = (UserPreferenceManager) Enview.getComponentManager().getComponent("com.saltware.enview.userpreference.UserPreferenceManager");
		
		String userLayoutData = userPreferenceManager.getPreference( request, "MY",currentPage.getPath());
		
		String layoutId = layoutFragment.getId();
		JSONObject userLayout = null;
		if( userLayoutData!=null) {
			userLayout = new JSONObject( userLayoutData) ;
		}
		
%>

		<div id="Enview_Layout_<%=layoutId%>" class="<%=layoutClass%>" style="float:left;width:100%">
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
							<div class="<%=portletHeaderClass%>" style="display:hidden">
							<% if( isPortletRemovable ) { %><span class='ui-icon ui-icon-close' title="포틀릿 삭제"></span><%}%>
							<span class='ui-icon ui-icon-plus' title="포틀릿 추가"></span>
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
		JSONObject p;
		Fragment f;
		JSONArray layouts = userLayout.getJSONArray("fragmentLayout");
		for( int i=0; i < layouts.length(); i++) {
			p  = layouts.getJSONObject(i);
			String _pName = p.getString("name");
			String _pId = i + "";

			PortletDefinition pd = registry.getPortletDefinitionByUniqueName( _pName);
			
			f = new FragmentImpl();
			f.setId( _pId);
			f.setName(_pName);
			f.setPortlet( pd);
			f.setType(f.PORTLET);
			
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
				<span class='ui-icon ui-icon-close' title='포틀릿 삭제'></span>
				<span class='ui-icon ui-icon-plus' title="포틀릿 추가"></span>
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
	catch(Exception e)
	{
		Log log = LogFactory.getLog(getClass());
		log.error( e.getMessage(), e);; 
	}
%>
