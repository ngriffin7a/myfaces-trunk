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
/*
 * Utilities used by MyFaces x:htmlEditor tag
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
var myFacesKupuTextToLoad;
var myFacesKupuOriginalDocOnLoad;

function myFacesKupuSet(text) {
	myFacesKupuTextToLoad = text;
	
	var onLoadSrc;
    if( document.all ) // IE
		onLoadSrc = document.body;
	else // Mozilla
		onLoadSrc = window;

	myFacesKupuOriginalDocOnLoad = onLoadSrc.onload;
	onLoadSrc.onload = myFacesKupuInit;
}

function myFacesKupuInit(){
	if( myFacesKupuOriginalDocOnLoad )
		myFacesKupuOriginalDocOnLoad();

	kupu = startKupu();
	kupu.getInnerDocument().documentElement.getElementsByTagName('body')[0].innerHTML = myFacesKupuTextToLoad;
}