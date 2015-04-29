window.JSErrorStorage = {
    count: 0,
    errors: {},
    add: function (err) {
        this.errors[this.count++] = err;
    },
    getMessages: function () {
        var s = [];
        for (var i = 0; i < this.count; i++) {
            s[i] = this.errors[i].message;
        }
        return s;
    }
};
window.addEventListener("error", function (evt) {
    window.JSErrorStorage.add(evt);
});