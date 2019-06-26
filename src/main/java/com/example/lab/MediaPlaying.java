package com.example.lab;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

import java.lang.annotation.Inherited;

public class MediaPlaying extends Thread{
    private AppCompatActivity activity;
    public MediaPlaying(AppCompatActivity activity) {
        this.activity = activity;
    }
    @Override
    public void run() {
        MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.test);
        mediaPlayer.start();
    }
}
