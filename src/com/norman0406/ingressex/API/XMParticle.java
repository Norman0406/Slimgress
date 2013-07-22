package com.norman0406.ingressex.API;

public class XMParticle
{	
	private final String guid;
	private final String energyTimestamp;
	private final long cellId;
	private final int amount;
	private final Utils.LocationE6 cellLocation;

	public XMParticle(String guid, String timestamp)
	{
		this.guid = guid; 
		this.energyTimestamp = timestamp;
		
		cellId = Long.parseLong(guid.substring(0, 16), 16);
		cellLocation = new Utils.LocationE6(cellId);
		
		// NOTE: not sure if correct
		String amountStr = guid.substring(guid.length() - 4, guid.length() - 2);		
		amount = Integer.parseInt(amountStr, 16);
	}
	
	public String getGuid()
	{
		return guid;
	}
	
	public String getEnergyTimestamp()
	{
		return energyTimestamp;
	}

	public long getCellId()
	{
		return cellId;
	}

	public int getAmount()
	{
		return amount;
	}

	public Utils.LocationE6 getCellLocation()
	{
		return cellLocation;
	}
}
