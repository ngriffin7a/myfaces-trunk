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

import java.io.File;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentInspectorTask
        extends MatchingTask
{
    //private static final Log log = LogFactory.getLog(ComponentInspectorTask.class);

    private File _basedir;
    private File _destdir;
    private boolean _overwrite = false;

    public File getBasedir()
    {
        return _basedir;
    }

    public void setBasedir(File basedir)
    {
        _basedir = basedir;
    }

    public File getDestdir()
    {
        return _destdir;
    }

    public void setDestdir(File destdir)
    {
        _destdir = destdir;
    }

    public boolean isOverwrite()
    {
        return _overwrite;
    }

    public void setOverwrite(boolean overwrite)
    {
        _overwrite = overwrite;
    }

    public void execute() throws BuildException
    {
        DirectoryScanner ds = getDirectoryScanner(_basedir);
        ds.scan();

        ComponentInspector inspector = new ComponentInspector(_destdir, _overwrite);

        String[] files = ds.getIncludedFiles();
        for (int i = 0; i < files.length; i++)
        {
            String fileName = files[i];
            int dot = fileName.lastIndexOf('.');
            String className;
            if (dot == -1)
            {
                //no extension
                className = fileName.replace('/', '.').replace('\\', '.');
            }
            else
            {
                //no extension
                className = fileName.substring(0, dot).replace('/', '.').replace('\\', '.');
            }

            inspector.inspect(className);
        }
    }

    public static void main(String[] args)
    {
    }

}
