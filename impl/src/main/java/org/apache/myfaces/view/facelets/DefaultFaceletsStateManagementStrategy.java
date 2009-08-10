/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.view.facelets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRemoveFromViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.ResponseStateManager;
import javax.faces.view.StateManagementStrategy;
import javax.faces.view.ViewDeclarationLanguage;

import org.apache.myfaces.application.TreeStructureManager.TreeStructComponent;
import org.apache.myfaces.shared_impl.renderkit.RendererUtils;
import org.apache.myfaces.shared_impl.util.ClassUtils;

/**
 * This class implements partial state saving feature when facelets
 * is used to render pages. (Theorically it could be applied on jsp case too,
 * but all considerations below should be true before apply it).
 * 
 * The following considerations apply for this class:
 * 
 * 1. This StateManagementStrategy should only be active if javax.faces.PARTIAL_STATE_SAVING
 *    config param is active(true). See javadoc on StateManager for details.
 * 2. A map using component clientId as keys are used to hold the state.
 * 3. Each component has a valid id after ViewDeclarationLanguage.buildView().
 *    This implies that somewhere, every TagHandler that create an UIComponent 
 *    instance should call setId and assign it.
 * 4. Every TagHandler that create an UIComponent instance should call markInitialState
 *    after the component is populated. Otherwise, full state is always saved.
 * 5. A SystemEventListener is used to keep track for added and removed components, listen
 *    PostAddToViewEvent and PreRemoveFromViewEvent event triggered by UIComponent.setParent()
 *    method.
 * 6. It is not possible to use javax.faces.component.visit API to traverse the component
 *    tree during save/restore, because UIData.visitTree traverse all rows and we only need
 *    to restore state per component (not per row).
 * 7. It is necessary to preserve the order of the children added/removed between requests.
 * 8. Added and removed components could be seen as subtrees. This imply that we need to save
 *    the structure of the added components subtree and remove one component could be remove
 *    all its children and facets from view inclusive.
 * 9. It is necessary to save and restore the list of added/removed components between several
 *    requests.
 * 
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 793245 $ $Date: 2009-07-11 18:50:53 -0500 (Sat, 11 Jul 2009) $
 * @since 2.0
 *
 */
public class DefaultFaceletsStateManagementStrategy extends StateManagementStrategy
{
    private static final String CLIENTIDS_ADDED = "CLIENTIDS_ADDED";
    
    private static final String CLIENTIDS_REMOVED = "CLIENTIDS_REMOVED";
    
    private static final String COMPONENT_ADDED_AFTER_BUILD_VIEW = "COMPONENT_ADDED_AFTER_BUILD_VIEW"; 
    
    private ViewDeclarationLanguage vdl;
    
