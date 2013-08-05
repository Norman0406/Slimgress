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

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

public class Knobs
{
    private Bundle mBundle;
    
    public Knobs(JSONObject json) throws JSONException
    {
        mBundle = new Bundle();
        put(json, mBundle);
    }
    
    private void put(JSONObject json, Bundle bundle) throws JSONException
    {
        if (json == null) {
            Log.w("Knobs", "invalid json object");
            return;
        }
        
        Iterator<?> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next().toString();
            Object item = json.get(key);
            
            if (item instanceof String) {
                mBundle.putString(key, (String)item);
            }
            else if (item instanceof Boolean) {
                mBundle.putBoolean(key, (Boolean)item);
            }
            else if (item instanceof Integer) {
                mBundle.putInt(key, (Integer)item);
            }
            else if (item instanceof Double) {
                mBundle.putDouble(key, (Double)item);
            }
            else if (item instanceof JSONObject) {
                Bundle newBundle = new Bundle();
                put((JSONObject)item, newBundle);
                mBundle.putBundle(key, newBundle);
            }
            else if (item instanceof JSONArray) {
                JSONArray array = (JSONArray)item;

                boolean classesAreEqual = true;
                
                // check array types first
                Object firstObject = null;
                for (int i = 0; i < array.length(); i++) {
                    if (firstObject == null)
                        firstObject = array.get(i);
                    else {
                        if (!array.get(i).getClass().equals(firstObject.getClass())) {
                            Log.w("Knobs", "classes inside array differ, this is not supported: " + key);
                            classesAreEqual = false;
                            break;
                        }
                    }
                }
                
                if (classesAreEqual && firstObject != null) {
                    Object[] newArray = new Object[array.length()];
                    for (int i = 0; i < array.length(); i++)
                        newArray[i] = array.get(i);
                    
                    if (firstObject instanceof String)
                        mBundle.putStringArray(key, (String[])newArray);
                    
                    // TODO: UNDONE
                }
            }
            else
                Log.w("Knobs", "unknown knob type: " + item.toString());
        }
    }
    
    protected Bundle getData()
    {
        return mBundle;
    }
}
