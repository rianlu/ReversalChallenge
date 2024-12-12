package com.wzl.reversalchallenge.utils.common

import android.content.Context
import android.os.Environment

public class CacheUtil {

    companion object {

        // 获取外部缓存大小
        public fun getExternalCacheSize(context: Context?): String {

            return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                FileUtil.formatSize(FileUtil.getFolderSize(context!!.externalCacheDir))
            } else {
                "0 B"
            }
        }

        // 获取全部缓存大小
        public fun getCacheSize(context: Context?): String {

            var totalSize: Long = FileUtil.getFolderSize(context!!.cacheDir)
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                totalSize += FileUtil.getFolderSize(context.externalCacheDir)
            }
            return FileUtil.formatSize(totalSize)
        }

        // 清除外部缓存
        public fun clearExternalCache(context: Context?) {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                FileUtil.deleteFolder(context!!.externalCacheDir)
            }
        }

        // 清除全部缓存
        public fun clearAllCache(context: Context?) {
            FileUtil.deleteFolder(context!!.cacheDir)
            clearExternalCache(context)
        }


    }
}