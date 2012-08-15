/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richFileUpload;

import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public enum FileUploadAttributes implements AttributeEnum {

    acceptedTypes,//tested in se2
    addLabel,
    binding,
    clearAllLabel,
    clearLabel,
    data,//tested in se2, not working "https://issues.jboss.org/browse/RF-12039"
    deleteLabel,
    dir,
    disabled,
    doneLabel,//tested in se2
    execute,//tested in se2
    fileUploadListener,
    id,
    lang,
    limitRender,//tested in se2
    listHeight,
    maxFilesQuantity,//tested in se2
    noDuplicate,
    onbeforedomupdate,//tested in se2 not working "https://issues.jboss.org/browse/RF-12037"
    onbegin,//tested in se2 not working "https://issues.jboss.org/browse/RF-12037"
    onclear,
    onclick,
    oncomplete,//tested in se2 not working "https://issues.jboss.org/browse/RF-12037"
    ondblclick,
    onfilesubmit,//tested in se2
    onkeydown,
    onkeypress,
    onkeyup,
    onmousedown,
    onmousemove,
    onmouseout,
    onmouseover,
    onmouseup,
    ontyperejected,//tested in se2
    onuploadcomplete,//tested in se2
    render,
    rendered,
    serverErrorLabel,
    sizeExceededLabel,//tested in se2
    status,//tested in se2
    style,
    styleClass,
    title,
    uploadLabel
}
