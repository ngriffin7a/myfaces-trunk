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

import org.apache.myfaces.codegen.CodeGenerator;
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
        ComponentDef component = parser.parse(xmlFile);
        try
        {
            generateComponent(component);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void generateComponent(ComponentDef component) throws IOException
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
                                       ComponentDef component) throws IOException
    {
        VelocityContext vc = getVelocityContext();
        vc.put("c", component);
        generateCode(_velocityTemplatesDir.getAbsolutePath(),
                     "Component.vsl",
                     printWriter);

    }



}
