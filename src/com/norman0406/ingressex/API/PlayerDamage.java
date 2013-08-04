package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerDamage
{
    private int mAmount;
    private String mAttackerGuid;
    private String mWeaponSerializationTag;

    public PlayerDamage(JSONObject json) throws NumberFormatException, JSONException
    {
        mAmount = Integer.parseInt(json.getString("damageAmount"));
        mAttackerGuid = json.getString("attackerGuid");
        mWeaponSerializationTag = json.getString("weaponSerializationTag");
    }
    
    public int getAmount()
    {
        return mAmount;
    }
    
    public String getAttackerGuid()
    {
        return mAttackerGuid;
    }
    
    public String getWeaponSerializationTag()
    {
        return mWeaponSerializationTag;
    }
}
