package com.wzl.reversalchallenge.utils.media

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.widget.Toast
import com.example.try_reversal.MainActivity

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MediaRecorderUtil(context: Context) : IRecorder {

    private var recorder: MediaRecorder? = null
    private val path: String = context.externalCacheDir?.absolutePath + "/"
    private lateinit var currentDate: String
    private lateinit var originPath: String
    private lateinit var reversePath: String

    // 开始录音
    override fun startRecord() {
        val cacheFolder = File(path)
        if (!cacheFolder.exists()) {
            cacheFolder.mkdir()
        }
        // 以时间作为录音文件名
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        val date: Date = Calendar.getInstance().time
        currentDate = dateFormat.format(date)
        originPath = "$path$currentDate.mp3"
        recorder = MediaRecorder()
        // 设置音频源为麦克风
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        // 设置媒体输出格式
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB)
        // 设置媒体输出路径
        recorder!!.setOutputFile(originPath)
        // 设置媒体编码格式
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        recorder!!.start()
    }

    // 停止录音
    override fun stopRecord() {

        recorder!!.stop()
        recorder!!.release()
        recorder = null
        // 生成倒放音频文件
        // 为了区分源文件，添加_reverse后缀
        reversePath = "$path${currentDate}_reverse.mp3"
        MediaUtil.reverseAudio(originPath, reversePath)
    }

    override fun startCopy(context: Context, uri: Uri) {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        val date: Date = Calendar.getInstance().time
        currentDate = dateFormat.format(date)
        originPath = "$path$currentDate.mp3"

        try {
            inputStream?.let { input ->
                val outputFile = File(originPath)
                FileOutputStream(outputFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                    outputStream.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        reversePath = "$path${currentDate}_reverse.mp3"
        MediaUtil.reverseAudio(originPath, reversePath)
    }

    // 释放资源，可在onStop()中调用
    override fun release() {
        if (recorder != null) {
            recorder!!.release()
            recorder = null
        }
    }

    override fun getOriginPath() : String {
        return originPath
    }

    override fun getReversePath() : String {
        return reversePath
    }
}