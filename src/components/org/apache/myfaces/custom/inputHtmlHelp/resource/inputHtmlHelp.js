function resetHelpValue(helpText, id)
{
    var element=document.getElementById(id);
    if(element.value==helpText)
    {
        element.value="";
    }
}

function selectText(id)
{
    var element=document.getElementById(id);
    element.select();
}