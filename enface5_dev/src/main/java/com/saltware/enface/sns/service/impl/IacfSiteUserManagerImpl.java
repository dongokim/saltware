package com.saltware.enface.sns.service.impl;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.saltware.enface.user.service.UserVO;
import com.saltware.enface.user.service.impl.UserException;
import com.saltware.enface.util.StringUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.util.json.JSONException;
import com.saltware.enview.util.json.JSONObject;

public class IacfSiteUserManagerImpl extends com.saltware.enface.user.service.impl.SiteUserManagerImpl{

	public IacfSiteUserManagerImpl() {
		super();
	}
	
	/**
	 * SNS에서 포털의 사용자 정보를 읽어 SYNC한다.
	 */
	public String authenticate( String userId, String password) throws UserException {
		
		log.info("*** IACF SiteUserManager authenticate");
		PortalConfiguration conf = Enview.getConfiguration();
		// String portalUrl =
		// "http://portal.saltware.co.kr:8080/user/loginProcess.face?destination=/statics/test/userJson.jsp";
		String portalUrl = conf.getString("sns.portal.authencate.url");
		String groupPrefix = conf.getString("sns.portal.groupId.prefix", "");
		String groupSuffix = conf.getString("sns.portal.groupId.suffix", "");
		String userSuffix = conf.getString("sns.portal.userId.suffix", "");
		String roleId = conf.getString("sns.portal.roleId", "ROLE_COMMON");
		
		if (portalUrl == null || ! userId.endsWith(userSuffix) ) {
			return super.authenticate(userId, password);
		} else {
			try {
				String portalUserId = userId;
				if( ! StringUtil.isEmpty(userSuffix) && userId.endsWith(userSuffix)) {
					portalUserId = userId.substring(0, userId.length() - userSuffix.length());
				}
				// 인증(아이디,비밀번호 확인)
				if (portalUrl.indexOf("?") == -1) {
					portalUrl += "?userId=" + portalUserId + "&password=" + password;
				} else {
					portalUrl += "&userId=" + portalUserId + "&password=" + password;
				}
				log.debug("** callUrl : " + portalUrl);
				String resultString = httpGet(portalUrl);
				log.debug("** response : " + resultString);
				
				if( resultString.startsWith("HTTP")) {
					throw new UserException("Backend server error : " + resultString);
				}

				JSONObject result = null;
				try {
					result = new JSONObject(resultString);
				} catch (JSONException je) {
					log.error(je, je);
					throw new UserException(je);
				}

				String status = result.getString("Status");
				if (!"Success".equals(status)) {
					String reason = result.getString("Reason");
					if (reason.length() > 100) {
						reason = reason.substring(0, 97) + "...";
					}
					throw new UserException( reason);
				}
				log.debug("** Authenticate success!");
				JSONObject userObject = result.getJSONObject("UserInfo");
				
				syncUser(userObject, userSuffix, groupPrefix, groupSuffix, roleId);

			} catch (JSONException e) {
				throw new UserException(e);
			} catch (SQLException e) {
				throw new UserException(e);
			} catch (IOException e) {
				throw new UserException(e);
			} catch (BaseException e) {
				throw new UserException( e);
			}
		}
		return "";
	}
	
