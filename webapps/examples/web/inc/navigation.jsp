<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"%>
<x:panelNavigation id="nav"
              styleClass="navigation"
              separatorClass="navseparator"
              itemClass="navitem"
              activeItemClass="navitem_active"
              openItemClass="navitem_open"   >
    <x:commandNavigation id="nav_1" value="#{example_messages['nav_Home']}" action="go_home" />
    <x:commandNavigation id="nav_2" value="#{example_messages['nav_Examples']}" >
        <x:commandNavigation id="nav_2_1" value="#{example_messages['nav_Sample_1']}" action="go_sample1" />
        <x:commandNavigation id="nav_2_2" value="#{example_messages['nav_Sample_2']}" action="go_sample2" />
        <x:commandNavigation id="nav_2_3" value="#{example_messages['nav_Validate']}" action="go_validate" />
        <x:commandNavigation id="nav_2_4" value="#{example_messages['nav_Components']}" immediate="" >
            <x:commandNavigation id="nav_2_4_1" value="#{example_messages['nav_dataTable']}" action="go_dataTable" />
            <x:commandNavigation id="nav_2_4_2" value="#{example_messages['nav_sortTable']}" action="go_sortTable" />
            <x:commandNavigation id="nav_2_4_3" value="#{example_messages['nav_Selectbox']}" action="go_selectbox" />
            <x:commandNavigation id="nav_2_4_4" value="#{example_messages['nav_FileUpload']}" action="go_fileupload" />
            <x:commandNavigation id="nav_2_4_5" value="#{example_messages['nav_TabbedPane']}" action="go_tabbedPane" />
            <x:commandNavigation id="nav_2_4_6" value="#{example_messages['nav_Calendar']}" action="go_calendar" />
            <x:commandNavigation id="nav_2_4_7" value="#{example_messages['nav_dataList']}" action="go_dataList" />
            <x:commandNavigation id="nav_2_4_8" value="#{example_messages['nav_tree']}" action="go_tree" />
            <x:commandNavigation id="nav_2_4_9" value="#{example_messages['nav_rdfTicker']}" action="go_rssticker" />
            <x:commandNavigation id="nav_2_4_10" value="#{example_messages['nva_dataScroller']}" action="go_datascroller" />
        </x:commandNavigation>
    </x:commandNavigation>
    <x:commandNavigation id="nav_3" value="#{example_messages['nav_Documentation']}" >
        <x:commandNavigation id="nav_3_1" value="#{example_messages['nav_Features']}" action="go_features"/>
    </x:commandNavigation>
    <x:commandNavigation id="nav_4" value="#{example_messages['nav_Options']}" action="go_options" />
    <f:verbatim>&nbsp;</f:verbatim>
    <x:commandNavigation id="nav_5" value="#{example_messages['nav_Info']}" >
        <x:commandNavigation id="nav_5_1" value="#{example_messages['nav_Contact']}" action="go_contact" />
        <x:commandNavigation id="nav_5_2" value="#{example_messages['nav_Copyright']}" action="go_copyright" />
    </x:commandNavigation>
</x:panelNavigation>
