/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package net.sourceforge.myfaces.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.Writer;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class CodeGenerator
{
    //private static final Log log = LogFactory.getLog(CodeGenerator.class);

    private VelocityContext _velocityContext = null;

    protected VelocityContext getVelocityContext()
    {
        _velocityContext = new VelocityContext();
        return _velocityContext;
    }

    protected void generateCode(String loaderPath, String template, Writer writer)
    {
        try
        {
            Velocity.setProperty("resource.loader", "file");
            Velocity.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            Velocity.setProperty("file.resource.loader.path", loaderPath);
            Velocity.init();

            Template velocityTemplate = Velocity.getTemplate(template, "ISO-8859-1");
            velocityTemplate.merge(_velocityContext, writer);
            writer.flush();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
