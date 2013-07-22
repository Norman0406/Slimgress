package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IngressInterface
{
	private static IngressInterface singleton = null;
	private Interface theInterface;
	
	Handshake mHandshake = null;
	World mWorld = null;
	Inventory mInventory = null;
	Agent mAgent = null;
	String mLastSyncTimestamp = "0";
	
	private IngressInterface()
	{
		theInterface = new Interface();
		mInventory = new Inventory();
		mWorld = new World();
	}
	
	public static IngressInterface getInstance()
	{
		if (singleton == null)
			singleton = new IngressInterface();
		
		return singleton;
	}
	
	public Interface.AuthSuccess authenticate(String token)
	{
		return theInterface.authenticate(token);
	}
	
	public synchronized void handshake(final Handshake.Callback callback)
	{
		theInterface.handshake(new Handshake.Callback() {
			@Override
			public void handle(Handshake handshake) {
				mHandshake = handshake;
				callback.handle(handshake);
			}
		});
	}
	
	public synchronized void checkInterface()
	{		
		// check
		if (mHandshake == null || !mHandshake.isValid())
			throw new RuntimeException("invalid handshake data");

		// get agent
		if (mAgent == null)
			mAgent = mHandshake.getAgent();
	}
	
	private synchronized void processGameBasket(GameBasket gameBasket)
	{
		mInventory.processGameBasket(gameBasket);
		mWorld.processGameBasket(gameBasket);
		
		// update player data
		PlayerEntity playerEntity = gameBasket.getPlayerEntity();
		if (playerEntity != null && mAgent != null)
			mAgent.update(playerEntity);
	}
	
	public void intGetInventory(final Runnable callback)
	{
		try {
			checkInterface();
			
			// create params
			JSONObject params = new JSONObject();
			params.put("lastQueryTimestamp", mLastSyncTimestamp);
			
			// request basket
			theInterface.request(mHandshake, "playerUndecorated/getInventory", params, new GameBasket.Callback() {
				@Override
				public void handle(GameBasket gameBasket) {
					// process basket
					processGameBasket(gameBasket);
					callback.run();
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
		
	public void intGetObjectsInCells(Utils.LocationE6 playerLocation, double areaM2, final Runnable callback)
	{
		try {
			checkInterface();
	
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
			theInterface.request(mHandshake, "gameplay/getObjectsInCells", params, new GameBasket.Callback() {
				@Override
				public void handle(GameBasket gameBasket) {
					// process basket
					processGameBasket(gameBasket);
					callback.run();
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public World getWorld()
	{
		return mWorld;
	}
	
	public Inventory getInventory()
	{
		return mInventory;
	}
	
	public Agent getAgent()
	{
		checkInterface();
		return mAgent;
	}
}
