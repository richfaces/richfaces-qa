/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2012-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.webdriver;

import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.MetamerAttributes;
import org.richfaces.tests.metamer.ftest.a4jActionListener.ActionListenerAttributes;
import org.richfaces.tests.metamer.ftest.a4jAjax.AjaxAttributes;
import org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jCommandButton.CommandButtonAttributes;
import org.richfaces.tests.metamer.ftest.a4jCommandLink.CommandLinkAttributes;
import org.richfaces.tests.metamer.ftest.a4jJSFunction.JSFunctionAttributes;
import org.richfaces.tests.metamer.ftest.a4jLog.LogAttributes;
import org.richfaces.tests.metamer.ftest.a4jMediaOutput.MediaOutputAttributes;
import org.richfaces.tests.metamer.ftest.a4jOutputPanel.OutputPanelAttributes;
import org.richfaces.tests.metamer.ftest.a4jParam.ParamAttributes;
import org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes;
import org.richfaces.tests.metamer.ftest.a4jPush.PushAttributes;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jRegion.RegionAttributes;
import org.richfaces.tests.metamer.ftest.a4jRepeat.RepeatAttributes;
import org.richfaces.tests.metamer.ftest.a4jStatus.StatusAttributes;
import org.richfaces.tests.metamer.ftest.richAccordion.AccordionAttributes;
import org.richfaces.tests.metamer.ftest.richAccordionItem.AccordionItemAttributes;
import org.richfaces.tests.metamer.ftest.richAutocomplete.AutocompleteAttributes;
import org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes;
import org.richfaces.tests.metamer.ftest.richCollapsiblePanel.CollapsiblePanelAttributes;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richColumnGroup.ColumnGroupAttributes;
import org.richfaces.tests.metamer.ftest.richComponentControl.ComponentControlAttributes;
import org.richfaces.tests.metamer.ftest.richContextMenu.ContextMenuAttributes;
import org.richfaces.tests.metamer.ftest.richDataGrid.DataGridAttributes;
import org.richfaces.tests.metamer.ftest.richDataScroller.DataScrollerAttributes;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;
import org.richfaces.tests.metamer.ftest.richDragIndicator.DragIndicatorAttributes;
import org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes;
import org.richfaces.tests.metamer.ftest.richDropDownMenu.DropDownMenuAttributes;
import org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes;
import org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.ExtendedDataTableAttributes;
import org.richfaces.tests.metamer.ftest.richFileUpload.FileUploadAttributes;
import org.richfaces.tests.metamer.ftest.richFocus.FocusAttributes;
import org.richfaces.tests.metamer.ftest.richGraphValidator.GraphValidatorAttributes;
import org.richfaces.tests.metamer.ftest.richHashParam.HashParamAttributes;
import org.richfaces.tests.metamer.ftest.richInplaceInput.InplaceInputAttributes;
import org.richfaces.tests.metamer.ftest.richInplaceSelect.InplaceSelectAttributes;
import org.richfaces.tests.metamer.ftest.richInputNumberSlider.InputNumberSliderAttributes;
import org.richfaces.tests.metamer.ftest.richInputNumberSpinner.InputNumberSpinnerAttributes;
import org.richfaces.tests.metamer.ftest.richJQuery.JQueryAttributes;
import org.richfaces.tests.metamer.ftest.richList.ListAttributes;
import org.richfaces.tests.metamer.ftest.richMenuGroup.MenuGroupAttributes;
import org.richfaces.tests.metamer.ftest.richMenuItem.MenuItemAttributes;
import org.richfaces.tests.metamer.ftest.richMenuSeparator.MenuSeparatorAttributes;
import org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes;
import org.richfaces.tests.metamer.ftest.richMessages.MessagesAttributes;
import org.richfaces.tests.metamer.ftest.richPanel.PanelAttributes;
import org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes;
import org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes;
import org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes;
import org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes;
import org.richfaces.tests.metamer.ftest.richPlaceholder.PlaceholderAttributes;
import org.richfaces.tests.metamer.ftest.richPopupPanel.PopupPanelAttributes;
import org.richfaces.tests.metamer.ftest.richProgressBar.ProgressBarAttributes;
import org.richfaces.tests.metamer.ftest.richSelect.SelectAttributes;
import org.richfaces.tests.metamer.ftest.richTab.TabAttributes;
import org.richfaces.tests.metamer.ftest.richTabPanel.TabPanelAttributes;
import org.richfaces.tests.metamer.ftest.richToggleControl.ToggleControlAttributes;
import org.richfaces.tests.metamer.ftest.richTogglePanel.TogglePanelAttributes;
import org.richfaces.tests.metamer.ftest.richTogglePanelItem.TogglePanelItemAttributes;
import org.richfaces.tests.metamer.ftest.richToolbar.ToolbarAttributes;
import org.richfaces.tests.metamer.ftest.richToolbarGroup.ToolbarGroupAttributes;
import org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes;
import org.richfaces.tests.metamer.ftest.richTree.TreeAttributes;
import org.richfaces.tests.metamer.ftest.richTreeModelAdaptor.TreeModelAdapterAttributes;
import org.richfaces.tests.metamer.ftest.richValidator.ValidatorAttributes;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AttributeList {

    public static Attributes<AccordionAttributes> accordionAttributes = new Attributes<AccordionAttributes>();
    public static Attributes<ActionListenerAttributes> actionListenerAttributes = new Attributes<ActionListenerAttributes>();
    public static Attributes<AjaxAttributes> ajaxAttributes = new Attributes<AjaxAttributes>();
    public static Attributes<AccordionItemAttributes> accordionItemAttributes = new Attributes<AccordionItemAttributes>();
    public static Attributes<AttachQueueAttributes> attachQueueAttrs1 = new Attributes<AttachQueueAttributes>("attributes1");
    public static Attributes<AttachQueueAttributes> attachQueueAttrs2 = new Attributes<AttachQueueAttributes>("attributes2");
    public static Attributes<AutocompleteAttributes> autocompleteAttributes = new Attributes<AutocompleteAttributes>();
    public static Attributes<BasicAttributes> basicAttributes = new Attributes<BasicAttributes>();
    public static Attributes<CalendarAttributes> calendarAttributes = new Attributes<CalendarAttributes>();
    public static Attributes<CollapsiblePanelAttributes> collapsiblePanelAttributes = new Attributes<CollapsiblePanelAttributes>();
    public static Attributes<CollapsibleSubTableTogglerAttributes> collapsibleSubTableTogglerAttributes = new Attributes<CollapsibleSubTableTogglerAttributes>();
    public static Attributes<ColumnAttributes> columnAttributes = new Attributes<ColumnAttributes>();
    public static Attributes<ColumnGroupAttributes> columnGroupAttributes = new Attributes<ColumnGroupAttributes>();
    public static Attributes<ContextMenuAttributes> contextMenuAttributes = new Attributes<ContextMenuAttributes>();
    public static Attributes<CommandButtonAttributes> commandButtonAttributes = new Attributes<CommandButtonAttributes>();
    public static Attributes<CommandLinkAttributes> commandLinkAttributes = new Attributes<CommandLinkAttributes>();
    public static Attributes<ComponentControlAttributes> componentControllAttributes = new Attributes<ComponentControlAttributes>();
    public static Attributes<DataGridAttributes> dataGridAttributes = new Attributes<DataGridAttributes>();
    public static Attributes<DataTableAttributes> dataTableAttributes = new Attributes<DataTableAttributes>();
    public static Attributes<DataScrollerAttributes> dataScrollerAttributes = new Attributes<DataScrollerAttributes>();
    public static Attributes<DragIndicatorAttributes> dragIndicatorAttributes = new Attributes<DragIndicatorAttributes>();
    public static Attributes<DragSourceAttributes> dragSourceAttributes = new Attributes<DragSourceAttributes>();
    public static Attributes<DropDownMenuAttributes> dropDownMenuAttributes = new Attributes<DropDownMenuAttributes>();
    public static Attributes<DropTargetAttributes> dropTargetAttributes = new Attributes<DropTargetAttributes>();
    public static Attributes<EditorAttributes> editorAttributes = new Attributes<EditorAttributes>();
    public static Attributes<ExtendedDataTableAttributes> extendedDataTableAttributes = new Attributes<ExtendedDataTableAttributes>();
    public static Attributes<FileUploadAttributes> fileUploadAttributes = new Attributes<FileUploadAttributes>();
    public static Attributes<FocusAttributes> focusAttributes = new Attributes<FocusAttributes>();
    public static Attributes<GraphValidatorAttributes> graphValidatorAttributes = new Attributes<GraphValidatorAttributes>();
    public static Attributes<HashParamAttributes> hashParamAttributes = new Attributes<HashParamAttributes>();
    public static Attributes<InplaceInputAttributes> inplaceInputAttributes = new Attributes<InplaceInputAttributes>();
    public static Attributes<InplaceSelectAttributes> inplaceSelectAttributes = new Attributes<InplaceSelectAttributes>();
    public static Attributes<InputNumberSliderAttributes> inputNumberSliderAttributes = new Attributes<InputNumberSliderAttributes>();
    public static Attributes<InputNumberSpinnerAttributes> inputNumberSpinnerAttributes = new Attributes<InputNumberSpinnerAttributes>();
    public static Attributes<JSFunctionAttributes> jsFunctionAttributes = new Attributes<JSFunctionAttributes>();
    public static Attributes<ListAttributes> listAttributes = new Attributes<ListAttributes>();
    public static Attributes<LogAttributes> logAttributes = new Attributes<LogAttributes>();
    public static Attributes<MediaOutputAttributes> mediaOutputAttributes = new Attributes<MediaOutputAttributes>();
    public static Attributes<MenuGroupAttributes> menuGroupAttributes = new Attributes<MenuGroupAttributes>();
    public static Attributes<MenuItemAttributes> menuItemAttributes = new Attributes<MenuItemAttributes>();
    public static Attributes<MenuSeparatorAttributes> menuSeparatorAttributes = new Attributes<MenuSeparatorAttributes>();
    public static Attributes<MessageAttributes> messageAttributes = new Attributes<MessageAttributes>();
    public static Attributes<MessagesAttributes> messagesAttributes = new Attributes<MessagesAttributes>();
    public static Attributes<MetamerAttributes> metamerAttributes = new Attributes<MetamerAttributes>();
    public static Attributes<OutputPanelAttributes> outputPanelAttributes = new Attributes<OutputPanelAttributes>();
    public static Attributes<PanelAttributes> panelAttributes = new Attributes<PanelAttributes>();
    public static Attributes<PanelMenuAttributes> panelMenuAttributes = new Attributes<PanelMenuAttributes>();
    public static Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = new Attributes<PanelMenuGroupAttributes>();
    public static Attributes<PanelMenuItemAttributes> panelMenuItemAttributes = new Attributes<PanelMenuItemAttributes>();
    public static Attributes<ParamAttributes> paramAttributes = new Attributes<ParamAttributes>();
    public static Attributes<PickListAttributes> pickListAttributes = new Attributes<PickListAttributes>();
    public static Attributes<PlaceholderAttributes> placeholderAttributes = new Attributes<PlaceholderAttributes>();
    public static Attributes<PollAttributes> pollAttributes = new Attributes<PollAttributes>();
    public static Attributes<PopupPanelAttributes> popupPanelAttributes = new Attributes<PopupPanelAttributes>();
    public static Attributes<ProgressBarAttributes> progressBarAttributes = new Attributes<ProgressBarAttributes>();
    public static Attributes<PushAttributes> pushAttributes = new Attributes<PushAttributes>();
    public static Attributes<QueueAttributes> queueAttributes = new Attributes<QueueAttributes>("queueAttributes");
    public static Attributes<RegionAttributes> regionAttributes = new Attributes<RegionAttributes>();
    public static Attributes<RepeatAttributes> repeatAttributes = new Attributes<RepeatAttributes>();
    public static Attributes<JQueryAttributes> jQueryAttributes = new Attributes<JQueryAttributes>();
    public static Attributes<SelectAttributes> selectAttributes = new Attributes<SelectAttributes>();
    public static Attributes<StatusAttributes> statusAttributes = new Attributes<StatusAttributes>();
    public static Attributes<TabAttributes> tabAttributes = new Attributes<TabAttributes>();
    public static Attributes<TabPanelAttributes> tabPanelAttributes = new Attributes<TabPanelAttributes>();
    public static Attributes<ToggleControlAttributes> toggleControlAttributes = new Attributes<ToggleControlAttributes>();
    public static Attributes<TogglePanelAttributes> togglePanelAttributes = new Attributes<TogglePanelAttributes>();
    public static Attributes<TogglePanelItemAttributes> togglePanelItemAttributes = new Attributes<TogglePanelItemAttributes>();
    public static Attributes<ToolbarAttributes> toolbarAttributes = new Attributes<ToolbarAttributes>();
    public static Attributes<ToolbarGroupAttributes> toolbarGroupAttributes = new Attributes<ToolbarGroupAttributes>();
    public static Attributes<TooltipAttributes> tooltipAttributes = new Attributes<TooltipAttributes>();
    public static Attributes<TreeAttributes> treeAttributes = new Attributes<TreeAttributes>("attributes");
    public static Attributes<TreeModelAdapterAttributes> treeModelAdapterAttributes = new Attributes<TreeModelAdapterAttributes>();
    public static Attributes<ValidatorAttributes> validatorAttributes = new Attributes<ValidatorAttributes>();
}
