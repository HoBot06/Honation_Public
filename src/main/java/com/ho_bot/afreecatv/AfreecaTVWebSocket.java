package com.ho_bot.afreecatv;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.bukkit.ChatColor;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.ho_bot.util.ConnectUtil;
import com.ho_bot.util.DonationUtil;
import com.ho_bot.util.LogUtil;
import com.ho_bot.util.VarUtil;

public class AfreecaTVWebSocket {
	
	private DonationUtil donationU = new DonationUtil();
	private ConnectUtil connectU = new ConnectUtil();
	
	private final AfreecaTVLiveInfo liveInfo;
    private final AfreecaTVUser afreecaTVUser;

    private Thread pingThread;

    private boolean isAlive = true;

    private static final String F = "\u000c";
    private static final String ESC = "\u001b\t";

    private static final String COMMAND_PING = "0000";
    private static final String COMMAND_CONNECT = "0001";
    private static final String COMMAND_JOIN = "0002";
    private static final String COMMAND_ENTER = "0004";
    private static final String COMMAND_ENTER_FAN = "0127"; // 0004직후 호출 됨, 입장한 유저의 열혈팬/팬 구분으로 추정
    private static final String COMMAND_CHAT = "0005";
    private static final String COMMAND_DONE = "0018";
    //private static final String COMMNAD_C = "0110";
    //private static final String COMMNAD_D = "0054";
    //private static final String COMMNAD_E = "0090";
    //private static final String COMMNAD_F = "0094";

    // 최초 연결시 전달하는 패킷, CONNECT_PACKET = f'{ESC}000100000600{F*3}16{F}'
    private static final String CONNECT_PACKET = makePacket(COMMAND_CONNECT, String.format("%s16%s", F.repeat(3), F));
    // CONNECT_PACKET 전송시 수신 하는 패킷, CONNECT_PACKET = f'{ESC}000100000700{F*3}16|0{F}'
    private static final String CONNECT_RES_PACKET = makePacket(COMMAND_CONNECT, String.format("%s16|0%s", F.repeat(2), F));
    // 주기적으로 핑을 보내서 메세지를 계속 수신하는 패킷, PING_PACKET = f'{ESC}000000000100{F}'
    private static final String PING_PACKET = makePacket(COMMAND_PING, F);

    private final String afreecaUri;
    private WebSocketClient webSocketClient = new WebSocketClient(VarUtil.Afreeca_httpClient);
    
    public AfreecaTVWebSocket(String serverUri, AfreecaTVLiveInfo liveInfo, AfreecaTVUser afreecaTVUser) {
    	
        this.afreecaUri = serverUri;
        this.liveInfo = liveInfo;
        this.afreecaTVUser = afreecaTVUser;
        
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
            	Session session;
                @Override
                public void onWebSocketConnect(Session sess) {
                    super.onWebSocketConnect(sess);
                    session = sess;
                    LogUtil.info(ChatColor.GREEN + "[AfreecaTVWebsocket][" + afreecaTVUser.name + "] 아프리카 웹소켓 연결이 연결되었습니다.");
                    startPingThread(session);
                    
                    connectU.runConnectS(afreecaTVUser.uuid, afreecaTVUser.nick, "아프리카TV");
                }

                @Override
                public void onWebSocketClose(int statusCode, String reason) {
                    super.onWebSocketClose(statusCode, reason);
                    LogUtil.info(ChatColor.RED + "[AfreecaTVWebsocket][" + afreecaTVUser.name + "] 아프리카 웹소켓 연결이 끊겼습니다.");
                    
                    isAlive = false;

                    pingThread.interrupt();
                    pingThread = null;
                    
                    connectU.runConnectF(afreecaTVUser.uuid, afreecaTVUser.nick, "아프리카TV");
                }
                
                @Override
                public void onWebSocketBinary(byte[] payload, int offset, int len) {
                	super.onWebSocketBinary(payload, offset, len);
                	
                	String message = new String(payload, StandardCharsets.UTF_8);
                    if (CONNECT_RES_PACKET.equals(message)) {
                        String CHATNO = liveInfo.CHATNO();
                        String JOIN_PACKET = makePacket(COMMAND_JOIN, String.format("%s%s%s", F, CHATNO, F.repeat(5)));
                        try {
							session.getRemote().sendString(JOIN_PACKET);
						} catch (IOException e) {
							e.printStackTrace();
						}
                        return;
                    }
                	
                    try {
                        AfreecaTVPacket packet = new AfreecaTVPacket(message.replace(ESC, "").split(F));

                        String cmd = packet.getCommand();
                        
                        List<String> dataList = switch (cmd) {
                            case COMMAND_ENTER -> null;
                            case COMMAND_ENTER_FAN -> null;
                            default -> packet.getDataList();
                        };
                        if (dataList == null) {
                            return;
                        }
                        String msg = null;
                        String id = null;
                        String nickname = null;
                        int payAmount = 0;
                        if (cmd.equals(COMMAND_DONE)) {
                            nickname = dataList.get(2);
                            payAmount = Integer.parseInt(dataList.get(3)) * 100;
                            if(payAmount > 0) {
                            	donationU.donation(afreecaTVUser.uuid ,afreecaTVUser.nick, nickname, "아프리카TV" , payAmount);
                            }
                            return;
                        } else if (cmd.equals(COMMAND_CHAT)) {
                            msg = dataList.get(0);
                            id = dataList.get(1);
                            if(VarUtil.afreecaManagerList == null) return;
                            if(!VarUtil.afreecaManagerList.contains(id)) return;
                        	String[] msg_s = msg.split(" ");
                        	if(msg_s[0].equalsIgnoreCase("!후원테스트")) {
                        		try {
                        			if(msg_s.length >= 2) {
                            			int amo = Integer.parseInt(msg_s[1]);
                            			donationU.donation(afreecaTVUser.uuid ,afreecaTVUser.nick, "후원테스트", "아프리카TV" , amo);
                            		}
								} catch (Exception e) {
								}
                        	}
                            return;
                        }
                		else {
                            return;
                        }
                        
                    } catch (Exception e) {
                    	e.printStackTrace();
                    	LogUtil.info(ChatColor.RED + "[AfreecaTVWebsocket][" + afreecaTVUser.name + "] 아프리카 메시지 파싱 중 오류가 발생했습니다.");
                    	
                    }
                }
                

                @Override
                public void onWebSocketText(String message) {
                }
                
                @Override
                public void onWebSocketError(Throwable cause) {
                	connectU.runConnectE(afreecaTVUser.uuid, afreecaTVUser.nick, "아프리카TV");
                	cause.printStackTrace();
                }
            };
            
            URI uri = new URI(afreecaUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            request.setSubProtocols("chat");
            webSocketClient.connect(socket, uri, request);
        } catch (Exception e) {
            e.printStackTrace();
        }	
    }

    private void startPingThread(Session session) {
    	
        pingThread = new Thread(() -> {
            try {
				session.getRemote().sendString(CONNECT_PACKET);
			} catch (IOException e) {
				e.printStackTrace();
			}
            while (isAlive) {
                try {
                    Thread.sleep(19996);
                    session.getRemote().sendString(PING_PACKET);
                    
                } catch (InterruptedException ignore) {
                	LogUtil.info(ChatColor.RED + "아프리카 웹소켓 핑 스레드가 종료되었습니다.");
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
    
    private static String makePacket(String command, String data) {
        return String.format("%s%s%s%s", ESC, command, makeLengthPacket(data), data);
    }

    private static String makeLengthPacket(String data) {
        return String.format("%06d00", data.length());
    }

}
