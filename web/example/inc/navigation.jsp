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
<x:navigation id="nav">
    <x:navigation_item id="nav-1" label="Home" treeId="/example/index.jsf" />
    <x:navigation_item id="nav-2" label="Examples" treeId="/example/examples.jsf">
        <x:navigation_item id="nav-2-1" label="Sample 1" treeId="/example/sample1.jsf"/>
        <x:navigation_item id="nav-2-2" label="Sample 2" treeId="/example/sample2.jsf"/>
        <x:navigation_item id="nav-2-3" label="Simple lists" treeId="/example/simpleList.jsf"/>
        <x:navigation_item id="nav-2-4" label="Simple sortable list" treeId="/example/simpleSortList.jsf"/>
    </x:navigation_item>
    <x:navigation_item id="nav-3" label="Documentation" >
        <x:navigation_item id="nav-3-1" label="Features" treeId="/example/features.jsf"/>
    </x:navigation_item>
    <x:navigation_item id="nav-4" label="Info" >
        <x:navigation_item id="nav-4-1" label="Contact" treeId="/example/contact.jsf"/>
        <x:navigation_item id="nav-4-2" label="Copyright" treeId="/example/copyright.jsf"/>
    </x:navigation_item>
</x:navigation>