    public DefaultFaceletsStateManagementStrategy (ViewDeclarationLanguage vdl)
    {
        this.vdl = vdl;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public UIViewRoot restoreView (FacesContext context, String viewId, String renderKitId)
    {
        ResponseStateManager manager;
        Object state[];
        Map<String, Object> states;
        
        UIViewRoot view;
        
        // Per the spec: build the view.
        
        try {
            view = vdl.createView (context, viewId);
            
            vdl.buildView (context, view);
            
            context.setViewRoot (view);
        }
        
        catch (Throwable e) {
            throw new FacesException ("unable to create view \"" + viewId + "\"", e);
        }
        
        // Get previous state from ResponseStateManager.
        
        manager = RendererUtils.getResponseStateManager (context, renderKitId);
        
        state = (Object[]) manager.getState (context, viewId);
        states = (Map<String, Object>) state[1];
        
        // Visit the children and restore their state.
        
        //view.visitTree (VisitContext.createVisitContext (context), new RestoreStateVisitor (states));
        
        //Restore state of current components
        restoreStateFromMap(context, states, view);
        
        // TODO: handle dynamic add/removes as mandated by the spec.  Not sure how to do handle this yet.
        List<String> clientIdsRemoved = getClientIdsRemoved(view);
        
        if (clientIdsRemoved != null)
        {
            for (String clientId : clientIdsRemoved)
            {
                view.invokeOnComponent(context, clientId, new ContextCallback()
                    {
                        @Override
                        public void invokeContextCallback(FacesContext context,
                                UIComponent target)
                        {
                            target.getParent().getChildren().remove(target);
                        }
                    });
            }
        }
        
        List<String> clientIdsAdded = getClientIdsAdded(view);        
        if (clientIdsAdded != null)
        {
            for (String clientId : clientIdsAdded)
            {
                final Object[] addedState = (Object[]) states.get(clientId); 
                if (addedState != null && addedState.length == 5)
                {
                    final String parentClientId = (String) addedState[0];
                    view.invokeOnComponent(context, parentClientId, new ContextCallback()
                    {
                        @Override
                        public void invokeContextCallback(FacesContext context,
                                UIComponent target)
                        {
                            if (addedState[1] != null)
                            {
                                String facetName = (String) addedState[1];
                                UIComponent child = internalRestoreTreeStructure((TreeStructComponent) addedState[3]);
                                child.processRestoreState(context, addedState[4]);
                                target.getFacets().put(facetName,child);
                            }
                            else
                            {
                                Integer childIndex = (Integer) addedState[2];
                                UIComponent child = internalRestoreTreeStructure((TreeStructComponent) addedState[3]);
                                child.processRestoreState(context, addedState[4]);
                                try
                                {
                                    target.getChildren().add(childIndex, child);                                    
                                }
                                catch (IndexOutOfBoundsException e)
                                {
                                    // We can't be sure about where should be this 
                                    // item, so just add it. 
                                    target.getChildren().add(child);
                                }
                            }
                        }
                    });
                }
            }
        }
        return view;
    }

    @Override
    public Object saveView (FacesContext context)
    {
        UIViewRoot view = context.getViewRoot();
        HashMap<String, Object> states;
        
        if (view == null) {
            // Not much that can be done.
            
            return null;
        }
        
        if (view.isTransient()) {
            // Must return null immediately per spec.
            
            return null;
        }
        
        // Make sure the client IDs are unique per the spec.
        
        checkIds (context, view, new HashSet<String>());
        
        // Create save state objects for every component.
        
        states = new HashMap<String, Object>();
        
        //view.visitTree (VisitContext.createVisitContext (context), new SaveStateVisitor (states));
        saveStateOnMap(context, states, view);
        
        // TODO: not sure the best way to handle dynamic adds/removes as mandated by the spec.
        
        // As required by ResponseStateManager, the return value is an Object array.  First
        // element is the structure object, second is the state map.
        
        return new Object[] { null, states };
    }
    
    private void restoreStateFromMap(final FacesContext context, final Map<String,Object> states,
            final UIComponent component)
    {
        //Restore view
        Object state = states.get(component.getClientId());
        if (state != null)
        {
            component.restoreState(context, state);
        }

        //Scan children
        if (component.getChildCount() > 0)
        {
            String currentClientId = component.getClientId();
            
            List<UIComponent> children  = component.getChildren();
            for (int i = 0; i < children.size(); i++)
            {
                UIComponent child = children.get(i);
                if (child != null && !child.isTransient())
                {
                    restoreStateFromMap( context, states, child);
                }
            }
        }

        //Scan facets
        Map<String, UIComponent> facetMap = component.getFacets();
        if (!facetMap.isEmpty())
        {
            String currentClientId = component.getClientId();
            
            for (Map.Entry<String, UIComponent> entry : facetMap.entrySet())
            {
                UIComponent child = entry.getValue();
                if (child != null && !child.isTransient())
                {
                    String facetName = entry.getKey();
                    restoreStateFromMap( context, states, child);
                }
            }
        }
    }
    
    static List<String> getClientIdsAdded(UIViewRoot root)
    {
        return (List<String>) root.getAttributes().get(CLIENTIDS_ADDED);
    }
    
    static void setClientsIdsAdded(UIViewRoot root, List<String> clientIdsList)
    {
        root.getAttributes().put(CLIENTIDS_ADDED, clientIdsList);
    }
    
    static List<String> getClientIdsRemoved(UIViewRoot root)
    {
        return (List<String>) root.getAttributes().get(CLIENTIDS_REMOVED);
    }
    
    static void setClientsIdsRemoved(UIViewRoot root, List<String> clientIdsList)
    {
        root.getAttributes().put(CLIENTIDS_REMOVED, clientIdsList);
    }
            
    private void saveStateOnMap(final FacesContext context, final Map<String,Object> states,
            final UIComponent component)
    {        
        //Save state        
        Object savedState = component.saveState(context);
        
        //Only save if the value returned is null
        if (savedState != null)
        {
            states.put(component.getClientId(), savedState);            
        }
        
        //Scan children
        if (component.getChildCount() > 0)
        {
            String currentClientId = component.getClientId();
            
            List<UIComponent> children  = component.getChildren();
            for (int i = 0; i < children.size(); i++)
            {
                UIComponent child = children.get(i);
                if (child != null && !child.isTransient())
                {
                    if (child.getAttributes().containsKey(COMPONENT_ADDED_AFTER_BUILD_VIEW))
                    {
                        //Save all required info to restore the subtree.
                        //This includes position, structure and state of subtree
                        states.put(child.getClientId(), 
                                new Object[]{
                                    currentClientId,
                                    null,
                                    i,
                                    internalBuildTreeStructureToSave(child),
                                    child.processSaveState(context)});
                    }
                    else
                    {
                        saveStateOnMap( context, states, child);
                    }
                }
            }
        }

        //Scan facets
        Map<String, UIComponent> facetMap = component.getFacets();
        if (!facetMap.isEmpty())
        {
            String currentClientId = component.getClientId();
            
            for (Map.Entry<String, UIComponent> entry : facetMap.entrySet())
            {
                UIComponent child = entry.getValue();
                if (child != null && !child.isTransient())
                {
                    String facetName = entry.getKey();
                    if (child.getAttributes().containsKey(COMPONENT_ADDED_AFTER_BUILD_VIEW))
                    {
                        //Save all required info to restore the subtree.
                        //This includes position, structure and state of subtree
                        states.put(child.getClientId(), new Object[]{
                            currentClientId,
                            facetName,
                            null,
                            internalBuildTreeStructureToSave(child),
                            child.processSaveState(context)});
                    }
                    else
                    {
                        saveStateOnMap( context, states, child);
                    }
                }
            }
        }
    }
    
    public void suscribeListeners(UIViewRoot uiViewRoot)
    {
        PostAddPreRemoveFromViewListener componentListener = new PostAddPreRemoveFromViewListener();
        uiViewRoot.subscribeToViewEvent(PostAddToViewEvent.class, componentListener);
        uiViewRoot.subscribeToViewEvent(PreRemoveFromViewEvent.class, componentListener);
    }
    
    private void checkIds (FacesContext context, UIComponent component, Set<String> existingIds)
    {
        String id;
        Iterator<UIComponent> children;
        
        if (component == null) {
            return;
        }
        
        // Need to use this form of the client ID method so we generate the client-side ID.
        
        id = component.getClientId (context);
        
        if (existingIds.contains (id)) {
            throw new IllegalStateException ("component with duplicate id \"" + id + "\" found");
        }
        
        existingIds.add (id);
        
        children = component.getFacetsAndChildren();
        
        while (children.hasNext()) {
            checkIds (context, children.next(), existingIds);
        }
    }
    
    /*
    private class RestoreStateVisitor implements VisitCallback {
        private Map<String, Object> states;
        
        private RestoreStateVisitor (Map<String, Object> states)
        {
            this.states = states;
        }
        
        @Override
        public VisitResult visit (VisitContext context, UIComponent target)
        {
            FacesContext facesContext = context.getFacesContext();
            Object state = states.get (target.getClientId (facesContext));
            
            if (state != null) {
                target.restoreState (facesContext, state);
            }
            
            return VisitResult.ACCEPT;
        }
    }
    
    private class SaveStateVisitor implements VisitCallback {
        private Map<String, Object> states;
        
        private SaveStateVisitor (Map<String, Object> states)
        {
            this.states = states;
        }
        
        @Override
        public VisitResult visit (VisitContext context, UIComponent target)
        {
            FacesContext facesContext = context.getFacesContext();
            Object state;
            
            if ((target == null) || target.isTransient()) {
                // No need to bother with these components or their children.
                
                return VisitResult.REJECT;
            }
            
            state = target.saveState (facesContext);
            
            if (state != null) {
                // Save by client ID into our map.
                
                states.put (target.getClientId (facesContext), state);
            }
            
            return VisitResult.ACCEPT;
        }
    }
    */
    
    public static class PostAddPreRemoveFromViewListener implements SystemEventListener
    {

        @Override
        public boolean isListenerForSource(Object source)
        {
            // PostAddToViewEvent and PreRemoveFromViewEvent are
            // called from UIComponentBase.setParent
            return (source instanceof UIComponent);
        }

        @Override
        public void processEvent(SystemEvent event)
        {
            UIComponent component = (UIComponent) event.getSource();
            
            if (event instanceof PostAddToViewEvent)
            {
                //PostAddToViewEvent
                UIViewRoot uiViewRoot = FacesContext.getCurrentInstance().getViewRoot();
                
                List<String> clientIdsAdded = getClientIdsAdded(uiViewRoot);
                if (clientIdsAdded == null)
                {
                    //Create a set that preserve insertion order
                    clientIdsAdded = new ArrayList<String>();
                }
                clientIdsAdded.add(component.getClientId());
                setClientsIdsAdded(uiViewRoot, clientIdsAdded);
                
                component.getAttributes().put(COMPONENT_ADDED_AFTER_BUILD_VIEW, Boolean.TRUE);
            }
            else
            {
                //PreRemoveFromViewEvent
                UIViewRoot uiViewRoot = FacesContext.getCurrentInstance().getViewRoot();
                
                List<String> clientIdsRemoved = getClientIdsRemoved(uiViewRoot);
                if (clientIdsRemoved == null)
                {
                    //Create a set that preserve insertion order
                    clientIdsRemoved = new ArrayList<String>();
                }
                clientIdsRemoved.add(component.getClientId());
                setClientsIdsRemoved(uiViewRoot, clientIdsRemoved);
            }
        }
    }
    
    private TreeStructComponent internalBuildTreeStructureToSave(UIComponent component)
    {
        TreeStructComponent structComp = new TreeStructComponent(component.getClass().getName(),
                                                                 component.getId());

        //children
        if (component.getChildCount() > 0)
        {
            List<TreeStructComponent> structChildList = new ArrayList<TreeStructComponent>();
            for (UIComponent child : component.getChildren())
            {
                if (!child.isTransient())
                {
                    TreeStructComponent structChild = internalBuildTreeStructureToSave(child);
                    structChildList.add(structChild);
                }
            }
            
            TreeStructComponent[] childArray = structChildList.toArray(new TreeStructComponent[structChildList.size()]);
            structComp.setChildren(childArray);
        }

        //facets
        Map<String, UIComponent> facetMap = component.getFacets();
        if (!facetMap.isEmpty())
        {
            List<Object[]> structFacetList = new ArrayList<Object[]>();
            for (Map.Entry<String, UIComponent> entry : facetMap.entrySet())
            {
                UIComponent child = entry.getValue();
                if (!child.isTransient())
                {
                    String facetName = entry.getKey();
                    TreeStructComponent structChild = internalBuildTreeStructureToSave(child);
                    structFacetList.add(new Object[] {facetName, structChild});
                }
            }
            
            Object[] facetArray = structFacetList.toArray(new Object[structFacetList.size()]);
            structComp.setFacets(facetArray);
        }

        return structComp;
    }
    
    private UIComponent internalRestoreTreeStructure(TreeStructComponent treeStructComp)
    {
        String compClass = treeStructComp.getComponentClass();
        String compId = treeStructComp.getComponentId();
        UIComponent component = (UIComponent)ClassUtils.newInstance(compClass);
        component.setId(compId);

        //children
        TreeStructComponent[] childArray = treeStructComp.getChildren();
        if (childArray != null)
        {
            List<UIComponent> childList = component.getChildren();
            for (int i = 0, len = childArray.length; i < len; i++)
            {
                UIComponent child = internalRestoreTreeStructure(childArray[i]);
                childList.add(child);
            }
        }

        //facets
        Object[] facetArray = treeStructComp.getFacets();
        if (facetArray != null)
        {
            Map<String, UIComponent> facetMap = component.getFacets();
            for (int i = 0, len = facetArray.length; i < len; i++)
            {
                Object[] tuple = (Object[])facetArray[i];
                String facetName = (String)tuple[0];
                TreeStructComponent structChild = (TreeStructComponent)tuple[1];
                UIComponent child = internalRestoreTreeStructure(structChild);
                facetMap.put(facetName, child);
            }
        }

        return component;
    }

    public static class TreeStructComponent implements Serializable
    {
        private static final long serialVersionUID = 5069109074684737231L;
        private String _componentClass;
        private String _componentId;
        private TreeStructComponent[] _children = null; // Array of children
        private Object[] _facets = null; // Array of Array-tuples with Facetname and TreeStructComponent

        TreeStructComponent(String componentClass, String componentId)
        {
            _componentClass = componentClass;
            _componentId = componentId;
        }

        public String getComponentClass()
        {
            return _componentClass;
        }

        public String getComponentId()
        {
            return _componentId;
        }

        void setChildren(TreeStructComponent[] children)
        {
            _children = children;
        }

        TreeStructComponent[] getChildren()
        {
            return _children;
        }

        Object[] getFacets()
        {
            return _facets;
        }

        void setFacets(Object[] facets)
        {
            _facets = facets;
        }
    }
    
}