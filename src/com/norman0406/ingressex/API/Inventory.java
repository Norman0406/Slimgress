package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class Inventory
{
	private List<Item> items;
	
	public Inventory()
	{
		items = new LinkedList<Item>();
	}
	
	public void processGameBasket(JSONHandlerGameBasket basket)
	{
		List<Item> newInv = basket.getInventory();
		if (newInv != null) {
			items.clear();
			items.addAll(basket.getInventory());
		}
	}	
}
