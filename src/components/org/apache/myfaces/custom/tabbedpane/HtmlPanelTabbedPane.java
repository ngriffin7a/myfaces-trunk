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
package net.sourceforge.myfaces.custom.tabbedpane;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.5  2004/07/10 17:11:02  o_rossmueller
 * added attributes activeTabStyleClass, inactiveTabStyleClass, activeSubStyleClass, inactiveSubStyleClass, tagContentStyleClass to overwrite style attributes using css
 *
 * Revision 1.4  2004/07/01 21:53:05  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/04/16 15:13:31  manolito
 * validator attribute support and MethodBinding invoke exception handling fixed
 *
 * Revision 1.2  2004/04/06 15:36:31  manolito
 * go to render phase after tab switching
 *
 */
public class HtmlPanelTabbedPane
        extends HtmlPanelGroup
{
    //private static final Log log = LogFactory.getLog(HtmlPanelTabbedPane.class);

    private MethodBinding _tabChangeListener = null;
    private String _activeTabStyleClass;
    private String _inactiveTabStyleClass;
    private String _activeSubStyleClass;
    private String _inactiveSubStyleClass;
    private String _tabContentStyleClass;


    public String getActiveTabStyleClass()
    {
        return _activeTabStyleClass;
    }


    public void setActiveTabStyleClass(String activeTabStyleClass)
    {
        this._activeTabStyleClass = activeTabStyleClass;
    }


    public String getInactiveTabStyleClass()
    {
        return _inactiveTabStyleClass;
    }


    public void setInactiveTabStyleClass(String inactiveTabStyleClass)
    {
        this._inactiveTabStyleClass = inactiveTabStyleClass;
    }


    public String getActiveSubStyleClass()
    {
        return _activeSubStyleClass;
    }


    public void setActiveSubStyleClass(String _activeSubStyleClass)
    {
        this._activeSubStyleClass = _activeSubStyleClass;
    }


    public String getInactiveSubStyleClass()
    {
        return _inactiveSubStyleClass;
    }


    public void setInactiveSubStyleClass(String _inactiveSubStyleClass)
    {
        this._inactiveSubStyleClass = _inactiveSubStyleClass;
    }


    public String getTabContentStyleClass()
    {
        return _tabContentStyleClass;
    }


    public void setTabContentStyleClass(String _tabContentStyleClass)
    {
        this._tabContentStyleClass = _tabContentStyleClass;
    }

    //TODO: additional HTML Table attributes (see HtmlPanelTabbedPaneTag)

    public void addTabChangeListener(TabChangeListener listener)
    {
        addFacesListener(listener);
    }

    public void removeTabChangeListener(TabChangeListener listener)
    {
        removeFacesListener(listener);
    }

    public MethodBinding getTabChangeListener()
    {
        return _tabChangeListener;
    }

    public void setTabChangeListener(MethodBinding tabChangeListener)
    {
        _tabChangeListener = tabChangeListener;
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof TabChangeEvent)
        {
            TabChangeEvent tabChangeEvent = (TabChangeEvent)event;
            if (tabChangeEvent.getComponent() == this)
            {
                setSelectedIndex(tabChangeEvent.getNewTabIndex());
                getFacesContext().renderResponse();
            }
        }
        super.broadcast(event);

        MethodBinding tabChangeListenerBinding = getTabChangeListener();
        if (tabChangeListenerBinding != null)
        {
            try
            {
                tabChangeListenerBinding.invoke(getFacesContext(), new Object[]{event});
            }
            catch (EvaluationException e)
            {
                Throwable cause = e.getCause();
                if (cause != null && cause instanceof AbortProcessingException)
                {
                    throw (AbortProcessingException)cause;
                }
                else
                {
                    throw e;
                }
            }
        }
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlPanelTabbedPane";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.TabbedPane";

    private Integer _selectedIndex = null;
    private String _bgcolor = null;

    public HtmlPanelTabbedPane()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        _selectedIndex = new Integer(selectedIndex);
    }

    public int getSelectedIndex()
    {
        if (_selectedIndex != null) return _selectedIndex.intValue();
        ValueBinding vb = getValueBinding("selectedIndex");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : 0;
    }

    public void setBgcolor(String bgcolor)
    {
        _bgcolor = bgcolor;
    }

    public String getBgcolor()
    {
        if (_bgcolor != null) return _bgcolor;
        ValueBinding vb = getValueBinding("bgcolor");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[9];
        values[0] = super.saveState(context);
        values[1] = _selectedIndex;
        values[2] = _bgcolor;
        values[3] = saveAttachedState(context, _tabChangeListener);
        values[4] = _activeTabStyleClass;
        values[5] = _activeSubStyleClass;
        values[6] = _inactiveTabStyleClass;
        values[7] = _inactiveSubStyleClass;
        values[8] = _tabContentStyleClass;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _selectedIndex = (Integer)values[1];
        _bgcolor = (String)values[2];
        _tabChangeListener = (MethodBinding)restoreAttachedState(context, values[3]);
        _activeTabStyleClass = (String) values[4];
        _activeSubStyleClass = (String) values[5];
        _inactiveTabStyleClass = (String) values[6];
        _inactiveSubStyleClass = (String) values[7];
        _tabContentStyleClass = (String) values[8];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
