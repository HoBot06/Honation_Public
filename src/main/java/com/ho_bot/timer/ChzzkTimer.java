package com.ho_bot.timer;

import java.util.Map.Entry;

import org.bukkit.scheduler.BukkitRunnable;

import com.ho_bot.chzzk.ChzzkApi;
import com.ho_bot.chzzk.ChzzkWebSocket;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class ChzzkTimer extends BukkitRunnable{

	@Override
	public void run() {
		for(Entry<String, String> channel_entry : VarUtil.chzzk_channellist.entrySet()) {
			String Name = channel_entry.getKey();
			if(VarUtil.chzzkWebSocketList.containsKey(Name)) {
				if(!ChzzkApi.isOnline(VarUtil.chzzk_channellist.get(Name))) {
					ChzzkWebSocket web = VarUtil.chzzkWebSocketList.get(Name);
					web.close();
					VarUtil.chzzkWebSocketList.remove(Name);
					LogUtil.info("[치지직] " + Name+"님의 방송연결이 끊어졌습니다");
				}
			}
		}
	}

}
