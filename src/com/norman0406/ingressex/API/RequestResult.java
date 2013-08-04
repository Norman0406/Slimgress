package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RequestResult
{
    private Handler mResultHandler;
    private Bundle mBundle;
    private Message mMessage;
    
    RequestResult(Handler handler)
    {
        mResultHandler = handler;
        mMessage = new Message();
    }
    
    private void handleException(String exception)
    {
        Log.e("RequestResult.Callback", exception);
        mBundle.putString("Exception", exception);
    };
    
    public void handleError(String error)
    {
        Log.e("RequestResult.Callback", error);
        mBundle.putString("Error", error);
    };
    
    public void handleGameBasket(GameBasket gameBasket)
    {
        // not implemented
    };
    
    public void handleResult(JSONObject result)
    {
        // not implemented
    };
    
    public void handleResult(JSONArray result)
    {
        // not implemented
    }
    
    public void handleResult(String result)
    {
        // not implemented
    }
    
    public Bundle getData()
    {
        return mBundle;
    }
    
    private void finished()
    {
        mMessage.setData(mBundle);
        mResultHandler.sendMessage(mMessage);
    }
    
    public static void handleRequest(JSONObject json, RequestResult result)
    {
        if (result == null)
            throw new RuntimeException("invalid result object");
        
        try {
            // handle exception string if available
            String excString = json.optString("exception"); 
            if (excString.length() > 0)
                result.handleException(excString);
            
            // handle error code if available
            String error = json.optString("error");
            if (error.length() > 0)
                result.handleError(error);
            else if (json.has("error"))
                Log.w("RequestResult", "request contains an unknown error type");
            
            // handle game basket if available
            JSONObject gameBasket = json.optJSONObject("gameBasket");
            if (gameBasket != null)
                result.handleGameBasket(new GameBasket(gameBasket));

            // handle result if available
            JSONObject resultObj = json.optJSONObject("result");
            JSONArray resultArr = json.optJSONArray("result");
            String resultStr = json.optString("result");
            if (resultObj != null)
                result.handleResult(resultObj);
            else if (resultArr != null)
                result.handleResult(resultArr);
            else if (resultStr != null)
                result.handleResult(resultStr);
            else if (json.has("result"))
                Log.w("RequestResult", "request contains an unknown result type");
            
            result.finished();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
