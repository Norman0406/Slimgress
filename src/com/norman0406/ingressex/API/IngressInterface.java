package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.geometry.S2LatLngRect;

public class IngressInterface
{
	private static IngressInterface mSingleton = null;
	private Interface mInterface;
	
	Handshake mHandshake = null;
	World mWorld = null;
	Inventory mInventory = null;
	Agent mAgent = null;
	String mLastSyncTimestamp = "0";
	
	private IngressInterface()
	{
		mInterface = new Interface();
		mInventory = new Inventory();
		mWorld = new World();
	}
	
	public static IngressInterface getInstance()
	{
		if (mSingleton == null)
			mSingleton = new IngressInterface();
		
		return mSingleton;
	}
	
	public Interface.AuthSuccess intAuthenticate(String token)
	{
		return mInterface.authenticate(token);
	}
	
	public synchronized void intHandshake(final Runnable callback)
	{
		mInterface.handshake(new Handshake.Callback() {
			@Override
			public void handle(Handshake handshake) {
				mHandshake = handshake;
				callback.run();
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
	
	public void intGetInventory(final Utils.LocationE6 playerLocation, final Runnable callback)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					checkInterface();
					
					// create params
					JSONObject params = new JSONObject();
					params.put("lastQueryTimestamp", mLastSyncTimestamp);
					
					// request basket
					mInterface.request(mHandshake, "playerUndecorated/getInventory", playerLocation, params, new GameBasket.Callback() {
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
			
		}).start();
	}
		
	public void intGetObjectsInCells(final Utils.LocationE6 playerLocation, final S2LatLngRect region, final Runnable callback)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					checkInterface();
					
					// get cell ids for surrounding area
					String cellIds[] = Utils.getCellIdsFromRegion(region, 16, 16);
			
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
															
					// request basket
					mInterface.request(mHandshake, "gameplay/getObjectsInCells", playerLocation, params, new GameBasket.Callback() {
						@Override
						public void handle(GameBasket gameBasket) {
							// process basket
							processGameBasket(gameBasket);
							callback.run();
						}
					});
					/*mInterface.request(mHandshake, "gameplay/getObjectsInCells", playerLocation, params, new GameBasket.Callback() {
						@Override
						public void handle(GameBasket gameBasket) {
							// process basket
							processGameBasket(gameBasket);
						}
					});*/
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public synchronized World getWorld()
	{
		return mWorld;
	}
	
	public synchronized Inventory getInventory()
	{
		return mInventory;
	}
	
	public synchronized Agent getAgent()
	{
		checkInterface();
		return mAgent;
	}
}
