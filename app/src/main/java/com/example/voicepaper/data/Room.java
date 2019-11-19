package com.example.voicepaper.data;

import android.graphics.Bitmap;

public class Room {
    // Room(id, name, profileString, comment, hostID, [RoomMember<List>])

    private int id;
    private String name;
    private Bitmap profileImage;
    private String profileString;
    private String comment;
    private int hostID;
    // private ArrayList<RoomMember> roomMemberItems;

    public Room(int id, String name, Bitmap profileImage, String profileString, String comment, int hostID) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.profileString = profileString;
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

    public int getHostID() {
        return hostID;
    }

    public void setHostID(int hostID) {
        this.hostID = hostID;
    }
}