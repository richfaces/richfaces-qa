if (Metamer == undefined) {
	var Metamer = {};
}

if (Metamer.ClientTime == undefined) {
	Metamer.ClientTime = {};
}

Metamer.zeroPad = function(num, places) {
	var zero = places - num.toString().length + 1;
	return Array(+(zero > 0 && zero)).join("0") + num;
};

Metamer.ClientTime.formatTime = function(date) {
	return date.getHours() + ":" + Metamer.zeroPad(date.getMinutes(), 2) + ":"
			+ Metamer.zeroPad(date.getSeconds(), 2) + "."
			+ Metamer.zeroPad(date.getMilliseconds(), 3);
};

Metamer.ClientTime.refresh = function(element) {
	var now = new Date();
	jQuery(element).text(Metamer.ClientTime.formatTime(now)).attr("title",
			now.getTime());
};
