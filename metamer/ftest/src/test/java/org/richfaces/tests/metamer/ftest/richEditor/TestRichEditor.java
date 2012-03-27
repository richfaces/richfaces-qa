/**
 *
 */
package org.richfaces.tests.metamer.ftest.richEditor;

import static org.jboss.arquillian.ajocado.Graphene.attributeEquals;
import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.editorAttributes;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.lang;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.readonly;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.required;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.requiredMessage;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.skin;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.styleClass;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.title;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.toolbar;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.width;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.frame.FrameIndexLocator;
import org.jboss.arquillian.ajocado.locator.frame.FrameRelativeLocator;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test case for basic functionality of rich:editor on page faces/components/richEditor/simple.xhtml.
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestRichEditor extends AbstractGrapheneTest {

    private JQueryLocator editor = pjq("span[id$=editor:inp]");
    private JQueryLocator editorTextArea = pjq("textarea[id$=editor:inp]");
    private JQueryLocator editorArea = jq("body");
    private FrameIndexLocator frameLocator = new FrameIndexLocator(0);
    private JQueryLocator hSubmit = pjq("input[id$=hButton]");
    private JQueryLocator a4jSubmit = pjq("input[id$=a4jButton]");
    private JQueryLocator validationMsgLoc = pjq("span.rf-msgs-sum");

    private JQueryLocator editorToolbarGroup = jq("span.cke_toolbar");

    private Attribute langAttribute = new Attribute("lang");

    public enum toolbarGroup {
        FILE_SAVE(7),
        CLIPBOARD(14),
        SEARCH_REPLACE(22),
        FORM(28),
        LISTS_ALIGNS(46),
        BASIC_FONT_CTRL(38),
        LINK(59),
        RESOURCES(63),
        STYLES_FONTS(73),
        TEXT_BG_COLOR(77),
        MISC(80);

        int code;

        toolbarGroup(int code) {
            this.code = code;
        }
        int id(){
            return code;
        }
    }

    private String validationMsg1 = "Editor's value cannot be empty!";
    private String validationMsg2 = "Some text in editor is required!";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richEditor/simple.xhtml");
    }

    @BeforeMethod
    public void beforeTestMethod() {
        waitModel.until(elementPresent.locator(editor));
    }

    /**
     * Simplified method to verify skin influence editor's class attribute.
     * Choosen skin should be present as cke_skin_{@skinName} class
     * in editor's container @@class attribute
     * @param skinName
     */
    private void verifySkin(String skinName) {
        editorAttributes.set(skin, skinName);
        String editorClass = selenium.getAttribute(editor.getAttribute(new Attribute("class")));
        assertTrue(editorClass.contains("cke_skin_" + skinName),
            "Seems that skin '" + skinName + "' didn't influence editor's @class attribute!");
    }


    @Test
    public void testHeight() {

        String height = "250px";
        editorAttributes.set(EditorAttributes.height, height);

        // height style is set for area used as editor canvas (not for whole editor component)
        // String style = selenium.getAttribute(
        //    editor.getDescendant(jq("table.cke_editor tr:eq(1) td")).getAttribute(styleAttribute));
        String style = selenium.getStyle(
            editor.getDescendant(jq("table.cke_editor tr:eq(1) td")), CssProperty.HEIGHT);
        assertTrue(style != null && style.contains(height));
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11394")
    public void testLang() {
        String langVal = "xyz";
        editorAttributes.set(lang, langVal);

        assertTrue(langVal.equals(selenium.getAttribute(editorTextArea.getAttribute(langAttribute))));
    }

    @Test
    public void testOnBlur() {
        testFireEvent(Event.BLUR, editorTextArea);
    }

    @Test
    public void testOnChange() {
        testFireEvent(Event.CHANGE, editorTextArea);
    }

    @Test
    public void testOnFocus() {
        testFireEvent(Event.FOCUS, editorTextArea);
    }

    @Test
    public void testOnInit() {
        testFireEvent(new Event("init"), editorTextArea);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RFPL-1658")
    public void testReadonly() {

        editorAttributes.set(readonly, Boolean.TRUE);

        String readOnlyAttrVal = selenium.getAttribute(
            editorTextArea.getAttribute(new Attribute("readonly")));
        assertTrue("true".equals(readOnlyAttrVal));

        selenium.selectFrame(frameLocator);
        String contentEditableVal = selenium.getAttribute(
            editorArea.getAttribute(new Attribute("contenteditable")));
        assertTrue("false".equals(contentEditableVal));

        selenium.selectFrame(FrameRelativeLocator.TOP);
    }

    @Test
    public void testRendered() {
        editorAttributes.set(rendered, Boolean.FALSE);
        assertFalse(selenium.isElementPresent(editor), "Editor is rendered even rendered attribute set to false.");
    }

    @Test
    public void testRequired() {
        editorAttributes.set(required, Boolean.TRUE);
        selenium.click(hSubmit);
        waitGui.until(textEquals.locator(validationMsgLoc).text(validationMsg1));

        fullPageRefresh();

        selenium.click(a4jSubmit);
        waitGui.until(textEquals.locator(validationMsgLoc).text(validationMsg1));
    }

    @Test
    public void testRequiredMessage() {
        editorAttributes.set(required, Boolean.TRUE);
        editorAttributes.set(requiredMessage, validationMsg2);
        selenium.click(hSubmit);
        waitGui.until(textEquals.locator(validationMsgLoc).text(validationMsg2));

        editorAttributes.set(requiredMessage, validationMsg1);
        selenium.click(a4jSubmit);
        waitGui.until(textEquals.locator(validationMsgLoc).text(validationMsg1));
    }

    @Test
    public void testSkin() {
        verifySkin("kama");
        verifySkin("office2003");
        verifySkin("richfaces");
        verifySkin("v2");
    }

    @Test
    public void testStyle() {

        final AttributeLocator<?> styleAttr = editor.getAttribute(Attribute.STYLE);
        final String style = "background-color: yellow; font-size: 1.5em;";

        editorAttributes.set(EditorAttributes.style, style);

        waitModel.failWith("Attribute style should contain \"" + style + "\"")
            .until(new SeleniumCondition() {
                @Override
                public boolean isTrue() {
                    return selenium.getAttribute(styleAttr).contains(style);
                }
            });

    }

    @Test
    public void testStyleClass() {

        final String styleClassVal = "metamer-ftest-class";
        editorAttributes.set(styleClass, styleClassVal);

        JQueryLocator elementWhichHasntThatClass = jq(editor.getRawLocator() + ":not(.{0})").format(styleClassVal);
        waitModel.until(elementPresent.locator(editor));
        assertTrue(selenium.isElementPresent(editor));
        assertFalse(selenium.isElementPresent(elementWhichHasntThatClass));
    }

    @Test
    public void testTitle() {

        String titleVal = "RichFaces 4";
        AttributeLocator<?> attribute = editor.getAttribute(new Attribute("title"));

        // title = null
        assertFalse(titleVal.equals(selenium.getAttribute(attribute)), "Attribute title should not be present.");

        // set title
        editorAttributes.set(title, titleVal);

        // wait until attribute present
        assertTrue(selenium.isAttributePresent(attribute), "Attribute title should be present.");
        // assert correct attribute value: title
        waitModel.until(attributeEquals.locator(attribute).text(titleVal));
    }

    @Test
    public void testToolbar() {

        editorAttributes.set(toolbar, "basic");

        /* for basic configuration of editor's toolbar there should be only one toolgroup
             (toolgroup is group of buttons, while span.toolbar is his container)
           Since there are not id's for toolbar based on toolbar name, it is not possible
           check exact toolbar present (all id's changes - for buttons as well as for toolbars)
        */
        assertEquals(selenium.getCount(editorToolbarGroup), 1);

        editorAttributes.set(toolbar, "full");
        assertEquals(selenium.getCount(editorToolbarGroup), 11);

        // since config facet has been introduced...
        editorAttributes.set(toolbar, "custom");
        assertEquals(selenium.getCount(editorToolbarGroup), 9);
    }

    @Test
    public void testWidth() {

        String STYLE_WIDTH = "345px";
        editorAttributes.set(width, STYLE_WIDTH);

        String styleAttr = selenium.getStyle(editor, CssProperty.WIDTH);
        assertTrue(styleAttr != null && styleAttr.contains(STYLE_WIDTH), "Style attribute should contain width value");
    }

}
