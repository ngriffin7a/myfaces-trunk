/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package org.apache.myfaces.bug925693;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.RequestDispatcher;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class Bug925693CactusTest extends ServletTestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(Bug925693CactusTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public Bug925693CactusTest(String name) {
        super(name);
    }

    public void testSimpleRender() throws Exception {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/home.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    public void endSimpleRender(WebResponse response) throws Exception {
        WebTable tables[] = response.getTables();
        assertEquals(1, tables.length);
        WebTable table = tables[0];
        System.err.println("table = " + table);
        // the table has 2 rows because of the way that httpunit parses it
        assertEquals(2, table.getRowCount());
    }

    public void beginAddARow(WebRequest request) throws Exception {
    }

    public void testAddARow() throws Exception {
        // simulates the config of the faces context
        FacesContext ctx = performFacesContextConfig();
        // this block simulates a button click by adding a line item to the invoice
        addLineItem(ctx);
        // render the page
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/home.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    private void addLineItem(FacesContext ctx) {
        Application app = ctx.getApplication();
        ValueBinding binding = app.createValueBinding("#{invoice}");
        Invoice invoice = (Invoice) binding.getValue(ctx);
        invoice.addLineItem();
    }

    private FacesContext performFacesContextConfig() {
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
                .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
        FacesContextFactory facesCtxFactory = (FacesContextFactory) FactoryFinder
                .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        FacesContext ctx = facesCtxFactory.getFacesContext(config
                .getServletContext(), request, response, lifecycle);
        return ctx;
    }

    private String getLifecycleId() {
        String lifecycleId = this.config.getServletContext().getInitParameter(
                FacesServlet.LIFECYCLE_ID_ATTR);
        return lifecycleId != null ? lifecycleId
                : LifecycleFactory.DEFAULT_LIFECYCLE;
    }

    public void endAddARow(WebResponse response) throws Exception {
        WebTable tables[] = response.getTables();
        assertEquals(1, tables.length);
        WebTable table = tables[0];
        System.err.println("table = " + table);
        // the table has 3 rows because of the way that httpunit parses it
        assertEquals(3, table.getRowCount());
    }

}