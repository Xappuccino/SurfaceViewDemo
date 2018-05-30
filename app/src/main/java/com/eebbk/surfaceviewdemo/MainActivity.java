package com.eebbk.surfaceviewdemo;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FrameAnimSurfaceView frameAnimSurfaceView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameAnimSurfaceView = findViewById(R.id.animSurface);
        imageView = findViewById(R.id.animation_tv);
        imageView.setImageResource(R.drawable.word_detail_recording);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
//        frameAnimSurfaceView = findViewById(R.id.animSurface);
//        frameAnimSurfaceView.setRe
        frameAnimSurfaceView.setDuration(100);
        frameAnimSurfaceView.setRepeat(true);
        frameAnimSurfaceView.setResourceIds("word_detail_recording", 16);
//        frameAnimSurfaceView.start();
        Log.d("DEVELOP-LZH", "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEVELOP-LZH","onDestroy");
    }
}
