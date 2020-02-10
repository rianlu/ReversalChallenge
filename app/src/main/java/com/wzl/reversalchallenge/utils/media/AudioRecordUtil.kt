package com.wzl.reversalchallenge.utils.media

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.wzl.reversalchallenge.utils.common.FileUtil
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by 24694
 * on 2020/2/8 13:33
 */

/**
 * 参考该博客 https://www.jianshu.com/p/90c4071c7768
 */
class AudioRecordUtil(context: Context) : IRecorder {

    // 音频源：音频输入-麦克风
    private val AUDIO_INPUT = MediaRecorder.AudioSource.MIC
    // 采样率
    // 44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    // 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private val AUDIO_SAMPLE_RATE = 16000
    // 音频通道 单声道
    private val AUDIO_CHANNEL: Int = AudioFormat.CHANNEL_IN_MONO
    // 音频格式：PCM编码
    private val AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT
    // 缓冲区大小：缓冲区字节大小
    private var bufferSizeInBytes = 0
    // 录音对象
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private val path: String = context.externalCacheDir?.absolutePath + "/"
    private lateinit var pcmPath: String
    private lateinit var mp3Path: String
    private lateinit var currentDate: String
    private lateinit var originPath: String
    private lateinit var reversePath: String

    init {
        // 获得缓冲区字节大小
        bufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE,
                AUDIO_CHANNEL, AUDIO_ENCODING)
        audioRecord = AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, bufferSizeInBytes)
    }


    override fun startRecord() {
        val cacheFolder = File(path)
        if (!cacheFolder.exists()) {
            cacheFolder.mkdir()
        }
        audioRecord?.startRecording()
        isRecording = true
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        val date: Date = Calendar.getInstance().time
        currentDate = dateFormat.format(date)
        pcmPath = "$path$currentDate.pcm"
        Thread(Runnable { writeAudioData(pcmPath) }).start()
    }

    override fun stopRecord() {
        isRecording = false
        audioRecord?.stop()
        mp3Path = "$path$currentDate.mp3"
        pcmToMp3()
    }

    override fun release() {
        audioRecord?.release()
    }

    override fun getOriginPath() : String {
        return originPath
    }

    override fun getReversePath() : String {
        return reversePath
    }

    private fun writeAudioData(fileName: String) {
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
        val audiodata = ByteArray(bufferSizeInBytes)

        var fos: FileOutputStream? = null
        var readsize = 0
        val file = File(fileName)
        try {
            fos = FileOutputStream(file) // 建立一个可存取字节的文件
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        while (isRecording) {
            readsize = audioRecord!!.read(audiodata, 0, bufferSizeInBytes)
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                try {
                    fos?.write(audiodata)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        try {
            fos?.close() // 关闭写入流
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun pcmToMp3() {
        val text = "ffmpeg -y -f s16be -ac 1 -ar 16000 -acodec pcm_s16le -i $pcmPath $mp3Path"
        val commands: Array<String> = text.split(" ").toTypedArray()

        RxFFmpegInvoke.getInstance().runCommandRxJava(commands).subscribe(object : RxFFmpegSubscriber() {
            override fun onFinish() {
                // 删除pcm原文件
                FileUtil.deleteFile(pcmPath)
                originPath = mp3Path
                reversePath = "$path${currentDate}_reverse.mp3"
                MediaUtil.reverseAudio(originPath, reversePath)
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