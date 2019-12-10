package com.wzl.reversalchallenge.utils

import android.content.Context
import android.os.Environment
import java.io.File

public class CacheUtil {

    companion object {

        public fun getExternalCacheSize(context: Context?): String {

            return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                formatSize(getFolderSize(context!!.externalCacheDir))
            } else {
                "0 B"
            }
        }

        private fun getFolderSize(folder: File?): Long {

            var size: Long = 0
            val files: Array<File>? = folder!!.listFiles()
            for (file in files!!) {
                size += if (file.isDirectory) {
                    getFolderSize(file)
                } else {
                    file.length()
                }
            }
            return size
        }

        public fun clearExternalCache(context: Context?) {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                deleteFolder(context!!.externalCacheDir)
            }
        }

        private fun deleteFolder(folder: File?) : Boolean {

            if (folder != null && folder.isDirectory) {
                val files: Array<String>? = folder.list()
                for (file in files!!) {
                    if (!deleteFolder(File(folder, file))) {
                        return false
                    }
                }
                return folder.delete()
            }
            if (folder != null) {
                return folder.delete()
            } else {
                return false
            }
        }

        // 格式化缓存大小单位
        private fun formatSize(size: Long): String {

            val b = size
            val kb = b / 1024
            if (kb < 1) {
                return "$b B"
            }
            val mb = kb / 1024
            if (mb < 1) {
                return "$kb KB"
            }
            val gb = mb / 1024
            if (gb < 1) {
                return "$mb MB"
            }
            val tb = gb / 1024
            if (tb < 1) {
                return "$gb GB"
            } else {
                return "0"
            }
        }
    }
}