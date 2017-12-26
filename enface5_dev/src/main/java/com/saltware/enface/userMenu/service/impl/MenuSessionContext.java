package com.saltware.enface.userMenu.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enboard.cache.CacheMngr;
import com.saltware.enboard.security.SecurityMngr;
import com.saltware.enboard.vo.BoardVO;
import com.saltware.enboard.vo.SecPmsnVO;
import com.saltware.enface.userMenu.service.EnviewMenuImpl;
import com.saltware.enface.userMenu.service.EnviewMenuLightWeightImpl;
import com.saltware.enhancer.util.StringUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.security.EnviewMenu;
import com.saltware.enview.security.MenuSessionConstants;

public class MenuSessionContext implements MenuSessionConstants, java.io.Serializable {
	private final Log log = LogFactory.getLog(getClass());

	private transient UserMenuDAO userMenuDAO;
	transient private CacheMngr boardCacheMngr;
	transient private PageManager pageManager;

	private boolean initialized = false;

	private String userId;
	private String principalId;
	private String groupId;
	private String groupPrincipalId;
	private String domainId;
	private String langKnd;
	private String myPageNameKey = "userId";
	private boolean isAdmin = false;

	private List quickMenuList;
	private List myMenuList;
	private List myQuickMenuList;

	private EnviewMenu rootMenu;
	private Hashtable menuIdMap;
	private Hashtable menuPathMap;

	public MenuSessionContext(UserMenuDAO userMenuDAO) {
		this.userMenuDAO = userMenuDAO;

		this.quickMenuList = new ArrayList();
		this.myMenuList = new ArrayList();
		this.myQuickMenuList = new ArrayList();
		this.menuIdMap = new Hashtable();
		this.menuPathMap = new Hashtable();
		this.initialized = false;
		this.myPageNameKey = Enview.getConfiguration().getString("mypage.pageName.key", "userId");
	}

	public CacheMngr getBoardCacheMgr() {
		if (boardCacheMngr == null) {
			boardCacheMngr = (CacheMngr) Enview.getComponentManager().getComponent("com.saltware.enboard.cache.CacheMngr");
		}
		return boardCacheMngr;
	}

	public PageManager getPageManager() {
		if (pageManager == null) {
			pageManager = (PageManager) Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		}
		return pageManager;
	}

