<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_4.tld" prefix="x"%>
<f:facet name="navigation">
    <x:navigation id="nav"
                  bundle="example_messages"
                  panelClass="navigation"
                  levelClasses="navlevel1,navlevel2,navlevel3"
                  separatorClass="navseparator"
                  itemClass="navitem"
                  openItemClass="navitem_open"   >
        <x:navigation_item id="nav-1" key="nav_Home" action="go_home" commandClass="navigation_link" />
        <x:navigation_item id="nav-2" key="nav_Examples" commandClass="navigation_link" >
            <x:navigation_item id="nav-2-1" key="nav_Sample_1" action="go_sample1" commandClass="navigation_link" />
            <x:navigation_item id="nav-2-2" key="nav_Sample_2" action="go_sample2" commandClass="navigation_link" />
            <x:navigation_item id="nav-2-3" key="nav_Components" commandClass="navigation_link" >
                <x:navigation_item id="nav-2-3-1" key="nav_Simple_list" action="go_simpleList" commandClass="navigation_link" />
                <x:navigation_item id="nav-2-3-2" key="nav_Sortable_list" action="go_simpleSortList" commandClass="navigation_link" />
                <x:navigation_item id="nav-2-3-3" key="nav_Selectbox" action="go_selectbox" commandClass="navigation_link" />
                <x:navigation_item id="nav-2-3-4" key="nav_FileUpload" action="go_fileupload" commandClass="navigation_link" />
            </x:navigation_item>
        </x:navigation_item>
        <x:navigation_item id="nav-3" key="nav_Documentation" commandClass="navigation_link" >
            <x:navigation_item id="nav-3-1" key="nav_Features" action="go_features" commandClass="navigation_link"/>
        </x:navigation_item>
        <x:navigation_item id="nav-4" key="nav_Options" action="go_options" commandClass="navigation_link" />
        <x:navigation_separator/>
        <x:navigation_item id="nav-5" key="nav_Info" commandClass="navigation_link" >
            <x:navigation_item id="nav-5-1" key="nav_Contact" action="go_contact" commandClass="navigation_link" />
            <x:navigation_item id="nav-5-2" key="nav_Copyright" action="go_copyright" commandClass="navigation_link" />
        </x:navigation_item>
    </x:navigation>
</f:facet>
