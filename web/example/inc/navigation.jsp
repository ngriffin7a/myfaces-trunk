<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_3.tld" prefix="x"%>
<x:navigation id="nav" bundle="net.sourceforge.myfaces.examples.resource.example_messages" cssClass="navigation">
    <x:navigation_item id="nav-1" key="nav_Home" treeId="/example/home.jsf" />
    <x:navigation_item id="nav-2" key="nav_Examples" >
        <x:navigation_item id="nav-2-1" key="nav_Sample_1" treeId="/example/sample1.jsf"/>
        <x:navigation_item id="nav-2-2" key="nav_Sample_2" treeId="/example/sample2.jsf"/>
        <x:navigation_item id="nav-2-3" key="nav_Components" >
            <x:navigation_item id="nav-2-3-1" key="nav_Simple_list" treeId="/example/simpleList.jsf"/>
            <x:navigation_item id="nav-2-3-2" key="nav_Sortable_list" treeId="/example/simpleSortList.jsf"/>
            <x:navigation_item id="nav-2-3-3" key="nav_Selectbox" treeId="/example/selectbox.jsf"/>
        </x:navigation_item>
    </x:navigation_item>
    <x:navigation_item id="nav-3" key="nav_Documentation" >
        <x:navigation_item id="nav-3-1" key="nav_Features" treeId="/example/features.jsf"/>
    </x:navigation_item>
    <x:navigation_item id="nav-4" key="nav_Options" treeId="/example/options.jsf"/>
    <x:navigation_item id="nav-5" key="nav_Info" >
        <x:navigation_item id="nav-5-1" key="nav_Contact" treeId="/example/contact.jsf"/>
        <x:navigation_item id="nav-5-2" key="nav_Copyright" treeId="/example/copyright.jsf"/>
    </x:navigation_item>
</x:navigation>
