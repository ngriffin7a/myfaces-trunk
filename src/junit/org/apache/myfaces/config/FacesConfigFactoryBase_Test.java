/*
 * Created on Apr 2, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.myfaces.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;

import junit.framework.TestCase;


/**
 * @author bdudney
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FacesConfigFactoryBase_Test extends TestCase {


  public static void main(String[] args) {
    junit.textui.TestRunner.run(FacesConfigFactoryBaseCactusTest.class);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Constructor for FacesConfigFactoryBaseTest.
   * @param name
   */
  public FacesConfigFactoryBase_Test(String name) {
    super(name);
  }

  public void testGetFacesConfig() {
  }

  public void testMetaInfLookup() throws Exception {
    FacesConfigFactoryBase subject = new FacesConfigFactoryImpl();
    FacesConfig facesConfig = new FacesConfig();
    ExternalContext context = new MockExternalContext();
    subject.performMetaInfFactoryConfig(facesConfig, context);
    assertEquals(facesConfig.getFactoryConfig().getApplicationFactory(), "java.lang.String");
    assertNull(facesConfig.getFactoryConfig().getFacesContextFactory());
    assertNull(facesConfig.getFactoryConfig().getLifecycleFactory());
    assertEquals(facesConfig.getFactoryConfig().getRenderKitFactory(), "java.lang.Integer");
  }
  
  public class MockExternalContext extends ExternalContext {

    public void dispatch(String path) throws IOException {
      throw new UnsupportedOperationException();
    }

    public String encodeActionURL(String url) {
      throw new UnsupportedOperationException();
    }

    public String encodeNamespace(String name) {
      throw new UnsupportedOperationException();
    }

    public String encodeResourceURL(String url) {
      throw new UnsupportedOperationException();
    }

    public Map getApplicationMap() {
      throw new UnsupportedOperationException();
    }

    public String getAuthType() {
      throw new UnsupportedOperationException();
    }

    public Object getContext() {
      throw new UnsupportedOperationException();
    }

    public String getInitParameter(String name) {
      throw new UnsupportedOperationException();
    }

    public Map getInitParameterMap() {
      throw new UnsupportedOperationException();
    }

    public String getRemoteUser() {
      throw new UnsupportedOperationException();
    }

    public Object getRequest() {
      throw new UnsupportedOperationException();
    }

    public String getRequestContextPath() {
      throw new UnsupportedOperationException();
    }

    public Map getRequestCookieMap() {
      throw new UnsupportedOperationException();
    }

    public Map getRequestHeaderMap() {
      throw new UnsupportedOperationException();
    }

    public Map getRequestHeaderValuesMap() {
      throw new UnsupportedOperationException();
    }

    public Locale getRequestLocale() {
      throw new UnsupportedOperationException();
    }

    public Iterator getRequestLocales() {
      throw new UnsupportedOperationException();
    }

    public Map getRequestMap() {
      throw new UnsupportedOperationException();
    }

    public Map getRequestParameterMap() {
      throw new UnsupportedOperationException();
    }

    public Iterator getRequestParameterNames() {
      throw new UnsupportedOperationException();
    }

    public Map getRequestParameterValuesMap() {
      throw new UnsupportedOperationException();
    }

    public String getRequestPathInfo() {
      throw new UnsupportedOperationException();
    }

    public String getRequestServletPath() {
      throw new UnsupportedOperationException();
    }

    public URL getResource(String path)
        throws MalformedURLException {
      throw new UnsupportedOperationException();
    }

    public InputStream getResourceAsStream(String path) {
      // the actuall classes don't matter just have to pass Class.forName
      InputStream stream = null;
      if(path.equals(FacesConfigFactoryBase.META_INF_SERVICES_LOCATION + FactoryFinder.APPLICATION_FACTORY)) {
        byte buf[] = "java.lang.String".getBytes();
        stream = new ByteArrayInputStream(buf);
      } else if(path.equals(FacesConfigFactoryBase.META_INF_SERVICES_LOCATION + FactoryFinder.RENDER_KIT_FACTORY)) {
        byte buf[] = "java.lang.Integer".getBytes();
        stream = new ByteArrayInputStream(buf);
      }
      return stream;
    }

    public Set getResourcePaths(String path) {
      Set names = new HashSet();
      names.add(FacesConfigFactoryBase.META_INF_SERVICES_LOCATION + FactoryFinder.APPLICATION_FACTORY);
      names.add(FacesConfigFactoryBase.META_INF_SERVICES_LOCATION + FactoryFinder.RENDER_KIT_FACTORY);
      return names;
    }

    public Object getResponse() {
      throw new UnsupportedOperationException();
    }

    public Object getSession(boolean create) {
      throw new UnsupportedOperationException();
    }

    public Map getSessionMap() {
      throw new UnsupportedOperationException();
    }

    public Principal getUserPrincipal() {
      throw new UnsupportedOperationException();
    }

    public boolean isUserInRole(String role) {
      throw new UnsupportedOperationException();
    }

    public void log(String message) {
      throw new UnsupportedOperationException();
    }

    public void log(String message, Throwable exception) {
    }

    public void redirect(String url) throws IOException {
      throw new UnsupportedOperationException();
    }
  }
}
