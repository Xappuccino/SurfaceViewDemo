package com.eebbk.surfaceviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private FrameAnimSurfaceView frameAnimSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameAnimSurfaceView = findViewById(R.id.animSurface);
//        imageView = findViewById(R.id.animation_tv);
//        imageView.setImageResource(R.drawable.word_detail_recording);
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
//        animationDrawable.start();
//        frameAnimSurfaceView = findViewById(R.id.animSurface);
//        frameAnimSurfaceView.setRe
        frameAnimSurfaceView.setDuration(80);
        frameAnimSurfaceView.setRepeat(true);
        frameAnimSurfaceView.setResourceIds("word_detail_recording", 16);
        frameAnimSurfaceView.start();
        Log.d("DEVELOP-LZH", "onCreate");
    }
}
