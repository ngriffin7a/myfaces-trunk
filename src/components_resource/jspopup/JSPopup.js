var orgApacheMyfacesPopupCurrentlyOpenedPopup;

function orgApacheMyfacesPopup(popupId,displayAtDistanceX,displayAtDistanceY)
{
    this.popupId = popupId;
    this.displayAtDistanceX=displayAtDistanceX;
    this.displayAtDistanceY=displayAtDistanceY;    
    this.display = orgApacheMyfacesPopupDisplay;
    this.hide = orgApacheMyfacesPopupHide;
    this.redisplay=orgApacheMyfacesPopupRedisplay;
}

function orgApacheMyfacesPopupDisplay(ev)
{

    if(orgApacheMyfacesPopupCurrentlyOpenedPopup!=null)
        orgApacheMyfacesPopupCurrentlyOpenedPopup.style.display="none";

    var elem;
    var x;
    var y;

    if(document.all)
    {
        elem = window.event.srcElement;
        x=window.event.x;
        y=window.event.y;
    }
    else
    {
        elem = ev.target;
        x=ev.pageX;
        y=ev.pageY;
    }

    x+=this.displayAtDistanceX;
    y+=this.displayAtDistanceY;

    var popupElem = document.getElementById(this.popupId);

    if(popupElem.style.display!="block")
    {
        popupElem.style.display="block";
        popupElem.style.left=""+x+"px";
        popupElem.style.top=""+y+"px";
        orgApacheMyfacesPopupCurrentlyOpenedPopup = popupElem;
    }
}

function orgApacheMyfacesPopupHide()
{
    var popupElem = document.getElementById(this.popupId);
    popupElem.style.display="none";
}

function orgApacheMyfacesPopupRedisplay()
{
    var popupElem = document.getElementById(this.popupId);
    popupElem.style.display="block";
    orgApacheMyfacesPopupCurrentlyOpenedPopup = popupElem;
}