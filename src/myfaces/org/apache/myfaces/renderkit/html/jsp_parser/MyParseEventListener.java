/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
package net.sourceforge.myfaces.renderkit.html.jsp_parser;

import net.sourceforge.myfaces.component.MyFacesComponent;
import net.sourceforge.myfaces.component.UICommand;
import net.sourceforge.myfaces.renderkit.html.state.TreeCopier;
import net.sourceforge.myfaces.taglib.MyFacesTagExtension;
import org.apache.jasper.Constants;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.compiler.*;
import org.xml.sax.Attributes;

import javax.faces.component.UIComponent;
import javax.faces.tree.Tree;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class MyParseEventListener
        implements ParseEventListener
{
    private TagLibraries _tagLibraries = new MyTagLibraries();
    private JspTreeParser _parser;
    private JspCompilationContext _ctxt;
    private UIComponent _currentComponent;
    private Map _beanClasses;

    public MyParseEventListener(JspTreeParser parser,
                                JspCompilationContext ctxt,
                                Tree tree,
                                Map beanClasses)
    {
        _parser = parser;
        _ctxt = ctxt;
        _currentComponent = tree.getRoot();
        _beanClasses = beanClasses;
    }

    public void beginPageProcessing() throws JasperException
    {
        //System.out.println("MyParseEventListener.beginPageProcessing");
    }

    public void endPageProcessing() throws JasperException
    {
        //System.out.println("MyParseEventListener.endPageProcessing");
    }

    /*
     * Custom tag support
     */
    public TagLibraries getTagLibraries()
    {
        //System.out.println("MyParseEventListener.getTagLibraries");
        return _tagLibraries;
    }

    public void handleBean(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        handleBean(attrs);
    }

    public void handleBean(Mark start, Mark stop, Attributes attrs, boolean isXml)
            throws JasperException
    {
        handleBean(attrs);
    }

    public void handleBeanEnd(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleBeanEnd");
    }

    public void handleCharData(Mark start, Mark stop, char[] chars)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleCharData");
    }

    public void handleComment(Mark start, Mark stop, char[] text) throws JasperException
    {
        //System.out.println("MyParseEventListener.handleComment");
    }

    public void handleDeclaration(Mark start, Mark stop, Attributes attrs, char[] text)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleDeclaration");
    }

    public void handleDirective(String directive,
                                Mark start, Mark stop,
                                Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleDirective");

        if (directive.equals("taglib"))
        {
            String uri = attrs.getValue("uri");
            String prefix = attrs.getValue("prefix");

            TagLibraryInfo tl = null;

            /*
            String[] location = _ctxt.getTldLocation(uri);
            if (location == null)
            {
            */
                tl = new TagLibraryInfoImpl(_ctxt, prefix, uri);
            /*
            }
            else
            {
                tl = new TagLibraryInfoImpl(_ctxt, prefix, uri, location);
            }
            */
            _tagLibraries.addTagLibrary(prefix, tl);
        }

        else if (directive.equals("include"))
        {
            String file = attrs.getValue("file");
            if (file == null)
            {
                throw new CompileException(start,
                                           Constants.getString("jsp.error.include.missing.file"));
            }

            _parser.parseFile(file);
        }

    }

    public void handleExpression(Mark start, Mark stop, Attributes attrs, char[] text)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleExpression");
    }

    public void handleForward(Mark start, Mark stop, Attributes attrs, Hashtable param)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleForward");
    }

    public void handleForward(Mark start, Mark stop, Attributes attrs, Hashtable param, boolean isXml)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleForward");
    }

    public void handleGetProperty(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleGetProperty");
    }

    public void handleInclude(Mark start, Mark stop, Attributes attrs, Hashtable param)
            throws JasperException
    {
        System.out.println("MyParseEventListener.handleInclude");
    }

    public void handleInclude(Mark start, Mark stop, Attributes attrs, Hashtable param, boolean isXml)
            throws JasperException
    {
        System.out.println("MyParseEventListener.handleInclude");
    }

    public void handleJspCdata(Mark start, Mark stop, char[] data)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleJspCdata");
    }

    public void handlePlugin(Mark start, Mark stop, Attributes attrs, Hashtable param,
                             String fallback)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handlePlugin");
    }

    public void handlePlugin(Mark start, Mark stop, Attributes attrs, Hashtable param,
                             String fallback, boolean isXml)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handlePlugin");
    }

    public void handleRootBegin(Attributes attrs) throws JasperException
    {
        //System.out.println("MyParseEventListener.handleRootBegin");
    }

    public void handleRootEnd()
    {
        //System.out.println("MyParseEventListener.handleRootEnd");
    }

    public void handleScriptlet(Mark start, Mark stop, Attributes attrs, char[] text)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleScriptlet");
    }

    public void handleSetProperty(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleSetProperty");
    }

    public void handleSetProperty(Mark start, Mark stop, Attributes attrs,
                                  boolean isXml)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleSetProperty");
    }

    /*
     * start: is either the start position at "<" if content type is JSP or empty, or
     *        is the start of the body after the "/>" if content type is tag dependent
     * stop: can be null if the body contained JSP tags...
     */
    public void handleTagBegin(Mark start, Mark stop, Attributes attrs, String prefix, String shortTagName,
                               TagLibraryInfo tli, TagInfo ti, boolean hasBody)
            throws JasperException
    {
        handleTagBegin(prefix, shortTagName, attrs, tli, ti);
    }

    public void handleTagBegin(Mark start, Mark stop, Attributes attrs, String prefix, String shortTagName,
                               TagLibraryInfo tli, TagInfo ti, boolean hasBody, boolean isXml)
            throws JasperException
    {
        handleTagBegin(prefix, shortTagName, attrs, tli, ti);
    }

    public void handleTagEnd(Mark start, Mark stop, String prefix, String shortTagName,
                             Attributes attrs, TagLibraryInfo tli, TagInfo ti, boolean hasBody)
            throws JasperException
    {
        handleTagEnd(prefix, shortTagName, attrs, tli, ti);
    }

    public void handleUninterpretedTagBegin(Mark start, Mark stop,
                                            String rawName, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleUninterpretedTagBegin");
    }

    public void handleUninterpretedTagEnd(Mark start, Mark stop,
                                          String rawName, char[] data)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleUninterpretedTagEnd");
    }

    public void setDefault(boolean b)
    {
        //System.out.println("MyParseEventListener.setDefault");
    }

    public void setReader(JspReader reader)
    {
        //System.out.println("MyParseEventListener.setReader");
    }

    public void setTemplateInfo(Mark start, Mark stop)
    {
        //System.out.println("MyParseEventListener.setTemplateInfo");
    }



    private HashMap _tagClasses = new HashMap();
    private static final Object NULL_DUMMY = new Object();
    private MyFacesTagExtension getMyFacesTagExtension(TagInfo ti)
    {
        Object obj = _tagClasses.get(ti.getTagClassName());
        if (obj == NULL_DUMMY)
        {
            return null;
        }
        else if (obj != null)
        {
            return (MyFacesTagExtension)obj;
        }

        Class c;
        try
        {
            c = Class.forName(ti.getTagClassName());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Class for tag " + ti.getTagName() + " not found!", e);
        }

        if (!MyFacesTagExtension.class.isAssignableFrom(c))
        {
            _tagClasses.put(ti.getTagClassName(), NULL_DUMMY);
            return null;
        }

        try
        {
            obj = c.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

        _tagClasses.put(ti.getTagClassName(), obj);
        return (MyFacesTagExtension)obj;
    }


    private void handleTagBegin(String prefix, String shortTagName,
                                Attributes attrs, TagLibraryInfo tli, TagInfo ti)
    {
        MyFacesTagExtension tag = getMyFacesTagExtension(ti);
        if (tag != null)
        {
            //Tag is instanceof MyFacesTagExtension
            UIComponent comp = tag.createComponent();
            if (comp != null)
            {
                comp.setAttribute(TreeCopier.CREATOR_TAG_ATTR, tag);

                String rendererType = tag.getRendererType();
                if (rendererType != null)
                {
                    comp.setRendererType(rendererType);
                }

                TagAttributeInfo[] attrInfos = ti.getAttributes();
                for (int i = 0; i < attrInfos.length; i++)
                {
                    String attrName = attrInfos[i].getName();
                    Object attrValue = attrs.getValue(attrName);

                    if (attrInfos[i].canBeRequestTime() &&
                        attrValue != null &&
                        ((String)attrValue).trim().startsWith("<%"))
                    {
                        //Request time value --> ignore
                        continue;
                    }

                    if (attrName.equals(MyFacesComponent.ID_ATTR))
                    {
                        comp.setComponentId((String)attrValue);
                    }
                    else if (attrName.equals(MyFacesComponent.MODEL_REFERENCE_ATTR))
                    {
                        comp.setModelReference((String)attrValue);
                    }
                    else if (attrName.equals(MyFacesComponent.RENDERER_TYPE_ATTR))
                    {
                        comp.setRendererType((String)attrValue);
                    }
                    else if (attrName.equals(MyFacesComponent.VALUE_ATTR))
                    {
                        //Will be converted to Object under current FacesContext later in the TreeCopier
                        comp.setAttribute(TreeCopier.HARDCODED_VALUE_ATTR, attrValue);
                    }
                    else if (attrName.equals(UICommand.COMMAND_NAME_ATTR) &&
                             comp.getComponentType().equals(UICommand.TYPE))
                    {
                        ((javax.faces.component.UICommand)comp).setCommandName((String)attrValue);
                    }
                    //else if (attrName.equals(???)) TODO: More known attributes?
                    else
                    {
                        comp.setAttribute(attrName, attrValue);
                    }

                }
                _currentComponent.addChild(comp);
                _currentComponent = comp;
            }
        }
    }

    private void handleTagEnd(String prefix, String shortTagName,
                              Attributes attrs, TagLibraryInfo tli, TagInfo ti)
    {
        if (getMyFacesTagExtension(ti) != null)
        {
            _currentComponent = _currentComponent.getParent();
        }
    }

    private void handleBean(Attributes attrs)
    {
        String id = attrs.getValue("id");
        if (id == null)
        {
            throw new IllegalArgumentException("No id attribute!");
        }

        String cl = attrs.getValue("class");
        if (cl == null)
        {
            throw new IllegalArgumentException("No class attribute!");
        }

        //TODO: what about scope, do we need to map application (servlet context) beans?
        /*
        String scope = attrs.getValue("scope");
        if (scope == null)
        {
            throw new IllegalArgumentException("No scope attribute!");
        }
        */

        _beanClasses.put(id, cl);
    }


}