	/**
	 * 메뉴가 게시판메뉴인지 확인하고 게시판 메뉴라면 게시판 권한이 있는지 확인한다.
	 * 
	 * @param menu
	 *            메뉴
	 * @return 메뉴 사용가능 여부
	 */
	private boolean validateBoardMenu(EnviewMenu menu) {
		String url = menu.getUrl();

		if (StringUtil.isEmpty(url)) {
			return true;
		}
		int p = url.indexOf("boardId=");
		if (p == -1) {
			return true;
		}
		int q = url.indexOf("&", p + 1);
		String boardId = (q == -1) ? url.substring(p + "boardId=".length()) : url.substring(p + "boardId=".length(), q);

		try {
			BoardVO boardVO = getBoardCacheMgr().getBoard(boardId, "ko");
			if (boardVO == null) {
				// 게시판을 못찾으면 false
				return false;
			}

			if (!boardVO.getBoardActive().equals("Y")) {
				// 게시판이 활성이 아니면 false
				return false;
			}
			HttpServletRequest request = Enview.getCurrentRequestContext().getRequest();
			SecPmsnVO secPmsnVO = SecurityMngr.getInst().getCurrentSecPmsn(boardVO, request);
			SecurityMngr.getInst().boardProtect(boardVO, "LIST", SecurityMngr.getLoginInfo(request), secPmsnVO, request.getRemoteAddr(), null, null, null, request);
		} catch (com.saltware.enboard.exception.BaseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 1. 메뉴목록에서 게시판메뉴인지 확인하고 게시판 메뉴인 경우 게시판 권한이 있는지 검사한 후 권한이 없으면 메뉴 리스트에서 제외한다. 2. 경량메뉴를 처리한다.
	 * 
	 * @param menuList
	 *            메뉴목록
	 * @return 메뉴목록
	 */
	private List filterMenuList(List menuList) {
		if (menuList == null || menuList.size() == 0) {
			return menuList;
		}
		boolean lightweight = Enview.getConfiguration().getBoolean("enview.menu.lightweight", true);
		boolean validateBoardAuth = Enview.getConfiguration().getBoolean("enview.menu.validateBoardAuth", true);

		List newList = new ArrayList();
		EnviewMenu menu;
		for (int i = 0; i < menuList.size(); i++) {
			menu = (EnviewMenu) menuList.get(i);
			// 게시판 권한체크
			if( validateBoardAuth && !validateBoardMenu(menu)) {
					continue;
			}
			if (lightweight) {
				newList.add(new EnviewMenuLightWeightImpl(menu));
			} else {
				newList.add(menu);
			}
		}
		return newList;
	}

	/**
	 * 사용자메뉴DAO를 리턴한다. 없는 경우 스프링 빈에서 가져온다.
	 * 
	 * @return
	 */
	protected UserMenuDAO getUserMenuDAO() {
		if (this.userMenuDAO == null) {
			this.userMenuDAO = (UserMenuDAO) Enview.getComponentManager().getComponent("com.saltware.enface.userMenu.service.impl.UserMenuDAO");
		}
		return this.userMenuDAO;
	}

	public void invalidateCache() throws BaseException {
		try {
			this.quickMenuList = new ArrayList();
			this.myMenuList = new ArrayList();
			this.myQuickMenuList = new ArrayList();
			this.menuIdMap = new Hashtable();
			this.menuPathMap = new Hashtable();
			this.initialized = false;

			boolean fullLoading = Enview.getConfiguration().getBoolean("enview.menu.initLoading", true);
			// if (fullLoading == true) {
			if (fullLoading) {
				log.info("*** start initLoading. userId=" + userId + ", principalId=" + principalId + ", menuType=" + MenuSessionContext.MENUTYPE_MAIN_MENU);
				List results = null;

				Map paramMap = new HashMap();
				paramMap.put("principalId", principalId);
				paramMap.put("langKnd", Enview.getLocale().getLanguage());
				paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MAIN_MENU);
				paramMap.put("domainId", domainId);
				if ("guest".equals(userId)) {
					paramMap.put("guest", "true");
					results = (List) getUserMenuDAO().findAll4Guest(paramMap);
				} else if ("admin".equals(userId) || isAdmin) {
					results = (List) getUserMenuDAO().findAll4Admin(paramMap);
				} else {
					results = (List) getUserMenuDAO().findAll(paramMap);
				}
				// 게시판 메뉴의 권한을 체크하여 권한이 없는 메뉴는 목록에서 삭제한다
				results = filterMenuList(results);

				List menuIdList = new ArrayList();
				for (Iterator it = results.iterator(); it.hasNext();) {
					EnviewMenu menu = (EnviewMenu) it.next();
					this.menuIdMap.put(menu.getPageId(), menu);
					this.menuPathMap.put(menu.getPath(), menu);
					menuIdList.add(menu.getPageId());
				}

				
				ArrayList menuList = new ArrayList();
				// log.info("*** results=" + results.size() + ", menuIdMap=" +
				// menuIdMap);
				for (Iterator it = menuIdList.iterator(); it.hasNext();) {
					String pageId = (String) it.next();

					EnviewMenu menu = (EnviewMenu) menuIdMap.get(pageId);
					String parent_id = menu.getParentId();

					// System.out.println("#### menu name=" + menu.getName() +
					// ", parentId=" + menu.getParentId());

					if (menu.isQuick()) {
						this.quickMenuList.add(menu);
					}

					// System.out.println("########## menuIdMap=" + menuIdMap);

					if (parent_id != null) {
						EnviewMenu parentMenu = (EnviewMenu) menuIdMap.get(parent_id);
						if (parentMenu != null) {
							parentMenu.addElement(menu);
							menu.setParentMenu(parentMenu);
						} else {
							log.error("ParentMenu[" + parent_id + "] is null (path=" + menu.getPath() + ")");
						}
					} else {
						this.rootMenu = menu;
					}
				}

				this.initialized = true;

				log.info("*** end initLoading. rootMenu=" + this.rootMenu);

				if (log.isDebugEnabled()) {
					traverseMenu(this.rootMenu, "+");
				}
			}
//		} catch (BaseException e) {
//			log.error(e.getMessage(), e);
		} finally {
			
		}
	}

	private void traverseMenu(EnviewMenu menu, String indent) {
		if (menu != null) {
			log.debug(indent + menu.getPath() + " : " + menu.getTitle());
			if (menu.getElements() != null) {
				for (Iterator it = menu.getElements().iterator(); it.hasNext();) {
					traverseMenu((EnviewMenu) it.next(), indent + "+");
				}
			}
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getGroupPrincipalId() {
		return groupPrincipalId;
	}

	public void setGroupPrincipalId(String groupPrincipalId) {
		this.groupPrincipalId = groupPrincipalId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getLangKnd() {
		return langKnd;
	}

	public void setLangKnd(String langKnd) {
		this.langKnd = langKnd;
	}

	public EnviewMenu getRootMenu() {
		return rootMenu;
	}

	public List getQuickMenuList() throws BaseException {
		List results = null;
			// if (this.initialized == true) {
			if (this.initialized) {
				return this.quickMenuList;
			}

			Map paramMap = new HashMap();
			paramMap.put("principalId", principalId);
			paramMap.put("langKnd", Enview.getLocale().getLanguage());
			paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MYQUICK_MENU);
			paramMap.put("domainId", domainId);
			if ("guest".equals(userId)) {
				paramMap.put("guest", "true");
				results = (List) getUserMenuDAO().findQuick4Guest(paramMap);
			} else if ("admin".equals(userId) || isAdmin) {
				results = (List) getUserMenuDAO().findQuick4Admin(paramMap);
			} else {
				results = (List) getUserMenuDAO().findQuick(paramMap);
			}

		return results;
	}

	public List getChildrenById(String pageId) throws BaseException {
		List results = null;

		try {
			EnviewMenu menu = (EnviewMenu) this.menuIdMap.get(pageId);
			if (menu != null) {
				if (menu.getElements().size() > 0) {
					results = menu.getElements();
				} else {
					Map paramMap = new HashMap();
					paramMap.put("principalId", principalId);
					paramMap.put("langKnd", Enview.getLocale().getLanguage());
					paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MAIN_MENU);
					paramMap.put("domainId", domainId);
					paramMap.put("pageId", pageId);

					List childs = null;
					if ("guest".equals(userId)) {
						paramMap.put("guest", "true");
						childs = (List) getUserMenuDAO().findById4Guest(paramMap);
					} else if ("admin".equals(userId) || isAdmin) {
						childs = (List) getUserMenuDAO().findById4Admin(paramMap);
					} else {
						childs = (List) getUserMenuDAO().findById(paramMap);
					}

					// 게시판 메뉴의 권한을 체크하여 권한이 없는 메뉴는 목록에서 삭제한다
					childs = filterMenuList(childs);

					if (childs != null) {
						for (Iterator it = childs.iterator(); it.hasNext();) {
							EnviewMenu childMenu = (EnviewMenu) it.next();
							this.menuIdMap.put(childMenu.getPageId(), childMenu);
							this.menuPathMap.put(childMenu.getPath(), childMenu);

							if (childMenu.isQuick()) {
								this.quickMenuList.add(childMenu);
							}
						}

						menu.setElements(childs);
						results = childs;
					}
				}
			} else {
				Map paramMap = new HashMap();
				paramMap.put("principalId", principalId);
				paramMap.put("langKnd", Enview.getLocale().getLanguage());
				paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MAIN_MENU);
				paramMap.put("pageId", pageId);
				paramMap.put("domainId", domainId);
				if ("guest".equals(userId)) {
					paramMap.put("guest", "true");
					menu = getUserMenuDAO().detail4Guest(paramMap);
				} else if ("admin".equals(userId) || isAdmin) {
					menu = getUserMenuDAO().detail4Admin(paramMap);
				} else {
					menu = getUserMenuDAO().detail(paramMap);
				}

				if (menu != null) {
					this.menuIdMap.put(menu.getPageId(), menu);
					this.menuPathMap.put(menu.getPath(), menu);

					if (menu.isQuick()) {
						this.quickMenuList.add(menu);
					}

					List childs = null;
					if ("guest".equals(userId)) {
						paramMap.put("guest", "true");
						childs = (List) getUserMenuDAO().findById4Guest(paramMap);
					} else if ("admin".equals(userId) || isAdmin) {
						childs = (List) getUserMenuDAO().findById4Admin(paramMap);
					} else {
						childs = (List) getUserMenuDAO().findById(paramMap);
					}

					if (childs != null) {
						// 게시판 메뉴의 권한을 체크하여 권한이 없는 메뉴는 목록에서 삭제한다
						childs = filterMenuList(childs);

						for (Iterator it = childs.iterator(); it.hasNext();) {
							EnviewMenu childMenu = (EnviewMenu) it.next();
							this.menuIdMap.put(childMenu.getPageId(), childMenu);
							this.menuPathMap.put(childMenu.getPath(), childMenu);

							if (childMenu.isQuick()) {
								this.quickMenuList.add(childMenu);
							}
						}

						menu.setElements(childs);
						results = childs;
					}
				}
			}
//		} catch (BaseException e) {
//			// log.error( e.getMessage(), e);;
//			throw new BaseException(e);
		} finally {
			
		}

		return results;
	}

	public List getChildrenByPath(String pagePath) throws BaseException {
		List results = null;

		// System.out.println("###### menuPathMap=" + menuPathMap);

		try {
			EnviewMenu menu = (EnviewMenu) this.menuPathMap.get(pagePath);
			if (menu != null) {
				if (menu.getElements().size() > 0) {
					results = menu.getElements();
				} else {
					Map paramMap = new HashMap();
					paramMap.put("principalId", principalId);
					paramMap.put("langKnd", Enview.getLocale().getLanguage());
					paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MAIN_MENU);
					paramMap.put("path", pagePath);
					paramMap.put("domainId", domainId);

					List childs = null;
					if ("guest".equals(userId)) {
						paramMap.put("guest", "true");
						childs = (List) getUserMenuDAO().findByPath4Guest(paramMap);
					} else if ("admin".equals(userId) || isAdmin) {
						childs = (List) getUserMenuDAO().findByPath4Admin(paramMap);
					} else {
						childs = (List) getUserMenuDAO().findByPath(paramMap);
					}

					if (childs != null) {
						// 게시판 메뉴의 권한을 체크하여 권한이 없는 메뉴는 목록에서 삭제한다
						childs = filterMenuList(childs);

						for (Iterator it = childs.iterator(); it.hasNext();) {
							EnviewMenu childMenu = (EnviewMenu) it.next();
							this.menuIdMap.put(childMenu.getPageId(), childMenu);
							this.menuPathMap.put(childMenu.getPath(), childMenu);

							if (childMenu.isQuick()) {
								this.quickMenuList.add(childMenu);
							}
						}

						menu.setElements(childs);
						results = childs;
					}
				}
			} else {
				Map paramMap = new HashMap();
				paramMap.put("principalId", principalId);
				paramMap.put("langKnd", Enview.getLocale().getLanguage());
				paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MAIN_MENU);
				paramMap.put("path", pagePath);
				paramMap.put("domainId", domainId);

				if ("guest".equals(userId)) {
					paramMap.put("guest", "true");
					menu = getUserMenuDAO().detailByPath4Guest(paramMap);
				} else if ("admin".equals(userId) || isAdmin) {
					menu = getUserMenuDAO().detailByPath4Admin(paramMap);
				} else {
					menu = getUserMenuDAO().detailByPath(paramMap);
				}

				if (menu != null) {
					this.menuIdMap.put(menu.getPageId(), menu);
					this.menuPathMap.put(menu.getPath(), menu);

					if (menu.isQuick()) {
						this.quickMenuList.add(menu);
					}

					List childs = null;
					if ("guest".equals(userId)) {
						paramMap.put("guest", "true");
						childs = (List) getUserMenuDAO().findByPath4Guest(paramMap);
					} else if ("admin".equals(userId) || isAdmin) {
						childs = (List) getUserMenuDAO().findByPath4Admin(paramMap);
					} else {
						childs = (List) getUserMenuDAO().findByPath(paramMap);
					}

					if (childs != null) {
						// 게시판 메뉴의 권한을 체크하여 권한이 없는 메뉴는 목록에서 삭제한다
						childs = filterMenuList(childs);
						for (Iterator it = childs.iterator(); it.hasNext();) {
							EnviewMenu childMenu = (EnviewMenu) it.next();
							this.menuIdMap.put(childMenu.getPageId(), childMenu);
							this.menuPathMap.put(childMenu.getPath(), childMenu);

							if (childMenu.isQuick()) {
								this.quickMenuList.add(childMenu);
							}
						}

						menu.setElements(childs);
						results = childs;
					}
				}
			}
//		} catch (BaseException e) {
//			// log.error( e.getMessage(), e);;
//			throw new BaseException(e);
		} finally {
			
		}

