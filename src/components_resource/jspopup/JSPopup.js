/*
 * Copyright 2004 The Apache Software Foundation.
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

function orgApacheMyfacesPopup(popupId)
{
    this.popupId = popupId;
    this.display = orgApacheMyfacesPopupDisplay;
    this.hide = orgApacheMyfacesPopupHide;
    this.redisplay=orgApacheMyfacesPopupRedisplay;
}

function orgApacheMyfacesPopupDisplay(ev)
{
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

    x-=5;
    y-=5;

    var popupElem = document.getElementById(this.popupId);

    if(popupElem.style.display!="block")
    {
        popupElem.style.display="block";
        popupElem.style.left=""+x+"px";
        popupElem.style.top=""+y+"px";
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
}