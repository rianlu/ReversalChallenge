package com.wzl.reversalchallenge

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber

public class MediaUtil(mContext: Context) {

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val path: String = mContext.externalCacheDir?.absolutePath + "/"
    private var fileName: String? = null
    private var newFileName: String? = null
    private lateinit var currentDate: Date
    private var dateFormat: SimpleDateFormat? = null

    // 开始录音
    fun startRecord() {

        // 以时间作为录音文件名
        dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        currentDate = Calendar.getInstance().time
        fileName = dateFormat!!.format(currentDate) + ".mp3"
        recorder = MediaRecorder()
        // 设置音频源为麦克风
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        // 设置媒体输出格式
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        // 设置媒体输出路径
        recorder!!.setOutputFile(path + fileName)
        // 设置媒体编码格式
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        recorder!!.start()
    }

    // 停止录音
    fun stopRecord() {

        recorder!!.stop()
        recorder!!.release()
        recorder = null
        // 生成倒放音频文件
        // 为了区分源文件，添加_reverse后缀
        newFileName = dateFormat!!.format(currentDate) + "_reverse.mp3"
        reverseAudio(fileName, newFileName)
    }

    fun isPlaying() {
        recorder
    }

    // 开始播放音频
    fun startPlay() {

        player = MediaPlayer()
        try {
            player!!.setDataSource(path + newFileName)
            player!!.prepare()
            player!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 停止播放
    fun stopPlay() {
        player?.stop()
        player?.release()
        player = null
    }

    // 释放资源，可在onStop()中调用
    fun closeAll() {
        if (recorder != null) {
            recorder!!.release()
            recorder = null
        }
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    // 音频反转
    private fun reverseAudio(fileName: String?, newFileName: String?) {
        // 这里拼接要注意空格
        val text = "ffmpeg -i $path$fileName -vf reverse -af areverse -preset superfast $path$newFileName"
        val commands: Array<String> = text.split(" ").toTypedArray()

        RxFFmpegInvoke.getInstance().runCommandRxJava(commands).subscribe(object : RxFFmpegSubscriber() {
            override fun onFinish() {

            }

            override fun onProgress(progress: Int, progressTime: Long) {

            }

            override fun onCancel() {

            }

            override fun onError(message: String) {

            }
        })
    }
}