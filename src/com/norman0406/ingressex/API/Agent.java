package com.norman0406.ingressex.API;

// TODO: let Agent deserialize it's values automatically from handshake and gameBasket data

public class Agent {
	public enum Team
	{
		RESISTANCE,
		ENLIGHTENED		
	}
	
	// can never change during the game
	private final String playerId;
	private final String nickname;
	private final Team team;
	
	// dynamic information
	private int ap;
	private int energy;
	
	// other information
	private boolean allowNicknameEdit;
	private boolean allowFactionChoice;
	private boolean canPlay;
	
	public Agent(final String playerId, final String nickname, final Team team)
	{
		this.playerId = playerId;
		this.nickname = nickname;
		this.team = team;
	}

	public int getAp() {
		return ap;
	}

	public void setAp(int ap) {
		this.ap = ap;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public boolean isAllowNicknameEdit() {
		return allowNicknameEdit;
	}

	public void setAllowNicknameEdit(boolean allowNicknameEdit) {
		this.allowNicknameEdit = allowNicknameEdit;
	}

	public boolean isAllowFactionChoice() {
		return allowFactionChoice;
	}

	public void setAllowFactionChoice(boolean allowFactionChoice) {
		this.allowFactionChoice = allowFactionChoice;
	}

	public boolean isCanPlay() {
		return canPlay;
	}

	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}
	
	public String getPlayerId() {
		return playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public Team getTeam() {
		return team;
	}
}
