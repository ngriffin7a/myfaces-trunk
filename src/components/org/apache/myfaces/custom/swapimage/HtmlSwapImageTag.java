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
package org.apache.myfaces.custom.swapimage;

import org.apache.myfaces.taglib.html.HtmlGraphicImageTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.2  2005/01/09 21:56:33  tomsp
 *          added new component swapimage
 *
 *          Revision 1.1  2005/01/09 12:36:58  tomsp
 *          added new component swapimage
 *
 */
public class HtmlSwapImageTag
        extends HtmlGraphicImageTagBase
{
    private static final String RENDERER_TYPE = "org.apache.myfaces.SwapImage";

    private String _swapImageUrl;
    private String _activeImageUrl;

    public String getComponentType()
    {
        return HtmlSwapImage.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        ((HtmlSwapImage) component).setSwapImageUrl(_swapImageUrl);
        ((HtmlSwapImage) component).setActiveImageUrl(_activeImageUrl);
    }

    public void setSwapImageUrl(String swapImageUrl)
    {
        _swapImageUrl = swapImageUrl;
    }

    public void setActiveImageUrl(String activeImageUrl)
    {
        _activeImageUrl = activeImageUrl;
    }

    public void setOnmouseover(String onmouseover)
    {
        throw new UnsupportedOperationException(HtmlSwapImageTag.class.getName() + ".setOnmouseover not supported.");
    }

    public void setOnmousedown(String onmousedown)
    {
        throw new UnsupportedOperationException(HtmlSwapImageTag.class.getName() + ".setOnmousedown not supported.");
    }

    public void setOnmouseup(String onmouseup)
    {
        throw new UnsupportedOperationException(HtmlSwapImageTag.class.getName() + ".setOnmouseup not supported.");
    }

    public void setOnmousemove(String onmousemove)
    {
        throw new UnsupportedOperationException(HtmlSwapImageTag.class.getName() + ".setOnmousemove not supported.");
    }

    public void setOnmouseout(String onmouseout)
    {
        throw new UnsupportedOperationException(HtmlSwapImageTag.class.getName() + ".setOnmouseout not supported.");
    }
}
