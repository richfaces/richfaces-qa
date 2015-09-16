/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
window.Metamer = window.Metamer || {};

window.Metamer.AttributesSetter = window.Metamer.AttributesSetter || (function () {

    var lastAttributeElement;
    var dirty = false;

    function getCorrectIdOfInput(nameWithTable) {
        var count = (nameWithTable.split(':').length - 1);
        if (count === 0) {
            nameWithTable = ':' + nameWithTable;
        }
        return nameWithTable;
    }

    function getAttributeInput(name) {
        name = getCorrectIdOfInput(name);
        var input = $('[id$=\'' + name + 'Input\']:first');
        if (!input.length) {
            throw 'Attribute input not found! Cannot find input { [id$=\'' + name + 'Input\']:first } !';
        }
        return input;
    }

    function getTagname(element) {
        return element.prop("tagName").toLowerCase();
    }

    function getCorrectValueDependingOnTagname(tagname, value) {
        switch (tagname) {
            case 'input':
                return (value === 'null') ? value = '' : value;
            case 'select':
            case 'table':
                return (value === '') ? value = 'null' : value;
        }
    }

    function getNumberOfCheckedOptions(input) {
        return input.find('input[type=radio]:checked').length;
    }

    function markAttributeChanged(input) {
//        input.css('background', 'darkgreen');
//        input.css('color', 'white');
        dirty = true;
    }

    return{
        checkAttributeIsSetToValue: function (name, value) {
            return this.getAttribute(name) === getCorrectValueDependingOnTagname(getTagname(getAttributeInput(name)), value);
        },
        checkAttributesAreSetToValues: function (names, values) {
            var result = names.length === values.length;
            for (var i = 0; i < names.length; i++) {
                result &= this.checkAttributeIsSetToValue(names[i], values[i]);
            }
            return result;
        },
        getAttribute: function (name) {
            var input = getAttributeInput(name);
            var tagname = getTagname(input);
            switch (tagname) {
                case 'input':
                case 'select':
                    return getCorrectValueDependingOnTagname(tagname, input.val());
                case 'table':
                    var checkedOptions = input.find('input[type=radio]:checked');
                    if (checkedOptions.length === 0) {
                        return 'null';
                    } else {
                        return checkedOptions.next('label').text().trim();
                    }
            }
        }
        ,
        isDirty: function () {
            return dirty;
        },
        setAttribute: function (name, value) {
            var input = getAttributeInput(name);
            var tagname = getTagname(input);
            var visibleOptionText = getCorrectValueDependingOnTagname(tagname, value);
            switch (tagname) {
                case 'input':
                    if (input.val() !== visibleOptionText) {
                        input.val(visibleOptionText);
                        markAttributeChanged(input);
                    }
                    break;
                case 'select':
                    var correctOption = input.find('option').filter(function () {
                        return $(this).text().trim() === visibleOptionText;
                    });
                    if (!correctOption.length) {
                        throw 'No such option <' + value + '> found for attribute <' + name + '>.';
                    }
                    var valueToSet = correctOption.val();
                    if (input.val() !== valueToSet) {
                        input.val(valueToSet);
                        markAttributeChanged(input);
                    }
                    break;
                case 'table':
                    var checkedSize = getNumberOfCheckedOptions(input);
                    var label = input.find('label').filter(function () {
                        return $(this).text().trim() === visibleOptionText;
                    });
                    if (!label.length) {
                        throw 'No such option <' + value + '> found for attribute <' + name + '>.';
                    }
                    input = label.prev('input[type=radio]');
                    if (!input.prop('checked') && !(checkedSize === 0 && visibleOptionText === 'null')) {
                        input.prop('checked', true);
                        markAttributeChanged(label);
                    }
                    break;
            }
            lastAttributeElement = input;
        },
        setAttributes: function (names, values) {
            var sameLength = names.length === values.length;
            if (!sameLength) {
                throw "The 'names' and 'values' arguments should have the same length!";
            }
            for (var i = 0; i < names.length; i++) {
                this.setAttribute(names[i], values[i]);
            }
        },
        submit: function () {
            if (dirty) {
                dirty = false;
                lastAttributeElement.closest("form").submit();
            }
        }
    };
})();