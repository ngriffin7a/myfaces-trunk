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
package net.sourceforge.myfaces.renderkit.html.state.client;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * StateRestorer that restores state info saved by the SerializingStateSaver.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
//FIXME
public class SerializingStateRestorer
    extends ClientStateRestorer
{
    public UIViewRoot getPreviousTree(FacesContext facesContext)
    {
        throw new UnsupportedOperationException();
    }

    public void restoreState(FacesContext facesContext) throws IOException
    {
        throw new UnsupportedOperationException();
    }

//    private static final Log log = LogFactory.getLog(SerializingStateRestorer.class);
//
//    protected static final String RESTORED_TREE_CONTEXT_ATTR
//        = SerializingStateSaver.class.getName() + ".RESTORED_TREE";
//
//    public Tree getPreviousTree(FacesContext facesContext)
//    {
//        ExternalContext extContext = facesContext.getExternalContext();
//        Map requestMap = extContext.getRequestMap();
//        Tree restoredTree
//            = (Tree)requestMap.get(RESTORED_TREE_CONTEXT_ATTR);
//        if (restoredTree == null)
//        {
//            String serializedTree
//                = (String)extContext.getRequestParameterMap().get(SerializingStateSaver.TREE_REQUEST_PARAM);
//            if (serializedTree == null)
//            {
//                //nothing to restore
//                return null;
//            }
//
//            restoredTree = unzipTree(serializedTree);
//            requestMap.put(RESTORED_TREE_CONTEXT_ATTR, restoredTree);
//        }
//        return restoredTree;
//    }
//
//    public void restoreState(FacesContext facesContext) throws IOException
//    {
//        Tree savedTree = getPreviousTree(facesContext);
//        if (savedTree == null)
//        {
//            return;
//        }
//
//        //restore locale
//        Locale savedlocale
//            = (Locale)savedTree.getRoot()
//                    .getAttribute(SerializingStateSaver.CURRENT_LOCALE_ATTR);
//        if (savedlocale == null)
//        {
//            log.warn("No locale in saved tree?!");
//        }
//        else
//        {
//            facesContext.setLocale(savedlocale);
//            savedTree.getRoot().setAttribute(SerializingStateSaver.CURRENT_LOCALE_ATTR,
//                                             null);
//        }
//
//        Tree currentTree = facesContext.getTree();
//        if (savedTree.getTreeId().equals(currentTree.getTreeId()))
//        {
//            //same treeId, set restored tree as new tree in context
//            facesContext.setTree(savedTree);
//
//            //recreate beans of "request" scope
//            recreateRequestScopeBeans(facesContext);
//
//            //restore model values
//            restoreModelValues(facesContext, false);
//        }
//        else
//        {
//            //restore global model values
//            restoreModelValues(facesContext, true);
//        }
//    }
//
//    private void restoreModelValues(FacesContext facesContext, boolean onlyGlobal)
//    {
//        UIComponent root = facesContext.getTree().getRoot();
//        Collection modelValuesColl
//            = (Collection)root.getAttributes().get(SerializingStateSaver.MODEL_VALUES_COLL_ATTR);
//        if (modelValuesColl != null)
//        {
//            for (Iterator it = modelValuesColl.iterator(); it.hasNext();)
//            {
//                ModelValueEntry entry = (ModelValueEntry)it.next();
//                if (!onlyGlobal || entry.isGlobal())
//                {
//                    String modelRef = entry.getModelReference();
//                    JspInfoUtils.checkModelInstance(facesContext, modelRef);
//
//                    ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
//                    ValueBinding vb = facesContext.getApplication().createValueBinding(modelRef);
//                    vb.setValue(facesContext, entry.getValue());
//                }
//            }
//            root.getAttributes().put(SerializingStateSaver.MODEL_VALUES_COLL_ATTR, null);
//        }
//    }
//
//
//    protected Tree unzipTree(String zippedTree)
//    {
//        try
//        {
//            ByteArrayInputStream byteStream = new ByteArrayInputStream(zippedTree.getBytes(ZipMinimizingStateSaver.ZIP_CHARSET));
//            InputStream decodedStream = Base64.getDecoder(byteStream);
//            InputStream unzippedStream = new GZIPInputStream(decodedStream);
//            ObjectInputStream ois = new MyFacesObjectInputStream(unzippedStream);
//            Tree tree = (Tree)ois.readObject();
//            ois.close();
//            unzippedStream.close();
//            decodedStream.close();
//            byteStream.close();
//
//            return tree;
//        }
//        catch (ClassNotFoundException e)
//        {
//            throw new RuntimeException(e);
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }

}
