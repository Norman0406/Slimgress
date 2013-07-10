package com.norman0406.ingressex.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Interface {
	//private Agent agent;
	//private Inventory inventory;
	private DefaultHttpClient client;
	private String authToken;
	
	private final String apiBase = "https://betaspike.appspot.com/";
	private final String apiLogin = "_ah/login";
	private final String apiHandshake = "handshake";
	
	public Interface(String token)
	{
		client = new DefaultHttpClient();	
		
		authToken = token;
		
		try {
			login();
			handshake();
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
		    	
		    	String params = "?continue=http://localhost/&auth=" + authToken;
		    	String login = apiBase + apiLogin + params;
				HttpGet get = new HttpGet(login);

				try {
					client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
					HttpResponse response = client.execute(get);
					if (response.getStatusLine().getStatusCode() != 302) {
						// Response should be a redirect
						//return false;
					}
					
					for(Cookie cookie : client.getCookieStore().getCookies()) {
						if(cookie.getName().equals("SACSID")) {
						}
					}
					
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
		    	
		    	String params = "{\"nemesisSoftwareVersion\":\"2013-06-28T23:28:27Z 760a7a8ffc opt\",\"deviceSoftwareVersion\":\"4.2.0\"}";
		    	try {
		    		params = URLEncoder.encode(params, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	String handshake = apiBase + apiHandshake + "?json=" + params;
				
				HttpGet get = new HttpGet(handshake);
				get.setHeader("Accept-Charset", "utf-8");
				get.setHeader("Cache-Control", "max-age=0");

				try {
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
	
					if (entity != null) {
					    String content = EntityUtils.toString(entity);
					    
					    content = content.replace("while(1);", "");
					    
					    try {
							JSONObject json = new JSONObject(content);
							
							String name = json.getJSONObject("result").getString("nickname");
							String xsrfToken = json.getJSONObject("result").getString("xsrfToken");
							
						    System.out.println(content);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		
		handshakeThread.start();
		handshakeThread.join();
		
	}

	private void request() throws ClientProtocolException, IOException
	{
		HttpPost httppost = new HttpPost("https://m-dot-betaspike.appspot.com/rpc/playerUndecorated/getInventory");
		
		httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httppost.setHeader("Accept-Encoding", "gzip");
		httppost.setHeader("User-Agent", "Nemesis (gzip)");
		//httppost.setHeader("X-XsrfToken", "");
		httppost.setHeader("Host", "m-dot-betaspike.appspot.com");
		httppost.setHeader("Connection", "Keep-Alive");
		//httppost.setHeader("Cookie", "");
		
		
		// Request parameters and other properties.
		/*List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("param-1", "12345"));
		params.add(new BasicNameValuePair("param-2", "Hello!"));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));*/
	
		//Execute and get the response.
		HttpResponse response = client.execute(httppost);
		HttpEntity entity = response.getEntity();
	
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    try {
		        // do something useful
		    } finally {
		        instream.close();
		    }
		}
	}
	
	
}
