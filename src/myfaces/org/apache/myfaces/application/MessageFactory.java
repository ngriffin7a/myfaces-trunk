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
package net.sourceforge.myfaces.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageFactory
{
    private static final Log log = LogFactory.getLog(MessageFactory.class);

    private static final String DEFAULT_BUNDLE = "javax.faces.Messages";
    private static final String MYFACES_BUNDLE = "net.sourceforge.myfaces.resource.Messages";
    private static final String DETAIL_SUFFIX = "_detail";

    public FacesMessage getMessage(FacesContext facesContext,
                                   String messageId)
    {
        return getMessage(facesContext,
                          facesContext.getViewRoot().getLocale(),
                          messageId,
                          null);
    }

    public FacesMessage getMessage(FacesContext facesContext,
                                   String messageId,
                                   Object args[])
    {
        return getMessage(facesContext,
                          facesContext.getViewRoot().getLocale(),
                          messageId,
                          args);
    }

    public FacesMessage getMessage(FacesContext facesContext,
                                   Locale locale,
                                   String messageId,
                                   Object args[])
    {
        ResourceBundle bundle;
        String summary;
        String detail;

        bundle = getApplicationBundle(facesContext, locale);
        summary = getBundleString(bundle, messageId);
        if (summary == null)
        {
            bundle = getMyFacesBundle(locale);
            summary = getBundleString(bundle, messageId);
            if (summary == null)
            {
                bundle = getDefaultBundle(locale);
                summary = getBundleString(bundle, messageId);
                if (summary == null)
                {
                    if (log.isWarnEnabled()) log.warn("No message with id " + messageId + " found in any bundle");
                    return new FacesMessage(messageId, messageId);
                }
            }
        }

        detail = getBundleString(bundle, messageId + DETAIL_SUFFIX);
        if (detail == null)
        {
            if (log.isInfoEnabled()) log.info("No detail for message id " + messageId + " found");
            detail = summary;
        }

        if (args != null && args.length > 0)
        {
            MessageFormat format;

            format = new MessageFormat(summary, locale);
            summary = format.format(args);

            format = new MessageFormat(detail, locale);
            detail = format.format(args);
        }

        return new FacesMessage(summary, detail);
    }


    private String getBundleString(ResourceBundle bundle, String key)
    {
        try
        {
            return bundle == null ? null : bundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            return null;
        }
    }


    private ResourceBundle getApplicationBundle(FacesContext facesContext, Locale locale)
    {
        String bundleName = facesContext.getApplication().getMessageBundle();
        if (bundleName != null)
        {
            try
            {
                return ResourceBundle.getBundle(bundleName, locale, Thread.currentThread().getContextClassLoader());
            }
            catch (MissingResourceException e)
            {
                log.error("Resource bundle " + bundleName + " could not be found.");
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    private ResourceBundle getMyFacesBundle(Locale locale)
    {
        try
        {
            return ResourceBundle.getBundle(MYFACES_BUNDLE,
                                            locale,
                                            Thread.currentThread().getContextClassLoader());
        }
        catch (MissingResourceException e)
        {
            log.error("Resource bundle " + MYFACES_BUNDLE + " could not be found.");
            return null;
        }
    }

    private ResourceBundle getDefaultBundle(Locale locale)
    {
        try
        {
            return ResourceBundle.getBundle(DEFAULT_BUNDLE,
                                            locale,
                                            FacesContext.class.getClassLoader());
        }
        catch (MissingResourceException e)
        {
            log.error("Resource bundle " + DEFAULT_BUNDLE + " could not be found.");
            return null;
        }
    }

}
