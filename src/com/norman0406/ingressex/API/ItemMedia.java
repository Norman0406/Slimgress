package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemMedia extends Item {
	
	private String mediaImageUrl;
	private String mediaUrl;
	private String mediaDescription;
	private boolean mediaHasBeenViewed;
	private String mediaReleaseDate;
	
	public ItemMedia(String guid, String timestamp) {
		super(guid, timestamp, Item.ItemType.Media);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject imageByUrl = json.getJSONObject("imageByUrl");
		JSONObject storyItem = json.getJSONObject("storyItem");
		
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
