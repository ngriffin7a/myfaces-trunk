/**
 * Smile, the open-source JSF implementation.
 * Copyright (C) 2004  The smile team (http://smile.sourceforge.net)
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
package net.sourceforge.myfaces.application.cbp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

import net.sourceforge.myfaces.exception.ComponentCreationException;
import net.sourceforge.myfaces.exception.SmileRuntimeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implemenation of the state manager
 * This implementation will keep state in the session
 * expect if explicitely indicated by initParameter "javax.faces.STATE_SAVING_METHOD"
 * 
 * @since 0.3
 * @author <a href="mailto:emol@users.sourceforge.net">Edwin Mol</a>
 */
public class StateManagerImpl extends StateManager {
	
	//TODO check correct save/restore of locale 	
	private static Log log = LogFactory.getLog(StateManagerImpl.class);

	public static final String SESSION_KEY_CURRENT_VIEW = "net.sourceforge.myfaces.cbp.current_view";
	
	
	/**
	 * @see javax.faces.application.StateManager#saveSerializedView(javax.faces.context.FacesContext)
	 */
	public SerializedView saveSerializedView(FacesContext ctx)
		throws IllegalStateException {
		UIViewRoot viewRoot = getViewRoot(ctx);
		if (isSavingStateInClient(ctx)) {
			//validate the tree client ids
			Set idSet = new HashSet();
			validate(ctx,viewRoot,idSet);
			//save Locale
			if (viewRoot.getLocale() == null) {
				viewRoot.setLocale(ctx.getExternalContext().getRequestLocale());
			}			
			TreeNode[] treeState = getTreeStructureArray(ctx);
			Object componentState = getComponentStateToSave(ctx);
			return new SerializedView(treeState,componentState);
		} else {
			return null;
		}

	}

	/**
	 * @see javax.faces.application.StateManager#getTreeStructureToSave(javax.faces.context.FacesContext)
	 */
	protected Object getTreeStructureToSave(FacesContext ctx) {
		return getTreeStructureArray(ctx);
	}

	/**
	 * @see javax.faces.application.StateManager#getComponentStateToSave(javax.faces.context.FacesContext)
	 */
	protected Object getComponentStateToSave(FacesContext ctx) {
		Object state = null;
		UIViewRoot viewRoot = getViewRoot(ctx);	
		state = viewRoot.processSaveState(ctx);
		return state;
	}

	/**
	 * @see javax.faces.application.StateManager#writeState(javax.faces.context.FacesContext, javax.faces.application.StateManager.SerializedView)
	 */
	public void writeState(FacesContext ctx, SerializedView view)
		throws IOException {
		if (ctx == null) {
			throw new SmileRuntimeException(new NullPointerException("FacesContext cannot be null"));
		}		
		if (isSavingStateInClient(ctx)) {
			RenderKit renderKit = ((RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY)).getRenderKit(ctx, RenderKitFactory.HTML_BASIC_RENDER_KIT);
			if (renderKit != null) {
				ResponseStateManager responseStatemanager = renderKit.getResponseStateManager();
				responseStatemanager.writeState(ctx,view);
			}
		}
		else {
			ctx.getExternalContext().getSessionMap().put(SESSION_KEY_CURRENT_VIEW,ctx.getViewRoot());				
		}
	}

	/**
	 * @see javax.faces.application.StateManager#restoreView(javax.faces.context.FacesContext, java.lang.String)
	 */
	public UIViewRoot restoreView(FacesContext ctx, String viewId,String renderKitId) {
		UIViewRoot ret = null;
		if (ctx == null) {
			throw new SmileRuntimeException(new NullPointerException("FacesContext cannot be null"));
		}		
		if (viewId == null || viewId.length() == 0) {
			return null;
		}
		if (isSavingStateInClient(ctx)) {
			ret = restoreTreeStructure(ctx,viewId,renderKitId);
			if (ret != null) {
				//try {
					restoreComponentState(ctx,ret,renderKitId);
				//} catch (IOException e) {
				//	log.error("Could not restore view <"+viewId+">");
				//}
			}
		} 
		else {
			if (ctx.getExternalContext().getSessionMap().containsKey(SESSION_KEY_CURRENT_VIEW)) {
				UIViewRoot viewRoot;
				viewRoot = (UIViewRoot) ctx.getExternalContext().getSessionMap().get(SESSION_KEY_CURRENT_VIEW);
				if(viewRoot.getViewId().equals(viewId)) {
					ret = viewRoot;
				}
			}
		}
		
		return ret;
	}

	/**
	 * @see javax.faces.application.StateManager#restoreTreeStructure(javax.faces.context.FacesContext, java.lang.String)
	 */
	protected UIViewRoot restoreTreeStructure(FacesContext ctx, String viewId,String renderKitId) {
		UIViewRoot viewRoot = null;
		if (ctx == null) {
			throw new SmileRuntimeException(new NullPointerException("FacesContext cannot be null"));
		}
		RenderKit renderKit = ((RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY)).getRenderKit(ctx, renderKitId);
		if (renderKit != null) {
			ResponseStateManager responseStatemanager = renderKit.getResponseStateManager();
			TreeNode[] saveTreeArray = (TreeNode[])responseStatemanager.getTreeStructureToRestore(ctx,viewId);
			if (saveTreeArray != null) {
				viewRoot = extractTreeFromArray(saveTreeArray);
			}
		}
		return viewRoot;
	}

