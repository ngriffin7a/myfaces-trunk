// x_load.js
// X v3.15, Cross-Browser DHTML Library from Cross-Browser.com
// Copyright (c) 2002,2003,2004 Michael Foster (mike@cross-browser.com)
// This library is distributed under the terms of the LGPL (gnu.org)

// xLoad()
// Call before the window.onload event.
// Only supports files with extensions '.js' and '.css'.
// Automatically loads files with NN4 support if browser is NN4.
// Returns true if browser has minimal dhtml support, else returns false.

function xLoad(url1, url2, etc)
{
  if (document.getElementById || document.all || document.layers) {
    var h, f;
    for (var i=0; i<arguments.length; ++i) {
      h = ''; // html to be written
      f = arguments[i].toLowerCase(); // lowercase file url
      // JS
      if (f.indexOf('.js') != -1) {
        // NN4 Support
        if (document.layers && !window.opera) {
          if (f.indexOf('x_core')) {
            f = f.replace('x_core', 'x_core_nn4');
          }
          else if (f.indexOf('x_event')) {
            f = f.replace('x_event', 'x_event_nn4');
          }
        }
        h = "<script type='text/javascript' src='" + f + "'></script>";
      }
      // CSS
      else if (f.indexOf('.css') != -1) {
        h = "<link rel='stylesheet' type='text/css' href='" + f + "'>";
      }    
      // Write the html into the document
      if (h.length) { document.writeln(h); }
    }
    return true;
  }
  // else browser does not have minimal dhtml support
  return false;
}
// end x_load.js