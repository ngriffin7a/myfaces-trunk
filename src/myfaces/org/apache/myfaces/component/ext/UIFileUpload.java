/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.component.ext;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIInput;

import java.io.File;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIFileUpload
        extends UIInput
{
    public static final String CONTENT_TYPE_ATTR = "contentType";
    public static final String FILE_PATH_ATTR = "filePath";

    public UIFileUpload()
    {
        UIComponentUtils.setTransient(this, true);  //No state to be saved
        setValid(true);
    }

    public String getContentType()
    {
        return (String)getAttribute(CONTENT_TYPE_ATTR);
    }

    public void setContentType(String contentType)
    {
        setAttribute(CONTENT_TYPE_ATTR, contentType);
    }

    public String getFilePath()
    {
        return (String)getAttribute(FILE_PATH_ATTR);
    }

    public void setFilePath(String filePath)
    {
        setAttribute(FILE_PATH_ATTR, filePath);
    }

    public File getFile()
    {
        return (File)getValue();
    }
}
