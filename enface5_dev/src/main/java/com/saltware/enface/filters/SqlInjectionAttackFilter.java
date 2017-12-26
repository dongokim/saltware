/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.saltware.enface.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SqlInjectionAttackFilter implements Filter
{
    public void init(FilterConfig config) throws ServletException
    {
    	//
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        if (!validateParameters(request)) 
        {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        chain.doFilter(request, response);
    }
    
    /**
    *
    * @param request
    * @return
    */
    private boolean validateParameters(ServletRequest request) {
    	Enumeration<String> params = request.getParameterNames();
    	String paramName;
    	while (params.hasMoreElements()) {
    		paramName = params.nextElement();
    		
    		if (isInvalid(request.getParameter(paramName))) {
    			return false;
    		}
    	}
    	return true;
    }

    private boolean isInvalid(String value)
    {
        return (value != null && (value.indexOf("'") != -1 || value.indexOf(";") != -1 || value.indexOf(":") != -1
                || value.indexOf("--") != -1 || value.indexOf("/\\*") != -1 || value.indexOf("\\*/") != -1));
    }

    public void destroy()
    {
    	//
    }
}
