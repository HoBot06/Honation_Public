package com.ho_bot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ho_bot.main.Honation;

public class DonationUtil {
	
	public void donation(UUID uuid, String playername, String sender, String platform, int cash) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				List<String> cmd = new ArrayList<>();
				for(Entry<String, List<String>> entry : VarUtil.Cmdlist.entrySet()) {
					if(isCash(cash, entry.getKey())) {
						cmd = entry.getValue();
					}
				}
				if(cmd == null) return;
				for(String s_cmd : cmd) {
					s_cmd = CmdUtil.replaceCmd(s_cmd, uuid, playername, sender, platform, cash);
					
					String[] ho_cmd = s_cmd.split(":");
					if(ho_cmd[0].equalsIgnoreCase("honation")) {
						if(ho_cmd.length >= 2) {
							try {
								if(ho_cmd[1].equalsIgnoreCase("kill")) {
									final Player player = Bukkit.getPlayer(uuid);
									player.setHealth(0);
									continue;
								}
								if(ho_cmd[1].equalsIgnoreCase("alltitle")) {
									String title = "";
									String subTitle = "";
									int fadeIn = 0;
									int stay = 0;
									int fadeOut = 0;
									for(int i = 2; i < ho_cmd.length; i++) {
										if(i==2) {
											title = ho_cmd[i].replace('&', ChatColor.COLOR_CHAR);
										}
										if(i==3) {
											subTitle = ho_cmd[i].replace('&', ChatColor.COLOR_CHAR);
										}
										if(i==4) {
											fadeIn = Integer.parseInt(ho_cmd[i]);
										}
										if(i==5) {
											stay = Integer.parseInt(ho_cmd[i]);
										}
										if(i==6) {
											fadeOut = Integer.parseInt(ho_cmd[i]);
										}
									}
									for(Player allp : Bukkit.getOnlinePlayers()) {
										allp.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
									}
									continue;
								}
								if(ho_cmd[1].equalsIgnoreCase("title")) {
									Player player = Bukkit.getPlayer(uuid);
									String title = "";
									String subTitle = "";
									int fadeIn = 0;
									int stay = 0;
									int fadeOut = 0;
									for(int i = 2; i < ho_cmd.length; i++) {
										if(i==2) {
											title = ho_cmd[i].replace('&', ChatColor.COLOR_CHAR);
										}
										if(i==3) {
											subTitle = ho_cmd[i].replace('&', ChatColor.COLOR_CHAR);
										}
										if(i==4) {
											fadeIn = Integer.parseInt(ho_cmd[i]);
										}
										if(i==5) {
											stay = Integer.parseInt(ho_cmd[i]);
										}
										if(i==6) {
											fadeOut = Integer.parseInt(ho_cmd[i]);
										}
									}
									player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
									continue;
								}
								if(ho_cmd[1].equalsIgnoreCase("teleport")) {
									Player player = Bukkit.getPlayer(uuid);
									String world = ho_cmd[2];
									double x = Double.parseDouble(ho_cmd[3]);
									double y = Double.parseDouble(ho_cmd[4]);
									double z = Double.parseDouble(ho_cmd[5]);
									Location loc = new Location(Bukkit.getWorld(world), x, y, z);
									player.teleport(loc);
									continue;
								}
								if(VarUtil.dlclist.containsKey(ho_cmd[1])) {
									String dlc_cmd = s_cmd.replace("honation:"+ho_cmd[1], "");
									VarUtil.dlclist.get(ho_cmd[1]).active(uuid, playername, sender, platform, cash, dlc_cmd);
									continue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					else {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s_cmd);
					}
				}
			}
		}.runTask(Honation.inst);
	}

	public void donation(UUID uuid, String playername, int cash) {
		donation(uuid, playername, "[테스트]", "[테스트플랫폼]", cash);
	}
	
	private boolean isCash(int cash, String area) {
		String[] s_area = area.split("~");
		if(s_area.length==1) {
			if(cash==Integer.parseInt(area)) {
				return true;
			}
		}
		else {
			if(cash>=Integer.parseInt(s_area[0])&&cash<=Integer.parseInt(s_area[1])) {
				return true;
			}
		}
		return false;
	}
}
