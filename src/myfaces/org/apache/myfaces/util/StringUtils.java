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
package net.sourceforge.myfaces.util;

import java.util.ArrayList;


/**
 * Implements utility functions for the String class
 *
 * <p>
 * Emphasis on performance and reduced memory allocation/garbage collection
 * in exchange for longer more complex code.
 * </p>
 *
 * @author Anton Koinov
 */
public class StringUtils
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String   EMPTY_STRING       = "";
    public static final String[] EMPTY_STRING_ARRAY = {};

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Checks that the string represents a floating point number that CANNOT be
     * in exponential notation
     *
     * @param str the string to check
     *
     * @return boolean
     */
    public static boolean isFloatNoExponent(String str)
    {
        int len = str.length();

        if (len == 0)
        {
            return false;
        }

        // skip first char if sign char
        char c = str.charAt(0);
        int  i = ((c == '-') || (c == '+')) ? 1 : 0;

        // is it only a sign?
        if (i >= len)
        {
            return false;
        }

        boolean decimalPointFound = false;

        do
        {
            c = str.charAt(i);

            if (c == '.')
            {
                // is this a second dot?
                if (decimalPointFound)
                {
                    return false;
                }

                decimalPointFound = true;
            }
            else if (!Character.isDigit(c))
            {
                return false;
            }

            i++;
        }
        while (i < len);

        return true;
    }

    public static boolean isFloatWithOptionalExponent(String str)
    {
        int len = str.length();

        if (len == 0)
        {
            return false;
        }

        // skip first char if sign char
        char c = str.charAt(0);
        int  i = ((c == '-') || (c == '+')) ? 1 : 0;

        // is it only a sign?
        if (i >= len)
        {
            return false;
        }

        boolean exponentFound     = false;
        boolean decimalPointFound = false;

        do
        {
            c = str.charAt(i);

            switch (c)
            {
                case '.':

                    // is this a second one, are in the exponent?
                    if (decimalPointFound || exponentFound)
                    {
                        return false;
                    }

                    decimalPointFound = true;

                    break;

                case 'e':
                case 'E':

                    // is this a second one?
                    if (exponentFound)
                    {
                        return false;
                    }

                    exponentFound = true;

                    // check for exponent sign
                    c = str.charAt(i + 1);

                    if ((c == '-') || (c == '+'))
                    {
                        i++;
                    }

                    break;

                default:

                    if (!Character.isDigit(c))
                    {
                        return false;
                    }
            }

            i++;
        }
        while (i < len);

        return true;
    }

    public static boolean isInteger(String str)
    {
        int len = str.length();

        if (len == 0)
        {
            return false;
        }

        // skip first char if sign char
        char c = str.charAt(0);
        int  i = ((c == '-') || (c == '+')) ? 1 : 0;

        // is it only a sign?
        if (i >= len)
        {
            return false;
        }

        do
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }

            i++;
        }
        while (i < len);

        return true;
    }

    public static boolean isUnsignedInteger(String str)
    {
        int len = str.length();

        if (len == 0)
        {
            return false;
        }

        for (int i = 0; i < len; i++)
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Undoubles the quotes inside the string <br> Example:<br>
     * <pre>
     * hello""world becomes hello"world
     * </pre>
     *
     * @param str input string to dequote
     * @param quote the quoting char
     *
     * @return dequoted string
     */
    public static String dequote(String str, char quote)
    {
        // Is there anything to dequote?
        if (str == null)
        {
            return null;
        }

        return dequote(
            str,
            0,
            str.length(),
            quote);
    }

    /**
     * Undoubles the quotes inside a substring <br> Example:<br>
     * <pre>
     * hello""world becomes hello"world
     * </pre>
     * WARNING: scan for quote may continue to the end of the string, make sure
     * that either <code>charAt(end + 1) == quote</code> or <code>end =
     * str.lentgth()</code>. If in doubt call
     * <code>dequote(str.substring(begin, end), quote)</code>
     *
     * @param str input string from which to get the substring, must not be
     *        null
     * @param begin begin index for substring
     * @param end end index for substring
     * @param quote the quoting char
     *
     * @return dequoted string
     *
     * @throws IllegalArgumentException if string is incorrectly quoted
     */
    public static String dequote(String str, int begin, int end, char quote)
    {
        // Is there anything to dequote?
        if (begin == end)
        {
            return EMPTY_STRING;
        }

        int _end = str.indexOf(quote, begin);

        // If no quotes, return the original string
        // and save StringBuffer allocation/char copying
        if (_end < 0)
        {
            return str.substring(begin, end);
        }

        StringBuffer sb     = new StringBuffer(end - begin);

        int          _begin = begin; // need begin later

        for (; (_end >= 0) && (_end < end); _end = str.indexOf(quote, _begin = _end + 2))
        {
            if (((_end + 1) >= end) || (str.charAt(_end + 1) != quote))
            {
                throw new IllegalArgumentException(
                    "Internal quote not doubled in string '" + str.substring(begin, end) + "'");
            }

            sb.append(substring(str, _begin, _end)).append(quote);
        }

        return sb.append(substring(str, _begin, end)).toString();
    }

    /**
     * Removes the surrounding quote and any double quote inside the string <br>
     * Example:<br>
     * <pre>
     * "hello""world" becomes hello"world
     * </pre>
     *
     * @param str input string to dequote
     * @param quote the quoting char
     *
     * @return
     */
    public static String dequoteFull(String str, char quote)
    {
        if (str == null)
        {
            return null;
        }

        return dequoteFull(
            str,
            0,
            str.length(),
            quote);
    }

    public static String dequoteFull(String str, int begin, int end, char quote)
    {
        // If empty substring, return empty string
        if (begin == end)
        {
            return EMPTY_STRING;
        }

        // If not quoted, return string
        if (str.charAt(begin) != quote)
        {
            return str.substring(begin, end);
        }

        int _end = end - 1;

        if ((str.length() < 2) || (str.charAt(_end) != quote))
        {
            throw new IllegalArgumentException(
                "Closing quote missing in string '" + substring(str, begin, end) + "'");
        }

        return dequote(str, begin + 1, _end, quote);
    }

    public static String replace(String str, String repl, String with)
    {
        int lastindex = 0;
        int pos = str.indexOf(repl);

        // If no replacement needed, return the original string
        // and save StringBuffer allocation/char copying
        if (pos < 0)
        {
            return str;
        }

        int          len = repl.length();
        StringBuffer out = new StringBuffer(str.length() * 2);

        for (; pos >= 0; pos = str.indexOf(repl, lastindex = pos + len))
        {
            out.append(substring(str, lastindex, pos)).append(with);
        }

        return out.append(substring(
                str,
                lastindex,
                str.length())).toString();
    }

    public static String replace(String str, char repl, String with)
    {
        int lastindex = 0;
        int pos = str.indexOf(repl);

        // If no replacement needed, return the original string
        // and save StringBuffer allocation/char copying
        if (pos < 0)
        {
            return str;
        }

        int          len = str.length();

        StringBuffer out = new StringBuffer(len * 2);

        for (; pos >= 0; pos = str.indexOf(repl, lastindex = pos + 1))
        {
            out.append(substring(str, lastindex, pos)).append(with);
        }

        return out.append(substring(str, lastindex, len)).toString();
    }

    public static StringBuffer replace(StringBuffer out, String s, String repl, String with)
    {
        int lastindex = 0;
        int len = repl.length();

        for (int index = s.indexOf(repl); index >= 0;
                    index = s.indexOf(repl, lastindex = index + len))
        {
            // we have search string at position index
            out.append(substring(s, lastindex, index)).append(with);
        }

        return out.append(substring(s, lastindex, len));
    }

    /**
     * Split a string into an array of strings arround a character separator.
     * This  function will be efficient for longer strings
     *
     * @param str the string to be split
     * @param separator the separator character
     *
     * @return array of string subparts
     */
    public static String[] splitLongString(String str, char separator)
    {
        if (str == null)
        {
            return null;
        }

        int len = str.length();

        if (len == 0)
        {
            return EMPTY_STRING_ARRAY;
        }

        int       oldPos = 0;
        ArrayList list = new ArrayList();

        for (
            int pos = str.indexOf(separator); pos >= 0;
                    pos = str.indexOf(separator, (oldPos = (pos + 1))))
        {
            list.add(substring(str, oldPos, pos));
        }

        list.add(substring(str, oldPos, len));

        return (String[]) list.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Split a string into an array of strings arround a character separator.
     * Each element can be optionally quoted by the quote character.<br>
     * This function will be efficient for long strings
     *
     * @param str the string to be split
     * @param separator the separator character
     * @param quote the quote character
     *
     * @return array of string subparts
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static String[] splitLongString(String str, char separator, char quote)
    {
        if (str == null)
        {
            return null;
        }

        int len = str.length();

        if (len == 0)
        {
            return EMPTY_STRING_ARRAY;
        }

        int       oldPos = 0;
        ArrayList list = new ArrayList();

        for (int pos = 0; pos < len; oldPos = ++pos)
        {
            // Skip quoted text, if any
            while ((pos < len) && (str.charAt(pos) == quote))
            {
                pos = str.indexOf(quote, pos + 1) + 1;

                if (pos == 0)
                {
                    throw new IllegalArgumentException(
                        "Closing quote missing in string '" + str + "'");
                }
            }

            boolean quoted;

            if (pos != oldPos)
            {
                quoted = true;

                if ((pos < len) && (str.charAt(pos) != separator))
                {
                    throw new IllegalArgumentException(
                        "Separator must follow closing quote in string '" + str + "'");
                }
            }
            else
            {
                quoted     = false;

                pos        = str.indexOf(separator, pos);

                if (pos < 0)
                {
                    pos = len;
                }
            }

            list.add(
                quoted ? dequote(
                    substring(str, oldPos + 1, pos - 1),
                    quote) : substring(str, oldPos, pos));
        }

        return (String[]) list.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Split a string into an array of strings arround a character separator.
     * This  function will be efficient for short strings, for longer strings,
     * another approach may be better
     *
     * @param str the string to be split
     * @param separator the separator character
     *
     * @return array of string subparts
     */
    public static String[] splitShortString(String str, char separator)
    {
        if (str == null)
        {
            return null;
        }

        int len = str.length();

        if (len == 0)
        {
            return EMPTY_STRING_ARRAY;
        }

        int tokenCount = 0;

        // Step 1: how many substrings? We exchange double scan time for less memory allocation
        for (int pos = str.indexOf(separator); pos >= 0; pos = str.indexOf(separator, pos + 1))
        {
            tokenCount++;
        }

        // Step 2: allocate exact size array
        String[] list   = new String[++tokenCount];

        int      oldPos = 0;

        // Step 3: retrieve substrings
        for (
            int pos = str.indexOf(separator), i = 0; pos >= 0;
                    pos = str.indexOf(separator, (oldPos = (pos + 1))))
        {
            list[i++] = substring(str, oldPos, pos);
        }

        list[tokenCount - 1] = substring(str, oldPos, len);

        return list;
    }

    /**
     * Split a string into an array of strings arround a character separator.
     * Each element can be optionally quoted by the quote character.<br>
     * This function will be efficient for short strings, for longer strings,
     * another approach may be better
     *
     * @param str the string to be split
     * @param separator the separator character
     * @param quote the quote character
     *
     * @return array of string subparts
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static String[] splitShortString(String str, char separator, char quote)
    {
        if (str == null)
        {
            return null;
        }

        int len = str.length();

        if (len == 0)
        {
            return EMPTY_STRING_ARRAY;
        }

        // Step 1: how many substrings? We exchange double scan time for less memory allocation
        int tokenCount = 0;

        for (int pos = 0; pos < len; pos++)
        {
            tokenCount++;

            int oldPos = pos;

            // Skip quoted text, if any
            while ((pos < len) && (str.charAt(pos) == quote))
            {
                pos = str.indexOf(quote, pos + 1) + 1;

                // pos == 0 is not found (-1 returned by indexOf + 1)
                if (pos == 0)
                {
                    throw new IllegalArgumentException(
                        "Closing quote missing in string '" + str + "'");
                }
            }

            if (pos != oldPos)
            {
                if ((pos < len) && (str.charAt(pos) != separator))
                {
                    throw new IllegalArgumentException(
                        "Separator must follow closing quote in strng '" + str + "'");
                }
            }
            else
            {
                pos = str.indexOf(separator, pos);

                if (pos < 0)
                {
                    break;
                }
            }
        }

        // Main loop will finish one substring short when last char is separator
        if (str.charAt(len - 1) == separator)
        {
            tokenCount++;
        }

        // Step 2: allocate exact size array
        String[] list = new String[tokenCount];

        // Step 3: retrieve substrings 
        // Note: on this pass we do not check for correctness, since we have already done so
        tokenCount--; // we want to stop one token short

        int oldPos = 0;

        for (int pos = 0, i = 0; i < tokenCount; i++, oldPos = ++pos)
        {
            boolean quoted;

            // Skip quoted text, if any
            while (str.charAt(pos) == quote)
            {
                pos = str.indexOf(quote, pos + 1) + 1;
            }

            if (pos != oldPos)
            {
                quoted = true;

                if (str.charAt(pos) != separator)
                {
                    throw new IllegalArgumentException(
                        "Separator must follow closing quote in strng '" + str + "'");
                }
            }
            else
            {
                quoted     = false;
                pos        = str.indexOf(separator, pos);
            }

            list[i] =
                quoted ? dequote(
                    substring(str, oldPos + 1, pos - 1),
                    quote) : substring(str, oldPos, pos);
        }

        list[tokenCount] = dequoteFull(str, oldPos, len, quote);

        return list;
    }

    public static String substring(String str, int begin, int end)
    {
        if (begin == end)
        {
            return EMPTY_STRING;
        }

        return str.substring(begin, end);
    }

    public static String[] trim(String[] strings)
    {
        if (strings == null)
        {
            return null;
        }

        for (int i = 0, len = strings.length; i < len; i++)
        {
            strings[i] = strings[i].trim();
        }

        return strings;
    }
}
