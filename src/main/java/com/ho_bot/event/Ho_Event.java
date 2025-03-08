package com.ho_bot.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.ho_bot.afreecatv.AfreecaTVApi;
import com.ho_bot.afreecatv.AfreecaTVUser;
import com.ho_bot.afreecatv.AfreecaTVUtil;
import com.ho_bot.chzzk.ChzzkApi;
import com.ho_bot.chzzk.ChzzkUser;
import com.ho_bot.chzzk.ChzzkUtil;
import com.ho_bot.main.Honation;
import com.ho_bot.tonation.TonationUtil;
import com.ho_bot.util.VarUtil;

public class Ho_Event implements Listener{
	
	public static Honation plugin;

	public static void setPlugin(Honation MainPlugin)
    {
        plugin = MainPlugin;
    }
	
	public AfreecaTVUtil afreecaTVutil = new AfreecaTVUtil();
	public ChzzkUtil chzzkutil = new ChzzkUtil();
	public TonationUtil tonationutil = new TonationUtil();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		//치지직
		if(VarUtil.chzzk_Uuidlist.containsKey(player.getUniqueId())) {
			String Name = VarUtil.chzzk_Uuidlist.get(player.getUniqueId());
			if(!VarUtil.chzzkWebSocketList.containsKey(Name)) {
				String channel = VarUtil.chzzk_channellist.get(Name);
				UUID uuid = VarUtil.chzzk_Namelist.get(Name);
				String nick = VarUtil.chzzk_Nicklist.get(Name);
				
				ChzzkUser uesr = new ChzzkUser(Name, channel, nick, uuid);
				try {
					chzzkutil.connectChzzk(uesr);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		//아프리카
		if(VarUtil.afreeca_Uuidlist.containsKey(player.getUniqueId())) {
			String Name = VarUtil.afreeca_Uuidlist.get(player.getUniqueId());
			if(!VarUtil.afreecaTVWebSocketList.containsKey(Name)) {
				String channel = VarUtil.afreeca_channellist.get(Name);
				UUID uuid = VarUtil.afreeca_Namelist.get(Name);
				String nick = VarUtil.afreeca_Nicklist.get(Name);
				
				AfreecaTVUser uesr = new AfreecaTVUser(Name, channel, nick, uuid);
				try {
					afreecaTVutil.connectAfreecaTV(uesr);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(VarUtil.afreeca_Uuidlist.containsKey(player.getUniqueId())) {
			String Name = VarUtil.afreeca_Uuidlist.get(player.getUniqueId());
			if(VarUtil.afreecaTVWebSocketList.containsKey(Name)) {
				String channel = VarUtil.afreeca_channellist.get(Name);
				if(!AfreecaTVApi.isOnline(channel)) {
					VarUtil.afreecaTVWebSocketList.get(Name).close();
					VarUtil.afreecaTVWebSocketList.remove(Name);
				}
			}
		}
		if(VarUtil.chzzk_Uuidlist.containsKey(player.getUniqueId())) {
			String Name = VarUtil.chzzk_Uuidlist.get(player.getUniqueId());
			if(VarUtil.chzzkWebSocketList.containsKey(Name)) {
				String channel = VarUtil.chzzk_channellist.get(Name);
				if(!ChzzkApi.isOnline(channel)) {
					VarUtil.chzzkWebSocketList.get(Name).close();
					VarUtil.chzzkWebSocketList.remove(Name);
				}
			}
		}
	}

}
