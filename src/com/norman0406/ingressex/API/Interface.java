package com.norman0406.ingressex.API;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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

public class Interface
{
    public enum AuthSuccess
    {
    	Successful,
    	TokenExpired,
    	UnknownError
    }
	
	private DefaultHttpClient mClient;
	private String mCookie;
	
	// ingress api definitions
	private static final String mApiVersion = "2013-07-12T15:48:09Z d6f04b1fab4f opt";
	private static final String mApiBase = "betaspike.appspot.com";
	private static final String mApiBaseURL = "https://" + mApiBase + "/";
	private static final String mApiLogin = "_ah/login?continue=http://localhost/&auth=";
	private static final String mApiHandshake = "handshake?json=";
	private static final String mApiRequest = "rpc/";
	
	protected Interface()
	{
		mClient = new DefaultHttpClient();
	}
    
	protected AuthSuccess authenticate(final String token)
	{
		FutureTask<AuthSuccess> future = new FutureTask<AuthSuccess>(new Callable<AuthSuccess>() {
			@Override
			public AuthSuccess call() throws Exception {
				// see http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app
		    	// also use ?continue= (?)
		    	
		    	String login = mApiBaseURL + mApiLogin + token;
				HttpGet get = new HttpGet(login);

				try {
					HttpResponse response = null;
					synchronized(Interface.this) {
						mClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
						response = mClient.execute(get);
					}
					assert(response != null);
					response.getEntity().consumeContent();
					
					if (response.getStatusLine().getStatusCode() == 401) {
						// the token has expired
						return AuthSuccess.TokenExpired;
					}
					else if (response.getStatusLine().getStatusCode() != 302) {
						// Response should be a redirect
						return AuthSuccess.UnknownError;
					}
					else {
						// get cookie
						synchronized(Interface.this) {
							for(Cookie cookie : mClient.getCookieStore().getCookies()) {
								if(cookie.getName().equals("SACSID")) {	// secure cookie! (ACSID is non-secure http cookie)
									mCookie = cookie.getValue();
								}
							}
						}
						return AuthSuccess.Successful;
					}
				}
				catch (ClientProtocolException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					synchronized(Interface.this) {
						mClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
					}
				}
				return AuthSuccess.Successful;
			}
		});
		
		// start thread
		new Thread(future).start();
		
		// obtain authentication return value
		AuthSuccess retVal = AuthSuccess.UnknownError;
		try {
			retVal = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	protected void handshake(final Handshake.Callback callback)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject params = new JSONObject();
		    	try {
		    		// set handshake parameters
			    	params.put("nemesisSoftwareVersion", mApiVersion);
					params.put("deviceSoftwareVersion", "4.2.0");
			    	String paramString = params.toString();
			    	paramString = URLEncoder.encode(paramString, "UTF-8");
			    	
			    	String handshake = mApiBaseURL + mApiHandshake + paramString;
					
					HttpGet get = new HttpGet(handshake);
					get.setHeader("Accept-Charset", "utf-8");
					get.setHeader("Cache-Control", "max-age=0");
			
					// do handshake
					HttpResponse response = null;
					synchronized(Interface.this) {
						response = mClient.execute(get);
					}
					assert(response != null);
					HttpEntity entity = response.getEntity();
	
					if (entity != null) {
					    String content = EntityUtils.toString(entity);
					    entity.consumeContent();
					    
					    content = content.replace("while(1);", "");
					    
					    // handle handshake data
					    callback.handle(new Handshake(new JSONObject(content)));
					}
				}
		    	catch (ClientProtocolException e) {
					e.printStackTrace();
				}
		    	catch (IOException e) {
					e.printStackTrace();
				}
		    	catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	protected void request(final Handshake handshake, final String requestString, final Utils.LocationE6 playerLocation,
			final JSONObject requestParams, final GameBasket.Callback callback) throws InterruptedException
	{
		if (!handshake.isValid() || handshake.getXSRFToken().length() == 0)
			throw new RuntimeException("handshake is not valid");
		
		new Thread(new Runnable() {
		    public void run() {

		    	// create post
				String postString = mApiBaseURL + mApiRequest + requestString;
				HttpPost post = new HttpPost(postString);
				
				// set additional parameters
		    	JSONObject params = new JSONObject();
		    	if (requestParams != null) {
		    		if (requestParams.has("params"))
		    			params = requestParams;
		    		else {
			    		try {
				    		params.put("params", requestParams);
							
							// add persistent request parameters
				    		if (playerLocation != null) {
								String loc = String.format("%08x,%08x", playerLocation.getLatitude(), playerLocation.getLongitude());
								params.getJSONObject("params").put("playerLocation", loc);
								params.getJSONObject("params").put("location", loc);
				    		}				    		
							params.getJSONObject("params").put("knobSyncTimestamp", getCurrentTimestamp());
							
							JSONArray collectedEnergy = new JSONArray();
							
							// UNDONE: add collected energy guids
							
							params.getJSONObject("params").put("energyGlobGuids", collectedEnergy);
						} catch (JSONException e) {
							e.printStackTrace();
						}
		    		}
		    	}
		    	else {
		    		try {
						params.put("params", null);
					} catch (JSONException e) {
						e.printStackTrace();
					}
		    	}
				
				try {
					StringEntity entity = new StringEntity(params.toString(), "UTF-8");
					entity.setContentType("application/json");
					post.setEntity(entity);
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}		
				
		    	// set header
				post.setHeader("Content-Type", "application/json;charset=UTF-8");
				//post.setHeader("Accept-Encoding", "gzip");	// TODO: decode
				post.setHeader("User-Agent", "Nemesis (gzip)");
				post.setHeader("X-XsrfToken", handshake.getXSRFToken());
				post.setHeader("Host", mApiBase);
				post.setHeader("Connection", "Keep-Alive");
				post.setHeader("Cookie", "SACSID=" + mCookie);
				
				// execute and get the response.
				try {
					HttpResponse response = null;
					String content = null;
					
					synchronized(Interface.this) {
						response = mClient.execute(post);
						assert(response != null);
	
						if (response.getStatusLine().getStatusCode() == 401) {
							// token expired or similar
							//isAuthenticated = false;
							response.getEntity().consumeContent();
						}
						else {
							HttpEntity entity = response.getEntity();
							content = EntityUtils.toString(entity);
							entity.consumeContent();
						}
					}

					// handle game basket
					if (content != null) {
						JSONObject json = new JSONObject(content);
						
						if (json.has("exception")) {
							String excMsg = json.getString("exception");
							throw new RuntimeException(excMsg);
						}
						
						callback.handle(json, new GameBasket(json));
					}
				}
				catch (ClientProtocolException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
		    }
		}).start();
	}
	
	private long getCurrentTimestamp()
	{
		return (new Date()).getTime();
	}
}
