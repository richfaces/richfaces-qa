/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.a4jQueue;

import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @version $Revision: 22680 $
 */
public enum QueueAttributes implements AttributeEnum {

    binding, //not tested
    id, //not tested
    ignoreDupResponses, //tested
    name, //not tested
    onbeforedomupdate, //do not work -> should be also added here? https://issues.jboss.org/browse/RF-11711
    oncomplete, //not tested, to be deleted https://issues.jboss.org/browse/RF-11711
    onerror, //do not work, -> should be also added here? https://issues.jboss.org/browse/RF-11711
    onevent, //not tested, to be deleted https://issues.jboss.org/browse/RF-11711
    onrequestdequeue, //tested
    onrequestqueue, //tested
    onsubmit, //not tested, to be deleted https://issues.jboss.org/browse/RF-11711
    rendered, //tested
    requestDelay, //tested
    timeout //tested
}
