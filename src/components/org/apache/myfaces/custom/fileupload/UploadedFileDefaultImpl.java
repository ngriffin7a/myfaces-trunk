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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.3  2004/07/12 03:05:54  svieujot
 *          Switch the FileItem for a byte[] because FileItem isn't serializable :
 *          java.io.NotSerializableException: org.apache.commons.fileupload.DeferredFileOutputStream
 *
 *          Revision 1.2  2004/07/01 21:53:05  mwessendorf
 *          ASF switch
 *
 *          Revision 1.1  2004/05/24 22:47:21  svieujot
 *          Initial UploadedFile moved to interface. So, UploadedFileDefaultImpl is now the default implementation class.
 *
 *          Revision 1.3  2004/05/10 22:17:24  o_rossmueller
 *          max file size configurable by filter init parameter 'maxFileSize'
 *          removed default creation of file contents byte array
 *
 */
public class UploadedFileDefaultImpl implements UploadedFile
{

    private String _name = null;
    private String _contentType = null;
    private byte[] bytes;


    public UploadedFileDefaultImpl()
    {
    }


    public UploadedFileDefaultImpl(FileItem fileItem) throws IOException
    {
        int sizeInBytes = (int)fileItem.getSize();
    	bytes = new byte[sizeInBytes];
    	fileItem.getInputStream().read(bytes);

    	if (bytes.length != 0) {
    		_name = fileItem.getName();
    		_contentType = fileItem.getContentType();
    	}
    }


    /**
     * Answer the uploaded file contents.
     *
     * @return file contents
     */
    public byte[] getBytes()
    {
        return bytes;
    }


    /**
     * Answer the uploaded file contents input stream
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException
    {
    	return new ByteArrayInputStream( bytes );
    }


    /**
     * @return Returns the _contentType.
     */
    public String getContentType()
    {
        return _contentType;
    }


    /**
     * @return Returns the _name.
     */
    public String getName()
    {
        return _name;
    }


    /**
     * Answer the size of this file.
     * @return
     */
    public long getSize() {
    	if( bytes == null )
    		return 0;
    	return bytes.length;
    }
}
