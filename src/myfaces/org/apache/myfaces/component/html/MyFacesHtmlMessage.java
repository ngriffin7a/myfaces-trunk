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
package net.sourceforge.myfaces.component.html;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesHtmlMessage
    extends HtmlMessage
{
    public MyFacesHtmlMessage()
    {
        System.out.println("now");
    }

    /**
     * TODO: only for debugging, remove later
     */
    public Object saveState(FacesContext facesContext) {
        Object obj = super.saveState(facesContext);    //To change body of overridden methods use File | Settings | File Templates.
        return obj;
    }

    /**
     * TODO: only for debugging, remove later
     */
    public void restoreState(FacesContext facesContext, Object object) {
        super.restoreState(facesContext, object);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * TODO: only for debugging, remove later
     */
    public void processRestoreState(FacesContext context, Object state)
    {
        if (context == null)
        {
            throw new NullPointerException();
        }
        Object stateStruct[] = (Object[])state;
        Object childState[] = (Object[])stateStruct[1];
        restoreState(context, stateStruct[0]);
        int i = 0;
        UIComponent kid;
        for (Iterator kids = getChildren().iterator(); kids.hasNext(); kid.processRestoreState(context, childState[i++]))
        {
            kid = (UIComponent)kids.next();
        }

        int facetsSize = getFacets().size();
        int j = 0;
        Object facetSaveState[][] = null;
        String facetName = null;
        UIComponent facet = null;
        Object facetState = null;
        for (; j < facetsSize; j++)
        {
            if (null != (facetSaveState = (Object[][])childState[i++]))
            {
                facetName = (String)facetSaveState[0][0];
                facetState = facetSaveState[0][1];
                facet = (UIComponent)getFacets().get(facetName);
                facet.processRestoreState(context, facetState);
            }
        }

    }

}
