package net.sourceforge.myfaces.component;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface MyFacesComponent
{
    //Standard attributes
    public static final String ID_ATTR = "id";
    public static final String MODEL_REFERENCE_ATTR = "modelReference";
    public static final String RENDERER_TYPE_ATTR = "rendererType";
    public static final String VALUE_ATTR = "value";
    public static final String VALID_ATTR = "valid";
    public static final String PARENT_ATTR = "parent";
    public static final String CONVERTER_ATTR = "converter";
    public static final String LABEL_ATTR = "label";
    public static final String FORMAT_PATTERN_ATTR = "formatPattern";

    //MyFaces extensions
    public static final String TRANSIENT_ATTR = "transient";

    //MyFaces internal
    public static final String STRING_VALUE_ATTR = "stringValue";

    /**
     * Components returning true affirm that their value need not be saved by the StateRenderer.
     * @return true if value need not be state saved, false if value must be saved and restored
     */
    public boolean isTransient();
}
