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
package net.sourceforge.myfaces.renderkit.html.ext;

//import com.oreilly.servlet.MultipartWrapper;
import net.sourceforge.myfaces.component.ext.UIFileUpload;
import net.sourceforge.myfaces.renderkit.attr.TextRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ext.FileUploadRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FileUploadRenderer
        extends HTMLRenderer
        implements FileUploadRendererAttributes
{
    public static final String TYPE = "FileUpload";

    /*
    private static final Integer MAX_UPLOAD_SIZE_DEFAULT
        = new Integer(Integer.MAX_VALUE);
        */


    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIFileUpload.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIFileUpload;
    }


    public void decode(FacesContext facescontext, UIComponent uiComponent)
        throws IOException
    {
        if (!supportsComponentType(uiComponent))
        {
            throw new IllegalArgumentException("Only UIFileUpload type supported.");
        }

        /*
        We must resolve license issues for oreilly classes first!

        ServletRequest multipartRequest = facescontext.getServletRequest();
        while (multipartRequest != null &&
               !(multipartRequest instanceof MultipartWrapper))
        {
            if (multipartRequest instanceof HttpServletRequestWrapper)
            {
                multipartRequest = ((HttpServletRequestWrapper)multipartRequest).getRequest();
            }
            else
            {
                multipartRequest = null;
            }
        }

        if (multipartRequest != null)
        {
            MultipartWrapper mpReq = (MultipartWrapper)multipartRequest;

            String paramName = uiComponent.getClientId(facescontext);
            File file = mpReq.getFile(paramName);
            if (file != null)
            {
                uiComponent.setValue(file);
                ((UIFileUpload)uiComponent).setFilePath(mpReq.getFilesystemName(paramName));
                ((UIFileUpload)uiComponent).setContentType(mpReq.getContentType(paramName));
                uiComponent.setValid(true);
            }
        }
        */

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (!supportsComponentType(uiComponent))
        {
            throw new IllegalArgumentException("Only UIFileUpload type supported.");
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<input type=\"file\"");
        String clientId = uiComponent.getClientId(facesContext);
        writer.write(" name=\"");
        writer.write(clientId);
        writer.write("\"");
        writer.write(" id=\"");
        writer.write(clientId);
        writer.write("\"");
        String value = ((UIFileUpload)uiComponent).getFilePath();
        if (value != null)
        {
            writer.write(" value=\"");
            writer.write(HTMLEncoder.encode(value, false, false));
            writer.write("\"");
        }
        String css = (String)uiComponent.getAttribute(INPUT_CLASS_ATTR);
        if (css != null)
        {
            writer.write(" class=\"");
            writer.write(css);
            writer.write("\"");
        }
        CommonAttributes.renderHTMLEventHandlerAttributes(facesContext, uiComponent);
        CommonAttributes.renderUniversalHTMLAttributes(facesContext, uiComponent);
        CommonAttributes.renderAttributes(facesContext, uiComponent, TextRendererAttributes.COMMON_TEXT_ATTRIBUTES);
        CommonAttributes.renderAttributes(facesContext, uiComponent, FileUploadRendererAttributes.COMMON_FILE_UPLOAD_ATTRIBUTES);
        writer.write(">");
    }

}
