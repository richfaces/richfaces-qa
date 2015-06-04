if (Metamer == undefined) {
    var Metamer = {};
}

if (Metamer.ClientTime == undefined) {
    Metamer.ClientTime = {};
}

Metamer.ClientTime.getEventsElements = function (events, eventTimeIdWithoutNumber) {
    var eventsOutputElements = [];
    for (var j = 1; j <= events; j++) {
        eventsOutputElements.push(document.getElementById(eventTimeIdWithoutNumber + j + ':outputTime'));
    }
    return eventsOutputElements;
};
