/**
 * MyFaces configuration
 */
window.myfaces = window.myfaces || {};
myfaces.config = myfaces.config || {};
/**
 * enables updating javax.faces.ViewState across all forms (Mojarra compatibility - https://issues.jboss.org/browse/RFPL-1187)
 */
myfaces.config.no_portlet_env = true;

/**
 * Variable used in Selenium tests for testing JavaScript events. Each event adds its name to this
 * variable. E.g. if onbegin, onbeforedomupdate and oncomplete is set, successful result will be
 * "begin beforedomupdate complete ".
 */
metamerEvents = "";

/**
 * Hides the attributes of a component, header and footer of the page.
 */
function hideControls() {
    jQuery('div.footer').hide();
    jQuery('div.header').hide();
    jQuery('table.attributes').hide()
}

/**
 * Hides the header of the page.
 */
function hideHeader() {
    jQuery('div.header').hide();
}

/**
 * Hides the footer of the page.
 */
function hideFooter() {
    jQuery('div.footer').hide();
}

/**
 * Hides the attributes of a component.
 */
function hideAttributes() {
    jQuery('table.attributes').hide()
}

/**
 * Shows the attributes of a component, header and footer of the page.
 */
function showControls() {
    jQuery('div.footer').show();
    jQuery('div.header').show();
    jQuery('table.attributes').show();
}

/**
 * Shows the header of the page.
 */
function showHeader() {
    jQuery('div.header').show();
}

/**
 * Shows the footer of the page.
 */
function showFooter() {
    jQuery('div.footer').show();
}

/**
 * Shows the attributes of a component.
 */
function showAttributes() {
    jQuery('table.attributes').show();
}

/**
 * Shows or hides the a4j:log above page footer.
 */
function showOrHideLog() {
    if (jQuery('input[id$=a4jLogCheckbox]').is(':checked')) {
        jQuery('div.log-panel').show();
    } else {
        jQuery('div.log-panel').hide();
    }
}

/**
 * Logs information about JSF lifecycle and invoked action/action listeners to the a4j:log.
 */
function updateLog(data) {
    var logEntries = data.replace("[", "").replace("]", "").split(",");

    for (index in logEntries) {
        RichFaces.log.debug(logEntries[index]);
    }
}

