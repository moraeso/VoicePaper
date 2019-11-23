package com.example.voicepaper.data;

import android.graphics.Bitmap;

public class Room {
    // Room(id, name, profileString, comment, hostID, [RoomMember<List>])

    private int id;
    private String title;
    private Bitmap profileImage;
    private String profileString;
    private String comment;
    private int permission;
    private String hostID;
    private String code;
    // private ArrayList<RoomMember> roomMemberItems;

    public Room(int id, String title, Bitmap profileImage, String profileString, int permission, String comment, String hostID, String code) {
        this.id = id;
        this.title = title;
        this.profileImage = profileImage;
        this.profileString = profileString;
        this.permission = permission;
        this.comment = comment;
        this.hostID = hostID;
        this.code = code;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfileString() {
        return profileString;
    }

    public void setProfileString(String profileString) {
        this.profileString = profileString;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPermission() { return permission; }

    public void setPermission(int permission) { this.permission = permission; }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}