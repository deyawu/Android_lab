package com.example.lab;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class AudioRecorderManager extends Thread{
    private volatile boolean endcollecting = false;
    private static final int SampleRate =  48000;
    public static final String TAG = "AudioRecorderManager";
    private AudioRecord audioRecord;
    private DataOutputStream out;
    private int buffersize;

    public AudioRecorderManager() {
        buffersize = AudioRecord.getMinBufferSize(SampleRate,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SampleRate,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize);
        try {
            String filename = "test" + new Date().getTime();
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            file.createNewFile();
            if(file == null) {
                Log.i("filenull", "createNewFile failed !");
            }
            else {
                out = new DataOutputStream(new FileOutputStream(file, true));
                Log.i("filepath", "音频文件路径" + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while(!endcollecting) {
            startRecording();
        }
        Log.i("END", "ready to stoprecording");
        stopRecording();
    }
    public void setEndcollecting() {
        endcollecting = true;
    }
    private void startRecording() {
        int bytesRecord  = 0;
        byte[] tempBuffer = new byte[buffersize];
        if(audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            return;
        }
        audioRecord.startRecording();   // 开始录音
        bytesRecord = audioRecord.read(tempBuffer, 0, buffersize);
        if(bytesRecord == AudioRecord.ERROR_INVALID_OPERATION 
                || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
            // TODO: 2019/6/26
        }
        if(bytesRecord != 0 && bytesRecord != -1) {
            // 可以将pcm音频写入文件或者进行处理
            try {
                out.write(tempBuffer, 0, bytesRecord);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording() {
        try {
            if(audioRecord != null) {
                if(audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                    audioRecord.stop();
                    audioRecord.release();
                }
            }
            if(out != null) {
                out.flush();
                out.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
