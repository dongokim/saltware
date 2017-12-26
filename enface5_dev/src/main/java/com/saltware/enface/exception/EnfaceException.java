package com.saltware.enface.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class EnfaceException implements HandlerExceptionResolver 
{
   private String view = null;
   public void setView(String view) 
   {
     this.view = view;
   }
   
   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception) 
   {
     request.setAttribute("exception",exception); 
     return new ModelAndView(view);
   }


}