/*
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
package org.apache.myfaces.config;

import java.io.InputStream;
import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * Parses a single InputStream into an unmarshalled faces config Object, that can
 * be processed by a respective FacesConfigDispenser.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.1  2004/07/07 00:25:05  o_rossmueller
 * tidy up config/confignew package (moved confignew classes to package config)
 *
 * Revision 1.3  2004/07/01 22:05:09  mwessendorf
 * ASF switch
 *
 * Revision 1.2  2004/06/04 23:51:48  o_rossmueller
 * Digester-based config parser/dispenser
 *
 * Revision 1.1  2004/05/17 14:28:27  manolito
 * new configuration concept
 *
 */
public interface FacesConfigUnmarshaller
{
    public Object getFacesConfig(InputStream in, String systemId) throws IOException, SAXException;
}
