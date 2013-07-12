package com.norman0406.ingressex.API;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
			if (it.getItemGuid() == itemGuid)
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
	
	public void update()
	{
		/*List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("lastQueryTimestamp", agent.getLastSyncTimestamp()));
		
		request("playerUndecorated/getInventory", params, new ProcessGameBasket(this));*/
	}
}
