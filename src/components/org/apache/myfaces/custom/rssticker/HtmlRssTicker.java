/*
 * Copyright 2004 The Apache Software Foundation.
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
package net.sourceforge.myfaces.custom.rssticker;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.digester.rss.Channel;
import org.apache.commons.digester.rss.Item;
import org.apache.commons.digester.rss.RSSDigester;
import org.xml.sax.SAXException;


/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/07/01 21:53:10  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/06/27 22:06:26  mwessendorf
 * Log
 *
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
