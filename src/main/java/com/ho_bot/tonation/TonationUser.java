package com.ho_bot.tonation;

import java.util.UUID;

public class TonationUser {
	
	public final String name;
	public final String id;
	public final String nick;
	public final UUID uuid;
	
	public TonationUser(String name, String id, String nick, UUID uuid) {
		this.name = name;
		this.id = id;
		this.nick = nick;
		this.uuid = uuid;
	}

}
