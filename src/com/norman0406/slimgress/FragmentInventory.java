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

import com.norman0406.slimgress.API.Game.GameState;
import com.norman0406.slimgress.API.Game.Inventory;
import com.norman0406.slimgress.API.Item.ItemBase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class FragmentInventory extends Fragment implements OnChildClickListener
{
    private IngressApplication mApp = IngressApplication.getInstance();
    private GameState mGame = mApp.getGame();

    List<ItemBase> mItemsMedia;
    List<ItemBase> mItemsMods;
    List<ItemBase> mItemsPortalKeys;
    List<ItemBase> mItemsPowerCubes;
    List<ItemBase> mItemsResonators;
    List<ItemBase> mItemsWeapons;

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

        mItemsMedia = new ArrayList<ItemBase>();
        mItemsMods = new ArrayList<ItemBase>();
        mItemsPortalKeys = new ArrayList<ItemBase>();
        mItemsPowerCubes = new ArrayList<ItemBase>();
        mItemsResonators = new ArrayList<ItemBase>();
        mItemsWeapons = new ArrayList<ItemBase>();

        fillInventory();

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

    private void fillInventory()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Inventory inv = mGame.getInventory();
                List<ItemBase> items = inv.getItems();

                for (ItemBase item : items) {
                    if (item.getItemType() == ItemBase.ItemType.Media)
                        mItemsMedia.add(item);
                    else if (item.getItemType() == ItemBase.ItemType.Mod)
                        mItemsMods.add(item);
                    else if (item.getItemType() == ItemBase.ItemType.PortalKey)
                        mItemsPortalKeys.add(item);
                    else if (item.getItemType() == ItemBase.ItemType.PowerCube)
                        mItemsPowerCubes.add(item);
                    else if (item.getItemType() == ItemBase.ItemType.Resonator)
                        mItemsResonators.add(item);
                    else if (item.getItemType() == ItemBase.ItemType.Virus)
                        mItemsWeapons.add(item);
                    else if (item.getItemType() == ItemBase.ItemType.XMP)
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
