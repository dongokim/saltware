package com.saltware.enface.enboard.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enboard.cache.CacheMngr;
import com.saltware.enboard.dao.BoardDAO;
import com.saltware.enboard.exception.BaseException;
import com.saltware.enboard.exception.MustLoginException;
import com.saltware.enboard.security.SecurityMngr;
import com.saltware.enboard.util.Constants;
import com.saltware.enboard.util.FormatUtil;
import com.saltware.enboard.util.ValidateUtil;
import com.saltware.enboard.vo.BoardVO;
import com.saltware.enboard.vo.SecPmsnVO;
import com.saltware.enview.Enview;
import com.saltware.enview.admin.attachfile.service.impl.AttachFileServiceImpl;
import com.saltware.enview.components.dao.ConnectionContext;
import com.saltware.enview.components.dao.ConnectionContextForRdbms;
import com.saltware.enview.components.dao.DAOFactory;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;

public class FileDownload {
	private static Log logger = LogFactory.getLog( FileDownload.class );

	private String upload       = null;
	private String updir_attach = null;
	private String updir_editor = null;
	private String updir_thumb = null;
	private String updir_poll = null;
	private String encode       = null;
	private String sep          = null;

	private static FileDownload singleton;
	private CacheMngr cacheMngr;
	private MultiResourceBundle mrBun;

	public static FileDownload getInst( HttpServletRequest request ) {
        synchronized( FileUpload.class ) {
            if( singleton == null )
                singleton = new FileDownload( request );
        }
        return singleton;
	}

	public FileDownload( HttpServletRequest request ) {
		
		cacheMngr = (CacheMngr)Enview.getComponentManager().getComponent("com.saltware.enboard.cache.CacheMngr");

		// 환경파일(enBoard.properties)에서 첨부파일 디렉터리를 읽어온다.
		// 환경설정이 없으면, CONTEXT_ROOT/upload 디렉터리를 default로 사용한다.
		// 2009.02.23.KWShin.
		sep	= System.getProperty("file.separator");
		upload = Constants.getUploadPath( request);
		upload =  upload  + sep;
		logger.info("upload=["+upload+"]");

		updir_attach = upload + "attach" + sep;
		updir_editor = upload + "editor" + sep;
		updir_thumb = upload + "thumb" + sep;
		updir_poll = Constants.getPollUploadPath(request) + sep;
		
		encode 		 = Enview.getConfiguration().getString("board.download.encode");
	}

