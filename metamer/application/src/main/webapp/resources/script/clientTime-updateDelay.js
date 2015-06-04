if (Metamer == undefined) {
    var Metamer = {};
}

if (Metamer.ClientTime == undefined) {
    Metamer.ClientTime = {};
}

Metamer.ClientTime.updateDelay = function (delayElement, timeElement, eventsOutputElements) {
    // get maximum event time
    var maxEventTime = 0;
    eventsOutputElements.forEach(function (element) {
        maxEventTime = Math.max(parseInt(element.title), maxEventTime);
    });

    // get begin time
    var beginTime = parseInt(timeElement.title);

    // count delay
    var delay = beginTime - maxEventTime;
    delay = delay >= 0 ? delay : -delay;

    // save counted delay to input
    var $delayElement = jQuery(delayElement);
    var previousVal = $delayElement.val();
    previousVal = previousVal ? ', ' + previousVal : '';
    $delayElement.val(delay + previousVal);
};

Metamer.ClientTime.updateMedian = function (delayElement, medianTimeElement) {

    // function for counting median from an (unsorted) array
    function median(values) {
        values.sort(function (a, b) {
            return a - b;
        });
        var half = Math.floor(values.length / 2);
        if (values.length % 2)
            return values[half];
        else
            return (values[half - 1] + values[half]) / 2.0;
    }

    var $delayElement = $(delayElement);
    var $medianTimeElement = $(medianTimeElement);

    // collect all delays to an array
    var times = [];
    $delayElement.val().split(', ').forEach(function (element) {
        times.push(parseInt(element));
    });

    // count and save median to input
    $medianTimeElement.val(median(times));
};

