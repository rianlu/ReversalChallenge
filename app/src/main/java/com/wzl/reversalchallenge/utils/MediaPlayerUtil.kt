package com.wzl.reversalchallenge.utils

import android.media.MediaPlayer
import java.io.IOException

/**
 * Created by 24694
 * on 2020/2/8 14:46
 */
class MediaPlayerUtil() {

    private var player = MediaPlayer()

    fun checkPlaying() : Boolean {
        return player.isPlaying
    }

    fun playOrigin(normalPath: String) {
        play(normalPath)
    }

    fun playReverse(reversePath: String) {
        play(reversePath)
    }

    private fun play(dataSource: String) {
        try {
            if (!checkPlaying()) {
                player = MediaPlayer()
                player.setDataSource(dataSource)
                player.prepare()
                player.start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 停止播放
    fun pausePlay() {
        player.pause()
    }

    fun release() {
        player.release()
    }
}