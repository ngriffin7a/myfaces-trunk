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
package net.sourceforge.myfaces.custom.fileupload;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.*;

import org.apache.commons.fileupload.*;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MultipartRequestWrapper
		extends HttpServletRequestWrapper
{
	HttpServletRequest request = null;
	HashMap parametersMap = null;
	FileUpload fileUpload = null;
	HashMap fileItems = null;
	int maxSize;

	public MultipartRequestWrapper(HttpServletRequest request, int maxSize){
		super( request );
		this.request = request;
        this.maxSize = maxSize;
	}
	
	private void parseRequest() {
		fileUpload = new FileUpload();
		fileUpload.setSizeMax(maxSize);
		fileUpload.setFileItemFactory(new DefaultFileItemFactory());
		
		List requestParameters = null;
		try{
			requestParameters = fileUpload.parseRequest(request);
        } catch (FileUploadBase.SizeLimitExceededException e) {
            // TODO: find a way to notify the user about the fact that the uploaded file exceeded size limit
            requestParameters = Collections.EMPTY_LIST;
		}catch(FileUploadException fue){
			// TODO : Log !
			requestParameters = Collections.EMPTY_LIST;
		}
		
		parametersMap = new HashMap( requestParameters.size() );
		fileItems = new HashMap();

    	for (Iterator iter = requestParameters.iterator(); iter.hasNext(); ){ 
    		FileItem fileItem = (FileItem) iter.next();

    		if (fileItem.isFormField()) {
    			String name = fileItem.getFieldName();
    			String value = fileItem.getString();
    			
    			addTextParameter(name, value);
    		} else { // fileItem is a File
   				if (fileItem.getName() != null) {
   					fileItems.put(fileItem.getFieldName(), fileItem);
   				}
    		}
    	}
    	
    	//Add the request parameters like http://www.myWebsite.com/clients/edit-jsf.jsf?unid=ff808081fbdce95b00fbdceabf370001
	}
	
	private void addTextParameter(String name, String value){
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
	public FileItem getFileItem(String fieldName) {
		if( fileItems == null ) parseRequest();
		
		return (FileItem) fileItems.get( fieldName );
	}
}
