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
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Sun saves a special Map in the root component, when there are common without an id.
 * They calculate a hashKey from the nested tags and store the corresponding component with
 * that key. During {@link javax.faces.webapp.FacesTag#findComponent} they use this map
 * to lookup and identify common that are already in the tree.
 * We must save and restore this Map, because otherwise the findComponent method would always
 * add a component that has no id again to our restored tree.
 * Since this is a heavy object and we do not want to drag the whole bunch of common, we use
 * this little trick:
 * Before we save the map by serializing, we replace each component by it's clientId. And then
 * when we have restored the map again, we do the other way round and replace each clientId by
 * the (hopefully found again) component.
 * And if we are in production and have jspInfoApplicationCaching = true, we can even abandon
 * serializing, and easily save the clientId-Map in the parsed tree's root.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TagHashHack
{
    private static final String TAG_HASH_ATTR = "tagHash";

    private TagHashHack() {}

    public static boolean isTagHashAttribute(UIComponent uiComponent,
                                             String attrName)
    {
        return (uiComponent.getParent() == null &&
                attrName.equals(TAG_HASH_ATTR));
    }

    /**
     * @param facesContext
     * @return  null, if there is no need to save
     */
    public static String getAsStringToBeSaved(FacesContext facesContext,
                                              Map tagHash)
    {
        Tree parsedTree = JspInfo.getTree(facesContext,
                                          facesContext.getTree().getTreeId());

        if (parsedTree.getRoot().getAttribute(TAG_HASH_ATTR) != null)
        {
            //Already in parsed tree
            return null;
        }

        //Remap each tagKey to the uniqueId instead of the component
        Map saveTagHash = new HashMap();
        for (Iterator it = tagHash.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            UIComponent comp = (UIComponent)entry.getValue();
            saveTagHash.put(entry.getKey(),
                            JspInfo.getUniqueComponentId(comp));
        }

        if (MyFacesConfig.isJspInfoApplicationCaching())
        {
            //Save tagHash in parsed tree and do not save in client
            parsedTree.getRoot().setAttribute(TAG_HASH_ATTR, saveTagHash);
            return null;
        }

        return ConverterUtils.serialize(saveTagHash);
    }


    public static Object getAsObjectFromSaved(String attrValue)
    {
        return ConverterUtils.deserialize(attrValue);
    }


    public static void convertUniqueIdsBackToComponents(Tree restoredTree)
    {
        UIComponent root = restoredTree.getRoot();
        Map restoredTagHash = (Map)root.getAttribute(TAG_HASH_ATTR);
        if (restoredTagHash != null)
        {
            Map realTagHash = new HashMap();
            for (Iterator tagHashIt = restoredTagHash.entrySet().iterator(); tagHashIt.hasNext();)
            {
                Map.Entry tagHashEntry = (Map.Entry)tagHashIt.next();
                String uniqueId = (String)tagHashEntry.getValue();
                UIComponent comp = JspInfo.findComponentByUniqueId(restoredTree,
                                                                   uniqueId);
                if (comp == null)
                {
                    LogUtil.getLogger().severe("Could not find component by uniqueId " + uniqueId);
                }
                realTagHash.put(tagHashEntry.getKey(), comp);
            }
            root.setAttribute(TAG_HASH_ATTR, realTagHash);
        }
    }

}
