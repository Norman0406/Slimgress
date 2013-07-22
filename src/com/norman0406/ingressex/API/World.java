package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class World
{
	private List<GameEntity> mGameEntities = new LinkedList<GameEntity>();
	private List<XMParticle> mXMParticles = new LinkedList<XMParticle>();
	
	public void processGameBasket(GameBasket basket)
	{
		mGameEntities.addAll(basket.getGameEntities());
		mXMParticles.addAll(basket.getEnergyGlobGuids());
	}
}
