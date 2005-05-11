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
package org.apache.myfaces.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.util.ClassUtils;

import javax.faces.application.FacesMessage;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @author Sean Schofield
 * @version $Revision$ $Date$
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
        FacesMessage message = getMessage(facesContext, messageId, args);
        message.setSeverity(severity);

        return message;
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

    /**
     * Uses <code>MessageFormat</code> and the supplied parameters to fill in the param placeholders in the String.
     *
     * @param locale The <code>Locale</code> to use when performing the substitution.
     * @param msgtext The original parameterized String.
     * @param params The params to fill in the String with.
     * @return The updated String.
     */
    public static String substituteParams(Locale locale, String msgtext, Object params[])
    {
        String localizedStr = null;
        if(params == null || msgtext == null)
            return msgtext;
        StringBuffer b = new StringBuffer(100);
        MessageFormat mf = new MessageFormat(msgtext);
        if(locale != null)
        {
            mf.setLocale(locale);
            b.append(mf.format(((Object) (params))));
            localizedStr = b.toString();
        }
        return localizedStr;
    }

    public static FacesMessage getMessage(String messageId, Object params[])
    {
        Locale locale = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if(context != null && context.getViewRoot() != null)
        {
            locale = context.getViewRoot().getLocale();
            if(locale == null)
                locale = Locale.getDefault();
        } else
        {
            locale = Locale.getDefault();
        }
        return getMessage(locale, messageId, params);
    }

    public static FacesMessage getMessage(Locale locale, String messageId, Object params[])
    {
        String summary = null;
        String detail = null;
        String bundleName = getApplication().getMessageBundle();
        ResourceBundle bundle = null;

        if (bundleName != null)
        {
            bundle = ResourceBundle.getBundle(bundleName, locale);
            try
            {
                summary = bundle.getString(messageId);
            }
            catch (MissingResourceException e) {}
        }

        if (summary == null)
        {
            bundle = ResourceBundle.getBundle(DEFAULT_BUNDLE, locale, ClassUtils.getCurrentLoader(bundleName));
            if(bundle == null)
            {
                throw new NullPointerException();
            }

            try
            {
                summary = bundle.getString(messageId);
            }
            catch(MissingResourceException e) { }
        }

        if(summary == null)
        {
            return null;
        }

        if (bundle == null)
        {
            throw new NullPointerException("Unable to locate ResrouceBundle: bundle is null");
        }
        summary = substituteParams(locale, summary, params);

        try
        {
            detail = substituteParams(locale, bundle.getString(messageId + DETAIL_SUFFIX), params);
        }
        catch(MissingResourceException e) { }

        return new FacesMessage(summary, detail);
    }

    public static FacesMessage getMessage(FacesContext context, String messageId)
    {
        return getMessage(context, messageId, ((Object []) (null)));
    }

    public static FacesMessage getMessage(FacesContext context, String messageId, Object params[])
    {
        if(context == null || messageId == null)
            throw new NullPointerException(" context " + context + " messageId " + messageId);
        Locale locale = null;
        if(context != null && context.getViewRoot() != null)
            locale = context.getViewRoot().getLocale();
        else
            locale = Locale.getDefault();
        if(null == locale)
            throw new NullPointerException(" locale " + locale);
        FacesMessage message = getMessage(locale, messageId, params);
        if(message != null)
        {
            return message;
        } else
        {
            locale = Locale.getDefault();
            return getMessage(locale, messageId, params);
        }
    }

    private static Application getApplication()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        if(context != null)
        {
            return FacesContext.getCurrentInstance().getApplication();
        } else
        {
            ApplicationFactory afactory = (ApplicationFactory)FactoryFinder.getFactory("javax.faces.application.ApplicationFactory");
            return afactory.getApplication();
        }
    }
}
