// x_dom.js
// X v3.15, Cross-Browser DHTML Library from Cross-Browser.com
// Copyright (c) 2002,2003,2004 Michael Foster (mike@cross-browser.com)
// This library is distributed under the terms of the LGPL (gnu.org)

/* xWalkTree()
   Perform a preorder traversal
   on the subtree starting at oNode
   and pass each Element node to fnVisit.
*/
function xWalkTree(oNode, fnVisit)
{
  if (oNode) {
    if (oNode.nodeType == 1) {fnVisit(oNode);}
    for (var c = oNode.firstChild; c; c = c.nextSibling) {
      xWalkTree(c, fnVisit);
    }
  }
}

/* xGetComputedStyle
   Works in Moz and Op. For finding width in IE this works as long as padding and border use pixel units in the CSS.
   For sProp use the css property name, not the object property name.
*/
function xGetComputedStyle(oEle, sProp)
{
  var p = 0;
  if(document.defaultView && document.defaultView.getComputedStyle){
    p = document.defaultView.getComputedStyle(oEle,'').getPropertyValue(sProp)
  }
  else if(oEle.currentStyle) {
    // convert css property name to object property name for IE (can this be done with RE?)
    var a = sProp.split('-');
    sProp = a[0];
    for (var i=1; i<a.length; ++i) {
      c = a[i].charAt(0);
      sProp += a[i].replace(c, c.toUpperCase());
    }   
    p = oEle.currentStyle[sProp];
  }
  return parseInt(p) || 0;
}

/* xGetElementsByClassName()
   Returns an array of elements which are
   descendants of parentEle and have tagName and clsName.
   If parentEle is null or not present, document will be used.
   if tagName is null or not present, "*" will be used.
*/
function xGetElementsByClassName(clsName, parentEle, tagName) {
	var elements = null;
	var found = new Array();
	var re = new RegExp('\\b'+clsName+'\\b', 'i');
	if (!parentEle) parentEle = document;
	if (!tagName) tagName = '*';
	if (parentEle.getElementsByTagName) {elements = parentEle.getElementsByTagName(tagName);}
	else if (document.all) {elements = document.all.tags(tagName);}
	if (elements) {
		for (var i = 0; i < elements.length; ++i) {
			if (elements[i].className.search(re) != -1) {
				found[found.length] = elements[i];
			}
		}
	}
	return found;
}

/* xGetElementsByAttribute
   Return an array of all sTag elements whose sAtt attribute matches sRE.
   sAtt can also be a property name but the property must be of type string.
*/
function xGetElementsByAttribute(sTag, sAtt, sRE)
{
  var a, list, found = new Array(), re = new RegExp(sRE, 'i');
  if (document.getElementsByTagName) {list = document.getElementsByTagName(sTag);}
  else if (document.all) {list = document.all.tags(sTag);}
  if (list) {
    for (var i = 0; i < list.length; ++i) {
      a = list[i].getAttribute(sAtt);
      if (!a) {a = list[i][sAtt];}
      if (typeof(a)=='string' && a.search(re) != -1) {
        found[found.length] = list[i];
      }
    }
  }
  return found;
}

/* xGetElementsByAttribute Test Results (Win2K)
  ('div', 'id', 'Col');        // op, moz, ie
  ('span', 'class', 'fw');     // op, moz, !ie
  ('span', 'className', 'fw'); // op, moz, ie
  ('input', 'name', 'inp');    // op, moz, ie
  ('input', 'name', '[2-4]');  // op, moz, ie
  ('input', 'type', 'submit'); // op, moz, ie
  ('input', 'type', 'hidden'); // op, moz, ie
  ('textarea', 'rows', '14');  // op, moz, !ie
  ('textarea', 'rows', '.');   // op, moz, !ie
  ('textarea', 'id', '.');     // op, moz, ie
  ('form', 'onsubmit', '.');   // op, moz, !ie
  ('form', 'name', '.');       // op, moz, ie
  ('a', 'href', '.');          // op, moz, ie
  ('a', 'target', '.');        // op, moz, ie
*/

// end x_dom.js
