/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation of a RichFaces behavior in faces-config.xml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21077 $
 */
@XmlRootElement(name = "behavior", namespace = "http://java.sun.com/xml/ns/javaee")
public class Behavior implements Serializable {

    private static final long serialVersionUID = -1L;
    private String description;
    private String behaviorId;
    private Class<?> behaviorClass;
    private List<Attribute> attributes;

    @XmlElement(name = "behavior-id", namespace = "http://java.sun.com/xml/ns/javaee")
    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    @XmlElement(name = "description", namespace = "http://java.sun.com/xml/ns/javaee")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "behavior-class", namespace = "http://java.sun.com/xml/ns/javaee")
    public Class<?> getBehaviorClass() {
        return behaviorClass;
    }

    public void setBehaviorClass(Class<?> behaviorClass) {
        this.behaviorClass = behaviorClass;
    }

    @XmlElement(name = "property", namespace = "http://java.sun.com/xml/ns/javaee")
    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
