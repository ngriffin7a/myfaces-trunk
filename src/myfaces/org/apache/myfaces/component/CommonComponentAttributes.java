package net.sourceforge.myfaces.component;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface CommonComponentAttributes
{
    //Standard attributes
    public static final String ID_ATTR = "id";
    public static final String CONVERTER_ATTR = "converter";
    public static final String MODEL_REFERENCE_ATTR = "modelReference";
    public static final String RENDERED_ATTR = "rendered";

    public static final String RENDERER_TYPE_ATTR = "rendererType";
    public static final String VALUE_ATTR = "value";
    public static final String VALID_ATTR = "valid";
    public static final String PARENT_ATTR = "parent";
    public static final String COMPONENT_ID_ATTR = "componentId";
    public static final String CLIENT_ID_ATTR = "clientId";

    //MyFaces internal
    public static final String STRING_VALUE_ATTR = "stringValue";


    //JSF internal
    public static final String FACET_PARENT_ATTR = "javax.faces.component.FacetParent";

}
