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
package org.apache.myfaces.custom.fileupload;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.html.util.MultipartRequestWrapper;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.15  2004/12/01 16:32:03  svieujot
 * Convert the Multipart filter in an ExtensionsFilter that provides an additional facility to include resources in a page.
 * Tested only with javascript resources right now, but should work fine with images too.
 * Some work to do to include css resources.
 * The popup component has been converted to use this new Filter.
 *
 * Revision 1.14  2004/10/13 11:50:57  matze
 * renamed packages to org.apache
 *
 * Revision 1.13  2004/09/03 12:32:05  tinytoony
 * file upload
 *
 * Revision 1.12  2004/07/14 06:02:48  svieujot
 * FileUpload : split file based and memory based implementation.
 * Use the storage="memory|file" attribute.
 * Default is memory because file based implementation fails to serialize.
 *
 * Revision 1.11  2004/07/12 03:06:36  svieujot
 * Restore error handling due to UploadedFileDefaultImpl changes
 *
 * Revision 1.10  2004/07/01 21:53:05  mwessendorf
 * ASF switch
 *
 * Revision 1.9  2004/05/24 22:48:10  svieujot
 * Making UploadedFile an interface, and adjusting the renderer.
 *
 * Revision 1.8  2004/05/18 14:26:49  manolito
 * added UserRoleAware support
 *
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
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_FILE_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        writer.endElement(HTML.INPUT_ELEM);
    }

    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!UserRoleUtils.isEnabledOnUserRole(uiComponent))
        {
            return false;
        }
        else
        {
            if (uiComponent instanceof HtmlInputFileUpload)
            {
                return ((HtmlInputFileUpload)uiComponent).isDisabled();
            }
            else
            {
                return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
            }
        }
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
                try{
                    UploadedFile upFile;
                    String implementation = ((HtmlInputFileUpload) uiComponent).getStorage();
                    if( implementation == null || ("memory").equals( implementation ) )
                        upFile = new UploadedFileDefaultMemoryImpl( fileItem );
                    else
                        upFile = new UploadedFileDefaultFileImpl( fileItem );
                    ((HtmlInputFileUpload)uiComponent).setSubmittedValue(upFile);
                    ((HtmlInputFileUpload)uiComponent).setValid(true);
                }catch(IOException ioe){
                    log.error(ioe);
                }
            }
        }
    }

    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException
    {
        if(submittedValue instanceof UploadedFile)
        {
            UploadedFile file = (UploadedFile) submittedValue;

            if(file.getName()!=null && file.getName().length()>0)
            {
                return file;
            }
        }

        return null;
    }
}
