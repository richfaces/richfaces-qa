package org.richfaces.tests.showcase.hotKey;

import org.richfaces.tests.showcase.pickList.AbstractPickListTest;
import org.testng.annotations.Test;

public class TestHotKey extends AbstractPickListTest {

	@Test
	public void testAddHotkey() {
		
		checkAddButton(sourceItemsSimple, addButton, targetItemsSimple, true);
	}
	
	@Test
	public void testRemoveHotKey() {
		
		super.testRemoveButton(true);
	}
	
	@Test
	public void testAddAllHotKey() {
		
		super.testAddAllButtonSimplePickList(true);
	}
	
	@Test
	public void testRemoveAllHotKey() {
		
		super.testRemoveAllButtonSimplePickList(true);
	}
	
	@Test
	public void testAddAllButtonSimplePickList() {

		super.testAddAllButtonSimplePickList();
	}

	@Test
	public void testRemoveAllButtonSimplePickList() {

		super.testRemoveAllButtonSimplePickList();
	}

	@Test
	public void testAddButtonSimplePickList() {

		super.testAddButtonSimplePickList();
	}

	@Test
	public void testFirstButtonOrdering() {

		super.testFirstButtonOrdering();
	}

	@Test
	public void testUpButtonOrdering() {

		super.testUpButtonOrdering();
	}

	@Test
	public void testDownButtonOrdering() {

		super.testDownButtonOrdering();
	}

	@Test
	public void testLastButtonOrdering() {

		super.testLastButtonOrdering();
	}

}