	public void syncUser( JSONObject userObject, String userSuffix, String groupPrefix, String groupSuffix, String roleId) throws JSONException, BaseException, SQLException {
		log.debug("** Sync user info");
		String userId = userObject.getString("userId");
		if( !userId.endsWith( userSuffix)) {
			userId = userId + userSuffix;
		}
		
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		UserVO userVO = siteUserManagerDAO.getUserDetail(paramMap);

		boolean insertFlag = false;
		if (userVO == null) {
			insertFlag = true;
			userVO = new UserVO();
			userVO.setUserId(userId);
			userVO.setPrincipalId(this.idGenerator.getNextIntegerId("SECURITY_PRINCIPAL"));
			userVO.setCredentialId(this.idGenerator.getNextIntegerId("SECURITY_CREDENTIAL"));
			userVO.setColumnValue(passwordEncoder.encode(userVO.getUserId(), userVO.getUserId()));
			userVO.setRoleId(roleId);
			// 신규일때는 닉네임에 사용자 이름을 넣는다.
			userVO.setNmNic(userObject.getString("nmKor"));
		}
		userVO.setDomainId( Integer.parseInt(Enview.getUserDomain().getDomainId()));
		userVO.setNmKor(userObject.getString("nmKor"));
		userVO.setEmailAddr(userObject.getString("emailAddr"));
		userVO.setMobileTel(userObject.getString("mobileTel"));
		
		// 소속
		userVO.setUserInfo04(userObject.getString("orgName"));
		
		// 직책
		if( userObject.has("ordCd")) {
			userVO.setOrgCd(userObject.getString("orgCd"));
		}
		
		// 그룹. 원래 그룹에 지정된 접두사, 접미사를 붙인다.
		String[] groups = null;
		String posiNameByGroup = "";
		if (userObject.has("groups")) {
			groups = userObject.getString("groups").split(",");
			for (int i = 0; i < groups.length; i++) {
				if( groups[i].indexOf("_STU") != -1) {
					posiNameByGroup = "학생";
				}else if( groups[i].indexOf("_PRO") != -1) {
					posiNameByGroup = "교원";
				}else if( groups[i].indexOf("_PER") != -1) {
					posiNameByGroup = "직원";
				}
				groups[i] = groupPrefix.trim() + groups[i].trim() + groupSuffix.trim();
			}
		}
		
		// 직책
		if( userObject.has("posiName") && userObject.getString("posiName").trim().length() > 0) {
			userVO.setUserInfo10( userObject.getString("posiName"));
		} else {
			userVO.setUserInfo10( posiNameByGroup);
		}
		
		// 역할
		String[] roles = null;
		// if( userObject.has("roles")) {
		// roles = userObject.getString("roles").split(",");
		// }
		
		// 퇴직구분(한전산업개발)
		userVO.setUserInfo09(userObject.getString("usests"));
		// 생일정보(한전산업개발)
		if( userObject.has("birthday") && userObject.getString("birthday").trim().length() > 0) {
			userVO.setUserInfo08(userObject.getString("birthday"));
		} else {
			userVO.setUserInfo08("");
		}		
		
		if (insertFlag) {
			siteUserManagerDAO.regist(userVO, groups, roles);
		} else {
			siteUserManagerDAO.update(userVO, groups, roles);
		}
		log.debug("** Sync user info ok!");
	}
	
