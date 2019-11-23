package com.example.voicepaper.data;

import android.graphics.Bitmap;

public class Room {
    // Room(id, name, profileString, comment, hostID, [RoomMember<List>])

    private int id;
    private String name;
    private Bitmap profileImage;
    private String profileString;
    private String comment;
    private int permission;
    private String hostID;
    // private ArrayList<RoomMember> roomMemberItems;

    public Room(int id, String name, Bitmap profileImage, String profileString, int permission, String comment, String hostID) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.profileString = profileString;
        this.permission = permission;
        this.comment = comment;
        this.hostID = hostID;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}