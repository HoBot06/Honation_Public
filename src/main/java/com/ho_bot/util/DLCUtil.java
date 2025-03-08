package com.ho_bot.util;

import java.util.Map;

import org.bukkit.ChatColor;

import com.ho_bot.dlc.DLC_Donation;

public class DLCUtil {

	public static void addDlc(String key, DLC_Donation dlc) {
		VarUtil.dlclist.put(key, dlc);
		LogUtil.info("[DLC] " + ChatColor.GREEN + key + " 연결 완료");
	}
	
	public static Map<String, DLC_Donation> getDlclist() {
		return VarUtil.dlclist;
	}
	
}
