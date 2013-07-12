package com.norman0406.ingressex.API;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Interface {
	
	private Agent agent = null;
	private Inventory inventory = null;
	private World world = null;
	
	private DefaultHttpClient client;
	private String authToken;
	private String xsrfToken;
	private String sacsidCookie;

	// ingress api definition
	private final String apiBase = "betaspike.appspot.com";
	private final String apiBaseURL = "https://" + apiBase + "/";
	private final String apiLogin = "_ah/login?continue=http://localhost/&auth=";
	private final String apiHandshake = "handshake?json=";
	private final String apiRequest = "rpc/";
	
	public Interface(String token) {
		client = new DefaultHttpClient();
		
		authToken = token;
		
		try {
			login();
			handshake();
			
			inventory = new Inventory();
			world = new World();

			updateInventory();
			//updateWorld();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateInventory() throws InterruptedException, JSONException {
		JSONObject params = new JSONObject();
		params.put("lastQueryTimestamp", agent.getLastSyncTimestamp());
		
		request("playerUndecorated/getInventory", params, new ProcessGameBasket(this));
	}
	
	private void updateWorld() throws InterruptedException, JSONException {

		JSONObject params = new JSONObject();

		JSONArray cells = new JSONArray();
		cells.put(null);
		
		JSONArray energyGlobGuids = new JSONArray();
				
		// create cells
		JSONArray cellsAsHex = new JSONArray();
		cellsAsHex.put("478af45f10000000");

		// create dates (timestamps?)
		JSONArray dates = new JSONArray();
		dates.put(0);
		
		params.put("cells", null);
		params.put("cellsAsHex", cellsAsHex);
		params.put("dates", dates);
		params.put("energyGlobGuids", energyGlobGuids);
		params.put("knobSyncTimestamp", Long.valueOf("1358200098740"));
		params.put("playerLocation", "02b1a282,00577cb9");
				
		request("gameplay/getObjectsInCells", params, new ProcessGameBasket(this));
	}
	
	private void login() throws InterruptedException {
		Thread loginThread = new Thread(new Runnable() {
		    public void run() {
		    	// see http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app
		    	// also use ?continue= (?)
		    	
		    	String login = apiBaseURL + apiLogin + authToken;
				HttpGet get = new HttpGet(login);

				try {
					client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
					HttpResponse response = client.execute(get);
					if (response.getStatusLine().getStatusCode() != 302) {
						// Response should be a redirect
						//return false;
					}
					
					// get cookie
					for(Cookie cookie : client.getCookieStore().getCookies()) {
						if(cookie.getName().equals("SACSID")) {	// secure cookie! (ACSID is non-secure http cookie)
							sacsidCookie = cookie.getValue();
						}
					}

					response.getEntity().consumeContent();
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
					client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
				}
		    }
		  });
		
		loginThread.start();
		loginThread.join();
	}
	
	private void handshake() throws InterruptedException {
		Thread handshakeThread = new Thread(new Runnable() {
		    public void run() {
		    	
		    	JSONObject params = new JSONObject();
		    	try {
			    	params.put("nemesisSoftwareVersion", "2013-06-28T23:28:27Z 760a7a8ffc opt");
					params.put("deviceSoftwareVersion", "4.2.0");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	// TODO: get from system information
		    	
		    	String paramString = params.toString();
		    	
		    	try {
		    		paramString = URLEncoder.encode(paramString, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	String handshake = apiBaseURL + apiHandshake + paramString;
				
				HttpGet get = new HttpGet(handshake);
				get.setHeader("Accept-Charset", "utf-8");
				get.setHeader("Cache-Control", "max-age=0");

				try {
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
	
					if (entity != null) {
					    String content = EntityUtils.toString(entity);
					    entity.consumeContent();
					    
					    content = content.replace("while(1);", "");
					    
					    try {
							processHandshakeData(new JSONObject(content));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					    finally {
						    entity.consumeContent();
					    }
					}					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		  });
		
		handshakeThread.start();
		handshakeThread.join();
	}
	
	private void processHandshakeData(JSONObject json) throws JSONException {
		// get json objects
		JSONObject result = json.getJSONObject("result");
		JSONArray playerEntity = result.getJSONArray("playerEntity");
		JSONObject controllingTeam = playerEntity.getJSONObject(2).getJSONObject("controllingTeam");
		JSONObject playerPersonal = playerEntity.getJSONObject(2).getJSONObject("playerPersonal");
		
		// set application specific data
		xsrfToken = result.getString("xsrfToken");
		
		// set static agent data
		if (agent == null) {
			Utils.Team agentTeam = Utils.Team.Enlightened;
			
			if (controllingTeam.getString("team").equals("RESISTANCE"))
				agentTeam = Utils.Team.Resistance;
			else if (controllingTeam.getString("team").equals("ALIENS"))
				agentTeam = Utils.Team.Enlightened;
			else
				System.out.println("unknown team string");
			
			agent = new Agent(playerEntity.getString(0), result.getString("nickname"), agentTeam);
		}
		
		// set dynamic agent data
		agent.setAp(Integer.parseInt(playerPersonal.getString("ap")));
		agent.setEnergy(playerPersonal.getInt("energy"));
		agent.setCanPlay(result.getBoolean("canPlay"));
		agent.setAllowNicknameEdit(playerPersonal.getBoolean("allowNicknameEdit"));
		agent.setAllowFactionChoice(playerPersonal.getBoolean("allowFactionChoice"));
	}
	
	private void request(final String requestString, final JSONObject requestParams, final RequestCallback finished) throws InterruptedException {
		Thread requestThread = new Thread(new Runnable() {
		    public void run() {
		    	
		    	JSONObject params = new JSONObject();
		    	try {
					params.put("params", requestParams);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
				String postString = apiBaseURL + apiRequest + requestString;
				
				HttpPost post = new HttpPost(postString);
				
				try {
					StringEntity entity = new StringEntity(params.toString(), "UTF-8");
					entity.setContentType("application/json");
					post.setEntity(entity);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				post.setHeader("Content-Type", "application/json;charset=UTF-8");
				//post.setHeader("Accept-Encoding", "gzip");	// TODO: decode
				post.setHeader("User-Agent", "Nemesis (gzip)");
				post.setHeader("X-XsrfToken", xsrfToken);
				post.setHeader("Host", apiBase);
				post.setHeader("Connection", "Keep-Alive");
				post.setHeader("Cookie", "SACSID=" + sacsidCookie);
				
				// execute and get the response.
				HttpResponse response;
				try {
					response = client.execute(post);
					
					HttpEntity entity = response.getEntity();
					
					if (entity != null) {
						String content = EntityUtils.toString(entity);						
					    finished.requestFinished(new JSONObject(content));
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		requestThread.start();
		requestThread.join();		
	}

	private abstract class RequestCallback {
		private Interface theInterface;
		RequestCallback(Interface inter) {
			theInterface = inter;
		}
		
		abstract void requestFinished(JSONObject json) throws JSONException;
	}
	
	private class ProcessGameBasket extends RequestCallback {
		ProcessGameBasket(Interface inter) {
			super(inter);
		}

		@Override
		public void requestFinished(JSONObject json) throws JSONException {
			JSONObject gameBasket = json.getJSONObject("gameBasket");
			
			processGameEntities(gameBasket.getJSONArray("gameEntities"));
			processInventory(gameBasket.getJSONArray("inventory"));
			processDeletedEntityGuids(gameBasket.getJSONArray("deletedEntityGuids"));

			String timestamp = json.getString("result");
			super.theInterface.getAgent().setLastSyncTimestamp(timestamp);
		}
		
		private void processGameEntities(JSONArray gameEntities) throws JSONException {
			if (super.theInterface.world != null) {
				// iterate over game entites
				for (int i = 0; i < gameEntities.length(); i++) {
					JSONArray resource = gameEntities.getJSONArray(i);

					// deserialize the game entity using the JSON representation
					GameEntity newEntity = GameEntity.createEntity(resource.getString(0), resource.getString(1), resource.getJSONObject(2));
					
					// add the new entity to the world
					if (newEntity != null) {
						super.theInterface.world.addEntity(newEntity);
					}
				}
			}
		}
		
		private void processInventory(JSONArray inventory) throws JSONException {
			if (super.theInterface.inventory != null) {
				// iterate over inventory items
				for (int i = 0; i < inventory.length(); i++) {
					JSONArray resource = inventory.getJSONArray(i);
					
					// deserialize the item using the JSON representation
					Item newItem = Item.createItem(resource.getString(0), resource.getString(1), resource.getJSONObject(2));

					// add the new item to the player inventory
					if (newItem != null) {
						super.theInterface.inventory.addItem(newItem);
					}
				}
			}
		}
		
		private void processDeletedEntityGuids(JSONArray deletedEntityGuids) throws JSONException {
		}
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public World getWorld() {
		return world;
	}
}
