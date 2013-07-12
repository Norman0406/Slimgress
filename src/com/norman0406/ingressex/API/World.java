package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class World {
	private List<GameEntity> gameEntities;
	
	public World() {
		gameEntities = new LinkedList<GameEntity>();
	}

	public void addEntity(GameEntity entity) {
		gameEntities.add(entity);
	}
	
	public List<GameEntity> getEntities() {
		return gameEntities;
	}
	
	public GameEntity getEntity(String guid) {
		for (GameEntity it : gameEntities) {
			if (it.getEntityGuid() == guid)
				return it;
		}
		return null;
	}
	
	public boolean removeEntity(String guid) {
		GameEntity entity = getEntity(guid);
		if (entity != null)
			return gameEntities.remove(entity);
		return false;
	}
}
