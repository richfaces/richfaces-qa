/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.editor;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.frame.FrameIndexLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestAutosaving extends AbstractGrapheneTest {

    JQueryLocator outputFromEditor = jq(".rf-p-b");
    JQueryLocator editorArea = jq("body.rf-ed-b");
    FrameIndexLocator frameLocator = new FrameIndexLocator(0);

    private final String[] EXPECTED_PARAGRAPHS = {
        "\"Little Red Riding Hood\" is a famous fairy tale about a young girl's encounter with a wolf. "
            + "The story has been changed considerably in its history and subject to numerous modern adaptations and readings.",

        "The version most widely known today is based on the Brothers Grimm variant. "
            + "It is about a girl called Little Red Riding Hood, after the red hooded cape or cloak she wears. "
            + "The girl walks through the woods to deliver food to her sick grandmother.",

        "A wolf wants to eat the girl but is afraid to do so in public. "
            + "He approaches the girl, and she naïvely tells him where she is going. "
            + "He suggests the girl pick some flowers, which she does. In the meantime, he goes to "
            + "the grandmother's house and gains entry by pretending to be the girl. He swallows the "
            + "grandmother whole, and waits for the girl, disguised as the grandmother.",

        "When the girl arrives, she notices he looks very strange to be her grandma. In most retellings",

        "A hunter, however, comes to the rescue and cuts the wolf open. Little "
            + "Red Riding Hood and her grandmother emerge unharmed. They fill the wolf's body with "
            + "heavy stones, which drown him when he falls into a well. Other versions of the story have "
            + "had the grandmother shut in the closet instead of eaten, and some have Little Red Riding "
            + "Hood saved by the hunter as the wolf advances on her rather than after she is eaten.",

        "The tale makes the clearest contrast between the safe world of the village and the "
            + "dangers of the forest, conventional antitheses that are essentially medieval, though no written versions are as old as that." };

    @Test
    public void testContentOfEditor() {

        String contentOfEditorInput = selenium.getText(outputFromEditor);

        int j = 1;
        for (String i : EXPECTED_PARAGRAPHS) {
            assertTrue(contentOfEditorInput.contains(i), "The " + j + ". paragraph of initial content is corrupted!");
            j++;
        }
    }

    @Test
    public void testAutoSaving() {

        fail("Implement me correctly");

        selenium.selectFrame(frameLocator);

        selenium.fireEvent(editorArea, Event.FOCUS);

        String text = selenium.getText(editorArea);

        eraseInput(editorArea);

        selenium.typeKeys(editorArea, "Test String");

        guardXhr(selenium).fireEvent(editorArea, Event.BLUR);

        String outputAfterSave = selenium.getText(outputFromEditor);

    }

}
