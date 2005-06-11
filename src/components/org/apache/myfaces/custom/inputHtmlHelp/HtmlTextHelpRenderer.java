package org.apache.myfaces.custom.inputHtmlHelp;

import org.apache.myfaces.renderkit.html.HtmlTextRendererBase;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.util.HTMLEncoder;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.component.html.util.AddResource;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import java.io.IOException;

/**
 * @author Thomas Obereder
 * @version Date: 09.06.2005, 22:50:48
 */
public class HtmlTextHelpRenderer extends HtmlTextRendererBase
{
    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        if(component instanceof HtmlInputTextHelp)
        {
            HtmlInputTextHelp helpTextComp = (HtmlInputTextHelp) component;
            //TODO replace with js import
            if(isAddResources(component))
                renderHelpTextJSFunctions(component, facesContext.getResponseWriter());
//            addJavaScriptResources(facesContext);
            renderInputTextHelp(facesContext, (UIInput)helpTextComp);
        }
        else
        {
            super.encodeEnd(facesContext, component);
        }
    }

    public static boolean isSelectText(UIComponent component)
    {
        if(component instanceof HtmlInputTextHelp)
        {
            HtmlInputTextHelp helpTextComp = (HtmlInputTextHelp) component;
            return helpTextComp.isSelectText();
        }
        return false;
    }

    public static String getHelpText(UIComponent component)
    {
        if(component instanceof HtmlInputTextHelp)
        {
            HtmlInputTextHelp helpTextComp = (HtmlInputTextHelp) component;
            if(helpTextComp.getHelpText() != null)
                return helpTextComp.getHelpText();
        }
        return null;
    }

    public static boolean isAddResources(UIComponent component)
    {
        if(component instanceof HtmlInputTextHelp)
        {
            HtmlInputTextHelp helpTextComp = (HtmlInputTextHelp) component;
            return helpTextComp.isAddResources();
        }
        return false;
    }

    public static void renderInputTextHelp(FacesContext facesContext, UIInput input)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, input);

        HtmlRendererUtils.writeIdIfNecessary(writer, input, facesContext);

        renderHelpTextAttributes(input, writer, facesContext);

        String value = RendererUtils.getStringValue(facesContext, input);
        value = (value.equals("") || value == null) ? getHelpText(input) : "";

        writer.writeAttribute(HTML.VALUE_ATTR, HTMLEncoder.encode(value,true,true), null);

        writer.endElement(HTML.INPUT_ELEM);
    }

    public static void renderHelpTextAttributes(UIComponent component,
                                                ResponseWriter writer,
                                                FacesContext facesContext)
            throws IOException
    {
        if(!(component instanceof HtmlInputTextHelp))
        {
            HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.COMMON_PASSTROUGH_ATTRIBUTES);
            return;
        }
        else
        {
            String id = component.getClientId(facesContext);
            if(isSelectText(component))
            {
                HtmlRendererUtils.renderHTMLAttributes(writer, component,
                        HTML.COMMON_PASSTROUGH_ATTRIBUTES_WITHOUT_ONCLICK);
                writer.writeAttribute(HTML.ONCLICK_ATTR,
                        HtmlInputTextHelp.JS_FUNCTION_SELECT_TEXT + "('" + id +"')", null);
            }
            else
            {
                if(getHelpText(component) != null)
                {
                    HtmlRendererUtils.renderHTMLAttributes(writer, component,
                            HTML.COMMON_PASSTROUGH_ATTRIBUTES_WITHOUT_ONCLICK);
                    writer.writeAttribute(HTML.ONCLICK_ATTR,
                            HtmlInputTextHelp.JS_FUNCTION_RESET_HELP + "('" +
                            getHelpText(component) + "', '" + id +"')", null);
                }
                else
                {
                    HtmlRendererUtils.renderHTMLAttributes(writer,
                            component, HTML.COMMON_PASSTROUGH_ATTRIBUTES);
                }
            }
        }
    }

    //TODO
    public static void renderHelpTextJSFunctions(UIComponent comp,
                                                ResponseWriter writer)
            throws IOException
    {
        if(getHelpText(comp) != null)
        {
            writer.startElement(HTML.SCRIPT_ELEM, comp);
            writer.writeAttribute(HTML.TYPE_ATTR,
                    HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            writer.write("\n<!--\nfunction " + HtmlInputTextHelp.JS_FUNCTION_RESET_HELP + "(helpText, id)\n" +
                    "{\n\t" +
                    "var element=document.getElementById(id);\n\t"+
                    "if(element.value==helpText)\n\t" +
                    "{\n\t\t" +
                    "element.value=''\n\t" +
                    "}\n" +
                    "}\n//-->\n");
            writer.write("\n<!--\nfunction " + HtmlInputTextHelp.JS_FUNCTION_SELECT_TEXT + "(id)\n" +
                    "{\n\t" +
                    "var element=document.getElementById(id);\n\t"+
                    "element.select();\n\t" +
                    "}\n//-->\n");
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }

    //TODO
    public static void addJavaScriptResources(FacesContext facesContext) throws IOException
    {
        AddResource.addJavaScriptToHeader(HtmlTextHelpRenderer.class,
                                            "inputHtmlHelp.js",
                                            facesContext);
    }
}
