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
package org.apache.myfaces.renderkit.html.util;

import org.apache.myfaces.config.MyfacesConfig;
import org.apache.myfaces.renderkit.html.HTML;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.9  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.8  2004/09/10 14:13:52  manolito
 * trivial change
 *
 * Revision 1.7  2004/09/08 15:51:15  manolito
 * Autoscroll now also for horizontal scrolling
 *
 * Revision 1.6  2004/09/08 15:23:11  manolito
 * Autoscroll feature
 *
 * Revision 1.5  2004/09/08 09:31:25  manolito
 * moved isJavascriptDetected from MyFacesConfig to JavascriptUtils class
 *
 * Revision 1.4  2004/07/16 13:06:30  manolito
 * encode javascript strings for jscook menu labels
 *
 * Revision 1.3  2004/07/09 02:44:55  dave0000
 * More efficient implementation
 *
 * Revision 1.2  2004/07/01 22:00:53  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/04/29 14:25:22  manolito
 * javascript function name bugfix
 *
 */
public final class JavascriptUtils
{
    private static final Log log = LogFactory.getLog(JavascriptUtils.class);

    public static final String JAVASCRIPT_DETECTED = JavascriptUtils.class.getName() + ".JAVASCRIPT_DETECTED";

    private static final String AUTO_SCROLL_PARAM = "autoScroll";
    private static final String AUTO_SCROLL_FUNCTION = "getScrolling()";

    private static final String OLD_VIEW_ID = JavascriptUtils.class + ".OLD_VIEW_ID";


    private JavascriptUtils()
    {
        // utility class, do not instantiate
    }
    
    private static final Set RESERVED_WORDS = 
        new HashSet(Arrays.asList(new String[]{
            "abstract",
            "boolean",
            "break",
            "byte",
            "case",
            "catch",
            "char",
            "class",
            "const",
            "continue",
            "default",
            "delete",
            "do",
            "double",
            "else",
            "export",
            "extends",
            "false",
            "final",
            "finally",
            "float",
            "for",
            "function",
            "goto",
            "if",
            "implements",
            "in",
            "instanceof",
            "int",
            "long",
            "native",
            "new",
            "null",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "super",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "transient",
            "true",
            "try",
            "typeof",
            "var",
            "void",
            "while",
            "with"
        }));
    
    public static String getValidJavascriptName(String s, boolean checkForReservedWord)
    {
        if (checkForReservedWord && RESERVED_WORDS.contains(s))
        {
            return s + "_";
        }
        
        StringBuffer buf = null;
        for (int i = 0, len = s.length(); i < len; i++)
        {
            char c = s.charAt(i);
            
            if (Character.isLetterOrDigit(c))
            {
                // allowed char
                if (buf != null) buf.append(c);
            }
            else
            {
                if (buf == null)
                {
                    buf = new StringBuffer(s.length() + 10);
                    buf.append(s.substring(0, i));
                }
                
                buf.append('_');
                if (c < 16)
                { 
                    // pad single hex digit values with '0' on the left
                    buf.append('0');
                }
                
                if (c < 128)
                {
                    // first 128 chars match their byte representation in UTF-8
                    buf.append(Integer.toHexString(c).toUpperCase());
                }
                else
                {
                    byte[] bytes;
                    try 
                    {
                        bytes = Character.toString(c).getBytes("UTF-8");
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        throw new RuntimeException(e);
                    }
                    
                    for (int j = 0; j < bytes.length; j++)
                    {
                        int intVal = bytes[j];
                        if (intVal < 0) 
                        {
                            // intVal will be >= 128
                            intVal = 256 + intVal;
                        }
                        else if (intVal < 16) 
                        {
                            // pad single hex digit values with '0' on the left
                            buf.append('0');
                        }
                        buf.append(Integer.toHexString(intVal).toUpperCase());
                    }
                }
            }
            
        }
        
        return buf == null ? s : buf.toString();
    }


    public static String encodeString(String string)
    {
        if (string == null)
        {
            return "";
        }
        StringBuffer sb = null;	//create later on demand
        String app;
        char c;
        for (int i = 0; i < string.length (); ++i)
        {
            app = null;
            c = string.charAt(i);
            switch (c)
            {
                case '\\' : app = "\\";  break;
                case '\"' : app = "\\\"";  break;
                case '\'' : app = "\\'";  break;
                case '\n' : app = "\\n";  break;
            }
            if (app != null)
            {
                if (sb == null)
                {
                    sb = new StringBuffer(string.substring(0, i));
                }
                sb.append(app);
            } else {
                if (sb != null)
                {
                    sb.append(c);
                }
            }
        }

        if (sb == null)
        {
            return string;
        }
        else
        {
            return sb.toString();
        }
    }


