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

import java.io.File;
import java.io.IOException;
import org.apache.commons.fileupload.FileItem;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UploadedFile
{
    private String _name = null;
    private String _contentType = null;
    protected byte[] _bytes =  null;
    
    public UploadedFile(){}
    
    public UploadedFile(FileItem fileItem) throws IOException {
    	int sizeInBytes = (int)fileItem.getSize();
    	_bytes = new byte[sizeInBytes];
    	fileItem.getInputStream().read( _bytes );
    	
    	if (_bytes.length != 0) {
    		_name = fileItem.getName();
    		_contentType = fileItem.getContentType();
    	}else{
    		_bytes = null;
    	}
    }


	/**
	 * @return Returns the _bytes.
	 */
	public byte[] getBytes() {
		return _bytes;
	}
	/**
	 * @param _bytes The _bytes to set.
	 */
	public void setBytes(byte[] _bytes) {
		this._bytes = _bytes;
	}
	/**
	 * @return Returns the _contentType.
	 */
	public String getContentType() {
		return _contentType;
	}
	/**
	 * @param type The _contentType to set.
	 */
	public void setContentType(String type) {
		_contentType = type;
	}
	/**
	 * @return Returns the _name.
	 */
	public String getName() {
		return _name;
	}
	/**
	 * @param _name The _name to set.
	 */
	public void setName(String _name) {
		this._name = _name;
	}
}