	public void doViewImage( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		boolean isThumbnail = false;
		boolean isPoll = false;
		try {
			String uri = request.getRequestURI();
			
			// no.gif처리 
			if( uri.endsWith("/no.gif")) {
				sendFile(request, response, new File( updir_thumb + "no.gif" ));
				return;
			}
			
			// 쎔네일 여부 체크
			isThumbnail = uri.indexOf("/thumb/") != -1;
			isPoll = uri.indexOf("/poll/") != -1;
			

			// URI에서 j 게시판 ID와 파일마스크 분리
			String[] uriArray = uri.split("/");
			if( uriArray.length<2) {
				logger.error( "uri is not valid : " + uri);
				response.setStatus(404);
				return;
			}
		    String boardId   = uriArray[ uriArray.length-2];
		    String fileMask   = uriArray[ uriArray.length-1];
		    
		    
	    	//쎔네일인 경우 권한 체크를 위해 원본 파일의 마스크로 변경
		    String thumbFileMask = fileMask;
		    if( isThumbnail) {
		    	fileMask = "T" + fileMask.substring(4);
		    }
		    
		    String extnSep = Enview.getConfiguration().getString(Constants.PROP_EXTN_SEPERATOR);
//	        String checkFile = "";
//	        if (fileMask.indexOf(extnSep) > -1) 
//	        	checkFile = fileMask.substring(0, fileMask.indexOf(extnSep));

			if ((fileMask == null || fileMask.trim().length() == 0 || fileMask.indexOf("/") > -1 
					 
//			|| fileMask.indexOf(extnSep) == -1 
			  || fileMask.indexOf("..") > -1 || fileMask.indexOf("\\") > -1)
			// 파일명 제한제거			  
//			  || (checkFile.length() != 21 && checkFile.length() != 22)
//			  || (fileMask.indexOf("20") != 0 && fileMask.indexOf("T20") != 0)
				) {
				logger.error( "invalid file : " + boardId + "/" + fileMask);
				response.setStatus(403);
				return;
			}
		    
		    
		    
			String userAgent = request.getHeader("user-agent");
			
			
		    String bltnNo;
		    String fileSeq;
		    
			
		    //String testEncode   = request.getParameter("e");
			//String testFileMask = request.getParameter("fm");
			//String testFileName = request.getParameter("fn");
			//String testSubId    = request.getParameter("s");
		    
		    //if ((Util.validateAllowChar(boardId) && Util.validateNumber(bltnNo) && Util.validateNumber(fileSeq) && "sub06".equals(subId)) || 
		    //    ("200601011111111111111_jpg".equals(testFileMask) && "sub01".equals(testSubId) && ("1".equals(testEncode) || "2".equals(testEncode) || "3".equals(testEncode)))) { 
			if( !ValidateUtil.validateAllowChar(boardId)) {
				logger.error( "board id is not valid : " + boardId);
				response.setStatus(404);
				return;
			}

		        ConnectionContext connCtxt  = null;
		        Connection        conn      = null;
		        PreparedStatement pstmt     = null;
		        ResultSet         rslt      = null;
		        BoardVO           boardVO   = null;
		        SecPmsnVO         secPmsnVO = null;
		        
//		        String fileMask = null;
		        String fileName = null;
		        
				mrBun = EnviewMultiResourceManager.getInstance().getBundle( SecurityMngr.getLocale( request ));

		        try { 
					connCtxt = new ConnectionContextForRdbms(true);
		        	
		            boardVO   = cacheMngr.getBoard (boardId, SecurityMngr.getLocale(request));
					secPmsnVO = SecurityMngr.getInst().getCurrentSecPmsn (boardVO, request);			
					SecurityMngr.getInst().boardProtect (boardVO, "READ", SecurityMngr.getLoginInfo(request), secPmsnVO, request.getRemoteAddr(), null, null, null, request);
						
					/*
					// 게시물별 권한을 적용하지 않고 파일명이 필요 없으므로 게시물 조회는 하지 않음
		            conn = connCtxt.getConnection();
		            // boardId와 fileMask에서 fileSeq를 찾는다.
		            pstmt = conn.prepareStatement("SELECT bltn_no, file_seq, file_nm, file_mask FROM "+boardVO.getFileTbl()+" WHERE board_id = ? AND file_mask=?");
		            pstmt.setString (1, boardId);
		            pstmt.setString (2, fileMask);
		            rslt = pstmt.executeQuery();
		            if (rslt.next()) {
		            	 bltnNo = rslt.getString(1);
		            	 fileSeq = rslt.getString(2);
		                 fileName = rslt.getString(3);
		                 fileMask = rslt.getString(4);
		            } else {
		            	// 파일없음
						logger.error( "bltn is not exist for : " + boardId + "/" + fileMask);
		            	response.setStatus(404);
		            	return;
		            }
		            pstmt.close();
		            */
		        } catch( Exception e ) {
		            throw e;
		        } finally {
		            if (rslt != null) try { rslt.close(); } catch (SQLException e) {}
		            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
		            if (connCtxt != null) connCtxt.release();
		        }

					File file = null;
						
					// 다운로드시 첨부파일뿐 아니라 에디터에서 추가한 그림파일도 다운 가능하도록 한다.
					String extendname = fileMask.substring(fileMask.lastIndexOf(extnSep)+1);
					if ( extendname.equalsIgnoreCase("gif")  || extendname.equalsIgnoreCase("jpg") || extendname.equalsIgnoreCase("jpeg") || extendname.equalsIgnoreCase("png") || extendname.equalsIgnoreCase("bmp"))				  {
						// 쎔네일인 경우 원본이 아닌 쎔네일 다운로드 
						if( isThumbnail) {
							//file = new File(updir_thumb + sep + boardId + sep + thumbFileMask);
							file = getFile(updir_thumb,boardId,thumbFileMask);
						} else if( isPoll) {
							//file = getFile(updir_poll,boardId,fileMask);
							file = new File(updir_poll + boardId + File.separator + fileMask);
						} else {
							//file = new File(updir_editor + sep + boardId + sep + fileMask);
							file = getFile(updir_editor,boardId,fileMask);
						}
					} else {
						logger.error( "invalid extension : " + boardId + "/" + fileMask);
						response.setStatus(403);
						return;
					}

					if (file != null && file.exists() && file.isFile() && !file.isDirectory() && !file.isHidden()) {
						logger.info("file=["+file.getAbsolutePath()+"]::["+file.getAbsolutePath()+"]");
						// OK
					} else {
						if( file != null) {
							logger.error( "file not exist : " + file.getAbsolutePath());
						}
						response.setStatus(404);
						return;
					}
					sendFile( request, response, file);
		} catch( MustLoginException mle ) {
//			errorPrint( response, mrBun.getMessage("eb.error.need.login")); // '로그인하셔야 합니다.'
			response.setStatus(401);
		} catch( BaseException be ) {
//			if( be.getMsgArgs().length > 0 ) {
//				errorPrint( response, mrBun.getMessage( be.getMessage(), be.getMsgArgs()));
//			} else {
//				errorPrint( response, mrBun.getMessage( be.getMessage()));
//			}
			response.setStatus(403);
        } catch( Exception e ) {
			response.setStatus(500);
		    logger.error( e.getMessage(), e);;
//			errorPrint( response, mrBun.getMessage("mm.error.system.failure, \n["+e.getMessage()+"]")); // '시스템 에러가 발생하였습니다'
		    
		}
	}
	
