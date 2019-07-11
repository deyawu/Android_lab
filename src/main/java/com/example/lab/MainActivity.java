package com.example.lab;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaPlaying mediaPlaying;
    private AudioRecorderManager audioRecorderManager;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial_App();
    }

    private void initial_App() {
        result = (TextView)findViewById(R.id.result);
    }

    public void is_existing(String filename) {
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        if (file.exists()) {
            Log.i("file_ex", "file is already existing!");
        }
    }

    public void Start_collecting(View view) {
        mediaPlaying = new MediaPlaying(this);
        mediaPlaying.start();
        audioRecorderManager = new AudioRecorderManager();
        audioRecorderManager.start();

        result.setText("START RECORDING");
    }

    public void End_collecting(View view) {
        mediaPlaying.setEndPlaying();
        audioRecorderManager.setEndcollecting();

        result.setText("END RECORDING");
        Log.i("END！","end for collecting!");
    }
}