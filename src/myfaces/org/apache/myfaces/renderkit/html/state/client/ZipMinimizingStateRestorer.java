/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.renderkit.html.state.client;

import net.sourceforge.myfaces.util.Base64;

import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

/**
 * StateRestorer that restores state info saved by the ZipMinimizingStateSaver.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ZipMinimizingStateRestorer
    extends MinimizingStateRestorer
{
    protected String getStateParameter(Map stateMap, String attrName)
    {
        return (String)stateMap.get(attrName);
    }

    protected Map restoreStateMap(FacesContext facesContext)
    {
        String stateParam = facesContext.getServletRequest().getParameter(ZipMinimizingStateSaver.STATE_PARAM);
        if (stateParam == null)
        {
            return Collections.EMPTY_MAP;
        }

        try
        {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(stateParam.getBytes(ZipMinimizingStateSaver.ZIP_CHARSET));
            InputStream decodedStream = Base64.getDecoder(byteStream);
            InputStream unzippedStream = new GZIPInputStream(decodedStream);

            StringBuffer buf = new StringBuffer();
            int c;
            while ((c = unzippedStream.read()) != -1)
            {
                buf.append((char)c);
            }

            unzippedStream.close();
            decodedStream.close();
            byteStream.close();

            String s = buf.toString();

            Map stateMap = new HashMap();
            StringTokenizer st = new StringTokenizer(s, "=&", true);
            while (st.hasMoreTokens())
            {
                String paramName = st.nextToken();
                st.nextToken(); // '='
                String paramValue = "";
                if (st.hasMoreTokens())
                {
                    String couldBeAmpersand = st.nextToken();
                    if (!couldBeAmpersand.equals("&"))
                    {
                        paramValue = couldBeAmpersand;
                        if (st.hasMoreTokens())
                        {
                            st.nextToken(); // '&'
                        }
                    }
                }
                paramValue = URLDecoder.decode(paramValue, ZipMinimizingStateSaver.ZIP_CHARSET);
                stateMap.put(paramName, paramValue);
            }

            return stateMap;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
