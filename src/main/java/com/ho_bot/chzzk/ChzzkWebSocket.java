package com.ho_bot.chzzk;

import java.io.IOException;
import java.net.URI;

import org.bukkit.ChatColor;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ho_bot.util.ConnectUtil;
import com.ho_bot.util.DonationUtil;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class ChzzkWebSocket {
	
	private DonationUtil donationU = new DonationUtil();
	private ConnectUtil connectU = new ConnectUtil();
	
	private final String chatChannelId;
    private final String accessToken;
    private final ChzzkUser chzzkUser;

    private Thread pingThread;

    private boolean isAlive = true;

    private static final int CHZZK_CHAT_CMD_PING = 0;
    //private static final int CHZZK_CHAT_CMD_PONG = 10000;
    private static final int CHZZK_CHAT_CMD_CONNECT = 100;
    //private static final int CHZZK_CHAT_CMD_CONNECT_RES = 10100;

    //private static final int CHZZK_CHAT_CMD_SEND_CHAT = 3101;
    //private static final int CHZZK_CHAT_CMD_REQUEST_RECENT_CHAT = 5101;
    private static final int CHZZK_CHAT_CMD_CHAT = 93101;
    private static final int CHZZK_CHAT_CMD_DONATION = 93102;

    private final JSONParser parser = new JSONParser();
    
    private final String chzzkUri;
    private final WebSocketClient webSocketClient = new WebSocketClient(VarUtil.Chzzk_httpClient);
    
    public ChzzkWebSocket(String serverUri, String chatChannelId, String accessToken, ChzzkUser chzzkUser) {
    	
        this.chzzkUri = serverUri;
        this.chatChannelId = chatChannelId;
        this.accessToken = accessToken;
        this.chzzkUser = chzzkUser;
    }
    
    public void run() {
        try {
            webSocketClient.start();
            
            connectWebSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    private void connectWebSocket() {
        try {
        	WebSocketAdapter socket = new WebSocketAdapter() {
                @Override
                public void onWebSocketConnect(Session sess) {
                    super.onWebSocketConnect(sess);
                    
                    LogUtil.info(ChatColor.GREEN + "[ChzzkWebsocket][" + chzzkUser.name + "] 치지직 웹소켓 연결이 연결되었습니다.");
                    
                    try {
                    	JSONObject baseObject = new JSONObject();
                        baseObject.put("ver", "2");
                        baseObject.put("svcid", "game");
                        baseObject.put("cid", chatChannelId);

                        JSONObject sendObject = new JSONObject(baseObject);
                        sendObject.put("cmd", CHZZK_CHAT_CMD_CONNECT);
                        sendObject.put("tid", 1);

                        JSONObject bdyObject = new JSONObject();
                        bdyObject.put("uid", null);
                        bdyObject.put("devType", 2001);
                        bdyObject.put("accTkn", accessToken);
                        bdyObject.put("auth", "READ");

                        sendObject.put("bdy", bdyObject);

                        sess.getRemote().sendString(sendObject.toString());
                        
                        startPingThread(sess);
                        
                        connectU.runConnectS(chzzkUser.uuid, chzzkUser.nick, "치지직");
					} catch (Exception e) {
						e.printStackTrace();
					}
                }

                @Override
                public void onWebSocketClose(int statusCode, String reason) {
                    super.onWebSocketClose(statusCode, reason);
                    LogUtil.info(ChatColor.RED + "[ChzzkWebsocket][" + chzzkUser.name + "] 치지직 웹소켓 연결이 끊겼습니다.");
                    
                    isAlive = false;

                    pingThread.interrupt();
                    pingThread = null;
                    
                    connectU.runConnectF(chzzkUser.uuid, chzzkUser.nick, "치지직");
                }

                @Override
                public void onWebSocketText(String message) {
                    super.onWebSocketText(message);
                    try {
                    	JSONObject messageObject = (JSONObject) parser.parse(message);
                        int cmd = Integer.parseInt(messageObject.get("cmd").toString());

                        if(cmd != CHZZK_CHAT_CMD_DONATION && cmd != CHZZK_CHAT_CMD_CHAT) return;
                        
                        JSONObject bdyObject = (JSONObject) ((JSONArray) messageObject.get("bdy")).get(0);
                        String uid = (String) bdyObject.get("uid");
                        String nickname = "익명";
                        if (!uid.equalsIgnoreCase("anonymous")) {
                            String profile = (String) bdyObject.get("profile");
                            JSONObject profileObejct = (JSONObject) parser.parse(profile);
                            nickname = (String) profileObejct.get("nickname");
                        }
                        if(cmd == CHZZK_CHAT_CMD_DONATION) {

                            String extras = (String) bdyObject.get("extras");
                            JSONObject extraObject = (JSONObject) parser.parse(extras);
                            if (extraObject.get("payAmount") == null) {
                                return;
                            }
                            int payAmount = Integer.parseInt(extraObject.get("payAmount").toString());
                            
                            donationU.donation(chzzkUser.uuid, chzzkUser.nick, nickname, "치지직", payAmount);
                            return;
                        }
                        else if(cmd == CHZZK_CHAT_CMD_CHAT){
                        	String msg = (String) bdyObject.get("msg");
                        	if(VarUtil.ChzzkManagerList == null) return;
                        	if(!VarUtil.ChzzkManagerList.contains(uid)) return;
                        	String[] msg_s = msg.split(" ");
                        	if(msg_s[0].equalsIgnoreCase("!후원테스트")) {
                        		try {
                        			if(msg_s.length >= 2) {
                            			int amo = Integer.parseInt(msg_s[1]);
                            			donationU.donation(chzzkUser.uuid, chzzkUser.nick, "후원테스트", "치지직", amo);
                            		}
								} catch (Exception e) {
								}
                        	}
                        }

                    } catch (Exception e) {
                    	LogUtil.info(ChatColor.RED + "[ChzzkWebsocket][" + chzzkUser.name + "] 치지직 메시지 파싱 중 오류가 발생했습니다.");
                    }
                }
                
                @Override
                public void onWebSocketError(Throwable cause) {
                	connectU.runConnectE(chzzkUser.uuid, chzzkUser.nick, "치지직");
                	cause.printStackTrace();
                }
            };
            URI uri = new URI(chzzkUri);
            webSocketClient.connect(socket, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPingThread(Session session) {
    	
        pingThread = new Thread(() -> {
            while (isAlive) {
                try {
                    Thread.sleep(19996);
                    JSONObject pongObject = new JSONObject();
                    pongObject.put("cmd", CHZZK_CHAT_CMD_PING);
                    pongObject.put("ver", 2);
                    session.getRemote().sendString(pongObject.toString());
                } catch (InterruptedException ignore) {
                	LogUtil.info(ChatColor.RED + "치지직 웹소켓 핑 스레드가 종료되었습니다.");
                } catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        pingThread.start();
    }
    
    public void close() {
    	try {
			webSocketClient.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