    public static boolean isJavascriptAllowed(ExternalContext externalContext)
    {
        MyfacesConfig myfacesConfig = MyfacesConfig.getCurrentInstance(externalContext);
        if (myfacesConfig.isAllowJavascript())
        {
            if (myfacesConfig.isDetectJavascript())
            {
                return isJavascriptDetected(externalContext);
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }


    public static void setJavascriptDetected(ExternalContext externalContext, boolean value)
    {
        externalContext.getSessionMap().put(JAVASCRIPT_DETECTED, Boolean.valueOf(value));
    }

    public static boolean isJavascriptDetected(ExternalContext externalContext)
    {
        //TODO/FIXME (manolito): This info should be better stored in the viewroot component and not in the session
        Boolean sessionValue = (Boolean)externalContext.getSessionMap().get(JAVASCRIPT_DETECTED);
        return sessionValue == null ? false : sessionValue.booleanValue();
    }


    /**
     * Adds the hidden form input value assignment that is necessary for the autoscroll
     * feature to an html link or button onclick attribute.
     */
    public static void appendAutoScrollAssignment(StringBuffer onClickValue, String formName)
    {
        onClickValue.append("document.forms['").append(formName).append("']");
        onClickValue.append(".elements['").append(AUTO_SCROLL_PARAM).append("']");
        onClickValue.append(".value=").append(AUTO_SCROLL_FUNCTION).append(";");
    }

    /**
     * Renders the hidden form input that is necessary for the autoscroll feature.
     */
    public static void renderAutoScrollHiddenInput(ResponseWriter writer) throws IOException
    {
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, AUTO_SCROLL_PARAM, null);
        writer.endElement(HTML.INPUT_ELEM);
    }

    /**
     * Renders the autoscroll javascript function.
     */
    public static void renderAutoScrollFunction(FacesContext facesContext,
                                                ResponseWriter writer) throws IOException
    {
        writer.write("\n<script language=\"JavaScript\">\n" +
                     "<!--\n" +
                     "function " + AUTO_SCROLL_FUNCTION + " {\n" +
                     "    x = 0; y = 0;\n" +
                     "    if (document.body && document.body.scrollLeft && !isNaN(document.body.scrollLeft)) {\n" +
                     "        x = document.body.scrollLeft;\n" +
                     "    } else if (window.pageXOffset && !isNaN(window.pageXOffset)) {\n" +
                     "        x = window.pageXOffset;\n" +
                     "    }\n" +
                     "    if (document.body && document.body.scrollTop && !isNaN(document.body.scrollTop)) {\n" +
                     "        y = document.body.scrollTop;\n" +
                     "    } else if (window.pageYOffset && !isNaN(window.pageYOffset)) {\n" +
                     "        y = window.pageYOffset;\n" +
                     "    }\n" +
                     "    return x + \",\" + y;\n" +
                     "}\n");
        ExternalContext externalContext = facesContext.getExternalContext();
        String oldViewId = getOldViewId(externalContext);
        if (oldViewId != null && oldViewId.equals(facesContext.getViewRoot().getViewId()))
        {
            //ok, we stayed on the same page, so let's scroll it to the former place
            String scrolling = (String)externalContext.getRequestParameterMap().get(AUTO_SCROLL_PARAM);
            if (scrolling != null && scrolling.length() > 0)
            {
                String x = "0";
                String y = "0";
                int comma = scrolling.indexOf(',');
                if (comma == -1)
                {
                    log.warn("Illegal autoscroll request parameter: " + scrolling);
                }
                else
                {
                    x = scrolling.substring(0, comma);
                    if (x.equals("undefined")) x = "0";
                    y = scrolling.substring(comma + 1);
                    if (y.equals("undefined")) y = "0";
                }
                writer.write("window.scrollTo(" + x + "," + y + ");\n");
            }
        }
        writer.write("//-->\n" +
                     "</script>\n");
    }


    public static void setOldViewId(ExternalContext externalContext, String viewId)
    {
        externalContext.getRequestMap().put(OLD_VIEW_ID, viewId);
    }

    public static String getOldViewId(ExternalContext externalContext)
    {
        return (String)externalContext.getRequestMap().get(OLD_VIEW_ID);
    }
}
