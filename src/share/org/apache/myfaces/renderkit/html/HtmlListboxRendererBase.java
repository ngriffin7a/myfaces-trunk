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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import java.io.IOException;


/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/07/01 22:00:56  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/06/04 00:26:16  o_rossmueller
 * modified renderes to comply with JSF 1.1
 *
 * Revision 1.2  2004/05/26 11:10:12  o_rossmueller
 * fix #959926: styleClass support for selectOneRadio, selectOneList, selectManyList
 *
 * Revision 1.1  2004/05/18 14:31:39  manolito
 * user role support completely moved to components source tree
 *
 */
public class HtmlListboxRendererBase
        extends HtmlRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            int size;
            if (uiComponent instanceof HtmlSelectManyListbox)
            {
                size = ((HtmlSelectManyListbox)uiComponent).getSize();
            }
            else
            {
                Integer i = (Integer)uiComponent.getAttributes().get(JSFAttr.SIZE_ATTR);
                size = i != null ? i.intValue() : 0;
            }
            HtmlRendererUtils.renderListbox(facesContext,
                                            (UISelectMany)uiComponent,
                                            isDisabled(facesContext, uiComponent),
                                            size);
        }
        else if (uiComponent instanceof HtmlSelectOneListbox)
        {
            int size;
            if (uiComponent instanceof HtmlSelectOneListbox)
            {
                size = ((HtmlSelectOneListbox)uiComponent).getSize();
            }
            else
            {
                Integer i = (Integer)uiComponent.getAttributes().get(JSFAttr.SIZE_ATTR);
                size = i != null ? i.intValue() : 0;
            }
            HtmlRendererUtils.renderListbox(facesContext,
                                            (UISelectOne)uiComponent,
                                            isDisabled(facesContext, uiComponent),
                                            size);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }
    
    
    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlListboxRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlSelectManyListbox)
        {
            return ((HtmlSelectManyListbox)uiComponent).isDisabled();
        }
        else if (uiComponent instanceof HtmlSelectOneListbox)
        {
            return ((HtmlSelectOneListbox)uiComponent).isDisabled();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
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
