<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"%>
<f:facet name="navigation">
    <x:panel_navigation id="nav"
                  styleClass="navigation"
                  separatorClass="navseparator"
                  itemClass="navitem"
                  activeItemClass="navitem_active"
                  openItemClass="navitem_open"   >
        <x:command_navigation id="nav_1" value="#{example_messages['nav_Home']}" action="go_home" styleClass="navigation_link" />
        <x:command_navigation id="nav_2" value="#{example_messages['nav_Examples']}" styleClass="navigation_link" >
            <x:command_navigation id="nav_2_1" value="#{example_messages['nav_Sample_1']}" action="go_sample1" styleClass="navigation_link" />
            <x:command_navigation id="nav_2_2" value="#{example_messages['nav_Sample_2']}" action="go_sample2" styleClass="navigation_link" />
            <x:command_navigation id="nav_2_3" value="#{example_messages['nav_Components']}" styleClass="navigation_link" >
                <x:command_navigation id="nav_2_3_1" value="#{example_messages['nav_Simple_list']}" action="go_simpleList" styleClass="navigation_link" />
                <x:command_navigation id="nav_2_3_2" value="#{example_messages['nav_Sortable_list']}" action="go_simpleSortList" styleClass="navigation_link" />
                <x:command_navigation id="nav_2_3_3" value="#{example_messages['nav_Selectbox']}" action="go_selectbox" styleClass="navigation_link" />
                <x:command_navigation id="nav_2_3_4" value="#{example_messages['nav_FileUpload']}" action="go_fileupload" styleClass="navigation_link" />
                <x:command_navigation id="nav_2_3_5" value="#{example_messages['nav_TabbedPane']}" action="go_tabbedPane" styleClass="navigation_link" />
            </x:command_navigation>
        </x:command_navigation>
        <x:command_navigation id="nav_3" value="#{example_messages['nav_Documentation']}" styleClass="navigation_link" >
            <x:command_navigation id="nav_3_1" value="#{example_messages['nav_Features']}" action="go_features" styleClass="navigation_link"/>
        </x:command_navigation>
        <x:command_navigation id="nav_4" value="#{example_messages['nav_Options']}" action="go_options" styleClass="navigation_link" />
        <x:output_navigation/>
        <x:command_navigation id="nav_5" value="#{example_messages['nav_Info']}" styleClass="navigation_link" >
            <x:command_navigation id="nav_5_1" value="#{example_messages['nav_Contact']}" action="go_contact" styleClass="navigation_link" />
            <x:command_navigation id="nav_5_2" value="#{example_messages['nav_Copyright']}" action="go_copyright" styleClass="navigation_link" />
        </x:command_navigation>
    </x:panel_navigation>
</f:facet>
