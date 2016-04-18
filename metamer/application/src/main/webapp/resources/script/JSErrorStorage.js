window.JSErrorStorage = {
    errors: [],
    add: function (err) {
        if (!!err.message) {
            this.errors.push(err.message);
        } else if (!!err.target.src) {// have to create custom message, err.toString() does not work
            this.errors.push('failed to load resource: ' + err.target.src);
        }
        else {
            this.errors.push('unknown error: ' + err);
        }
        console.log(err);
    },
    getMessages: function () {
        return this.errors;
    }
};

window.addEventListener('error', function (evt) {
    window.JSErrorStorage.add(evt);
}, true);