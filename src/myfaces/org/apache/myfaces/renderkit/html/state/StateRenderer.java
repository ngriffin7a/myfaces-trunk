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
package net.sourceforge.myfaces.renderkit.html.state;

import net.sourceforge.myfaces.renderkit.html.FormRenderer;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateRenderer
    extends HTMLRenderer
{
    public static final String TYPE = "StateRenderer";

    protected static final String TREE_ID_REQUEST_PARAM = "_tId";
    protected static final String LOCALE_REQUEST_PARAM = "_locale";
    protected static final String LOCALE_REQUEST_PARAM_DELIMITER = "_";

    public static final String BODY_CONTENT_REQUEST_ATTR
        = StateRenderer.class.getName() + ".BODY_CONTENT";

    public static final String TRANSIENT_ATTR
        = StateRenderer.class.getName() + ".TRANSIENT";

    protected static final String LISTENER_TYPE_ACTION = "Action";
    protected static final String LISTENER_TYPE_VALUE_CHANGED = "ValueChanged";

    static final String NULL_DUMMY_VALUE = "__NULL__";

    protected StateSaver _stateSaver;
    protected StateRestorer _stateRestorer;

    public StateRenderer()
    {
        init();
    }

    protected void init()
    {
        _stateSaver = new StateSaver();
        _stateRestorer = new StateRestorer();
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return false;
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return false;
    }


    /**
     * Method decode is called directly from the ReconstituteComponentTreePhase with a null
     * UIComponent parameter.
     * Additionally, Renderers can call this method with a component argument to explicitly
     * restore the state of a single component. e.g. The NavigationItemRenderer calls this
     * method for each UINavigationItem to restore state of the Navigation independent of
     * the tree restoring.
     * @param facesContext
     * @param comp  component that should be restored or null if full tree should be restored
     * @throws java.io.IOException
     */
    public void decode(FacesContext facesContext, UIComponent comp) throws IOException
    {
        if (comp == null)
        {
            _stateRestorer.restoreState(facesContext);
        }
        else
        {
            _stateRestorer.restoreComponentState(facesContext, comp);
        }
    }

    /**
     * Called directly by ViewHandlerJspImpl
     * @param facesContext
     * @param dummy
     * @throws java.io.IOException
     */
    public void encodeBegin(FacesContext facesContext, UIComponent dummy) throws IOException
    {
        _stateSaver.init(facesContext);
    }

    /**
     * Called directly by FormRenderer or HyperlinkRenderer
     * @param facesContext
     * @param commandComponent
     * @throws java.io.IOException
     */
    public void encodeChildren(FacesContext facesContext, UIComponent commandComponent)
        throws IOException
    {
        String commandRendererType = commandComponent.getRendererType();
        if (commandRendererType.equals(FormRenderer.TYPE))
        {
            _stateSaver.encodeState(facesContext, StateSaver.HIDDEN_INPUTS_ENCODING);
        }
        else
        {
            _stateSaver.encodeState(facesContext, StateSaver.URL_ENCODING);
        }
    }

    /**
     * Called directly by doAfterBody() in UseFacesTag
     */
    public void encodeEnd(FacesContext facesContext, UIComponent none) throws IOException
    {
        _stateSaver.release(facesContext);
    }

}
