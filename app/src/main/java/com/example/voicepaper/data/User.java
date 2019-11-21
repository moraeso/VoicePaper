package com.example.voicepaper.data;

import android.graphics.Bitmap;

public class User {
    private String token; // 유저 token을 저장함.

    private String id; // 유저 id
    private String pw;    //유저 비밀번호, 비밀번호 변경을 위해 가지고 있음.
    private String name;  //유저 이름
    private String profileString; //profile image의 uri를 string 으로 저장함
    private Bitmap profileImage; //profile bitmap image를 저장함. profileString의 값이 null이 아니면, 유저 이미지를 가져와 bitmap으로 저장함.

    public User() {}

    //private Room class list를 가질 필요있음.

    public User(String token, String id, String name, String profileString, String pw){
        this.token = token;

        this.id = id;
        this.pw = pw;
        this.name = name;

        this.profileString = profileString;
        //로그인 성공시 user객체를 생성함.
        //추가 해야하는것. room class list
    }

    /*
    --------------------getter, setter --------------------
     */
    public String getID() { return id; }
    public void setID(String id) { this.id = id; }

    public String getPw(){ return pw;}
    public void setPw(String pw){ this.pw = pw;}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfileString() { return profileString; }
    public void setProfileString(String profileString) { this.profileString = profileString; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Bitmap getProfileImage(){ return profileImage; }
    public void setProfileImage(Bitmap profileImage){ this.profileImage = profileImage; }
}