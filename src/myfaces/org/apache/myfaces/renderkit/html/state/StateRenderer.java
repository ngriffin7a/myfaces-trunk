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

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.renderkit.html.FormRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.state.client.*;
import net.sourceforge.myfaces.renderkit.html.state.server.HTTPSessionStateRestorer;
import net.sourceforge.myfaces.renderkit.html.state.server.HTTPSessionStateSaver;
import net.sourceforge.myfaces.util.FacesUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateRenderer
    extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(StateRenderer.class);

    public static final String TYPE = "State";

    protected StateSaver _stateSaver;
    protected StateRestorer _stateRestorer;

    public StateRenderer()
    {
    }

    private void init(FacesContext facesContext)
    {
        ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
        int stateSavingMode = MyFacesConfig.getStateSavingMode(servletContext);

        switch (stateSavingMode)
        {
            case MyFacesConfig.STATE_SAVING_MODE__SERVER_SESSION:
                _stateSaver = new HTTPSessionStateSaver();
                _stateRestorer = new HTTPSessionStateRestorer();
                break;

            case MyFacesConfig.STATE_SAVING_MODE__CLIENT_SERIALIZED:
                _stateSaver = new SerializingStateSaver();
                _stateRestorer = new SerializingStateRestorer();
                break;

            case MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED:
                _stateSaver = new MinimizingStateSaver();
                _stateRestorer = new MinimizingStateRestorer();
                break;

            case MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED_ZIPPED:
                _stateSaver = new ZipMinimizingStateSaver();
                _stateRestorer = new ZipMinimizingStateRestorer();
                break;

            default:
                throw new IllegalArgumentException("Unknown state saving mode " + stateSavingMode);
        }
    }

    public String getRendererType()
    {
        return TYPE;
    }

    /*
    public boolean supportsComponentType(String s)
    {
        return false;
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return false;
    }

    protected void initAttributeDescriptors()
    {
    }
    */


    /**
     * Method decode is called directly from the ReconstituteComponentTreePhase.
     * The underlying StateRestorer is added as an request attribute named
     * {@link StateRestorer#STATE_RESTORER_REQUEST_ATTR}.
     *
     * @param facesContext
     * @param comp  component that should be restored or null if full tree should be restored
     * @throws java.io.IOException
     */
    public void decode(FacesContext facesContext, UIComponent comp) throws IOException
    {
        if (_stateSaver == null) init(facesContext);
        _stateRestorer.restoreState(facesContext);
        if (log.isTraceEnabled()) LogUtil.logTree(log, "Current tree after restoring state");

        FacesUtils.getRequestMap(facesContext).put(StateRestorer.STATE_RESTORER_REQUEST_ATTR, _stateRestorer);
    }

    /**
     * Called directly by ViewHandlerJspImpl.
     * {@link StateSaver#init} method of the underlying StateSaver is called.
     *
     * @param facesContext
     * @param dummy
     * @throws java.io.IOException
     */
    public void encodeBegin(FacesContext facesContext, UIComponent dummy) throws IOException
    {
        if (_stateSaver == null) init(facesContext);
        _stateSaver.init(facesContext);
    }

    /**
     * Called directly by FormRenderer or HyperlinkRenderer
     * {@link StateSaver#encodeState} method of the underlying StateSaver is called.
     *
     * @param facesContext
     * @param commandComponent
     * @throws java.io.IOException
     */
    public void encodeChildren(FacesContext facesContext, UIComponent commandComponent)
        throws IOException
    {
        if (_stateSaver == null) init(facesContext);
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
     * {@link StateSaver#release} method of the underlying StateSaver is called.
     */
    public void encodeEnd(FacesContext facesContext, UIComponent none) throws IOException
    {
        if (_stateSaver == null) init(facesContext);
        _stateSaver.release(facesContext);
        if (log.isTraceEnabled()) LogUtil.logTree(log, "Current tree after saving state");
    }

}
