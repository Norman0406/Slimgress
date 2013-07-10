package com.norman0406.ingressex.API;

enum Team
{
	RESISTANCE,
	ENLIGHTENED
}

public class Agent {
	public String nickname;
	public Team team;
	public int ap;
	public int energy;
	
	public boolean allowNicknameEdit;
	public boolean allowFactionChoice;
	public String xsrfToken;
	public boolean canPlay;
	
	public Agent()
	{
	}
}
