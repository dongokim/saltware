/**
 commons_fileupload-1.2.1 버전에 맞추어진 UploadMngr.
*/

package com.saltware.enface.enboard.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enboard.cache.CacheMngr;
import com.saltware.enboard.exception.BaseException;
import com.saltware.enboard.security.SecurityMngr;
import com.saltware.enboard.util.Constants;
import com.saltware.enboard.util.FormatUtil;
import com.saltware.enboard.util.ImageUtil;
import com.saltware.enboard.util.ValidateUtil;
import com.saltware.enboard.vo.BoardVO;
import com.saltware.enview.Enview;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.SeekableStream;
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import javax.imageio.ImageIO;
//import javax.media.jai.JAI;
//import javax.media.jai.RenderedOp;

public class FileUpload {
	
	private static Log logger = LogFactory.getLog(FileUpload.class);

	private String jspdir       = "/WEB-INF/apps/enboard/WEB-INF/view";
	private String forward      = "/board/common/upload.jsp";
	private String upload       = null;
	private String updir_attach = null;
	private String updir_editor = null;
	private String updir_thumb  = null;
	private String updir_poll   = null;
	private String sep = null;


	private static FileUpload singleton;
	private CacheMngr cacheMngr;
	private MultiResourceBundle mrBun;
	
	public static FileUpload getInst( HttpServletRequest request ) {
        try {
            synchronized( FileUpload.class ) {
                if( singleton == null )
                    singleton = new FileUpload( request );
            }
        } catch(Exception e) {
            logger.error( e.getMessage(), e);;
        }
        return singleton;
	}
	
	
	protected boolean isImageExt( String ext) {
		if (( ext.equalsIgnoreCase("gif")  || ext.equalsIgnoreCase("jpg")
			       || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("bmp")
			       || ext.equalsIgnoreCase("png"))) {
			return true;
		}
		return false;
	}
	
	public FileUpload( HttpServletRequest request ) {
		
		cacheMngr = (CacheMngr)Enview.getComponentManager().getComponent("com.saltware.enboard.cache.CacheMngr");
		
		// 환경파일(enboard.properties)에서 첨부파일 디렉터리를 읽어온다.
		// 환경설정이 없으면, CONTEXT_ROOT/upload 디렉터리를 default로 사용한다.
		// 2009.02.23.KWShin.
		sep	= System.getProperty("file.separator");

		upload = Constants.getUploadPath( request);
		upload =  upload  + sep;
		
		updir_attach = upload + "attach" + sep;
		updir_editor = upload + "editor" + sep;
		updir_thumb  = upload + "thumb"  + sep;
		updir_poll   = Constants.getPollUploadPath( request) + sep;
	}
	
	DecimalFormat sizeFomat = new DecimalFormat("#,##0.0");
	private String formatSize( long size) {
		
		if( size == -1) {
			return "Unknown";
		}
		
		double sz ;
		String unit;
		if( size > 1024 * 1024 * 1024) {
			sz = size * 1.0 / (1024 * 1024 * 1024);
			unit = "GB";
		} else if ( size > 1024 * 1024) {
			sz = size * 1.0 / (1024 * 1024);
			unit = "MB";
		} else if( size > 1024) {
			sz  = size * 1.0 / 1024;
			unit="KB";
		} else {
			sz = size;
			unit="B";
		}
		
		return sizeFomat.format( sz) + " " + unit;
	}

