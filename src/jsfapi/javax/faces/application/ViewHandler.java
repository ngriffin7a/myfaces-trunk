/*
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
package javax.faces.application;

import javax.faces.FacesException;

/**
 * DOCUMENT ME!
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class ViewHandler
{
    //private static final Log log = LogFactory.getLog(ViewHandler.class);

    public static final String CHARACTER_ENCODING_KEY = "javax.faces.request.charset";
    public static final String DEFAULT_SUFFIX_PARAM_NAME = "javax.faces.DEFAULT_SUFFIX";
    public static final String DEFAULT_SUFFIX = ".jsp";

    public abstract java.util.Locale calculateLocale(javax.faces.context.FacesContext context);

    public abstract java.lang.String calculateRenderKitId(javax.faces.context.FacesContext context);

    public abstract javax.faces.component.UIViewRoot createView(javax.faces.context.FacesContext context,
                                                                java.lang.String viewId);

    public abstract java.lang.String getActionURL(javax.faces.context.FacesContext context,
                                                  java.lang.String viewId);

    public abstract java.lang.String getResourceURL(javax.faces.context.FacesContext context,
                                                    java.lang.String path);

    public abstract void renderView(javax.faces.context.FacesContext context,
                                    javax.faces.component.UIViewRoot viewToRender)
            throws java.io.IOException,
                   FacesException;

    public abstract javax.faces.component.UIViewRoot restoreView(javax.faces.context.FacesContext context,
                                                                 java.lang.String viewId);

    public abstract void writeState(javax.faces.context.FacesContext context)
            throws java.io.IOException;
}
