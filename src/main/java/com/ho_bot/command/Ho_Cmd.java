package com.ho_bot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ho_bot.afreecatv.AfreecaTVUser;
import com.ho_bot.afreecatv.AfreecaTVUtil;
import com.ho_bot.chzzk.ChzzkUser;
import com.ho_bot.chzzk.ChzzkUtil;
import com.ho_bot.file.ConfigFile;
import com.ho_bot.file.ConnectFile;
import com.ho_bot.file.ManagerFile;
import com.ho_bot.main.Honation;
import com.ho_bot.tonation.TonationUser;
import com.ho_bot.tonation.TonationUtil;
import com.ho_bot.util.DonationUtil;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class Ho_Cmd implements TabExecutor{
	
	private ConfigFile configF = new ConfigFile();
	private ManagerFile managerF = new ManagerFile();
	private ConnectFile connectF = new ConnectFile();
	private AfreecaTVUtil afreecaTVutil = new AfreecaTVUtil();
	private ChzzkUtil chzzkutil = new ChzzkUtil();
	private DonationUtil donationU = new DonationUtil();
	private TonationUtil tonationU = new TonationUtil();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("honation")) {
			if(sender instanceof Player p) {
				if(!sender.isOp()) {
					notOp(p);
					return false;
				}
			}
			if(args.length < 1) {
				HelpUsage(sender);
				return false;
			}
			if(args[0].equalsIgnoreCase("add")) {
				AddCmd(sender, args);
				return true;
			}
			if(args[0].equalsIgnoreCase("connect")) {
				ConnectCmd(sender, args);
				return true;
			}
			if(args[0].equalsIgnoreCase("allconnect")) {
				AllConnectCmd(sender, args);
				return true;
			}
			if(args[0].equalsIgnoreCase("disconnect")) {
				DisconnectCmd(sender, args);
				return true;
			}
			if(args[0].equalsIgnoreCase("alldisconnect")) {
				AllDisconnectCmd(sender, args);
				return true;
			}
			if(args[0].equalsIgnoreCase("give")) {
				GiveCmd(sender, args);
				return true;
			}
			if(args[0].equalsIgnoreCase("reload")) {
				reloadCmd(sender, args);
				return true;
			}
			if(args[0].equalsIgnoreCase("manager")) {
				setManager(sender, args);
				return true;
			}
			
			//명령어 출력
			HelpUsage(sender);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(args.length==1) return Arrays.asList("add", "connect", "allconnect", "disconnect", "alldisconnect", "give", "reload", "manager");
		if(args.length==2) {
			if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("connect") || args[0].equalsIgnoreCase("disconnect")) {
				return Arrays.asList("chzzk", "afreeca", "tonation");
			}
			if(args[0].equalsIgnoreCase("allconnect") || args[0].equalsIgnoreCase("alldisconnect")) {
				return Arrays.asList("chzzk", "afreeca", "tonation", "all");
			}
			if(args[0].equalsIgnoreCase("give")) {
				List<String> namelist = new ArrayList<>();
				if(args[1].equalsIgnoreCase("afreeca")) {
					for(String s : VarUtil.afreeca_channellist.keySet()) {
						namelist.add(s);
					}
				}
				if(args[1].equalsIgnoreCase("chzzk")) {
					for(String s : VarUtil.chzzk_channellist.keySet()) {
						namelist.add(s);
					}
				}
				if(args[1].equalsIgnoreCase("tonation")) {
					for(String s : VarUtil.tonation_channellist.keySet()) {
						namelist.add(s);
					}
				}
				return namelist;
			}
			if(args[0].equalsIgnoreCase("reload")) return Arrays.asList("all", "config", "manager", "streamer", "donation", "connect");
		}
		if(args.length==3) {
			if(args[0].equalsIgnoreCase("connect") || args[0].equalsIgnoreCase("disconnect")) {
				
				List<String> namelist = new ArrayList<>();
				if(args[1].equalsIgnoreCase("afreeca")) {
					for(String s : VarUtil.afreeca_channellist.keySet()) {
						namelist.add(s);
					}
				}
				if(args[1].equalsIgnoreCase("chzzk")) {
					for(String s : VarUtil.chzzk_channellist.keySet()) {
						namelist.add(s);
					}
				}
				if(args[1].equalsIgnoreCase("tonation")) {
					for(String s : VarUtil.tonation_channellist.keySet()) {
						namelist.add(s);
					}
				}
				return namelist;
			}
		}
		return null;
	}
	
	private void AddCmd(CommandSender s, String[] args) {
		if(args[1].equalsIgnoreCase("chzzk")) {
			try {
				String Name = args[2];
				String Nick = args[3];
				UUID uuid = null;
				if(Bukkit.getPlayer(Nick)!=null) {
					uuid = Bukkit.getPlayer(Nick).getUniqueId();
				}
				else {
					uuid = Bukkit.getOfflinePlayer(Nick).getUniqueId();
				}
				if(uuid != null) {
					String channelid = args[4];
					configF.addChzzkConfig(Name, Nick, uuid.toString(), channelid);
					s.sendMessage("[치지직] "+ Name+"님의 방송이 추가되었습니다");
				}
			} catch (Exception e) {
				HelpUsage(s);
				e.printStackTrace();
			}
		}
		else if(args[1].equalsIgnoreCase("afreeca")) {
			try {
				String Name = args[2];
				String Nick = args[3];
				UUID uuid = null;
				if(Bukkit.getPlayer(Nick)!=null) {
					uuid = Bukkit.getPlayer(Nick).getUniqueId();
				}
				else {
					uuid = Bukkit.getOfflinePlayer(Nick).getUniqueId();
				}
				if(uuid != null) {
					String channelid = args[4];
					configF.addAfreecaConfig(Name, Nick, uuid.toString(), channelid);
					s.sendMessage("[아프리카TV] " +Name+"님의 방송이 추가되었습니다");
				}
			} catch (Exception e) {
				HelpUsage(s);
				e.printStackTrace();
			}
		}
		else if(args[1].equalsIgnoreCase("tonation")) {
			try {
				String Name = args[2];
				String Nick = args[3];
				UUID uuid = null;
				if(Bukkit.getPlayer(Nick)!=null) {
					uuid = Bukkit.getPlayer(Nick).getUniqueId();
				}
				else {
					uuid = Bukkit.getOfflinePlayer(Nick).getUniqueId();
				}
				if(uuid != null) {
					String channelid = args[4];
					configF.addTonationConfig(Name, Nick, uuid.toString(), channelid);
					s.sendMessage("[투네이션] " +Name+"님의 투네가 추가되었습니다");
				}
			} catch (Exception e) {
				HelpUsage(s);
				e.printStackTrace();
			}
		}
		else {
			HelpUsage(s);
		}
	}
	
	private void ConnectCmd(CommandSender s, String[] args) {
		if(args[1].equalsIgnoreCase("chzzk")) {
			String Name = args[2];
			String Nick = VarUtil.chzzk_Nicklist.get(Name);
			UUID uuid = VarUtil.chzzk_Namelist.get(Name);
			if(VarUtil.chzzk_Uuidlist.containsKey(uuid)) {
				if(!VarUtil.chzzkWebSocketList.containsKey(Name)) {
					String channel = VarUtil.chzzk_channellist.get(Name);
					ChzzkUser user = new ChzzkUser(Name, channel, Nick, uuid);
					try {
						chzzkutil.connectChzzk(user);
						s.sendMessage("[치지직] 방송 연결이 정상적으로 작동되었습니다");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					s.sendMessage("[치지직] 이미 방송에 등록되어있습니다");
				}
			}
			else {
				s.sendMessage("[치지직] 리스트에 없습니다");
			}
		}
		else if(args[1].equalsIgnoreCase("afreeca")) {
			String Name = args[2];
			String Nick = VarUtil.afreeca_Nicklist.get(Name);
			UUID uuid = VarUtil.afreeca_Namelist.get(Name);
			if(VarUtil.afreeca_Uuidlist.containsKey(uuid)) {
				if(!VarUtil.afreecaTVWebSocketList.containsKey(Name)) {
					String channel = VarUtil.afreeca_channellist.get(Name);
					AfreecaTVUser user = new AfreecaTVUser(Name, channel, Nick, uuid);
					try {
						afreecaTVutil.connectAfreecaTV(user);
						s.sendMessage("[아프리카] 방송 연결이 정상적으로 작동되었습니다");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					s.sendMessage("[아프리카] 이미 방송에 등록되어있습니다");
				}
			}
			else {
				s.sendMessage("[아프리카] 리스트에 없습니다");
			}
		}
		else if(args[1].equalsIgnoreCase("tonation")) {
			String Name = args[2];
			String Nick = VarUtil.tonation_Nicklist.get(Name);
			UUID uuid = VarUtil.tonation_Namelist.get(Name);
			if(VarUtil.tonation_Uuidlist.containsKey(uuid)) {
				if(!VarUtil.tonationWebSocketList.containsKey(Name)) {
					String channel = VarUtil.tonation_channellist.get(Name);
					TonationUser user = new TonationUser(Name, channel, Nick, uuid);
					try {
						tonationU.connectTonation(user);
						s.sendMessage("[투네이션] 방송 연결이 정상적으로 작동되었습니다");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					s.sendMessage("[투네이션] 이미 API에 등록되어있습니다");
				}
			}
			else {
				s.sendMessage("[투네이션] 리스트에 없습니다");
			}
		}
		else {
			HelpUsage(s);
		}
	}

	private void AllConnectCmd(CommandSender s, String[] args) {
		if(args[1].equalsIgnoreCase("chzzk")) {
			for(Entry<String, String> entry : VarUtil.chzzk_channellist.entrySet()) {
				if(!VarUtil.chzzkWebSocketList.containsKey(entry.getKey())) {
					String Nick = VarUtil.chzzk_Nicklist.get(entry.getKey());
					UUID uuid = VarUtil.chzzk_Namelist.get(entry.getKey());
					String channel = VarUtil.chzzk_channellist.get(entry.getKey());
					ChzzkUser user = new ChzzkUser(entry.getKey(), channel, Nick, uuid);
					try {
						chzzkutil.connectChzzk(user);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[치지직] 방송 전체 연결이 작동되었습니다");
		}
		else if(args[1].equalsIgnoreCase("afreeca")) {
			for(Entry<String, String> entry : VarUtil.afreeca_channellist.entrySet()) {
				if(!VarUtil.afreecaTVWebSocketList.containsKey(entry.getKey())) {
					String Nick = VarUtil.afreeca_Nicklist.get(entry.getKey());
					UUID uuid = VarUtil.afreeca_Namelist.get(entry.getKey());
					String channel = VarUtil.afreeca_channellist.get(entry.getKey());
					AfreecaTVUser user = new AfreecaTVUser(entry.getKey(), channel, Nick, uuid);
					try {
						afreecaTVutil.connectAfreecaTV(user);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[아프리카] 방송 전체 연결이 작동되었습니다");
		}
		else if(args[1].equalsIgnoreCase("tonation")) {
			for(Entry<String, String> entry : VarUtil.tonation_channellist.entrySet()) {
				if(!VarUtil.tonationWebSocketList.containsKey(entry.getKey())) {
					String Nick = VarUtil.tonation_Nicklist.get(entry.getKey());
					UUID uuid = VarUtil.tonation_Namelist.get(entry.getKey());
					String channel = VarUtil.tonation_channellist.get(entry.getKey());
					TonationUser user = new TonationUser(entry.getKey(), channel, Nick, uuid);
					try {
						tonationU.connectTonation(user);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[투네이션] 투네 전체 연결이 작동되었습니다");
		}
		else if(args[1].equalsIgnoreCase("all")) {
			for(Entry<String, String> entry : VarUtil.chzzk_channellist.entrySet()) {
				if(!VarUtil.chzzkWebSocketList.containsKey(entry.getKey())) {
					String Nick = VarUtil.chzzk_Nicklist.get(entry.getKey());
					UUID uuid = VarUtil.chzzk_Namelist.get(entry.getKey());
					String channel = VarUtil.chzzk_channellist.get(entry.getKey());
					ChzzkUser user = new ChzzkUser(entry.getKey(), channel, Nick, uuid);
					try {
						chzzkutil.connectChzzk(user);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			for(Entry<String, String> entry : VarUtil.afreeca_channellist.entrySet()) {
				if(!VarUtil.afreecaTVWebSocketList.containsKey(entry.getKey())) {
					String Nick = VarUtil.afreeca_Nicklist.get(entry.getKey());
					UUID uuid = VarUtil.afreeca_Namelist.get(entry.getKey());
					String channel = VarUtil.afreeca_channellist.get(entry.getKey());
					AfreecaTVUser user = new AfreecaTVUser(entry.getKey(), channel, Nick, uuid);
					try {
						afreecaTVutil.connectAfreecaTV(user);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			for(Entry<String, String> entry : VarUtil.tonation_channellist.entrySet()) {
				if(!VarUtil.tonationWebSocketList.containsKey(entry.getKey())) {
					String Nick = VarUtil.tonation_Nicklist.get(entry.getKey());
					UUID uuid = VarUtil.tonation_Namelist.get(entry.getKey());
					String channel = VarUtil.tonation_channellist.get(entry.getKey());
					TonationUser user = new TonationUser(entry.getKey(), channel, Nick, uuid);
					try {
						tonationU.connectTonation(user);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[ALL] 전체 연결이 작동되었습니다");
		}
		else {
			HelpUsage(s);
		}
	}
	
	private void DisconnectCmd(CommandSender s, String[] args) {
		if(args[1].equalsIgnoreCase("chzzk")) {
			String Name = args[2];
			UUID uuid = VarUtil.chzzk_Namelist.get(Name);
			if(VarUtil.chzzk_Uuidlist.containsKey(uuid)) {
				if(VarUtil.chzzkWebSocketList.containsKey(Name)) {
					try {
						chzzkutil.disconnectChzzk(Name);
						s.sendMessage("[치지직] 방송 해제가 정상적으로 작동되었습니다");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					s.sendMessage("[치지직] 방송이 등록되어있지 않습니다");
				}
			}
			else {
				s.sendMessage("[치지직] 리스트에 없습니다");
			}
		}
		else if(args[1].equalsIgnoreCase("afreeca")) {
			String Name = args[2];
			UUID uuid = VarUtil.afreeca_Namelist.get(Name);
			if(VarUtil.afreeca_Uuidlist.containsKey(uuid)) {
				if(VarUtil.afreecaTVWebSocketList.containsKey(Name)) {
					try {
						afreecaTVutil.disconnectAfreecaTV(Name);
						s.sendMessage("[아프리카] 방송 해제가 정상적으로 작동되었습니다");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					s.sendMessage("[아프리카] 방송이 등록되어있지 않습니다");
				}
			}
			else {
				s.sendMessage("[아프리카] 리스트에 없습니다");
			}
		}
		else if(args[1].equalsIgnoreCase("tonation")) {
			String Name = args[2];
			UUID uuid = VarUtil.tonation_Namelist.get(Name);
			if(VarUtil.tonation_Uuidlist.containsKey(uuid)) {
				if(VarUtil.tonationWebSocketList.containsKey(Name)) {
					try {
						tonationU.disconnectTonation(Name);
						s.sendMessage("[투네이션] 투네 해제가 정상적으로 작동되었습니다");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					s.sendMessage("[투네이션] 투네가 등록되어있지 않습니다");
				}
			}
			else {
				s.sendMessage("[투네이션] 리스트에 없습니다");
			}
		}
		else {
			HelpUsage(s);
		}
	}
	
	private void AllDisconnectCmd(CommandSender s, String[] args) {
		if(args[1].equalsIgnoreCase("chzzk")) {
			for(Entry<String, String> entry : VarUtil.chzzk_channellist.entrySet()) {
				if(VarUtil.chzzkWebSocketList.containsKey(entry.getKey())) {
					try {
						chzzkutil.disconnectChzzk(entry.getKey());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[치지직] 방송 전체 연결해제가 작동되었습니다");
		}
		else if(args[1].equalsIgnoreCase("afreeca")) {
			for(Entry<String, String> entry : VarUtil.afreeca_channellist.entrySet()) {
				if(VarUtil.afreecaTVWebSocketList.containsKey(entry.getKey())) {
					try {
						afreecaTVutil.disconnectAfreecaTV(entry.getKey());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[아프리카] 방송 전체 연결해제가 작동되었습니다");
		}
		else if(args[1].equalsIgnoreCase("tonation")) {
			for(Entry<String, String> entry : VarUtil.tonation_channellist.entrySet()) {
				if(VarUtil.tonationWebSocketList.containsKey(entry.getKey())) {
					try {
						tonationU.disconnectTonation(entry.getKey());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[투네이션] 투네 전체 연결해제가 작동되었습니다");
		}
		else if(args[1].equalsIgnoreCase("all")) {
			for(Entry<String, String> entry : VarUtil.chzzk_channellist.entrySet()) {
				if(VarUtil.chzzkWebSocketList.containsKey(entry.getKey())) {
					try {
						chzzkutil.disconnectChzzk(entry.getKey());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			for(Entry<String, String> entry : VarUtil.afreeca_channellist.entrySet()) {
				if(VarUtil.afreecaTVWebSocketList.containsKey(entry.getKey())) {
					try {
						afreecaTVutil.disconnectAfreecaTV(entry.getKey());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			for(Entry<String, String> entry : VarUtil.tonation_channellist.entrySet()) {
				if(VarUtil.tonationWebSocketList.containsKey(entry.getKey())) {
					try {
						tonationU.disconnectTonation(entry.getKey());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			s.sendMessage("[ALL] 전체 연결해제가 작동되었습니다");
		}
		else {
			HelpUsage(s);
		}
	}
	
	private void GiveCmd(CommandSender s, String[] args) {
		if(args.length >= 3) {
			try {
				String Name = args[1];
				int cash = Integer.parseInt(args[2]);
				if(VarUtil.chzzk_Namelist.containsKey(Name)) {
					UUID uuid = VarUtil.chzzk_Namelist.get(Name);
					String playername = VarUtil.chzzk_Nicklist.get(Name);
					donationU.donation(uuid, playername, cash);
				}
				else if(VarUtil.afreeca_Namelist.containsKey(Name)) {
					UUID uuid = VarUtil.afreeca_Namelist.get(Name);
					String playername = VarUtil.afreeca_Nicklist.get(Name);
					donationU.donation(uuid, playername, cash);
				}
				else if(VarUtil.tonation_Namelist.containsKey(Name)) {
					UUID uuid = VarUtil.tonation_Namelist.get(Name);
					String playername = VarUtil.tonation_Nicklist.get(Name);
					donationU.donation(uuid, playername, cash);
				}
			} catch (Exception e) {
				HelpUsage(s);
				e.printStackTrace();
			}
		}
		else {
			HelpUsage(s);
		}
	}
	
	private void setManager(CommandSender s, String[] args) {
		if(args.length >= 2) {
			try {
				String id = args[2];
				if(args[1].equalsIgnoreCase("afreeca")) {
					managerF.addAfreecaManager(id);
					s.sendMessage(id + "의 아이디가 매니저로 등록되었습니다");
				}
				if(args[1].equalsIgnoreCase("chzzk")) {
					managerF.addChzzkManager(id);
					s.sendMessage(id + "의 아이디가 매니저로 등록되었습니다");
				}
			} catch (Exception e) {
				HelpUsage(s);
				e.printStackTrace();
			}
		}
		else {
			HelpUsage(s);
		}
	}
	
	private void reloadCmd(CommandSender s, String[] args) {
		if(args.length >= 2) {
			try {
				if(args[1].equalsIgnoreCase("all")) {
					configF.reloadConfigALL();
					managerF.reloadManager();
					connectF.reloadConnect();
					s.sendMessage("전체 데이터 리로드 완료");
				}
				if(args[1].equalsIgnoreCase("config")) {
					configF.reloadConfigALL();
					s.sendMessage("Config 전체 데이터 리로드 완료");
				}
				if(args[1].equalsIgnoreCase("manager")) {
					managerF.reloadManager();
					s.sendMessage("매니저 데이터 리로드 완료");
				}
				if(args[1].equalsIgnoreCase("streamer")) {
					configF.reloadConfigStream();
					s.sendMessage("방송 데이터 리로드 완료");
				}
				if(args[1].equalsIgnoreCase("donation")) {
					configF.reloadConfigDonation();
					s.sendMessage("후원 데이터 리로드 완료");
				}
				if(args[1].equalsIgnoreCase("connect")) {
					connectF.reloadConnect();
					s.sendMessage("연결 데이터 리로드 완료");
				}
			} catch (Exception e) {
				HelpUsage(s);
				e.printStackTrace();
			}
		}
		else {
			HelpUsage(s);
		}
	}
	
	private void HelpUsage(CommandSender s) {
		s.sendMessage("===================================================");
        s.sendMessage("/Honation add [chzzk/afreeca/tonation] [등록이름] [마크닉네임] [방송코드]");
        s.sendMessage("/Honation connect [chzzk/afreeca/tonation] [등록이름]");
        s.sendMessage("/Honation allconnect [chzzk/afreeca/tonation/all]");
        s.sendMessage("/Honation disconnect [chzzk/afreeca/tonation] [등록이름]");
        s.sendMessage("/Honation alldisconnect [chzzk/afreeca/tonation/all]");
        s.sendMessage("/Honation give [등록이름] [금액]");
        s.sendMessage("/Honation manager [chzzk/afreeca] [방송코드]");
        s.sendMessage("/Honation reload [all/config/manager/streamer/donation/connect]");
        s.sendMessage("===================================================");
    }
	
	private void notOp(Player p) {
		p.sendMessage("[Honation] OP가 아닙니다");
	}

}
