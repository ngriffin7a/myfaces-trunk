<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_4.tld" prefix="x"%>
<f:facet name="navigation">
    <x:navigation id="nav"
                  bundle="example_messages"
                  panelClass="navigation"
                  separatorClass="navseparator"
                  itemClass="navitem"
                  activeItemClass="navitem_active"
                  openItemClass="navitem_open"   >
        <x:navigation_item id="nav_1" key="nav_Home" action="go_home" commandClass="navigation_link" />
        <x:navigation_item id="nav_2" key="nav_Examples" commandClass="navigation_link" >
            <x:navigation_item id="nav_2_1" key="nav_Sample_1" action="go_sample1" commandClass="navigation_link" />
            <x:navigation_item id="nav_2_2" key="nav_Sample_2" action="go_sample2" commandClass="navigation_link" />
            <x:navigation_item id="nav_2_3" key="nav_Components" commandClass="navigation_link" >
                <x:navigation_item id="nav_2_3_1" key="nav_Simple_list" action="go_simpleList" commandClass="navigation_link" />
                <x:navigation_item id="nav_2_3_2" key="nav_Sortable_list" action="go_simpleSortList" commandClass="navigation_link" />
                <x:navigation_item id="nav_2_3_3" key="nav_Selectbox" action="go_selectbox" commandClass="navigation_link" />
                <x:navigation_item id="nav_2_3_4" key="nav_FileUpload" action="go_fileupload" commandClass="navigation_link" />
            </x:navigation_item>
        </x:navigation_item>
        <x:navigation_item id="nav_3" key="nav_Documentation" commandClass="navigation_link" >
            <x:navigation_item id="nav_3_1" key="nav_Features" action="go_features" commandClass="navigation_link"/>
        </x:navigation_item>
        <x:navigation_item id="nav_4" key="nav_Options" action="go_options" commandClass="navigation_link" />
        <x:navigation_separator/>
        <x:navigation_item id="nav_5" key="nav_Info" commandClass="navigation_link" >
            <x:navigation_item id="nav_5_1" key="nav_Contact" action="go_contact" commandClass="navigation_link" />
            <x:navigation_item id="nav_5_2" key="nav_Copyright" action="go_copyright" commandClass="navigation_link" />
        </x:navigation_item>
    </x:navigation>
</f:facet>
