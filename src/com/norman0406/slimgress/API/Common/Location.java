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

package com.norman0406.slimgress.API.Common;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;

public class Location
{
    private long latitude;
    private long longitude;

    public Location(JSONObject json) throws JSONException
    {
        latitude = json.getInt("latE6");
        longitude = json.getInt("lngE6");
    }

    public Location(long cellId)
    {
        S2CellId cell = new S2CellId(cellId);

        S2LatLng pos = cell.toLatLng();
        latitude = pos.lat().e6();
        longitude = pos.lng().e6();
    }

    public Location(double latDeg, double lngDeg)
    {
        S2LatLng pos = S2LatLng.fromDegrees(latDeg, lngDeg);

        latitude = pos.lat().e6();
        longitude = pos.lng().e6();
    }

    public Location(long latE6, long lngE6)
    {
        latitude = latE6;
        longitude = lngE6;
    }

    public long getLatitude()
    {
        return latitude;
    }

    public long getLongitude()
    {
        return longitude;
    }

    public LatLng getLatLng()
    {
        S2LatLng pos = S2LatLng.fromE6(latitude, longitude);
        return new LatLng(pos.latDegrees(), pos.lngDegrees());
    }
}
