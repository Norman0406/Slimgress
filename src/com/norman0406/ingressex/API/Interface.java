package com.norman0406.ingressex.API;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
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

import com.google.common.geometry.S1Angle;
import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2RegionCoverer;
import com.norman0406.ingressex.ActivityMain.AuthenticateCallback;
import com.norman0406.ingressex.API.Utils.LocationE6;

public class Interface {
	
	private Agent agent = null;
	private Inventory inventory = null;
	private World world = null;
	
	private String syncTimestamp;
	public boolean isAuthenticated;
	
	private DefaultHttpClient client;
	//private String authToken;
	private String xsrfToken;
	private String sacsidCookie;

	// ingress api definition
	private final String apiVersion = "2013-07-12T15:48:09Z d6f04b1fab4f opt";
	private final String apiBase = "betaspike.appspot.com";
	private final String apiBaseURL = "https://" + apiBase + "/";
	private final String apiLogin = "_ah/login?continue=http://localhost/&auth=";
	private final String apiHandshake = "handshake?json=";
	private final String apiRequest = "rpc/";

	private abstract class RequestCallback {
		private Interface theInterface;
		RequestCallback(Interface inter) {
			theInterface = inter;
		}
		
		abstract void requestFinished(JSONObject json) throws JSONException;
	}
	
	public Interface() {
		client = new DefaultHttpClient();
		isAuthenticated = false;
	}
	
	public void authenticate(final String token, final AuthenticateCallback callback) {
		new Thread(new Runnable() {
		    public void run() {
		    	// see http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app
		    	// also use ?continue= (?)
		    	
		    	String login = apiBaseURL + apiLogin + token;
				HttpGet get = new HttpGet(login);

				try {
					client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
					HttpResponse response = client.execute(get);
					
					if (response.getStatusLine().getStatusCode() == 401) {
						// TODO: the token has expired
						// call AccountManager.invalidateToken() and authenticate again
						callback.authenticationFinished(token, 1);
					}
					else if (response.getStatusLine().getStatusCode() != 302) {
						// Response should be a redirect
						//return false;
						callback.authenticationFinished(token, 2);
					}
					else {
						// get cookie
						for(Cookie cookie : client.getCookieStore().getCookies()) {
							if(cookie.getName().equals("SACSID")) {	// secure cookie! (ACSID is non-secure http cookie)
								sacsidCookie = cookie.getValue();
							}
						}
						
						handshake(new JSONHandlerHandshake());
						
						inventory = new Inventory();
						world = new World();
						
						callback.authenticationFinished(token, 0);
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
		  }).start();
	}
	
	private void handshake(JSONHandler handler) {
    	JSONObject params = new JSONObject();
    	try {
	    	params.put("nemesisSoftwareVersion", apiVersion);
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
			    	handler.handleJSON(new JSONObject(content));
					//processHandshakeData(new JSONObject(content));
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

	public boolean getIsAuthenticated() {
		return isAuthenticated;
	}
	
	private void request(final String requestString, final JSONObject requestParams, final JSONHandler handler) throws InterruptedException {
		new Thread(new Runnable() {
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

					//if (response.getStatusLine().getStatusCode() == 200) {					
						HttpEntity entity = response.getEntity();
						
						if (entity != null) {
							String content = EntityUtils.toString(entity);						
							handler.handleJSON(new JSONObject(content));
						//}
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
		}).start();	
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
