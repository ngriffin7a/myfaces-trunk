/*
 * MyFaces - the free JSF implementation Copyright (C) 2003, 2004 The MyFaces
 * Team (http://myfaces.sourceforge.net)
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.custom.fileupload2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/13 10:42:04  manolito
 * introduced commons codecs and fileupload
 *
 */
public class MultipartRequestWrapper
		extends HttpServletRequestWrapper
{
	HttpServletRequest request = null;
	HashMap parametersMap = null;
	org.apache.commons.fileupload.FileUpload fileUpload = null;
	HashMap fileItems = null;
	
	public MultipartRequestWrapper(HttpServletRequest request){
		super( request );
		this.request = request;
	}
	
	private void parseRequest() {
		fileUpload = new org.apache.commons.fileupload.FileUpload();
		fileUpload.setSizeMax(10 * 1024 * 1024); // 10Mb
		// TODO : make this a configurable parameter.
		// The best would be to be able to specify it in the x:inputFileUpload tag.
		fileUpload.setFileItemFactory(new org.apache.commons.fileupload.DefaultFileItemFactory());
		
		List requestParameters = null;
		try{
			requestParameters = fileUpload.parseRequest(request);
		}catch(org.apache.commons.fileupload.FileUploadException fue){
			// TODO : Log !
			requestParameters = Collections.EMPTY_LIST;
		}
		
		parametersMap = new HashMap( requestParameters.size() );
		fileItems = new HashMap();

    	for (Iterator iter = requestParameters.iterator(); iter.hasNext(); ){ 
    		org.apache.commons.fileupload.FileItem fileItem = (org.apache.commons.fileupload.FileItem) iter.next();

    		if (fileItem.isFormField()) {
    			String name = fileItem.getFieldName();
    			String value = fileItem.getString();
    			
    			if( ! parametersMap.containsKey( name ) ){
    				String[] valuesArray = {value};
    				parametersMap.put(name, valuesArray);
    			}else{
    				String[] storedValues = (String[])parametersMap.get( name );
    				int lengthSrc = storedValues.length;
    				String[] valuesArray = new String[lengthSrc+1];
    				System.arraycopy(storedValues, 0, valuesArray, 0, lengthSrc);
    				valuesArray[lengthSrc] = value;
    				parametersMap.put(name, valuesArray);
    			}
    		} else { // fileItem is a File
   				if (fileItem.getName() != null) {
   					fileItems.put(fileItem.getFieldName(), fileItem);
   				}
    		}
    	}
	}
	
	public Enumeration getParameterNames() {
		if( parametersMap == null ) parseRequest();
		
		return Collections.enumeration( parametersMap.keySet() );
	}
	
	public String getParameter(String name) {
		if( parametersMap == null ) parseRequest();
		
		String[] values = (String[])parametersMap.get( name );
		if( values == null )
			return null;
		return values[0];
	}
	
	public String[] getParameterValues(String name) {
		if( parametersMap == null ) parseRequest();
		
		return (String[])parametersMap.get( name );
	}
	
	public Map getParameterMap() {
		if( parametersMap == null ) parseRequest();
		
		return parametersMap;
	}
	
	// Hook for the x:inputFileUpload tag.
	public org.apache.commons.fileupload.FileItem getFileItem(String fieldName) {
		if( fileItems == null ) parseRequest();
		
		return (org.apache.commons.fileupload.FileItem) fileItems.get( fieldName );
	}
}

