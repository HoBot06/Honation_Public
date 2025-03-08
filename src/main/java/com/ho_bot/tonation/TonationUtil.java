package com.ho_bot.tonation;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class TonationUtil {
	
	public void connectTonation(TonationUser tonationUser) throws InterruptedException {
		String payLoad = VarUtil.tonation_payload.get(tonationUser.id);
		LogUtil.info(ChatColor.GRAY + "[TonationWebsocket][" + tonationUser.nick + "] 연결시도..");
		try {
            TonationWebSocket webSocket = new TonationWebSocket("wss://ws.toon.at/", payLoad, tonationUser);
            webSocket.run();
            VarUtil.tonationWebSocketList.put(tonationUser.name, webSocket);
        } catch (Exception e) {
        	e.printStackTrace();
        	LogUtil.info(ChatColor.RED + "[TonationWebsocket][" + tonationUser.nick + "] 투네이션 채팅에 연결 중 오류가 발생했습니다.");
        }
    }
	
	public void disconnectTonation(Map<String, TonationWebSocket> tonationWebSocketList) throws InterruptedException {
		for (Entry<String, TonationWebSocket> webSocket : tonationWebSocketList.entrySet()) {
        	webSocket.getValue().close();
        }
        tonationWebSocketList.clear();
    }
	
	public void disconnectTonation(String Name) throws InterruptedException {
		VarUtil.tonationWebSocketList.get(Name).close();
		VarUtil.tonationWebSocketList.remove(Name);
    }

}
