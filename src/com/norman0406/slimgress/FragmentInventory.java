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

package com.norman0406.slimgress;

import java.util.ArrayList;
import java.util.List;

import com.norman0406.slimgress.API.Game;
import com.norman0406.slimgress.API.Interface;
import com.norman0406.slimgress.API.Inventory;
import com.norman0406.slimgress.API.Item;
import com.norman0406.slimgress.API.ItemMedia;
import com.norman0406.slimgress.API.ItemMod;
import com.norman0406.slimgress.API.ItemPortalKey;
import com.norman0406.slimgress.API.ItemPowerCube;
import com.norman0406.slimgress.API.ItemResonator;
import com.norman0406.slimgress.API.ItemVirus;
import com.norman0406.slimgress.API.ItemXMP;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentInventory extends Fragment implements OnChildClickListener
{
    private IngressApplication mApp = IngressApplication.getInstance();
    private Game mGame = mApp.getGame();
    
    List<Item> mItemsMedia;
    List<Item> mItemsMods;
    List<Item> mItemsPortalKeys;
    List<Item> mItemsPowerCubes;
    List<Item> mItemsResonators;
    List<Item> mItemsWeapons;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_inventory,
				container, false);
		
		ExpandableListView list = (ExpandableListView)rootView.findViewById(R.id.listView);

		ArrayList<String> groupItem = new ArrayList<String>();
		ArrayList<Object> childItem = new ArrayList<Object>();
		
		groupItem.add("Media");
		groupItem.add("Mods");
		groupItem.add("Portal-Keys");
		groupItem.add("PowerCubes");
		groupItem.add("Resonators");
		groupItem.add("Weapons");

	    mItemsMedia = new ArrayList<Item>();
	    mItemsMods = new ArrayList<Item>();
	    mItemsPortalKeys = new ArrayList<Item>();
	    mItemsPowerCubes = new ArrayList<Item>();
	    mItemsResonators = new ArrayList<Item>();
	    mItemsWeapons = new ArrayList<Item>();
	    
	    fillInventory();
		
		ArrayList<String> childMedia = new ArrayList<String>();
		ArrayList<String> childMods = new ArrayList<String>();
		ArrayList<String> childPortalKeys = new ArrayList<String>();
		ArrayList<String> childPowerCubes = new ArrayList<String>();
		ArrayList<String> childResonators = new ArrayList<String>();
		ArrayList<String> childWeapons = new ArrayList<String>();
				
		/*MainActivity mainAct = (MainActivity)this.getActivity();
		
		Interface ingInt = mainAct.ingressInterface;
		
		if (ingInt != null) {
			Inventory inv = mainAct.ingressInterface.getInventory();	
			
			List<Item> items = inv.getItems();

			for (Item it : items) {
				switch (it.getItemType()) {
				case Media: {
					ItemMedia item = (ItemMedia)it;
					childMedia.add(item.getMediaDescription());
					break;					
				}
				case Mod: {
					ItemMod item = (ItemMod)it;
					childMods.add(item.getModDisplayName());
					break;
				}
				case PortalKey: {
					ItemPortalKey item = (ItemPortalKey)it;
					childPortalKeys.add(item.getPortalTitle());
					break;
				}
				case PowerCube: {
					ItemPowerCube item = (ItemPowerCube)it;
					childPowerCubes.add("PowerCube");
					break;
				}
				case Resonator: {
					ItemResonator item = (ItemResonator)it;
					childResonators.add("Resonator");
					break;
				}
				case XMP: {
					ItemXMP item = (ItemXMP)it;
					childWeapons.add("XMP-Burster");
					break;
				}
				case Virus: {
					ItemVirus item = (ItemVirus)it;
					childWeapons.add("Virus");
					break;
				}
				}
			}

			childItem.add(childMedia);
			childItem.add(childMods);
			childItem.add(childPortalKeys);
			childItem.add(childPowerCubes);
			childItem.add(childResonators);
			childItem.add(childWeapons);			
		}*/
 
		InventoryList inventoryList = new InventoryList(groupItem, childItem);
		inventoryList.setInflater(inflater, this.getActivity());
		list.setAdapter(inventoryList);
		list.setOnChildClickListener(this);
		
		return rootView;
	}
	
	private void fillInventory()
	{
	    new Thread(new Runnable() {
            @Override
            public void run()
            {
                Inventory inv = mGame.getInventory();
                List<Item> items = inv.getItems();
                
                for (Item item : items) {
                    if (item.getItemType() == Item.ItemType.Media)
                        mItemsMedia.add(item);
                    else if (item.getItemType() == Item.ItemType.Mod)
                        mItemsMods.add(item);
                    else if (item.getItemType() == Item.ItemType.PortalKey)
                        mItemsPortalKeys.add(item);
                    else if (item.getItemType() == Item.ItemType.PowerCube)
                        mItemsPowerCubes.add(item);
                    else if (item.getItemType() == Item.ItemType.Resonator)
                        mItemsResonators.add(item);
                    else if (item.getItemType() == Item.ItemType.Virus)
                        mItemsWeapons.add(item);
                    else if (item.getItemType() == Item.ItemType.XMP)
                        mItemsWeapons.add(item);
                }                
            }
	    }).start();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		Toast.makeText(getActivity(), "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}
}