	public void doService( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
//logger.info("I'm FileUpload");

//byte buf[] = new byte[100000];
//StringBuffer sb = new StringBuffer();
//int red = 0;
//ServletInputStream sis = request.getInputStream();
//while((red = sis.readLine(buf, 0, 100000)) != -1) {
//	sb.append(new String(buf,0,red,"UTF-8"));
//}
//logger.info(sb.toString());

		long   filesize   = 0;
		String subDir	  = Enview.getConfiguration().getString("board.upload.subdir","");
		String filename   = null;
		String filemask   = null, filemask050 = null, filemask100 = null, filemask150 = null;
		String extendname = null;
		String movepath   = null;
		String boardId = null;
		String subId      = null;
		String mode       = null;
		String jalign     = null;
		String jvalign    = null;
		String fwidth     = null;
		String fheight    = null;
		String seq        = null;
		//String callback	  = null; 
		String callback_func = null;	//2014.08.12 smarteditor 인 경우 callback, callback_func를 반드시 넘겨야함.
		 
		long uploadsize = 0;
		
		// 개별카페홈 대문영역 편집기능에서 필요하여 추가.2012.07.18.KWShin.
		String cmntId     = null;
		boolean isCafeGate = ("gate".equals(request.getParameter("cafe"))) ? true : false;
		boolean isPoll = ("true".equals(request.getParameter("poll"))) ? true : false;
		
		
		StringBuffer sbJW = new StringBuffer();

		if (isCafeGate) { // 개별카페홈 대문영역 편집기에서 이미지/플래쉬 첨부기능에 의해 요청이 올라온 경우
			upload = Enview.getConfiguration().getString("cafe.each.resource.path");
			if (ValidateUtil.isEmpty (upload)) upload = request.getSession().getServletContext().getRealPath("/cola/cafe/each");
			upload = upload  + sep;
		}

		try {
			if( ServletFileUpload.isMultipartContent( request )) {
				
				mrBun = EnviewMultiResourceManager.getInstance().getBundle( SecurityMngr.getLocale( request ));

				DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
				// Repository: sets the directory used to temporarily store files that are larger than the configured size threshold.
				diskFileItemFactory.setRepository( new File( upload ));
				diskFileItemFactory.setSizeThreshold( 1024 * 100 );		//max memory size 100k
				ServletFileUpload servletFileUpload = new ServletFileUpload( diskFileItemFactory );
				
				final HttpSession session = request.getSession (true);
				
				// 업로드 파일 크기를 알수 없는 경우 완료를 알수 없어 진행 상태값이 안지워져 남아 있을 수 있으므로 업로드 진행 상태값을세션에서  지운다.
				session.removeAttribute ("uploadFileTotal");
				session.removeAttribute ("uploadFileCurrent");
				session.removeAttribute ("uploadFileTotalF");
				session.removeAttribute ("uploadFileCurrentF");
				   
				// 업로드 진행 상태 리스너를 만들어 등록한다.
				ProgressListener progressListener = new ProgressListener() {
				/**
				 * 진행상태를 갱신한다.	
				 */
					public void update (long current, long total, int pItems) {
						if( current == total) {
							//완료 되었으면 업로드 상태값을 지운다. 
							session.removeAttribute ("uploadFileTotal");
							session.removeAttribute ("uploadFileCurrent");
							session.removeAttribute ("uploadFileTotalF");
							session.removeAttribute ("uploadFileCurrentF");
						} else {
							//진행중이면 업로드 상태값을 갱신한다. 
							session.setAttribute ("uploadFileTotal",  total);
							session.setAttribute ("uploadFileCurrent", current);
							session.setAttribute ("uploadFileTotalF",  formatSize(total));
							session.setAttribute ("uploadFileCurrentF",  formatSize( current));
					   }
				   	}
				};
				servletFileUpload.setProgressListener (progressListener);

				BoardVO boardVO = null;
				boardId = request.getParameter("boardId");
				if (isCafeGate) { // 개별카페홈 대문영역 편집기에서 올라온 경우
					uploadsize = Long.parseLong (Enview.getConfiguration().getString("cafe.max.upload.size"));
				} else if (isPoll) { // 설ansdmf rptlvks todtjdrhk  
					uploadsize = Long.parseLong (Enview.getConfiguration().getString("poll.max.upload.size", "10000000"));
				} else { // 게시판에서 올라온 경우
					boardVO = cacheMngr.getBoard (boardId, SecurityMngr.getLocale (request));
					
					if ("each".equals( Enview.getConfiguration().getString(Constants.PROP_ATTACH_FILE_SIZE_OPTION))) {
						uploadsize = boardVO.getMaxFileSize();
					} else {
						uploadsize = boardVO.getMaxFileCnt() * boardVO.getMaxFileSize();
					}
				}

				List fileItemList = servletFileUpload.parseRequest( request );

				logger.debug("** servletFileUpload.parseRequest fileItemList >>>> " + fileItemList);

				for( int i = 0; i < fileItemList.size(); i++ ) {
					FileItem fileItem = (FileItem)fileItemList.get(i);
					if( fileItem.isFormField()) {
						if      ("subId"    .equals (fileItem.getFieldName())) subId     = fileItem.getString();
						else if ("mode"     .equals (fileItem.getFieldName())) mode      = fileItem.getString();
						else if ("jalign"   .equals (fileItem.getFieldName())) jalign    = fileItem.getString();
						else if ("jvalign"  .equals (fileItem.getFieldName())) jvalign   = fileItem.getString();
						else if ("fwidth"   .equals (fileItem.getFieldName())) fwidth    = fileItem.getString();
						else if ("fheight"  .equals (fileItem.getFieldName())) fheight   = fileItem.getString();
						else if ("seq"      .equals (fileItem.getFieldName())) seq       = fileItem.getString();
						else if ("cmntId"   .equals (fileItem.getFieldName())) cmntId    = fileItem.getString();
						//else if ("callback" .equals (fileItem.getFieldName())) callback    = fileItem.getString();	//2014.08.14 smarteditor 인 경우 callback, callback_func이 반드시 넘어옴.
						else if ("callback_func" .equals (fileItem.getFieldName())) callback_func    = fileItem.getString();	//2014.08.14 smarteditor 인 경우 callback, callback_func이 반드시 넘어옴.
						
						logger.debug("** param:" + fileItem.getFieldName() + "=" + fileItem.getString() );
					} else {
						logger.debug("** param:" + fileItem.getFieldName() + "=" + fileItem.getName()  + " ( " + fileItem.getSize() + " Bytes )");
					}
				}

				logger.debug("** FileUpload doservice subId:" + subId );
				
				// 'sub01': 파일첨부
				// 'sub02': 편집기에서 파일삽입, subJW: JWEditor 를 이용한 파일 삽입
				// 'sub11': 이미지첨부형 설문게시판에서 설문이미지 첨부
				if      ("sub01".equals (subId)) movepath = updir_attach + boardId + sep;
				else if ("sub02".equals (subId) || "subJW".equals (subId)) {
					if (isCafeGate)              movepath = upload + cmntId + sep;
					else			             movepath = updir_editor + boardId + sep;
				} 
				else if ("sub11".equals (subId)) movepath = updir_poll   + boardId + sep;
				else throw new BaseException("eb.error.parameter.invalid"); // '부적절한 파라미터 값입니다.'
				
				String extnSep = Enview.getConfiguration().getString(Constants.PROP_EXTN_SEPERATOR);
				
				for (int i=0; i<fileItemList.size(); i++) {
					FileItem fileItem = (FileItem)fileItemList.get(i);
					
					if (!fileItem.isFormField()) {
						filesize = fileItem.getSize();

						if( filesize == 0 ) {
							continue;
						}
						
							int idx = fileItem.getName().lastIndexOf("/");
							if (idx == -1)
								idx = fileItem.getName().lastIndexOf("\\");

							filename = fileItem.getName().substring(idx+1);
							
							//if (StringUtils.countMatches(filename, ".") != 1 || filename.indexOf(".") == -1 || 
							if( filename.indexOf(".") == -1 || filename.indexOf("/") > -1 || filename.indexOf("\\") > -1)
								throw new BaseException("eb.error.fileNm.invalid"); // '부적절한 파일명입니다.'
							
							if( filename.lastIndexOf(".") > -1 ) extendname = filename.substring(filename.lastIndexOf(".") + 1 );
							
							if (ValidateUtil.isEmpty (extendname) || extendname.indexOf(".") > -1)
								throw new BaseException("eb.error.extension.invalid"); // '부적절한 파일 확장자입니다.'
							
							// 모든 경우에 보조속성에 설정된 업로드 허용/금지 확장자를 거른다. 2009.02.28.KWShin.
							boolean uploadable = true;
							StringTokenizer stnz = null;
							
							if (!isCafeGate) {
								//logger.info("EXT_MASK=["+boardVO.getExtMask()+"]");
								//logger.info("BAD_EXT_MASK=["+boardVO.getBadExtMask()+"]");
								
								if( !ValidateUtil.isEmpty( boardVO.getExtMask() )) {
									stnz = new StringTokenizer( boardVO.getExtMask(), "," );
									boolean isPermited = false;
									while( stnz.hasMoreTokens()) {
										if( extendname.equalsIgnoreCase( stnz.nextToken())) {
											isPermited = true;
											break;
										}
									}
									if( !isPermited ) uploadable = false; 
								
								} else if( !ValidateUtil.isEmpty( boardVO.getBadExtMask() )) {
									stnz = new StringTokenizer( boardVO.getBadExtMask(), "," );
									boolean isProhibited = false;
									while( stnz.hasMoreTokens()) {
										if( extendname.equalsIgnoreCase( stnz.nextToken())) {
											isProhibited = true;
											break;
										}
									}
									if( isProhibited ) uploadable = false;
								} 
							}
							if( !uploadable ) throw new BaseException("eb.error.extension.prohibit");

							// 설문이미지의 경우에는 다음과 같은 확장자의 파일만 허용한다.090603.KWShin.
							if ( "sub11".equals(subId)) {
								if (( !extendname.equalsIgnoreCase("gif")  && !extendname.equalsIgnoreCase("jpg")
							       && !extendname.equalsIgnoreCase("jpeg") && !extendname.equalsIgnoreCase("bmp")
							       && !extendname.equalsIgnoreCase("png"))) {
									throw new BaseException("eb.error.extension.prohibit");
								}
							}

							// 편집기를 통해 올라온 파일 들 중에서 Thumbnail 이미지를 만들 수 있는지 여부 판단.
							boolean makeThumbnail = false;
							boolean makeAttachThumbnail = false;
							boolean cantDownload  = false;
							
							if ("sub02".equals (subId) || "subJW".equals (subId)) {
								//에디터 파일경우
								if( isImageExt(extendname)) {
									// 이미지 파일이면 썸네일 만듬
									if ( Enview.getConfiguration().getBoolean("board.thumbnail")) {
										makeThumbnail = true; // 썸네일 생성으로 환경이 설정되고, 정해진 확장자의 이미지 파일만.
										if (isCafeGate) makeThumbnail = false; // 개별카페홈 대문영역 이미지는 썸네일 필요없다.
									}
								} else {
									// 에디터파일인데 이미지가 아니면 삭제
									cantDownload = true;
								}
							} else if( "sub01".equals (subId) && Enview.getConfiguration().getBoolean("board.attach.thumbnail")) {
									makeAttachThumbnail = true; // 첨부파일 썸네일 생성
							}
							
							if( "thumbnailtest".equals(mode)) makeThumbnail = true;
							
							if( makeThumbnail )  {
								filemask = FormatUtil.getDateF("yyyyMMdd") + System.currentTimeMillis() + extnSep + extendname;
								filemask050 = "T050" + filemask;
								filemask100 = "T100" + filemask;
								filemask150 = "T150" + filemask;
								filemask    = "T" + filemask;
							} else if( makeAttachThumbnail) {
								filemask = FormatUtil.getDateF("yyyyMMdd") + System.currentTimeMillis() + extnSep + extendname;
								filemask050 = "A050" + filemask;
								filemask100 = "A100" + filemask;
								filemask150 = "A150" + filemask;
								filemask    = "A" + filemask;
							} else if (cantDownload) { 
								// 편집기에서 덧붙인 파일 중 정해진 확장자의 이미지 파일이 아니면 'X'를 덧붙여서 저장한다.
								// 이는 모든 파일의 내역을 첨부파일테이블에 유지해서,
								// 게시물 이동/복사/삭제시 기능을 편하게 구현하기 위함이다.
								// 안그러면, BULLETIN.BLTN_CNTT 내용을 뒤져서 파일이름을 찾아야 한다.
								filemask = "X" + FormatUtil.getDateF("yyyyMMdd") + System.currentTimeMillis() + extnSep + extendname;
							} else {
								filemask = FormatUtil.getDateF("yyyyMMdd") + System.currentTimeMillis() + extnSep + extendname;
							}
							String fileList = (String)session.getAttribute("deletableFile");
							session.setAttribute("deletableFile", (fileList == null ? "" : fileList + "," ) + filemask );

							String thumbPath = updir_thumb+boardId+sep;
							
							//설문이미지인 경우에는 subdir 체크 안함.
							if ( !"sub11".equals(subId)) {
								if (!subDir.equals("")) {
									String fileSubPath = "";
									if (subDir.equalsIgnoreCase("YYYY")) {
										fileSubPath = FormatUtil.getDateF("yyyy");
									}
									movepath += fileSubPath + sep;
									thumbPath += fileSubPath + sep;
								}	
							}
							
							( new File( movepath)).mkdirs();
							try {
								File file = new File( movepath + filemask );
								fileItem.write( file );
								
								if(((extendname.equalsIgnoreCase("gif") || extendname.equalsIgnoreCase("jpeg") || extendname.equalsIgnoreCase("bmp")  || extendname.equalsIgnoreCase("png"))
										&& !extendname.equalsIgnoreCase(getFileType(file)))
								  && (extendname.equalsIgnoreCase("jpg") && !"JPEG".equals(getFileType(file)))) {
								    
								    if( file.exists() && file.isFile() && !file.isDirectory() && !file.isHidden()) file.delete();
								    
									logger.info( extendname + " : " + getFileType( file ));
								    throw new BaseException("eb.error.file.format.invalid");
								}
								
							} catch( IOException e ) {
								logger.info("UploadMngr:: File '"+movepath+filemask+"' was not uploaded.");
								logger.error( e.getMessage(), e);;
							}
							
							// resize
							int imageMaxWidth = Enview.getConfiguration().getInt("board.image.maxWidth", -1);
							int imageMaxHeight = Enview.getConfiguration().getInt("board.image.maxHeight", -1);
							
							if( imageMaxWidth != -1 && imageMaxHeight != -1) {
								ImageUtil.resizeImage(movepath+filemask, imageMaxWidth, imageMaxHeight);
							}
							
							( new File( thumbPath)).mkdirs();
							Thumbnailer tn150 = new Thumbnailer (movepath+filemask, thumbPath+filemask150, 150, 150);
							
							// 업로드시에 에디터에서 저장한 그림파일만 썸네일을 생성한다 
							try {
								if (makeThumbnail || makeAttachThumbnail) {
									// 썸네일 이미지를 만들어야 할 상황이면 무조건 3종류의 썸네일 이미지를 만든다.
									//logger.info("BEGIN::150::"+System.currentTimeMillis());
									ImageUtil.makeThumbnail(movepath+filemask, thumbPath+filemask150, 150, 150);
									tn150.createThumbnail();
									//logger.info("BEGIN::100::"+System.currentTimeMillis());
									Thumbnailer tn100 = new Thumbnailer (movepath+filemask, thumbPath+filemask100, 100, 100);
									tn100.createThumbnail();
									//logger.info("BEGIN::50::"+System.currentTimeMillis());
									Thumbnailer tn50 = new Thumbnailer (movepath+filemask, thumbPath+filemask050, 50, 50);
									tn50.createThumbnail();
									//logger.info("END::50::"+System.currentTimeMillis());
								}
							} catch( Exception e ) {
								logger.info("UploadMngr : thumbnail fail!");
							}
						break;
					}
				}				
			}			
			sbJW.append (filemask); // 실제 파일명을 JWEditor에게 알려준다.
			
			logger.debug("movepath >>>> " + movepath + " filemask >>>> " + filemask);
			
		} catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException slee) {
		    logger.warn ("Warning at UploadMngr", slee);
			long maxSize = uploadsize / 1024 / 1024;
			if ("subJW".equals(subId)) sbJW.append ("ERROR::"+mrBun.getMessage("eb.info.upload.limit.size", ""+maxSize)+"::END******************************************************************************************************************************************************************************************************");
			else                       request.setAttribute( "message", mrBun.getMessage("eb.info.upload.limit.size", ""+maxSize )); // '파일사이즈를 초과하였습니다 \\n최대 사이즈는 {0}M 입니다'
		} catch (BaseException be) {
		    logger.warn( "Warning at UploadMngr", be );
			if (be.getMsgArgs().length > 0) {
				if ("subJW".equals(subId)) sbJW.append ("ERROR::"+mrBun.getMessage (be.getMessage(), be.getMsgArgs())+"::END**************************************************************************************************************************************************************************************************************");
				else                       request.setAttribute ("message", mrBun.getMessage(be.getMessage(), be.getMsgArgs()));
			} else {
				if ("subJW".equals(subId)) sbJW.append ("ERROR::"+mrBun.getMessage (be.getMessage())+"::END*******************************************************************************************************************************************************************************************************************************");
				else                       request.setAttribute ("message", mrBun.getMessage(be.getMessage()));
			}
		} catch (Exception e) {
			logger.error ("Error at UploadMngr", e);
			if ("subJW".equals(subId)) sbJW.append ("ERROR::"+mrBun.getMessage("eb.error.upload.atch.file")+"::END*************************************************************************************************************************************************************************************************************************");
			else                       request.setAttribute ("message", mrBun.getMessage("eb.error.upload.atch.file"));
		}
		