		return results;
	}

	public List getMenuList(String principalType, int menuType) throws BaseException {
		List results = null;
		try {

			Map paramMap = new HashMap();
			paramMap.put("langKnd", Enview.getLocale().getLanguage());
			paramMap.put("domainId", domainId);
			if ("U".equals(principalType)) {
				paramMap.put("principalId", principalId);
			} else {
				paramMap.put("principalId", groupPrincipalId);
			}

			switch (menuType) {
			case MenuSessionContext.MENUTYPE_MAIN_MENU:
				// paramMap.put("menuType", "" + this.MENUTYPE_MAIN_MENU);
				break;
			case MenuSessionContext.MENUTYPE_MY_MENU:
				paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MY_MENU);
				break;
			case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
				paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MYQUICK_MENU);
				break;
			default:
				break;
			}
			results = (List) getUserMenuDAO().findAll(paramMap);
//		} catch (BaseException e) {
//			// log.error( e.getMessage(), e);;
//			throw new BaseException(e);
		} finally {
			
		}

		return results;
	}

	public void addMenuList(String principalType, int menuType, String pageIds) throws BaseException {
		try {
			List newList = new ArrayList();

			String pid = null;
			if ("U".equals(principalType)) {
				pid = principalId;
			} else {
				pid = groupPrincipalId;
			}

			log.info("*** pid=" + pid);

			String[] pageIdArray = pageIds.split(",");
			for (int i = 0; i < pageIdArray.length; i++) {
				EnviewMenu menu = new EnviewMenuImpl();
				menu.setPrincipalId(pid);
				menu.setLangKnd(langKnd);
				menu.setPageId(pageIdArray[i]);
				switch (menuType) {
				case MenuSessionContext.MENUTYPE_MAIN_MENU:
					menu.setMenuType("" + MenuSessionContext.MENUTYPE_MAIN_MENU);
					break;
				case MenuSessionContext.MENUTYPE_MY_MENU:
					menu.setMenuType("" + MenuSessionContext.MENUTYPE_MY_MENU);
					break;
				case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
					menu.setMenuType("" + MenuSessionContext.MENUTYPE_MYQUICK_MENU);
					break;
				default:
					break;
				}

				newList.add(menu);
			}

			getUserMenuDAO().insertList(newList);

			Map paramMap = new HashMap();
			paramMap.put("principalId", pid);
			paramMap.put("langKnd", Enview.getLocale().getLanguage());
			paramMap.put("menuType", "" + menuType);
			// System.out.println("************************ domainId=["+domainId+"]");
			// System.out.println("************************ paramMap.put(\"domainId\", domainId)=["+paramMap.put("domainId",
			// domainId)+"]");
			newList = (List) getUserMenuDAO().findAll(paramMap);

			switch (menuType) {
			case MenuSessionContext.MENUTYPE_MAIN_MENU:
				break;
			case MenuSessionContext.MENUTYPE_MY_MENU:
				this.myMenuList = newList;
				break;
			case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
				this.myQuickMenuList = newList;
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			// log.error( e.getMessage(), e);;
			throw new BaseException(e);
		}
	}

	public void setMenuList(String principalType, int menuType, String pageIds) throws BaseException {
		try {
			List newList = new ArrayList();

			String pid = null;
			if ("U".equals(principalType)) {
				pid = principalId;
			} else {
				pid = groupPrincipalId;
			}

			log.info("*** pid=" + pid);

			Map paramMap = new HashMap();
			paramMap.put("principalId", pid);
			paramMap.put("menuType", "" + menuType);
			getUserMenuDAO().deleteMyMenuAll(paramMap);

			String[] pageIdArray = pageIds.split(",");
			for (int i = 0; i < pageIdArray.length; i++) {
				EnviewMenu menu = new EnviewMenuImpl();
				menu.setPrincipalId(pid);
				menu.setLangKnd(langKnd);
				menu.setPageId(pageIdArray[i]);
				switch (menuType) {
				case MenuSessionContext.MENUTYPE_MAIN_MENU:
					menu.setMenuType("" + MenuSessionContext.MENUTYPE_MAIN_MENU);
					break;
				case MenuSessionContext.MENUTYPE_MY_MENU:
					menu.setMenuType("" + MenuSessionContext.MENUTYPE_MY_MENU);
					break;
				case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
					menu.setMenuType("" + MenuSessionContext.MENUTYPE_MYQUICK_MENU);
					break;
				default:
					break;
				}

				newList.add(menu);
			}

			getUserMenuDAO().insertList(newList);

			paramMap.put("langKnd", Enview.getLocale().getLanguage());
			newList = (List) getUserMenuDAO().findAll(paramMap);

			switch (menuType) {
			case MenuSessionContext.MENUTYPE_MAIN_MENU:
				break;
			case MenuSessionContext.MENUTYPE_MY_MENU:
				this.myMenuList = newList;
				break;
			case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
				this.myQuickMenuList = newList;
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			// log.error( e.getMessage(), e);;
			throw new BaseException(e);
		}
	}

	public void deleteMenuList(String principalType, int menuType, String pageId, String domainId) throws BaseException {
		try {
			List deleteList = new ArrayList();

			String pid = null;
			if ("U".equals(principalType)) {
				pid = principalId;
			} else {
				pid = groupPrincipalId;
			}

			EnviewMenu menu = new EnviewMenuImpl();
			menu.setPrincipalId(pid);
			menu.setPageId(pageId);
			menu.setLangKnd(langKnd);
			menu.setDomainId(domainId);
			switch (menuType) {
			case MenuSessionContext.MENUTYPE_MAIN_MENU:
				menu.setMenuType("" + MenuSessionContext.MENUTYPE_MAIN_MENU);
				break;
			case MenuSessionContext.MENUTYPE_MY_MENU:
				menu.setMenuType("" + MenuSessionContext.MENUTYPE_MY_MENU);
				break;
			case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
				menu.setMenuType("" + MenuSessionContext.MENUTYPE_MYQUICK_MENU);
				break;
			default:
				break;
			}

			deleteList.add(menu);

			getUserMenuDAO().deleteList(deleteList);

			Map paramMap = new HashMap();
			paramMap.put("principalId", pid);
			paramMap.put("langKnd", Enview.getLocale().getLanguage());
			paramMap.put("menuType", "" + menuType);

			List newList = (List) getUserMenuDAO().findAll(paramMap);

			switch (menuType) {
			case MenuSessionContext.MENUTYPE_MAIN_MENU:

				break;
			case MenuSessionContext.MENUTYPE_MY_MENU:
				this.myMenuList = newList;
				break;
			case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
				this.myQuickMenuList = newList;
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			// log.error( e.getMessage(), e);;
			throw new BaseException(e);
		}
	}

	public void changeOrder(String principalType, int menuType, String pageId, boolean toDown) throws BaseException {
		try {
			String pid = null;
			if ("U".equals(principalType)) {
				pid = principalId;
			} else {
				pid = groupPrincipalId;
			}

			Map paramMap = new HashMap();
			paramMap.put("principalId", pid);
			paramMap.put("langKnd", Enview.getLocale().getLanguage());
			paramMap.put("menuType", "" + menuType);
			paramMap.put("domainId", domainId);

			// System.out.println("###### paramMap=" + paramMap + ", toDown=" +
			// toDown + ", pageId=" + pageId);

			List results = (List) getUserMenuDAO().findAll(paramMap);
			// System.out.println("###### results=" + results);

			List newList = new ArrayList();
			EnviewMenu currentVO = null;
			int currentIdx = 0;

			Iterator it = results.iterator();
			for (int i = 0; i < results.size(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu) it.next();
				enviewMenu.setPrincipalId((String) paramMap.get("principalId"));
				enviewMenu.setLangKnd((String) paramMap.get("langKnd"));
				enviewMenu.setMenuType((String) paramMap.get("menuType"));

				// if (pageId.equals(enviewMenu.getPageId()) == true) {
				if (pageId.equals(enviewMenu.getPageId())) {
					currentIdx = i;
					currentVO = enviewMenu;
				} else {
					newList.add(enviewMenu);
				}
			}

			// System.out.println("###### currentIdx=" + currentIdx);

			int childCount = results.size();
			// if (toDown == true) {
			if (toDown) {
				if (currentIdx == (childCount - 1)) {
					newList.add(0, currentVO);
				} else {
					newList.add(currentIdx + 1, currentVO);
				}
			} else {
				if (currentIdx == 0) {
					newList.add(newList.size(), currentVO);
				} else {
					newList.add(currentIdx - 1, currentVO);
				}
			}

			// System.out.println("###### newList=" + newList);

			getUserMenuDAO().updateList(newList);

			switch (menuType) {
			case MenuSessionContext.MENUTYPE_MAIN_MENU:

				break;
			case MenuSessionContext.MENUTYPE_MY_MENU:
				this.myMenuList = newList;
				break;
			case MenuSessionContext.MENUTYPE_MYQUICK_MENU:
				this.myQuickMenuList = newList;
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			throw new BaseException(e);
		}
	}

	public void changeMyPageOrder(String pageId, boolean toDown) throws BaseException {
		try {
			Map paramMap = new HashMap();
			if ("userId".equals(myPageNameKey)) {
				paramMap.put("userId", userId);
			} else {
				paramMap.put("userId", principalId);
			}
			paramMap.put("langKnd", Enview.getLocale().getLanguage());
			paramMap.put("domainId", domainId);

			List results = (List) getUserMenuDAO().findByUser(paramMap);
			// System.out.println("###### results=" + results);

			List newList = new ArrayList();
			EnviewMenu currentVO = null;
			int currentIdx = 0;

			Iterator it = results.iterator();
			for (int i = 0; i < results.size(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu) it.next();

				// if (pageId.equals(enviewMenu.getPageId()) == true) {
				if (pageId.equals(enviewMenu.getPageId())) {
					currentIdx = i;
					currentVO = enviewMenu;
				} else {
					newList.add(enviewMenu);
				}
			}

			// System.out.println("###### currentIdx=" + currentIdx);

			int childCount = results.size();
			// if (toDown == true) {
			if (toDown) {
				if (currentIdx == (childCount - 1)) {
					newList.add(0, currentVO);
				} else {
					newList.add(currentIdx + 1, currentVO);
				}
			} else {
				if (currentIdx == 0) {
					newList.add(newList.size(), currentVO);
				} else {
					newList.add(currentIdx - 1, currentVO);
				}
			}

			// System.out.println("###### newList=" + newList);

			getUserMenuDAO().updateMyPageOrderList(newList);
		} catch (SQLException e) {
			throw new BaseException(e);
		}
	}

	public List getNavigation(String principalType, String rootPath, String requestPath) {
		List results = new ArrayList();

		return results;
	}

	public List getMyPageList() {
		Map paramMap = new HashMap();
		if ("userId".equals(myPageNameKey)) {
			paramMap.put("userId", userId);
		} else {
			paramMap.put("userId", principalId);
		}
		paramMap.put("langKnd", Enview.getLocale().getLanguage());
		paramMap.put("domainId", domainId);

		return (List) getUserMenuDAO().findByUser(paramMap);
	}

	public List getGroupPageList() {
		Map paramMap = new HashMap();
		paramMap.put("groupId", groupId);
		paramMap.put("langKnd", Enview.getLocale().getLanguage());
		paramMap.put("domainId", domainId);

		return (List) getUserMenuDAO().findByGroup(paramMap);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{").append("menu.size=").append(menuIdMap == null ? 0 : menuIdMap.size()).append(", quickMenu.size=").append(quickMenuList == null ? 0 : quickMenuList.size()).append(", myMenu.size=").append(myMenuList == null ? 0 : myMenuList.size())
				.append(", myQuickMenu.size=").append(myQuickMenuList == null ? 0 : myQuickMenuList.size()).append("}");
		return sb.toString();
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
