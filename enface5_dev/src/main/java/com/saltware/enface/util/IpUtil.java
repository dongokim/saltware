package com.saltware.enface.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sun.security.action.GetLongAction;

import com.saltware.enview.Enview;
import com.saltware.enview.capabilities.Capabilities;

public class IpUtil {
    protected static Log log = LogFactory.getLog( IpUtil.class);
    protected static List koreanIpList = null;
	protected static String koreanIpXmlUrl =  "https://xn--3e0bx5euxnjje69i70af08bea817g.xn--3e0b707e/jsp/infoboard/stats/interProCurrentXml.jsp";
	protected static String koreanIpXmlPath = "/interProCurrentXml.xml";

	
	/**
	 * IP 비교가 쉽도록 ipv4주소를 long값으로 변환한다.
	 * @param ip
	 * @return
	 */
	private static  long ip2long( String ipv4) {
		
		String s[] = ipv4.split("\\.");
		if( s.length != 4) {
			// ipv4가 아닌경우
			return 0;
		}
		
		long l = Long.parseLong(s[0]) * 255 * 255 *255
				+ Long.parseLong(s[1]) * 255 * 255
				+ Long.parseLong(s[2]) * 255
				+ Long.parseLong(s[3]);
		return l;
	}
	
	private static  String long2ip( long longIp) {
		String ip =  (longIp % 255) + "";
		longIp = longIp / 255;
		
		ip = ( longIp % 255) + "."  + ip;
		longIp = longIp / 255;
		ip = ( longIp % 255) + "."  + ip;
		longIp = longIp / 255;
		ip = longIp + "."  + ip;
		return ip;
	}
	
	
	
	
	
	public static List getIpList() {
		
		log.info("Loading korean ip list from " + koreanIpXmlUrl);
		long time = System.currentTimeMillis();
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod( koreanIpXmlUrl);
		try {
			int status = client.executeMethod(method);
			if( status == 200) {
				return getIpList( method.getResponseBodyAsStream());
			}
		} catch (HttpException e) {
			log.error( "Failed to get korean ip list from " + koreanIpXmlUrl + ":" + e.getMessage());
		} catch (IOException e) {
			log.error( "Failed to get korean ip list from " + koreanIpXmlUrl + ":" + e.getMessage());
		} catch (SAXException e) {
			log.error( "Failed to parse korean ip list from " + koreanIpXmlUrl + ":" + e.getMessage());
		} catch (ParserConfigurationException e) {
			log.error( "Failed to parse korean ip list from " + koreanIpXmlUrl + ":" + e.getMessage());
		}
		return null;
	}
	
	public static List getLocalIpList() {
		log.info("Loading korean ip list form local resource " + koreanIpXmlPath);
		try {
			return getIpList(  IpUtil.class.getClassLoader().getResourceAsStream( koreanIpXmlPath));
		} catch (HttpException e) {
			log.error( "Failed to get korean ip list from " + koreanIpXmlPath + ":" + e.getMessage());
		} catch (IOException e) {
			log.error( "Failed to get korean ip list from " + koreanIpXmlPath + ":" + e.getMessage());
		} catch (SAXException e) {
			log.error( "Failed to parse korean ip list from " + koreanIpXmlPath + ":" + e.getMessage());
		} catch (ParserConfigurationException e) {
			log.error( "Failed to parse korean ip list from " + koreanIpXmlPath + ":" + e.getMessage());
		}
		return null;
	}
	
	public static List getIpList( InputStream is) throws SAXException, ParserConfigurationException, IOException{
		List krIpList = new ArrayList();
		
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( is);
			NodeList nList = doc.getElementsByTagName("ipv4");
			for (int i = 0; i < nList.getLength(); i++) {
				Node n = nList.item(i);
				if( n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element)n;
					long[] range = new long[2];
					// 시작주소와 끝 주소를 읽은뒤 비교가 쉽게 long으로 바꾼다.
					String sno = e.getElementsByTagName("sno").item(0).getTextContent();
					String eno = e.getElementsByTagName("eno").item(0).getTextContent();
					log.trace(sno + " ~ " + eno);
					range[0] = ip2long( sno);
					range[1] = ip2long( eno);
					krIpList.add( range);
				}
			}
		return krIpList;
	}
	
	
	
	public static boolean isKoreanIp( String ip) {
		// localhost면 통과
		if( "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			return true;
		}
		
		// ip목록이 없으면 읽는다.
		if( koreanIpList==null) {
			// 한국인터넷진흥원에서 읽기
			koreanIpList = getIpList();
			if( koreanIpList==null || koreanIpList.size()==0) {
				// 로칼파일에서 읽기
				koreanIpList = getLocalIpList();
			}
		}
		
		long time= System.currentTimeMillis();
		long longIp = ip2long( ip);
		// ipv4가 아니면 통과.
		if( longIp==0) {
			return true;
		}
		for (int i = 0; i < koreanIpList.size(); i++) {
			long[] range = (long[])koreanIpList.get(i);
			if( longIp >= range[0] && longIp <= range[1]) {
				if( log.isDebugEnabled()) {
					log.debug( "korean ip : " + long2ip( range[0]) + " ~ " + long2ip( range[1]));
				}
				 return true;
			}
		}
		return false;
	}
	
	public static void main( String args[]) {
		String ip = "203.248.161.1";
		long longIp = ip2long( ip);
		System.out.println( "ip=" + ip);
		System.out.println( "longIp=" + longIp);
		System.out.println( "ip=" + long2ip( longIp));
		System.out.println( "isKoreanIp=" + isKoreanIp(ip));
	}
    
}
