window.metamer = window.metamer || {};

window.metamer.utils = (function () {
    // private functions / variables

    return{
        // public functions / variables

        isElementInViewPort: function (element) {
            // taken from http://stackoverflow.com/questions/123999/how-to-tell-if-a-dom-element-is-visible-in-the-current-viewport#answer-15203639
            var rect = element.getBoundingClientRect();
            var vWidth = window.innerWidth || doc.documentElement.clientWidth;
            var vHeight = window.innerHeight || doc.documentElement.clientHeight;
            var efp = function (x, y) {
                return document.elementFromPoint(x, y);
            };

            // Return false if it's not in the viewport
            if (rect.right < 0 || rect.bottom < 0
                    || rect.left > vWidth || rect.top > vHeight)
                return false;

            // Return true if any of its four corners are visible
            return (
                    element.contains(efp(rect.left, rect.top))
                    || element.contains(efp(rect.right, rect.top))
                    || element.contains(efp(rect.right, rect.bottom))
                    || element.contains(efp(rect.left, rect.bottom))
                    );
        },
        scrollToView: function (element) {
            element.scrollIntoView();
        }
    };
})();