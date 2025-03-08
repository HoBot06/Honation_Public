package com.ho_bot.file;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Charsets;
import com.ho_bot.afreecatv.AfreecaTVApi;
import com.ho_bot.afreecatv.AfreecaTVLiveInfo;
import com.ho_bot.chzzk.ChzzkApi;
import com.ho_bot.main.Honation;
import com.ho_bot.tonation.TonationApi;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class ConfigFile {
	
	public void SaveConfig() {
		Honation.inst.saveConfig();
	}
	
	public void reloadConfigALL() {
		reloadConfigStream();
		reloadConfigDonation();
	}
	
	public void reloadConfigDonation() {
		Honation.inst.reloadConfig();
		try {
			File file = new File(Honation.inst.getDataFolder( ) + File.separator + "config.yml");
			FileReader f = new FileReader(file, Charsets.UTF_8);
			Map<String, Object> config_map = new Yaml().load(f);
			
			if(config_map.containsKey("후원")) {
				Map<String, Object> donation_file = (Map<String, Object>) config_map.get("후원");
				VarUtil.Cmdlist.clear();
				for(Entry<String, Object> entry : donation_file.entrySet()) {
					VarUtil.Cmdlist.put(entry.getKey(), (List<String>) entry.getValue());
				}
			}
			LogUtil.info(ChatColor.GREEN+"후원 데이터 저장완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveConfig();
	}
	
	public void reloadConfigStream() {
		Honation.inst.reloadConfig();
		try {
			File file = new File(Honation.inst.getDataFolder( ) + File.separator + "config.yml");
			FileReader f = new FileReader(file, Charsets.UTF_8);
			Map<String, Object> config_map = new Yaml().load(f);
			
			//치지직
			Map<String, Object> chzzk_file = (Map<String, Object>) config_map.get("치지직");
			VarUtil.chzzk_channellist.clear();
			VarUtil.chzzk_Namelist.clear();
			VarUtil.chzzk_Uuidlist.clear();
			if(chzzk_file!=null) {
				for(Entry<String, Object> entry : chzzk_file.entrySet()) {
					Map<String, Object> playerMap = (Map<String, Object>) entry.getValue();
					VarUtil.chzzk_channellist.put(entry.getKey(), (String) playerMap.get("아이디"));
					VarUtil.chzzk_Namelist.put(entry.getKey(), UUID.fromString((String) playerMap.get("UUID")));
					VarUtil.chzzk_Nicklist.put(entry.getKey(), (String) playerMap.get("닉네임"));
					VarUtil.chzzk_Uuidlist.put(UUID.fromString((String) playerMap.get("UUID")), entry.getKey());
					
					String chatChannelId = ChzzkApi.getChatChannelId((String) playerMap.get("아이디"));
					VarUtil.chzzk_chatChannelId.put((String) playerMap.get("아이디"), chatChannelId);
					String token = ChzzkApi.getAccessToken(chatChannelId);
					if(token != null) {
						VarUtil.chzzk_accessToken.put((String) playerMap.get("아이디"), token.split(";")[0]);
					}
				}
			}
			
			//아프리카
			Map<String, Object> afreeca_file = (Map<String, Object>) config_map.get("아프리카");
			VarUtil.afreeca_channellist.clear();
			VarUtil.afreeca_Namelist.clear();
			VarUtil.afreeca_Uuidlist.clear();
			if(afreeca_file != null) {
				for(Entry<String, Object> entry : afreeca_file.entrySet()) {
					Map<String, Object> playerMap = (Map<String, Object>) entry.getValue();
					VarUtil.afreeca_channellist.put(entry.getKey(), (String) playerMap.get("아이디"));
					VarUtil.afreeca_Namelist.put(entry.getKey(), UUID.fromString((String) playerMap.get("UUID")));
					VarUtil.afreeca_Nicklist.put(entry.getKey(), (String) playerMap.get("닉네임"));
					VarUtil.afreeca_Uuidlist.put(UUID.fromString((String) playerMap.get("UUID")), entry.getKey());
					
					AfreecaTVLiveInfo liveInfo = AfreecaTVApi.getPlayerLive((String) playerMap.get("아이디"));
					VarUtil.afreeca_liveInfo.put((String) playerMap.get("아이디"), liveInfo);
				}
			}
			
			//투네이션
			Map<String, Object> tonation_file = (Map<String, Object>) config_map.get("투네이션");
			VarUtil.tonation_channellist.clear();
			VarUtil.tonation_Namelist.clear();
			VarUtil.tonation_Uuidlist.clear();
			if(tonation_file != null) {
				for(Entry<String, Object> entry : tonation_file.entrySet()) {
					Map<String, Object> playerMap = (Map<String, Object>) entry.getValue();
					VarUtil.tonation_channellist.put(entry.getKey(), (String) playerMap.get("아이디"));
					VarUtil.tonation_Namelist.put(entry.getKey(), UUID.fromString((String) playerMap.get("UUID")));
					VarUtil.tonation_Nicklist.put(entry.getKey(), (String) playerMap.get("닉네임"));
					VarUtil.tonation_Uuidlist.put(UUID.fromString((String) playerMap.get("UUID")), entry.getKey());
					
					String payLoad = TonationApi.getPayLoad((String) playerMap.get("아이디"));
					VarUtil.tonation_payload.put((String) playerMap.get("아이디"), payLoad);
				}
			}
			LogUtil.info(ChatColor.GREEN+"방송 데이터 저장완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveConfig();
	}
	
	public void addChzzkConfig(String Name, String Nick, String UUID, String channel) {
		Honation.inst.getConfig().set("치지직."+Name+".닉네임", Nick);
		Honation.inst.getConfig().set("치지직."+Name+".UUID", UUID);
		Honation.inst.getConfig().set("치지직."+Name+".아이디", channel);
		
		SaveConfig();
	}
	
	public void addAfreecaConfig(String Name, String Nick, String UUID, String channel) {
		Honation.inst.getConfig().set("아프리카."+Name+".닉네임", Nick);
		Honation.inst.getConfig().set("아프리카."+Name+".UUID", UUID);
		Honation.inst.getConfig().set("아프리카."+Name+".아이디", channel);
		
		SaveConfig();
	}
	
	public void addTonationConfig(String Name, String Nick, String UUID, String channel) {
		Honation.inst.getConfig().set("투네이션."+Name+".닉네임", Nick);
		Honation.inst.getConfig().set("투네이션."+Name+".UUID", UUID);
		Honation.inst.getConfig().set("투네이션."+Name+".아이디", channel);
		
		SaveConfig();
	}
	
	public void addDonationConfig(long cash, String cmd) {
		Honation.inst.getConfig().set("후원."+cash, cmd);
		
		SaveConfig();
	}
}
