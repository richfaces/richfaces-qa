function attributesOndblclick(elem, key, klass) {
    var endsWithClassRegExp = /.*Class/;
    var startsWithOnRegExp = /on.*/;

    function setValue(input, valToSet) {
        var $input = $(input);
        if (valToSet !== $input.val()) {
            $input.val(valToSet).change();
        }
    }

    if (startsWithOnRegExp.test(key)) {
        var valueToSet = 'alert(\'' + key + ' triggered (' + klass + ')\')';
        setValue(elem, valueToSet);
    } else if (endsWithClassRegExp.test(key)) {
        var valueToSet = 'metamer-ftest-class';
        setValue(elem, valueToSet);
    }
}

