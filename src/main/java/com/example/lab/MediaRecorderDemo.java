package com.example.lab;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MediaRecorderDemo extends Thread{
    private final String TAG = "MediaRecord";
    private MediaRecorder mMediaRecorder;
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;
    private String filePath;
    private AppCompatActivity activity;
    public MediaRecorderDemo(){
        //this.filePath = "/dev/null";

    }

    public MediaRecorderDemo(File file) {
        this.filePath = file.getAbsolutePath();
    }

    public MediaRecorderDemo(AppCompatActivity activity) {
        this.activity = activity;
        String filename = "test.amr";
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        this.filePath = file.getPath();
        Log.i("filepath", filePath.toString());
    }

    private long startTime;
    private long endTime;

    /**
     * 开始录音 使用amr格式
     *
     *            录音文件
     * @return
     */
    @Override
    public void run() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.i("ACTION_START", "startTime" + startTime);
        } catch (IllegalStateException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"
                            + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"
                            + e.getMessage());
        }
    }

    /**
     * 停止录音
     *
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();
        Log.i("ACTION_END", "endTime" + endTime);
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        Log.i("ACTION_LENGTH", "Time" + (endTime - startTime));
        return endTime - startTime;
    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    /**
     * 更新话筒状态
     *
     */
    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double)mMediaRecorder.getMaxAmplitude() /BASE;
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            Log.i(TAG,"分贝值："+db);
            if(activity != null) {
                TextView result = (TextView) activity.findViewById(R.id.result);
                result.setText(Double.toString(db));
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }
}