/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.custom.fileupload;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/08/02 04:26:06  svieujot
 * Fix for bug #1001511 : setHeaderEncoding
 *
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
		fileUpload.setHeaderEncoding( request.getCharacterEncoding() );
		
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
