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

/**
 * When document is ready, check browser's url, whether it contains param 'atts'. In case it has such param, parse the param and
 * set the attributes with Metamer.AttributesSetter.
 *
 * E.g.
 *   link: localhost:8080/metamer/faces/components/richContextMenu/simple.xhtml?atts={"onclick":"alert(\"hello\")", "showDelay":500, "mode":"ajax"}
 *   will open page, set attributes (onclick=alert("hello"), showDelay=500, mode=ajax) and submit the attributes form (only if some of the attributes has changed).
 *
 *   link: localhost:8080/metamer/faces/components/richTree/simpleSwingTreeNode.xhtml?atts={"attributes:dir":"ltr", "treeNode1Attributes:dir":"rtl", "treeNode2Attributes:dir":"ltr" }
 *   will open page, set attribute 'dir' of tree, treeNode1 and treeNode2 alternately to 'ltr' and 'rtl' and submit the attributes (only if some of the attributes has changed).
 */
$(document).ready(
        function () {
            // parse attributes to set from window.location.search (from actual url after '?')
            function parseAttributesFromURL(callback) {
                var ATTS = 'atts=';
                function getJSONFromSearchString(value) {
                    return JSON.parse(value.replace(ATTS, ''));
                }
                // unescape the string and split it to separate params
                var params = decodeURI(window.location.search.substr(1)).split('&');
                for (var i = 0; i < params.length; i++) {
                    if (params[i].indexOf(ATTS) !== -1) {
                        callback(getJSONFromSearchString(params[i]));
                        return;
                    }
                }
            }
            function setAttributes(attsToSetMap) {
                // set each attribute
                for (var key in attsToSetMap) {
                    if (attsToSetMap.hasOwnProperty(key)) {
                        Metamer.AttributesSetter.setAttribute(key, attsToSetMap[key]);
                    }
                }
                // submit if needed
                Metamer.AttributesSetter.submit();// is only invoked when some attribute has changed
            }
            // call the function
            parseAttributesFromURL(setAttributes);
        }
);