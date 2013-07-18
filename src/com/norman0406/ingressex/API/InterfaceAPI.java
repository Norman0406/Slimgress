package com.norman0406.ingressex.API;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2RegionCoverer;

public class InterfaceAPI {
	
	Interface theInt = null;
	
	InterfaceAPI(Interface theInt) {
		this.theInt = theInt;
	}
	
	private void checkInterface() {
		if (theInt == null || theInt.getIsAuthenticated())
			throw new IllegalStateException();
	}
	
	/*private void updateInventory() throws InterruptedException, JSONException {
		JSONObject params = new JSONObject();
		params.put("lastQueryTimestamp", agent.getLastSyncTimestamp());
		
		request("playerUndecorated/getInventory", params, new ProcessGameBasket(this));
	}
	
	private String[] getCellIds(Utils.LocationE6 location, int minLevel, int maxLevel, double area_m2) {
		S2LatLng pointLatLng = S2LatLng.fromE6(location.getLatitude(), location.getLongitude());
		
		//double area_m2 = 2000 * 2000;	// 1 km2
		double radius_m2 = 6371 * 1000; 
		double sr = area_m2 / (radius_m2 * radius_m2);
				
		S2Cap h1 = S2Cap.fromAxisArea(pointLatLng.toPoint(), sr);
		S2RegionCoverer rCov = new S2RegionCoverer();

		rCov.setMinLevel(minLevel);
		rCov.setMaxLevel(maxLevel);
		
		// get cells
		ArrayList<S2CellId> cells = rCov.getCovering(h1).cellIds();
		ArrayList<Long> cellIds = new ArrayList<Long>();
		for (int i = 0; i < cells.size(); i++) {
			
			S2CellId cellId = cells.get(i);
			
			// can happen for some reason
			if (cellId.level() < minLevel || cellId.level() > maxLevel)
				continue;
			
			cellIds.add(cellId.id());
		}
		
		// convert to hex values
		String cellIdsHex[] = new String[cellIds.size()];
		for (int i = 0; i < cellIdsHex.length; i++) {
			cellIdsHex[i] = Long.toHexString(cellIds.get(i));
		}

		return cellIdsHex;
	}
	
	private void updateWorld() throws InterruptedException, JSONException {

		JSONObject params = new JSONObject();
		
		// get player location
		Utils.LocationE6 playerLocation = new Utils.LocationE6(50.345963, 7.588223);
		
		// get cell ids within area
		double area = 1000 * 1000;	// area to cover
		String cellIds[] = getCellIds(playerLocation, 16, 16, area);
		
		// create cells
		JSONArray cellsAsHex = new JSONArray();
		for (int i = 0; i < cellIds.length; i++)
			cellsAsHex.put(cellIds[i]);

		// create dates (timestamps?)
		JSONArray dates = new JSONArray();
		for (int i = 0; i < cellsAsHex.length(); i++)
			dates.put(0);
		
		params.put("cellsAsHex", cellsAsHex);
		params.put("dates", dates);
		String loc = String.format("%08x,%08x", playerLocation.getLatitude(), playerLocation.getLongitude());
		params.put("playerLocation", loc);
		params.put("knobSyncTimestamp", syncTimestamp);	// necessary?
		
		request("gameplay/getObjectsInCells", params, new ProcessGameBasket(this));
	}*/
}
