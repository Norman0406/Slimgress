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

package com.norman0406.slimgress.API.Interface;

import org.json.JSONException;
import org.json.JSONObject;

public class APGain
{
    public enum Trigger
    {
        Unknown,
        DeployedResonator,
        CapturedPortal,
        CreatedLink,
        CreatedField,
        DestroyedResonator,
        DestroyedLink,
        DestroyedField,
        DeployedMod,
        FullyDevloyedPortal,
        HackingEnemyPortal,
        RedeemedAP,
        RechargeResonator,
        RemoteRechargeResonator,
        InvitedPlayerJoined
    }

    private int mAmount;
    private Trigger mTrigger;

    public APGain(JSONObject json) throws NumberFormatException, JSONException
    {
        mAmount = Integer.parseInt(json.getString("apGainAmount"));
        String trigger = json.getString("apTrigger");
        if (trigger.equals("DEPLOYED_RESONATOR"))
            mTrigger = Trigger.DeployedResonator;
        else if (trigger.equals("CAPTURED_PORTAL"))
            mTrigger = Trigger.CapturedPortal;
        else if (trigger.equals("CREATED_PORTAL_LINK"))
            mTrigger = Trigger.CreatedLink;
        else if (trigger.equals("CREATED_A_PORTAL_REGION"))
            mTrigger = Trigger.CreatedField;
        else if (trigger.equals("DESTROYED_A_RESONATOR"))
            mTrigger = Trigger.DestroyedResonator;
        else if (trigger.equals("DESTROYED_A_PORTAL_LINK"))
            mTrigger = Trigger.DestroyedLink;
        else if (trigger.equals("DESTROYED_PORTAL_REGION"))
            mTrigger = Trigger.DestroyedField;
        else if (trigger.equals("DEPLOYED_RESONATOR_MOD"))
            mTrigger = Trigger.DeployedMod;
        else if (trigger.equals("PORTAL_FULLY_POPULATED_WITH_RESONATORS"))
            mTrigger = Trigger.FullyDevloyedPortal;
        else if (trigger.equals("HACKING_ENEMY_PORTAL"))
            mTrigger = Trigger.HackingEnemyPortal;
        else if (trigger.equals("REDEEMED_AP"))
            mTrigger = Trigger.RedeemedAP;
        else if (trigger.equals("RECHARGE_RESONATOR"))
            mTrigger = Trigger.RechargeResonator;
        else if (trigger.equals("REMOTE_RECHARGE_RESONATOR"))
            mTrigger = Trigger.RemoteRechargeResonator;
        else if (trigger.equals("INVITED_PLAYER_JOINED"))
            mTrigger = Trigger.InvitedPlayerJoined;
        else
            mTrigger = Trigger.Unknown;
    }

    public int getAmount()
    {
        return mAmount;
    }

    public Trigger getTrigger()
    {
        return mTrigger;
    }
}
