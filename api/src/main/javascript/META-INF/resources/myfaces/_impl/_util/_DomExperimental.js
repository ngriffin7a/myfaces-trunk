if (_MF_SINGLTN) {
    _MF_SINGLTN(_PFX_UTIL + "_DomExperimental", myfaces._impl._util._Dom, /** @lends myfaces._impl._util._Dom.prototype */ {
        constructor_:function () {
            this._callSuper("constructor_");
            myfaces._impl._util._Dom = this;
        },

       html5FormDetection:function (item) {
            var browser = this._RT.browser;
            //ie shortcut, not really needed but speeds things up
            if (browser.isIEMobile && browser.isIEMobile <= 8) {
                return null;
            }
            var elemForm = this.getAttribute(item, "form");
            return (elemForm) ? this.byId(elemForm) : null;
        },

        isMultipartCandidate:function (executes) {
            if (this._Lang.isString(executes)) {
                executes = this._Lang.strToArray(executes, /\s+/);
            }

            for (var executable in executes) {
                if (!executes.hasOwnProperty(executable)) continue;
                var element = this.byId(executes[executable]);
                var inputs = this.findByTagName(element, "input", true);
                for (var key in inputs) {
                    if (!inputs.hasOwnProperty(key)) continue;
                    if (this.getAttribute(inputs[key], "type") == "file") return true;
                }
            }
            return false;
        },

        getNamedElementFromForm: function(form, elementId) {
            return form[elementId];
        }
    });
}
