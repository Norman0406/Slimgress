package com.norman0406.ingressex;

import java.util.ArrayList;
import java.util.List;

import com.norman0406.ingressex.API.Interface;
import com.norman0406.ingressex.API.Inventory;
import com.norman0406.ingressex.API.Item;
import com.norman0406.ingressex.API.ItemMedia;
import com.norman0406.ingressex.API.ItemMod;
import com.norman0406.ingressex.API.ItemPortalKey;
import com.norman0406.ingressex.API.ItemPowerCube;
import com.norman0406.ingressex.API.ItemResonator;
import com.norman0406.ingressex.API.ItemVirus;
import com.norman0406.ingressex.API.ItemXMP;

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

public class FragmentInventory extends Fragment implements OnChildClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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

		/*ArrayList<String> childMedia = new ArrayList<String>();
		ArrayList<String> childMods = new ArrayList<String>();
		ArrayList<String> childPortalKeys = new ArrayList<String>();
		ArrayList<String> childPowerCubes = new ArrayList<String>();
		ArrayList<String> childResonators = new ArrayList<String>();
		ArrayList<String> childWeapons = new ArrayList<String>();	
		
		MainActivity mainAct = (MainActivity)this.getActivity();
		
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

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(getActivity(), "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}
}
