/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.custom.fileupload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
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
