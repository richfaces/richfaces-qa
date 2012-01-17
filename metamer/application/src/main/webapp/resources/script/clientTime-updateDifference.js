if (Metamer == undefined) {
	var Metamer = {};
}

if (Metamer.ClientTime == undefined) {
	Metamer.ClientTime = {};
}

Metamer.ClientTime.updateDifference = function (differenceTimeElement, beginTimeElement, eventTimeId, reverseOrder) {
	var maxEventTime = 0;
	
	for (var i = 1; i < 100; i++) {
		var id = eventTimeId + i + ":outputTime";
		var element = document.getElementById(id);
		if (!element) {
			if (i == 1) {
				throw new Error("no eventTime element with id '" + id + "' was found");
			}
			break;
		}
		var eventTime = parseInt(element.title);
		maxEventTime = Math.max(eventTime, maxEventTime);
	}
	
	var beginTime = parseInt(beginTimeElement.title);
	
	var difference = beginTime - maxEventTime;
	
	if (reverseOrder) {
		difference = 0 - difference;
	}
	
	jQuery(differenceTimeElement).text(difference);
}