	/**
	 * @see javax.faces.application.StateManager#restoreComponentState(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
	 */
	protected void restoreComponentState(FacesContext ctx, UIViewRoot viewRoot,String renderKitId){
		if (ctx == null) {
			throw new SmileRuntimeException(new NullPointerException("FacesContext cannot be null"));
		}
		RenderKit renderKit = ((RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY)).getRenderKit(ctx, renderKitId);
		if (renderKit != null) {
			ResponseStateManager responseStatemanager = renderKit.getResponseStateManager();
			Object componentState = responseStatemanager.getComponentStateToRestore(ctx);
			viewRoot.processRestoreState(ctx,componentState);
		}
	}
	
	/**
	 * Add all components in the tree to the list recursively from the
	 * current component downwards, including all facets
	 * @param component
	 * @param treeNodeList
	 */
	private void addTreeNodeToList(UIComponent component, List treeNodeList, FacesContext ctx) {
		//only add non-transient components
		if (component.isTransient()) {
			return;
		}
		treeNodeList.add(StateUtil.createTreeNode(component,ctx));
		for (Iterator iter = component.getFacets().keySet().iterator();
		iter.hasNext();
		) {
			String key = (String) iter.next();
			UIComponent facet = (UIComponent)component.getFacets().get(key);
			treeNodeList.add(StateUtil.createFacetTreeNode(facet,key,ctx));
		}
		for (Iterator iter = component.getChildren().iterator();
			iter.hasNext();
			) {
			UIComponent comp = (UIComponent) iter.next();
			if (comp != null) {
				addTreeNodeToList(comp,treeNodeList,ctx);
			}
		}

	}
	
	/**
	 * @param saveTreeArray
	 * @return
	 */
	private UIViewRoot extractTreeFromArray(TreeNode[] saveTreeArray) {
		UIViewRoot viewRoot = null;
		Map components = new HashMap(); //temporary map to keep track of restored components		
		for (int i = 0; i < saveTreeArray.length; i++) {
			TreeNode node = saveTreeArray[i];
			UIComponent component = getComponentInstance(node.getClassName());
			UIComponent parent = null;
			if (node.getParentId() != null) {
				parent = (UIComponent)components.get(node.getParentId());
			}
			component.setId(node.getId());
			if (parent == null && component instanceof UIViewRoot) {
				viewRoot = (UIViewRoot) component;				
				viewRoot.setViewId(node.getViewId());
				if (node.getId() != null) {
					components.put(node.getId(),viewRoot);
				}
			}			
			else if (node.isFacet()){				
				parent.getFacets().put(node.getFacetKey(),component);
			} else {
				if (parent == null) {
					parent = viewRoot;
				}
				parent.getChildren().add(component);	
				components.put(node.getId(),component);
			}		
		}
		return viewRoot;
	}	
	
	/**
	 * @param ctx
	 * @return
	 * @throws SmileRuntimeException
	 */
	private UIViewRoot getViewRoot(FacesContext ctx) throws SmileRuntimeException {
		if (ctx == null) {
			throw new SmileRuntimeException(new NullPointerException("FacesContext cannot be null"));
		}
		UIViewRoot viewRoot = ctx.getViewRoot();
		if (viewRoot == null) {
			throw new SmileRuntimeException(new NullPointerException("UIViewRoot cannot be null"));
		}
		return viewRoot;
	}
	
	/**
	 * @param className
	 * @return
	 */
	private UIComponent getComponentInstance(String className) {
		Class componentClass = null;
		UIComponent component = null;
		try {
			componentClass =
				Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ComponentCreationException("A UIComponent could not be instanciated for class name <"+className+">",e);
		}
		try {
			component = (UIComponent) componentClass.newInstance();
		} catch (InstantiationException e) {
			throw new ComponentCreationException("A UIComponent could not be instanciated for class name <"+className+">",e);
		} catch (IllegalAccessException e) {
			throw new ComponentCreationException("A UIComponent could not be instanciated for class name <"+className+">",e);
		} catch (ClassCastException e) {
			throw new ComponentCreationException("A UIComponent could not be instanciated for class name <"+className+">",e);
		}		
		return component;
	}

	/**
	 * Check client id uniqueness
	 * @param ctx
	 * @param component
	 * @param idSet
	 * @throws IllegalStateException
	 */
	private void validate(FacesContext ctx, UIComponent component,Set idSet) throws IllegalStateException {
		for (Iterator iter = component.getFacetsAndChildren(); iter.hasNext();) {
			UIComponent comp = (UIComponent) iter.next();
			if (idSet.contains(comp.getClientId(ctx))) {
				throw new IllegalStateException("component <" + comp.getId() + " it's clientId <" + comp.getClientId(ctx) + "> occurs twice ! It's parent Id is <" + comp.getParent().getId() +">.");
			} else {
				idSet.add(comp.getClientId(ctx));
				validate(ctx,comp,idSet);
			}
		}
	}
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private TreeNode[] getTreeStructureArray(FacesContext ctx) {
		UIViewRoot viewRoot = getViewRoot(ctx);
		List treeNodeList = new ArrayList();
		addTreeNodeToList(viewRoot,treeNodeList,ctx);
		TreeNode[] ret = new TreeNode[treeNodeList.size()];
		for (int i = 0; i < treeNodeList.size(); i++) {
			TreeNode node = (TreeNode) treeNodeList.get(i);
			ret[i] = node;
		}
		return ret;
	}
}
