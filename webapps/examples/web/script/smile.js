function changeImage(id, newImage) {
	document.getElementById(id).src = newImage;
}

function setHiddenField(id, newValue) {
	document.getElementById(id).value = newValue;
}

function submitForm(id) {
	document.getElementById(id).submit();
}

function treeExpandCollapse(id) {
	document.getElementById(id).getStyle().setProperty('display','none');
	alert('ec');
}

function positionDesignerSpan(spanid,compid) {
	xMoveTo(spanid,xPageX(compid)-5,xPageY(compid)-5);
	xShow(spanid);
}

function positionDesignerSpan(spanid,compid) {
	xMoveTo(spanid,xPageX(compid)-5,xPageY(compid)-5);
	xShow(spanid);
}

function showDesignerMenu(compid,spanid) {
	xMoveTo(compid,xPageX(spanid),xPageY(spanid));
	xShow(compid);
}

function hideDesignerMenu(compid,spanid) {
	xHide(compid);
}
