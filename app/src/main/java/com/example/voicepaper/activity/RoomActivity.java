package com.example.voicepaper.activity;

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
import com.example.voicepaper.adapter.VoiceRecyclerViewDecoration;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.data.Voice;
import com.example.voicepaper.fragment.room.RecordFragment;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.VoiceListTask;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView roomProfileIv;
    private TextView roomTitleTv, roomCommentTv;
    private Button recordBtn;

    private SwipeRefreshLayout swipeRefresh;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView voiceRecycleView;
    private VoiceRecycleViewerAdapter voiceAdapter;

    private Room room;

    private String code;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_room);

        initView();
        initListener();
        getRoomInfo();
        initVoiceRecyclerViewAdapter();
        loadVoiceData();

        setSwipeRefresh();
    }

    private void setSwipeRefresh() {
        // 새로고침
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.layout_swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);

                        /*
                        여기서 새로고침 시 기능 추가

                        */

                        voiceRecycleView.smoothScrollToPosition(0);
                    }
                }, 1000);
            }
        });
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

    private void initView() {
        roomProfileIv = (ImageView) findViewById(R.id.iv_roomProfile);
        roomTitleTv = (TextView) findViewById(R.id.tv_roomTitle);
        roomCommentTv = (TextView) findViewById(R.id.tv_roomComment);
        recordBtn = (Button) findViewById(R.id.btn_record);
    }

    private void initListener() {
        recordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_record:
                /*
                녹음 기능
                 */
                RecordFragment recordFragment = RecordFragment.newInstance();
                recordFragment.setRoomId(room.getId());

                recordFragment.show(getSupportFragmentManager(),null);

                break;
        }
    }

    private void getRoomInfo() {
        code = getIntent().getExtras().getString("code");
        Toast.makeText(this, "Room Code : " + code, Toast.LENGTH_SHORT).show();

        ArrayList<Room> roomItems = AppManager.getInstance().getRoomList();
        for (Room item : roomItems) {
            if (code.equals(item.getCode())) {
                room = item;
                break;
            }
        }

        roomProfileIv.setImageBitmap(room.getProfileImage());
        roomTitleTv.setText(room.getTitle());
        roomCommentTv.setText(room.getComment());
    }

    private void loadVoiceData() {
    /*

    보이스 데이터 불러오기 통신

     */
        ContentValues values = new ContentValues();
        values.put("roomID",room.getId());

        VoiceListTask voiceListTask = new VoiceListTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                voiceAdapter.addAll(((ArrayList<Voice>)object));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        voiceListTask.execute();
//        ArrayList<Voice> bufferItems = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            // Voice(int id, int userId, String userName, int roomId, String voiceFile) {
//            bufferItems.add(new Voice(1, 1, "user00", room.getId(), "voiceFile.url"));
//        }
//        voiceAdapter.addAll(bufferItems);
    }
}