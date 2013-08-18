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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import com.norman0406.slimgress.API.Game.GameState;
import com.norman0406.slimgress.API.Game.Inventory;
import com.norman0406.slimgress.API.Item.ItemBase;
import com.norman0406.slimgress.API.Item.ItemBase.Rarity;
import com.norman0406.slimgress.API.Item.ItemFlipCard;
import com.norman0406.slimgress.API.Item.ItemMod;
import com.norman0406.slimgress.API.Item.ItemPortalKey;
import com.norman0406.slimgress.API.Item.ItemPowerCube;
import com.norman0406.slimgress.API.Item.ItemResonator;
import com.norman0406.slimgress.API.Item.ItemWeaponUltraStrike;
import com.norman0406.slimgress.API.Item.ItemBase.ItemType;
import com.norman0406.slimgress.API.Item.ItemFlipCard.FlipCardType;
import com.norman0406.slimgress.API.Item.ItemMedia;
import com.norman0406.slimgress.API.Item.ItemWeaponXMP;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class FragmentInventory extends Fragment implements OnChildClickListener
{
    private IngressApplication mApp = IngressApplication.getInstance();
    private GameState mGame = mApp.getGame();

    ArrayList<String> mGroupNames;
    ArrayList<Object> mGroups;
    ArrayList<String> mGroupMedia;
    ArrayList<String> mGroupMods;
    ArrayList<String> mGroupPortalKeys;
    ArrayList<String> mGroupPowerCubes;
    ArrayList<String> mGroupResonators;
    ArrayList<String> mGroupWeapons;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_inventory,
                container, false);

        final ExpandableListView list = (ExpandableListView)rootView.findViewById(R.id.listView);
        final ProgressBar progress = (ProgressBar)rootView.findViewById(R.id.progressBar1);

        list.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);

        // create group names
        mGroupNames = new ArrayList<String>();
        mGroups = new ArrayList<Object>();

        mGroupMedia = new ArrayList<String>();
        mGroupMods = new ArrayList<String>();
        mGroupPortalKeys = new ArrayList<String>();
        mGroupPowerCubes = new ArrayList<String>();
        mGroupResonators = new ArrayList<String>();
        mGroupWeapons = new ArrayList<String>();

        final FragmentInventory thisObject = this;

        final Handler handler = new Handler();

        mGame.intGetInventory(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                fillInventory(new Runnable() {
                    @Override
                    public void run()
                    {
                        handler.post(new Runnable() {
                            @Override
                            public void run()
                            {
                                InventoryList inventoryList = new InventoryList(mGroupNames, mGroups);
                                inventoryList.setInflater(inflater, thisObject.getActivity());
                                list.setAdapter(inventoryList);
                                list.setOnChildClickListener(thisObject);

                                list.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
                return true;
            }
        }));

        return rootView;
    }

    private void fillInventory(final Runnable callback)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                fillMedia();
                fillMods();
                fillResonators();
                fillPortalKeys();
                fillWeapons();
                fillPowerCubes();

                callback.run();
            }
        }).start();
    }

    void fillMedia()
    {
        Inventory inv = mGame.getInventory();

        int count = 0;
        for (int level = 1; level <= 8; level++) {
            List<ItemBase> items = inv.getItems(ItemType.Media, level);
            count += items.size();

            LinkedList<ItemMedia> skipItems = new LinkedList<ItemMedia>();
            for (ItemBase item1 : items) {
                ItemMedia theItem1 = (ItemMedia)item1;

                // skip items that have already been checked
                if (skipItems.contains(theItem1))
                    continue;

                String descr = "L" + level + " " + theItem1.getMediaDescription();

                // check for multiple media items with the same description
                int itemCount = 1;
                for (ItemBase item2 : items) {
                    ItemMedia theItem2 = (ItemMedia)item2;

                    // don't check the doubles
                    if (theItem2 == theItem1)
                        continue;

                    if (theItem1.getMediaDescription().equals(theItem2.getMediaDescription())) {
                        itemCount++;
                        skipItems.add(theItem2);
                    }
                }

                if (!theItem1.getMediaHasBeenViewed())
                    descr += " [NEW]";

                if (itemCount > 1)
                    descr += " (" + itemCount + ")";
                mGroupMedia.add(descr);
            }
        }
        mGroupNames.add("Media (" + count + ")");
        mGroups.add(mGroupMedia);
    }

    void fillMods()
    {
        Inventory inv = mGame.getInventory();

        ItemType[] types = { ItemType.ModForceAmp,
                ItemType.ModHeatsink,
                ItemType.ModLinkAmp,
                ItemType.ModMultihack,
                ItemType.ModShield,
                ItemType.ModTurret
        };

        Rarity[] rarities = { Rarity.None,
                Rarity.None,
                Rarity.LessCommon,
                Rarity.Common,
                Rarity.VeryCommon,
                Rarity.Rare,
                Rarity.VeryRare,
                Rarity.ExtraRare
        };

        int count = 0;

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < rarities.length; j++) {
                List<ItemBase> items = inv.getItems(types[i], rarities[j]);
                count += items.size();

                if (items.size() > 0) {
                    ItemMod theFirstItem = (ItemMod)(items.get(0));
                    String descr = theFirstItem.getModDisplayName();

                    switch (theFirstItem.getItemRarity()) {
                    case None:
                        break;
                    case LessCommon:
                        descr += " - Less Common";
                        break;
                    case Common:
                        descr += " - Common";
                        break;
                    case VeryCommon:
                        descr += " - Very Common";
                        break;
                    case Rare:
                        descr += " - Rare";
                        break;
                    case VeryRare:
                        descr += " - VeryRare";
                        break;
                    case ExtraRare:
                        descr += " - Extra Rare";
                        break;
                    }

                    if (items.size() > 0) {
                        if (items.size() > 1)
                            descr += " (" + items.size() + ")";
                        mGroupMods.add(descr);
                    }
                }
            }
        }

        mGroupNames.add("Mods (" + count + ")");
        mGroups.add(mGroupMods);
    }

    void fillResonators()
    {
        Inventory inv = mGame.getInventory();

        int count = 0;
        for (int level = 1; level <= 8; level++) {
            List<ItemBase> items = inv.getItems(ItemType.Resonator, level);
            count += items.size();

            String descr = "L" + level + " Resonator";
            if (items.size() > 0) {
                if (items.size() > 1)
                    descr += " (" + items.size() + ")";
                mGroupResonators.add(descr);
            }
        }
        mGroupNames.add("Resonators (" + count + ")");
        mGroups.add(mGroupResonators);

    }

    void fillWeapons()
    {
        Inventory inv = mGame.getInventory();

        // get xmp weapon items
        int count = 0;
        for (int level = 1; level <= 8; level++) {
            List<ItemBase> items = inv.getItems(ItemType.WeaponXMP, level);
            count += items.size();

            String descr = "L" + level + " XMP";
            if (items.size() > 0) {
                if (items.size() > 1)
                    descr += " (" + items.size() + ")";
                mGroupWeapons.add(descr);
            }
        }

        // get ultrastrike weapon items
        for (int level = 1; level <= 8; level++) {
            List<ItemBase> items = inv.getItems(ItemType.WeaponUltraStrike, level);
            count += items.size();

            String descr = "L" + level + " UltraStrike";
            if (items.size() > 0) {
                if (items.size() > 1)
                    descr += " (" + items.size() + ")";
                mGroupWeapons.add(descr);
            }
        }

        // get flipcard items
        List<ItemBase> items = inv.getItems(ItemType.FlipCard);
        count += items.size();

        int adaCount = 0, jarvisCount = 0;
        for (ItemBase item : items) {
            ItemFlipCard theItem = (ItemFlipCard)item;
            if (theItem.getFlipCardType() == FlipCardType.Ada)
                adaCount++;
            else if (theItem.getFlipCardType() == FlipCardType.Jarvis)
                jarvisCount++;
        }

        String descr = "ADA Refactor";
        if (adaCount > 0) {
            if (adaCount > 1)
                descr += " (" + adaCount + ")";
            mGroupWeapons.add(descr);
        }

        descr = "Jarvis Virus";
        if (jarvisCount > 0) {
            if (jarvisCount > 1)
                descr += " (" + adaCount + ")";
            mGroupWeapons.add(descr);
        }

        mGroupNames.add("Weapons (" + count + ")");
        mGroups.add(mGroupWeapons);
    }

    void fillPowerCubes()
    {
        Inventory inv = mGame.getInventory();

        int count = 0;
        for (int level = 1; level <= 8; level++) {
            List<ItemBase> items = inv.getItems(ItemType.PowerCube, level);
            count += items.size();

            String descr = "L" + level + " PowerCube";
            if (items.size() > 0) {
                if (items.size() > 1)
                    descr += " (" + items.size() + ")";
                mGroupPowerCubes.add(descr);
            }
        }
        mGroupNames.add("PowerCubes (" + count + ")");
        mGroups.add(mGroupPowerCubes);
    }

    void fillPortalKeys()
    {
        Inventory inv = mGame.getInventory();

        int count = 0;
        List<ItemBase> items = inv.getItems(ItemType.PortalKey);
        count += items.size();

        LinkedList<ItemPortalKey> skipItems = new LinkedList<ItemPortalKey>();
        for (ItemBase item1 : items) {
            ItemPortalKey theItem1 = (ItemPortalKey)item1;

            // skip items that have already been checked
            if (skipItems.contains(theItem1))
                continue;

            String descr = theItem1.getPortalTitle();

            // check for multiple portal keys with the same portal guid
            int itemCount = 1;
            for (ItemBase item2 : items) {
                ItemPortalKey theItem2 = (ItemPortalKey)item2;

                // don't check the doubles
                if (theItem2 == theItem1)
                    continue;

                if (theItem1.getPortalGuid().equals(theItem2.getPortalGuid())) {
                    itemCount++;
                    skipItems.add(theItem2);
                }
            }

            if (itemCount > 1)
                descr += " (" + itemCount + ")";
            mGroupPortalKeys.add(descr);
        }
        mGroupNames.add("PortalKeys (" + count + ")");
        mGroups.add(mGroupPortalKeys);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
    {
        Toast.makeText(getActivity(), "Clicked On Child", Toast.LENGTH_SHORT).show();
        return true;
    }
}
