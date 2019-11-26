package com.example.voicepaper.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.RoomSlidePagerAdapter;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.data.User;
import com.example.voicepaper.fragment.main.CreateRoomFragment;
import com.example.voicepaper.fragment.main.InputRoomCodeFragment;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.ConfirmDialog;
import com.example.voicepaper.util.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, DialogInterface.OnDismissListener {

    private CreateRoomFragment createRoomFragment;
    private InputRoomCodeFragment inputRoomCodeFragment;

    private ViewPager roomPager;
    private RoomSlidePagerAdapter roomPagerAdapter;
    private Button createRoomBtn, inputRoomCodeBtn;

    private ConfirmDialog confirmDialog;
    private int selectedRoomPos;

    private final static int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_main);

        ///////////////// 임시 유저 ////////////////////////
        AppManager.getInstance().getUser().setID("testID");
        ////////////////////////////////////////////////////

        initView();
        initListener();
        initMyRoomList();
        initRoomPagerAdapter();


        // 앨범 접근 허용(나중에 옮기기)
        checkPermission();
    }

    void initView() {
        roomPager = (ViewPager) findViewById(R.id.roomViewPager);

        createRoomBtn = (Button) findViewById(R.id.btn_createRoom);
        inputRoomCodeBtn = (Button) findViewById(R.id.btn_inputRoomCode);

        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext()); }

    void initListener() {
        createRoomBtn.setOnClickListener(this);
        inputRoomCodeBtn.setOnClickListener(this);
        confirmDialog.getOkBtn().setOnClickListener(this);

    }

    void initMyRoomList() {
        ArrayList<Room> rooms = new ArrayList<>();

        Drawable drawable = getResources().getDrawable(R.drawable.ic_user_main);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        rooms.add(new Room(1001, "1Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC,
                "동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리 나라 만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세.",
                AppManager.getInstance().getUser().getID(), "456"));

        rooms.add(new Room(1001, "2Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        rooms.add(new Room(1001, "3Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        rooms.add(new Room(1001, "4Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        rooms.add(new Room(1001, "5Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        rooms.add(new Room(1001, "6Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        rooms.add(new Room(1001, "7Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        rooms.add(new Room(1001, "8Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        rooms.add(new Room(1001, "9Room", bitmap, "abc.url",
                Constants.VOICE_PUBLIC, "ㅂㅇ", AppManager.getInstance().getUser().getID(), "123"));

        AppManager.getInstance().setRoomList(rooms);
    }


    void initRoomPagerAdapter() {
        roomPagerAdapter = new RoomSlidePagerAdapter(getSupportFragmentManager());
        setRoomPagerAdapter();
        roomPager.setAdapter(roomPagerAdapter);

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createRoom:
                createRoomFragment = CreateRoomFragment.newInstance();
                createRoomFragment.show(getSupportFragmentManager(), "CreateRoom");
                getSupportFragmentManager().executePendingTransactions();
                break;
            case R.id.btn_inputRoomCode:
                inputRoomCodeFragment = InputRoomCodeFragment.newInstance();
                inputRoomCodeFragment.show(getSupportFragmentManager(), "InputRoomCode");
                getSupportFragmentManager().executePendingTransactions();
                break;
            case R.id.btn_setting:
                break;
            case R.id.iv_roomProfile1:
                enterRoomAt(roomPager.getCurrentItem() * 4);
                break;
            case R.id.iv_roomProfile2:
                enterRoomAt(roomPager.getCurrentItem() * 4 + 1);
                break;
            case R.id.iv_roomProfile3:
                enterRoomAt(roomPager.getCurrentItem() * 4 + 2);
                break;
            case R.id.iv_roomProfile4:
                enterRoomAt(roomPager.getCurrentItem() * 4 + 3);
                break;
            case R.id.btn_ok_dialog:
                deleteRoomAt(selectedRoomPos);
                confirmDialog.dismiss();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_roomProfile1:
                selectedRoomPos = roomPager.getCurrentItem() * 4;
                showDeleteRoomDialog();
                break;
            case R.id.iv_roomProfile2:
                selectedRoomPos = roomPager.getCurrentItem() * 4 + 1;
                showDeleteRoomDialog();
                break;
            case R.id.iv_roomProfile3:
                selectedRoomPos = roomPager.getCurrentItem() * 4 + 2;
                showDeleteRoomDialog();
                break;
            case R.id.iv_roomProfile4:
                selectedRoomPos = roomPager.getCurrentItem() * 4 + 3;
                showDeleteRoomDialog();
                break;
        }

        return true; // 이벤트 계속 진행 false, 이벤트 완료 true
    }

    // CreateRoomFragment에서 앨범에서 사진 받아왔을 때
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    //갤러리 접근 권한 설정
    private void checkPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

            Log.v("갤러리 권한", "갤러리 사용을 위해 권한이 필요합니다.");
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d("sssong:MainActivity", "===============");
        Log.d("sssong:MainActivity", "dismiss event");
        // 다시 set
        setRoomPagerAdapter();
    }

    private void setRoomPagerAdapter() {

        ArrayList<Room> rooms = AppManager.getInstance().getRoomList();

        roomPagerAdapter.setRoomList(rooms);
        roomPagerAdapter.setMaxPages((rooms.size() - 1) / 4 + 1);

        roomPagerAdapter.initFragments();

        for (int i=0; i<roomPagerAdapter.getCount(); i++) {
            roomPagerAdapter.addItem(i);
        }

        roomPagerAdapter.notifyDataSetChanged();
    }

    private void enterRoomAt(int pos) {
        //Toast.makeText(AppManager.getInstance().getContext(),
        //        "Enter Room : " + AppManager.getInstance().getRoomList().get(pos).getName(),
        //        Toast.LENGTH_SHORT).show();

        //아이디 비밀번호를 받아와 서버와 통신
        Intent intent = new Intent(this, RoomActivity.class);

        intent.putExtra("code", AppManager.getInstance().getRoomList().get(pos).getCode());

        startActivity(intent);

    }

    private void showDeleteRoomDialog() {
        confirmDialog.setMessage("방을 삭제하시겠습니까?\n"
                + "[" + AppManager.getInstance().getRoomList().get(selectedRoomPos).getTitle() + "]");
        confirmDialog.show();
    }

    private void deleteRoomAt(int pos) {
        Log.d("sssong:MainActivity", "delete room number : " + pos);
        AppManager.getInstance().getRoomList().remove(pos);
        setRoomPagerAdapter();
    }
}