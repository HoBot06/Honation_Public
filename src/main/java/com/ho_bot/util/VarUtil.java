package com.ho_bot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.ho_bot.afreecatv.AfreecaTVLiveInfo;
import com.ho_bot.afreecatv.AfreecaTVWebSocket;
import com.ho_bot.chzzk.ChzzkWebSocket;
import com.ho_bot.dlc.DLC_Donation;
import com.ho_bot.tonation.TonationWebSocket;

public class VarUtil {
	
	/**이름, 채널코드*/
	public static Map<String, String> chzzk_channellist = new HashMap<>();
	/**UUID, 이름*/
	public static Map<UUID, String> chzzk_Uuidlist = new HashMap<>();
	/**이름, UUID*/
	public static Map<String, UUID> chzzk_Namelist = new HashMap<>();
	/**이름, 닉네임*/
	public static Map<String, String> chzzk_Nicklist = new HashMap<>();
	/**id, Chat*/
	public static Map<String, String> chzzk_chatChannelId = new HashMap<>();
	/**id, Token*/
	public static Map<String, String> chzzk_accessToken = new HashMap<>();
	
	public static Map<String, ChzzkWebSocket> chzzkWebSocketList = new HashMap<>();
	
	
	/**이름, 채널코드*/
	public static Map<String, String> afreeca_channellist = new HashMap<>();
	/**UUID, 이름*/
	public static Map<UUID, String> afreeca_Uuidlist = new HashMap<>();
	/**이름, UUID*/
	public static Map<String, UUID> afreeca_Namelist = new HashMap<>();
	/**이름, 닉네임*/
	public static Map<String, String> afreeca_Nicklist = new HashMap<>();
	/**id, liveinfo*/
	public static Map<String, AfreecaTVLiveInfo> afreeca_liveInfo = new HashMap<>();
	
	public static Map<String, AfreecaTVWebSocket> afreecaTVWebSocketList = new HashMap<>();
	
	
	/**이름, 채널코드*/
	public static Map<String, String> tonation_channellist = new HashMap<>();
	/**UUID, 이름*/
	public static Map<UUID, String> tonation_Uuidlist = new HashMap<>();
	/**이름, UUID*/
	public static Map<String, UUID> tonation_Namelist = new HashMap<>();
	/**이름, 닉네임*/
	public static Map<String, String> tonation_Nicklist = new HashMap<>();
	/**id, payload*/
	public static Map<String, String> tonation_payload = new HashMap<>();
	
	public static Map<String, TonationWebSocket> tonationWebSocketList = new HashMap<>();
	
	public static boolean isConnect_S = false;
	public static boolean isConnect_F = false;
	public static boolean isConnect_E = false;
	
	public static String cmdConnect_S = "";
	public static String cmdConnect_F = "";
	public static String cmdConnect_E = "";
	
	public static Map<String, List<String>> Cmdlist = new HashMap<>();
	public static Map<String, DLC_Donation> dlclist = new HashMap<>();
	public static List<String> afreecaManagerList = new ArrayList<>();
	public static List<String> ChzzkManagerList = new ArrayList<>();

	public static HttpClient Afreeca_httpClient;
	public static HttpClient Chzzk_httpClient;
	public static HttpClient Tonation_httpClient;
	
	public static void httpStart() {
		SslContextFactory.Client a_sslContextFactory = new SslContextFactory.Client();
		a_sslContextFactory.setTrustAll(true);
		a_sslContextFactory.setEndpointIdentificationAlgorithm(null);

		ClientConnector a_clientConnector = new ClientConnector();
		a_clientConnector.setSslContextFactory(a_sslContextFactory);
		
		HttpClient a_client = new HttpClient(new HttpClientTransportOverHTTP(a_clientConnector));
		a_client.setMaxConnectionsPerDestination(500);
		Afreeca_httpClient = a_client;
		
		SslContextFactory.Client c_sslContextFactory = new SslContextFactory.Client();
		c_sslContextFactory.setTrustAll(true);
		c_sslContextFactory.setEndpointIdentificationAlgorithm(null);

		ClientConnector c_clientConnector = new ClientConnector();
		c_clientConnector.setSslContextFactory(c_sslContextFactory);
		
		HttpClient c_client = new HttpClient(new HttpClientTransportOverHTTP(c_clientConnector));
		c_client.setMaxConnectionsPerDestination(500);
		Chzzk_httpClient = c_client;
		
		SslContextFactory.Client t_sslContextFactory = new SslContextFactory.Client();
		t_sslContextFactory.setTrustAll(true);
		t_sslContextFactory.setEndpointIdentificationAlgorithm(null);

		ClientConnector t_clientConnector = new ClientConnector();
		t_clientConnector.setSslContextFactory(t_sslContextFactory);
		
		HttpClient t_client = new HttpClient(new HttpClientTransportOverHTTP(t_clientConnector));
		t_client.setMaxConnectionsPerDestination(500);
		Tonation_httpClient = t_client;
	}
}
