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
import net.sourceforge.myfaces.renderkit.html.util.DummyFormResponseWriter;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormUtils;
import net.sourceforge.myfaces.renderkit.html.util.JavascriptUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2004/09/08 09:32:03  manolito
 * MyfacesConfig moved to config package
 *
 * Revision 1.5  2004/07/26 09:19:08  manolito
 * removed onclick from passthrough attributes for ButtonRenderer
 *
 * Revision 1.4  2004/07/01 22:00:57  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/06/03 12:57:03  o_rossmueller
 * modified link renderer to use one hidden field for all links according to 1.1 renderkit docs
 * added onclick=clear_XXX to button
 *
 * Revision 1.2  2004/06/03 11:45:40  o_rossmueller
 * added check for .y image button suffix according to 1.1 renderkit docs
 *
 * Revision 1.1  2004/05/18 14:31:39  manolito
 * user role support completely moved to components source tree
 *
 */
public class HtmlButtonRendererBase
    extends HtmlRenderer
{
    private static final String IMAGE_BUTTON_SUFFIX_X = ".x";
    private static final String IMAGE_BUTTON_SUFFIX_Y = ".y";

    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UICommand.class);

        //super.decode must not be called, because value is handled here
        if (!isReset(uiComponent) && isSubmitted(facesContext, uiComponent))
        {
            uiComponent.queueEvent(new ActionEvent(uiComponent));
        }
    }

    private static boolean isReset(UIComponent uiComponent)
    {
        return "reset".equalsIgnoreCase((String) uiComponent.getAttributes().get(HTML.TYPE_ATTR));
    }

    private static boolean isSubmitted(FacesContext facesContext, UIComponent uiComponent)
    {
        String clientId = uiComponent.getClientId(facesContext);
        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        return paramMap.containsKey(clientId) || paramMap.containsKey(clientId + IMAGE_BUTTON_SUFFIX_X) || paramMap.containsKey(clientId + IMAGE_BUTTON_SUFFIX_Y);
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UICommand.class);

        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);

        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        String image = getImage(uiComponent);

        if (image != null)
        {
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_IMAGE, null);
            writer.writeAttribute(HTML.SRC_ATTR, image, JSFAttr.IMAGE_ATTR);
        }
        else
        {
            String type = getType(uiComponent);

            if (type == null)
            {
                type = HTML.INPUT_TYPE_SUBMIT;
            }
            writer.writeAttribute(HTML.TYPE_ATTR, type, JSFAttr.TYPE_ATTR);
            Object value = getValue(uiComponent);
            if (value != null)
            {
                writer.writeAttribute(HTML.VALUE_ATTR, value, null);
            }
        }
        if (JavascriptUtils.isJavascriptAllowed(facesContext.getExternalContext()))
        {
            renderClearFormOnClick(uiComponent, facesContext, writer);
            HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                   HTML.BUTTON_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED_AND_ONCLICK);
        }
        else
        {
            HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                   HTML.BUTTON_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        }

        if (isDisabled(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        writer.endElement(HTML.INPUT_ELEM);
    }


    private void renderClearFormOnClick(UIComponent uiComponent, FacesContext facesContext, ResponseWriter writer)
        throws IOException
    {
        //Find form
        UIComponent parent = uiComponent.getParent();
        while (parent != null && !(parent instanceof UIForm))
        {
            parent = parent.getParent();
        }

        UIForm nestingForm = null;
        String formName;
        DummyFormResponseWriter dummyFormResponseWriter;
        if (parent != null)
        {
            //link is nested inside a form
            nestingForm = (UIForm)parent;
            formName = nestingForm.getClientId(facesContext);
            dummyFormResponseWriter = null;
        }
        else
        {
            //not nested in form, we must add a dummy form at the end of the document
            formName = DummyFormUtils.DUMMY_FORM_NAME;
            dummyFormResponseWriter = DummyFormUtils.getDummyFormResponseWriter(facesContext);
            dummyFormResponseWriter.setWriteDummyForm(true);
        }
        StringBuffer onClick = new StringBuffer();
        String commandOnClick = (String)uiComponent.getAttributes().get(HTML.ONCLICK_ATTR);

        if (commandOnClick != null)
        {
            onClick.append(commandOnClick);
            onClick.append(';');
        }

        //call the clear_<formName> method
        onClick.append(HtmlRendererUtils.getClearHiddenCommandFormParamsFunctionName(formName)).append("();");

        //add hidden field for the case there is no commandLink in the form
        String hiddenFieldName = HtmlRendererUtils.getHiddenCommandLinkFieldName(formName);
        if (nestingForm != null)
        {
            HtmlFormRendererBase.addHiddenCommandParameter(nestingForm, hiddenFieldName);
        }
        else
        {
            dummyFormResponseWriter.addDummyFormParameter(hiddenFieldName);
        }

        renderOnClickAttribute(facesContext, writer, nestingForm, formName, onClick.toString());
    }



    /**
     * Can be overwritten if additional javascript is needed on every link click.
     * @param facesContext
     * @param writer
     * @param nestingForm  form, this link is nested in or null if no nesting form
     * @param formName     name of nesting form or dummy form
     * @param onClickValue value of onClick attribute to be rendered
     * @throws IOException
     */
    protected void renderOnClickAttribute(FacesContext facesContext,
                                          ResponseWriter writer,
                                          UIForm nestingForm,
                                          String formName,
                                          String onClickValue)
        throws IOException
    {
        writer.writeAttribute(HTML.ONCLICK_ATTR, onClickValue, null);
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlButtonRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlCommandButton)
        {
            return ((HtmlCommandButton)uiComponent).isDisabled();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
        }
    }


    private String getImage(UIComponent uiComponent)
    {
        if (uiComponent instanceof HtmlCommandButton)
        {
            return ((HtmlCommandButton)uiComponent).getImage();
        }
        return (String)uiComponent.getAttributes().get(JSFAttr.IMAGE_ATTR);
    }

    private String getType(UIComponent uiComponent)
    {
        if (uiComponent instanceof HtmlCommandButton)
        {
            return ((HtmlCommandButton)uiComponent).getType();
        }
        return (String)uiComponent.getAttributes().get(JSFAttr.TYPE_ATTR);
    }

    private Object getValue(UIComponent uiComponent)
    {
        if (uiComponent instanceof ValueHolder)
        {
            return ((ValueHolder)uiComponent).getValue();
        }
        return uiComponent.getAttributes().get(JSFAttr.VALUE_ATTR);
    }
}
