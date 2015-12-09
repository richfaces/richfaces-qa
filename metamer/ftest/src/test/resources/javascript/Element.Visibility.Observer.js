window.Element = window.Element || {};
window.Element.Visibility = window.Element.Visibility || {};

window.Element.Visibility.Observer = (function () {

    MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

    var ITEM_NAME = 'records';
    var ITEMS_SEPARATOR = ';;';
    var REGEXP_VISIBLE = /<visible>/;

    function addToSessionStorage(newRecord) {
        if (!!newRecord) {// do not insert empty record
            var newIsVisible = REGEXP_VISIBLE.test(newRecord);
            var recordsBefore = sessionStorage.getItem(ITEM_NAME);
            if (!!recordsBefore) {// some records before
                var recordsArray = recordsBefore.split(ITEMS_SEPARATOR);
                var lastIsVisible = REGEXP_VISIBLE.test(recordsArray[recordsArray.length - 1]);
                // insert only changed states
                if (lastIsVisible ? !newIsVisible : newIsVisible) {// XOR
                    sessionStorage.setItem(ITEM_NAME, recordsBefore + ITEMS_SEPARATOR + newRecord);
                }
            } else {// no records before
                sessionStorage.setItem(ITEM_NAME, newRecord);
            }
        }
    }
    function cleanRecordsInSessionStorage() {
        sessionStorage.removeItem(ITEM_NAME);
    }

    function isElementVisible(element) {
        return $(element).is(':visible');
    }

    function generateStatusImageVisibleMessage(element) {
        return 'at <' + new Date().getTime() + '> element was <'
                + (isElementVisible(element) === true ? '' : 'not ') + 'visible>';
    }

    function watchForElementUsingMutationObserver(element) {
        // create observer
        var observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                addToSessionStorage(generateStatusImageVisibleMessage(mutation.target));
            });
        });
        // create target and configuration
        var config = {
            attributes: true
        };
        // connect observer
        observer.observe(element, config);
    }

    function watchForElementChangeByRepeatedlyInvokingCheckFunction($element) {
        function repeatableAction(check, repeats, delay) {
            var iteration = 0;
            var intervalID = window.setInterval(function () {
                check();
                if (++iteration === repeats) {
                    window.clearInterval(intervalID);
                }
            }, delay);
        }

        function check() {
            addToSessionStorage(generateStatusImageVisibleMessage($element));
        }
        var repeats = 80;
        var delay = 100;
        repeatableAction(check, repeats, delay);
    }

    return {
        getRecords: function () {
            return sessionStorage.getItem(ITEM_NAME).split(ITEMS_SEPARATOR);
        },
        watchForVisibilityChangeOfElement: function (element) {
            cleanRecordsInSessionStorage();
            var $element = $(element);
            if (!!MutationObserver) {
                watchForElementUsingMutationObserver(element);
            } else {// MutationObserver does not work on PhantomJS 1.x
                watchForElementChangeByRepeatedlyInvokingCheckFunction($element);
            }
        }
    };
})();