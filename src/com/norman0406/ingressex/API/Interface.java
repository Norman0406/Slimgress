package com.norman0406.ingressex.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Interface {
	private Agent agent = null;
	private Inventory inventory = null;
	
	private DefaultHttpClient client;
	private String authToken;
	private String xsrfToken;
	private String sacsidCookie;

	// ingress api definition
	private final String apiBase = "https://betaspike.appspot.com/";
	private final String apiLogin = "_ah/login?continue=http://localhost/&auth=";
	private final String apiHandshake = "handshake?json=";
	private final String apiRequest = "rpc/";
	
	public Interface(String token)
	{
		client = new DefaultHttpClient();
		
		authToken = token;
		
		try {
			login();
			handshake();

			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("lastQueryTimestamp", "1373498144202"));
			
			request("playerUndecorated/getInventory", params, new ProcessGameBasket(this));

			/*String json;
			new ProcessGameBasket().requestFinished(json);*/
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void login() throws InterruptedException
	{
		Thread loginThread = new Thread(new Runnable() {
		    public void run() {
		    	// see http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app
		    	// also use ?continue= (?)
		    	
		    	String login = apiBase + apiLogin + authToken;
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
	
	private void handshake() throws InterruptedException
	{
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
		    	String handshake = apiBase + apiHandshake + paramString;
				
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
	
	private void processHandshakeData(JSONObject json) throws JSONException
	{
		// get json objects
		JSONObject result = json.getJSONObject("result");
		JSONArray playerEntity = result.getJSONArray("playerEntity");
		JSONObject controllingTeam = playerEntity.getJSONObject(2).getJSONObject("controllingTeam");
		JSONObject playerPersonal = playerEntity.getJSONObject(2).getJSONObject("playerPersonal");
		
		// set application specific data
		xsrfToken = result.getString("xsrfToken");
		
		// set static agent data
		if (agent == null) {
			Agent.Team agentTeam = Agent.Team.ENLIGHTENED;
			
			if (controllingTeam.getString("team").equals("RESISTANCE"))
				agentTeam = Agent.Team.RESISTANCE;
			
			agent = new Agent(playerEntity.getString(0), result.getString("nickname"), agentTeam);
		}
		
		// set dynamic agent data
		agent.setAp(Integer.parseInt(playerPersonal.getString("ap")));
		agent.setEnergy(playerPersonal.getInt("energy"));
		agent.setCanPlay(result.getBoolean("canPlay"));
		agent.setAllowNicknameEdit(playerPersonal.getBoolean("allowNicknameEdit"));
		agent.setAllowFactionChoice(playerPersonal.getBoolean("allowFactionChoice"));
	}

	private abstract class RequestCallback
	{
		private Interface theInterface;
		RequestCallback(Interface inter)
		{
			theInterface = inter;
		}
		
		abstract void requestFinished(JSONObject json) throws JSONException;
	}
	
	private void request(final String requestString, final List<NameValuePair> requestParams, final RequestCallback finished) throws InterruptedException
	{
		Thread requestThread = new Thread(new Runnable() {
		    public void run() {
		    	
		    	// create some json params
		    	JSONObject params = new JSONObject();
		    	JSONObject actualParams = new JSONObject();

	    		try {
			    	for (NameValuePair pair : requestParams) {
			    		actualParams.put(pair.getName(), pair.getValue());
			    	}
			    	params.put("params", actualParams);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	String paramString = params.toString();
		    	
				String postString = apiBase + apiRequest + requestString;
				
				HttpPost post = new HttpPost(postString);
				
				try {
					StringEntity entity = new StringEntity(paramString, "UTF-8");
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
				//post.setHeader("Host", apiBase);	// is not working
				post.setHeader("Host", "m-dot-betaspike.appspot.com");	// is working
				post.setHeader("Connection", "Keep-Alive");
				post.setHeader("Cookie", "SACSID=" + sacsidCookie);
				
				// execute and get the response.
				HttpResponse response;
				try {
					response = client.execute(post);
					HttpEntity entity = response.getEntity();
					
					if (entity != null) {
						try {
							String content = EntityUtils.toString(entity);
						    finished.requestFinished(new JSONObject(content));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    finally {
						    entity.consumeContent();
					    }
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		requestThread.start();
		requestThread.join();		
	}
	
	private class ProcessGameBasket extends RequestCallback {
		ProcessGameBasket(Interface inter) {
			super(inter);
		}

		@Override
		public void requestFinished(JSONObject json) throws JSONException
		{
			// NOTE: inventory is beeing loaded in chunks. process multiple times
			
			String lastTimestamp = json.getString("result");
			
			JSONObject gameBasket = json.getJSONObject("gameBasket");
			
			processGameEntities(gameBasket.getJSONArray("gameEntities"));
			processInventory(gameBasket.getJSONArray("inventory"));
		}
		
		private void processGameEntities(JSONArray gameEntities) throws JSONException
		{
		}
		
		private void processInventory(JSONArray inventory) throws JSONException
		{
			// create a new inventory
			if (inventory.length() > 0 && super.theInterface.inventory == null) {
				super.theInterface.inventory = new Inventory();
			}
			
			// iterate over inventory items
			for (int i = 0; i < inventory.length(); i++) {
				JSONArray resource = inventory.getJSONArray(i);
				JSONObject itemDescr = resource.getJSONObject(2);
				
				Item newItem = Item.createItem(resource.getString(0), resource.getString(1), itemDescr);

				// add the new item to the player inventory
				if (newItem != null) {
					super.theInterface.inventory.addItem(newItem);
				}
			}
			
			System.out.println("Hallo");
		}		
	}
}