		logger.info("FileUpload is finished!!!");

		if ("subJW".equals(subId)) {
			response.setContentType ("text/json;charset=UTF-8");
			PrintWriter p = response.getWriter();
			p.print (sbJW.toString());
			p.flush (); // JEUS에서는 close()해서는 안돼고 flush()를 해주어야만 한다. 안 그러면 응답이 안나간다. 이런... 닝기리... 10시간 이상 까먹었다. 2008.12.14.KWShin
			p.close ();
		} else {
			request.setAttribute ("boardId", boardId);
			request.setAttribute ("mode",    mode);
			request.setAttribute ("jalign",  jalign);
			request.setAttribute ("jvalign", jvalign);				
			request.setAttribute ("fwidth",  fwidth);
			request.setAttribute ("fheight", fheight);
			request.setAttribute ("seq",     seq);
			request.setAttribute ("fileSize", String.valueOf(filesize));
			request.setAttribute ("fileName", filename);
			request.setAttribute ("fileSubDir", subDir);
			request.setAttribute ("fileMask", filemask);
			request.setAttribute ("callback_func", callback_func);	//2014.08.14 smarteditor인 경우 callback_func를 반드시 넣어줘야함.
			
			RequestDispatcher rd;
			rd = request.getSession().getServletContext().getRequestDispatcher(jspdir+forward);
			rd.forward(request, response);
		}
	}
	
	/*
	public static void createImage(String loadfile, String savefile, int width, int height, boolean border, String bdColor) throws ServletException, IOException 	{
		
		if(getDecoderCheck(loadfile)) {
			File 	    save = new File(savefile);
			RenderedOp    op = JAI.create("fileload", loadfile);
	     	BufferedImage im = op.getAsBufferedImage();
	     	
	     	if(im.getWidth()  < width) width = im.getWidth();
	     	if(im.getHeight() < height) height = im.getHeight();
			
			BufferedImage thumb = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D 	  g2d   = thumb.createGraphics();
						
			g2d.drawImage(im, 0, 0, width, height, null);
			
			if(border == true) {
				g2d.setColor(Color.decode(bdColor));
				g2d.drawRect(0, 0, --width, --height);
			}
			ImageIO.write(thumb, "png", save);
		}
	}
	*/
	public static boolean getDecoderCheck(String filename) {
		SeekableStream 	ss 	   = null;
		File 			file   = null;
		String[]		ext	   = null;
		String[]		images = {"gif","jpe","png","tiff","bmp"};
		boolean 		check  = false;
		
		try {
			file = new File(filename);
			ss   = new FileSeekableStream(file);
			ext  = ImageCodec.getDecoderNames(ss);
			
			for(int i=0; i<ext.length; i++) {
				for(int j=0; j<images.length; j++) {
					if (ext[i].indexOf(images[j]) > -1) {
						check = true;
						break;
					}
				}
			}	
		} catch (Exception e) {}
		
		return check;
	}
	
	/*********************************************************************************************************
	 * 파일의 헤더를 분석하여 파일의 확장자를 결정. 
	 *********************************************************************************************************/
	public String getFileType( File file ) { 
		InputStream inputStream = null;
		byte[] buf = new byte[132]; 
		try { 
			inputStream = new FileInputStream(file); 
			inputStream.read(buf, 0, 132); 
		} catch (IOException ioexception) {
			return "UNKNOWN"; 
		} finally {
			if (inputStream != null) try { inputStream.close(); } catch (Exception exception) {}
		}
		
		int b0 = buf[0] & 255; 
		int b1 = buf[1] & 255; 
		int b2 = buf[2] & 255; 
		int b3 = buf[3] & 255; 
		
		if (buf[128] == 68 && buf[129] == 73 && buf[130] == 67 && buf[131] == 77 
			&& ((b0 == 73 && b1 == 73) || (b0 == 77 && b1 == 77))) 
			return "TIFF_AND_DICOM"; 
		
		if (b0 == 73 && b1 == 73 && b2 == 42 && b3 == 0) 
			return "TIFF"; 
		
		if (b0 == 77 && b1 == 77 && b2 == 0 && b3 == 42) 
			return "TIFF"; 
		
		if (b0 == 255 && b1 == 216 && b2 == 255) 
			return "JPEG"; 
		
		if (b0 == 71 && b1 == 73 && b2 == 70 && b3 == 56) 
			return "GIF"; 
		
		if (buf[128] == 68 && buf[129] == 73 && buf[130] == 67 && buf[131] == 77) 
			return "DICOM"; 
		
		if (b0 == 8 && b1 == 0 && b3 == 0) 
			return "DICOM"; 
		
		if (b0 == 83 && b1 == 73 && b2 == 77 && b3 == 80) 
			return "FITS"; 
		
		if (b0 == 80 && (b1 == 50 || b1 == 53) && (b2 == 10 || b2 == 13 || b2 == 32 || b2 == 9)) 
			return "PGM"; 
		
		if ( b0 == 66 && b1 == 77)
			return "BMP"; 
		
		if (b0 == 73 && b1 == 111)
			return "ROI"; 
		
		if (b0 >= 32 && b0 <= 126 && b1 >= 32 && b1 <= 126 && b2 >= 32 && b2 <= 126 
			&& b3 >= 32 && b3 <= 126 && buf[8] >= 32 && buf[8] <= 126)
			return "TEXT"; 
		
		if (b0 == 137 && b1 == 80 && b2 == 78 && b3 == 71) 
			return "PNG"; 
		
		return "UNKNOWN"; 
	}
}


