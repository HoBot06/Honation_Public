package com.ho_bot.chzzk;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class ChzzkUtil {
	
	public void connectChzzk(ChzzkUser chzzkUser) throws InterruptedException {
		String chzzkId = chzzkUser.id;
		LogUtil.info(ChatColor.GRAY + "[ChzzkWebsocket][" + chzzkUser.nick + "] 방송 연결시도..");
		try {
            if(ChzzkApi.isOnline(chzzkId)) {
            	if(VarUtil.chzzk_chatChannelId.get(chzzkId)==null) {
        			VarUtil.chzzk_chatChannelId.put(chzzkId, ChzzkApi.getChatChannelId(chzzkId));
        			VarUtil.chzzk_accessToken.put(chzzkId, ChzzkApi.getAccessToken(VarUtil.chzzk_chatChannelId.get(chzzkId)).split(";")[0]);
        		}
            	String chatChannelId = VarUtil.chzzk_chatChannelId.get(chzzkId);
                String accessToken = VarUtil.chzzk_accessToken.get(chzzkId);
                
                ChzzkWebSocket webSocket = new ChzzkWebSocket("wss://kr-ss1.chat.naver.com/chat", chatChannelId, accessToken, chzzkUser);
                webSocket.run();
                VarUtil.chzzkWebSocketList.put(chzzkUser.name, webSocket);
            }
            else {
            	LogUtil.info(ChatColor.RED + "[ChzzkWebsocket][" + chzzkUser.nick + "] 방송 온라인이 아닙니다");
            }
        } catch (Exception e) {
        	LogUtil.info(ChatColor.RED + "[ChzzkWebsocket][" + chzzkUser.nick + "] 치지직 채팅에 연결 중 오류가 발생했습니다.");
        }
    }
	
	public void disconnectChzzk(Map<String, ChzzkWebSocket> chzzkWebSocketList) throws InterruptedException {
		for (Entry<String, ChzzkWebSocket> webSocket : chzzkWebSocketList.entrySet()) {
        	webSocket.getValue().close();
        }
        chzzkWebSocketList.clear();
    }
	
	public void disconnectChzzk(String Name) throws InterruptedException {
		VarUtil.chzzkWebSocketList.get(Name).close();
		VarUtil.chzzkWebSocketList.remove(Name);
    }

}
