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
package org.apache.myfaces.codegen.component;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentGeneratorTask
        extends MatchingTask
{
    private static final Log log = LogFactory.getLog(ComponentGeneratorTask.class);

    private File _basedir;
    private File _destdir;
    private File _velocityLoaderPath;
    private String _includes;

    public File getBasedir()
    {
        return _basedir;
    }

    public void setBasedir(File basedir)
    {
        _basedir = basedir;
    }

    public String getIncludes()
    {
        return _includes;
    }

    public void setIncludes(String includes)
    {
        _includes = includes;
    }

    public File getDestdir()
    {
        return _destdir;
    }

    public void setDestdir(File destdir)
    {
        _destdir = destdir;
    }

    public File getVelocityLoaderPath()
    {
        return _velocityLoaderPath;
    }

    public void setVelocityLoaderPath(File velocityLoaderPath)
    {
        _velocityLoaderPath = velocityLoaderPath;
    }

    public void execute() throws BuildException
    {
        log.info("Executing ComponentGeneratorTask: ");
        log.info("baseDir : "+_basedir);
        log.info("destDir : "+_destdir);
        log.info("velocityLoaderPath : "+_velocityLoaderPath);

        ComponentGenerator generator = new ComponentGenerator(_destdir, _velocityLoaderPath);

        if(_includes==null)
        {
            DirectoryScanner ds = getDirectoryScanner(_basedir);
            ds.scan();

            String[] files = ds.getIncludedFiles();
            for (int i = 0; i < files.length; i++)
            {
                String xmlFileName = files[i];
                log.info("on xmlFileName : "+files[i]);
                generator.generate(new File(_basedir, xmlFileName));
            }
        }
        else
        {
            //_includes = _includes.replace('/','\\');

            log.info("on xmlFileName : "+_basedir+"/"+_includes);
            generator.generate(new File(_basedir, _includes));
        }
    }

    public static void main(String[] args)
    {
    }

}
