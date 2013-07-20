package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IngressInterface {
	
	World world = null;
	Inventory inventory = null;
	Agent agent = null;
	String lastSyncTimestamp = "0";
	
	public IngressInterface() {
		inventory = new Inventory();
		world = new World();
	}
	
	private Interface checkInterface() {
		final Interface theInt = Interface.getInstance();
		if (!theInt.getIsAuthenticated())
			throw new RuntimeException("interface not authenticated");	
		if (!theInt.getHandshakeData().isValid())
			throw new RuntimeException("invalid handshake data");	
				
		if (agent == null) {
			agent = Interface.getInstance().getHandshakeData().getAgent();
		}
		
		return theInt;
	}
	
	private void processGameBasket(JSONHandlerGameBasket gameBasket) {
		inventory.processGameBasket(gameBasket);
		world.processGameBasket(gameBasket);
		
		// update player data
		PlayerEntity playerEntity = gameBasket.getPlayerEntity();
		if (playerEntity != null && agent != null)
			agent.update(playerEntity);
	}
	
	public void intGetInventory() throws JSONException, InterruptedException {
		Interface theInt = checkInterface();
		
		// create params
		JSONObject params = new JSONObject();
		params.put("lastQueryTimestamp", lastSyncTimestamp);
		
		// request basket
		JSONHandlerGameBasket gameBasket = new JSONHandlerGameBasket();
		theInt.request("playerUndecorated/getInventory", params, gameBasket);
		
		// process basket
		processGameBasket(gameBasket);
	}
	
	public void intGetObjectsInCells(Utils.LocationE6 playerLocation, double areaM2) throws JSONException, InterruptedException {
		Interface theInt = checkInterface();

		// get cell ids for surrounding area
		String cellIds[] = Utils.getCellIdsFromLocationArea(playerLocation, 16, 16, areaM2);

		// create cells
		JSONArray cellsAsHex = new JSONArray();
		for (int i = 0; i < cellIds.length; i++)
			cellsAsHex.put(cellIds[i]);

		// create dates (timestamps?)
		JSONArray dates = new JSONArray();
		for (int i = 0; i < cellsAsHex.length(); i++)
			dates.put(0);		
		
		// create params
		JSONObject params = new JSONObject();
		params.put("cellsAsHex", cellsAsHex);
		params.put("dates", dates);
		String loc = String.format("%08x,%08x", playerLocation.getLatitude(), playerLocation.getLongitude());
		params.put("playerLocation", loc);
		//params.put("knobSyncTimestamp", syncTimestamp);	// necessary?
		
		// request basket
		JSONHandlerGameBasket gameBasket = new JSONHandlerGameBasket();
		theInt.request("gameplay/getObjectsInCells", params, gameBasket);
		
		// process basket
		processGameBasket(gameBasket);		
	}
	
	public World getWorld() {
		return world;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Agent getAgent() {
		checkInterface();
		return agent;
	}
}
