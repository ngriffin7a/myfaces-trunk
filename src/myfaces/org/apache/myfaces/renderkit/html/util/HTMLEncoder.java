/**
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
package net.sourceforge.myfaces.renderkit.html.util;


/**
 * Converts Strings so that they can be used within HTML-Code.
 */
public abstract class HTMLEncoder
{
	/**
	 * Variant of {@link #encode} where encodeNewline is false and encodeNbsp is true.
	 */
	public static String encode (String string)
	{
		return encode(string, false, true);
	}

	/**
	 * Variant of {@link #encode} where encodeNbsp is true.
	 */
	public static String encode (String string, boolean encodeNewline)
	{
		return encode(string, encodeNewline, true);
	}

	/**
	 * Encodes the given string, so that it can be used within a html page.
	 * @param string the string to convert
	 * @param encodeNewline if true newline characters are converted to &lt;br&gt;'s
	 * @param encodeSubsequentBlanksToNbsp if true subsequent blanks are converted to &amp;nbsp;'s
	 */
	public static String encode (String string,
								 boolean encodeNewline,
								 boolean encodeSubsequentBlanksToNbsp)
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
                case '"': app = "&quot;"; break;    //"
                case '&': app = "&amp;"; break;     //&
                case '<': app = "&lt;"; break;      //<
                case '>': app = "&gt;"; break;      //>
                case ' ':
                    if (encodeSubsequentBlanksToNbsp &&
                        (i == 0 || (i - 1 >= 0 && string.charAt(i - 1) == ' ')))
                    {
                        //Space at beginning or after another space
                        app = "&nbsp;";
                    }
                    break;
                case '\n':
                    if (encodeNewline)
                    {
                        app = "<br>";
                    }
                    break;

                //german umlauts
			    case '\u00E4' : app = "&auml;";  break;     //ä
			    case '\u00C4' : app = "&Auml;";  break;     //Ä
			    case '\u00F6' : app = "&ouml;";  break;     //ö
			    case '\u00D6' : app = "&Ouml;";  break;     //Ö
			    case '\u00FC' : app = "&uuml;";  break;     //ü
			    case '\u00DC' : app = "&Uuml;";  break;     //Ü
			    case '\u00DF' : app = "&szlig;"; break;     //ß

                //misc
                //case 0x80: app = "&euro;"; break;  sometimes euro symbol is ascii 128, should we suport it?
                case '\u20AC': app = "&euro;";  break;
                case '\u00AB': app = "&laquo;"; break;
                case '\u00BB': app = "&raquo;"; break;
                case '\u00A0': app = "&nbsp;"; break;

                default:
                    if (((int)c) >= 0x80)
                    {
                        //encode all non basic latin characters
                        app = "&#" + ((int)c) + ";";
                    }
                    break;
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


}
