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
package org.apache.myfaces.xdoclet;

import java.util.Properties;

import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xjavadoc.XField;

/**
 * @xdoclet.taghandler namespace="WapFaces"
 * @author  <a href="mailto:Jiri.Zaloudek@ivancice.cz">Jiri Zaloudek</a> (latest modification by $Author$)
 * @version $Revision$ $Date$ 
 * $Log$
 * Revision 1.1  2004/12/30 09:37:27  matzew
 * added a new RenderKit for WML. Thanks to Jirí Žaloudek
 *
 */
public class WapFacesTagHandler extends XDocletTagSupport {
    private int iter = 0;
    
    public String componentFamily() throws XDocletException {
        return (getTagValue(FOR_CLASS, "wapfaces.tag", "componentFamily", null, null, false, false));
    }
    
    public String rendererType() throws XDocletException {
        return (getTagValue(FOR_CLASS, "wapfaces.tag", "rendererType", null, null, false, false));
    }
    
    public String tagName() throws XDocletException {
        return (getTagValue(FOR_CLASS, "wapfaces.tag", "tagName", null, null, false, false));
    }

    public String tagBaseClass() throws XDocletException {
        return (getTagValue(FOR_CLASS, "wapfaces.tag", "tagBaseClass", null, null, false, false));
    }
    
    public String bodyContent() throws XDocletException {
        return (getTagValue(FOR_CLASS, "wapfaces.tag", "bodyContent", null, null, false, false));
    }
    
    private String attributeName(){
        XField field = getCurrentField();
        return(field.getName());        
    }
    
    private String attributeType(){
        XField field = getCurrentField();
        return(field.getType().getQualifiedName());        
    }
    
    /** Return initial value for attribute. 
     * @return init value, if initValue isn't set reurn null 
     */
    public String attributeInitValue() throws XDocletException {
        String value = getTagValue(FOR_FIELD, "wapfaces.attribute", "initValue", null, null, false, false);
        
        if (value == null) return(attributeNullValue());
        return (value);
    }
    
    /** Return the right null value according to type of property*/
    public String attributeNullValue() throws XDocletException {
        String type = attributeType();
        if (type == null) throw new XDocletException("Missing(or wrong) attribute 'type' in property declaration.");
        if (type.equals("boolean")) return("false");
        if (type.equals("byte")) return("0");
        if (type.equals("short")) return("0");
        if (type.equals("int")) return("0");
        if (type.equals("long")) return("0");
        if (type.equals("float")) return("0.0");
        if (type.equals("double")) return("0.0");
        if (type.equals("char")) return("\u0000");
        return("null");
    }
    
    public void ifIsAttributePrimitive(String template) throws XDocletException {
        if (isAttributePrimitive())
            generate(template);
    }
    
    public void ifIsNotAttributePrimitive(String template) throws XDocletException {
        if (!isAttributePrimitive())
            generate(template);
    }
    
    
    private boolean isAttributePrimitive() throws XDocletException {
        String type = attributeType();
        if (type == null) throw new XDocletException("Missing(or wrong) attribute 'type' in property declaration.");
        if (type.equals("boolean") || type.equals("byte") || type.equals("short") ||
        type.equals("int") || type.equals("long") || type.equals("float") ||
        type.equals("double") || type.equals("char"))
            return (true);
        
        return(false);
    }
    
    /** Return the class name according to primitive type. If field type isn't primitive, return the type. */
    public String classForType() throws XDocletException {
        //if (!isAttributePrimitive())  throw new XDocletException("Method 'classForPrimitiveType' can be call only for primitive types. You can test it with method 'ifIsAttributePrimitive'.");
        
        String type = attributeType();
        try {
            if (isAttributePrimitive()){
                if (type != null && type.equals("int")) // int -> Integer
                    return("Integer");
                else
                    return (firstLetterToUpperCase(type));
            }
            return (type);
            
        } catch (Exception ex){
            throw new XDocletException(ex.getMessage());
        }
    }
    
    public String isAttributeRequired() throws XDocletException {
        return(String.valueOf(attributeRequired()));
    }
    
    public void ifIsAttributeRequired(String template) throws XDocletException {
        if (attributeRequired())
            generate(template);
    }
    
    private boolean attributeRequired() throws XDocletException {
        String value = getTagValue(FOR_FIELD, "wapfaces.attribute", "required", null, "false", false, false);

        try {
            return (stringToBoolean(value));
        } catch (NumberFormatException ex) {
            return(false);
        }
    }
    
