package com.wzl.reversalchallenge.utils.media

import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber

/**
 * Created by 24694
 * on 2020/2/8 16:03
 */
class MediaUtil {

    companion object {
        // 音频反转
        fun reverseAudio(originPath: String?, reversePath: String?) {
            // 这里拼接要注意空格
            val text = "ffmpeg -i $originPath -vf reverse -af areverse -preset superfast $reversePath"
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
}