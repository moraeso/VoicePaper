package com.example.voicepaper.data;

public class Voice {
    private int id;
    private int userId;
    private String userName;
    private int roomId;
    private String voiceFile;

    public Voice(int id, int userId, String userName, int roomId, String voiceFile) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.roomId = roomId;
        this.voiceFile = voiceFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getVoiceFile() {
        return voiceFile;
    }

    public void setVoiceFile(String voiceFile) {
        this.voiceFile = voiceFile;
    }
}