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
package net.sourceforge.myfaces.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * RenderKitFactory implementation as defined in Spec. JSF.7.3
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.12  2004/05/18 07:13:32  manolito
 * X-checked against specs: no more synchronization needed, allow replacement of renderKit, no excepetion on unknown id
 *
 */
public class RenderKitFactoryImpl
    extends RenderKitFactory
{
    private static final Log log = LogFactory.getLog(RenderKitFactoryImpl.class);

    private static Map _renderkits = new HashMap();

    public RenderKitFactoryImpl()
    {
    }


    public void addRenderKit(String renderKitId, RenderKit renderKit)
    {
        if (renderKitId == null) throw new NullPointerException("renderKitId");
        if (renderKit == null) throw new NullPointerException("renderKit");
        if (log.isInfoEnabled())
        {
            if (_renderkits.containsKey(renderKitId))
            {
                log.info("RenderKit with renderKitId '" + renderKitId + "' was replaced.");
            }
        }
        _renderkits.put(renderKitId, renderKit);
    }


    public RenderKit getRenderKit(FacesContext context, String renderKitId)
            throws FacesException
    {
        if (renderKitId == null) throw new NullPointerException("renderKitId");
        RenderKit renderkit = (RenderKit)_renderkits.get(renderKitId);
        if (renderkit == null)
        {
            //throw new IllegalArgumentException("Unknown RenderKit '" + renderKitId + "'.");
            //JSF Spec API Doc says:
            // "If there is no registered RenderKit for the specified identifier, return null"
            // vs "IllegalArgumentException - if no RenderKit instance can be returned for the specified identifier"
            //First sentence is more precise, so we just log a warning
            log.warn("Unknown RenderKit '" + renderKitId + "'.");
        }
        return renderkit;
    }


    public Iterator getRenderKitIds()
    {
        return _renderkits.keySet().iterator();
    }
}
