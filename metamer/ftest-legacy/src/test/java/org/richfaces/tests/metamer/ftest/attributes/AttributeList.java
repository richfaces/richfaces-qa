package org.richfaces.tests.metamer.ftest.attributes;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.AbstractGrapheneTest.pjq;

import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.MetamerAttributes;
import org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;

public class AttributeList {

    public static Attributes<AttachQueueAttributes> attachQueueAttributes = new Attributes<AttachQueueAttributes>();
    public static Attributes<AttachQueueAttributes> attachQueueAttrs1 = new Attributes<AttachQueueAttributes>(
        pjq("table.attributes[id$=attributes1]"));
    public static Attributes<AttachQueueAttributes> attachQueueAttrs2 = new Attributes<AttachQueueAttributes>(
        pjq("table.attributes[id$=attributes2]"));
    public static Attributes<BasicAttributes> basicAttributes = new Attributes<BasicAttributes>();
    public static Attributes<DataTableAttributes> dataTableAttributes = new Attributes<DataTableAttributes>();
    public static Attributes<DataTableFacets> dataTableFacets = new Attributes<DataTableFacets>(RequestType.XHR); // for facets
    public static Attributes<MetamerAttributes> metamerAttributes = new Attributes<MetamerAttributes>(
        jq("form[id$=displayControlsForm]"));
    public static Attributes<QueueAttributes> queueAttributes = new Attributes<QueueAttributes>(
        pjq("table.attributes[id$=queueAttributes]"));
}
