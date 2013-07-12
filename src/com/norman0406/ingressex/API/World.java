package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class World {
	private List<GameEntity> gameEntities;
	private List<XMParticle> xmParticles;
	
	public World() {
		gameEntities = new LinkedList<GameEntity>();
		xmParticles = new LinkedList<XMParticle>();
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
	
	public void addParticle(XMParticle particle) {
		xmParticles.add(particle);
	}
	
	public List<XMParticle> getParticles() {
		return xmParticles;
	}
	
	public XMParticle getParticle(String guid) {
		for (XMParticle it : xmParticles) {
			if (it.getGuid() == guid)
				return it;
		}
		return null;
	}
	
	public boolean removeParticle(String guid) {
		XMParticle particle = getParticle(guid);
		if (particle != null)
			return xmParticles.remove(particle);
		return false;
	}
}
