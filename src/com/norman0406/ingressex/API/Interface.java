package com.norman0406.ingressex.API;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
	
	private DefaultHttpClient client;
	private String sacsidCookie;
	
	// ingress api definitions	
	private static final String apiVersion = "2013-07-12T15:48:09Z d6f04b1fab4f opt";
	private static final String apiBase = "betaspike.appspot.com";
	private static final String apiBaseURL = "https://" + apiBase + "/";
	private static final String apiLogin = "_ah/login?continue=http://localhost/&auth=";
	private static final String apiHandshake = "handshake?json=";
	private static final String apiRequest = "rpc/";
		
	public interface CallbackRequest
	{
		public void handle(GameBasket gameBasket);
	}
	
	protected Interface()
	{
		client = new DefaultHttpClient();
	}
    
	protected synchronized AuthSuccess authenticate(final String token)
	{
		FutureTask<AuthSuccess> future = new FutureTask<AuthSuccess>(new Callable<AuthSuccess>() {
			@Override
			public AuthSuccess call() throws Exception {
				// see http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app
		    	// also use ?continue= (?)
		    	
		    	String login = apiBaseURL + apiLogin + token;
				HttpGet get = new HttpGet(login);

				try {
					client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
					HttpResponse response = client.execute(get);
					
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
						for(Cookie cookie : client.getCookieStore().getCookies()) {
							if(cookie.getName().equals("SACSID")) {	// secure cookie! (ACSID is non-secure http cookie)
								sacsidCookie = cookie.getValue();
							}
						}
						return AuthSuccess.Successful;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	protected synchronized void handshake(final Handshake.Callback callback)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject params = new JSONObject();
		    	try {
		    		// set handshake parameters
			    	params.put("nemesisSoftwareVersion", apiVersion);
					params.put("deviceSoftwareVersion", "4.2.0");
			    	String paramString = params.toString();
			    	paramString = URLEncoder.encode(paramString, "UTF-8");
			    	
			    	String handshake = apiBaseURL + apiHandshake + paramString;
					
					HttpGet get = new HttpGet(handshake);
					get.setHeader("Accept-Charset", "utf-8");
					get.setHeader("Cache-Control", "max-age=0");
			
					// do handshake
					HttpResponse response = client.execute(get);			
					HttpEntity entity = response.getEntity();
	
					if (entity != null) {
					    String content = EntityUtils.toString(entity);
					    entity.consumeContent();
					    
					    content = content.replace("while(1);", "");
					    
					    callback.handle(new Handshake(new JSONObject(content)));
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	protected synchronized void request(final Handshake handshake, final String requestString,
			final JSONObject requestParams, final GameBasket.Callback callback) throws InterruptedException
	{
		if (!handshake.isValid() || handshake.getXSRFToken().length() == 0)
			throw new RuntimeException("handshake is not valid");
		
		new Thread(new Runnable() {
		    public void run() {
		    	
		    	JSONObject params = new JSONObject();
		    	try {
					params.put("params", requestParams);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    	
				String postString = apiBaseURL + apiRequest + requestString;
				
				HttpPost post = new HttpPost(postString);
				
				try {
					StringEntity entity = new StringEntity(params.toString(), "UTF-8");
					entity.setContentType("application/json");
					post.setEntity(entity);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				post.setHeader("Content-Type", "application/json;charset=UTF-8");
				//post.setHeader("Accept-Encoding", "gzip");	// TODO: decode
				post.setHeader("User-Agent", "Nemesis (gzip)");
				post.setHeader("X-XsrfToken", handshake.getXSRFToken());
				post.setHeader("Host", apiBase);
				post.setHeader("Connection", "Keep-Alive");
				post.setHeader("Cookie", "SACSID=" + sacsidCookie);
				
				// execute and get the response.
				HttpResponse response;
				try {
					response = client.execute(post);

					if (response.getStatusLine().getStatusCode() == 401) {
						// token expired or similar
						//isAuthenticated = false;
					}
					else {
						HttpEntity entity = response.getEntity();
						
						if (entity != null) {
							String content = EntityUtils.toString(entity);
							callback.handle(new GameBasket(new JSONObject(content)));
						}
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
}
