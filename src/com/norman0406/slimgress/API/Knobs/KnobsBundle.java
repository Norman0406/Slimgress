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

package com.norman0406.slimgress.API.Knobs;

import org.json.JSONException;
import org.json.JSONObject;

public class KnobsBundle
{
    private long mSyncTimestamp;
    private ClientFeatureKnobs mClientFeatureKnobs;
    private InventoryKnobs mInventoryKnobs;
    private NearbyPortalKnobs mNearbyPortalKnobs;
    private PortalKnobs mPortalKnobs;
    private PortalModSharedKnobs mPortalModSharedKnobs;
    private RecycleKnobs mRecycleKnobs;
    private ScannerKnobs mScannerKnobs;
    private WeaponRangeKnobs mWeaponRangeKnobs;
    private XMCostKnobs mXMCostKnobs;

    public KnobsBundle(JSONObject json) throws JSONException
    {
        mSyncTimestamp = Long.parseLong(json.getString("syncTimestamp"));

        JSONObject bundleMap = json.getJSONObject("bundleMap");

        mClientFeatureKnobs = new ClientFeatureKnobs(bundleMap.getJSONObject("ClientFeatureKnobs"));
        mInventoryKnobs = new InventoryKnobs(bundleMap.getJSONObject("InventoryKnobs"));
        mNearbyPortalKnobs = new NearbyPortalKnobs(bundleMap.getJSONObject("NearbyPortalKnobs"));
        mPortalKnobs = new PortalKnobs(bundleMap.getJSONObject("PortalKnobs"));
        mPortalModSharedKnobs = new PortalModSharedKnobs(bundleMap.getJSONObject("PortalModSharedKnobs"));
        mRecycleKnobs = new RecycleKnobs(bundleMap.getJSONObject("recycleKnobs"));
        mScannerKnobs = new ScannerKnobs(bundleMap.getJSONObject("ScannerKnobs"));
        mWeaponRangeKnobs = new WeaponRangeKnobs(bundleMap.getJSONObject("WeaponRangeKnobs"));
        mXMCostKnobs = new XMCostKnobs(bundleMap.getJSONObject("XmCostKnobs"));
    }

    public long getSyncTimestamp()
    {
        return mSyncTimestamp;
    }

    public ClientFeatureKnobs getClientFeatureKnobs()
    {
        return mClientFeatureKnobs;
    }

    public InventoryKnobs getInventoryKnobs()
    {
        return mInventoryKnobs;
    }

    public NearbyPortalKnobs getNearbyPortalKnobs()
    {
        return mNearbyPortalKnobs;
    }

    public PortalKnobs getPortalKnobs()
    {
        return mPortalKnobs;
    }

    public PortalModSharedKnobs getPortalModSharedKnobs()
    {
        return mPortalModSharedKnobs;
    }

    public RecycleKnobs getRecycleKnobs()
    {
        return mRecycleKnobs;
    }

    public ScannerKnobs getScannerKnobs()
    {
        return mScannerKnobs;
    }

    public WeaponRangeKnobs getWeaponRangeKnobs()
    {
        return mWeaponRangeKnobs;
    }

    public XMCostKnobs getXMCostKnobs()
    {
        return mXMCostKnobs;
    }
}
