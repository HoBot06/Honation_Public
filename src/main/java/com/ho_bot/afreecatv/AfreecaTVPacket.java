package com.ho_bot.afreecatv;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AfreecaTVPacket {
	
	private final String command;
    private final List<String> dataList;

    private final LocalDateTime receivedTime = LocalDateTime.now();

    public AfreecaTVPacket(String[] args) {
        this.dataList = new ArrayList<>(Arrays.asList(args));
        String cmd = dataList.remove(0);
        this.command = cmd.substring(0, 4);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Command: ").append(command).append("\n");
        sb.append("Data: ");
        for (String d : dataList) {
            sb.append(d).append(" ");
        }
        return sb.toString();
    }

	public String getCommand() {
		return command;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public LocalDateTime getReceivedTime() {
		return receivedTime;
	}

}
