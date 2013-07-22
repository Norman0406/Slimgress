package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemMedia extends Item
{	
	private String mMediaImageUrl;
	private String mMediaUrl;
	private String mMediaDescription;
	private boolean mMediaHasBeenViewed;
	private String mMediaReleaseDate;
	
	public ItemMedia(JSONArray json) throws JSONException
	{
		super(ItemType.Media, json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject imageByUrl = item.getJSONObject("imageByUrl");
		JSONObject storyItem = item.getJSONObject("storyItem");
		
		mMediaImageUrl = imageByUrl.getString("imageUrl");
		mMediaUrl = storyItem.getString("primaryUrl");
		mMediaDescription = storyItem.getString("shortDescription");
		mMediaHasBeenViewed = storyItem.getBoolean("hasBeenViewed");
		mMediaReleaseDate = storyItem.getString("releaseDate");
	}

	public String getMediaImageUrl()
	{
		return mMediaImageUrl;
	}

	public String getMediaUrl()
	{
		return mMediaUrl;
	}

	public String getMediaDescription()
	{
		return mMediaDescription;
	}

	public boolean isMediaHasBeenViewed()
	{
		return mMediaHasBeenViewed;
	}

	public String getMediaReleaseDate()
	{
		return mMediaReleaseDate;
	}
}
