/***********************************************************************
*
* Slimgress: Ingress API for Android
* Copyright (C) 2013 Norman Link <norman.link@gmx.net>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
***********************************************************************/

package com.norman0406.slimgress.API.Interface;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
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

import com.norman0406.slimgress.API.Common.Location;

import android.os.Build;
import android.util.Log;

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
    private static final String mApiVersion = "2013-07-29T18:57:27Z 7af0d9a744b opt";
    private static final String mApiBase = "m-dot-betaspike.appspot.com";
    private static final String mApiBaseURL = "https://" + mApiBase + "/";
    private static final String mApiLogin = "_ah/login?continue=http://localhost/&auth=";
    private static final String mApiHandshake = "handshake?json=";
    private static final String mApiRequest = "rpc/";

    public Interface()
    {
        mClient = new DefaultHttpClient();
    }

    public AuthSuccess authenticate(final String token)
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
                        Log.i("Interface", "executing authentication");
                        response = mClient.execute(get);
                    }
                    assert(response != null);
                    String content = EntityUtils.toString(response.getEntity());
                    response.getEntity().consumeContent();

                    if (response.getStatusLine().getStatusCode() == 401) {
                        // the token has expired
                        Log.i("Interface", "authentication token has expired");
                        return AuthSuccess.TokenExpired;
                    }
                    else if (response.getStatusLine().getStatusCode() != 302) {
                        // Response should be a redirect
                        Log.i("Interface", "unknown error: " + response.getStatusLine().getReasonPhrase());
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

                        if (mCookie == null) {
                            Log.e("Interface", "response does not contain a secure cookie");
                            Log.d("Interface", "content: " + content);
                            return AuthSuccess.UnknownError;
                        }

                        Log.i("Interface", "authentication successful");
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

    public void handshake(final Handshake.Callback callback)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject params = new JSONObject();
                try {
                    // set handshake parameters
                    params.put("nemesisSoftwareVersion", mApiVersion);
                    params.put("deviceSoftwareVersion", Build.VERSION.RELEASE);

                    // TODO:
                    /*params.put("activationCode", "");
                    params.put("tosAccepted", "1");
                    params.put("a", "");*/

                    String paramString = params.toString();
                    paramString = URLEncoder.encode(paramString, "UTF-8");

                    String handshake = mApiBaseURL + mApiHandshake + paramString;

                    HttpGet get = new HttpGet(handshake);
                    get.setHeader("Accept-Charset", "utf-8");
                    get.setHeader("Cache-Control", "max-age=0");

                    // do handshake
                    HttpResponse response = null;
                    synchronized(Interface.this) {
                        Log.i("Interface", "executing handshake");
                        response = mClient.execute(get);
                    }
                    assert(response != null);
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        String content = EntityUtils.toString(entity);
                        Header contentType = entity.getContentType();
                        entity.consumeContent();

                        // check for content type json
                        if (!contentType.getName().equals("Content-Type") || !contentType.getValue().contains("application/json"))
                            throw new RuntimeException("content type is not json");

                        content = content.replace("while(1);", "");

                        // handle handshake data
                        callback.handle(new Handshake(new JSONObject(content)));

                        Log.i("Interface", "handshake finished");
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

    public void request(final Handshake handshake, final String requestString, final Location playerLocation,
            final JSONObject requestParams, final RequestResult result) throws InterruptedException
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

                            // TODO: add collected energy guids

                            params.getJSONObject("params").put("energyGlobGuids", collectedEnergy);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    try {
                        params.put("params", null);
                    }
                    catch (JSONException e) {
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
                post.setHeader("Accept-Encoding", "gzip");
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

                            // decompress gzip if necessary
                            Header contentEncoding = entity.getContentEncoding();
                            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip"))
                                content = decompressGZIP(entity);
                            else
                                content = EntityUtils.toString(entity);

                            entity.consumeContent();
                        }
                    }

                    // handle request result
                    if (content != null) {
                        JSONObject json = new JSONObject(content);
                        RequestResult.handleRequest(json, result);
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

    private static String decompressGZIP(HttpEntity compressedEntity) throws IOException {
        final int bufferSize = 8192;
        InputStream input = compressedEntity.getContent();
        GZIPInputStream gzipStream = new GZIPInputStream(input, bufferSize);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = gzipStream.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gzipStream.close();
        input.close();
        return string.toString();
    }

    private long getCurrentTimestamp()
    {
        return (new Date()).getTime();
    }
}
