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

import net.sourceforge.myfaces.codegen.CodeGenerator;
import org.apache.velocity.VelocityContext;

import java.io.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentGenerator
        extends CodeGenerator
{
    //private static final Log log = LogFactory.getLog(ComponentGenerator.class);

    private static final String GENERATED_CODE_BEGIN_MARKER = "GENERATED CODE BEGIN";
    private static final String GENERATED_CODE_END_MARKER = "GENERATED CODE END";

    private File _destdir;
    private File _velocityTemplatesDir;

    //private String _velocityTemplate = "Component.vsl";

    public ComponentGenerator(File destdir, File velocityTemplatesDir)
    {
        _destdir = destdir;
        _velocityTemplatesDir = velocityTemplatesDir;
    }

    public void generate(File xmlFile)
    {
        ComponentDefinitionParser parser = new ComponentDefinitionParser();
        Component component = parser.parse(xmlFile);
        try
        {
            generateComponent(component);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void generateComponent(Component component) throws IOException
    {
        System.out.println("Generating component " + component.getQualifiedClassName());
        String componentClass = component.getQualifiedClassName();

        File bakFile = new File(_destdir, componentClass.replace('.', '/') + ".bak");
        bakFile.delete();

        File oldFile = new File(_destdir, componentClass.replace('.', '/') + ".java");
        if (!oldFile.exists())
        {
            throw new IllegalStateException("Destination file does not exist");
        }
        if (!oldFile.renameTo(bakFile))
        {
            throw new RuntimeException("Destination file could not be renamed");
        }

        File newFile = new File(_destdir, componentClass.replace('.', '/') + ".java");
        FileWriter fileWriter = new FileWriter(newFile, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        FileReader fileReader = new FileReader(bakFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        //copy lines until "GENERATED CODE BEGIN" marker
        boolean ignore = false;
        boolean generated = false;
        String line = bufferedReader.readLine();
        while (line != null)
        {
            if (!ignore)
            {
                printWriter.println(line);
                if (line.indexOf(GENERATED_CODE_BEGIN_MARKER) != -1)
                {
                    ignore = true;
                }
            }
            else
            {
                if (line.indexOf(GENERATED_CODE_END_MARKER) != -1)
                {
                    generateComponentLines(printWriter, component);
                    generated = true;
                    printWriter.println(line);
                    ignore = false;
                }
            }
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        fileReader.close();

        printWriter.close();
        fileWriter.close();

        if (!generated)
        {
            System.err.println("No markers found - nothing generated for class " + componentClass);
        }
    }


    public void generateComponentLines(PrintWriter printWriter,
                                       Component component) throws IOException
    {
        VelocityContext vc = getVelocityContext();
        vc.put("c", component);
        generateCode(_velocityTemplatesDir.getAbsolutePath(),
                     "Component.vsl",
                     printWriter);

    }



}
