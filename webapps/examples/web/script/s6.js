function setupCollapsible(initialState) // initialState = 'collapsed' or 'expanded'
{
  var i, t, list = xGetElementsByClassName('collapsible', document, 'DIV');
  for (i = 0; i < list.length; ++i) {
    t = list[i]; // target element
    do { t = t.previousSibling; } while (t && t.nodeType != 1); // find prev sib (trigger element)
    if (t) {
      t.style.cursor = 'pointer';
      t.target = list[i];
      t.collapsed = (initialState.charAt(0)=='c') ? false : true;
      t.onclick = headingOnClick;
      t.onclick();
    }
  }
}
function headingOnClick()
{
  var d, t = 'Click to ';
  if (this.collapsed) {
    d = 'block';
    t += 'Collapse';
  }
  else {
    d = 'none';
    t += 'Expand';
  }
  this.target.style.display = d;
  this.setAttribute('title', t);
  this.collapsed = !this.collapsed;
}
