/*
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
package net.sourceforge.myfaces.codegen.component;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;

import java.io.File;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentGeneratorTask
        extends MatchingTask
{
    //private static final Log log = LogFactory.getLog(ComponentInspectorTask.class);

    private File _basedir;
    private File _destdir;
    private File _velocityLoaderPath;

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
        DirectoryScanner ds = getDirectoryScanner(_basedir);
        ds.scan();

        ComponentGenerator generator = new ComponentGenerator(_destdir, _velocityLoaderPath);

        String[] files = ds.getIncludedFiles();
        for (int i = 0; i < files.length; i++)
        {
            String xmlFileName = files[i];
            generator.generate(new File(_basedir, xmlFileName));
        }
    }

    public static void main(String[] args)
    {
    }

}
