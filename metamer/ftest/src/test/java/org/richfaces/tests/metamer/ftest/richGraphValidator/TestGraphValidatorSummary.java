/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richGraphValidator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.testng.annotations.Test;

/**
 * Test for page /faces/components/richGraphValidator/allWithSummary.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestGraphValidatorSummary extends AbstractGraphValidatorTest {

    @Override
    public String getComponentTestPagePath() {
        return "richGraphValidator/allWithSummary.xhtml";
    }

    @Test
    @CoversAttributes("summary")
    public void testSummary() {
        String msg = "My own validation message!";
        graphValidatorAttributes.set(GraphValidatorAttributes.summary, msg);

        setInputSecretCorrect();//all inputs are correct now, not submitted yet
        setWrongSettingForGroup(Group.ValidationGroupAllComponents);
        applyChanges();

        assertFalse(getPage().getGraphValidatorGlobalMessages().advanced().isVisible(), "Global messages should not be visible.");
        RichFacesMessages msgs = getPage().getGraphValidatorMessages();
        assertTrue(msgs.advanced().isVisible(), "Graph validator messages should be visible.");
        assertEquals(msgs.size(), 1, "There should be one message.");
        assertEquals(msgs.getItem(0).getSummary(), msg, "Summary of message.");
    }
}
