function orgApacheMyfacesJsListenerSetExpressionProperty(
    srcId, destId, property, expression)
{
    var log = true;
    var logStr;

    try
    {

        if(log) logStr+="Source-Element id: "+srcId;

        var srcElem = document.getElementById(srcId);

        if(!srcElem)
        {
            srcElem = document.getElementsByName(srcId);
        }

        if(log) logStr+="\n Source-element: "+srcElem;
        if(log) logStr+="\n Type of source-element: "+typeof(srcElem);
        if(log && typeof(srcElem)=='object') logStr+="\n Constructor : " +srcElem.constructor;
        if(log && orgApacheMyfacesJsListenerIsArray(srcElem))
            logStr+="\n Source-element is an array";

        if(log) logStr += "\n\n  Destination-element id: "+destId;

        var destElem = document.getElementById(destId);

        if(!destElem)
        {
            destElem = document.getElementsByName(destId);
        }

        if(log) logStr+="\n Destination-element: "+destElem;
        if(log) logStr+="\n Type of destination-element: "+typeof(destElem);
        if(log && typeof(destElem)=='object') logStr+="Constructor : " +destElem.constructor;
        if(log && orgApacheMyfacesJsListenerIsArray(destElem))
            logStr+="\n Destination-element is an array";

        if(log) logStr+="\n\n  Expression before parsing: "+expression;

        expression = orgApacheMyfacesJsListenerReplaceMakro(expression, "srcElem",srcElem);
        expression = orgApacheMyfacesJsListenerReplaceMakro(expression, "destElem",destElem);

        if(log) logStr+="\n Expression after parsing: "+expression;

        var value = eval(expression);

        if(property)
        {
            var destElemStr = "destElem";

            if(orgApacheMyfacesJsListenerIsArray(destElem))
                destElemStr+="[0]";

            destElemStr+=".";

            var valueStr;

            if(typeof (value) == 'string')
            {
                valueStr = "'"+value+"'";
            }
            else
            {
                valueStr = value;
            }

            var propertySetStr = destElemStr+property+"="+valueStr+";";

            if(log) logStr+="\n\n  Property set string: "+propertySetStr;

            eval(propertySetStr);
        }
    }
    catch(e)
    {
        var errorString = 'Error encountered : ';
        errorString += e['message'];
        errorString += logStr;

        if(document.all)
        {
            e['description']=errorString;
            throw e;
        }
        else
        {
            throw errorString;
        }
    }

}

function orgApacheMyfacesJsListenerIsArray (array)
{
    return false;

    /*
    if ((array != null) && (typeof array == "object"))
    {
        if(array.constructor == Array)
            return true;

        try
        {
            var elem = array[0];
        }
        catch(e)
        {
            return false;
        }

        return true;
    }

    return false; */
}

function orgApacheMyfacesJsListenerReplaceMakro(expression, macroName, elem)
{

    var regEx = new RegExp("\\$"+macroName, "g");

    if(orgApacheMyfacesJsListenerIsArray(elem))
    {
        var arrRegEx = new RegExp("\\$"+macroName+"[\\[]([0-9]+)[\\]]", "g");
        expression = expression.replace(arrRegEx,macroName+"[$1]");
        expression = expression.replace(regEx,macroName+"[0]");
    }
    else
    {
        expression = expression.replace(regEx,macroName);
    }

    return expression;
}