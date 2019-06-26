package com.example.lab;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaPlaying mediaPlaying;
    private AudioRecorderManager audioRecorderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // new MediaPlaying(this).start();
//        filetest();
//        is_existing("test.txt");
    }

    public void filetest() {
        String filename = "test.txt";
        final String EXISTS = "file_exists";
        try {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            if (file.exists()) {
                Log.i(EXISTS, "file is already existing!");
                file.delete();
            }
            Log.i(EXISTS, "new file\n");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(new String("just for test").getBytes());
            out.flush();
            out.close();
            Log.i("file_path", file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }

    public void End_collecting(View view) {
        mediaPlaying.setEndPlaying();
        audioRecorderManager.setEndcollecting();
        Log.i("END！","end for collecting!");
    }
}