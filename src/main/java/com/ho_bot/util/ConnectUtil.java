package com.ho_bot.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.ho_bot.main.Honation;

public class ConnectUtil {
	
	public void runConnectS(UUID uuid, String playername, String platform) {
		if(!VarUtil.isConnect_S) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				String cmd = VarUtil.cmdConnect_S;
				cmd = CmdUtil.replaceCmd(cmd, uuid, playername, "", platform, 0);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
		}.runTask(Honation.inst);
	}
	
	public void runConnectF(UUID uuid, String playername, String platform) {
		if(!VarUtil.isConnect_F) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				String cmd = VarUtil.cmdConnect_F;
				cmd = CmdUtil.replaceCmd(cmd, uuid, playername, "", platform, 0);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
		}.runTask(Honation.inst);
	}
	
	public void runConnectE(UUID uuid, String playername, String platform) {
		if(!VarUtil.isConnect_E) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				String cmd = VarUtil.cmdConnect_E;
				cmd = CmdUtil.replaceCmd(cmd, uuid, playername, "", platform, 0);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
		}.runTask(Honation.inst);
	}

}
