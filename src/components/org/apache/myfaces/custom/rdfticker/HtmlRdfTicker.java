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
package net.sourceforge.myfaces.custom.rdfticker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ParseException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.RSSParser;

/**
 * @author mwessendorf
 *
 */
public class HtmlRdfTicker extends HtmlOutputText{
	
	public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.RdfTicker";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";
	private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Ticker";

	//private fields
	private String _rdfUrl = null;
	private ChannelIF _channel; 


	public HtmlRdfTicker()
	{
		setRendererType(DEFAULT_RENDERER_TYPE);
	}

	public String getFamily()
	{
		return COMPONENT_FAMILY;
	}
	
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = _rdfUrl;
		return ((Object) (values));
	}
 
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[])state;
		super.restoreState(context, values[0]);
		_rdfUrl = (String)values[1];
	}	

	public String getRdfUrl() {
		if (_rdfUrl != null) return _rdfUrl;
		ValueBinding vb = getValueBinding("rdfUrl");
		return vb != null ? (String)vb.getValue(getFacesContext()) : null;

	}

	public void setRdfUrl(String string) {
		_rdfUrl = string;
		loadNews(_rdfUrl); 
	}

	/**
	 * @param _rdfUrl
	 */
	private void loadNews(String string) {
		try { 
			URL _url = new URL(string); 
			_channel = RSSParser.parse(new ChannelBuilder(),_url); 
		  } catch(MalformedURLException mue){ 
			mue.printStackTrace(); 
		  } catch (IOException e1) { 
			e1.printStackTrace(); 
		  } catch (ParseException e) { 
			e.printStackTrace(); 
		  } 
	}

	public ChannelIF getChannel() { 
	  return _channel; 
	} 
	public int itemCount(){ 
	  return _channel.getItems().size(); 
	} 
	public Collection items(){ 
	  return _channel.getItems(); 
	} 
}
