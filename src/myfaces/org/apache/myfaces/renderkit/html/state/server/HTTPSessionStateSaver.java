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
package net.sourceforge.myfaces.renderkit.html.state.server;

import net.sourceforge.myfaces.component.ext.UISaveState;
import net.sourceforge.myfaces.renderkit.html.state.ModelValueEntry;
import net.sourceforge.myfaces.renderkit.html.state.StateSaver;
import net.sourceforge.myfaces.renderkit.html.state.StateUtils;
import net.sourceforge.myfaces.tree.TreeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
//FIXME
public class HTTPSessionStateSaver
    implements StateSaver
{
    public void encodeState(FacesContext facesContext, int encodingType) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void init(FacesContext facesContext) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void release(FacesContext facesContext) throws IOException
    {
        throw new UnsupportedOperationException();
    }

//    private static final Log log = LogFactory.getLog(HTTPSessionStateSaver.class);
//
//    protected static final String TREE_SESSION_ATTR
//        = HTTPSessionStateSaver.class.getName() + ".TREE";
//    protected static final String LOCALE_SESSION_ATTR
//        = HTTPSessionStateSaver.class.getName() + ".LOCALE";
//    protected static final String MODEL_VALUES_COLL_SESSION_ATTR
//        = HTTPSessionStateSaver.class.getName() + ".MODEL_VALUES_COLL";
//
//    public void init(FacesContext facesContext) throws IOException
//    {
//    }
//
//    public void encodeState(FacesContext facesContext, int encodingType) throws IOException
//    {
//    }
//
//    public void release(FacesContext facesContext) throws IOException
//    {
//        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
//        HttpSession session = request.getSession(true);
//
//        Tree tree = facesContext.getTree();
//
//        //discard internal attributes ("javax.*" and "net.sourceforge.*")
//        StateUtils.discardInternalAttributes(facesContext, tree);
//
//        session.setAttribute(TREE_SESSION_ATTR, tree);
//        session.setAttribute(LOCALE_SESSION_ATTR, facesContext.getLocale());
//
//        saveModelValues(facesContext, session);
//    }
//
//
//    private void saveModelValues(FacesContext facesContext,
//                                 HttpSession session)
//    {
//        Collection modelValuesColl = null;
//
//        //look for UISaveState components:
//        Iterator it = TreeUtils.treeIterator(facesContext.getTree());
//        while (it.hasNext())
//        {
//            UIComponent comp = (UIComponent)it.next();
//            if (comp instanceof UISaveState)
//            {
//                String modelRef = ((UISaveState)comp).getValueRef();
//                if (modelRef == null)
//                {
//                    log.error("UISaveState without model reference?!");
//                }
//                else
//                {
//                    if (((UISaveState)comp).isGlobal())
//                    {
//
//                    }
//                    else
//                    {
//                        if (modelValuesColl == null)
//                        {
//                            modelValuesColl = new ArrayList();
//                        }
//                        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
//                        ValueBinding vb = af.getApplication().getValueBinding(modelRef);
//                        Object v = vb.getValue(facesContext);
//                        modelValuesColl.add(new ModelValueEntry(modelRef,
//                                                                v,
//                                                                ((UISaveState)comp).isGlobal()));
//                    }
//                }
//            }
//        }
//
//        if (modelValuesColl != null)
//        {
//            session.setAttribute(MODEL_VALUES_COLL_SESSION_ATTR,
//                                 modelValuesColl);
//        }
//    }
//
}
