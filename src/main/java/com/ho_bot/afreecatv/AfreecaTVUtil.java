package com.ho_bot.afreecatv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;

import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class AfreecaTVUtil {
	
	public List<AfreecaTVUser> getUesrlist() {
		List<AfreecaTVUser> userlist = new ArrayList<>();
		for(Entry<String, String> entry : VarUtil.afreeca_channellist.entrySet()) {
			String name = entry.getKey();
			String channel = entry.getValue();
			String nick = VarUtil.afreeca_Nicklist.get(name);
			UUID uuid = VarUtil.afreeca_Namelist.get(name);
			
			AfreecaTVUser uesr = new AfreecaTVUser(name, nick, channel, uuid);
			userlist.add(uesr);
		}
		return userlist;
	}
	
	public void connectAfreecaTV(AfreecaTVUser afreecaTVUser) throws InterruptedException {
		String afreecaTVId = afreecaTVUser.id;
		LogUtil.info(ChatColor.GRAY + "[AfreecaTVWebsocket][" + afreecaTVUser.name + "] 방송 연결시도..");
		try {
        	if(AfreecaTVApi.isOnline(afreecaTVId)) {
        		if(VarUtil.afreeca_liveInfo.get(afreecaTVId)==null) {
        			VarUtil.afreeca_liveInfo.put(afreecaTVId, AfreecaTVApi.getPlayerLive(afreecaTVId));
        		}
        		AfreecaTVLiveInfo liveInfo = VarUtil.afreeca_liveInfo.get(afreecaTVId);
                AfreecaTVWebSocket webSocket = new AfreecaTVWebSocket("wss://" + liveInfo.CHDOMAIN().toLowerCase() + ":" + liveInfo.CHPT() + "/Websocket/" + liveInfo.BJID(), liveInfo, afreecaTVUser);
                webSocket.run();
                VarUtil.afreecaTVWebSocketList.put(afreecaTVUser.name, webSocket);
        	}
        	else {
        		LogUtil.info(ChatColor.RED + "[AfreecaTVWebsocket][" + afreecaTVUser.name + "] 방송 온라인이 아닙니다");
        	}
        } catch (Exception e) {
        	e.printStackTrace();
            LogUtil.info(ChatColor.RED + "[AfreecaTVWebsocket][" + afreecaTVUser.name + "] 아프리카TV 채팅에 연결 중 오류가 발생했습니다.");
        }
    }
	
	public void disconnectAfreecaTV(Map<String, AfreecaTVWebSocket> afreecaTVWebSocketList) throws InterruptedException {
		for (Entry<String, AfreecaTVWebSocket> webSocket : afreecaTVWebSocketList.entrySet()) {
        	webSocket.getValue().close();
        }
        afreecaTVWebSocketList.clear();
    }
	
	public void disconnectAfreecaTV(String Name) throws InterruptedException {
		VarUtil.afreecaTVWebSocketList.get(Name).close();
		VarUtil.afreecaTVWebSocketList.remove(Name);
    }

}
