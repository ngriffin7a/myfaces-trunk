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
package net.sourceforge.myfaces.context;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageImpl;
import javax.faces.context.MessageResources;
import java.text.MessageFormat;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * Implementation of a javax.faces.context.MessageResources (see Spec. JSF.5.6).
 * This implementation uses a ResourceBundle with resource strings in a special format.
 * For each key the specified Bundle must return a String of the following format:
 * <severity>|<summary>|<detail>
 * Possible values for severity are: "INFO", "WARN", "ERROR" and "FATAL"
 * Example: "ERROR|Input Error|The specified value is not valid."
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageResourcesResourceBundleImpl
    extends MessageResources
{
    private static final String SEVERITY_INFO = "INFO";
    private static final String SEVERITY_WARN = "WARN";
    private static final String SEVERITY_ERROR = "ERROR";
    private static final String SEVERITY_FATAL = "FATAL";

    private String _resourceBundleName;

    public MessageResourcesResourceBundleImpl(String resourceBundleName)
    {
        _resourceBundleName = resourceBundleName;
    }


    //Implementation

    public Message getMessage(FacesContext facesContext, String messageId, Object[] params)
    {
        ResourceBundle bundle = ResourceBundle.getBundle(_resourceBundleName,
                                                         facesContext.getLocale());
        String s = bundle.getString(messageId);
        StringTokenizer st = new StringTokenizer(s, "|");
        String token;

        try
        {
            int severity;
            token = st.nextToken().trim();
            if (token.equals(SEVERITY_INFO))
            {
                severity = Message.SEVERITY_INFO;
            }
            else if (token.equals(SEVERITY_WARN))
            {
                severity = Message.SEVERITY_WARN;
            }
            else if (token.equals(SEVERITY_ERROR))
            {
                severity = Message.SEVERITY_ERROR;
            }
            else if (token.equals(SEVERITY_FATAL))
            {
                severity = Message.SEVERITY_FATAL;
            }
            else
            {
                throw new FacesException("Error in MessageResourcesResourceBundle properties file: Unknown severity \"" + token + "\".");
            }

            String summary = st.nextToken();
            //remove leading whitespace
            if (summary.length() > 0 &&
                Character.isWhitespace(summary.charAt(0)))
            {
                summary = summary.substring(1);
            }
            //remove ending whitespace
            if (summary.length() > 0 &&
                Character.isWhitespace(summary.charAt(summary.length() - 1)))
            {
                summary = summary.substring(0, summary.length() - 1);
            }

            String detail;
            if (st.hasMoreTokens())
            {
                detail = st.nextToken().trim();
                //remove leading whitespace
                if (detail.length() > 0 &&
                    Character.isWhitespace(detail.charAt(0)))
                {
                    detail = detail.substring(1);
                }
            }
            else
            {
                detail = "";
            }

            if (params != null)
            {
                MessageFormat mf = new MessageFormat(summary, facesContext.getLocale());
                summary = mf.format(params);

                mf = new MessageFormat(detail, facesContext.getLocale());
                detail = mf.format(params);
            }

            return new MessageImpl(severity, summary, detail);
        }
        catch (NoSuchElementException e)
        {
            throw new FacesException("Error in MessageResourcesResourceBundle properties file.", e);
        }
    }

    public Message getMessage(FacesContext facesContext, String messageId)
    {
        return getMessage(facesContext, messageId, null);
    }

    public Message getMessage(FacesContext facesContext, String messageId, Object p)
    {
        return getMessage(facesContext, messageId,
                          new Object[] {p});
    }

    public Message getMessage(FacesContext facesContext, String messageId, Object p1, Object p2)
    {
        return getMessage(facesContext, messageId,
                          new Object[]{p1, p2});
    }

    public Message getMessage(FacesContext facesContext, String messageId, Object p1, Object p2, Object p3)
    {
        return getMessage(facesContext, messageId,
                          new Object[]{p1, p2, p3});
    }

    public Message getMessage(FacesContext facesContext, String messageId, Object p1, Object p2, Object p3, Object p4)
    {
        return getMessage(facesContext, messageId,
                          new Object[]{p1, p2, p3, p4});
    }

}
