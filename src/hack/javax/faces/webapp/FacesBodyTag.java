/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package javax.faces.webapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * Patch for the EA2 release of Sun's JSF API that corrects the
 * "doStartTag() does nothing" issue.
 *
 * Two ways to apply this patch:
 * 1. Make sure that this class comes before the original api class in the classpath.
 *    Tomcat puts the WEB-INF/classes dir before the lib dir, so normally this should
 *    work.
 * or
 * 2. You can also patch the jsf-api.jar directly. Just remove the original class
 *    from the jar file and add this version. You can use the jar tool in the
 *    Java JDK or WinZip or any other zip tool.
 *
 * Sorry for the inconvenience, but because of current JSF license we are not allowed
 * to publish the jsf-api.jar together with MyFaces.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class FacesBodyTag
        extends FacesTag
        implements BodyTag
{
    public int getDoStartValue() throws JspException
    {
        return BodyTag.EVAL_BODY_BUFFERED;
    }

    public void doInitBody() throws JspException
    {
    }

    public int doAfterBody() throws JspException
    {
        return SKIP_BODY;
    }

    public int getDoEndValue() throws JspException
    {
        return Tag.EVAL_PAGE;
    }
}
