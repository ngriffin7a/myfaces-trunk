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
package net.sourceforge.myfaces.renderkit.html.ext;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.Renderer;

import net.sourceforge.myfaces.component.ext.Toolbar;
import net.sourceforge.myfaces.component.ext.ToolbarButton;
import net.sourceforge.myfaces.component.ext.ToolbarButtonPressedEvent;
import net.sourceforge.myfaces.exception.InternalServerException;
import net.sourceforge.myfaces.renderkit.RenderUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Dimitry D'hondt
 * 
 * The Toolbar Renderer
 */
public class ToolbarRenderer extends Renderer {

	private static Log log = LogFactory.getLog(ToolbarRenderer.class);

	public ToolbarRenderer() {
	}

	/**
	 * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void decode(FacesContext ctx, UIComponent component) {
		if (component instanceof Toolbar) {
			Toolbar toolbar = (Toolbar) component;
			String buttonindexId = toolbar.getClientId(ctx) + "_smile_buttonindex";
			String inputValue =	(String) ctx.getExternalContext().getRequestParameterMap().get(buttonindexId);
			if(inputValue.length() > 0) {
				int index = Integer.parseInt(inputValue);
				ToolbarButtonPressedEvent event = new ToolbarButtonPressedEvent(toolbar);
				ToolbarButton button = toolbar.getButton(index);
				event.setButton(button);
				event.setPhaseId(PhaseId.INVOKE_APPLICATION);
				toolbar.queueEvent(event);
				
				if(button.isToggleButton()) {
					button.setPressed(!button.isPressed());
				}
			}
		} else {	
			throw new IllegalStateException(
				"Invalid component type '" + component.getClass().getName() + "' for ToolbarRenderer. Component '" + component.getId() + "'.");
		}
	}

	/**
	 * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeEnd(FacesContext ctx, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		
		ResponseWriter out = ctx.getResponseWriter();
		String clientId = component.getClientId(ctx);
		String align = "left";
		
		if (component instanceof Toolbar) {
			Toolbar toolbar = (Toolbar) component;

			String buttonindexId = toolbar.getClientId(ctx) + "_smile_buttonindex";
			RenderUtils.renderHiddenField(buttonindexId);

			out.startElement("table",null);
//			out.writeAttribute("border","1",null);
			out.writeAttribute("width","100%",null);
			if(toolbar.isSolidBackground()) {
				out.writeAttribute("class","smiletoolbar",null);
//				out.writeAttribute("class","infobar",null);
			}
			out.startElement("tr",null);
			out.startElement("td",null);
			out.writeAttribute("align",align,null);
			out.startElement("table",null);
//			out.writeAttribute("border","1",null);
			out.startElement("tr",null);
			
			int buttonCount = toolbar.getButtons().size();
			for (int i = 0; i < buttonCount; i++) {
				ToolbarButton button = toolbar.getButton(i);
				if(button.getUnpressedImageURL() == null || button.getUnpressedImageURL().length() == 0) {
					log.warn("Toolbar '" + toolbar.getId() + "' contains a button with null, or zero-length unpressed image URL - ignoring button.");
					continue;
				}
				
				if(button.getUnpressedImageURL().equals("-")) {
					out.startElement("td",null);
					out.write("&nbsp;");
					out.endElement("td");
				} else if(button.getUnpressedImageURL().equals(">")) {
					out.endElement("tr");
					out.endElement("table");
					out.endElement("td");
					out.startElement("td",null);
					align="right";
					out.writeAttribute("align",align,null);
					out.startElement("table",null);
//					out.writeAttribute("border","1",null);
					out.startElement("tr",null);					
				} else {
					String primaryImage = button.getUnpressedImageURL().trim();
					String secondaryImage = button.getPressedImageURL().trim();
					if(secondaryImage == null || (secondaryImage != null && secondaryImage.length() == 0)) {
						secondaryImage = primaryImage;
					}
					
					String tooltip = button.getTooltip();

					if(button.isPressed()) {
						String temp;
						temp = secondaryImage;
						secondaryImage = primaryImage;
						primaryImage = temp;
					}
				
					String onmouseout = "";
					String onmouseover = "";
					String onmouseup = "";
					String onmousedown = "";
					
					String imageId = toolbar.getClientId(ctx) + "_smile_" + i;
					
					out.startElement("td",null);
					out.startElement("img",null);
					out.writeAttribute("id",imageId,null);
					out.writeURIAttribute("src",primaryImage, null);
					if(primaryImage != null && primaryImage.length() > 0) {
						String formName = RenderUtils.determineFormName(toolbar);
						if(formName == null) {
							throw new InternalServerException("Toolbars should be nested in a UIForm !");
						}
						onmousedown = onmousedown + " changeImage('" + imageId + "','" + secondaryImage + "');";   	
						onmouseup = onmouseup + " setHiddenField('" + buttonindexId + "','" + i + "'); submitForm('" + formName + "');";
						onmouseout = onmouseout + " setHiddenField('" + buttonindexId + "','');"; 
						onmouseout = onmouseout + " changeImage('" + imageId + "','" + primaryImage + "');";
					}

					if(tooltip != null && tooltip.length() != 0) {
						onmouseover = onmouseover + " return overlib('" + tooltip + "');";
						onmouseout = onmouseout + " return nd();";
					}

					if(onmouseover.length() > 0) out.writeAttribute("onmouseover",onmouseover,null);
					if(onmouseout.length() > 0) out.writeAttribute("onmouseout",onmouseout,null);
					if(onmouseup.length() > 0) out.writeAttribute("onmouseup",onmouseup,null);
					if(onmousedown.length() > 0) out.writeAttribute("onmousedown",onmousedown,null);
					
					out.endElement("img");
					out.endElement("td");
				}
			}
			out.endElement("tr");
			out.endElement("table");
			out.endElement("td");
			out.endElement("tr");
			out.endElement("table");
		} else {
			throw new IllegalStateException(
				"Invalid component type '" + component.getClass().getName() + "' for ToolbarRenderer. Component '" + component.getId() + "'.");
		}
	}
	
	/**********************************************************************************/

	/**
	 * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeBegin(FacesContext facescontext, UIComponent uicomponent) throws IOException {
	}

	/**
	 * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException {
	}
}
