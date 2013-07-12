package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class Inventory {
	private List<Item> items;
	
	public Inventory() {
		items = new LinkedList<Item>();
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public Item getItem(String guid) {
		for (Item it : items) {
			if (it.getEntityGuid() == guid)
				return it;
		}
		return null;
	}
	
	public boolean removeItem(String guid) {
		Item item = getItem(guid);
		if (item != null)
			return items.remove(item);
		return false;
	}
	
	public void update() {
		/*List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("lastQueryTimestamp", agent.getLastSyncTimestamp()));
		
		request("playerUndecorated/getInventory", params, new ProcessGameBasket(this));*/
	}
}
