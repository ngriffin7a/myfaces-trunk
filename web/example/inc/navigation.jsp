<!--
/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
//-->
<%@ taglib uri="/WEB-INF/myfaces_ext.tld" prefix="x"%>
<x:navigation id="nav" bundle="net.sourceforge.myfaces.example.example_messages" cssClass="navigation">
    <x:navigation_item id="nav-1" key="nav_Home" treeId="/example/index.jsf" />
    <x:navigation_item id="nav-2" key="nav_Examples" >
        <x:navigation_item id="nav-2-1" key="nav_Sample_1" treeId="/example/sample1.jsf"/>
        <x:navigation_item id="nav-2-2" key="nav_Sample_2" treeId="/example/sample2.jsf"/>
        <x:navigation_item id="nav-2-3" key="nav_Simple_list" treeId="/example/simpleList.jsf"/>
        <x:navigation_item id="nav-2-4" key="nav_Sortable_list" treeId="/example/simpleSortList.jsf"/>
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
