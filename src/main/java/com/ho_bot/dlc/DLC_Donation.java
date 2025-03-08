package com.ho_bot.dlc;

import java.util.UUID;

public abstract class DLC_Donation {
	
	final String key;
	
	
	public DLC_Donation(String key) {
		this.key = key;
	}
	
	
	/**UUID uuid, String playername, String sender, String platform, int cash, String cmd*/
	public abstract void active(UUID uuid, String playername, String sender, String platform, int cash, String cmd);

}
