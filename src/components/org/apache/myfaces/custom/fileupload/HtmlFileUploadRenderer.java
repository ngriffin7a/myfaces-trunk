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

import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/05/10 22:17:24  o_rossmueller
 * max file size configurable by filter init parameter 'maxFileSize'
 * removed default creation of file contents byte array
 *
 * Revision 1.6  2004/04/26 13:16:32  manolito
 * Log was missing
 *
 * Revision 1.4 Sylvain Vieujot
 * Don't change the file bean if no file is uploaded. 
 * 
 * Revision 1.3 Sylvain Vieujot
 * Fix a null pointer exception in encodeEnd if the file name is null.
 * 
 * Revision 1.2 Sylvain Vieujot
 * Upgraded to use commons fileUpload
 */
public class HtmlFileUploadRenderer
    extends Renderer
{
    private static final Log log = LogFactory.getLog(HtmlFileUploadRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        super.encodeEnd(facesContext, uiComponent); //check for NP

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, "file", null);
        String clientId = uiComponent.getClientId(facesContext);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        UploadedFile value = (UploadedFile)((HtmlInputFileUpload)uiComponent).getValue();
        if (value != null)
        {
        	if( value.getName() != null )
        	{
        		writer.writeAttribute(HTML.VALUE_ATTR, value.getName(), null);
        	}
        }
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_FILE_PASSTHROUGH_ATTRIBUTES);
        writer.endElement(HTML.INPUT_ELEM);
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        super.decode(facesContext, uiComponent); //check for NP

        //MultipartWrapper might have been wrapped again by one or more additional
        //Filters. We try to find the MultipartWrapper, but if a filter has wrapped
        //the ServletRequest with a class other than HttpServletRequestWrapper
        //this will fail.
        ServletRequest multipartRequest = (ServletRequest)facesContext.getExternalContext().getRequest();
        while (multipartRequest != null &&
               !(multipartRequest instanceof MultipartRequestWrapper))
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
        	MultipartRequestWrapper mpReq = (MultipartRequestWrapper)multipartRequest;

            String paramName = uiComponent.getClientId(facesContext);
            FileItem fileItem = mpReq.getFileItem(paramName);
            if (fileItem != null)
            {
            	if( fileItem.getName() != null )
            	{
            		if( fileItem.getName().length() > 0 )
	            	{
                     UploadedFile upFile = new UploadedFile( fileItem );
                     ((HtmlInputFileUpload)uiComponent).setSubmittedValue(upFile);
                     ((HtmlInputFileUpload)uiComponent).setValid(true);
	            	}
            	}
            }
        }
    }
}
