package com.eebbk.surfaceviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class FrameAnimSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = "DEVELOP-LZH";
    private SurfaceHolder mSurfaceHolder = null;
    private Canvas mCanvas = null;
    private long mFrameIntervals = 80;
    private int currentIndex = 0;
    private Context mContext;
    private int frameTotal;
    private Timer mTimer;
    private Bitmap mBitmap;
    private boolean isDestroy = false;
    private boolean isRepeat = true;
    private int[] mBitmapResourceIds;
//    private int[] mBitmapResourceIds = {
//            R.mipmap.word_detail_recording_1,
//            R.mipmap.word_detail_recording_2,
//            R.mipmap.word_detail_recording_3,
//            R.mipmap.word_detail_recording_4,
//            R.mipmap.word_detail_recording_5,
//            R.mipmap.word_detail_recording_6,
//            R.mipmap.word_detail_recording_7,
//            R.mipmap.word_detail_recording_8,
//            R.mipmap.word_detail_recording_9,
//            R.mipmap.word_detail_recording_10,
//            R.mipmap.word_detail_recording_11,
//            R.mipmap.word_detail_recording_12,
//            R.mipmap.word_detail_recording_13,
//            R.mipmap.word_detail_recording_14,
//            R.mipmap.word_detail_recording_15,
//            R.mipmap.word_detail_recording_16
//    };

    public FrameAnimSurfaceView(Context context) {
        this(context, null);
        Log.d(TAG, "constructor1");
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        setZOrderOnTop(true);
//        setZOrderMediaOverlay(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    public FrameAnimSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameAnimSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        mContext = context;
        this.setOnClickListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated");
        if (isDestroy || mBitmapResourceIds != null) {
            isDestroy = false;
            setZOrderOnTop(true);
            start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDestroy = true;
        stop();
        setZOrderOnTop(false);
        Log.d(TAG, "surfaceDestroyed");
    }


    private void drawing() {
//        Log.d(TAG,"drawing");
        if (mBitmapResourceIds == null || mBitmapResourceIds.length == 0) {
            return;
        }
        if (mSurfaceHolder != null) {
            mCanvas = mSurfaceHolder.lockCanvas();
        }
        if (mCanvas != null && mSurfaceHolder != null) {
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mBitmap = BitmapFactory.decodeResource(getResources(), mBitmapResourceIds[currentIndex++]);
//            Paint paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setStyle(Paint.Style.STROKE);
//            Rect mSrcRect, mDestRect;
//            mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//            mDestRect = new Rect(0, 0, getWidth(), getHeight());
//            mCanvas.drawBitmap(mBitmap, mSrcRect, mDestRect, paint);
//            mCanvas.drawBitmap(mBitmap, getWidth() / 2, getHeight() / 2, null);
            mCanvas.drawBitmap(mBitmap,0,0,null);
            if (currentIndex == mBitmapResourceIds.length) {
                currentIndex = 0;
                if (!isRepeat) {
                    stop();
                }
            }
        }
        if (mCanvas != null) {
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

    public void setDuration(int duration) {
        mFrameIntervals = duration;
    }

    public void start() {
        if (!isDestroy) {
            currentIndex = 0;
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    drawing();
                }
            }, 0, mFrameIntervals);
        } else {
            Log.d(TAG, "Destroyed");
        }
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void restart() {
        if (!isDestroy) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    drawing();
                }
            }, 0, mFrameIntervals);
        }
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }


    @Override
    public void onClick(View view) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        } else {
            restart();
        }
    }

    private float getScale(float width, float height) {
        float scaleX = getWidth() / width;
        float scaleY = getHeight() / height;
        float scale;
        if (scaleX <= 1) {
            if (scaleY <= 1) {
                scale = scaleX > scaleY ? scaleY : scaleX;
            } else {
                scale = scaleX;
            }
        } else {
            if (scaleY <= 1) {
                scale = scaleY;
            } else {
                scale = scaleX > scaleY ? scaleX : scaleY;
            }
        }
        return scale;
    }

    public void setResourceIds(String commonPrefix, int frameTotal) {
        this.frameTotal = frameTotal;
        mBitmapResourceIds = new int[frameTotal];
        for (int i = 0; i < frameTotal; i++) {
            String name = commonPrefix + "_" + (i + 1);
            mBitmapResourceIds[i] = mContext.getResources()
                    .getIdentifier(name, "mipmap", mContext.getPackageName());
        }
    }

}
