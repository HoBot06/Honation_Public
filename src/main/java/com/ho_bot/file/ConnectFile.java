package com.ho_bot.file;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.ho_bot.main.Honation;
import com.ho_bot.util.VarUtil;
import com.ho_bot.util.YmlUtil;

public class ConnectFile {
	
	private YmlUtil YU = new YmlUtil();
	
	public void reloadConnect() {
		File file = new File(Honation.inst.getDataFolder( ) + File.separator + "connect.yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		if(!file.exists()) {
			String s = "say test";
			
			yml.set("연결완료.use", false);
			yml.set("연결완료.command", s);
			VarUtil.isConnect_S = false;
			VarUtil.cmdConnect_S = s;
			
			yml.set("연결끊김.use", false);
			yml.set("연결끊김.command", s);
			VarUtil.isConnect_F = false;
			VarUtil.cmdConnect_F = s;
			
			yml.set("연결오류.use", false);
			yml.set("연결오류.command", s);
			VarUtil.isConnect_E = false;
			VarUtil.cmdConnect_E = s;
			
			YU.saveYml(file, yml);
			return;
		}
		
		VarUtil.isConnect_S = yml.getBoolean("연결완료.use");
		VarUtil.isConnect_F = yml.getBoolean("연결끊김.use");
		VarUtil.isConnect_E = yml.getBoolean("연결오류.use");
		
		VarUtil.cmdConnect_S = yml.getString("연결완료.command");
		VarUtil.cmdConnect_F = yml.getString("연결끊김.command");
		VarUtil.cmdConnect_E = yml.getString("연결오류.command");
	}

}
