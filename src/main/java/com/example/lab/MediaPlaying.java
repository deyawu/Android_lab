package com.example.lab;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Inherited;
import java.util.Date;

/*
 参考自 https://github.com/shaoshuai904/SoundMeter
 */

public class MediaPlaying extends Thread{
    private static final int HEIGHT = Short.MAX_VALUE;       /* 正弦波高度及2*PI */
    private static final double Frequency = 20000;   /* 正弦声波频率 */
    private static final int RATE = 48000;
    private AudioTrack audioTrack;
    private int length;    /* 一个正弦波的长度 */
    private short[] wave; /* 正弦波 */

    private AppCompatActivity activity;
    private volatile boolean endPlaying  = false;

    public MediaPlaying(AppCompatActivity activity) {
        this.activity = activity;
        wave = new short[RATE];
        length = AudioTrack.getMinBufferSize(RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,// 类型
                RATE,// 频率
                AudioFormat.CHANNEL_CONFIGURATION_MONO, // 单声道
                AudioFormat.ENCODING_PCM_16BIT, // 位数
                length * 2,// 缓冲区
                AudioTrack.MODE_STATIC// 使用循环模式
        );
        audioTrack.setStereoVolume(10, 10); // 设置音量
    }
    @Override
    public void run() {
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

    private static short[] GetSinWave(short[] wave, int waveLen, int length) {
        for (int i = 0; i < length; i++) {
            /* wave[i] = (short) (HEIGHT * (Math.sin(2 * Math.PI
                    * ((Frequency / length * i)))));
            */
            wave[i] = (short)(HEIGHT * (1 - Math.sin(2 * Math.PI
                    * (i % waveLen) * 1.00 / waveLen)));
        }
        return wave;
    }

    private void startSending() {
            int waveLen = RATE / (int)Frequency;
            wave = GetSinWave(wave, waveLen, RATE);

            if(audioTrack != null) {
                audioTrack.write(wave, 0, wave.length);
                audioTrack.setLoopPoints(0, Build.VERSION_CODES.M, -1); // 循环播放
                audioTrack.play();
            }
            else {
                Log.i("NULL", "audioTrack is null !");
            }
    }

    private void fileSaving() {
        try {
            String filename = "/out" + new Date().getTime() + ".pcm";
            File file = new File(Environment.getExternalStorageDirectory() + filename);
            file.createNewFile();
            if(file == null) {
                Log.i("filenull", "createNewFile failed !");
            }
            else {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(file, true));
                Log.i("filepath", "音频文件路径" + file.getAbsolutePath());
                for(int i = 0;i < wave.length;i ++) {
                    out.write(wave[i]);
                    out.write(' ');
                }
                out.flush();
                out.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}