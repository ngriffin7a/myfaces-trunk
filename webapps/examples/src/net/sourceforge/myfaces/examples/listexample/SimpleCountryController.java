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
package net.sourceforge.myfaces.examples.listexample;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import java.math.BigDecimal;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class SimpleCountryController
    implements ActionListener
{
    public PhaseId getPhaseId()
    {
        return PhaseId.APPLY_REQUEST_VALUES;
    }

    public void processAction(ActionEvent event) throws AbortProcessingException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        //Get isoCode from component
        UIComponent uiCommand = event.getComponent();
        String isoCode = (String)uiCommand.getAttribute("isoCode");
        String name = (String)uiCommand.getAttribute("name");
        BigDecimal size = (BigDecimal)uiCommand.getAttribute("size");

        //Create new form and set the isoCode
        SimpleCountryForm form = new SimpleCountryForm();
        form.setIsoCode(isoCode);
        form.setName(name);
        form.setSize(size);

        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ValueBinding vb = af.getApplication().getValueBinding("countryForm");
        vb.setValue(facesContext, form);    //TODO: countryForm should be managedBean!

        //Jump to detail page
        TreeFactory tf = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
        Tree tree = tf.getTree(facesContext, "/countryForm.jsf");
        facesContext.setTree(tree);
        facesContext.renderResponse();
    }

}
