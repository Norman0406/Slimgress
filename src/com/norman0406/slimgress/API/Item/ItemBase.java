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

package com.norman0406.slimgress.API.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.norman0406.slimgress.API.Common.EntityBase;

import android.util.Log;

public abstract class ItemBase extends EntityBase
{
    public enum ItemType
    {
        Media,
        ModForceAmp,
        ModHeatsink,
        ModLinkAmp,
        ModMultihack,
        ModShield,
        ModTurret,
        PortalKey,
        PowerCube,
        Resonator,
        FlipCard,
        WeaponXMP,
        WeaponUltraStrike,
    }

    public enum Rarity
    {
        None,
        VeryCommon,
        Common,
        LessCommon,
        Rare,
        VeryRare,
        ExtraRare
    }

    private int mItemAccessLevel = 0;
    private Rarity mItemRarity = Rarity.None;
    private final ItemType mItemType;
    private String mItemPlayerId;
    private String mItemAcquisitionTimestamp;

    public static ItemBase createByJSON(JSONArray json) throws JSONException
    {
        if (json.length() != 3) {
            Log.e("ItemBase", "invalid array size");
            return null;
        }

        JSONObject item = json.getJSONObject(2);

        JSONObject itemResource = null;
        if (item.has("resource"))
            itemResource = item.getJSONObject("resource");
        else if (item.has("resourceWithLevels"))
            itemResource = item.getJSONObject("resourceWithLevels");
        else if (item.has("modResource"))
            itemResource = item.getJSONObject("modResource");

        // create item
        ItemBase newItem = null;

        String itemType = itemResource.getString("resourceType");
        if (itemType.equals(ItemPortalKey.getNameStatic()))
            newItem = new ItemPortalKey(json);
        else if (itemType.equals(ItemWeaponXMP.getNameStatic()))
            newItem = new ItemWeaponXMP(json);
        else if (itemType.equals(ItemWeaponUltraStrike.getNameStatic()))
            newItem = new ItemWeaponUltraStrike(json);
        else if (itemType.equals(ItemResonator.getNameStatic()))
            newItem = new ItemResonator(json);
        else if (itemType.equals(ItemModShield.getNameStatic()))
            newItem = new ItemModShield(json);
        else if (itemType.equals(ItemPowerCube.getNameStatic()))
            newItem = new ItemPowerCube(json);
        else if (itemType.equals(ItemMedia.getNameStatic()))
            newItem = new ItemMedia(json);
        else if (itemType.equals(ItemModForceAmp.getNameStatic()))
            newItem = new ItemModForceAmp(json);
        else if (itemType.equals(ItemModMultihack.getNameStatic()))
            newItem = new ItemModMultihack(json);
        else if (itemType.equals(ItemModLinkAmp.getNameStatic()))
            newItem = new ItemModLinkAmp(json);
        else if (itemType.equals(ItemModTurret.getNameStatic()))
            newItem = new ItemModTurret(json);
        else if (itemType.equals(ItemModHeatsink.getNameStatic()))
            newItem = new ItemModHeatsink(json);
        else if (itemType.equals(ItemFlipCard.getNameStatic()))
            newItem = new ItemFlipCard(json);
        else {
            // unknown resource type
            Log.w("Item", "unknown resource type: " + itemType);
        }

        return newItem;
    }

    protected ItemBase(ItemType type, JSONArray json) throws JSONException
    {
        super(json);
        mItemType = type;

        JSONObject item = json.getJSONObject(2);
        JSONObject itemResource = null;
        if (item.has("resource"))
            itemResource = item.getJSONObject("resource");
        else if (item.has("resourceWithLevels"))
            itemResource = item.getJSONObject("resourceWithLevels");
        else if (item.has("modResource"))
            itemResource = item.getJSONObject("modResource");
        else
            throw new JSONException("resource not found");

        if (itemResource.has("resourceRarity") || itemResource.has("rarity")) {
            String rarity = null;
            if (itemResource.has("resourceRarity"))
                rarity = itemResource.getString("resourceRarity");
            else if (itemResource.has("rarity"))
                rarity = itemResource.getString("rarity");
            else
                throw new RuntimeException("unknown rarity string");

            if (mItemRarity != null) {
                if (rarity.equals("VERY_COMMON"))
                    mItemRarity = ItemBase.Rarity.VeryCommon;
                else if (rarity.equals("COMMON"))
                    mItemRarity = ItemBase.Rarity.Common;
                else if (rarity.equals("LESS_COMMON"))
                    mItemRarity = ItemBase.Rarity.LessCommon;
                else if (rarity.equals("RARE"))
                    mItemRarity = ItemBase.Rarity.Rare;
                else if (rarity.equals("VERY_RARE"))
                    mItemRarity = ItemBase.Rarity.VeryRare;
                else if (rarity.equals("EXTREMELY_RARE"))
                    mItemRarity = ItemBase.Rarity.ExtraRare;
            }
        }

        if (itemResource.has("level")) {
            int level = itemResource.getInt("level");
            mItemAccessLevel = level;
        }

        JSONObject itemInInventory = item.getJSONObject("inInventory");
        mItemPlayerId = itemInInventory.getString("playerId");
        mItemAcquisitionTimestamp = itemInInventory.getString("acquisitionTimestampMs");
    }

    public static String getNameStatic() {
        // override!
        throw new RuntimeException("this method has to be overridden");
    }

    public abstract String getName();

    public int getItemAccessLevel()
    {
        return mItemAccessLevel;
    }

    public Rarity getItemRarity()
    {
        return mItemRarity;
    }

    public String getItemPlayerId()
    {
        return mItemPlayerId;
    }

    public String getItemAcquisitionTimestamp()
    {
        return mItemAcquisitionTimestamp;
    }

    public ItemType getItemType()
    {
        return mItemType;
    }
}
