package com.example.voicepaper.manager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.voicepaper.R;
import com.example.voicepaper.util.Constants;

public class ImageManager {

    public static final int PICK_FROM_ALBUM = 1;

    private static ImageManager instance;

    private ImageManager() {}

    public static ImageManager getInstance() {
        if (instance == null) return instance = new ImageManager();
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        Log.d("sssong:getPathFromURI", cursor.getString(column_index));
        return cursor.getString(column_index);
    }

    public Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    // 이미지 각도 조절
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
