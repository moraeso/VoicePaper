package com.example.voicepaper.manager;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.voicepaper.R;
import com.example.voicepaper.util.Constants;

public class GlideManager {

    private static GlideManager instance;

    private GlideManager() {}

    public static GlideManager getInstance() {
        if (instance == null) return instance = new GlideManager();
        return instance;
    }

    public void GlideInto(Context context, ImageView iv, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.img_user) // loading img
                .error(R.drawable.img_user) // error img
                .into(iv);
    }

    public String getFullImageString(String img_str, String type_str) {
        String buf[] = img_str.split("/");
        return Constants.URL + "/" + type_str + "/" + buf[2];
    }
}