	public void syncUsers( Map paramMap) throws BaseException {
		if( paramMap == null) {
			return;
		}
		PortalConfiguration conf = Enview.getConfiguration();
		String syncUsersUrl = conf.getString("sns.portal.syncUsers.url");
		String groupPrefix = conf.getString("sns.portal.groupId.prefix", "");
		String groupSuffix = conf.getString("sns.portal.groupId.suffix", "");
		String userSuffix = conf.getString("sns.portal.userId.suffix", "");
		String roleId = conf.getString("sns.portal.roleId", "ROLE_COMMON");
		
		String batchDay = StringUtil.nvl((String)paramMap.get("batchDay"));
		
		int batchPage = 1;
		int batchSize = 0;
		
		try {
			batchPage = Integer.parseInt((String)paramMap.get("batchPage"));
		} catch (NumberFormatException e) {
		}
		
		try {
			batchSize = Integer.parseInt((String)paramMap.get("batchSize"));
		} catch (NumberFormatException e) {
		}
		
		int startLine = 0;
		int endLine = 0;
		if( batchSize > 0) {
			startLine = batchSize * ( batchPage-1);
			endLine = batchSize * ( batchPage) - 1;
		}
		
		
		if( batchDay != null) {
			syncUsersUrl += "?batchDay=" + batchDay; 
		}
		
		int count = 0;

		BufferedReader br = null;
		String line;
		String cols[] = new String[] {"userId", "nmKor", "posiName", "orgCd", "orgName", "emailAddr", "mobileTel", "groups"};
		String data[];
		try {
			br = new BufferedReader( new InputStreamReader( httpGetAsStream(syncUsersUrl)));
			while((line=br.readLine())!= null) {
				// 시작 skip
				if( count < startLine) {
					count++;
					continue;
				}
				if( endLine > 0 && count > endLine) {
					break;
				}
				
				line = line.trim();
				log.debug( line);
				if( line.length()==0) continue;
				data = line.split("\\t");
				if( data.length != cols.length) {
					log.error("invalid field count " + data.length + " / " + cols.length  + " : "  + line);
					continue;
				}
				JSONObject userObject = new JSONObject();
				for (int i = 0; i < data.length; i++) {
					if( cols[i].equals("userId")) {
						userObject.put( cols[i], data[i] + userSuffix);
					} else {
						userObject.put( cols[i], data[i]);
					}
				}
				if( !userObject.has("groups")) {
					userObject.put("groups", "UOS_STU1");
				}
				try {
					syncUser(userObject, userSuffix, groupPrefix, groupSuffix, roleId);
				} catch (SQLException e) {
					writeSyncLog( "Error:" + e.getMessage());
					writeSyncLog( line);
					log.error( e, e);
					continue;
				}
				count++;
				if( count % 100==0) {
					writeSyncLog( "Processing " + count);
				}
			}
			if( paramMap != null) {
				paramMap.put("count", count+"");
			}
			
		} catch (IOException ie) {
			log.error( ie, ie);
			throw new BaseException( ie);
		} catch (JSONException je) {
			log.error( je, je);
			throw new BaseException( je);
//		} catch (SQLException se) {
//			log.error( se, se);
//			throw new BaseException( se);
		}
	}

	public String httpGet(String addr) throws IOException {
		int status = 0;
		URL url = new URL( addr);
		GetMethod getMethod = new GetMethod( addr);
		getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		
		if( url.getProtocol().equals("https")) {
			Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
			HostConfiguration hc = new HostConfiguration();
			hc.setHost(url.getHost(), url.getPort(), easyhttps);
			HttpClient client = new HttpClient();
			status = client.executeMethod(hc, getMethod);
		} else {
			HttpClient httpClient = new HttpClient();
			status = httpClient.executeMethod(getMethod);
		}
		if (status != 200) {
			throw new IOException( getMethod.getStatusText());
		} else {
			return getMethod.getResponseBodyAsString();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static InputStream httpGetAsStream(String addr) throws IOException {
		int status = 0;
		URL url = new URL( addr);
		GetMethod getMethod = new GetMethod( addr);
		getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		
		if( url.getProtocol().equals("https")) {
			Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
			HostConfiguration hc = new HostConfiguration();
			hc.setHost(url.getHost(), url.getPort(), easyhttps);
			HttpClient client = new HttpClient();
			status = client.executeMethod(hc, getMethod);
		} else {
			HttpClient httpClient = new HttpClient();
			status = httpClient.executeMethod(getMethod);
		}
		if (status != 200) {
			throw new IOException( getMethod.getStatusText());
		} else {
			return getMethod.getResponseBodyAsStream();
		}
	}
	

	public String httpPost(String url, Map paramMap) throws IOException {
		HttpClient httpClient = new HttpClient();
		System.out.println("Url:" + url);
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		String key, value;
		Iterator it = paramMap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			value = (String) paramMap.get(key);
			post.addParameter(key, value);
		}
		int status = httpClient.executeMethod(post);
		System.out.println("Status:" + post.getStatusLine());
		return new String(post.getResponseBody(), "utf-8");

	}
	
	public void writeSyncLog( String msg) {
		FileWriter fw = null;
		try {
			fw = new FileWriter("/tmp/sync.log", true);
			fw.write( msg);
			fw.write("\n");
		} catch (IOException e) {
			System.out.println( msg);
		} finally {
			if( fw!=null) {
				try {
					fw.close();
				} catch (IOException e2) {
				}
			}
		}
	}

}
