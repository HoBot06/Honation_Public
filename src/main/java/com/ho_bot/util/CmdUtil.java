package com.ho_bot.util;

import java.util.UUID;

import org.bukkit.Bukkit;

public class CmdUtil {
	
	public static String replaceCmd(String cmd, UUID uuid, String playername, String sender, String platform, int cash) {
		String s_cmd = cmd;
		if(Bukkit.getPlayer(uuid)!=null && Bukkit.getPlayer(uuid).getName()!=null) {
			s_cmd=s_cmd.replace("%player%", Bukkit.getPlayer(uuid).getName());
		}
		else {
			s_cmd=s_cmd.replace("%player%",playername);
		}
		if(Bukkit.getPlayer(uuid)!=null && Bukkit.getPlayer(uuid).getCustomName()!=null) {
			s_cmd=s_cmd.replace("%custom%", Bukkit.getPlayer(uuid).getCustomName());
		}
		else {
			s_cmd=s_cmd.replace("%custom%",playername);
		}
		if(sender != null) {
			s_cmd=s_cmd.replace("%sender%", sender);
		}
		else {
			s_cmd=s_cmd.replace("%sender%", "[익명]");
		}
		s_cmd=s_cmd.replace("%amount%", cash+"");
		s_cmd=s_cmd.replace("%uuid%", uuid.toString());
		s_cmd=s_cmd.replace("%platform%", platform);
		return s_cmd;
	}

}
