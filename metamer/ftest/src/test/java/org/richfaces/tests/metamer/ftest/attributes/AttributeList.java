package org.richfaces.tests.metamer.ftest.attributes;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.AbstractMetamerTest.pjq;

import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jRepeat.RepeatAttributes;
import org.richfaces.tests.metamer.ftest.a4jStatus.StatusAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets;
import org.richfaces.tests.metamer.ftest.richAutocomplete.AutocompleteAttributes;
import org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richComponentControl.ComponentControlAttributes;
import org.richfaces.tests.metamer.ftest.richContextMenu.ContextMenuAttributes;
import org.richfaces.tests.metamer.ftest.richDataScroller.DataScrollerAttributes;
import org.richfaces.tests.metamer.ftest.richDragIndicator.DragIndicatorAttributes;
import org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes;
import org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes;
import org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes;
import org.richfaces.tests.metamer.ftest.richGraphValidator.GraphValidatorAttributes;
import org.richfaces.tests.metamer.ftest.richJQuery.RichJQueryAttributes;
import org.richfaces.tests.metamer.ftest.richList.ListAttributes;
import org.richfaces.tests.metamer.ftest.richMenuItem.MenuItemAttributes;
import org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes;
import org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes;
import org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes;
import org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes;
import org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes;
import org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes;
import org.richfaces.tests.metamer.ftest.richTree.TreeAttributes;

public class AttributeList {
    public static Attributes<BasicAttributes> basicAttributes = new Attributes<BasicAttributes>();

    public static Attributes<TooltipAttributes> tooltipAttributes = new Attributes<TooltipAttributes>();
    public static Attributes<AttachQueueAttributes> attachQueueAttrs1 = new Attributes<AttachQueueAttributes>(
        pjq("table.attributes[id$=attributes1]"));
    public static Attributes<AttachQueueAttributes> attachQueueAttrs2 = new Attributes<AttachQueueAttributes>(
        pjq("table.attributes[id$=attributes2]"));
    public static Attributes<QueueAttributes> queueAttributes = new Attributes<QueueAttributes>(
        pjq("table.attributes[id$=queueAttributes]"));
    public static Attributes<PollAttributes> pollAttributes = new Attributes<PollAttributes>();
    public static Attributes<RepeatAttributes> repeatAttributes = new Attributes<RepeatAttributes>();
    public static Attributes<StatusAttributes> statusAttributes = new Attributes<StatusAttributes>();
    public static Attributes<DataTableAttributes> dataTableAttributes = new Attributes<DataTableAttributes>();
    public static Attributes<DataTableFacets> dataTableFacets = new Attributes<DataTableFacets>(RequestType.XHR); // for facets
                                                                                                                  // use XHR by
                                                                                                                  // default
    public static Attributes<AutocompleteAttributes> autocompleteAttributes = new Attributes<AutocompleteAttributes>();
    public static Attributes<CollapsibleSubTableTogglerAttributes> collapsibleSubTableTogglerAttributes = new Attributes<CollapsibleSubTableTogglerAttributes>();
    public static Attributes<ColumnAttributes> columnAttributes = new Attributes<ColumnAttributes>();
    public static Attributes<ComponentControlAttributes> componentControllAttributes = new Attributes<ComponentControlAttributes>();
    public static Attributes<DataScrollerAttributes> dataScrollerAttributes = new Attributes<DataScrollerAttributes>();
    public static Attributes<DragIndicatorAttributes> dragIndicatorAttributes = new Attributes<DragIndicatorAttributes>();
    public static Attributes<DragSourceAttributes> dragSourceAttributes = new Attributes<DragSourceAttributes>();
    public static Attributes<DropTargetAttributes> dropTargetAttributes = new Attributes<DropTargetAttributes>();
    public static Attributes<EditorAttributes> editorAttributes = new Attributes<EditorAttributes>();
    public static Attributes<GraphValidatorAttributes> graphValidatorAttributes = new Attributes<GraphValidatorAttributes>();
    public static Attributes<RichJQueryAttributes> jQueryAttributes = new Attributes<RichJQueryAttributes>();
    public static Attributes<ListAttributes> listAttributes = new Attributes<ListAttributes>();
    public static Attributes<MessageAttributes> messageAttributes = new Attributes<MessageAttributes>();
    public static Attributes<PanelMenuAttributes> panelMenuAttributes = new Attributes<PanelMenuAttributes>();
    public static Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = new Attributes<PanelMenuGroupAttributes>();
    public static Attributes<PanelMenuItemAttributes> panelMenuItemAttributes = new Attributes<PanelMenuItemAttributes>();
    public static Attributes<PickListAttributes> pickListAttributes = new Attributes<PickListAttributes>();
    public static Attributes<TreeAttributes> treeAttributes = new Attributes<TreeAttributes>(jq("span[id*=attributes]"));
    public static Attributes<MenuItemAttributes> menuItemAttributes = new Attributes<MenuItemAttributes>();
    public static Attributes<ContextMenuAttributes> contextMenuAttributes = new Attributes<ContextMenuAttributes>();
    public static Attributes<CalendarAttributes> calendarAttributes = new Attributes<CalendarAttributes>();

}