    public String isRtExprValue() throws XDocletException {
        return(String.valueOf(rtExprValue()));
    }
    
    private boolean rtExprValue() throws XDocletException {
        String value = getTagValue(FOR_FIELD, "wapfaces.attribute", "rtexprvalue", null, "false", false, false);

        try {
            return (stringToBoolean(value));
        } catch (NumberFormatException ex) {
            return(false);
        }
    }

    public void ifIsAttributeValueBinding(String template) throws XDocletException {
        if (isAttributeValueBinding())
            generate(template);
    }
    
    public void ifIsNotAttributeValueBinding(String template) throws XDocletException {
        if (!isAttributeValueBinding())
            generate(template);
    }
    
    private boolean isAttributeValueBinding() throws XDocletException {
        String value = getTagValue(FOR_FIELD, "wapfaces.attribute", "valueBinding", null, "false", false, false);
        
        try {
            return (stringToBoolean(value));
        } catch (NumberFormatException ex) {
            return(false);
        }
    }
    
    public void ifIsNotAttributeAbstract(String template) throws XDocletException {
        if (!isAttributeAbstract()) // if is not abstract
            generate(template);
    }
    
    private boolean isAttributeAbstract() throws XDocletException {
        String value = getTagValue(FOR_FIELD, "wapfaces.attribute", "abstract", null, "false", false, false);
        
        try {
            return (stringToBoolean(value));
        } catch (NumberFormatException ex) {
            return(false);
        }
    }
    
    public void ifIsNotAttributeInherit(String template) throws XDocletException {
        if (!isAttributeInherid()) // if is not inherit
            generate(template);
    }
    
    private boolean isAttributeInherid() throws XDocletException {
        String value = getTagValue(FOR_FIELD, "wapfaces.attribute", "inherit", null, "false", false, false);
        
        try {
            return (stringToBoolean(value));
        } catch (NumberFormatException ex) {
            return(false);
        }
    }

    public void ifIsAttributeReplaced(String template) throws XDocletException{
        if (isAttributeReplaced())
            generate(template);
    }
    
    public void ifIsNotAttributeReplaced(String template) throws XDocletException{
        if (!isAttributeReplaced()) // if is not attribute replaced
            generate(template);
    }
    
    private boolean isAttributeReplaced() throws XDocletException {
        boolean isEmpty = getReplacedAttributeName() == null || "".endsWith(getReplacedAttributeName());
        
        return(!isEmpty);
    }

    public String getReplacedAttributeName() throws XDocletException {
        String value = getTagValue(FOR_FIELD, "wapfaces.attribute", "replaceWith", null, null, false, false);
        
        return(value);
    }
    
    public String getterMethodName() throws XDocletException {
        String name = attributeName();
        
        try {
            return (getterPrefix() + firstLetterToUpperCase(name));
        } catch (Exception ex){
            throw new XDocletException(ex.getMessage());
        }
    }
    
    /** returns the attribute name with first letter in upper case */
    public String firstLetterToUpperCaseAttributeName() throws XDocletException {
        String name = attributeName();
        
        try {
            return (firstLetterToUpperCase(name));
        } catch (Exception ex){
            throw new XDocletException(ex.getMessage());
        }
    }
    
    public String setterMethodName() throws XDocletException {
        String name = attributeName();
        try {
            return ("set" + firstLetterToUpperCase(name));
        } catch (Exception ex){
            throw new XDocletException(ex.getMessage());
        }
    }
    
    public void iterateValue() {
        iter = iter + 1;
    }
    
    public String iteratorValue() {
        return (String.valueOf(iter));
    }
    
    public void setIterator(Properties prop) throws XDocletException {
        String value = prop.getProperty("value");
        try {
            iter = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new XDocletException("You have to set property 'value' in method setIterator. 'Value' is a integer number.");
        }
    }
    
    private String getterPrefix(){
        if ("boolean".equals(attributeType()))
            return("is");
        return("get");
    }
    
    private String firstLetterToUpperCase(String str) throws Exception {
        if (str != null && str.length() > 0){
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
            return (str);
        }
        else throw new Exception("Attribut name error. Name must have at least one character.");
    }
    
    private boolean stringToBoolean(String str){
        Boolean bool = Boolean.valueOf(str);
        return(bool.booleanValue());
    }
}
