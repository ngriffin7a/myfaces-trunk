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
package net.sourceforge.myfaces.examples.misc;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class OptionsForm
{
    private static final List AVAILABLE_LOCALES
        = Arrays.asList(new Locale[] {Locale.ENGLISH,
                                      Locale.GERMAN});

    private Locale _locale = null;

    public String getLanguage()
    {
        return _locale != null
                ? _locale.getLanguage()
                : FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
    }

    public void setLanguage(String language)
    {
        _locale = new Locale(language);
    }

    public Locale getLocale()
    {
        return _locale;
    }

    public Iterator getAvailableLanguages()
    {
        final Iterator it = AVAILABLE_LOCALES.iterator();
        return new Iterator() {
            public boolean hasNext()
            {
                return it.hasNext();
            }

            public Object next()
            {
                Locale locale = (Locale)it.next();
                String language = locale.getDisplayLanguage(locale);
                return new SelectItem(locale.getLanguage(), language, language);
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }


}
