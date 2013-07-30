package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class Inventory
{
	private List<Item> mItems;
	
	public Inventory()
	{
		mItems = new LinkedList<Item>();
	}
	
	public void processGameBasket(GameBasket basket)
	{
		List<Item> newInv = basket.getInventory();
		if (newInv != null) {
			mItems.clear();
			mItems.addAll(basket.getInventory());
		}
	}
	
	public final List<Item> getItems()
	{
		return mItems;
	}
}
