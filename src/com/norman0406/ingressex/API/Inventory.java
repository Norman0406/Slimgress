package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class Inventory {
	private List<Item> items;
	
	public Inventory()
	{
		items = new LinkedList<Item>();
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public Item getItem(String itemGuid) {
		for (Item it : items) {
			if (it.itemGuid == itemGuid)
				return it;
		}
		return null;
	}
	
	public boolean removeItem(String itemGuid) {
		Item itemToRemove = getItem(itemGuid);
		if (itemToRemove != null)
			return items.remove(itemToRemove);
		return false;
	}
}
