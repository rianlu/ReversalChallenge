package com.wzl.reversalchallenge.utils.media

import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
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

    fun playOrigin(normalPath: String): Int {
        if(play(normalPath)==1)
            return 1
        return 0
    }

    fun playReverse(reversePath: String):Int {
        if(play(reversePath)==1)
            return 1
        return 0
    }

    private fun play(dataSource: String): Int {
        try {
            if (!checkPlaying()) {
                player = MediaPlayer()
                player.setDataSource(dataSource)
                player.prepare()
                player.start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return 1;
        }catch (e: IllegalStateException) {
            Log.e("AudioPlay", "IllegalStateException: MediaPlayer is in an invalid state", e)
            return 1;
        }
        return 0;
    }

    // 停止播放
    fun stopPlay() {
        player.stop()
    }

    fun release() {
        player.release()
    }
}