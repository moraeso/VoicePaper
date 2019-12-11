package com.example.voicepaper.activity;

import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.VoiceRecycleViewerAdapter;
import com.example.voicepaper.adapter.Decoration.VoiceRecyclerViewDecoration;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.data.RoomMember;
import com.example.voicepaper.data.Voice;
import com.example.voicepaper.fragment.room.RecordFragment;
import com.example.voicepaper.fragment.room.RoomMemberFragment;
import com.example.voicepaper.fragment.room.RoomSettingFragment;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.manager.ImageManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.RoomMemberTask;
import com.example.voicepaper.network.RoomTask;
import com.example.voicepaper.network.VoiceListTask;
import com.example.voicepaper.util.ConfirmDialog;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener {

    private RoomSettingFragment roomSettingFragment;

    public ImageView roomProfileIv;
    private TextView roomTitleTv, roomCommentTv;
    private Button recordBtn, memberBtn, settingBtn;

    private SwipeRefreshLayout swipeRefresh;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView voiceRecycleView;
    private VoiceRecycleViewerAdapter voiceAdapter;

    private ArrayList<RoomMember> roomMemberList;
    private Room room;
    private int id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_room);

        id = getIntent().getExtras().getInt("id");

        initView();
        initListener();

        initVoiceRecyclerViewAdapter();

        loadRoomMemberList();
        loadRoomInfo();
        loadVoiceData();

        initSwipeRefresh();
    }

    private void initSwipeRefresh() {
        // 새로고침
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.layout_swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);

                        loadRoomMemberList();
                        loadVoiceData();
                        loadRoomInfo();
                        voiceRecycleView.smoothScrollToPosition(0);
                    }
                }, 1000);
            }
        });
    }

    private void initView() {
        roomProfileIv = (ImageView) findViewById(R.id.iv_roomProfile);
        roomTitleTv = (TextView) findViewById(R.id.tv_roomTitle);
        roomCommentTv = (TextView) findViewById(R.id.tv_roomComment);
        recordBtn = (Button) findViewById(R.id.btn_record);
        memberBtn = (Button) findViewById(R.id.btn_member);
        settingBtn = (Button) findViewById(R.id.btn_setting);
    }

    private void initListener() {
        recordBtn.setOnClickListener(this);
        memberBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
    }

    private void initVoiceRecyclerViewAdapter() {
        // RecycleView 생성 및 사이즈 고정
        voiceRecycleView = (RecyclerView) findViewById(R.id.rv_voiceList);
        voiceRecycleView.setHasFixedSize(true);

        // Grid 레이아웃 적용
        layoutManager = new GridLayoutManager(AppManager.getInstance().getContext(), 2);
        voiceRecycleView.setLayoutManager(layoutManager);
        voiceRecycleView.addItemDecoration(new VoiceRecyclerViewDecoration((RoomActivity) this));

        // 어뎁터 연결
        voiceAdapter = new VoiceRecycleViewerAdapter();
        voiceRecycleView.setAdapter(voiceAdapter);
    }

    private void initViewContents() {

        roomTitleTv.setText(room.getTitle());
        roomCommentTv.setText(room.getComment());

        if (room.getProfileString().equals("undefined") ||
                room.getProfileString().equals("")) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_user_main);
            roomProfileIv.setImageBitmap(((BitmapDrawable) drawable).getBitmap());
        } else {
            String url = ImageManager.getInstance().getFullImageString(room.getProfileString(), "groupImage");
            ImageManager.getInstance().GlideInto(AppManager.getInstance().getContext(), roomProfileIv, url);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:
                /*
                녹음 기능
                 */
                RecordFragment recordFragment = RecordFragment.newInstance();
                recordFragment.setRoomId(room.getId());

                recordFragment.show(getSupportFragmentManager(),null);

                break;

            case R.id.btn_member:
                RoomMemberFragment roomMemberFragment = RoomMemberFragment.newInstance(room, roomMemberList);
                roomMemberFragment.show(getSupportFragmentManager(), null);

                break;

            case R.id.btn_setting:
                if (AppManager.getInstance().getUser().getID().equals(room.getHostID())) {
                    roomSettingFragment = RoomSettingFragment.newInstance(
                            room.getId(), room.getTitle(), room.getComment(),
                            room.getPermission(), room.getProfileString());
                    roomSettingFragment.show(getSupportFragmentManager(), "InputRoomCode");
                    getSupportFragmentManager().executePendingTransactions();
                } else {
                    ConfirmDialog cd = new ConfirmDialog(AppManager.getInstance().getContext());
                    cd.setMessage("호스트만 사용 가능합니다.");
                    cd.show();
                }
        }
    }

    private void loadRoomInfo() {
        ContentValues values = new ContentValues();
        values.put("userID", AppManager.getInstance().getUser().getID());
        values.put("roomID", id);
        RoomTask roomTask = new RoomTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                room = (Room)object;
                voiceAdapter.setPermission(room.getPermission());
                voiceAdapter.isUserHost(AppManager.getInstance().getUser().getID().equals(room.getHostID()));
                initViewContents();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AppManager.getInstance().getContext(),
                        "error : " + e, Toast.LENGTH_SHORT).show();
            }
        });
        roomTask.execute();
    }

    private void loadRoomMemberList() {
        ContentValues values = new ContentValues();
        values.put("userID", AppManager.getInstance().getUser().getID());
        values.put("roomID", id);
        RoomMemberTask roomMemberTask = new RoomMemberTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                roomMemberList = (ArrayList<RoomMember>) object;
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AppManager.getInstance().getContext(),
                        "error : " + e, Toast.LENGTH_SHORT).show();
            }
        });
        roomMemberTask.execute();
    }

    private void loadVoiceData() {
    /*

    보이스 데이터 불러오기 통신

     */
        ContentValues values = new ContentValues();
        values.put("userID", AppManager.getInstance().getUser().getID());
        values.put("roomID", id);

        VoiceListTask voiceListTask = new VoiceListTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                if (object == null)
                    return;
                voiceAdapter.addAll(((ArrayList<Voice>)object));
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RoomActivity.this, "error : " + e, Toast.LENGTH_SHORT).show();
            }
        });
        voiceListTask.execute();
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        loadRoomMemberList();
        loadRoomInfo();
        loadVoiceData();
    }
}