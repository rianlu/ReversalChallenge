package com.wzl.reversalchallenge.utils.media

import android.content.Context
import android.net.Uri

/**
 * Created by 24694
 * on 2020/2/8 14:34
 */
interface IRecorder {

    fun startRecord()

    fun stopRecord()

    fun startCopy(context: Context, uri: Uri)

    fun release()

    fun getOriginPath() : String

    fun getReversePath() : String
}