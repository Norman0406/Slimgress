package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemMedia extends Item {
	
	private String mediaImageUrl;
	private String mediaUrl;
	private String mediaDescription;
	private boolean mediaHasBeenViewed;
	private String mediaReleaseDate;
	
	public ItemMedia(JSONArray json) throws JSONException {
		super(ItemType.Media, json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject imageByUrl = item.getJSONObject("imageByUrl");
		JSONObject storyItem = item.getJSONObject("storyItem");
		
		mediaImageUrl = imageByUrl.getString("imageUrl");
		mediaUrl = storyItem.getString("primaryUrl");
		mediaDescription = storyItem.getString("shortDescription");
		mediaHasBeenViewed = storyItem.getBoolean("hasBeenViewed");
		mediaReleaseDate = storyItem.getString("releaseDate");
	}

	public String getMediaImageUrl() {
		return mediaImageUrl;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public String getMediaDescription() {
		return mediaDescription;
	}

	public boolean isMediaHasBeenViewed() {
		return mediaHasBeenViewed;
	}

	public String getMediaReleaseDate() {
		return mediaReleaseDate;
	}
}
