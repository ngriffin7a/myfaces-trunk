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

import java.io.IOException;
import java.io.InputStream;



/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.5  2004/07/01 21:53:05  mwessendorf
 *          ASF switch
 *
 *          Revision 1.4  2004/05/24 22:48:10  svieujot
 *          Making UploadedFile an interface, and adjusting the renderer.
 *
 *          Revision 1.3  2004/05/10 22:17:24  o_rossmueller
 *          max file size configurable by filter init parameter 'maxFileSize'
 *          removed default creation of file contents byte array
 *
 */
public interface UploadedFile
{


    /**
     * Answer the uploaded file contents.
     *
     * @return file contents
     */
    public byte[] getBytes() throws IOException;


    /**
     * Answer the uploaded file contents input stream
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException;


    /**
     * @return Returns the _contentType.
     */
    public String getContentType();



    /**
     * @return Returns the _name.
     */
    public String getName();


    /**
     * Answer the size of this file.
     * @return
     */
    public long getSize();
}
