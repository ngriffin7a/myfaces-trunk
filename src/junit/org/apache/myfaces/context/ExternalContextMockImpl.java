package net.sourceforge.myfaces.context;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.http.Cookie;


public class ExternalContextMockImpl extends ExternalContext {

    public ExternalContextMockImpl() {
        throw new UnsupportedOperationException();
    }

    public Map getApplicationMap() {
        throw new UnsupportedOperationException();
    }

    public Object getContext() {
        throw new UnsupportedOperationException();
    }

    public Object getRequest() {
        throw new UnsupportedOperationException();
    }

    public Map getRequestMap() {
        throw new UnsupportedOperationException();
    }

    public Object getResponse() {
        throw new UnsupportedOperationException();
    }

    public Object getSession(boolean arg0) {
        throw new UnsupportedOperationException();
    }

    public Map getSessionMap() {
        throw new UnsupportedOperationException();
    }

    public Map getRequestParameterMap() {
        throw new UnsupportedOperationException();
    }

    public Map getRequestParameterValuesMap() {
        throw new UnsupportedOperationException();
    }

    public Iterator getRequestParameterNames() {
        throw new UnsupportedOperationException();
    }

    public Map getRequestHeaderMap() {
        throw new UnsupportedOperationException();
    }

    public Map getRequestHeaderValuesMap() {
        throw new UnsupportedOperationException();
    }

    public Map getRequestCookieMap() {
        throw new UnsupportedOperationException();
    }

    public Locale getRequestLocale() {
        throw new UnsupportedOperationException();
    }

    public String getRequestPathInfo() {
        throw new UnsupportedOperationException();
    }

    public String getRequestContextPath() {
        throw new UnsupportedOperationException();
    }

    public String getRequestServletPath() {
        throw new UnsupportedOperationException();
    }

    public Cookie[] getRequestCookies() {
        throw new UnsupportedOperationException();
    }

    public String getInitParameter(String arg0) {
        throw new UnsupportedOperationException();
    }

    public Map getInitParameterMap() {
        throw new UnsupportedOperationException();
    }

    public Set getResourcePaths(String arg0) {
        throw new UnsupportedOperationException();
    }

    public InputStream getResourceAsStream(String arg0) {
        throw new UnsupportedOperationException();
    }

    public String getAuthType() {
        throw new UnsupportedOperationException();
    }

    public String getRemoteUser() {
        throw new UnsupportedOperationException();
    }

    public boolean isUserInRole(String arg0) {
        throw new UnsupportedOperationException();
    }

    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    }

    public String encodeActionURL(String arg0) {
        throw new UnsupportedOperationException();
    }

    public String encodeResourceURL(String arg0) {
        throw new UnsupportedOperationException();
    }

    public String encodeNamespace(String arg0) {
        throw new UnsupportedOperationException();
    }

    public void dispatchMessage(String arg0) throws IOException, FacesException {
        throw new UnsupportedOperationException();
    }

    public void log(String arg0) {
        throw new UnsupportedOperationException();
    }

    public void log(String arg0, Throwable arg1) {
        throw new UnsupportedOperationException();
    }
}

class RequestMap implements Map {

    public int size() {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }
    
}