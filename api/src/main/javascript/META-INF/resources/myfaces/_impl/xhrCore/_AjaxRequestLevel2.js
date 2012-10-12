/* Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * this method is used only for pure multipart form parts
 * otherwise the normal method is used
 * IT is a specialized request which uses the form data
 * element for the handling of forms
 */
_MF_CLS(_PFX_XHR+"_AjaxRequestLevel2", myfaces._impl.xhrCore._AjaxRequest, {

    _sourceForm: null,

    constructor_: function(arguments) {
        this._callSuper("constructor_", arguments);
        //TODO xhr level2 can deal with real props

    },

    getFormData: function() {
        var ret;

        if (this._context._mfInternal.xhrOp.indexOf("multipart") != -1) {
            ret = new FormData(this._sourceForm);
        } else {
            ret = this._callSuper("getFormData");
        }
        return ret;
    },

    /**
        * applies the content type, this needs to be done only for xhr
        * level1
        * @param xhr
        * @private
        */
       _applyContentType: function(xhr) {
            //content type is not set in case of xhr level2 unless we have a pps
            //but then we have to deal with the file handling differently
            if (this._context._mfInternal.xhrOp.indexOf("multipart") == -1) {
               //xhr.setRequestHeader(this._CONTENT_TYPE, contentType);
                this._callSuper("_applyContentType", xhr);
            }
       },

    _formDataToURI: function() {
        //i assume in xhr level2 form data takes care of the http get parametrisation
        return "";
    },

    _getTransport: function() {
        return new XMLHttpRequest();
    }




});