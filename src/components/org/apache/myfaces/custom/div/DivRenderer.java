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
package org.apache.myfaces.custom.div;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
/**
 * @author bdudney (latest modification by $Author$) 
 * @version $Revision$ $Date$ 
 * $Log$
 * Revision 1.2  2004/11/10 11:09:52  bdudney
 * div renderer now puts the class/style in quotes
 *
 * Revision 1.1  2004/11/08 03:43:20  bdudney
 * Added a div element. x:div to use, inserts a div with class or style attributes
 *
 */
public class DivRenderer extends HtmlRenderer {
  private static final Log log = LogFactory.getLog(DivRenderer.class);
  public static final String RENDERER_TYPE = "org.apache.myfaces.DivRenderer";

  public void encodeBegin(FacesContext context, UIComponent component)
      throws IOException {
    if ((context == null) || (component == null)) {
      throw new NullPointerException();
    }
    Div div = (Div) component;
    ResponseWriter writer = context.getResponseWriter();
    writer.write("<" + HTML.DIV_ELEM +" ");
    String styleClass = div.getStyleClass();
    String style = div.getStyle();
    if(null == styleClass && null == style) {
      throw new IllegalStateException("Either style or styleClass must be specified");
    }
    if(null != styleClass && null != style) {
      throw new IllegalStateException("Only one of style or styleClass can be specified");
    }
    if(null != styleClass) {
      writer.write("class=\"" + styleClass + "\"");
    }
    if(null != style) {
      writer.write("style=\"" + style + "\"");
    }
    writer.write(">");
  }

  public void encodeEnd(FacesContext context, UIComponent component)
      throws IOException {
    if ((context == null) || (component == null)) {
      throw new NullPointerException();
    }
    ResponseWriter writer = context.getResponseWriter();
    writer.write("</" + HTML.DIV_ELEM + ">");
  }
}