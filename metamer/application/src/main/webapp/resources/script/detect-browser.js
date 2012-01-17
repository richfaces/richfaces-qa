(function($) {
    var userAgent = navigator.userAgent.toLowerCase();

    jQuery.browser = {
        chrome: /chrome/.test( userAgent ),
        safari: /webkit/.test( userAgent ) && !/chrome/.test( userAgent ),
        webkit: /webkit/.test( userAgent ),
        opera: /opera/.test( userAgent ),
        konqueror: /konqueror/.test( userAgent ),
        msie: /msie/.test( userAgent ) && !/opera/.test( userAgent ),
        mozilla: /mozilla/.test( userAgent ) && !/(compatible|webkit)/.test( userAgent ),
        version: typeof window.opera != "undefined" ? opera.version() : (userAgent.match( /.+(?:rv|it|ra|ie|me)[\/: ]([\d.]+)/ ) || [])[1],
        os : navigator.platform
    };
    
    if (jQuery.browser.mozilla) {
    	var matcher = /firefox\/([\d.]+)/;
    	if (matcher.test(userAgent)) {
    		jQuery.browser.version = userAgent.match(matcher)[1];
    	}
    }

    if (jQuery.browser.chrome) {
        jQuery.browser.name = "Chrome";
    } else if (jQuery.browser.safari) {
        jQuery.browser.name = "Safari";
    } else if (jQuery.browser.opera) {
        jQuery.browser.name = "Opera";
    } else if (jQuery.browser.msie) {
        jQuery.browser.name = "Internet Explorer";
    } else if (jQuery.browser.mozilla) {
        jQuery.browser.name = "Firefox";
    } else if (jQuery.browser.konqueror) {
        jQuery.browser.name = "Konqueror";
    } else {
        jQuery.browser.name = "Unknown browser";
    }

})(jQuery);