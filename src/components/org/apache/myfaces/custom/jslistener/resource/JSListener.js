function orgApacheMyfacesJsListenerSetExpressionProperty(srcId, destId, property, expression)
{
    var srcElem = document.getElementById(srcId);
    var destElem = document.getElementById(destId);

    expression = expression.replace(/\$srcElem/g,"srcElem");
    expression = expression.replace(/\$destElem/g,"destElem");

    var value = eval(expression);

    if(property)
    {
        var propertySetStr = "destElem."+property+"='"+value+"';";

        eval(propertySetStr);
    }
}