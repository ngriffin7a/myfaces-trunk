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
package net.sourceforge.myfaces.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageConfig
    implements Config
{
    private static final Log log = LogFactory.getLog(MessageConfig.class);

    private static final String SEVERITY_INFO = "INFO";
    private static final String SEVERITY_WARN = "WARN";
    private static final String SEVERITY_ERROR = "ERROR";
    private static final String SEVERITY_FATAL = "FATAL";

    private String _messageId;
    private String _messageClass;
    private FacesMessage.Severity _severity = FacesMessage.SEVERITY_ERROR;
    private Map _summaryMap;
    private Map _detailMap;

    public String getMessageId()
    {
        return _messageId;
    }

    public void setMessageId(String messageId)
    {
        _messageId = messageId;
    }

    public String getMessageClass()
    {
        return _messageClass;
    }

    public void setMessageClass(String messageClass)
    {
        _messageClass = messageClass;
    }


    public void setSeverity(String severity)
    {
        if (severity.equals(SEVERITY_INFO))
        {
            _severity = FacesMessage.SEVERITY_INFO;
        }
        else if (severity.equals(SEVERITY_WARN))
        {
            _severity = FacesMessage.SEVERITY_WARN;
        }
        else if (severity.equals(SEVERITY_ERROR))
        {
            _severity = FacesMessage.SEVERITY_ERROR;
        }
        else if (severity.equals(SEVERITY_FATAL))
        {
            _severity = FacesMessage.SEVERITY_FATAL;
        }
        else
        {
            throw new FacesException("Illegal severity '" + severity + "'.");
        }
    }

    public FacesMessage.Severity getSeverity()
    {
        return _severity;
    }

    public void setSeverity(FacesMessage.Severity severity)
    {
        _severity = severity;
    }


    public void addSummary(String lang, String summary)
    {
        getSummaryMap().put(lang, summary);
    }

    public Map getSummaryMap()
    {
        if (_summaryMap == null)
        {
            _summaryMap = new HashMap();
        }
        return _summaryMap;
    }

    public String getSummary(String language)
    {
        String s = (String)getSummaryMap().get(language);
        if (s == null)
        {
            log.warn("No summary in language '" + language + "' for message '" + _messageId + "' defined.");
            return _messageId;
        }
        return s;
    }


    public void addDetail(String lang, String detail)
    {
        getDetailMap().put(lang, detail);
    }

    public Map getDetailMap()
    {
        if (_detailMap == null)
        {
            _detailMap = new HashMap();
        }
        return _detailMap;
    }

    public String getDetail(String language)
    {
        String s = (String)getDetailMap().get(language);
        if (s == null)
        {
            log.warn("No detail in language '" + language + "' for message '" + _messageId + "' defined.");
            return _messageId;
        }
        return s;
    }


    public FacesMessage getMessage(FacesContext facesContext, Object[] args)
    {
        //FIXME
        /*
        if (_messageClass != null && !_messageClass.equals(MessageImpl.class.getName()))
        {
            return (FacesMessage)ConfigUtil.newInstance(_messageClass);
        }
        */

        //FIXME
        //String language = facesContext.getLocale().getLanguage();
        String language = null;

        String summary = (String)getSummaryMap().get(language);
        String detail = (String)getDetailMap().get(language);
        if (summary == null && detail == null)
        {
            //FIXME
            //String defaultLang = MyFacesConfig.getDefaultLanguage((ServletContext)facesContext.getExternalContext().getContext());
            String defaultLang = null;
            summary = getSummary(defaultLang);
            detail  = getDetail(defaultLang);
        }

        if (args != null)
        {
            //FIXME
            /*
            MessageFormat mf = new MessageFormat(summary, facesContext.getLocale());
            summary = mf.format(args);

            mf = new MessageFormat(detail, facesContext.getLocale());
            detail = mf.format(args);
            */
        }

        //FIXME
        //return new MessageImpl(_severity, summary, detail);
        return null;
    }

}
