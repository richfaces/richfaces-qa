function attributesOndblclick(elem, key, klass) {
    var $this = $(elem);
    var valToSet = 'alert(\'' + key + ' triggered (' + klass + ')\')';
    if (valToSet !== $this.val()) {
        $this.val(valToSet).change();
    }
}

