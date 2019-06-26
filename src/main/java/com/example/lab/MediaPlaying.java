package com.example.lab;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.annotation.Inherited;

/*
 参考自 https://github.com/shaoshuai904/SoundMeter
 */

public class MediaPlaying extends Thread{
    private static final int HEIGHT = 127;       /* 正弦波高度及2*PI */
    private static final double TWO_PI = 2 * 3.14;
    private static final int Frequency = 20000;   /* 正弦声波频率 */
    private static final int RATE = 48000;
    private AudioTrack audioTrack;
    private int length, waveLen;    /* 一个正弦波的长度和频率 */
    private byte[] wave; /* 正弦波 */

    private AppCompatActivity activity;
    private volatile boolean endPlaying  = false;

    public MediaPlaying(AppCompatActivity activity) {
        this.activity = activity;
        wave = new byte[RATE];
        waveLen = RATE / Frequency;
        length = waveLen * Frequency;
        audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,// 类型
                RATE,// 频率
                AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_8BIT, // 位数
                length,// 缓冲区
                AudioTrack.MODE_STATIC// 使用循环模式
        );
        audioTrack.setStereoVolume(10, 10); // 设置音量
    }
    @Override
    public void run() {
//        MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.test);
//        mediaPlayer.start();
        startSending();
        while (!endPlaying) {
            // do nothing
        }
        audioTrack.stop();
        audioTrack.flush();
        audioTrack.release();   // 停止发射声波
        Log.i("OUT", Boolean.toString(endPlaying) + "stop audiotracking!!");
    }

    public void setEndPlaying() {
        endPlaying = true;
    }

    private static byte[] GetSinWave(byte[] wave, int waveLen, int length) {
        for (int i = 0; i < length; i++) {
            wave[i] = (byte) (HEIGHT * (1 - Math.sin(TWO_PI
                    * ((i % waveLen) * 1.00 / waveLen))));
        }
        return wave;
    }

    private void startSending() {
            wave = GetSinWave(wave, waveLen, length);
            if(audioTrack != null) {
                audioTrack.write(wave, 0, length);
                audioTrack.setLoopPoints(0, Build.VERSION_CODES.M, -1); // 循环播放
                audioTrack.play();
            }
            else {
                Log.i("NULL", "audioTrack is null !");
            }
    }
}
