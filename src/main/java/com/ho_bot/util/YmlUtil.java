package com.ho_bot.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class YmlUtil {
	
	public void saveYml(File file, YamlConfiguration yml) {
		try {
			yml.save(file);
		} catch (IOException e) {
		}
	}

}
