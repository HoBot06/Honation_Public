package com.ho_bot.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.ho_bot.afreecatv.AfreecaTVUtil;
import com.ho_bot.chzzk.ChzzkUtil;
import com.ho_bot.command.Ho_Cmd;
import com.ho_bot.event.Ho_Event;
import com.ho_bot.file.ConfigFile;
import com.ho_bot.file.ConnectFile;
import com.ho_bot.file.ManagerFile;
import com.ho_bot.timer.AfreecaTvTimer;
import com.ho_bot.timer.ChzzkTimer;
import com.ho_bot.tonation.TonationUtil;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class Honation extends JavaPlugin{
	
	public static Honation inst;
	
	private ConfigFile configfile = new ConfigFile();
	private ManagerFile managerfile = new ManagerFile();
	private AfreecaTVUtil afreecatvU = new AfreecaTVUtil();
	private ChzzkUtil chzzkutil = new ChzzkUtil();
	private TonationUtil tonationU = new TonationUtil();
	private ConnectFile connectF = new ConnectFile();
	
	public void onEnable() {
		inst = this;
		Bukkit.getConsoleSender().sendMessage("Honation_Public " + getDescription().getVersion() + " Online");
		
		Ho_Event.setPlugin(this);
		getServer().getPluginManager().registerEvents(new Ho_Event(), this);
		
		getCommand("honation").setExecutor(new Ho_Cmd());
		getCommand("honation").setTabCompleter(new Ho_Cmd());
		
		getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        
        configfile.reloadConfigALL();
        managerfile.reloadManager();
        connectF.reloadConnect();
		
		ChzzkTimer chzzkTimer = new ChzzkTimer();
		chzzkTimer.runTaskTimerAsynchronously(this, 0L, 20*600L);
		
		AfreecaTvTimer afreecaTvTimer = new AfreecaTvTimer();
		afreecaTvTimer.runTaskTimerAsynchronously(this, 0, 20*600L);
		
//		CmdTimer cmdTimer = new CmdTimer();
//		cmdTimer.runTaskTimer(this, 0, 5L);
		
		try {
			VarUtil.httpStart();
			VarUtil.Afreeca_httpClient.start();
			VarUtil.Chzzk_httpClient.start();
			VarUtil.Tonation_httpClient.start();
			//LogUtil.info("Client 시작");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onDisable() {
		try {
			afreecatvU.disconnectAfreecaTV(VarUtil.afreecaTVWebSocketList);
        } catch (InterruptedException e) {
            LogUtil.info(ChatColor.RED + "플러그인 비활성화 중 아프리카에서 오류가 발생했습니다.");
        }
		try {
			chzzkutil.disconnectChzzk(VarUtil.chzzkWebSocketList);
        } catch (InterruptedException e) {
            LogUtil.info(ChatColor.RED + "플러그인 비활성화 중 치지직에서 오류가 발생했습니다.");
        }
		try {
			tonationU.disconnectTonation(VarUtil.tonationWebSocketList);
        } catch (InterruptedException e) {
            LogUtil.info(ChatColor.RED + "플러그인 비활성화 중 투네이션에서 오류가 발생했습니다.");
        }
		
		try {
			VarUtil.Afreeca_httpClient.stop();
			VarUtil.Chzzk_httpClient.stop();
			VarUtil.Tonation_httpClient.stop();
			//LogUtil.info("Client 종료");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
