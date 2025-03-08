package com.ho_bot.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.ho_bot.main.Honation;

public class LogUtil {
	
	private static final String prefix = ChatColor.AQUA + "[Honation] ";

    public static void info(String msg) {
        Bukkit.getConsoleSender().sendMessage(prefix + msg);
    }

    public static void say(String msg) {
        String command = "say " + msg;
        Bukkit.getScheduler()
                .callSyncMethod(Honation.inst, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

}
