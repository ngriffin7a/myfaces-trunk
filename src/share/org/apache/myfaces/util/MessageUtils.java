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
package net.sourceforge.myfaces.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/08/17 12:15:34  manolito
 * NPE when there was no ViewRoot
 *
 * Revision 1.2  2004/07/01 22:01:13  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/03/30 16:59:47  manolito
 * MessageFactory removed, MessageUtils moved to util in src/share
 *
 */
public class MessageUtils
{
    private static final Log log = LogFactory.getLog(MessageUtils.class);

    private static final String DEFAULT_BUNDLE = "javax.faces.Messages";
    private static final String DETAIL_SUFFIX = "_detail";

    public static FacesMessage getMessage(FacesMessage.Severity severity,
                                          String messageId,
                                          Object arg1)
    {
        return getMessage(severity,
                          messageId,
                          new Object[]{arg1},
                          FacesContext.getCurrentInstance());
    }

    public static FacesMessage getMessage(FacesMessage.Severity severity,
                                          String messageId,
                                          Object[] args)
    {
        return getMessage(severity,
                          messageId,
                          args,
                          FacesContext.getCurrentInstance());
    }

    public static FacesMessage getMessage(FacesMessage.Severity severity,
                                          String messageId,
                                          Object[] args,
                                          FacesContext facesContext)
    {
        Locale locale;
        if (facesContext.getViewRoot() != null)
        {
            locale = facesContext.getViewRoot().getLocale();
        }
        else
        {
            locale = facesContext.getApplication().getDefaultLocale();
        }
        return internalGetMessage(facesContext,
                                  locale,
                                  severity,
                                  messageId,
                                  args);
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args)
    {
        addMessage(severity, messageId, args, null, FacesContext.getCurrentInstance());
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args,
                                  FacesContext facesContext)
    {
        addMessage(severity, messageId, args, null, facesContext);
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args,
                                  String forClientId)
    {
        addMessage(severity, messageId, args, forClientId, FacesContext.getCurrentInstance());
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args,
                                  String forClientId,
                                  FacesContext facesContext)
    {
        facesContext.addMessage(forClientId,
                                getMessage(severity, messageId, args, facesContext));
    }




    private static FacesMessage internalGetMessage(FacesContext facesContext,
                                                   Locale locale,
                                                   FacesMessage.Severity severity,
                                                   String messageId,
                                                   Object args[])
    {
        ResourceBundle appBundle;
        ResourceBundle defBundle;
        String summary;
        String detail;

        appBundle = getApplicationBundle(facesContext, locale);
        summary = getBundleString(appBundle, messageId);
        if (summary != null)
        {
            detail = getBundleString(appBundle, messageId + DETAIL_SUFFIX);
        }
        else
        {
            defBundle = getDefaultBundle(locale);
            summary = getBundleString(defBundle, messageId);
            if (summary != null)
            {
                detail = getBundleString(defBundle, messageId + DETAIL_SUFFIX);
            }
            else
            {
                //Try to find detail alone
                detail = getBundleString(appBundle, messageId + DETAIL_SUFFIX);
                if (detail != null)
                {
                    summary = null;
                }
                else
                {
                    detail = getBundleString(defBundle, messageId + DETAIL_SUFFIX);
                    if (detail != null)
                    {
                        summary = null;
                    }
                    else
                    {
                        //Neither detail nor summary found
                        if (log.isWarnEnabled()) log.warn("No message with id " + messageId + " found in any bundle");
                        return new FacesMessage(severity, messageId, null);
                    }
                }
            }
        }

        if (args != null && args.length > 0)
        {
            MessageFormat format;

            if (summary != null)
            {
                format = new MessageFormat(summary, locale);
                summary = format.format(args);
            }

            if (detail != null)
            {
                format = new MessageFormat(detail, locale);
                detail = format.format(args);
            }
        }

        return new FacesMessage(severity, summary, detail);
    }


    private static String getBundleString(ResourceBundle bundle, String key)
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


    private static ResourceBundle getApplicationBundle(FacesContext facesContext, Locale locale)
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

    private static ResourceBundle getDefaultBundle(Locale locale)
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
