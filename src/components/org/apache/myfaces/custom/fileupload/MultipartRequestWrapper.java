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

import org.apache.commons.fileupload.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2004/09/09 13:43:59  manolito
 * query string parameters where missing in the parameter map
 *
 * Revision 1.5  2004/08/16 18:06:47  svieujot
 * Another bug fix for bug #1001511. Patch submitted by Takashi Okamoto.
 *
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

	    String charset = request.getCharacterEncoding();
		fileUpload.setHeaderEncoding(charset);


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

    			// The following code avoids commons-fileupload charset problem.
    			// After fixing commons-fileupload, this code should be
    			//
    			// 	String value = fileItem.getString();
    			//
    			String value = null;
    			if ( charset == null) {
    			    value = fileItem.getString();
    			} else {
    			    try {
    			        value = new String(fileItem.get(), charset);
    			    } catch (UnsupportedEncodingException e){
    			        value = fileItem.getString();
    			    }
    			}

    			addTextParameter(name, value);
    		} else { // fileItem is a File
   				if (fileItem.getName() != null) {
   					fileItems.put(fileItem.getFieldName(), fileItem);
   				}
    		}
    	}

    	//Add the query string paramters
        for (Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            String[] valuesArray = (String[])entry.getValue();
            for (int i = 0; i < valuesArray.length; i++)
            {
                addTextParameter((String)entry.getKey(), valuesArray[i]);
            }
        }
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
