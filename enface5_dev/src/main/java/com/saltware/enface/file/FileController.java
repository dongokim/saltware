package com.saltware.enface.file;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class FileController extends MultiActionController
{
	private FileService fileService;
	
	public void setFileService(FileService fileService)
	{
		this.fileService = fileService;
	}
	
	public ModelAndView fileEdit(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mv = new ModelAndView("/file/edit");
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!");
		
		return mv;
	}
	 
	public void fileSave(HttpServletRequest request, HttpServletResponse response)
	{
		try{
			FileVO paramVO = new FileVO();
			
			paramVO.setFileNo(Long.toString(System.currentTimeMillis()));
			paramVO.setFileName(request.getParameter("fileName").split("\\|")[0]);
			paramVO.setFileMask(request.getParameter("fileMask").split("\\|")[0]);
			paramVO.setFileLoc("/upload/file/");
			
			if(this.fileService.save(paramVO) > 0)
			{
				response.sendRedirect("/user/fileList.face");
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	public ModelAndView fileList(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mv = new ModelAndView("/file/list");
		
		try{
			mv.addObject("list", this.fileService.list());
			return mv;
		}
		catch(Exception e)
		{
			
		}
		return mv;
	}
}
