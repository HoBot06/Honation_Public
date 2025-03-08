package com.ho_bot.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.ho_bot.main.Honation;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;
import com.ho_bot.util.YmlUtil;

public class ManagerFile {
	
	private YmlUtil YU = new YmlUtil();
	
	public void reloadManager() {
		VarUtil.afreecaManagerList = getAfreecaManager();
		VarUtil.ChzzkManagerList = getChzzkManager();
		
		LogUtil.info(ChatColor.GREEN+"Manager 데이터 저장완료");
	}
	
	public List<String> getAfreecaManager() {
		File file = new File(Honation.inst.getDataFolder( ) + File.separator + "manager.yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		if(yml.contains("afreeca")) {
			if(yml.isList("afreeca")) return yml.getStringList("afreeca");
		}
		return null;
	}
	
	public List<String> getChzzkManager() {
		File file = new File(Honation.inst.getDataFolder( ) + File.separator + "manager.yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		if(yml.contains("chzzk")) {
			if(yml.isList("chzzk")) return yml.getStringList("chzzk");
		}
		return null;
	}
	
	public void setAfreecaManager(List<String> list) {
		File file = new File(Honation.inst.getDataFolder( ) + File.separator + "manager.yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		yml.set("afreeca", list);
		YU.saveYml(file, yml);
	}
	
	public void setChzzkManager(List<String> list) {
		File file = new File(Honation.inst.getDataFolder( ) + File.separator + "manager.yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		yml.set("chzzk", list);
		YU.saveYml(file, yml);
	}
	
	public void addAfreecaManager(String id) {
		List<String> list = new ArrayList<>();
		if(getAfreecaManager() != null) {
			list = getAfreecaManager();
		}
		list.add(id);
		setAfreecaManager(list);
	}
	
	public void addChzzkManager(String id) {
		List<String> list = new ArrayList<>();
		if(getChzzkManager() != null) {
			list = getChzzkManager();
		}
		list.add(id);
		setChzzkManager(list);
	}

}
