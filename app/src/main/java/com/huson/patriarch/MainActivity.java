package com.huson.patriarch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.img_main)
    ImageView imgMain;
    @Bind(R.id.ll_map)
    LinearLayout llMap;
    @Bind(R.id.ll_leave)
    LinearLayout llLeave;
    @Bind(R.id.ll_video)
    LinearLayout llVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_main, R.id.ll_map, R.id.ll_leave, R.id.ll_video})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {

            case R.id.img_main:
                break;
            case R.id.ll_map:
                intent.setClass(MainActivity.this, PositionActivity.class);
                break;
            case R.id.ll_leave:
                break;
            case R.id.ll_video:
                intent.setClass(MainActivity.this, VideoActivity.class);
                break;
        }
        startActivity(intent);
    }
}
