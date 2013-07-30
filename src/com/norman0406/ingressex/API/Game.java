package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;

public class Game
{
	private Interface mInterface;
	
	private Handshake mHandshake = null;
	private World mWorld = null;
	private Inventory mInventory = null;
	private Agent mAgent = null;
	private List<Plext> mPlexts = null;
	private String mLastSyncTimestamp = "0";
	
	public Game()
	{
		mInterface = new Interface();
		mInventory = new Inventory();
		mWorld = new World();
		mPlexts = new LinkedList<Plext>();
	}
	
	public Interface.AuthSuccess intAuthenticate(String token)
	{
		return mInterface.authenticate(token);
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
	
	public void intGetInventory(final Utils.LocationE6 playerLocation, final Handler.Callback callback)
	{
		try {
			checkInterface();
			
			// create params
			JSONObject params = new JSONObject();
			params.put("lastQueryTimestamp", mLastSyncTimestamp);
			
			// request basket
			mInterface.request(mHandshake, "playerUndecorated/getInventory", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					processGameBasket(gameBasket);
					callback.handleMessage(new Message());
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intGetObjectsInCells(final Utils.LocationE6 playerLocation, final S2LatLngRect region, final Handler.Callback callback)
	{
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
				public void handle(JSONObject json, GameBasket gameBasket) {
					processGameBasket(gameBasket);
					callback.handleMessage(new Message());
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intLoadCommunication(final Utils.LocationE6 position, final double radiusKM, final boolean factionOnly, final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			final double earthKM = 2 * Math.PI * 6371;	// circumference

			S2LatLng center = S2LatLng.fromE6(position.getLatitude(), position.getLongitude());
			S2LatLng size = S2LatLng.fromRadians((Math.PI / earthKM) * radiusKM,  (2 * Math.PI / earthKM) * radiusKM);
			S2LatLngRect region = S2LatLngRect.fromCenterSize(center, size);

			// get cell ids for area
			String[] cellIds = Utils.getCellIdsFromRegion(region, 8, 12);

			// create cells
			JSONArray cellsAsHex = new JSONArray();
			for (int i = 0; i < cellIds.length; i++)
				cellsAsHex.put(cellIds[i]);

			// create params
			JSONObject params = new JSONObject();
			params.put("cellsAsHex", cellsAsHex);
			params.put("minTimestampMs", -1);
			params.put("maxTimestampMs", -1);
			params.put("desiredNumItems", 50);
			params.put("factionOnly", factionOnly);
			params.put("ascendingTimestampOrder", false);
			
			mInterface.request(mHandshake, "playerUndecorated/getPaginatedPlexts", position, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					try {
						JSONArray result = json.getJSONArray("result");
						
						// add plexts
						for (int i = 0; i < result.length(); i++) {
							Plext newPlext = Plext.createPlext(result.getJSONArray(i));
							mPlexts.add(newPlext);
						}

						handler.handleMessage(new Message());
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intSendMessage(final Utils.LocationE6 playerPosition, final String message, final boolean factionOnly, final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			JSONObject params = new JSONObject();
			params.put("message", message);
			params.put("factionOnly", factionOnly);
			
			mInterface.request(mHandshake, "player/say", playerPosition, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					handler.handleMessage(new Message());
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intGetGameScore(final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			mInterface.request(mHandshake, "playerUndecorated/getGameScore", null, null, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					try {
						JSONObject result = json.getJSONObject("result");

						Bundle bundle = new Bundle();
						bundle.putInt("ResistanceScore", result.getInt("resistanceScore"));
						bundle.putInt("EnlightenedScore", result.getInt("alienScore"));
						Message msg = new Message();
						msg.setData(bundle);
						handler.handleMessage(msg);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intRedeemReward(String passcode, final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			JSONObject params = new JSONObject();
			JSONArray passcodes = new JSONArray();
			passcodes.put(passcode);
			params.put("params", passcodes);

			mInterface.request(mHandshake, "playerUndecorated/redeemReward", null, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Bundle bundle = new Bundle();
					
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0)
						bundle.putString("Error", errorMsg);
					else if (json.optJSONObject("result") != null) {
						// passcode accepted, process new items (does it work this way?)
						processGameBasket(gameBasket);
					}
					
					Message msg = new Message();
					msg.setData(bundle);
					handler.handleMessage(msg);
				}
			});
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void intGetNumberOfInvites(final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			mInterface.request(mHandshake, "playerUndecorated/getInviteInfo", null, null, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					try {
						JSONObject result = json.getJSONObject("result");
	
						Bundle bundle = new Bundle();
						bundle.putInt("NumInvites", result.getInt("numAvailableInvites"));
						Message msg = new Message();
						msg.setData(bundle);
						
						handler.handleMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intInviteUser(final String email, final String customMessage, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			if (customMessage == null)
				params.put("customMessage", "");
			else
				params.put("customMessage", customMessage);
			params.put("inviteeEmailAddress", email);
			
			mInterface.request(mHandshake, "playerUndecorated/inviteViaEmail", null, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					try {
						Bundle bundle = new Bundle();
						
						String errorMsg = json.optString("error");
						if (errorMsg.length() > 0)
							bundle.putString("Error", errorMsg);
						
						JSONObject result = json.optJSONObject("result");
						if (result != null)
							bundle.putInt("NumInvites", result.getInt("numAvailableInvites"));

						Message msg = new Message();
						msg.setData(bundle);
						handler.handleMessage(msg);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intValidateNickname(final String nickname, final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			JSONObject params = new JSONObject();
			JSONArray nicknames = new JSONArray();
			nicknames.put(nickname);
			params.put("params", nicknames);
			
			mInterface.request(mHandshake, "playerUndecorated/validateNickname", null, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();
					
					// INVALID_CHARACTERS, TOO_SHORT, BAD_WORDS, NOT_UNIQUE, CANNOT_EDIT
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intPersistNickname(final String nickname, final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			JSONObject params = new JSONObject();
			JSONArray nicknames = new JSONArray();
			nicknames.put(nickname);
			params.put("params", nicknames);
			
			mInterface.request(mHandshake, "playerUndecorated/persistNickname", null, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();
					
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intChooseFaction(final Utils.Team team, final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			JSONObject params = new JSONObject();
			JSONArray factions = new JSONArray();
			factions.put(Utils.getTeam(team));
			params.put("params", factions);
			
			mInterface.request(mHandshake, "playerUndecorated/chooseFaction", null, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();
					
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intGetNicknameFromUserGUID(String guid, final Handler.Callback handler)
	{
		String[] guids = { guid };
		intGetNicknamesFromUserGUIDs(guids, handler);
	}
	
	public void intGetNicknamesFromUserGUIDs(final String[] guids, final Handler.Callback handler)
	{
		try {
			checkInterface();
			
			// create params (don't know why there are two nested arrays)
			JSONObject params = new JSONObject();
			
			JSONArray playerGuids = new JSONArray();
			for (String guid : guids)
				playerGuids.put(guid);

			JSONArray playerIds = new JSONArray();
			playerIds.put(playerGuids);
			params.put("params", playerIds);
			
			mInterface.request(mHandshake, "playerUndecorated/getNickNamesFromPlayerIds", null, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					try {
						Bundle bundle = new Bundle();
						
						// get nickname if available
						JSONArray result = json.optJSONArray("result");
						if (result != null && result.length() > 0) {
							for (int i = 0; i < result.length(); i++) {
								if (!result.isNull(i))
									bundle.putString(guids[i], result.getString(i));
							}									
						}
						
						Message msg = new Message();
						msg.setData(bundle);
						handler.handleMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intFireXMP(ItemXMP xmp, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("itemGuid", xmp.getEntityGuid());
			
			mInterface.request(mHandshake, "gameplay/fireUntargetedRadialWeapon", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// PLAYER_DEPLETED, WEAPON_DOES_NOT_EXIST
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intHackPortal(GameEntityPortal portal, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("itemGuid", portal.getEntityGuid());
			
			mInterface.request(mHandshake, "gameplay/collectItemsFromPortal", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// TOO_SOON_BIG, TOO_SOON_(x), TOO_OFTEN, OUT_OF_RANGE, NEED_MORE_ENERGY, SERVER_ERROR
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intDeployResonator(ItemResonator resonator, GameEntityPortal portal, int slot, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("portalGuid", portal.getEntityGuid());
			params.put("preferredSlot", slot);
			
			JSONArray resonators = new JSONArray();
			resonators.put(resonator.getEntityGuid());
			params.put("itemGuids", resonators);
			
			mInterface.request(mHandshake, "gameplay/deployResonatorV2", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// PORTAL_OUT_OF_RANGE, TOO_MANY_RESONATORS_FOR_LEVEL_BY_USER, PORTAL_AT_MAX_RESONATORS, ITEM_DOES_NOT_EXIST, SERVER_ERROR
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intUpgradeResonator(ItemResonator resonator, GameEntityPortal portal, int slot, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("emitterGuid", resonator.getEntityGuid());
			params.put("portalGuid", portal.getEntityGuid());
			params.put("resonatorSlotToUpgrade", slot);
			
			mInterface.request(mHandshake, "gameplay/upgradeResonatorV2", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// PORTAL_OUT_OF_RANGE, CAN_ONLY_UPGRADE_TO_HIGHER_LEVEL, TOO_MANY_RESONATORS_FOR_LEVEL_BY_USER
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intAddMod(ItemMod mod, GameEntityPortal portal, int slot, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("modResourceGuid", mod.getEntityGuid());
			params.put("modableGuid", portal.getEntityGuid());
			params.put("index", slot);
			
			mInterface.request(mHandshake, "gameplay/addMod", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// PORTAL_OUT_OF_RANGE, (there must be others)
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intRemoveMod(GameEntityPortal portal, int slot, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("modableGuid", portal.getEntityGuid());
			params.put("index", slot);
			
			mInterface.request(mHandshake, "gameplay/removeMod", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// PORTAL_OUT_OF_RANGE, (there must be others)
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intDropItem(Item item, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("itemGuid", item.getEntityGuid());
			
			mInterface.request(mHandshake, "gameplay/dropItem", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					handler.handleMessage(new Message());
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intPickupItem(String guid, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("itemGuid", guid);
			
			mInterface.request(mHandshake, "gameplay/pickUp", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// RESOURCE_NOT_AVAILABLE
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intRecycleItem(Item item, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("itemGuid", item.getEntityGuid());
			
			mInterface.request(mHandshake, "gameplay/recycleItem", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// DOES_NOT_EXIST
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else if (json.optJSONObject("result") != null) {
						// result contains the gained xm value, put into msg
						processGameBasket(gameBasket);
					}
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intUsePowerCube(ItemPowerCube powerCube, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("itemGuid", powerCube.getEntityGuid());
			
			mInterface.request(mHandshake, "gameplay/dischargePowerCube", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					// DOES_NOT_EXIST
					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else if (json.optJSONObject("result") != null) {
						// result contains the gained xm value, put into msg
						processGameBasket(gameBasket);
					}
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intRechargePortal(GameEntityPortal portal, int[] slots, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("portalGuid", portal.getEntityGuid());
			
			JSONArray resonatorSlots = new JSONArray();
			for (int slot : slots) {
				resonatorSlots.put(slot);
			}
			params.put("resonatorSlots", resonatorSlots);
			
			mInterface.request(mHandshake, "gameplay/rechargeResonatorsV2", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intRemoteRechargePortal(GameEntityPortal portal, ItemPortalKey key, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("portalGuid", portal.getEntityGuid());
			params.put("portalKeyGuid", key.getEntityGuid());
			
			JSONArray resonatorSlots = new JSONArray();
			for (int i = 0; i < 8; i++) 
				resonatorSlots.put(i);
			params.put("resonatorSlots", resonatorSlots);
			
			mInterface.request(mHandshake, "gameplay/remoteRechargeResonatorsV2", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intQueryLinkablilityForPortal(GameEntityPortal portal, ItemPortalKey key, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("originPortalGuid", portal.getEntityGuid());
			
			JSONArray queryForPortals = new JSONArray();
			queryForPortals.put(key.getEntityGuid());
			params.put("portalLinkKeyGuidSet", queryForPortals);
			
			mInterface.request(mHandshake, "gameplay/getLinkabilityImpediment", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					
					// TODO: don't know the result yet
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intLinkPortal(GameEntityPortal portal, ItemPortalKey toKey, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("originPortalGuid", portal.getEntityGuid());
			params.put("destinationPortalGuid", toKey.getPortalGuid());
			params.put("linkKeyGuid", toKey.getEntityGuid());
						
			mInterface.request(mHandshake, "gameplay/createLink", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intSetNotificationSettings(final Handler.Callback handler)
	{
	}
	
	public void intGetModifiedEntity(final Handler.Callback handler)
	{
	}
	
	public void intFlipPortal(GameEntityPortal portal, ItemVirus virus, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();

			JSONObject params = new JSONObject();
			params.put("portalGuid", portal.getEntityGuid());
			params.put("resourceGuid", virus.getEntityGuid());
						
			mInterface.request(mHandshake, "gameplay/flipPortal", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					Message msg = new Message();

					String errorMsg = json.optString("error");
					if (errorMsg.length() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("Error", errorMsg);
						msg.setData(bundle);
					}
					else
						processGameBasket(gameBasket);
					
					handler.handleMessage(msg);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intSetPortalDetailsForCuration(final Handler.Callback handler)
	{
	}
	
	public void intGetUploadUrl(final Handler.Callback handler)
	{
		try {
			checkInterface();

			mInterface.request(mHandshake, "playerUndecorated/getUploadUrl", null, null, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					try {
						Bundle bundle = new Bundle();
						bundle.putString("Result", json.getString("result"));
						Message msg = new Message();
						msg.setData(bundle);
						handler.handleMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void intUploadPortalPhotoByUrl(String requestId, String imageUrl, final Handler.Callback handler)
	{
		//Log.w("", "not yet implemented");
	}
	
	public void intUploadPortalImage(final Handler.Callback handler)
	{
	}
	
	public void intFindNearbyPortals(int maxPortals, Utils.LocationE6 playerLocation, final Handler.Callback handler)
	{
		try {
			checkInterface();
	
			JSONObject params = new JSONObject();
			params.put("maxPortals", maxPortals);
						
			mInterface.request(mHandshake, "gameplay/findNearbyPortals", playerLocation, params, new GameBasket.Callback() {
				@Override
				public void handle(JSONObject json, GameBasket gameBasket) {
					// UNDONE
					handler.handleMessage(new Message());
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	public synchronized List<Plext> getPlexts()
	{
		return mPlexts;
	}
}