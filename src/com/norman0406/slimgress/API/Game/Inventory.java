/***********************************************************************
*
* Slimgress: Ingress API for Android
* Copyright (C) 2013 Norman Link <norman.link@gmx.net>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
***********************************************************************/

package com.norman0406.slimgress.API.Game;

import java.util.LinkedList;
import java.util.List;

import com.norman0406.slimgress.API.Interface.GameBasket;
import com.norman0406.slimgress.API.Item.ItemBase;

public class Inventory
{
	private List<ItemBase> mItems;
	
	public Inventory()
	{
		mItems = new LinkedList<ItemBase>();
	}
    
    public void clear()
    {
        mItems.clear();
    }
	
	public void processGameBasket(GameBasket basket)
	{
	    // add new inventory items
		List<ItemBase> newInv = basket.getInventory();
		if (newInv != null)
			mItems.addAll(basket.getInventory());
	}
	
	public final List<ItemBase> getItems()
	{
		return mItems;
	}
}
