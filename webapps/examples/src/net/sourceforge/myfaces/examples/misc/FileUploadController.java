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
package net.sourceforge.myfaces.examples.misc;

import net.sourceforge.myfaces.component.ext.UploadedFile;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class FileUploadController
    implements ActionListener
{
    public PhaseId getPhaseId()
    {
        return PhaseId.UPDATE_MODEL_VALUES;
    }

    public void processAction(ActionEvent event) throws AbortProcessingException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        FileUploadForm form = (FileUploadForm)facesContext.getModelValue("fileUploadForm");
        if (form != null)
        {
            UploadedFile upFile = form.getUpFile();
            facesContext.getServletContext().setAttribute("fileupload_file", upFile.getFile());
            facesContext.getServletContext().setAttribute("fileupload_type", upFile.getContentType());
        }
    }
}
