package com.saltware.enface.enboard.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Vector; 
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enboard.util.Constants;
import com.saltware.enboard.util.ValidateUtil;
import com.saltware.enview.Enview;

public class FileDelete {
	private static Log logger = LogFactory.getLog(FileDelete.class);
	
	private String jspdir       = "/WEB-INF/apps/enboard/WEB-INF/view";
	private String forward      = "/board/common/delete.jsp";
	private String upload       = null;
	private String updir_attach = null;
	private String updir_editor = null;
	private String updir_poll   = null;
	private String sep          = null;

	private static FileDelete singleton;

	public static FileDelete getInst( HttpServletRequest request ) {
        synchronized( FileUpload.class ) {
            if( singleton == null )
                singleton = new FileDelete( request );
        }
        return singleton;
	}

	public FileDelete( HttpServletRequest request ){

		// 환경파일(enBoard.properties)에서 첨부파일 디렉터리를 읽어온다.
		// 환경설정이 없으면, CONTEXT_ROOT/upload 디렉터리를 default로 사용한다.
		// 2009.02.23.KWShin.
		sep	= System.getProperty("file.separator");
		upload = Constants.getUploadPath( request);		
		upload =  upload  + sep;
		updir_attach = upload + "attach" + sep;
		updir_editor = upload + "editor" + sep;
		updir_poll   = upload + "poll"   + sep;
	}

	public void doService( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		

		try {
			String extnSep = Enview.getConfiguration().getString(Constants.PROP_EXTN_SEPERATOR);
			String deletedList[] = null;
			String uploadpath    = upload;
			
			String vaccum     = request.getParameter("vaccum");
			String semaphore  = request.getParameter("semaphore");
			String delBoardId = request.getParameter("delBoardId");
			String unDelList  = request.getParameter("unDelList");
			String delList    = request.getParameter("delList");
			String subId      = request.getParameter("subId");
			String fileMask   = request.getParameter("fileMask");
			String seq        = request.getParameter("seq");

			if (     "sub01".equals(subId)) uploadpath = updir_attach + delBoardId + sep;
			else if ("sub02".equals(subId)) uploadpath = updir_editor + delBoardId + sep;
			else if ("sub11".equals(subId)) uploadpath = updir_poll   + delBoardId + sep;
			else return;

			HttpSession session = request.getSession(true);

			if (request.getHeader("referer") != null
			 &&	semaphore != null
			 && semaphore.equals((String)session.getAttribute( Constants.SESSION_SEMAPHORE ))) { // 보안체크. 편집화면 내려보낼때 내려보낸 토큰이 있어야만 한다.090603.KWShin.
			
				if ("sub11".equals(subId)) { // 설문 이미지 삭제.090603.KWShin.
					
					String onlyName = "";
					if (fileMask != null && fileMask.length() > 0)
						onlyName = fileMask.substring(0, fileMask.indexOf(extnSep));

					if ((onlyName.indexOf("20") == 0 || onlyName.indexOf("T20") == 0) 
					 && (onlyName.length() == 21 || onlyName.length() == 22) ) {
						
						File file = new File( uploadpath + fileMask );
						if (file.exists() && file.isFile() && !file.isDirectory() && !file.isHidden()) {
							file.delete();
							logger.info("DeleteMngr::File '" + uploadpath + fileMask + "' was deleted." );
						}
					}
				} else {
					Vector vector = new Vector();
					if ( unDelList !=null &&  unDelList.length() > 0) {
						if (unDelList != null && unDelList.indexOf("/") == -1 && unDelList.indexOf("..") == -1 && unDelList.indexOf("\\") == -1) {
							StringTokenizer tokenizer = new StringTokenizer(unDelList, "|");
		
							if (tokenizer.countTokens() > 0) {
								while (tokenizer.hasMoreTokens()) {
									String tmpfile = tokenizer.nextToken();
									vector.add(tmpfile);
		
									if (tmpfile != null && tmpfile.length() > 0) {
										tmpfile = tmpfile.substring(0, tmpfile.lastIndexOf("-"));
										
										String checkFile = "";
										if (tmpfile.indexOf(extnSep) > -1)
											checkFile = tmpfile.substring(0, tmpfile.indexOf(extnSep));
										
										if ((tmpfile.indexOf("20") == 0 || tmpfile.indexOf("T20") == 0) 
										 && (checkFile.length() == 21 || checkFile.length() == 22) ) {
										
											File file = new File( uploadpath + tmpfile );
			
											// 파일 삭제시 첨부파일뿐 아니라 에디터로 등록된 파일도 삭제한다
											String extendname = tmpfile.substring( tmpfile.lastIndexOf(extnSep) + 1 );
											if ( "sub01".equals(subId) && !file.exists()
											 && (extendname.equalsIgnoreCase("gif")  || extendname.equalsIgnoreCase("jpg")
											  || extendname.equalsIgnoreCase("jpeg") ||	extendname.equalsIgnoreCase("png"))) {
												uploadpath = updir_editor + delBoardId + sep;
												file = new File( uploadpath + tmpfile );
											}
											
											String fileList = (String)session.getAttribute("deletableFile");
											if (fileList != null) {				
												String nextFileList = "";
												StringTokenizer stz = new StringTokenizer(fileList, ",");
												while (stz.hasMoreTokens()) {
													String parsingFile = stz.nextToken();
													if (tmpfile.equals(parsingFile)) {											
														if (file.exists() && file.isFile() && !file.isHidden()) {
															file.delete();
															logger.info("DeleteMngr::File '" + uploadpath + tmpfile + "' was deleted." );
														}
													} else 
														nextFileList += parsingFile + ",";
												}
												session.setAttribute( "deletableFile", nextFileList );
											}
										} else continue;
									} else continue;
								} // while
							} else return; // tokenizer
						} else return; // unDelList
					} // unDelList.length
					
					if (delList != null && delList.length() > 0) {
						StringTokenizer tokenizer = new StringTokenizer(delList, "|");
						if (tokenizer.countTokens() > 0) {
							while (tokenizer.hasMoreTokens())
								vector.add(tokenizer.nextToken());
						}
					}
					if (vector.size() > 0) {
						deletedList = new String[vector.size()];
						for (int i = 0; i < vector.size(); i++)
							deletedList[i] = (String)vector.elementAt(i);
					}
				} // subId가 sub11이 아닌 경우.
			} // 보안체크

			request.setAttribute("deletedList", deletedList);
			request.setAttribute("delBoardId", delBoardId);
			request.setAttribute("vaccum", vaccum);
			request.setAttribute("subId", subId);
			request.setAttribute("seq", seq);

		} catch (Exception e) {
			logger.error("Error at DeleteMngr", e);
		}

		RequestDispatcher rd;
		rd = request.getSession().getServletContext().getRequestDispatcher(jspdir+forward);
		rd.forward(request, response);
	}
}

	
