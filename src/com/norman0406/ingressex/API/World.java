package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

public class World
{
	private List<GameEntity> gameEntities = new LinkedList<GameEntity>();
	private List<XMParticle> xmParticles = new LinkedList<XMParticle>();
	
	public void processGameBasket(GameBasket basket)
	{
		gameEntities.addAll(basket.getGameEntities());
		xmParticles.addAll(basket.getEnergyGlobGuids());
	}
}
