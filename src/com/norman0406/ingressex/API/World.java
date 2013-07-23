package com.norman0406.ingressex.API;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class World
{
	private Map<String, GameEntity> mGameEntities = new HashMap<String, GameEntity>();
	private Map<String, XMParticle> mXMParticles = new HashMap<String, XMParticle>();
		
	public void processGameBasket(GameBasket basket)
	{
		// only add non-existing game entities
		List<GameEntity> entities = basket.getGameEntities();
		for (GameEntity entity : entities) {
			if (!mGameEntities.containsKey(entity.getEntityGuid()))
				mGameEntities.put(entity.getEntityGuid(), entity);
		}

		// only add non-existing xm particles
		List<XMParticle> xmParticles = basket.getEnergyGlobGuids();
		for (XMParticle particle : xmParticles) {
			if (!mXMParticles.containsKey(particle.getGuid()))
				mXMParticles.put(particle.getGuid(), particle);
		}
	}
	
	public final Map<String, GameEntity> getGameEntities()
	{
		return mGameEntities;
	}
	
	public final Map<String, XMParticle> getXMParticles()
	{
		return mXMParticles;
	}
}
