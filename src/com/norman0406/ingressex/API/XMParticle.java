package com.norman0406.ingressex.API;

public class XMParticle
{	
	private final String mGuid;
	private final String mEnergyTimestamp;
	private final long mCellId;
	private final int mAmount;
	private final Utils.LocationE6 mCellLocation;

	public XMParticle(String guid, String timestamp)
	{
		mGuid = guid; 
		mEnergyTimestamp = timestamp;
		
		mCellId = Long.parseLong(guid.substring(0, 16), 16);
		mCellLocation = new Utils.LocationE6(mCellId);
		
		// NOTE: not sure if correct
		String amountStr = guid.substring(guid.length() - 4, guid.length() - 2);		
		mAmount = Integer.parseInt(amountStr, 16);
	}
	
	public String getGuid()
	{
		return mGuid;
	}
	
	public String getEnergyTimestamp()
	{
		return mEnergyTimestamp;
	}

	public long getCellId()
	{
		return mCellId;
	}

	public int getAmount()
	{
		return mAmount;
	}

	public Utils.LocationE6 getCellLocation()
	{
		return mCellLocation;
	}
}
