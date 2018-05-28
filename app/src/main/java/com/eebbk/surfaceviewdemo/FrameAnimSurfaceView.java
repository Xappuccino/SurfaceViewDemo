package com.eebbk.surfaceviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class FrameAnimSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = "DEVELOP-LZH";
    private SurfaceHolder mSurfaceHolder = null;
    private Canvas mCanvas = null;
    boolean isDrawing = false;
    private Thread mThread = null;
    private long mFrameIntervals = 80;
    private int currentIndex = 0;
    private Context mContext;
    private int frameTotal;
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
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        Log.d(TAG, "init");
    }

    public FrameAnimSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d(TAG, "constructor2");
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
        if (!isDrawing) {
            mThread = new Thread(new DrawRunnable());
            isDrawing = true;
            mThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDrawing = false;
        isDestroy = true;
        Log.d(TAG, "surfaceDestroyed");
    }


    private void drawing() {
//        Log.d(TAG,"drawing");
        if (mBitmapResourceIds == null) {
            isDrawing = false;
            return;
        }
        if (mSurfaceHolder != null) {
            mCanvas = mSurfaceHolder.lockCanvas();
        }
        try {
            if (mSurfaceHolder != null && mCanvas != null) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                Rect mSrcRect, mDestRect;
                mBitmap = BitmapFactory.decodeResource(getResources(), mBitmapResourceIds[currentIndex]);
                mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
                mDestRect = new Rect(0, 0, getWidth(), getHeight());

//                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                mCanvas.drawBitmap(mBitmap, 0, 0, null);
                mCanvas.drawBitmap(mBitmap, mSrcRect, mDestRect, paint);
            }
        } finally {
            currentIndex++;
            if (currentIndex == mBitmapResourceIds.length) {
                currentIndex = 0;
                if (!isRepeat) {
                    isDrawing = false;
                }
            }
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
            if (mBitmap != null) {
                mBitmap.recycle();
            }
        }
    }

    private void setResourceIDs(String commonPrefix, int frameTotal) {
        this.frameTotal = frameTotal;
        mBitmapResourceIds = new int[frameTotal];
        for (int i = 0; i < frameTotal; i++) {
            String name = commonPrefix + "_" + (i + 1);
            mBitmapResourceIds[i] = mContext.getResources().getIdentifier(name, "mipmap", mContext.getPackageName());
        }
    }

    public void setDuration(int duration) {
        mFrameIntervals = duration;
    }

    public void start() {
        if (!isDestroy) {
            isDrawing = true;
            mThread = new Thread(new DrawRunnable());
            mThread.start();
        }
    }

    public void stop() {
        isDrawing = false;
    }

    public void restart() {
        if (!isDestroy) {
            isDrawing = true;
            mThread = new Thread(new DrawRunnable());
            mThread.start();
        }
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isDrawing = false;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    @Override
    public void onClick(View view) {
        if (isDrawing) {
            stop();
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

    private class DrawRunnable implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "run");
            synchronized (mSurfaceHolder) {
                while (isDrawing) {
                    try {
                        drawing();
                        Thread.sleep(mFrameIntervals);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
