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




/**
 * Converts Strings so that they can be used within HTML-Code.
 */
public abstract class UnicodeEncoder
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

		StringBuffer sb = null;
		char c;
		for (int i = 0; i < string.length (); ++i)
		{
			c = string.charAt(i);
			if (((int)c) >= 0x80)
			{
				if( sb == null ){
					sb = new StringBuffer( string.length()+4 );
					sb.append( string.substring(0,i) );
				}
				//encode all non basic latin characters
				sb.append("&#" + ((int)c) + ";");
			}
			else if( sb != null )
			{
				sb.append(c);
			}
		}

		return sb != null ? sb.toString() : string;
	}


}
