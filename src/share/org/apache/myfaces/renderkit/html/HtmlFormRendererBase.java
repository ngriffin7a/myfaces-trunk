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

import net.sourceforge.myfaces.config.MyfacesConfig;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.JavascriptUtils;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.8  2004/09/08 15:23:12  manolito
 * Autoscroll feature
 *
 * Revision 1.7  2004/07/01 22:00:57  mwessendorf
 * ASF switch
 *
 * Revision 1.6  2004/06/03 12:57:03  o_rossmueller
 * modified link renderer to use one hidden field for all links according to 1.1 renderkit docs
 * added onclick=clear_XXX to button
 *
 * Revision 1.5  2004/05/18 12:02:29  manolito
 * getActionURL and getResourceURL must not call encodeActionURL or encodeResourceURL
 *
 * Revision 1.4  2004/04/30 09:11:38  manolito
 * no message
 *
 * Revision 1.3  2004/04/29 19:34:38  o_rossmueller
 * javascript for 'target' attribute handling
 *
 * Revision 1.2  2004/04/27 10:32:24  manolito
 * clear hidden inputs javascript function
 *
 * Revision 1.1  2004/04/22 10:20:34  manolito
 * tree component
 *
 */
public class HtmlFormRendererBase
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlFormRenderer.class);

    private static final String HIDDEN_SUBMIT_INPUT_SUFFIX = "_SUBMIT";
    private static final String HIDDEN_SUBMIT_INPUT_VALUE = "1";

    private static final String HIDDEN_COMMAND_INPUTS_SET_ATTR
            = HtmlFormRendererBase.class.getName() + ".HIDDEN_COMMAND_INPUTS_SET";


    public void encodeBegin(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIForm.class);

        UIForm htmlForm = (HtmlForm)component;

        ResponseWriter writer = facesContext.getResponseWriter();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        String clientId = htmlForm.getClientId(facesContext);
        String actionURL = viewHandler.getActionURL(facesContext, viewId);

        writer.startElement(HTML.FORM_ELEM, htmlForm);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.METHOD_ATTR, "post", null);
        writer.writeURIAttribute(HTML.ACTION_ATTR,
                                 facesContext.getExternalContext().encodeActionURL(actionURL),
                                 null);

        HtmlRendererUtils.renderHTMLAttributes(writer, htmlForm, HTML.FORM_PASSTHROUGH_ATTRIBUTES);
        writer.write(""); // forse start element tag to be closed
    }


    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        //write state marker
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        viewHandler.writeState(facesContext);

        //write hidden input to determine "submitted" value on decode
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, component.getClientId(facesContext) +
                                              HIDDEN_SUBMIT_INPUT_SUFFIX, null);
        writer.writeAttribute(HTML.VALUE_ATTR, HIDDEN_SUBMIT_INPUT_VALUE, null);
        writer.endElement(HTML.INPUT_ELEM);

        if (MyfacesConfig.getCurrentInstance(facesContext.getExternalContext()).isAutoScroll())
        {
            JavascriptUtils.renderAutoScrollHiddenInput(writer);
        }

        //render hidden command inputs
        Set set = (Set)component.getAttributes().get(HIDDEN_COMMAND_INPUTS_SET_ATTR);
        if (set != null && !set.isEmpty())
        {
            HtmlRendererUtils.renderHiddenCommandFormParams(writer, set);

            String target;
            if (component instanceof HtmlForm)
            {
                target = ((HtmlForm)component).getTarget();
            }
            else
            {
                target = (String)component.getAttributes().get(HTML.TARGET_ATTR);
            }
            HtmlRendererUtils.renderClearHiddenCommandFormParamsFunction(writer,
                                                                         component.getClientId(facesContext),
                                                                         set,
                                                                         target);
        }

        writer.endElement(HTML.FORM_ELEM);
    }


    public void decode(FacesContext facesContext, UIComponent component)
    {
        RendererUtils.checkParamValidity(facesContext, component, UIForm.class);

        /*
        if (HTMLUtil.isDisabled(component))
        {
            return;
        }
        */

        UIForm htmlForm = (UIForm)component;

        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        String submittedValue = (String)paramMap.get(component.getClientId(facesContext) +
                                                     HIDDEN_SUBMIT_INPUT_SUFFIX);
        if (submittedValue != null && submittedValue.equals(HIDDEN_SUBMIT_INPUT_VALUE))
        {
            htmlForm.setSubmitted(true);
        }
        else
        {
            htmlForm.setSubmitted(false);
        }
    }


    public static void addHiddenCommandParameter(UIComponent form, String paramName)
    {
        Set set = (Set)form.getAttributes().get(HIDDEN_COMMAND_INPUTS_SET_ATTR);
        if (set == null)
        {
            set = new HashSet();
            form.getAttributes().put(HIDDEN_COMMAND_INPUTS_SET_ATTR, set);
        }
        set.add(paramName);
    }

}
