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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.digester.rss.Item;


import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

/**
 * @author mwessendorf
 *
 */
public class HtmlRssTickerRenderer extends HtmlRenderer {
	
	
	
	public void encodeEnd(FacesContext facesContext, UIComponent component)
			throws IOException
	{
		RendererUtils.checkParamValidity(facesContext, component, HtmlRssTicker.class);

		HtmlRssTicker tickerComponent = (HtmlRssTicker) component;

		ResponseWriter writer = facesContext.getResponseWriter();

		HtmlRendererUtils.writePrettyLineSeparator(facesContext);


		tableBegin(facesContext, tickerComponent, writer); 
		rowBody(facesContext, tickerComponent, writer); 
		tableEnd(facesContext, tickerComponent, writer); 
	
	
	}

	
//-------------------------------------------------private Methods
  /**
   * @param facesContext
   * @param tickerComponent
   * @param writer
   */
  private void tableEnd(FacesContext facesContext, HtmlRssTicker tickerComponent, ResponseWriter writer) throws IOException  {
	writer.endElement(HTML.TBODY_ELEM); 
	writer.endElement(HTML.TABLE_ELEM); 
  }

  /**
   * @param facesContext
   * @param tickerComponent
   * @param writer
   */ 
  private void rowBody(FacesContext facesContext, HtmlRssTicker tickerComponent, ResponseWriter writer)throws IOException  {

	writer.startElement(HTML.THEAD_ELEM,tickerComponent); 
 	writer.startElement(HTML.TR_ELEM,tickerComponent); 
	writer.startElement(HTML.TH_ELEM,tickerComponent); 

	writer.write(tickerComponent.getChannel().getTitle()); 
 
	writer.endElement(HTML.TH_ELEM); 
	writer.endElement(HTML.TR_ELEM); 
	writer.endElement(HTML.THEAD_ELEM); 
 
	Item[] columns = tickerComponent.items(); 
	for (int i = 0; i < columns.length; i++) {
		
	  rowBegin(facesContext,tickerComponent,writer); 
 
	  writer.startElement(HTML.TD_ELEM,tickerComponent); 
	  writer.write("<a href=\""+ columns[i].getLink()+"\" target=\"_new\">"); 
	  writer.write(columns[i].getTitle()); 
	  writer.write("</a>"); 
	  writer.endElement(HTML.TD_ELEM);  
	  rowEnd(facesContext,tickerComponent,writer); 
	} 
  }

  /**
   * @param facesContext
   * @param tickerComponent
   * @param writer
   */
  private void tableBegin(FacesContext facesContext, HtmlRssTicker tickerComponent, ResponseWriter writer)throws IOException  {
	writer.startElement(HTML.TABLE_ELEM, tickerComponent); 
	writer.startElement(HTML.TBODY_ELEM, tickerComponent); 
  }
  

	/**
	 * @param facesContext
	 * @param tickerComponent
	 * @param writer
	 */
	private void rowEnd(FacesContext facesContext, HtmlRssTicker tickerComponent, ResponseWriter writer)throws IOException {
		writer.endElement(HTML.TR_ELEM); 
	}


 /**
  * @param facesContext
  * @param tickerComponent
  * @param writer
 */
  private void rowBegin(FacesContext facesContext, HtmlRssTicker tickerComponent, ResponseWriter writer) throws IOException {
	writer.startElement(HTML.TR_ELEM, tickerComponent); 
  }
  
}