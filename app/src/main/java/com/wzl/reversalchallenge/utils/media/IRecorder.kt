package com.wzl.reversalchallenge.utils.media

/**
 * Created by 24694
 * on 2020/2/8 14:34
 */
interface IRecorder {

    fun startRecord()

    fun stopRecord()

    fun release()

    fun getOriginPath() : String

    fun getReversePath() : String
}