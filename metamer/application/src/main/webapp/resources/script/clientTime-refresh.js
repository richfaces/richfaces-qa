if (Metamer == undefined) {
	var Metamer = {};
}

if (Metamer.ClientTime == undefined) {
	Metamer.ClientTime = {};
}

Metamer.ClientTime.refresh = function (element) {
   	var now = new Date();
   	jQuery(element)
		.text(now.format("HH:MM:ss.l", false))
		.attr("title", now.getTime());
}