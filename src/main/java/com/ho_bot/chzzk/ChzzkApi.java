package com.ho_bot.chzzk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ho_bot.exception.DoneException;
import com.ho_bot.exception.ExceptionCode;

public class ChzzkApi {
	
	public static String getChatChannelId(String id) {
        String requestURL = "https://api.chzzk.naver.com/polling/v2/channels/" + id + "/live-status";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create(requestURL))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .build(); // HttpRequest 생성


            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = future.get();
            
            if (response.statusCode() == 200) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response.body());
                return ((JSONObject)jsonObject.get("content")).get("chatChannelId").toString();
            } else {
                //throw new DoneException(ExceptionCode.API_CHAT_CHANNEL_ID_ERROR);
            	return null;
            }
        } catch (Exception e) {
            //throw new DoneException(ExceptionCode.API_CHAT_CHANNEL_ID_ERROR);
            return null;
        }
    }

    // TODO: Cookie를 이용한 API 호출
    public static String getAccessToken(String chatChannelId) {
        String requestURL = "https://comm-api.game.naver.com/nng_main/v1/chats/access-token?channelId=" + chatChannelId + "&chatType=STREAMING";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create(requestURL))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .build(); // HttpRequest 생성

            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = future.get();
            
            if (response.statusCode() == 200) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response.body());
                String accessToken = ((JSONObject)jsonObject.get("content")).get("accessToken").toString();
                String extraToken = ((JSONObject)jsonObject.get("content")).get("extraToken").toString();
                return accessToken + ";" + extraToken;
            } else {
                //throw new DoneException(ExceptionCode.API_ACCESS_TOKEN_ERROR);
                return null;
            }
        } catch (Exception e) {
            //throw new DoneException(ExceptionCode.API_ACCESS_TOKEN_ERROR);
        	return null;
        }

    }
    
    public static boolean isOnline(String id) {
    	String requestURL = "https://api.chzzk.naver.com/polling/v2/channels/" + id + "/live-status";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create(requestURL))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .build(); // HttpRequest 생성

            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = future.get();
            
            if (response.statusCode() == 200) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response.body());
                String status = ((JSONObject)jsonObject.get("content")).get("status").toString();
                if(status.equalsIgnoreCase("OPEN")) {
                	return true;
                }
                else {
                	return false;
                }
            } else {
                throw new DoneException(ExceptionCode.API_CHAT_CHANNEL_ID_ERROR);
            }
        } catch (Exception e) {
            throw new DoneException(ExceptionCode.API_CHAT_CHANNEL_ID_ERROR);
        }
    }

}