	public void sendFile( HttpServletRequest request, HttpServletResponse response, File file) throws Exception{
		String mime = request.getSession().getServletContext().getMimeType(file.toString());
		if (mime == null) mime = "application/octet-stream";
		response.setContentType(mime);
		response.setContentLength((int)file.length());

//		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Content-Transfer-Encoding", "binary;");

		BufferedInputStream bin = null;
		BufferedOutputStream bos = null;
		try {
			bin = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());
        		
			byte[] buf = new byte[20480]; //buffer size 2K.
			int read = 0;
			while ((read = bin.read(buf)) != -1) 
				bos.write(buf,0,read);
		} catch (Exception sube) {
			throw sube;
		} finally {
			if (bos != null) bos.close();
			if (bin != null) bin.close();
		}  
		
	}
	
	
	public void doService( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		try {
		    String boardId   = request.getParameter("boardId");
		    String bltnNo    = request.getParameter("bltnNo");
		    String fileSeq   = request.getParameter("fileSeq");
		    String subId     = request.getParameter("subId");		    
		    String fileMask  = request.getParameter("fileMask");
			String userAgent = request.getHeader("user-agent");
			
			int idx = 0;
		    //String testEncode   = request.getParameter("e");
			//String testFileMask = request.getParameter("fm");
			//String testFileName = request.getParameter("fn");
			//String testSubId    = request.getParameter("s");
		    
		    //if ((Util.validateAllowChar(boardId) && Util.validateNumber(bltnNo) && Util.validateNumber(fileSeq) && "sub06".equals(subId)) || 
		    //    ("200601011111111111111_jpg".equals(testFileMask) && "sub01".equals(testSubId) && ("1".equals(testEncode) || "2".equals(testEncode) || "3".equals(testEncode)))) { 
			if( ValidateUtil.validateAllowChar(boardId) && ValidateUtil.validateNumber(bltnNo) 
//					&& ValidateUtil.validateNumber(fileSeq) 
			 && ("sub06".equals(subId)||"sub16".equals(subId)||"sub26".equals(subId)||"sub36".equals(subId))) { 

		        ConnectionContext connCtxt  = null;
		        Connection        conn      = null;
		        PreparedStatement pstmt     = null;
		        ResultSet         rslt      = null;
		        BoardVO           boardVO   = null;
		        SecPmsnVO         secPmsnVO = null;
		        
//		        String fileMask = null;
		        String fileName = null;
		        // 게시물 등록자
	            String bltnUserId = "";
		        
				mrBun = EnviewMultiResourceManager.getInstance().getBundle( SecurityMngr.getLocale( request ));

				//if (testFileMask == null && bltnNo != null) {
			    if (bltnNo != null) {
			        try { 
						connCtxt = new ConnectionContextForRdbms(true);
			        	
			            boardVO   = cacheMngr.getBoard (boardId, SecurityMngr.getLocale(request));
						secPmsnVO = SecurityMngr.getInst().getCurrentSecPmsn (boardVO, request);			
						SecurityMngr.getInst().boardProtect (boardVO, "READ", SecurityMngr.getLoginInfo(request), secPmsnVO, request.getRemoteAddr(), null, null, null, request);

			            conn = connCtxt.getConnection();
			            if( fileMask==null) {
				            pstmt = conn.prepareStatement("SELECT file_nm, file_mask FROM "+boardVO.getFileTbl()+" WHERE board_id = ? AND bltn_no = ? AND file_seq = ? ORDER BY file_seq");
				            pstmt.setString (1, boardId);
				            pstmt.setString (2, bltnNo);
				            pstmt.setInt    (3, Integer.parseInt(fileSeq));
				            rslt = pstmt.executeQuery();
				            if (rslt.next()) {
				                 fileName = rslt.getString(1);
				                 fileMask = rslt.getString(2);
				            }
				            rslt.close();
				            pstmt.close();
			            } else {
				            pstmt = conn.prepareStatement("SELECT bltnNo, fileSeq, file_nm, file_mask FROM "+boardVO.getFileTbl()+" WHERE board_id = ? AND file_mask=?");
				            pstmt.setString (1, boardId);
				            pstmt.setString (2, fileMask);
				            rslt = pstmt.executeQuery();
				            if (rslt.next()) {
				            	 bltnNo = rslt.getString(1);
				            	 fileSeq = rslt.getString(2);
				                 fileName = rslt.getString(3);
				                 fileMask = rslt.getString(4);
				            }
				            rslt.close();
				            pstmt.close();
			            	
			            }
			            pstmt = conn.prepareStatement("SELECT user_id FROM " + boardVO.getBltnTbl() + " WHERE board_id = ? and bltn_no= ? ");
			            pstmt.setString(1, boardVO.getBoardRid());
			            pstmt.setString(2, bltnNo);
			            rslt = pstmt.executeQuery();
			            if( rslt.next()) {
			            	bltnUserId = rslt.getString(1);
			            }
			            rslt.close();
			            pstmt.close();
			            
			            // 첨부파일 다운로드 횟수에 제한이 걸려 있고,
			            // 이 게시판의 운영자이상의 권한이 있는 경우가 아니고,
			            // 현재 사용자(로그인되어 있지 않은 경우에는 동일노드에서)가 작성한 게시물이 아니면,
			            // 첨부파일 다운로드 감사기록을 뒤져서 다운로드 건수가 제한회수 이상이면 메시지처리.2009.04.27.KWShin.
			            // 자기가 등록한 게시물인 경우 다운로드 제한 체크 안함 
			            if( boardVO.getMaxFileDown() > 0 && !(secPmsnVO.getIsAdmin()) && ! ( secPmsnVO.getIsLogin() &&  bltnUserId.equals(secPmsnVO.getLoginId()))) {
				            StringBuffer sb = new StringBuffer();
				            sb.append("SELECT count(t1.board_id) FROM "+boardVO.getFileTbl()+" t1 ");
				            sb.append( " JOIN action_hist t2 ON t2.board_id = t1.board_id AND t2.bltn_no = t1.bltn_no AND t2.file_mask = t1.file_mask ");
				            sb.append( " JOIN " + boardVO.getBltnTbl() + " t3 ON t3.board_id = t1.board_id AND t3.bltn_no = t1.bltn_no");
				            sb.append(" WHERE t1.board_id = ? ");
				            sb.append(  " AND t1.bltn_no = ? ");
				            if( !secPmsnVO.getIsLogin()) sb.append(" AND t3.user_id is null AND t3.user_ip != ? ");
				            else sb.append(" AND t3.user_id != ? ");
				            sb.append(  " AND t2.action_cd='92'");
				            sb.append(  " AND t2.file_mask=? ");
				            if( !secPmsnVO.getIsLogin()) sb.append(" AND t2.user_id is null AND t2.user_ip=? ");
				            else sb.append(" AND t2.user_id=? ");
				            
				            pstmt = conn.prepareStatement(sb.toString());
				            
				            idx=0;
				            pstmt.setString(++idx, boardId);
				            pstmt.setString(++idx, bltnNo);
				            if( !secPmsnVO.getIsLogin()) {
				            	pstmt.setString(++idx, request.getRemoteAddr());	
				            } else {
				            	pstmt.setString(++idx, secPmsnVO.getLoginId());	
				            }
				            pstmt.setString(++idx, fileMask);
				            if( !secPmsnVO.getIsLogin()) {
				            	pstmt.setString(++idx, request.getRemoteAddr());
				            } else {
				            	pstmt.setString(++idx, secPmsnVO.getLoginId());
				            }	
				            
				            rslt = pstmt.executeQuery();
				            if (rslt.next()) {
				            	// 첨부파일 다운로드 기록이 제한횟수보다 크면 exception을 던져서, 아랫부분에서 에러메시지를 출력하도록 한다.2009.04.27.KWShin.
				            	if (rslt.getInt(1) >= boardVO.getMaxFileDown()) throw new BaseException("eb.error.atch.limit.maxdown"); // '첨부파일 다운로드 제한 횟수만큼 이미 다운로드하셨습니다.'
				            }
				            rslt.close();
				            pstmt.close();
			            }
			        } catch( Exception e ) {
			            throw e;
			        } finally {
			            if (rslt != null) try { rslt.close(); } catch (SQLException e) {}
			            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			            if (connCtxt != null) connCtxt.release();
			        }
		        //} else {
		        //    fileMask = testFileMask;
		        //    fileName = testFileName;
		        } 
			    logger.info("** fileMask=" + fileMask);
			    String extnSep = Enview.getConfiguration().getString(Constants.PROP_EXTN_SEPERATOR);
		        String checkFile = "";
		        if( fileMask==null) {
					errorPrint( response, mrBun.getMessage("eb.error.not.exist.file"));
					return;
		        }
		        if (fileMask.indexOf(extnSep) > -1) 
		        	checkFile = fileMask.substring(0, fileMask.indexOf(extnSep));

				if ((fileMask == null || fileMask.trim().length() == 0 || fileMask.indexOf("/") > -1 
//				|| fileMask.indexOf(extnSep) == -1 
				  || fileMask.indexOf("..") > -1 || fileMask.indexOf("\\") > -1)
//				  || (checkFile.length() != 21 && checkFile.length() != 22)
//				  || (fileMask.indexOf("20") != 0 && fileMask.indexOf("T20") != 0)
				  
						) {
					errorPrint( response, mrBun.getMessage("eb.error.atch.abnormal.file")); // '정상적인 파일이 아닙니다.'							
				} else {
					
					// 게시판별로 별도의 디렉터리에 저장되어 있다. 2009.02.23.KWShin.
					//File file = new File(updir_attach + sep + boardId + sep + fileMask);
					File file = getFile(updir_attach,boardId,fileMask);
						
					// 다운로드시 첨부파일뿐 아니라 에디터에서 추가한 그림파일도 다운 가능하도록 한다.
					String extendname = fileMask.substring(fileMask.lastIndexOf(extnSep)+1);
					if (file == null || !file.exists()
					 && (extendname.equalsIgnoreCase("gif")  || extendname.equalsIgnoreCase("jpg") || extendname.equalsIgnoreCase("jpeg") || extendname.equalsIgnoreCase("png") || extendname.equalsIgnoreCase("bmp"))
					 &&	("sub06".equals(subId)||"sub16".equals(subId)||"sub26".equals(subId)||"sub36".equals(subId))) {
						// 게시판별로 별도의 디렉터리에 저장되어 있다. 2009.02.23.KWShin.
						//file = new File(updir_editor + sep + boardId + sep + fileMask);
						file = getFile(updir_editor,boardId,fileMask);
					}

					if (file != null && file.exists() && file.isFile() && !file.isDirectory() && !file.isHidden()) {
						logger.info("file=["+file.getAbsolutePath()+"]::["+file.getAbsolutePath()+"]");
						
						/*
						 * 정확한 이유가 뭘까?
						 * 체과연에서 JEUS에 설치되었을 때는 다음의 encoding 관련 로직을 막지 않으면
						 * 파일 다운로드시 파일이름이 깨졌다.
						 * 하지만, Tomcat에서는 다음의 로직을 풀어야 파일이름이 깨지지 않는다.
						 * 2009.02.22.KWShin.
						 * 묘하네.. 대전교육정보원은 JEUS5.0인데 다음의 로직을 풀어야 파일이름이 깨지지 않는다.
						 * 2010.02.08.KWShin.
						 */
						/*
						 * 2014.02.20 전부 다 막음  
						//if (testEncode == null) {
//						
//							if ("1".equals(encode)) // upload.jsp가 EUC-KR을 사용하므로...
//								fileName = new String(fileName.getBytes("EUC-KR"),"8859_1");
//							else if ("2".equals(encode))
//								fileName = new String(fileName.getBytes("8859_1"),"KSC5601"); 
//							else if ("3".equals(encode))
//								fileName = new String(fileName.getBytes("KSC5601"),"8859_1");					
//						
						//} else {
						//	if ("2".equals(testEncode))
						//		fileName = new String(fileName.getBytes("8859_1"),"KSC5601"); 
						//	else if ("3".equals(testEncode))
						//		fileName = new String(fileName.getBytes("KSC5601"),"8859_1"); 
						//}
						 */

						// 파일명 인코딩변경 
						String charSet = request.getCharacterEncoding();
						if( "4".equals(encode)) {
							charSet = "KSC5601";
						}
						
						String mime = request.getSession().getServletContext().getMimeType(file.toString());
						if (mime == null) mime = "application/octet-stream";
					    response.setContentType(mime + "; charset=" + charSet);
						response.setContentLength((int)file.length());
			
						logger.debug("** userAgent=" + userAgent);
						logger.debug("** fileName=" + fileName);
						String encodedFileName;
						/*
						 * 20161223 IE11의 경우 MSIE를 사용하지 않는다.- 권은총
						 */
				        if (userAgent.indexOf("MSIE") > -1 || userAgent.contains("Trident")) {
				            // IE 5.5 일 경우
				            if (userAgent.indexOf("MSIE 5.5") > -1) {
				            } else {// 그밖에
				                if ( charSet.equalsIgnoreCase("UTF-8") ) {
				                	fileName = URLEncoder.encode(fileName,charSet);
				                	fileName = fileName.replaceAll("\\+", "%20");
				                } else {
				                	//fileNm = "\"" + new String(fileNm.getBytes(userCharset), "ISO-8859-1") + "\"";
				                	fileName = "\"" + new String(fileName.getBytes(charSet), "UTF-8") + "\"";
				                }
				            }
				        } else {
				            // IE 를 제외한 브라우저
				        	//fileNm = "\"" + new String(fileNm.getBytes(userCharset), "ISO-8859-1") + "\"";
				        	fileName = "\"" + new String(fileName.getBytes(charSet), "UTF-8") + "\"";
				        }
						logger.debug("** fileName=" + fileName);
					    
						if (userAgent.indexOf("MSIE 5.5") != -1) {
							response.setHeader("Content-Type", "doesn/matter;");
							response.setHeader("Content-Disposition", "filename="+fileName+";");
						} else {
							if ("sub16".equals(subId) || "sub36".equals(subId)) {
								// 'Downopen'의 경우, 파일이 다운로드 되면서 바로 열리도록 한다.
								response.setHeader("Content-Type", "application/download;");
								response.setHeader("Content-Disposition", "inline; filename="+ fileName +";");
							} else {
								response.setHeader("Content-Type", "application/octet-stream;");
								response.setHeader("Content-Disposition", "attachment; filename="+fileName+";");
							}
						}
						
						response.setHeader("Content-Length", String.valueOf(file.length()));
						response.setHeader("Content-Transfer-Encoding", "binary;");
			
						BufferedInputStream bin = null;
						BufferedOutputStream bos = null;
						try {
							bin = new BufferedInputStream(new FileInputStream(file));
							bos = new BufferedOutputStream(response.getOutputStream());
			            		
							byte[] buf = new byte[2048]; //buffer size 2K.
							int read = 0;
							while ((read = bin.read(buf)) != -1) 
								bos.write(buf,0,read);
			            		
							//if (testFileMask == null) {
								connCtxt = new ConnectionContextForRdbms(true);

								String dbType = Enview.getConfiguration().getString("enview.db.type");
								StringBuffer sb = new StringBuffer();
								
								// 여기까지 도달했으면 여기서 첨부파일 다운로드 감사기록을 남기자.2009.04.27.KWShin.
								if( secPmsnVO.getIsLogin() && Enview.getConfiguration().getBoolean("board.action.92.history")) {
										Map logInfo = new HashMap();
										logInfo.put("actionCd", "92"); // 첨부파일다운로드
										logInfo.put("userIp",   request.getRemoteAddr());
										logInfo.put("userId", secPmsnVO.getLoginId());
										logInfo.put("boardId", boardId);
										logInfo.put("bltnNo",   bltnNo);
										logInfo.put("domainId", String.valueOf(boardVO.getDomainId()));
										logInfo.put("fileMask", fileMask);
										logInfo.put("loginInfo", secPmsnVO.getLoginInfo());
										logInfo.put("connCtxt", connCtxt);

										BoardDAO boardDAO = (BoardDAO)DAOFactory.getInst().getDAO( BoardDAO.DAO_NAME_PREFIX );
										boardDAO.setActionHist( logInfo );
								}
								
					            if( ! ( secPmsnVO.getIsLogin() &&  bltnUserId.equals(secPmsnVO.getLoginId()))) { 
									// 'DownloadCnt'나 'DownopenCnt'가 불리우는 경우 조회수를 증가시킨다.
									if ("sub26".equals(subId) || "sub36".equals(subId)) {
										sb.setLength(0);
										if ("mssql".equals (dbType)) {
											sb.append("UPDATE "+boardVO.getBltnTbl()+" SET bltn_read_cnt = ISNULL(bltn_read_cnt, 0) + 1"
											         +" WHERE board_id=? AND bltn_no=? ");
										} else if ("db2".equals (dbType)) {
											sb.append("UPDATE "+boardVO.getBltnTbl()+" SET bltn_read_cnt = VALUE(bltn_read_cnt, 0) + 1"
													+" WHERE board_id=? AND bltn_no=? ");
										} else {
											sb.append("UPDATE "+boardVO.getBltnTbl()+" SET bltn_read_cnt = NVL(bltn_read_cnt, 0) + 1"
													+" WHERE board_id=? AND bltn_no=? ");
										}
										pstmt = connCtxt.getConnection().prepareStatement(sb.toString());
										
										idx = 0;
										pstmt.setString(++idx, boardId);
										pstmt.setString(++idx, bltnNo);
										
										pstmt.executeUpdate();
										pstmt.close();
									}
									// 해당 파일의 다운로드 횟수를 증가시킨다.2010.01.14.KWShin.
									sb.setLength(0);
									if ("mssql".equals (dbType)) {
										sb.append("UPDATE "+boardVO.getFileTbl()+" SET down_cnt = ISNULL(down_cnt, 0) + 1"
										         +" WHERE board_id=? AND bltn_no=? AND file_seq=? ");
									} else if ("db2".equals (dbType)) {
										sb.append("UPDATE "+boardVO.getFileTbl()+" SET down_cnt = VALUE(down_cnt, 0) + 1"
												+" WHERE board_id=? AND bltn_no=? AND file_seq=? ");
									} else {
										sb.append("UPDATE "+boardVO.getFileTbl()+" SET down_cnt = NVL(down_cnt, 0) + 1"
												+" WHERE board_id=? AND bltn_no=? AND file_seq=? ");
									}
									
									pstmt = connCtxt.getConnection().prepareStatement(sb.toString());
									idx = 0;
									pstmt.setString(++idx, boardId);
									pstmt.setString(++idx, bltnNo);
									pstmt.setString(++idx, fileSeq);
									
									pstmt.executeUpdate();
									pstmt.close();
									
									connCtxt.commit();
					            }
							//}
						} catch (Exception sube) {
				            if (connCtxt != null) connCtxt.rollback();
							throw sube;
						} finally {
							if (bos != null) bos.close();
							if (bin != null) bin.close();
				            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
				            if (connCtxt != null) connCtxt.release();
						}  
					} else {
						errorPrint( response, mrBun.getMessage("eb.error.not.exist.file"));
					}
				}
			} else
		        errorPrint( response, mrBun.getMessage("eb.error.parameter.invalid")); // '부적절한 파라미터 값입니다<br>'
		} catch( MustLoginException mle ) {
			errorPrint( response, mrBun.getMessage("eb.error.need.login")); // '로그인하셔야 합니다.'
		} catch( BaseException be ) {
			if( be.getMsgArgs().length > 0 ) {
				errorPrint( response, mrBun.getMessage( be.getMessage(), be.getMsgArgs()));
			} else {
				errorPrint( response, mrBun.getMessage( be.getMessage()));
			}
        } catch( Exception e ) {
		    logger.error( e.getMessage(), e);;
			errorPrint( response, mrBun.getMessage("mm.error.system.failure, \n["+e.getMessage()+"]")); // '시스템 에러가 발생하였습니다'
		}
	}
	
	public File getFile( String uploadPath, String boardId, String fileMask) {
		//String fullPath = uploadPath + File.separator + boardId + File.separator + fileMask;
		String fullPath = "";
		String subDir = Enview.getConfiguration().getString("board.upload.subdir","");
		if (!subDir.equals("")) {
			String fileSubPath = "";
			if (subDir.equalsIgnoreCase("YYYY")) {
				if ("A".equals(fileMask.substring(0,1)) || "T".equals(fileMask.substring(0,1))) {
					fileSubPath = fileMask.substring(1,5);
				} else {
					fileSubPath = fileMask.substring(0,4);
				}
			}
			fullPath = uploadPath + boardId + File.separator + fileSubPath + File.separator + fileMask;
		} else {
			fullPath = uploadPath + boardId + File.separator + fileMask;
		}
		
		logger.info(" fullPath >>>> " + fullPath);

		File file = new File( fullPath);
		if( file.exists()) {
			return file;
		}
		
		return null;
	}
	
	protected void errorPrint(HttpServletResponse res, String msg) throws IOException {
		//res.setContentType("text/html; charset=EUC-KR");
		res.setContentType("text/html; charset=UTF-8");
 		PrintWriter out = res.getWriter();
 		
 		out.println("<script>");
 		out.println("alert('"+msg+"');");
 		out.println("</script>");		
	}
}
