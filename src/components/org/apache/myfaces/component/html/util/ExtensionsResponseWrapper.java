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
package org.apache.myfaces.component.html.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.xml.sax.InputSource;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$ $Log$
 * @version $Revision: 1.1 $ $Date: 2004/12/01 16:32:03 $ Revision 1.1  2004/12/01 20:25:10  svieujot
 * @version $Revision: 1.1 $ $Date: 2004/12/01 16:32:03 $ Make the Extensions filter support css and image resources.
 * @version $Revision: 1.1 $ $Date: 2004/12/01 16:32:03 $ Convert the popup calendar to use this new filter.
 * @version $Revision: 1.1 $ $Date: 2004/12/01 16:32:03 $
 * 
 */
public class ExtensionsResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream output;

    public ExtensionsResponseWrapper(HttpServletResponse response){
        super( response );
        output = new ByteArrayOutputStream();
    }
    
    public String toString(){
        return output.toString();
    	//return new String( output.toByteArray() );
    	/*
    	try{
    		return output.toString( "UTF-8" ); // convertion
    	}catch(UnsupportedEncodingException uee){
    		uee.printStackTrace();
    		return output.toString();
    	} */      	
    }
    
    /** This method is used by Tomcat.
     */
    public PrintWriter getWriter(){
        return new PrintWriter( output );
    }
    
	/** This method is used by Jetty.
	*/
	public ServletOutputStream getOutputStream(){
		return new MyServletOutputStream( output );
	}
    
    public InputSource getInputSource(){
		ByteArrayInputStream bais = new ByteArrayInputStream( output.toByteArray() );
		return new InputSource( bais );
    }

     /**
     *  Prevent content-length being set as the page might be modified.
     */
    public void setContentLength(int contentLength) {
        // noop
    }
    
    public void flushBuffer() throws IOException{
    	output.flush();
    }
    
    /** Used in the <code>getOutputStream()</code> method.
     */ 
    private class MyServletOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream outputStream;
		
		public MyServletOutputStream(ByteArrayOutputStream outputStream){
			this.outputStream = outputStream;
		}
		
		public void write(int b){
		    outputStream.write( b );
		}
    }
}