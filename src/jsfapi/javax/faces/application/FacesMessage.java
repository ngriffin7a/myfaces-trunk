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
package javax.faces.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENT ME!
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesMessage
        implements java.io.Serializable
{
    //private static final Log log = LogFactory.getLog(FacesMessage.class);

    public static final java.lang.String FACES_MESSAGES = "javax.faces.Messages";

    public static final FacesMessage.Severity SEVERITY_INFO = new Severity("Info", 1);
    public static final FacesMessage.Severity SEVERITY_WARN = new Severity("Warn", 2);
    public static final FacesMessage.Severity SEVERITY_ERROR = new Severity("Error", 3);
    public static final FacesMessage.Severity SEVERITY_FATAL = new Severity("Fatal", 4);
    public static final java.util.List VALUES;
    public static final java.util.Map VALUES_MAP;
    static
    {
        Map map = new HashMap(7);
        map.put(SEVERITY_INFO.toString(), SEVERITY_INFO);
        map.put(SEVERITY_WARN.toString(), SEVERITY_WARN);
        map.put(SEVERITY_ERROR.toString(), SEVERITY_ERROR);
        map.put(SEVERITY_FATAL.toString(), SEVERITY_FATAL);
        VALUES = Collections.unmodifiableList(new ArrayList(map.values()));
        VALUES_MAP = Collections.unmodifiableMap(map);
    }

    private FacesMessage.Severity _severity;
    private String _summary;
    private String _detail;

    public FacesMessage()
    {
    }

    public FacesMessage(String summary)
    {
        _summary = summary;
    }

    public FacesMessage(String summary, String detail)
    {
        _summary = summary;
        _detail = detail;
    }

    public FacesMessage(FacesMessage.Severity severity,
                        String summary,
                        String detail)
    {
        _severity = severity;
        _summary = summary;
        _detail = detail;
    }

    public FacesMessage.Severity getSeverity()
    {
        return _severity;
    }

    public void setSeverity(FacesMessage.Severity severity)
    {
        _severity = severity;
    }

    public String getSummary()
    {
        return _summary;
    }

    public void setSummary(String summary)
    {
        _summary = summary;
    }

    public String getDetail()
    {
        return _detail;
    }

    public void setDetail(String detail)
    {
        _detail = detail;
    }


    public static class Severity
            implements Comparable
    {
        private String _name;
        private int _ordinal;

        private Severity(String name, int ordinal)
        {
            _name = name;
            _ordinal = ordinal;
        }

        public int getOrdinal()
        {
            return _ordinal;
        }

        public String toString()
        {
            return _name;
        }

        public int compareTo(Object o)
        {
            if (!(o instanceof Severity))
            {
                throw new IllegalArgumentException(o.getClass().getName());
            }
            return getOrdinal() - ((Severity)o).getOrdinal();
        }
    }

}
