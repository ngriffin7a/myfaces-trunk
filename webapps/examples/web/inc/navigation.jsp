<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_3.tld" prefix="x"%>
<f:facet name="navigation">
    <x:navigation id="nav"
                  bundle="net.sourceforge.myfaces.examples.resource.example_messages"
                  panelClass="navigation"
                  levelClasses="navlevel1,navlevel2,navlevel3"
                  itemClass="navitem"
                  openItemClass="navitem_open"   >
        <x:navigation_item id="nav-1" key="nav_Home" treeId="/home.jsf" commandClass="navigation_link" />
        <x:navigation_item id="nav-2" key="nav_Examples" commandClass="navigation_link" >
            <x:navigation_item id="nav-2-1" key="nav_Sample_1" treeId="/sample1.jsf" commandClass="navigation_link" />
            <x:navigation_item id="nav-2-2" key="nav_Sample_2" treeId="/sample2.jsf" commandClass="navigation_link" />
            <x:navigation_item id="nav-2-3" key="nav_Components" commandClass="navigation_link" >
                <x:navigation_item id="nav-2-3-1" key="nav_Simple_list" treeId="/simpleList.jsf" commandClass="navigation_link" />
                <x:navigation_item id="nav-2-3-2" key="nav_Sortable_list" treeId="/simpleSortList.jsf" commandClass="navigation_link" />
                <x:navigation_item id="nav-2-3-3" key="nav_Selectbox" treeId="/selectbox.jsf" commandClass="navigation_link" />
                <x:navigation_item id="nav-2-3-4" key="nav_FileUpload" treeId="/fileupload.jsf" commandClass="navigation_link" />
            </x:navigation_item>
        </x:navigation_item>
        <x:navigation_item id="nav-3" key="nav_Documentation" commandClass="navigation_link" >
            <x:navigation_item id="nav-3-1" key="nav_Features" treeId="/features.jsf" commandClass="navigation_link"/>
        </x:navigation_item>
        <x:navigation_item id="nav-4" key="nav_Options" treeId="/options.jsf" commandClass="navigation_link" />
        <x:navigation_item id="nav-s1" columnClass="navseparator" label=" " />
        <x:navigation_item id="nav-5" key="nav_Info" commandClass="navigation_link" >
            <x:navigation_item id="nav-5-1" key="nav_Contact" treeId="/contact.jsf" commandClass="navigation_link" />
            <x:navigation_item id="nav-5-2" key="nav_Copyright" treeId="/copyright.jsf" commandClass="navigation_link" />
        </x:navigation_item>
    </x:navigation>
</f:facet>
