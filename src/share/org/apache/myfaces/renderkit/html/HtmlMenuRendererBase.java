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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

/**
 * X-CHECKED: tlddoc of h:selectManyListbox
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/06/04 00:26:16  o_rossmueller
 * modified renderes to comply with JSF 1.1
 *
 * Revision 1.1  2004/05/18 14:31:39  manolito
 * user role support completely moved to components source tree
 *
 */
public class HtmlMenuRendererBase
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlMenuRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, null);

        if (component instanceof UISelectMany)
        {
            HtmlRendererUtils.renderMenu(facesContext,
                                         (UISelectMany)component,
                                         isDisabled(facesContext, component));
        }
        else if (component instanceof UISelectOne)
        {
            HtmlRendererUtils.renderMenu(facesContext,
                                         (UISelectOne)component,
                                         isDisabled(facesContext, component));
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlMenuRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlSelectManyMenu)
        {
            return ((HtmlSelectManyMenu)uiComponent).isDisabled();
        }
        else if (uiComponent instanceof HtmlSelectOneMenu)
        {
            return ((HtmlSelectOneMenu)uiComponent).isDisabled();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
        }
    }


    private String getStyleClass(UISelectMany component)
    {
        if (component instanceof HtmlSelectManyMenu)
        {
            return ((HtmlSelectManyMenu)component).getStyleClass();
        }
        else
        {
            return (String)component.getAttributes().get(HTML.STYLE_CLASS_ATTR);
        }
    }

    private String getStyleClass(UISelectOne component)
    {
        if (component instanceof HtmlSelectOneMenu)
        {
            return ((HtmlSelectOneMenu)component).getStyleClass();
        }
        else
        {
            return (String)component.getAttributes().get(HTML.STYLE_CLASS_ATTR);
        }
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            HtmlRendererUtils.decodeUISelectMany(facesContext, uiComponent);
        }
        else if (uiComponent instanceof UISelectOne)
        {
            HtmlRendererUtils.decodeUISelectOne(facesContext, uiComponent);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            return RendererUtils.getConvertedUISelectManyValue(facesContext,
                                                               (UISelectMany)uiComponent,
                                                               submittedValue);
        }
        else if (uiComponent instanceof UISelectOne)
        {
            return RendererUtils.getConvertedUIOutputValue(facesContext,
                                                           (UISelectOne)uiComponent,
                                                           submittedValue);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }

}
