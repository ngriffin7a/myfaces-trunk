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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
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
    
    private JavascriptUtils()
    {
    }    //Util class

    private static final Set RESERVED_WORDS;

    static
    {
        RESERVED_WORDS = new HashSet(Arrays.asList(new String[]{
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
    }

    private static byte[] COLON_BYTES = new byte[] {':'};
    private static byte[] HYPHEN_BYTES = new byte[] {'-'};
    private static byte[] UNDERSCORE_BYTES = new byte[] {'_'};

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
            byte[] bytesToAdd = null;

            if (Character.isLetter(c) ||
                Character.isDigit(c))
            {
                //allowed char
            }
            else if (c == ':')
            {
                bytesToAdd = COLON_BYTES;
            }
            else if (c == '-')
            {
                bytesToAdd = HYPHEN_BYTES;
            }
            else if (c == '_')
            {
                bytesToAdd = UNDERSCORE_BYTES;
            }
            else
            {
                try
                {
                    bytesToAdd = ("" + c).getBytes("UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException(e);
                }
            }

            if (bytesToAdd != null)
            {
                if (buf == null)
                {
                    buf = new StringBuffer();
                    buf.append(s.substring(0, i));
                }
                buf.append('_');
                for (int j = 0; j < bytesToAdd.length; j++)
                {
                    int b = (int)bytesToAdd[j];
                    if (b < 0) b = 256 + b;
                    else if (b < 16) buf.append('0');
                    buf.append(Integer.toHexString(b).toUpperCase());
                }
            }
            else if (buf != null)
            {
                buf.append(c);
            }
        }

        return buf == null ? s : buf.toString();
    }

}
