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
    
    public void clear()
    {
        mItems.clear();
    }
	
	public void processGameBasket(GameBasket basket)
	{
	    // add new inventory items
		List<Item> newInv = basket.getInventory();
		if (newInv != null)
			mItems.addAll(basket.getInventory());
	}
	
	public final List<Item> getItems()
	{
		return mItems;
	}
}
