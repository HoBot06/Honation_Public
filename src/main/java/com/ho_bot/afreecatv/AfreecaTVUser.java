package com.ho_bot.afreecatv;

import java.util.UUID;

public class AfreecaTVUser {
	
	public final String name;
	public final String id;
	public final String nick;
	public final UUID uuid;
	
	public AfreecaTVUser(String name, String id, String nick, UUID uuid) {
		this.name = name;
		this.id = id;
		this.nick = nick;
		this.uuid = uuid;
	}

}
