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

import com.oreilly.servlet.MultipartWrapper;
import net.sourceforge.myfaces.component.ext.UIFileUpload;
import net.sourceforge.myfaces.component.ext.UploadedFile;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.attr.ext.FileUploadRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLInputAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

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
    implements CommonRendererAttributes,
    HTMLUniversalAttributes,
    HTMLEventHandlerAttributes,
    HTMLInputAttributes,
               FileUploadRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "FileUpload";

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

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIFileUpload.TYPE, TLD_EXT_URI, "file_upload", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIFileUpload.TYPE, TLD_EXT_URI, "file_upload", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIFileUpload.TYPE, TLD_EXT_URI, "file_upload", HTML_INPUT_ATTRIBUTES);
        addAttributeDescriptors(UIFileUpload.TYPE, TLD_EXT_URI, "file_upload", FILE_UPLOAD_ATTRIBUTES);
        addAttributeDescriptors(UIFileUpload.TYPE, TLD_EXT_URI, "file_upload", USER_ROLE_ATTRIBUTES);
    }



    public void decode(FacesContext facescontext, UIComponent uiComponent)
        throws IOException
    {
        if (!supportsComponentType(uiComponent))
        {
            throw new IllegalArgumentException("Only UIFileUpload type supported.");
        }

        //MultipartWrapper might have been wrapped again by one or more additional
        //Filters. We try to find the MultipartWrapper, but if a filter has wrapped
        //the ServletRequest with a class other than HttpServletRequestWrapper
        //this will fail.
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
                UploadedFile upFile = new UploadedFile(mpReq.getFilesystemName(paramName),
                                                       mpReq.getContentType(paramName),
                                                       file);
                uiComponent.setValue(upFile);
                uiComponent.setValid(true);
            }
        }
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
        UploadedFile value = (UploadedFile)uiComponent.currentValue(facesContext);
        if (value != null)
        {
            writer.write(" value=\"");
            writer.write(HTMLEncoder.encode(value.getFilePath(), false, false));
            writer.write("\"");
        }

        HTMLUtil.renderCssClass(writer, uiComponent,INPUT_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_INPUT_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_INPUT_FILE_UPLOAD_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(facesContext, uiComponent);

        writer.write(">");
    }

}
