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
package net.sourceforge.myfaces.custom.rssticker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.digester.rss.Channel;
import org.apache.commons.digester.rss.Item;
import org.apache.commons.digester.rss.RSSDigester;
import org.xml.sax.SAXException;


/**
 * @author mwessendorf
 *
 */
public class HtmlRssTicker extends HtmlOutputText{
	
	public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.RssTicker";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";
	private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Ticker";

	//private fields
	private String _rssUrl = null;
	private RSSDigester _digester = null;
	private Channel _channel; 


	public HtmlRssTicker()
	{
		_digester = new RSSDigester();
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
		values[1] = _rssUrl;
		return ((Object) (values));
	}
 
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[])state;
		super.restoreState(context, values[0]);
		_rssUrl = (String)values[1];
	}	

	public String getRssUrl() {
		if (_rssUrl != null) return _rssUrl;
		ValueBinding vb = getValueBinding("rssUrl");
		return vb != null ? (String)vb.getValue(getFacesContext()) : null;

	}

	public void setRssUrl(String string) {
		_rssUrl = string;
		loadNews(_rssUrl); 
	}

	/**
	 * @param _rssUrl
	 */
	private void loadNews(String string) {
		try { 
			
			this._channel = (Channel)_digester.parse(string); 
		  } catch(MalformedURLException mue){ 
			mue.printStackTrace(); 
		  } catch (IOException e1) { 
			e1.printStackTrace(); 
		  } catch (SAXException e) { 
			e.printStackTrace(); 
		  } 
	}

	public Channel getChannel() { 
	  return _channel; 
	} 
	public int itemCount(){ 
	  return _channel.getItems().length;
	} 
	public Item[] items(){ 
	  return _channel.getItems(); 
	} 
}
