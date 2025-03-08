package com.ho_bot.tonation;

import java.io.IOException;
import java.net.URI;

import org.bukkit.ChatColor;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ho_bot.util.ConnectUtil;
import com.ho_bot.util.DonationUtil;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class TonationWebSocket {
	
	private DonationUtil donationU = new DonationUtil();
	private ConnectUtil connectU = new ConnectUtil();
	
    private final TonationUser tonationUser;

    private Thread pingThread;

    private boolean isAlive = true;

    private static final int TONATION_CHAT_CMD_PING = 0;

    private final JSONParser parser = new JSONParser();
    
    private final String tonationUri;
    private final WebSocketClient webSocketClient = new WebSocketClient(VarUtil.Tonation_httpClient);
    
	public TonationWebSocket(String serverUri, String payLoad, TonationUser tonationUser) {
        this.tonationUri = serverUri + payLoad;
        this.tonationUser = tonationUser;
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
                    LogUtil.info(ChatColor.GREEN + "[TonationWebsocket][" + tonationUser.name + "] 투네이션 웹소켓 연결이 연결되었습니다.");
                    startPingThread(sess);
                    
                    connectU.runConnectS(tonationUser.uuid, tonationUser.nick, "투네이션");
                }

                @Override
                public void onWebSocketClose(int statusCode, String reason) {
                    super.onWebSocketClose(statusCode, reason);
                    LogUtil.info(ChatColor.RED + "[TonationWebSocket][" + tonationUser.name + "] 투네이션 웹소켓 연결이 끊겼습니다.");
                    
                    isAlive = false;

                    pingThread.interrupt();
                    pingThread = null;
                    
                    connectU.runConnectF(tonationUser.uuid, tonationUser.nick, "투네이션");
                }

                @Override
                public void onWebSocketText(String message) {
                    super.onWebSocketText(message);
                    try {
                    	JSONObject messageObject = (JSONObject) parser.parse(message);
                		JSONObject content = (JSONObject) messageObject.get("content");
                		if(content == null) return;
                		int payAmount = Integer.parseInt(content.get("amount").toString());
                		if(payAmount <= 0) return;
                		String uid = (String) content.get("uid");
                		String nickname = "익명";
                        if (uid!=null) {
                            nickname = (String) content.get("name");
                        }
                		donationU.donation(tonationUser.uuid, tonationUser.nick, nickname, "투네이션", payAmount);
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
                
                @Override
                public void onWebSocketError(Throwable cause) {
                	connectU.runConnectE(tonationUser.uuid, tonationUser.nick, "투네이션");
                	cause.printStackTrace();
                }
            };

            URI uri = new URI(tonationUri);
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
                    pongObject.put("cmd", TONATION_CHAT_CMD_PING);
                    pongObject.put("ver", 2);
                    session.getRemote().sendString(pongObject.toString());
                } catch (InterruptedException ignore) {
                	LogUtil.info(ChatColor.RED + "투네이션 웹소켓 핑 스레드가 종료되었습니다.");
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
