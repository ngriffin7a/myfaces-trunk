<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>
<x:panelNavigation id="nav"
              styleClass="navigation"
              separatorClass="navseparator"
              itemClass="navitem"
              activeItemClass="navitem_active"
              openItemClass="navitem_open"   >
    <x:commandNavigation id="nav_1" value="#{example_messages['nav_Home']}" action="go_home">
        <f:param name="param" value="value"></f:param>
    </x:commandNavigation>
    <x:commandNavigation id="nav_2" value="#{example_messages['nav_Examples']}" >
        <x:commandNavigation id="nav_2_1" value="#{example_messages['nav_Sample_1']}" action="go_sample1" />
        <x:commandNavigation id="nav_2_2" value="#{example_messages['nav_Sample_2']}" action="go_sample2" />
        <x:commandNavigation id="nav_2_3" value="#{example_messages['nav_Validate']}" action="go_validate" />
        <x:commandNavigation id="nav_2_4" value="#{example_messages['nav_Components']}" >
            <x:commandNavigation id="nav_2_4_1" value="#{example_messages['nav_aliasBean']}" action="go_aliasBean" />
            <x:commandNavigation id="nav_2_4_1_2" value="#{example_messages['nav_buffer']}" action="go_buffer" />
            <x:commandNavigation id="nav_2_4_2" value="#{example_messages['nav_dataTable']}" action="go_dataTable" />
            <x:commandNavigation id="nav_2_4_3" value="#{example_messages['nav_sortTable']}" action="go_sortTable" />
            <x:commandNavigation id="nav_2_4_4" value="#{example_messages['nav_Selectbox']}" action="go_selectbox" />
            <x:commandNavigation id="nav_2_4_5" value="#{example_messages['nav_FileUpload']}" action="go_fileupload" />
            <x:commandNavigation id="nav_2_4_6" value="#{example_messages['nav_TabbedPane']}" action="go_tabbedPane" />
            <x:commandNavigation id="nav_2_4_7" value="#{example_messages['nav_Calendar']}" action="go_calendar" />
            <x:commandNavigation id="nav_2_4_71" value="#{example_messages['nav_Popup']}" action="go_popup" />
            <x:commandNavigation id="nav_2_4_72" value="#{example_messages['nav_JsListener']}" action="go_jslistener" />            
            <x:commandNavigation id="nav_2_4_8" value="#{example_messages['nav_Date']}" action="go_date" />
            <x:commandNavigation id="nav_2_4_81" value="#{example_messages['nav_InputHtml']}" action="go_inputHtml" />
            <x:commandNavigation id="nav_2_4_82" value="#{example_messages['nav_InputHtmlHelp']}" action="go_inputHtmlHelp" />
            <x:commandNavigation id="nav_2_4_9" value="#{example_messages['nav_dataList']}" action="go_dataList" />
            <x:commandNavigation id="nav_2_4_10" value="#{example_messages['nav_tree']}" action="go_tree" />
            <x:commandNavigation id="nav_2_4_11" value="#{example_messages['nav_treeTable']}" action="go_treeTable"/>
            <x:commandNavigation id="nav_2_4_12" value="#{example_messages['nav_rssTicker']}" action="go_rssticker" />
            <x:commandNavigation id="nav_2_4_13" value="#{example_messages['nav_dataScroller']}" action="go_datascroller" />
            <x:commandNavigation id="nav_2_4_14" value="#{example_messages['nav_panelstack']}" action="go_panelstack" />
            <x:commandNavigation id="nav_2_4_15" value="#{example_messages['nav_css']}" action="go_css" />
            <x:commandNavigation id="nav_2_4_16" value="#{example_messages['nav_newspaperTable']}" action="go_newspaperTable" />
            <x:commandNavigation id="nav_2_4_17" value="#{example_messages['nav_swapimage']}" action="go_swapimage" />
            <x:commandNavigation id="nav_2_4_18" value="#{example_messages['nav_forceId']}" action="go_forceId" />
            <x:commandNavigation id="nav_2_4_19" value="#{example_messages['nav_selectOneCountry']}" action="go_selectOneCountry" />
            <x:commandNavigation id="nav_2_4_20" value="#{example_messages['nav_crossDataTable']}" action="go_crossDataTable" />
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
