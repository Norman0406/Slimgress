package com.norman0406.ingressex.API;

enum AccessLevel
{
	AL_NONE,
	AL_1,
	AL_2,
	AL_3,
	AL_4,
	AL_5,
	AL_6,
	AL_7,
	AL_8
};

enum ItemRarity
{	// are there more to come?
	IR_NONE,
	IR_COMMON,
	IR_RARE,
	IR_VERYRARE
}

public interface Item {
	public AccessLevel accessLevel = AccessLevel.AL_NONE;
	public ItemRarity rarity = ItemRarity.IR_NONE;
}
