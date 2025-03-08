package com.ho_bot.timer;

import java.util.Map.Entry;

import org.bukkit.scheduler.BukkitRunnable;

import com.ho_bot.afreecatv.AfreecaTVApi;
import com.ho_bot.afreecatv.AfreecaTVWebSocket;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class AfreecaTvTimer extends BukkitRunnable{

	@Override
	public void run() {
		for(Entry<String, String> channel_entry : VarUtil.afreeca_channellist.entrySet()) {
			String Name = channel_entry.getKey();
			if(VarUtil.afreecaTVWebSocketList.containsKey(Name)) {
				if(!AfreecaTVApi.isOnline(VarUtil.afreeca_channellist.get(Name))) {
					AfreecaTVWebSocket web = VarUtil.afreecaTVWebSocketList.get(Name);
					web.close();
					VarUtil.afreecaTVWebSocketList.remove(Name);
					LogUtil.info("[아프리카] " + Name+"님의 방송연결이 끊어졌습니다");
				}
			}
		}
	}

}
