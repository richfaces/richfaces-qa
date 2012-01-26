function updateMessage(containerElem, data) {
    author = '<span style="font-weight:bold" class="author">' + data.author + '</span>: ';
    message = '<span class="message">' + data.text + '</span>';
    timestamp = ' <span class="timestamp">[' + data.timestamp + ']</span>';
    jQuery(containerElem).children("div").replaceWith('<div>' + author + message + timestamp + '</div>');
}
