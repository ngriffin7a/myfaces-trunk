/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.tree;

import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

/**
 * Backer bean for use in example.  Basically makes a TreeNode available.
 *
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class TreeBacker
{
    public TreeNode getTreeData()
    {
        TreeNode treeData = new TreeNodeBase("foo-folder", "Inbox", false);

        // construct a set of fake data (normally your data would come from a database)

        // populate Frank's portion of the tree
        TreeNodeBase personNode = new TreeNodeBase("person", "Frank Foo", false);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo", false));
        TreeNodeBase folderNode = new TreeNodeBase("foo-folder", "Requires Foo Reviewer", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "X050001", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050003", true));
        personNode.getChildren().add(folderNode);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo Recommendation", false));
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Approval", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J050001", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050003", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "E050011", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "R050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "C050003", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Processing", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "X050003", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050011", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "F050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "G050003", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Approval", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J050006", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050007", true));
        personNode.getChildren().add(folderNode);

        treeData.getChildren().add(personNode);

        // populate Betty's portion of the tree
        personNode = new TreeNodeBase("person", "Betty Bar", false);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo", false));
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Reviewer", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "X012000", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X013000", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X014000", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Recommendation", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J010026", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J020002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J030103", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "E030214", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "R020444", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "C010000", true));
        personNode.getChildren().add(folderNode);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo Approval", false));
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Processing", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "T052003", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "T020011", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Approval", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J010002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J030047", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "F030112", true));
        personNode.getChildren().add(folderNode);

        treeData.getChildren().add(personNode);

        return treeData;
    }
}
