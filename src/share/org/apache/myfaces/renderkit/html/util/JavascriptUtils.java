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
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.config.MyfacesConfig;

import javax.faces.context.ExternalContext;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
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
    //private static final Log log = LogFactory.getLog(JavascriptUtils.class);

    public static final String JAVASCRIPT_DETECTED = JavascriptUtils.class + ".JAVASCRIPT_DETECTED";


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

}
