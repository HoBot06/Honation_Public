package com.ho_bot.tonation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ho_bot.exception.DoneException;
import com.ho_bot.exception.ExceptionCode;

public class TonationApi {
	
    public static String getPayLoad(String chatChannelId) {
    	String requestURL = "https://toon.at/widget/alertbox/" + chatChannelId;
    	
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(requestURL))
                    .build();
            
            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = future.get();
            
            String script = response.body();
            String payload = parsePayloadFromHtml(script);
            return payload;
        } catch (Exception e) {
            throw new DoneException(ExceptionCode.API_ACCESS_TOKEN_ERROR);
        }

    }
    
    private static String parsePayloadFromHtml(String html) {
    	Pattern p = Pattern.compile("\"payload\":\"(.*)\",");
        Matcher m = p.matcher(html);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

}
