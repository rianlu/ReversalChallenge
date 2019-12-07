package com.wzl.reversalchallenge;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;

public class MediaUtil {

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String path;
    private Context mContext;
    private String fileName;
    private String newFileName;
    private Date currentDate;

    // 创建需要传入Context，用于获取Android外部缓存路径
    MediaUtil(Context context) {
        mContext = context;
        path = mContext.getExternalCacheDir().getAbsolutePath() + "/";
    }

    // 开始录音
    void startRecord() {

        // 以时间作为录音文件名
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        currentDate = Calendar.getInstance().getTime();
        fileName = dateFormat.format(currentDate) + ".mp3";
        recorder = new MediaRecorder();
        // 设置音频源为麦克风
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置媒体输出格式
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 设置媒体输出路径
        recorder.setOutputFile(path + fileName);
        // 设置媒体编码格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    // 停止录音
    void stopRecord() {

        recorder.stop();
        recorder.release();
        recorder = null;
        // 生成倒放音频文件
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        // 为了区分源文件，添加_reverse后缀
        newFileName = dateFormat.format(currentDate) + "_reverse.mp3";
        reverseAudio(fileName, newFileName);
    }

    // 开始播放音频
    void startPlay() {

        player = new MediaPlayer();
        try {
            player.setDataSource(path + newFileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 停止播放
    void stopPlay() {
        player.stop();
        player.release();
        player = null;
    }

    // 释放资源，可在onStop()中调用
    void closeAll() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }

    // 音频反转
    private void reverseAudio(String fileName, String newFileName) {
        // 这里拼接要注意空格
        String text = "ffmpeg -i " + path + fileName + " -vf reverse -af areverse -preset superfast " + path + newFileName;
        String[] commands = text.split(" ");

        RxFFmpegInvoke.getInstance().runCommandRxJava(commands).subscribe(new RxFFmpegSubscriber() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onProgress(int progress, long progressTime) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {

            }
        });
    }
}