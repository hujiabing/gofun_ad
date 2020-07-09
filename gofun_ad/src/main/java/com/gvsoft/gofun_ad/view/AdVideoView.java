package com.gvsoft.gofun_ad.view;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gvsoft.gofun_ad.inter.Callback;
import com.gvsoft.gofun_ad.inter.OnAdShowCallback;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.tread.ThreadPoolUtil;
import com.gvsoft.gofun_ad.util.DownloadUtils;

import java.io.File;

public class AdVideoView extends SurfaceView implements MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener {
    private SurfaceHolder mSurfaceHolder;//SurfaceHolder
    private MediaPlayer mMediaPlayer;//媒体播放器

    private int mVideoHeight;//视频宽高
    private int mVideoWidth;//视频高
    private int mCurrentPos;//当前进度
    private ThreadPoolUtil mThreadPool;
    private Runnable playRunnable;
    private Runnable rePlayRunnable;
    private File currentFile;

    private boolean isRelease = false;

    public AdVideoView(Context context) {
        this(context, null);
    }

    public AdVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mThreadPool = ThreadPoolUtil.defaultInstance();
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mSurfaceHolder = this.getHolder();
        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaPlayer.setDisplay(mSurfaceHolder);//把视频画面输出到SurfaceView中
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                holder.setFixedSize(width, height);

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    public void closeVolume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(0, 0);
        }
    }

    public void openVolume() {
        if (mMediaPlayer != null) {
            AudioManager audioManager = (AudioManager) getContext().getSystemService(Service.AUDIO_SERVICE);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
            mMediaPlayer.setVolume(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
            mMediaPlayer.start();
        }

    }

    private void rePlay(String url) {
        rePlayRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mMediaPlayer.reset();//重置MediaPlayer
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    mMediaPlayer.setLooping(false);
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                                @Override
                                public void onSeekComplete(MediaPlayer mp) {
                                    // seekTo 方法完成时的回调
                                    mp.start();
                                }
                            });
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    mp.seekTo(mCurrentPos);
                                }
                            });
                        }
                    });

                } catch (Exception e) {

                }
            }
        };
        mThreadPool.execute(rePlayRunnable);
    }

    private void play(String url) {
        playRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mMediaPlayer.reset();//重置MediaPlayer
//                    mMediaPlayer.setDisplay(mSurfaceHolder);//把视频画面输出到SurfaceView中
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    mMediaPlayer.setLooping(false);
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    mp.start();
                                }
                            });
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mThreadPool.execute(playRunnable);

    }

    public void stopPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mCurrentPos = mMediaPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int w = adjustSize(mVideoWidth, widthMeasureSpec);
//        int h = adjustSize(mVideoHeight, heightMeasureSpec);
//        setMeasuredDimension(w, h);
        if (-1 == mWidth || -1 == mHeight) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(mWidth, mHeight);
        }
    }

    private int mWidth = -1;
    private int mHeight = -1;


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mVideoWidth = mp.getVideoWidth();
        mVideoHeight = mp.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
        }
    }


    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (rePlayRunnable != null) {
            mThreadPool.removeRunnable(rePlayRunnable);
            rePlayRunnable = null;
        }
        if (playRunnable != null) {
            mThreadPool.removeRunnable(playRunnable);
            playRunnable = null;
        }
    }

    private AdData data;

    public void load(Context context, AdData data, OnAdShowCallback callback) {
        this.data = data;
        if (data == null) return;
        DownloadUtils.load(context.getApplicationContext(), data, new Callback() {
            @Override
            public void onFailure(AdData data) {

            }

            @Override
            public void onResponse(AdData data, File file) {
                if (isRelease) return;
                currentFile = file;
                play(file.getAbsolutePath());
                if (callback != null) {
                    callback.onAdShow();
                }
            }
        });
    }

    public void load(Context context, AdData data) {
        load(context, data, null);
    }

    public void reLoad(Context context) {
        if (data == null) {
            return;
        }
        if (currentFile != null && currentFile.exists()) {
            long length = currentFile.length();
            if (length > 0) {
                rePlay(currentFile.getAbsolutePath());
                return;
            }
        } else {
            load(context, data);
        }
    }
}
